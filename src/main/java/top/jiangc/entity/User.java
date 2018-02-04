package top.jiangc.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.springframework.stereotype.Component;

@Component("user")
@Entity(name="user_account") // 实体类，利用对象关系映射生成数据库表
public class User {
	
	@Id
	@Column(length=50,name="user_id")
	private String userId;
	
	@Column(nullable=false,length=50,name="user_name",unique=true)
	private String userName;
	
	@Column(length=100,name="nick_name")
	private String nickName;
	
	@Column(nullable=false,length=100,name="password")
	private String password;
	
	@Column(nullable=false,length=50,name="email")
	private String email;
	
	@Column(length=200,name="address")
	private String address;
	
	@Column(nullable=false,name="status")
	private Integer status;
	
	@Column(length=50,name="code")
	private String code;
	
	//创建时间
	@Column(name="create_time", nullable=false)
	private Date createTime;
	
	//修改时间
	@Column(name="update_time",nullable=false)
	private Date updateTime;
	
	//角色id
	@Column(name="role_id",columnDefinition="int default 3",nullable=false)
	private int roleId;
	
	public User() {
		super();
	}
	
	public User(String userId, String userName, String nickName,
			String password, String email, String address, Integer status) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.nickName = nickName;
		this.password = password;
		this.email = email;
		this.address = address;
		this.status = status;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName
				+ ", nickName=" + nickName + ", password=" + password
				+ ", email=" + email + ", address=" + address + ", status="
				+ status + ", code=" + code + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + "]";
	}

	
	
}
