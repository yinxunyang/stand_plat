package com.bestvike.portal.data;

import javax.persistence.Transient;

public class BaseData {

	@Transient
	private String fuzzy;
	@Transient
	private int page;
	@Transient
	private int limit;
	@Transient
	private String sort;

	public String getFuzzy() {
		return fuzzy;
	}

	public void setFuzzy(String fuzzy) {
		this.fuzzy = fuzzy;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
}
