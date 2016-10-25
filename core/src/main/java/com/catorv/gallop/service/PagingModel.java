package com.catorv.gallop.service;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by cator on 22/10/2016.
 */
public class PagingModel<T> {

	private Collection<T> list;
	private int total;

	public PagingModel() {
		this(new ArrayList<T>());
	}

	public PagingModel(Collection<T> list, int total) {
		Preconditions.checkNotNull(list);
		this.list = list;
		this.total = total;
	}

	public PagingModel(Collection<T> list) {
		this.list = list;
		this.total = -1;
	}

	public Collection<T> getList() {
		return list;
	}

	public void setList(Collection<T> list) {
		this.list = list;
	}

	public int getTotal() {
		return total >= 0 ? total : list.size();
	}

	public void setTotal(int total) {
		this.total = total;
	}
}
