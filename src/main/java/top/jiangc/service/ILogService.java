package top.jiangc.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import top.jiangc.entity.Page;
import top.jiangc.entity.PageRequest;
import top.jiangc.entity.Project;
import top.jiangc.entity.User;
import top.jiangc.entity.UserLog;

public interface ILogService {

	/**
	 * 添加用户操作日志
	 *
	 * @author jiangc
	 * @param userLog
	 */
	public Integer addUserLog(UserLog userLog);

	/**
	 * 获取用户的日志信息
	 *
	 * jiangc
	 * @param operationTime
	 * @return
	 */
	public List<Map<String, Object>> findUserLog(Date operationTime);

	/**
	 * 获取用户的日志信息（分页显示）
	 *
	 * jiangc
	 * @param pageRequest
	 * @return
	 */
	public Page<Map<String, Object>> getUserLog(PageRequest pageRequest);

	/**
	 * 统计用户日志
	 *
	 * jiangc
	 * @param operation
	 * @return
	 */
	public List<Map<String, Object>> reportUserLog(String operation);

	/**
	 * 获取用户日志操作分类
	 *
	 * jiangc
	 * @return
	 */
	public List<Map<String, Object>> getUserLogCategory();

	/**
	 * 获取监测项目名
	 *
	 * jiangc
	 * @return
	 */
	public List<Map<String, Object>> getProjects();

	/**
	 * 判断添加的项目是否存在 
	 *
	 * jiangc
	 * @param projectName
	 * @param stationIp
	 * @return true 存在
	 */
	public boolean isExistProject(String projectName, String stationIp);

	/**
	 * 添加项目
	 *
	 * jiangc
	 * @param projectName
	 * @param stationIp
	 * @param userId
	 * @return
	 */
	public Project addProject(String projectName, String stationIp, User user);

	/**
	 * 分页查询监测项目
	 *
	 * jiangc
	 * @param pageRequest
	 * @param downloadAddr 
	 * @return
	 */
	public Page<Map<String, Object>> getProjectPage(PageRequest pageRequest, String contextPath);

	/**
	 * 添加浏览页面记录
	 *
	 * jiangc
	 * @param params
	 * @return
	 */
	public int saveBrowseLog(Map<String, Object> params);

	/**
	 * 修改页面浏览结束的时间
	 *
	 * jiangc
	 * @param params
	 * @return
	 */
	public int updateBrowseLog(Map<String, Object> params);

	/**
	 * 添加页面操作日志
	 *
	 * jiangc
	 * @param params
	 * @return
	 */
	public int saveHandleLog(Map<String, Object> params);

	/**
	 * 分页查询用户浏览日志信息
	 *
	 * jiangc
	 * @param pageReq
	 * @return
	 */
	public Page<Map<String, Object>> getBrowseLog(PageRequest pageReq);

	/**
	 * 分页查询用户操作日志信息
	 *
	 * jiangc
	 * @param pageReq
	 * @return
	 */
	public Page<Map<String, Object>> getHandleLog(PageRequest pageReq);

	/**
	 * 按操作按钮统计用户操作数据
	 *
	 * jiangc
	 * @param projectId
	 * @return
	 */
	public List<Map<String, Object>> reportBrowse(String projectId);

	/**
	 * 按用户浏览的页面统计数据
	 *
	 * jiangc
	 * @param projectId
	 * @return
	 */
	public List<Map<String, Object>> reportHandle(String projectId);

}
