package top.jiangc.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * md5加密
 * @author jiangcheng
 *
 * 2017年3月2日
 */
public class EncryptUtil {
	
	private static Logger logger = LoggerFactory.getLogger(EncryptUtil.class);

	/**
	 * MD5加密
	 * @author: jiangcheng
	 *
	 */
	public static String MD5(String str){
		return DigestUtils.md5Hex(str);
	}
	
	/**
	 * MD5校验
	 * @author: jiangcheng
	 *
	 * @param newpasswd
	 * @param oldpasswd
	 * @return
	 */
	public static boolean checkpassword(String newpasswd,String oldpasswd){
		if(MD5(newpasswd).equals(oldpasswd))
		return true;
		else
		return false;
	}
}
