package org.csn.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


public class HTTPRequestMethod {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private Client client = ClientBuilder.newClient();
    //private Invocation.Builder invocationBuilder;
    private Response response;
    private String output;

//    public void setUpREST(String url, String input, HttpMethodType method ) {
//        logger.info("Input URL: {}", url);
//        webTarget = client.target(url);
//
//        switch (method) {
//            case POST:
//            case PUT:
//                logger.info("Input Data: {}", input);
//
//                break;
//            case GET:
//            case DELETE:
//                break;
//            default:
//                break;
//        }
//    }

//    public String tearDownREST(HttpMethodType method) {
//        switch (method) {
//            case POST:
//            case PUT:
//                if (!(response.getStatus() == 200 || response.getStatus() == 201))
//                    throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
//                break;
//            case GET:
//            case DELETE:
//                if (response.getStatus() != 200)
//                    throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
//                break;
//            default:
//                break;
//        }
//
//        output = response.readEntity(String.class);
//        logger.info(output);
//        return output;
//    }

    public String postMethod(String url, String input) {
        logger.info("Input Data: {}", input);
        response = client.target(url).request().accept(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(input, MediaType.APPLICATION_JSON), Response.class);
        output = response.readEntity(String.class);
        logger.info("Output Data: {}", output);
        return output;
    }


    public String getMethod(String url) {
        response = client.target(url).request().accept(MediaType.APPLICATION_JSON_TYPE).get();
        output = response.readEntity(String.class);
        logger.info("Output Data: {}", output);
        return output;
    }

    public String putMethod(String url, String input) {
        logger.info("Input Data: {}", input);
        response = client.target(url).request().accept(MediaType.APPLICATION_JSON_TYPE).put(Entity.entity(input, MediaType.APPLICATION_JSON), Response.class);
        output = response.readEntity(String.class);
        logger.info("Output Data: {}", output);
        return output;
    }

    public String deleteMethod(String url) {
        response = client.target(url).request().accept(MediaType.APPLICATION_JSON_TYPE).delete();
        output = response.readEntity(String.class);
        logger.info("Output Data: {}", output);
        return output;
    }

}
