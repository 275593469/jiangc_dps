package top.jiangc.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import top.jiangc.dao.ILogDao;
import top.jiangc.dao.IdataTemplate;
import top.jiangc.entity.BrowseLog;
import top.jiangc.entity.HandleLog;
import top.jiangc.entity.PageRequest;
import top.jiangc.entity.Project;
import top.jiangc.entity.UserLog;

import com.alibaba.druid.util.StringUtils;

@Repository("logDao")
public class LogDao implements ILogDao {

	@Resource(name = "dataTemplate")
	private IdataTemplate dataTemplate;
	
	@Override
	public Integer addUserLog(UserLog userLog) {
		Map<String, Object> params = new HashMap<>();
		
		String sql = "INSERT INTO user_log(user_log_id,user_id,operation,result,operation_time,ip)"
				+" VALUES(:userLogId,:userId,:operation,:result,:operationTime,:ip);";
		params.put("userLogId", userLog.getUserLogId());
		params.put("userId", userLog.getUser().getUserId());
		params.put("operation", userLog.getOperation());
		params.put("result", userLog.getResult());
		params.put("operationTime", userLog.getOperationTime());
		params.put("ip", userLog.getIp());
		
		return dataTemplate.deleteOrUpDate(sql, params);
		
	}

	@Override
	public List<Map<String, Object>> findUserLog(Date operationTime) {
		Map<String, Object> params = new HashMap<>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select a.nick_name,l.operation,l.result,l.operation_time");
		sql.append(" from user_log l LEFT JOIN user_account a on l.user_id=a.user_id ");
		sql.append(" where l.operation_time > :operationTime ORDER BY l.operation_time DESC");
		if(null == operationTime){
			long currentTime = new Date().getTime();
			operationTime = new Date(currentTime - 24*60*60*1000);
		}
		params.put("operationTime", operationTime);
		return dataTemplate.queryListEntity(sql.toString(), params);
	}

	@Override
	public List<Map<String, Object>> getUserLog(Integer start,
			Integer length, String search) {
		Map<String, Object> params = new HashMap<>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select l.user_log_id as userLogId, a.nick_name as nickName,l.operation,l.result,date_format(l.operation_time, '%Y-%m-%d %H:%i:%s') as operationTime,l.ip");
		sql.append(" from user_log l LEFT JOIN user_account a on l.user_id=a.user_id ");
		if(!StringUtils.isEmpty(search)){//查询关键字非空
			sql.append(" where a.nick_name like :nickName");
			params.put("nickName", search+"%");
		}
		sql.append(" ORDER BY l.operation_time DESC");
		sql.append(" limit :page1,:page2");
		params.put("page1", start);
		params.put("page2", length);
		
		return dataTemplate.queryListEntity(sql.toString(), params);
	}

	@Override
	public List<Map<String, Object>> getUserLogCount(String searchValue) {
		Map<String, Object> params = new HashMap<>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(1) as count");
		sql.append(" from user_log l LEFT JOIN user_account a on l.user_id=a.user_id ");
		if(!StringUtils.isEmpty(searchValue)){//查询关键字非空
			sql.append(" where a.nick_name like :nickName");
			params.put("nickName", searchValue+"%");
		}
		int count = dataTemplate.getCountBy(sql.toString(), params);
		System.out.println(count);
		return dataTemplate.queryListEntity(sql.toString(), params);
	}

	@Override
	public List<Map<String, Object>> reportUserLog(String operation) {
		Map<String, Object> params = new HashMap<>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DATE_FORMAT( operation_time, '%H' ) as operationTime, operation,COUNT( * ) as count FROM user_log");
		if(!StringUtils.isEmpty(operation)){
			sql.append(" where operation = :operation");
			params.put("operation", operation);
		}
		sql.append(" GROUP BY operation,DATE_FORMAT( operation_time, '%H');");
		return dataTemplate.queryListEntity(sql.toString(), params);
	}

	@Override
	public List<Map<String, Object>> getUserLogCategory() {
		String sql = "SELECT operation from user_log GROUP BY operation;";
		return dataTemplate.queryListEntity(sql);
	}

