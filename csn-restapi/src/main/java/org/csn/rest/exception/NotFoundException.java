package org.csn.rest.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * Not Found Exception
 * @author NFM
 */
public class NotFoundException extends WebApplicationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6754530688708358128L;

	/**
     * Create a HTTP 404 (Not Found) exception.
     */
    public NotFoundException() {
        super(Response.status(Status.NOT_FOUND).build());
    }
    
    /**
     * Create a HTTP 404 (Not Found) exception with message
     * @param message exception message
     */
    public NotFoundException(final String message) {
        super(Response.status(Status.NOT_FOUND).entity(message).type("text/plain").build());
    }

}
