package top.jiangc.dao;

import java.util.List;
import java.util.Map;

public interface IdataTemplate {

	public List<?> queryListEntity(String sql, Map<String, Object> params, Class<?> clazz);
	
	public List<Map<String, Object>> queryListEntity(String sql, Map<String, Object> params);

	public List<Map<String, Object>> queryListEntity(String sql, Object... objs);

	public Integer deleteOrUpDate(String sql, Map<String, Object> params);

	public Integer deleteOrUpDate(String sql, Object... objs);

	public Integer getCountBy(String sql, Map<String, Object> params);

	public Integer getCountBy(String sql, Object... objs);
}
