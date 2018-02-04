package top.jiangc.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import top.jiangc.entity.Page;
import top.jiangc.entity.PageRequest;
import top.jiangc.entity.User;
import top.jiangc.service.IUserService;
import top.jiangc.util.EncryptUtil;
import top.jiangc.util.JsonUtil;
import top.jiangc.util.LogUtil;
import top.jiangc.util.UUIDUtil;

import com.alibaba.druid.util.StringUtils;

/**
 * 用户控制层
 *@author jiangcheng
 *
 *2017年3月8日
 */
@RestController("userController")
@RequestMapping("/user")
public class UserController {
	Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Resource(name = "userService")
	private IUserService userService;
	
	/**
	 * 用户注册
	 * @author: jiangcheng
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/register",method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String registerUser(@ModelAttribute User user, final HttpServletRequest request){
		Map<String, Object> mapResult = new HashMap<>();
		String userName = "";
		String nickName = "";
		String email = "";
		String password = "";
		if(null != user){
			userName = user.getUserName();
			nickName = user.getNickName();
			email = userName;
			password = user.getPassword();
		}
		if(StringUtils.isEmpty(userName) || StringUtils.isEmpty(nickName) || StringUtils.isEmpty(password)){
			return JsonUtil.returnFail("参数不完整", null);
		}
		if(userName.length() > 50){
			return JsonUtil.returnFail("用户名过长", null);
		}
		if(nickName.length() > 100){
			return JsonUtil.returnFail("昵称过长", null);
		}
		if(null != userService.findUserByName(userName)){
			return JsonUtil.returnFail("用户名已存在", null);
		}
		
		String code = JsonUtil.getSNID();
		String userId = UUIDUtil.getUUID();
		user.setUserId(userId);
		//用户注册
		Integer result = userService.addUser(user, code);//code激活码
		if(result == 1){//注册成功，发送邮件
			long timestamp = new Date().getTime();
			String activeAddr = request.getScheme()+"://"+request.getServerName()+":"+request.getLocalPort()+request.getContextPath();
			String path = activeAddr+"/user/activeAccount?userId="+userId+"&code="+code+"&timestamp="+timestamp;
			userService.sendActiveMail(user, path, email);
			String toEmail = "http://mail."+email.substring(email.indexOf("@")+1);
			mapResult.put("userName", user.getUserName());
			mapResult.put("toEmail", toEmail);
			return JsonUtil.returnSuccess("注册成功", mapResult);
		}else{
			return JsonUtil.returnFail("注册失败", null);
		}
	}

	/**
	 * 账号激活
	 *
	 * jiangc
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/activeAccount",method=RequestMethod.GET)
	public ModelAndView activeAccount(@RequestParam(value="userId",required=true) String userId,
			@RequestParam(value="code",required=true) String activeCode, 
			@RequestParam(value="timestamp",required=true) Long activeTimestamp,
			Model model, final HttpServletRequest request){
		Map<String, Object> msgMap = new HashMap<>();
		Long timestamp = new Date().getTime();//当前时间戳
		if((timestamp - activeTimestamp) > 60*60*1000){
			//激活链接失效，请重新激活
			msgMap.put("message", "激活链接已过期，请重新进行激活操作");
			msgMap.put("url", "/user/activeAccount?userId="+userId+"&code="+activeCode+"&timestamp="+new Date().getTime());
			msgMap.put("text", "");
			model.addAttribute("msg", msgMap);
			return new ModelAndView("message/msg");
		}
		
		if(userService.activeAccount(userId, activeCode)){
			//激活成功
			msgMap.put("message", "恭喜您激活成功！现在可以登录了。");
			msgMap.put("url", request.getContextPath()+"/login");
			msgMap.put("text", "登录");
			model.addAttribute("msg", msgMap);
			return new ModelAndView("message/msg");
		}else{
			//激活失败,发送邮件重新激活
			msgMap.put("message", "激活失败！请确保激活链接的准确性");
			msgMap.put("url", "##");
			msgMap.put("text", "");
			model.addAttribute("msg", msgMap);
			return new ModelAndView("message/msg");
		}
	}

	/**
	 * 用户登录
	 * @author: jiangcheng
	 *
	 * @param user
	 * @param request
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@ModelAttribute User user, final HttpServletRequest request, final HttpServletResponse response) throws IOException{
		
		HttpSession session = request.getSession();
		Map<String, Object> msgMap = new HashMap<>();
		String userName = "";
		String password = "";
		if(null != user){
			userName = user.getUserName();
			password = user.getPassword();
		}
		User resultUser = userService.findUserByName(userName);
		if(null == resultUser){
			msgMap.put("user", user);
			msgMap.put("code", 2);//code:2表示用户名不存在
			return JsonUtil.returnFail("用户名不存在", msgMap);
		}
		if(resultUser.getStatus() == 0){//未激活状态
			msgMap.put("user", resultUser);
			msgMap.put("code", 0);//code:0表示未激活
			return JsonUtil.returnFail("账号未激活", msgMap);
		}
		//校验密码
		if(EncryptUtil.checkpassword(password, resultUser.getPassword())){
			//校验成功
			msgMap.put("user", resultUser);
			msgMap.put("code", 1);//code：1表示登录成功
			//将用户信息存入session
			session.setAttribute("user", resultUser);
			Cookie cookie = new Cookie("userId", resultUser.getUserId());
			cookie.setPath("/");
			cookie.setMaxAge(-1);
			response.addCookie(cookie);//保存cookie
			//记录日志
			LogUtil.userLog(request, "用户登录", 1);
			//TODO 可以使用cookie实现几天之内免登录的功能
			if(null != request.getAttribute("requestUrl")){
				try {
					request.getRequestDispatcher((String) request.getAttribute("requestUrl")).forward(request, response);
				} catch (Exception e) {
					logger.error(e.getMessage());
				} 
			}
			return JsonUtil.returnSuccess("登录成功", msgMap);
		}else{
			msgMap.put("user", user);
			msgMap.put("code", 3);//code:3密码错误
			return JsonUtil.returnFail("密码错误", msgMap);
		}
		
	}
	
	/**
	 * 发送激活邮件
	 *
	 * jiangc
	 * @param userName
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/sendActiveMail",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String sendActiveMail(@RequestParam(value="userName",required=true) String userName, final HttpServletRequest request){
		Map<String, Object> resultMap = new HashMap<>();
		if(StringUtils.isEmpty(userName)){
			return JsonUtil.returnFail("用户名不能为空", null);
		}
		
		User user = userService.findUserByName(userName);
		if(null != user){
			if(!StringUtils.isEmpty(user.getEmail())){
				String activeAddr = request.getScheme()+"://"+request.getServerName()+":"+request.getLocalPort()+request.getContextPath();
				String path = activeAddr+"/user/activeAccount?userId="+user.getUserId()+"&code="+user.getCode()+"&timestamp="+new Date().getTime();
				userService.sendActiveMail(user, path, user.getEmail());
				resultMap.put("user", user);
				String toEmail = "http://mail."+user.getEmail().substring(user.getEmail().indexOf("@")+1);
				resultMap.put("toEmail", toEmail);
				return JsonUtil.returnSuccess("邮件发送成功", resultMap);
			}
		}
		return JsonUtil.returnFail("用户名不存在", null);
	}
	
	/**
	 * 通过用户名发邮件重置密码
	 *
	 * jiangc
	 * @param email
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/resetPasswordByUserName",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String resetPasswordByEmail(@RequestParam(value="userName",required=true) String userName, final HttpServletRequest request){
		Map<String, Object> resultMap = new HashMap<>();
		if(StringUtils.isEmpty(userName)){
			return JsonUtil.returnFail("用户名不能为空", null);
		}
		
		User user = userService.findUserByName(userName);
		if(null != user){
			if(!StringUtils.isEmpty(user.getEmail())){
				String activeAddr = request.getScheme()+"://"+request.getServerName()+":"+request.getLocalPort()+request.getContextPath();
				String path = activeAddr+"/user/resetPassword?userId="+user.getUserId()+"&code="+user.getCode()+"&timestamp="+new Date().getTime();
				userService.sendResetMail(user, path, user.getEmail());
				String toEmail = "http://mail."+user.getEmail().substring(user.getEmail().indexOf("@")+1);
				resultMap.put("toEmail", toEmail);
				resultMap.put("user", user);
				return JsonUtil.returnSuccess("邮件发送成功", resultMap);
			}
		}
		return JsonUtil.returnFail("用户名不存在", null);
	}
	
	/**
	 * 重置密码
	 *
	 * jiangc
	 * @param userId 用户名
	 * @param request
	 * @return
	 */
	@RequestMapping("/resetPassword")
	public ModelAndView resetPassword(@RequestParam(value="userId",required=true) String userId,
			@RequestParam(value="code",required=true) String activeCode, 
			@RequestParam(value="timestamp",required=true) Long activeTimestamp,
			Model model,
			final HttpServletRequest request){
		Map<String, Object> msgMap = new HashMap<>();
		Long timestamp = new Date().getTime();//当前时间戳
		if((timestamp - activeTimestamp) > 60*60*1000){
			//激活链接失效，请重新激活
			msgMap.put("message", "链接已过期，请重新进行重置密码操作");
			msgMap.put("url", "/user/resetPassword?userId="+userId+"&code="+activeCode+"&timestamp="+new Date().getTime());
			msgMap.put("text", "");
			model.addAttribute("msg", msgMap);
			return new ModelAndView("message/msg");
		}
		
		if(userService.resetPassword(userId, activeCode)){
			//激活成功
			msgMap.put("message", "密码重置成功！现在可以重新登录了。");
			msgMap.put("url", request.getContextPath()+"/login");
			msgMap.put("text", "登录");
			model.addAttribute("msg", msgMap);
			return new ModelAndView("message/msg");
		}else{
			//激活失败,发送邮件重新激活
			msgMap.put("message", "密码重置失败！请确保激活链接的准确性");
			msgMap.put("url", "##");
			msgMap.put("text", "");
			model.addAttribute("msg", msgMap);
			return new ModelAndView("message/msg");
		}
		
	}
	
