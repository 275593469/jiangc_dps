package top.jiangc.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;

import top.jiangc.dao.ILogDao;
import top.jiangc.entity.BrowseLog;
import top.jiangc.entity.HandleLog;
import top.jiangc.entity.Page;
import top.jiangc.entity.PageRequest;
import top.jiangc.entity.Project;
import top.jiangc.entity.User;
import top.jiangc.entity.UserLog;
import top.jiangc.service.ILogService;
import top.jiangc.util.UUIDUtil;

/**
 * 日志处理
 * @author jiangc
 * 2017年4月4日
 */
@Service("logService")
public class LogService implements ILogService {
	
	@Resource
	private ILogDao logDao;

	@Override
	public Integer addUserLog(UserLog userLog) {
		return logDao.addUserLog(userLog);
		
	}

	@Override
	public List<Map<String, Object>> findUserLog(Date operationTime) {
		return logDao.findUserLog(operationTime);
	}

	@Override
	public Page<Map<String, Object>> getUserLog(PageRequest pageRequest) {
		Page<Map<String, Object>> page = new Page<>();
		List<Map<String, Object>> lstUserLog = null;
		List<Map<String, Object>> lstCount = null;
		Integer start = pageRequest.getstart();
		Integer length = pageRequest.getlength();
		String search = pageRequest.getsearch();
		//查询数据
		lstUserLog = logDao.getUserLog(start, length, search);
		//查询出数据量，分页
		lstCount = logDao.getUserLogCount(search);
		page.setLstEntity(lstUserLog);
		page.setSearchValue(search);
		page.setPageCount(((BigInteger) lstCount.get(0).get("count")).longValue());
		return page;
	}

	@Override
	public List<Map<String, Object>> reportUserLog(String operation) {
		return logDao.reportUserLog(operation);
	}

	@Override
	public List<Map<String, Object>> getUserLogCategory() {
		return logDao.getUserLogCategory();
	}

	@Override
	public List<Map<String, Object>> getProjects() {
		return logDao.getProjects();
	}

	@Override
	public boolean isExistProject(String projectName, String stationIp) {
		List<Map<String,Object>> lstResult = new ArrayList<>();
		lstResult = logDao.findProject(projectName, stationIp);
		if(lstResult.size() > 0){
			return true;
		}else{
			return false;
		}
		
	}

	@Override
	public Project addProject(String projectName, String stationIp, User user) {
		Project project = new Project();
		project.setProjectId(UUIDUtil.getUUID());
		project.setProjectName(projectName);
		project.setStationIp(stationIp);
		project.setStatus(1);
		project.setUser(user);
		
		int result = logDao.addProject(project);
		if(result == 1){
			return project;
		}
		return null;
	}

	@Override
	public Page<Map<String, Object>> getProjectPage(PageRequest pageRequest, String contextPath) {
		Page<Map<String, Object>> page = new Page<>();
		List<Map<String, Object>> lstProject = new ArrayList<>();
		lstProject = logDao.getProjectPage(pageRequest);
		for(Map<String, Object> map : lstProject){
			int status = (int) map.get("status");
			switch(status){
				case 0: map.put("status", "删除");
				break;
				case 1: map.put("status", "正常");
				break;
				default: map.put("status", "待审核");
				break;
			}
			String sParams = "projectId="+map.get("projectId")+"&projectName="+map.get("projectName");
			String downloadAddr = "<a href=\""+contextPath+"/log/downloadJs?"+sParams+"\">下载js</a>";
			//添加下载地址
			map.put("download", downloadAddr);
		}
		long pageCount = logDao.getProjectPageCount(pageRequest);
		page.setLstEntity(lstProject);
		page.setPageCount(pageCount);
		
		return page;
	}

	@Override
	public int saveBrowseLog(Map<String, Object> params) {
		String browseId = (String) params.get("browseId");
		browseId = StringUtils.isEmpty(browseId)?UUIDUtil.getUUID():browseId;
		String title = (String) params.get("title");
		String pageUrl = (String) params.get("pageUrl");
		String userId = (String) params.get("userId");
		String projectId = (String) params.get("projectId");
		
		BrowseLog bLog = new BrowseLog();
		bLog.setBrowseId(browseId);
		bLog.setPageTitle(title);
		bLog.setPageUrl(pageUrl);
		User user = new User();
		user.setUserId(userId);
		bLog.setUser(user);
		bLog.setProjectId(projectId);
		bLog.setBeginTime(new Date());
		
		return logDao.saveBrowseLog(bLog);
	}

	@Override
	public int updateBrowseLog(Map<String, Object> params) {
		String browseId = (String) params.get("browseId");
		Date endTime = new Date();
		
		return logDao.updateBrowseLog(browseId, endTime);
	}

	@Override
	public int saveHandleLog(Map<String, Object> params) {
		HandleLog hLog = new HandleLog();
		hLog.setHandleId(UUIDUtil.getUUID());
		hLog.setActiveTime(new Date());
		hLog.setButtonText((String) params.get("buttonText"));
		hLog.setEvent((String) params.get("event"));
		hLog.setProjectId((String) params.get("projectId"));
		User user = new User();
		user.setUserId((String) params.get("userId"));
		hLog.setUser(user);
		return logDao.saveHandleLog(hLog);
	}

	@Override
	public Page<Map<String, Object>> getBrowseLog(PageRequest pageReq) {
		Page<Map<String, Object>> page = new Page<>();
		List<Map<String, Object>> lstLog = new ArrayList<>();
		Integer start = pageReq.getstart();
		Integer length = pageReq.getlength();
		String search = pageReq.getsearch();
		lstLog = logDao.getBrowseLogs(start, length, search);
		long count = logDao.getBrowseLogCount(search);
		page.setLstEntity(lstLog);
		page.setPageCount(count);
		return page;
	}

	@Override
	public Page<Map<String, Object>> getHandleLog(PageRequest pageReq) {
		Page<Map<String, Object>> page = new Page<>();
		List<Map<String, Object>> lstLog = new ArrayList<>();
		Integer start = pageReq.getstart();
		Integer length = pageReq.getlength();
		String search = pageReq.getsearch();
		lstLog = logDao.getHandleLogs(start, length, search);
		long count = logDao.getHandleLogCount(search);
		page.setLstEntity(lstLog);
		page.setPageCount(count);
		return page;
	}

	@Override
	public List<Map<String, Object>> reportBrowse(String projectId) {
		return logDao.reportBrowse(projectId);
	}

	@Override
	public List<Map<String, Object>> reportHandle(String projectId) {
		return logDao.reportHandle(projectId);
	}

	
}
