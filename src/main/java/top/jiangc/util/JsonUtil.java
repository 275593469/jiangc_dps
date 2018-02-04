package top.jiangc.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONML;
import org.json.JSONObject;

import top.jiangc.entity.Page;

public class JsonUtil {

	public static String returnSuccess(String message, Object obj){
		
		Map<String, Object> map = new HashMap<>();
		map.put("result", obj);
		map.put("code", "success");
		map.put("message", message);
		return JSONObject.valueToString(map);
		
	}
	
	public static String returnFail(String message, Object obj){
		Map<String, Object> map = new HashMap<>();
		map.put("result", obj);
		map.put("code", "fail");
		map.put("message", message);
		return JSONObject.valueToString(map);
	}
	
	public static String getSNID(){
		String _sResult=null;
		Random _random = new Random();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmsssss");//设置日期格式
		_sResult=df.format(new Date())+_random.nextInt(999);
		return _sResult;
		
	}
	
	/**
	 * 返回datatable的数据格式
	 *
	 * jiangc
	 * @param <T>
	 * draw int 页面请求的数据，表示请求的次数
	 * @return
	 */
	public static <T> String returnDataTable(Page<T> page, int draw){
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("draw", draw);
		resultMap.put("recordsTotal", page.getPageCount());//总记录数
		resultMap.put("recordsFiltered", page.getPageCount());//过滤后的数据量
		resultMap.put("data", page.getLstEntity());//数据
		
		return JSONObject.valueToString(resultMap);
	}
	
	/**
	 * 将对象转换成json格式
	 *
	 * jiangc
	 * @param obj
	 * @return
	 */
	public static String toString(Object obj){
		return JSONObject.valueToString(obj);
	}
	
	/**
	 * json转对象
	 *
	 * jiangc
	 * @param sJson
	 * @return
	 */
	public static Object parseObject(String sJson){
		return JSONObject.stringToValue(sJson);
	}
	
	/**
	 * 将json字符串转换成Map
	 *
	 * jiangc
	 * @param sJson
	 * @return
	 */
	public static Map<String, Object> parseMap(String sJson){
		JSONObject jsonObject = new JSONObject(sJson);
        Map<String, Object> result = new HashMap<>();
        Iterator iterator = jsonObject.keys();
        String key = null;
        Object value = null;
        
        while (iterator.hasNext()) {

            key = (String) iterator.next();
            value = jsonObject.get(key);
            result.put(key, value);

        }
        return result;
	}
}
