package top.jiangc.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import top.jiangc.entity.Role;
import top.jiangc.service.IRoleService;
import top.jiangc.util.JsonUtil;

/**
 * 角色权限管理
 * @author jiangc
 *
 */
@RequestMapping("/role")
@RestController("roleController")
public class RoleController {
	private final Logger logger = LoggerFactory.getLogger(RoleController.class);

	@Resource(name="roleService")
	private IRoleService roleService;
	
	/**
	 * 添加角色
	 *
	 * jiangc
	 * @param request
	 * @return
	 */
	@RequestMapping("/addRole")
	public String addRole(final HttpServletRequest request,
			@RequestParam(value="roleName",required=true) String roleName,
			@RequestParam(value="permission",required=true) String permission){
		Role role = new Role();
		role.setRoleName(roleName);
		role.setPermission(permission);
		if(roleService.isExistRole(roleName)){
			return JsonUtil.returnFail("该角色名已存在", 2);
		}
		
		if(roleService.addRole(role)){
			return JsonUtil.returnSuccess("添加成功", role);
		}else{
			return JsonUtil.returnFail("添加失败", null);
		}
	}
	
	/**
	 * 查询出角色及其权限
	 *
	 * jiangc
	 * @param request
	 * @return
	 */
	@RequestMapping("/getRolesAndPermission")
	public String getRolesAndPermission(final HttpServletRequest request){
		List<Map<String, Object>> lstResult = new ArrayList<>();
		lstResult = roleService.getRolesAndPermission();
		if(lstResult.size() > 0){
			return JsonUtil.returnSuccess("查询成功", lstResult);
		}
		return JsonUtil.returnFail("暂无数据", null);
	}
	
	/**
	 * 修改角色名及其权限
	 * @author: jiangcheng
	 *
	 * @param request
	 * @param roleId
	 * @param roleName
	 * @param permission
	 * @return
	 */
	@RequestMapping("/updateRole")
	public String updateRole(final HttpServletRequest request,
			@RequestParam(value="roleId",required=true) Integer roleId,
			@RequestParam(value="roleName",required=true) String roleName,
			@RequestParam(value="permission",required=true) String permission){
		Role role = new Role();
		role.setRoleId(roleId);
		role.setRoleName(roleName);
		role.setPermission(permission);
		if(roleService.isExistRole(roleId, roleName)){
			return JsonUtil.returnFail("该角色名已存在", 2);
		}
		
		if(roleService.updateRole(role)){
			return JsonUtil.returnSuccess("修改成功", role);
		}else{
			return JsonUtil.returnFail("添加失败", null);
		}
	}
	
	/**
	 * 查询资源信息
	 *
	 * jiangc
	 * @param request
	 * @return
	 */
	@RequestMapping("getResources")
	public String getResources(final HttpServletRequest request){
		List<Map<String, Object>> lstResult = new ArrayList<>();
		lstResult = roleService.getResources();
		if(lstResult.size() > 0){
			return JsonUtil.returnSuccess("查询成功", lstResult);
		}
		return JsonUtil.returnFail("暂无数据", null);
	}
	
	/**
	 * 根据角色id获取角色信息
	 * @author: jiangcheng
	 *
	 * @param request
	 * @param roleId
	 * @return
	 */
	@RequestMapping("/getRoleById")
	public String getRoleById(final HttpServletRequest request,
			@RequestParam(value="roleId",required=true) String roleId){
		List<Map<String, Object>> lstResult = new ArrayList<>();
		lstResult = roleService.getRoleById(roleId);
		if(lstResult.size() > 0){
			return JsonUtil.returnSuccess("查询成功", lstResult);
		}
		return JsonUtil.returnFail("暂无数据", null);
	}
}
