package top.jiangc.util;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import top.jiangc.entity.User;
import top.jiangc.entity.UserLog;
import top.jiangc.service.ILogService;

/**
 * 记录日志的类
 * @author jiangc
 *
 */
public class LogUtil {

	private static ILogService logService = (ILogService) BeanUtil.getBean("logService");
	
	/**
	 * 用户日志记录
	 *
	 * jiangc
	 * @param request
	 * @param operation 操作信息
	 * @param result 结果  0：失败；1：成功；2：正在进行中
	 */
	public static void userLog(HttpServletRequest request, String operation, Integer result){
		 
		User user = (User) request.getSession().getAttribute("user");
		UserLog userLog = new UserLog();
		userLog.setUserLogId(UUIDUtil.getUUID());
		userLog.setUser(user);
		userLog.setOperation(operation);
		userLog.setResult(result);
		System.out.println(getIpAddress(request));
		userLog.setIp(getIpAddress(request));
		userLog.setOperationTime(new Date());
		logService.addUserLog(userLog);
		
	}
	
    /** 
     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址, 
     * 参考文章： http://developer.51cto.com/art/201111/305181.htm 
     *  
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？ 
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。 
     *  
     * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130, 
     * 192.168.1.100 
     *  
     * 用户真实IP为： 192.168.1.110 
     *  
     * @param request 
     * @return 
     */  
    public static String getIpAddress(HttpServletRequest request) {  
        String ip = request.getHeader("x-forwarded-for");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        }  
        return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
    }
    
}
