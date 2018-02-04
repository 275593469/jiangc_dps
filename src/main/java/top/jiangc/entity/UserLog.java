package top.jiangc.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.stereotype.Component;

/**
 * 用户相关的操作日志
 *@author jiangcheng
 *
 *2017年3月31日
 */
@Entity(name="user_log")
@Component("userLog")
public class UserLog {

	@Id
	@Column(name="user_log_id",length=50)
	private String userLogId;
	
	//用户id
	@ManyToOne(targetEntity=User.class,optional=true,cascade=CascadeType.ALL)
	@JoinColumn(name="user_id",nullable=false)
	private User user;
	
	//用户操作描述
	@Column(name="operation",length=100)
	private String operation;
	
	//操作结果
	@Column(name="result",length=2)
	private Integer result;
	
	//请求IP
	@Column(name="ip",length=50)
	private String ip;
	
	//操作时间
	@Column(name="operation_time")
	private Date operationTime;

	public UserLog() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getUserLogId() {
		return userLogId;
	}

	public void setUserLogId(String userLogId) {
		this.userLogId = userLogId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Date getOperationTime() {
		return operationTime;
	}

	public void setOperationTime(Date operationTime) {
		this.operationTime = operationTime;
	}

	@Override
	public String toString() {
		return "UserLog [userLogId=" + userLogId + ", user=" + user
				+ ", operation=" + operation + ", result=" + result + ", ip="
				+ ip + ", operationTime=" + operationTime + "]";
	}
	
	
}
