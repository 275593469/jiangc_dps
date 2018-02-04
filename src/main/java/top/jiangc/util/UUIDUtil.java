package top.jiangc.util;

import java.util.UUID;

/**
 * <table width="100%" border="1px">
 * <tr>
 * <td width="20%">作者</td><td width="80%" colspan="2">sam</td>
 * </tr>
 * <tr>
 * <td width="20%">版本</td><td width="80%" colspan="2">v1.0</td>
 * </tr>
 * <tr>
 * <td width="20%">创建日期</td><td width="80%" colspan="2">2013-06-24</td>
 * </tr>
 * <tr>
 * <td width="100%" colspan="3">修订记录:</td>
 * <tr>
 * <td width="20%">修改日期</td><td width="20%">修改人</td><td width="60%">修改记录</td>
 * </tr>
 * <tr>
 * <td width="20%">-------</td><td width="20%">-------</td><td width="60%">--------------</td>
 * </tr>
 * <tr>
 * <td width="20%">描述信息</td><td width="80%" colspan="2">为数据库获取一个唯一的主键id的代码</td>
 * </tr>
 * </tr>
 * </table>
 */
public class UUIDUtil {

	/**
	 * 描述信息:获得一个UUID
	 * 
	 * @return String UUID
	 */
	public static String getUUID() {
		String s = UUID.randomUUID().toString();
		// 去掉“-”符号
		return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24);
	}

	/**
	 * 描述信息:获得指定数目的UUID
	 * 
	 * @param piNumber 需要获得的UUID数量
	 * @return String[] UUID数组
	 */
	public static String[] getUUID(int piNumber) {
		if (piNumber < 1) {
			return null;
		}
		String[] ss = new String[piNumber];
		for (int i = 0; i < piNumber; i++) {
			ss[i] = getUUID();
		}
		return ss;
	}

}
