package com.catorv.gallop.database;

import com.catorv.gallop.lifecycle.Destroy;
import com.google.inject.Inject;
import com.google.inject.persist.PersistService;

/**
 * JPA Initializer
 * Created by cator on 8/13/16.
 */
class JpaInitializer {

	private PersistService service;

	@Inject
	public JpaInitializer(PersistService service) throws Exception {
		this.service = service;
		service.start();
	}

	@Destroy
	public void shutdown() throws Exception {
		service.stop();
	}
}

