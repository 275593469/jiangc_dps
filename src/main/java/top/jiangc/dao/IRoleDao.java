package top.jiangc.dao;

import java.util.List;
import java.util.Map;

import top.jiangc.entity.Role;

public interface IRoleDao {

	/**
	 * 添加角色
	 *
	 * jiangc
	 * @param role
	 * @return
	 */
	public int addRole(Role role);

	/**
	 * 根据角色名查询角色
	 *
	 * jiangc
	 * @param roleName
	 * @return
	 */
	public List<Map<String, Object>> findRoleByName(String roleName);

	/**
	 * 获取角色列表
	 *
	 * jiangc
	 * @return
	 */
	public List<Map<String, Object>> getRoles();

	/**
	 * 获取资源列表
	 *
	 * jiangc
	 * @return
	 */
	public List<Map<String, Object>> getResources();

	public int updateRole(Role role);

	public List<Map<String, Object>> getRoleById(String roleId);

}
