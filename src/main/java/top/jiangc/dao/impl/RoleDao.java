package top.jiangc.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import top.jiangc.dao.IRoleDao;
import top.jiangc.dao.IdataTemplate;
import top.jiangc.entity.Role;

@Repository("roleDao")
public class RoleDao implements IRoleDao {
	
	@Resource(name = "dataTemplate")
	private IdataTemplate dataTemplate;

	@Override
	public int addRole(Role role) {
		String sql = "INSERT INTO role(role_name,permission) VALUES(?,?);";
		Object[] objs = {role.getRoleName(),role.getPermission()};
		
		return dataTemplate.deleteOrUpDate(sql, objs);
	}

	@Override
	public List<Map<String, Object>> findRoleByName(String roleName) {
		String sql = "select role_id,role_name,permission from role where role_name = ?;";
		return dataTemplate.queryListEntity(sql, new Object[]{roleName});
	}

	@Override
	public List<Map<String, Object>> getRoles() {
		String sql = "select * from role;";
		return dataTemplate.queryListEntity(sql);
	}

	@Override
	public List<Map<String, Object>> getResources() {
		String sql = "select * from resource;";
		return dataTemplate.queryListEntity(sql);
	}

	@Override
	public int updateRole(Role role) {
		String sql = "update role set role_name = ?,permission = ? where role_id = ?;";
		return dataTemplate.deleteOrUpDate(sql, new Object[]{role.getRoleName(),role.getPermission(),role.getRoleId()});
	}

	@Override
	public List<Map<String, Object>> getRoleById(String roleId) {
		String sql = "select role_id,role_name,permission from role where role_id = ?;";
		return dataTemplate.queryListEntity(sql, new Object[]{roleId});
	}

}
