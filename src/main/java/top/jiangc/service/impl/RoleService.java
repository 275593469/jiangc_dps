package top.jiangc.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import top.jiangc.dao.IRoleDao;
import top.jiangc.entity.Role;
import top.jiangc.service.IRoleService;

/**
 * 角色权限管理service
 * @author jiangc
 *
 */
@Service("roleService")
public class RoleService implements IRoleService {

	@Resource(name="roleDao")
	private IRoleDao roleDao;
	
	@Override
	public boolean addRole(Role role) {
		int result = roleDao.addRole(role);
		if(result == 1){
			return true;
		}
		return false;
	}

	@Override
	public boolean isExistRole(String roleName) {
		List<Map<String, Object>> lstResult = new ArrayList<>();
		lstResult = roleDao.findRoleByName(roleName);
		if(lstResult.size() > 0){
			return true;
		}
		return false;
	}

	@Override
	public List<Map<String, Object>> getRolesAndPermission() {
		List<Map<String, Object>> lstRole = new ArrayList<>();
		List<Map<String, Object>> lstResource = new ArrayList<>();
		Map<String, Object> mapResource = new HashMap<>();
		lstRole = roleDao.getRoles();
		lstResource = roleDao.getResources();
		//遍历资源，并将Resource以id为key，name为value存入map中
		for(Map<String, Object> map : lstResource){
			mapResource.put(map.get("resource_id").toString(), map.get("resource_name"));
		}
			
		//遍历角色，将id换成name
		for(Map<String, Object> map : lstRole){
			String permission = (String) map.get("permission");
			if("all".equalsIgnoreCase(permission)){
				map.put("permission", "全部");
			}else{
				
				String[] perms = permission.split(":");
				StringBuffer sbPermission = new StringBuffer();
				for(int i=0; i<perms.length; i++){
					sbPermission.append(mapResource.get(perms[i])+",");
				}
				map.put("permission", sbPermission.substring(0, sbPermission.lastIndexOf(",")));
			}
			
		}
		return lstRole;
	}

	@Override
	public boolean updateRole(Role role) {
		int result = roleDao.updateRole(role);
		if(result == 1){
			return true;
		}
		return false;
	}

	@Override
	public List<Map<String, Object>> getResources() {
		return roleDao.getResources();
	}

	@Override
	public List<Map<String, Object>> getRoleById(String roleId) {
		List<Map<String, Object>> lstRole = new ArrayList<>();
		List<Map<String, Object>> lstResource = new ArrayList<>();
		Map<String, Object> mapResource = new HashMap<>();
		lstRole = roleDao.getRoleById(roleId);
		lstResource = roleDao.getResources();
		//遍历资源，并将Resource以id为key，name为value存入map中
		for(Map<String, Object> map : lstResource){
			mapResource.put(map.get("resource_id").toString(), map.get("resource_name"));
		}
			
		//遍历角色，将id换成name
		for(Map<String, Object> map : lstRole){
			String permission = (String) map.get("permission");
			if("all".equalsIgnoreCase(permission)){
				map.put("permission", "all");
			}else{
				
				String[] perms = permission.split(":");
				Map<String, Object> mapPermission = new HashMap<>();
				for(int i=0; i<perms.length; i++){
					mapPermission.put(perms[i],mapResource.get(perms[i]));
				}
				map.put("permission", mapPermission);
			}
			
		}
		return lstRole;
	}

	@Override
	public boolean isExistRole(Integer roleId, String roleName) {
		List<Map<String, Object>> lstResult = new ArrayList<>();
		lstResult = roleDao.findRoleByName(roleName);
		if(lstResult.size() > 0 && !lstResult.get(0).get("role_id").equals(roleId)){
			return true;
		}
		return false;
	}

}
