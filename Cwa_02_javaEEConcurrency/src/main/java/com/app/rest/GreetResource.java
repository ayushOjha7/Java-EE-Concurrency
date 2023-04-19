package com.app.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/greet")
public class GreetResource {
	
	@GET
	public String greet() {
		
		return "Java EE Concurrency begins";
	}
}
