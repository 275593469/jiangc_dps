package top.jiangc.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.stereotype.Component;

@Component("resource")
@Entity(name="resource")
public class Resource {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="resource_id")
	private Integer resourceId;
	
	@Column(name="resource_name",length=50,nullable=false)
	private String resourceName;
	
	@Column(name="resource_addr",length=50,nullable=false)
	private String resourceAddr;

	public Resource() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Resource(Integer resourceId, String resourceName, String resourceAddr) {
		super();
		this.resourceId = resourceId;
		this.resourceName = resourceName;
		this.resourceAddr = resourceAddr;
	}

	public Integer getResourceId() {
		return resourceId;
	}

	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getResourceAddr() {
		return resourceAddr;
	}

	public void setResourceAddr(String resourceAddr) {
		this.resourceAddr = resourceAddr;
	}

	@Override
	public String toString() {
		return "Resource [resourceId=" + resourceId + ", resourceName="
				+ resourceName + ", resourceAddr=" + resourceAddr + "]";
	}
	
	
}
