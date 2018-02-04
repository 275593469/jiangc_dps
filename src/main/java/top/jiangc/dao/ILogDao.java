package top.jiangc.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import top.jiangc.entity.BrowseLog;
import top.jiangc.entity.HandleLog;
import top.jiangc.entity.Page;
import top.jiangc.entity.PageRequest;
import top.jiangc.entity.Project;
import top.jiangc.entity.UserLog;

public interface ILogDao {

	/**
	 * 添加用户操作信息
	 *
	 * jiangc
	 * @param userLog
	 * @return
	 */
	public Integer addUserLog(UserLog userLog);

	public List<Map<String, Object>> findUserLog(Date operationTime);

	public List<Map<String, Object>> getUserLog(Integer start,
			Integer length, String search);

	public List<Map<String, Object>> getUserLogCount(String searchValue);

	public List<Map<String, Object>> reportUserLog(String operation);

	public List<Map<String, Object>> getUserLogCategory();

	public List<Map<String, Object>> getProjects();

	/**
	 * 添加监测项目
	 *
	 * jiangc
	 * @param project
	 * @return
	 */
	public int addProject(Project project);

	/**
	 * 查询项目是否已存在
	 *
	 * jiangc
	 * @param projectName
	 * @param stationIp
	 * @return
	 */
	public List<Map<String, Object>> findProject(String projectName, String stationIp);

	/**
	 * 分页查询监测项目
	 *
	 * jiangc
	 * @param pageRequest
	 * @return
	 */
	public List<Map<String, Object>> getProjectPage(PageRequest pageRequest);

	/**
	 * 查询监测项目的数量
	 *
	 * jiangc
	 * @param pageRequest
	 * @return
	 */
	public long getProjectPageCount(PageRequest pageRequest);

	/**
	 * 添加用户浏览日志
	 *
	 * jiangc
	 * @param bLog
	 * @return
	 */
	public int saveBrowseLog(BrowseLog bLog);

	/**
	 * 修改用户浏览日志结束时间
	 *
	 * jiangc
	 * @param browseId
	 * @param endTime
	 * @return
	 */
	public int updateBrowseLog(String browseId, Date endTime);

	/**
	 * 添加用户操作日志
	 *
	 * jiangc
	 * @param hLog
	 * @return
	 */
	public int saveHandleLog(HandleLog hLog);

	/**
	 * 分页查询浏览日志数据
	 *
	 * jiangc
	 * @param start
	 * @param length
	 * @param search
	 * @return
	 */
	public List<Map<String, Object>> getBrowseLogs(Integer start,
			Integer length, String search);

	/**
	 * 统计浏览数据总量
	 *
	 * jiangc
	 * @param search
	 * @return
	 */
	public long getBrowseLogCount(String search);

	/**
	 * 分页查询用户操作日志数据
	 *
	 * jiangc
	 * @param start
	 * @param length
	 * @param search
	 * @return
	 */
	public List<Map<String, Object>> getHandleLogs(Integer start,
			Integer length, String search);

	/**
	 * 用户操作日志总记录数
	 *
	 * jiangc
	 * @param search
	 * @return
	 */
	public long getHandleLogCount(String search);

	public List<Map<String, Object>> reportBrowse(String projectId);

	public List<Map<String, Object>> reportHandle(String projectId);

}
