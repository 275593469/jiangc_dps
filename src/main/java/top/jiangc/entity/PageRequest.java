package top.jiangc.entity;

/**
 * 分页查询请求参数
 * @author jiangc
 *
 */
public class PageRequest {

	private int start;//和mysql的分页一样
	private int length;
	private String search;
	
	public PageRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PageRequest(int start, int length, String search) {
		super();
		this.start = start;
		this.length = length;
		this.search = search;
	}
	public int getstart() {
		return start;
	}
	public void setstart(int start) {
		this.start = start;
	}
	public int getlength() {
		return length;
	}
	public void setlength(int length) {
		this.length = length;
	}
	public String getsearch() {
		return search;
	}
	public void setsearch(String search) {
		this.search = search;
	}
	@Override
	public String toString() {
		return "PageRequest [start=" + start + ", length=" + length
				+ ", search=" + search + "]";
	}
	
	
}
