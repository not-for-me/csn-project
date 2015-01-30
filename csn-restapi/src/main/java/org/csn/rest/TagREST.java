package org.csn.rest;

import org.csn.component.Coordinator;
import org.csn.data.ReturnType;
import org.csn.rest.data.JsonKeyName;
import org.csn.rest.data.TagObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TagREST {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    ObjectMapper mapper = new ObjectMapper();
    Map<String, Object> jsonDataMap = new HashMap<String, Object>();
    Map<String, Map<String, Object>> retJsonMap = new HashMap<String, Map<String, Object>>();
    private Coordinator coordinator;

    public TagREST(Coordinator coordinator) {
        this.coordinator = coordinator;
        jsonDataMap.put(JsonKeyName.CATEGORY_NAME,"Tag");
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createConcepts(TagObject tag) {
        logger.info(tag.toString());
        logger.info("Input Tag: {}", tag );
        ReturnType retType = coordinator.getSensorNetworkManager().createTag(tag.getTag());

        jsonDataMap.put(JsonKeyName.METHOD_NAME,"POST");
        jsonDataMap.put(JsonKeyName.SCOPE_NAME,"ONE");

        if(retType == ReturnType.Done) {
            jsonDataMap.put(JsonKeyName.RESULT_NAME, "OK");
//            SensorNetwork network = coordinator.getSensorNetworkManager().getNetwork(id);
            jsonDataMap.put(JsonKeyName.RET_DATA_NAME, null);
        }
        else {
            jsonDataMap.put(JsonKeyName.RESULT_NAME, "FAIL");
            jsonDataMap.put(JsonKeyName.RET_DATA_NAME, null);
        }

        retJsonMap.put(JsonKeyName.RET_KEY_NAME, jsonDataMap);
        try { logger.info("Data which is sent: {}", mapper.writeValueAsString(retJsonMap)); } catch (JsonProcessingException e) { e.printStackTrace(); }
        return Response.ok(retJsonMap, MediaType.APPLICATION_JSON).build();
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllConcepts() {
        Set<String> tagSet = coordinator.getSensorNetworkManager().getAllTags();
        if(tagSet != null) {
            try { logger.info("Data which is sent: {}", mapper.writeValueAsString(tagSet)); } catch (JsonProcessingException e) { e.printStackTrace(); }
            return Response.ok(tagSet, MediaType.APPLICATION_JSON).build();
        }
        else
            throw new org.csn.rest.exception.NotFoundException();
    }

    @POST
    @Path("/{tag}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response annotateTags(@PathParam("tag") String tag, Set<String> idSet) {
        logger.info(tag.toString());
        logger.info("Input Tag: {}", tag );
        logger.info("ID Set: {}", idSet);
        ReturnType retType = coordinator.getSensorNetworkManager().addTags(tag, idSet);

        jsonDataMap.put(JsonKeyName.METHOD_NAME,"POST");
        jsonDataMap.put(JsonKeyName.SCOPE_NAME,"ONE");

        if(retType == ReturnType.Done) {
            jsonDataMap.put(JsonKeyName.RESULT_NAME, "OK");
            jsonDataMap.put(JsonKeyName.RET_DATA_NAME, null);
        }
        else {
            jsonDataMap.put(JsonKeyName.RESULT_NAME, "FAIL");
            jsonDataMap.put(JsonKeyName.RET_DATA_NAME, null);
        }

        retJsonMap.put(JsonKeyName.RET_KEY_NAME, jsonDataMap);
        try { logger.info("Data which is sent: {}", mapper.writeValueAsString(retJsonMap)); } catch (JsonProcessingException e) { e.printStackTrace(); }
        return Response.ok(retJsonMap, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/{tag}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response annotateTags(@PathParam("tag") String tag) {
        logger.info(tag.toString());
        logger.info("Input Tag: {}", tag );
        String id = coordinator.getSensorNetworkManager().getTagID(tag);
        Set<String> idSet = coordinator.getSensorNetworkManager().searchNetworkWithTag(tag);
        jsonDataMap.put(JsonKeyName.METHOD_NAME,"POST");
        jsonDataMap.put(JsonKeyName.SCOPE_NAME,"ONE");

        if(idSet != null) {
            jsonDataMap.put(JsonKeyName.RESULT_NAME, "OK");
            jsonDataMap.put(JsonKeyName.RET_DATA_NAME, idSet);
        }
        else {
            jsonDataMap.put(JsonKeyName.RESULT_NAME, "FAIL");
            jsonDataMap.put(JsonKeyName.RET_DATA_NAME, null);
        }

        retJsonMap.put(JsonKeyName.RET_KEY_NAME, jsonDataMap);
        try { logger.info("Data which is sent: {}", mapper.writeValueAsString(retJsonMap)); } catch (JsonProcessingException e) { e.printStackTrace(); }
        return Response.ok(retJsonMap, MediaType.APPLICATION_JSON).build();
    }

//    @DELETE
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response deleteTag(TagObject tag) {
//        logger.info(tag.toString());
//        logger.info("Input Tag: {}", tag );
//        Set<String> tagSet = coordinator.getSensorNetworkManager().re
//        if(tagSet != null) {
//            try { logger.info("Data which is sent: {}", mapper.writeValueAsString(tagSet)); } catch (JsonProcessingException e) { e.printStackTrace(); }
//            return Response.ok(tagSet, MediaType.APPLICATION_JSON).build();
//        }
//        else
//            throw new cir.lab.exception.NotFoundException();
//    }

}
