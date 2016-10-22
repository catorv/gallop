package com.catorv.gallop.service;

import java.util.Collection;

/**
 * Abstract Message
 * Created by cator on 8/15/16.
 */
public class PagingMessage<T> extends BasicMessage<PagingModel<T>> {

	public PagingMessage(ServiceException exception, Session session) {
		super(exception, session);
	}

	public PagingMessage(PagingModel<T> content, Session session) {
		super(content, session);
	}

	public PagingMessage(Collection<T> list, int total, Session session) {
		super(new PagingModel<>(list, total), session);
	}

	public PagingMessage(Collection<T> list, Session session) {
		super(new PagingModel<>(list, -1), session);
	}

	public void add(T element) {
		getContent().getList().add(element);
	}

	public void remove(T element) {
		getContent().getList().remove(element);
	}

}