	@Override
	public List<Map<String, Object>> getProjects() {
		String sql = "select project_id as projectId,project_name as projectName,station_ip as stationIp from project where `status` = 1;";
		return dataTemplate.queryListEntity(sql);
	}

	@Override
	public int addProject(Project project) {
		Date date = new Date();
		String sql = "INSERT INTO project(project_id,project_name,station_ip,manager_id,create_time,update_time,`status`) VALUES (?,?,?,?,?,?,?);";
		return dataTemplate.deleteOrUpDate(sql, new Object[]{project.getProjectId(),project.getProjectName(),
				project.getStationIp(),project.getUser().getUserId(),date,date,project.getStatus()});
	}

	@Override
	public List<Map<String, Object>> findProject(String projectName,
			String stationIp) {
		String sql = "select project_id as projectId from project where project_name=? and station_ip=? and status=1;";
		return dataTemplate.queryListEntity(sql, new Object[]{projectName, stationIp});
	}

	@Override
	public List<Map<String, Object>> getProjectPage(PageRequest pageRequest) {
		Map<String, Object> params = new HashMap<>();
		List<Map<String, Object>> lstResult = new ArrayList<>();
		int start = pageRequest.getstart();
		int length = pageRequest.getlength();
		String search = pageRequest.getsearch();
		String sql = "select p.project_id as projectId,p.project_name as projectName,p.station_ip as stationIp,"
				+ "date_format(p.create_time, '%Y-%m-%d %H:%i:%s') as createTime,date_format(p.update_time, '%Y-%m-%d %H:%i:%s') as updateTime,p.`status`,u.nick_name as nickName "
				+ "from project p LEFT JOIN user_account u ON p.manager_id=u.user_id";
		if(!StringUtils.isEmpty(search)){
			sql += " p.projectName like :search";
			params.put("search", search+"%");
		}
		sql += " limit :start,:length";
		params.put("start", start);
		params.put("length", length);
		
		lstResult = dataTemplate.queryListEntity(sql, params);
		return lstResult;
	}

	@Override
	public long getProjectPageCount(PageRequest pageRequest) {
		Map<String, Object> params = new HashMap<>();
		int start = pageRequest.getstart();
		int length = pageRequest.getlength();
		String search = pageRequest.getsearch();
		String sql = "select count(1) from project p";
		if(!StringUtils.isEmpty(search)){
			sql += " p.projectName like :search";
			params.put("search", search+"%");
		}
		
		long count = dataTemplate.getCountBy(sql, params);
		return count;
	}

	@Override
	public int saveBrowseLog(BrowseLog bLog) {
		String sql = "insert into browse_log(browse_id,user_id,page_url,page_title,begin_time,project_id) values(?,?,?,?,?,?);";
		Object[] objs = new Object[]{bLog.getBrowseId(),bLog.getUser().getUserId(),bLog.getPageUrl(),
				bLog.getPageTitle(),bLog.getBeginTime(),bLog.getProjectId()};
		return dataTemplate.deleteOrUpDate(sql, objs);
	}

	@Override
	public int updateBrowseLog(String browseId, Date endTime) {
		String sql = "UPDATE browse_log set end_time=? where browse_id=?;";
		return dataTemplate.deleteOrUpDate(sql, new Object[]{endTime, browseId});
	}

	@Override
	public int saveHandleLog(HandleLog hLog) {
		String sql = "insert into handle_log(handle_id,user_id,event,button_text,active_time,project_id) values(?,?,?,?,?,?);";
		Object[] objs = new Object[]{hLog.getHandleId(),hLog.getUser().getUserId(),hLog.getEvent(),
				hLog.getButtonText(),hLog.getActiveTime(),hLog.getProjectId()};
		return dataTemplate.deleteOrUpDate(sql, objs);
	}

