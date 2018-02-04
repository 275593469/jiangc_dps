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
 *用户页面操作事件记录 
 *@author jiangcheng
 *
 *2017年3月31日
 */
@Component("handleLog")
@Entity(name="handle_log")
public class HandleLog {

	@Id
	@Column(length=50,name="handle_id")
	private String handleId;
	
	//用户id
	@ManyToOne(targetEntity=User.class,optional=true,cascade=CascadeType.ALL)
	@JoinColumn(name="user_id",nullable=false)
	private User user;
	
	//页面上的事件
	@Column(length=36,name="event",nullable=false)
	private String event;
	
	//按钮的文字
	@Column(length=100,name="button_text",nullable=false)
	private String buttonText;
	
	//时间触发时间
	@Column(name="active_time")
	private Date activeTime;
	
	//监测项目id
	@Column(name="project_id",length=50,nullable=false)
	private String projectId;
	
	public HandleLog() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String getHandleId() {
		return handleId;
	}
	public void setHandleId(String handleId) {
		this.handleId = handleId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getButtonText() {
		return buttonText;
	}
	public void setButtonText(String buttonText) {
		this.buttonText = buttonText;
	}
	public Date getActiveTime() {
		return activeTime;
	}
	public void setActiveTime(Date activeTime) {
		this.activeTime = activeTime;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	@Override
	public String toString() {
		return "HandleLog [handleId=" + handleId + ", user=" + user
				+ ", event=" + event + ", buttonText=" + buttonText
				+ ", activeTime=" + activeTime + "]";
	}
	
	
}
