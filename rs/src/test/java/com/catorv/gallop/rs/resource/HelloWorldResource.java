package com.catorv.gallop.rs.resource;

import com.google.common.base.Strings;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 *
 * Created by cator on 8/16/16.
 */
@Path("helloworld")
public class HelloWorldResource {
	public static final String CLICHED_MESSAGE = "Hello World!\n";

	@QueryParam("name")
	private String name = "Cator Vee";

	@Inject
	private Logger logger;

	@Inject
	private GuiceService guiceService;

	@GET
	@Produces("text/plain")
	public String getHello() {
		if (Strings.isNullOrEmpty(name)) {
			return CLICHED_MESSAGE;
		}
		return "Hello " + name + "!\n";
	}

	@Path("useragent")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getUserAgent(
			@HeaderParam("user-agent") String userAgent
	) {
		return userAgent + "\n";
	}

	@Path("json")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Bean getJson() {
		return new Bean();
	}

	@Path("value")
	@GET
	public String getValue() {
		logger.debug("hhhhh");
		return guiceService.getNamedValue();
	}

}

