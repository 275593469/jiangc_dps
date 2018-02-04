package top.jiangc.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import top.jiangc.entity.Page;
import top.jiangc.entity.PageRequest;
import top.jiangc.entity.Project;
import top.jiangc.entity.User;
import top.jiangc.hadoop.UploadFile;
import top.jiangc.service.ILogService;
import top.jiangc.util.FileUpload;
import top.jiangc.util.JsonUtil;
import top.jiangc.util.LogUtil;

import com.alibaba.druid.util.StringUtils;

/**
 * 日志管理
 * @author jiangc
 *
 */
@RequestMapping("/log")
@RestController("logController")
public class LogController {
	Logger logger = LoggerFactory.getLogger(LogController.class);
	
	@Autowired
	private ILogService logService;
	
	@Autowired
	private UploadFile uploadFile;
	
	/**
	 * 查询出用户的日志信息(用于主页日志的显示)
	 *
	 * jiangc
	 * @param request
	 * @return
	 */
	@RequestMapping("/getUserLog")
	public String getUserLog(final HttpServletRequest request, 
			@RequestParam(name="time",required=false) Date operationTime){
		Map<String, Object> resultMap = new HashMap<>();
		List<Map<String, Object>> lstUserLog = new ArrayList<>();
		lstUserLog = logService.findUserLog(operationTime);
		if(lstUserLog.size() > 0){
			long nowDate = new Date().getTime();
			resultMap.put("nowDate", nowDate);
			resultMap.put("logs", lstUserLog);
			return JsonUtil.returnSuccess("查询成功", resultMap);
		}else{
			return JsonUtil.returnFail("查询失败", null);
		}
	}

	/**
	 * 查询用户的日志信息（用与分页的显示）
	 *
	 * jiangc
	 * @param request
	 * @param pageRequest
	 * @return
	 */
	@RequestMapping("/getUserLogPage")
	public String getUserLogPage(final HttpServletRequest request, 
			@RequestParam(value="start",required=true) Integer start,
			@RequestParam(value="length",required=true) Integer length,
			@RequestParam(value="search[value]",required=false) String search,
			@RequestParam(value="draw",required=true) Integer draw){
		PageRequest pageRequest = new PageRequest(start, length, search);
		Page<Map<String, Object>> page = logService.getUserLog(pageRequest);
		
		return JsonUtil.returnDataTable(page, draw);
	}
	
	/**
	 * 用户日志的报表统计
	 *
	 * jiangc
	 * @param reqest
	 * @param operation
	 * @return
	 */
	@RequestMapping("/reportUserLog")
	public String reportUserLog(final HttpServletRequest reqest,
			@RequestParam(value="operation",required=false) String operation){
		List<Map<String, Object>> lstResult = new ArrayList<>();
		lstResult = logService.reportUserLog(operation);
		if(lstResult.size() > 0){
			return JsonUtil.returnSuccess("查询成功", lstResult);
		}else{
			return JsonUtil.returnFail("暂无数据", null);
		}
	}
	
	/**
	 * 获取用户日志操作类别
	 *
	 * jiangc
	 * @param request
	 * @return
	 */
	@RequestMapping("/getUserLogCategory")
	public String getUserLogCategory(final HttpServletRequest request){
		List<Map<String, Object>> lstResult = new ArrayList<>();
		lstResult = logService.getUserLogCategory();
		if(lstResult.size() > 0){
			return JsonUtil.returnSuccess("查询成功", lstResult);
		}else{
			return JsonUtil.returnFail("暂无数据", null);
		}
	}
	
	/**
	 * 日志记录到文件
	 *
	 * jiangc
	 * @param request
	 * @return
	 */
	private void saveLogFile(String sJson){
		FileUpload.createFileLog(sJson);
	}
	
	/**
	 * 保存用户浏览记录
	 *
	 * jiangc
	 * @param request
	 * @return
	 */
	@RequestMapping("/saveBrowseLog")
	public String saveBrowseLog(final HttpServletRequest request, 
			@RequestParam(value="json",required=true) String sJson){
		saveLogFile(sJson);//保存日志文件
		Map<String, Object> params = new HashMap<>();
		params = JsonUtil.parseMap(sJson);
		Boolean isStart = (Boolean) params.get("isStart");
		int result = 0;
		if(isStart){//页面浏览开始
			result = logService.saveBrowseLog(params);
		}else{//切换页面
			result = logService.updateBrowseLog(params);
		}
		
		if(result > 0){
			return JsonUtil.returnSuccess("操作成功", null);
		}else{
			return JsonUtil.returnFail("操作失败", null);
		}
	}
	
