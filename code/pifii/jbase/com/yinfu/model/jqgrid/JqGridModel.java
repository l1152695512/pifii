
package com.yinfu.model.jqgrid;

import java.io.Serializable;
import java.util.List;

public class JqGridModel<M> implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long page;//当前页数
	private Long total;//总页数
	private Long records;//总记录数
	private List<M> rows;//列内容
	
	
	public Long getPage() {
		return page;
	}

	public void setPage(Long page) {
		this.page = page;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Long getRecords() {
		return records;
	}

	public void setRecords(Long records) {
		this.records = records;
	}

	public List<M> getRows() {
		return rows;
	}

	public void setRows(List<M> rows) {
		this.rows = rows;
	}

	public JqGridModel() {
	}
	
	public JqGridModel(long page, long total, long records, List<M> rows) {
		super();
		this.page = page;
		this.total = total;
		this.records = records;
		this.rows = rows;
	}
}