	/**
	 * 获取登录用户的基本信息和角色权限信息
	 * @author: jiangcheng
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping("/session")
	public String getSession(final HttpServletRequest request){
		HttpSession session = request.getSession();
		List<Map<String, Object>> lstResult = new ArrayList<>();
		User user = (User) session.getAttribute("user");
		if(null != user){
			lstResult = userService.getUserPermission(user.getUserId());
			return JsonUtil.returnSuccess("查询成功", lstResult);
		}else{
			return JsonUtil.returnFail("查询失败", null);
		}
	}
	
	/**
	 * 账号登出
	 * @author: jiangcheng
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout(final HttpServletRequest request, final HttpServletResponse response){
		HttpSession session = request.getSession();
		//日志记录
		LogUtil.userLog(request, "退出登录", 1);
		
		session.removeAttribute("user");
		
		return JsonUtil.returnSuccess("操作成功", null);
	}
	
	/**
	 * 修改密码(用户登录后修改)
	 * @author: jiangcheng
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping("/updatePassword")
	public String updatePassword(final HttpServletRequest request, 
			@RequestParam(value="oldPassword",required=true) String oldPassword, 
			@RequestParam(value="newPassword",required=true) String newPassword){
		
		User user = (User) request.getSession().getAttribute("user");
		if(StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(newPassword)){
			return JsonUtil.returnFail("参数不完整", null);
		}
		//判断原密码是否正确
		if(user.getPassword().equals(EncryptUtil.MD5(oldPassword))){
			return userService.updatePassWord(user.getUserId(), newPassword)? 
					JsonUtil.returnSuccess("修改成功", null): JsonUtil.returnFail("修改失败", null);
		}else{
			return JsonUtil.returnFail("原密码错误", null);
		}
		
	}
	
	/**
	 * 分页获取用户信息
	 * @author: jiangcheng
	 *
	 * @param request
	 * @param start
	 * @param length
	 * @param search
	 * @param draw
	 * @return
	 */
	@RequestMapping("/getUserPage")
	public String getUserPage(final HttpServletRequest request,
			@RequestParam(value="start",required=true) Integer start,
			@RequestParam(value="length",required=true) Integer length,
			@RequestParam(value="search[value]",required=false) String search,
			@RequestParam(value="draw",required=true) Integer draw){
		PageRequest pageRequest = new PageRequest(start, length, search);
		Page<Map<String, Object>> page = userService.getUsers(pageRequest);
		
		return JsonUtil.returnDataTable(page, draw);
	}
	
	/**
	 * 根据用户id获取用户信息
	 *
	 * jiangc
	 * @param request
	 * @param userId
	 * @return
	 */
	@RequestMapping("/getUserById")
	public String getUserById(final HttpServletRequest request,
			@RequestParam(value="userId",required=true) String userId){
		List<Map<String, Object>> lstResult = new ArrayList<>();
		lstResult = userService.getUserById(userId);
		
		if(lstResult.size() > 0){
			return JsonUtil.returnSuccess("查询成功", lstResult);
		}else{
			return JsonUtil.returnFail("暂无数据", null);
		}
	}
	
	@RequestMapping("/updateUserRole")
	public String updateUserRole(final HttpServletRequest request,
			@RequestParam(value="userIds",required=true) String sUserIds,
			@RequestParam(value="roleId",required=true) String roleId){

		String[] userIds =sUserIds.split(",");
		Integer result = userService.updateUserRole(userIds,roleId);
		if(result > 0){
			return JsonUtil.returnSuccess("成功修改"+result+"条数据", null);
		}
		return JsonUtil.returnFail("修改失败", null);
	}
	
}
