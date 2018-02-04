package top.jiangc.entity;

import java.util.List;

/**
 * 分页工具类
 * @author jiangc
 *
 * @param <T>
 */
public class Page<T> {

	private List<T> lstEntity;
	private long pageCount;//总记录数
	private int pageIndex;//起始页，从第一页开始算起
	private int pageSize;//页面大小
	private String searchValue;//查询关键字
	
	public Page() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Page(List<T> lstEntity, long pageCount, int pageIndex, int pageSize,
			String searchValue) {
		super();
		this.lstEntity = lstEntity;
		this.pageCount = pageCount;
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
		this.searchValue = searchValue;
	}
	public List<T> getLstEntity() {
		return lstEntity;
	}
	public void setLstEntity(List<T> lstEntity) {
		this.lstEntity = lstEntity;
	}
	public long getPageCount() {
		return pageCount;
	}
	public void setPageCount(long pageCount) {
		this.pageCount = pageCount;
	}
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public String getSearchValue() {
		return searchValue;
	}
	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}
	@Override
	public String toString() {
		return "Page [lstEntity=" + lstEntity + ", pageCount=" + pageCount
				+ ", pageIndex=" + pageIndex + ", pageSize=" + pageSize
				+ ", searchValue=" + searchValue + "]";
	}
	
	
	
}
