package top.jiangc.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.stereotype.Component;

/**
 * 用户页面浏览记录
 *@author jiangcheng
 *
 *2017年3月31日
 */
@Component("browseLog")
@Entity(name="browse_log")
public class BrowseLog {
	
	@Id
	@Column(name="browse_id", length=50)
	private String browseId;
	
	//用户id
	@ManyToOne(targetEntity=User.class,optional=true,cascade=CascadeType.ALL)
	@JoinColumn(name="user_id",nullable=false)
	private User user;
	
	//页面路径
	@Column(length=100,name="page_url",nullable=false)
	private String pageUrl;
	
	//页面标题
	@Column(length=100,name="page_title",nullable=false)
	private String pageTitle;
	
	//浏览开始时间
	@Column(name="begin_time")
	private Date beginTime;
	
	//浏览结束时间
	@Column(name="end_time")
	private Date endTime;
	
	//监测项目id
	@Column(name="project_id",length=50,nullable=false)
	private String projectId;

	public BrowseLog() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getBrowseId() {
		return browseId;
	}

	public void setBrowseId(String browseId) {
		this.browseId = browseId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	@Override
	public String toString() {
		return "BrowseLog [browseId=" + browseId + ", user=" + user
				+ ", pageUrl=" + pageUrl + ", pageTitle=" + pageTitle
				+ ", beginTime=" + beginTime + ", endTime=" + endTime + "]";
	}
	
	

}