	/**
	 * 保存页面上的操作事件
	 *
	 * jiangc
	 * @param request
	 * @return
	 */
	@RequestMapping("/saveHandleLog")
	public String saveHandleLog(final HttpServletRequest request, 
			@RequestParam(value="json",required=true) String sJson){
		saveLogFile(sJson);//保存日志文件
		Map<String, Object> params = new HashMap<>();
		params = JsonUtil.parseMap(sJson);
		int result = 0;
		result = logService.saveHandleLog(params);
		if(result > 0){
			return JsonUtil.returnSuccess("操作成功", null);
		}else{
			return JsonUtil.returnFail("操作失败", null);
		}
	}
	
	/**
	 * 获取监测的项目名
	 *
	 * jiangc
	 * @param request
	 * @return
	 */
	@RequestMapping("/getProject")
	public String getProjects(final HttpServletRequest request){
		List<Map<String, Object>> lstProject = new ArrayList<>();
		lstProject = logService.getProjects();
		
		if(lstProject.size() > 0){
			return JsonUtil.returnSuccess("查询成功", lstProject);
		}else{
			return JsonUtil.returnFail("暂无数据", null);
		}
	}
	
	/**
	 * 添加监听的项目
	 *
	 * jiangc
	 * @param request
	 * @param projectName  项目名
	 * @param serverIp 项目服务器ip
	 * @param port  端口号
	 * @return  result: 2 表示session超时或未登录 ;3表示项目已存在;1:添加成功；0：添加失败
	 */
	@RequestMapping("/addProject")
	public String addProject(final HttpServletRequest request,
			@RequestParam(value="projectName",required=true) String projectName,
			@RequestParam(value="serverIp",required=true) String serverIp,
			@RequestParam(value="port",required=false) String port){
		
		User user = (User) request.getSession().getAttribute("user");
		if(null == user){
			return JsonUtil.returnFail("您还没有登录", 2);
		}
		
		port = StringUtils.isEmpty(port)?"":":"+port;
		String stationIp = "http://"+serverIp+port;
		if(logService.isExistProject(projectName, stationIp)){
			return JsonUtil.returnFail("该项目已经存在", 3);
		}
		
		Project project = logService.addProject(projectName, stationIp, user);
		if(null != project){
			return JsonUtil.returnSuccess("添加成功", project);
		}else{
			return JsonUtil.returnFail("添加失败", 0);
		}
		
	}
	
	/**
	 * 分页查询监测的项目
	 *
	 * jiangc
	 * @param request
	 * @param start 起始页 （与mysql的limit分页相同）
	 * @param length 页面大小
	 * @param search
	 * @param draw
	 * @return
	 */
	@RequestMapping("/getProjectPage")
	public String getProjectPage(final HttpServletRequest request,
			@RequestParam(value="start",required=true) Integer start,
			@RequestParam(value="length",required=true) Integer length,
			@RequestParam(value="search[value]",required=false) String search,
			@RequestParam(value="draw",required=true) Integer draw){
		PageRequest pageRequest = new PageRequest(start, length, search);
		String contextPath = request.getContextPath();
		Page<Map<String, Object>> page = logService.getProjectPage(pageRequest, contextPath);
		
		return JsonUtil.returnDataTable(page, draw);
	}
	