	@Override
	public List<Map<String, Object>> getBrowseLogs(Integer start,
			Integer length, String search) {
		StringBuffer sbSql = new StringBuffer();
		Map<String, Object> params = new HashMap<>();
		sbSql.append(" SELECT b.browse_id AS browseId,b.user_id AS userId,u.nick_name AS nickName,");
		sbSql.append(" b.page_url AS pageUrl,b.page_title AS pageTitle,date_format(b.begin_time, '%Y-%m-%d %H:%i:%s') AS beginTime,");
		sbSql.append(" date_format(b.end_time, '%Y-%m-%d %H:%i:%s') AS endTime,b.project_id AS projectId,p.project_name AS projectName");
		sbSql.append(" FROM browse_log AS b");
		sbSql.append(" LEFT JOIN user_account u on b.user_id=u.user_id");
		sbSql.append(" LEFT JOIN project p on b.project_id=p.project_id");
		if(!StringUtils.isEmpty(search)){
			sbSql.append(" WHERE u.nick_name like :search");
			params.put("search", search+"%");
		}
		sbSql.append(" ORDER BY b.begin_time DESC");
		sbSql.append(" limit :start,:length");
		params.put("start", start);
		params.put("length", length);

		return dataTemplate.queryListEntity(sbSql.toString(), params);
	}

	@Override
	public long getBrowseLogCount(String search) {
		StringBuffer sbSql = new StringBuffer();
		Map<String, Object> params = new HashMap<>();
		sbSql.append(" SELECT count(1)");
		sbSql.append(" FROM browse_log AS b");
		sbSql.append(" LEFT JOIN user_account u on b.user_id=u.user_id");
		if(!StringUtils.isEmpty(search)){
			sbSql.append(" WHERE u.nick_name like :search");
			params.put("search", search+"%");
		}

		return dataTemplate.getCountBy(sbSql.toString(), params);
	}

	@Override
	public List<Map<String, Object>> getHandleLogs(Integer start,
			Integer length, String search) {
		StringBuffer sbSql = new StringBuffer();
		Map<String, Object> params = new HashMap<>();
		sbSql.append(" SELECT h.handle_id AS handleId,h.user_id AS userId,u.nick_name AS nickName,");
		sbSql.append(" h.event,h.button_text AS buttonText,date_format(h.active_time, '%Y-%m-%d %H:%i:%s') AS activeTime,");
		sbSql.append(" h.project_id AS projectId,p.project_name AS projectName");
		sbSql.append(" FROM handle_log AS h");
		sbSql.append(" LEFT JOIN user_account u on h.user_id=u.user_id");
		sbSql.append(" LEFT JOIN project p on h.project_id=p.project_id");
		if(!StringUtils.isEmpty(search)){
			sbSql.append(" WHERE u.nick_name like :search");
			params.put("search", search+"%");
		}
		sbSql.append(" ORDER BY h.active_time DESC");
		sbSql.append(" limit :start,:length");
		params.put("start", start);
		params.put("length", length);

		return dataTemplate.queryListEntity(sbSql.toString(), params);
	}

	@Override
	public long getHandleLogCount(String search) {
		StringBuffer sbSql = new StringBuffer();
		Map<String, Object> params = new HashMap<>();
		sbSql.append(" SELECT count(1)");
		sbSql.append(" FROM handle_log AS h");
		sbSql.append(" LEFT JOIN user_account u on h.user_id=u.user_id");
		if(!StringUtils.isEmpty(search)){
			sbSql.append(" WHERE u.nick_name like :search");
			params.put("search", search+"%");
		}

		return dataTemplate.getCountBy(sbSql.toString(), params);
	}

	@Override
	public List<Map<String, Object>> reportBrowse(String projectId) {
		String sql = "select button_text AS buttonText,COUNT(1) as count from handle_log where project_id=? GROUP BY button_text";
		return dataTemplate.queryListEntity(sql, projectId);
	}

	@Override
	public List<Map<String, Object>> reportHandle(String projectId) {
		String sql = "select page_title AS pageTitle,COUNT(1) as count from browse_log where project_id=? GROUP BY page_title";
		return dataTemplate.queryListEntity(sql,  projectId);
	}
	

}
