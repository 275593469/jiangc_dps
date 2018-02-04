package top.jiangc.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.stereotype.Component;

@Component("project")
@Entity(name="project")
public class Project {

	@Id
	@Column(name="project_id",length=50)
	private String projectId;
	
	//项目名
	@Column(name="project_name",length=50,nullable=false)
	private String projectName;
	
	//服务器ip+端口
	@Column(name="station_ip",length=50,nullable=false)
	private String stationIp;
	
	//状态：1：正常，0：删除
	@Column(name="status",nullable=false)
	private int status;
	
	//创建时间
	@Column(name="create_time",nullable=false)
	private Date createTime;
	
	//修改时间
	@Column(name="update_time",nullable=false)
	private Date updateTime;
	
	//管理员id
	@ManyToOne(targetEntity=User.class,optional=true,cascade=CascadeType.ALL)
	@JoinColumn(name="manager_id",nullable=false)
	private User user;

	public Project() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Project(String projectId, String projectName, String stationIp,
			int status, User user) {
		super();
		this.projectId = projectId;
		this.projectName = projectName;
		this.stationIp = stationIp;
		this.status = status;
		this.user = user;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getStationIp() {
		return stationIp;
	}

	public void setStationIp(String stationIp) {
		this.stationIp = stationIp;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Project [projectId=" + projectId + ", projectName="
				+ projectName + ", stationIp=" + stationIp + ", status="
				+ status + ", user=" + user + "]";
	}
	
	
	
}