	/**
	 * 下载监测js文件
	 *
	 * jiangc
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/downloadJs")
	public String downloadJs(final HttpServletRequest request, final HttpServletResponse response,
			@RequestParam(value="projectId",required=true) String projectId,
			@RequestParam(value="projectName",required=true) String projectName) throws IOException{
		 String fileName = "listener.js";
		 Map<String, Object> params = new HashMap<>();
		 String path = request.getContextPath(); 
		 String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";  
		 params.put("projectId", projectId);
		 params.put("projectName", projectName);
		 params.put("contextPath", basePath);
		 //用来获取类路径下的资源文件
		 ClassPathResource classPathResource = new ClassPathResource("config/"+fileName);
		 InputStream is = classPathResource.getInputStream();
		 OutputStream os = response.getOutputStream();
		 try{
			 response.setContentType("application/force-download");// 设置强制下载不打开
	         response.addHeader("Content-Disposition","attachment;fileName=" +  fileName);// 设置文件名
			 downLoadJs(is, os, params);
		 }catch(Exception e){
			 logger.error(e.getMessage());
			 throw new RuntimeException(e);
		 }finally{
			 os.close();
			 is.close();
		 }
		 
    return null;
	}

	/**
	 * 下载监测的js
	 * @author: jiangcheng
	 *
	 * @param is
	 * @param os
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private void downLoadJs(InputStream is, OutputStream os, Map<String, Object> params) throws Exception {
		StringBuffer sbResult = new StringBuffer();
		if(null != is){
			byte[] buf = new byte[1024];
			int len = 0;
			while((len = is.read(buf)) != -1){
				sbResult.append(new String(buf, "utf-8"));
			}
			
			String sreplaced = replaceParams(sbResult.toString(), params);
			os.write(sreplaced.getBytes("utf-8"));
		}
	}

	/**
	 * 替换字符串里面的参数
	 * @author: jiangcheng
	 *
	 * @param string
	 * @param params
	 * @return
	 */
	private String replaceParams(String targetStr, Map<String, Object> params) {
		for(Entry<String, Object> set: params.entrySet()){
			String key = set.getKey();
			String value = (String) set.getValue();
			targetStr = targetStr.replace("${"+key+"}", value);
		}
		return targetStr.trim();
	}
	
	/**
	 * 分页查询用户浏览日志
	 *
	 * jiangc
	 * @param request
	 * @param start
	 * @param length
	 * @param search
	 * @param draw
	 * @return
	 */
	@RequestMapping("/getBrowseLogPage")
	public String getBrowseLogPage(final HttpServletRequest request, 
			@RequestParam(value="start",required=true) Integer start,
			@RequestParam(value="length",required=true) Integer length,
			@RequestParam(value="search[value]",required=false) String search,
			@RequestParam(value="draw",required=true) Integer draw){
		PageRequest pageReq = new PageRequest(start, length, search);
		
		Page<Map<String, Object>> page = logService.getBrowseLog(pageReq);
		
		return JsonUtil.returnDataTable(page, draw);
	}
	
	/**
	 * 分页查询用户操作日志信息
	 *
	 * jiangc
	 * @param request
	 * @param start
	 * @param length
	 * @param search
	 * @param draw
	 * @return
	 */
	@RequestMapping("/getHandleLogPage")
	public String getHandleLogPage(final HttpServletRequest request,
			@RequestParam(value="start",required=true) Integer start,
			@RequestParam(value="length",required=true) Integer length,
			@RequestParam(value="search[value]",required=false) String search,
			@RequestParam(value="draw",required=true) Integer draw){
		PageRequest pageReq = new PageRequest(start, length, search);
		
		Page<Map<String, Object>> page = logService.getHandleLog(pageReq);
		
		return JsonUtil.returnDataTable(page, draw);
	}
	
	/**
	 * 根据按钮名来统计用户浏览日志信息
	 *
	 * jiangc
	 * @param request
	 * @return
	 */
	@RequestMapping("/reportBrowseByButton")
	public String reportBrowseByButton(final HttpServletRequest request,
			@RequestParam(value="projectId",required=true) String projectId){
		
		List<Map<String, Object>> lstResult = new ArrayList<>(); 
		lstResult = logService.reportBrowse(projectId);
		if(lstResult.size() > 0){
			return JsonUtil.returnSuccess("查询成功", lstResult);
		}
		return JsonUtil.returnFail("查询失败", null);
	}
	
	/**
	 * 统计用户页面操作日志信息
	 *
	 * jiangc
	 * @param request
	 * @return
	 */
	@RequestMapping("/reportHandleByTitle")
	public String reportHandleByTitle(final HttpServletRequest request, 
			@RequestParam(value="projectId",required=true) String projectId){
		List<Map<String, Object>> lstResult = new ArrayList<>();
		lstResult = logService.reportHandle(projectId);
		if(lstResult.size() > 0){
			return JsonUtil.returnSuccess("查询成功", lstResult);
		}
		return JsonUtil.returnFail("查询失败", null);
	}
	
