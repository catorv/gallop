package com.catorv.test.gallop.rs;

import com.catorv.gallop.rs.netty.NettyHttpServerRunner;

/**
 * Created by cator on 8/22/16.
 */
public class GuiceNettyHttpServerRunner extends NettyHttpServerRunner {

	@Override
	protected void configure() {
		resourceConfig.packages("com.catorv.gallop.rs.resource");
	}

}
