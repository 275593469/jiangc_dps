package top.jiangc.dao.impl;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.beanutils.ConvertUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import top.jiangc.dao.IdataTemplate;

@Transactional
@Repository("dataTemplate")
public class DataTemplate implements IdataTemplate {
	
	@PersistenceContext
	private EntityManager em;

    /** 
     * * 查询数据集合 
     * @param sql 查询sql sql中的参数用:name格式  
     * @param params 查询参数map格式，key对应参数中的:name 
     * @param clazz 实体类型为空则直接转换为map格式 
     * @return 
     */
	@Override
	public List<?> queryListEntity(String sql, Map<String, Object> params,
			Class<?> clazz) {
		Session session = em.unwrap(Session.class);
		SQLQuery  query =  session.createSQLQuery(sql);
		if(null !=params){
			for(Entry<String, Object> set: params.entrySet()){
				query.setParameter(set.getKey(), set.getValue());
			}
		}
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map<String, Object>> resultList = query.list();
		if(null != clazz){
			return convert(clazz, resultList);
		}
		
		return resultList;
	}
	
	/** 
     * * 查询数据集合 
     * @param sql 查询sql sql中的参数用:name格式  
     * @param params 查询参数map格式，key对应参数中的:name 
     * @return 
     */
	@Override
	public List<Map<String, Object>> queryListEntity(String sql, Map<String, Object> params) {
		Session session = em.unwrap(Session.class);
		SQLQuery  query =  session.createSQLQuery(sql);
		if(null !=params){
			for(Entry<String, Object> set: params.entrySet()){
				query.setParameter(set.getKey(), set.getValue());
			}
		}
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map<String, Object>> resultList = query.list();
		return convert(resultList);
	}
	
	/** 
     * * 查询数据集合 
     * @param sql 查询sql sql中的参数用?格式  
     * @param objs 
     * @return 
     */
	@Override
	public List<Map<String, Object>> queryListEntity(String sql, Object... objs) {
		Session session = em.unwrap(Session.class);
		SQLQuery  query =  session.createSQLQuery(sql);
		if(objs.length > 0){
			for(int i=0; i<objs.length; i++){
				query.setParameter(i, objs[i]);//查询时setParameter 从“0”开始，update是setParameter从“1”开始，不知道为啥
			}
		}
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map<String, Object>> resultList = query.list();
		return convert(resultList);
	}  
	

	private List<?> convert(Class<?> clazz, List<Map<String, Object>> list) {
		List<Object> result = new ArrayList<Object>(); 
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		try {  
            PropertyDescriptor[] props = Introspector.getBeanInfo(clazz).getPropertyDescriptors();  
            for (Map<String, Object> map : list) {  
                Object obj = clazz.newInstance();  
                for (String key:map.keySet()) {  
                    String attrName = key.toLowerCase();  
                    for (PropertyDescriptor prop : props) {  
                        attrName = removeUnderLine(attrName);  
                        if (!attrName.equals(prop.getName())) {  
                            continue;  
                        }  
                        Method method = prop.getWriteMethod();  
                        Object value = map.get(key);  
                        if (value != null) {  
                            value = ConvertUtils.convert(value,prop.getPropertyType());  
                        }  
                        method.invoke(obj,value);  
                    }  
                }  
                result.add(obj);  
            }  
       } catch (Exception e) {  
           throw new RuntimeException("数据转换错误");  
       }  
       return result;  
	}
	
	private String removeUnderLine(String attrName) {  
        //去掉数据库字段的下划线  
         if(attrName.contains("_")) {  
            String[] names = attrName.split("_");  
            String firstPart = names[0];  
            String otherPart = "";  
            for (int i = 1; i < names.length; i++) {  
                String word = names[i].replaceFirst(names[i].substring(0, 1), names[i].substring(0, 1).toUpperCase());  
                otherPart += word;  
            }  
            attrName = firstPart + otherPart;  
         }  
        return attrName;  
    }  
	
	/** 
     * 获取记录条数 
     * @param sql 参数:name 格式
     * @param params 
     * @return 
     */  
	@Override
    public Integer getCountBy(String sql,Map<String, Object> params){  
        Query query =  em.createNativeQuery(sql);  
        if (params != null) {  
            for (String key : params.keySet()) {  
                query.setParameter(key, params.get(key));  
            }  
        }  
        BigInteger bigInteger  = (BigInteger) query.getSingleResult();  
        return bigInteger.intValue();  
    }  
	
	/** 
     * 获取记录条数 
     * @param sql 参数 ？ 格式
     * @param params 
     * @return 
     */  
	@Override
	public Integer getCountBy(String sql, Object... objs) {
		 Query query =  em.createNativeQuery(sql);  
	        if (objs.length > 0) {  
	            for (int i=0; i<objs.length; i++) {  
	                query.setParameter(i+1, objs[i]);  
	            }  
	        }  
	        BigInteger bigInteger  = (BigInteger) query.getSingleResult();  
	        return bigInteger.intValue();  
	}
      
    /** 
     * 新增或者删除 
     * @param sql 参数 :name 格式
     * @param params 
     * @return 
     */  
	@Override
    public Integer deleteOrUpDate(String sql,Map<String, Object> params){  
        Query query =  em.createNativeQuery(sql);  
        if (params != null) {  
            for (String key : params.keySet()) {
                query.setParameter(key, params.get(key));  
            }  
        }  
        return query.executeUpdate();  
    }

    /** 
     * 新增或者删除 
     * @param sql 参数 ？的 格式
     * @param params 
     * @return 
     */  
	@Override
	public Integer deleteOrUpDate(String sql, Object... objs) {
		System.out.println(objs.length);
		 Query query =  em.createNativeQuery(sql);  
	        if (objs.length > 0) {  
	            for (int i=0; i<objs.length; i++) {
	                query.setParameter(i+1, objs[i]);  
	            }  
	        }  
	        return query.executeUpdate();  
	}
	
	/**
	 * 将数据库中查询的结果的null替换空字符串
	 *
	 * jiangc
	 * @param list
	 * @return
	 */
	private List<Map<String, Object>> convert(List<Map<String, Object>> list){
		for(Map<String, Object> map: list){
			for(Entry<String, Object> set: map.entrySet()){
				if(null == set.getValue()) set.setValue("");
			}
		}
		return list;
	}
	

}