	/**
	 * 上传处理日志文件
	 *
	 * jiangc
	 * @param request
	 * @return
	 */
	@RequestMapping("/uploadLogs")
	public String uploadLogs(final HttpServletRequest request,
			@RequestParam(value="fileData",required=true) MultipartFile file){
		
		if(file.isEmpty()){
			return JsonUtil.returnFail("请添加文件", null);
		}
		InputStream is = null;
		BufferedReader br = null;
		Map<String, Object> mapResult = null;
		try {
			is = file.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
			
			String logLine = null;
			List<String> lstLog = new ArrayList<>();
			while((logLine = br.readLine()) != null){
				if(!logLine.trim().equals("")){
					lstLog.add(logLine);
				}
			}
			//处理日志
			mapResult = handleLogs(lstLog);
		} catch (IOException e) {
			//记录处理数据日志
			LogUtil.userLog(request, "数据处理", 0);
			logger.error(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if(br != null) br.close();
				if(is != null) is.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		}
		LogUtil.userLog(request, "数据处理", 1);
		return JsonUtil.returnSuccess("处理成功", mapResult);
	}

	/**
	 * 处理用户行为数据的日志文件数据
	 *
	 * jiangc
	 * @param lstLog
	 */
	private Map<String, Object> handleLogs(List<String> lstLog) {
		Map<String, Object> mapBrowse = new HashMap<>();
		Map<String, Object> mapHandle = new HashMap<>();
		Map<String, Object> mapResult = new HashMap<>();
		for(String log : lstLog){
			String[] logs = log.split("  ");
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:S");
//			Date data = sdf.parse(logs[0]);
			Map<String, Object> mapLog = JsonUtil.parseMap(logs[1]);
			if(null != mapLog.get("browseId") ){//表示浏览日志
				String title = (String) mapLog.get("title");//浏览页面标题
				if(mapBrowse.get(title) == null){
					mapBrowse.put(title, 1);
				}else{
					int count = (int)mapBrowse.get(title);
					mapBrowse.put(title, ++count); 
				}
			}else{
				String buttonText = (String) mapLog.get("buttonText");//操作页面按钮
				if(mapHandle.get(buttonText) == null){
					mapHandle.put(buttonText, 1);
				}else{
					int count = (int)mapHandle.get(buttonText);
					mapHandle.put(buttonText, ++count); 
				}
			}
			
		}
		
		mapResult.put("browse", mapBrowse);
		mapResult.put("handle", mapHandle);
		return mapResult;
		
	}
	
	/**
	 * 上传日志文件到Hadoop并处理
	 *
	 * jiangc
	 * @param request
	 * @return
	 */
	@RequestMapping("/uploadLogToHadoop")
	public String uploadLogToHadoop(final HttpServletRequest request,
			@RequestParam(value="fileData",required=true) MultipartFile file){
		
		if(file.isEmpty()){
			return JsonUtil.returnFail("请添加文件", null);
		}
		String fileName = file.getOriginalFilename();
		InputStream is = null;
		Map<String, Object> mapResult = new HashMap<>();
		try {
			is = file.getInputStream();
			List<String> lst = uploadFile.UploadAndReport(is, fileName);
			Map<String, Object> mapBrowse = new HashMap<>();
			Map<String, Object> mapHandle = new HashMap<>();
			for(String log : lst){
				String[] logs = log.split(":");
				if("browse".equals(logs[0])){//浏览记录
					String[] browses = logs[1].split("\\s+");
					mapBrowse.put(browses[0], browses[1]);
				}else{
					String[] handles = logs[1].split("\\s+");
					mapHandle.put(handles[0], handles[1]);
				}
				mapResult.put("browse", mapBrowse);
				mapResult.put("handle", mapHandle);
			}
		} catch (IOException e) {
			//记录处理数据日志
			LogUtil.userLog(request, "Hadoop数据处理", 0);
			logger.error(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if(is != null) is.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		}
		LogUtil.userLog(request, "Hadoop数据处理", 1);
		return JsonUtil.returnSuccess("处理成功", mapResult);
	}
	
}
