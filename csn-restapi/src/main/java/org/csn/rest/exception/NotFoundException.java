package org.csn.rest.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class NotFoundException extends WebApplicationException {
	private static final long serialVersionUID = -423529454250636704L;

	/**
     * Create a HTTP 404 (Not Found) exception.
     */
    public NotFoundException() {
        super(Response.status(Status.NOT_FOUND).build());
    }

    public NotFoundException(String message) {
        super(Response.status(Status.NOT_FOUND).entity(message).type("text/plain").build());
    }

}
