package org.csn.rest;

import org.csn.component.Coordinator;
import org.csn.data.SensorData;
import org.csn.data.SensorNetwork;
import org.csn.db.CSNDAOFactory;
import org.csn.db.dao.SearchDAO;
import org.csn.rest.data.JsonKeyName;
import org.csn.rest.exception.NotFoundException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SearchREST {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    ObjectMapper mapper = new ObjectMapper();
    SearchDAO searchDAO;

    public SearchREST() {
        searchDAO = new CSNDAOFactory().searchDAO();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchNetworksWithConcepts(@QueryParam("target") String target, Set<String> tags) {
        logger.info("Network Searching with Tags\n, {} ", tags.toString());
        Map<String, Set<String>> retMap = new HashMap<String, Set<String>>();
		
        Set<String> networks = searchDAO.searchNetworkWithConcepts(tags);
        retMap.put("searchResult", networks);
        try { logger.info("Data which is sent: {}", mapper.writeValueAsString(retMap)); } catch (JsonProcessingException e) { e.printStackTrace(); }
        return Response.ok(retMap, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchSensorData(
            @QueryParam("target") String target,
            @QueryParam("from") String startDate,
            @QueryParam("to") String endDate,
            @QueryParam("unit") String unit,
            @QueryParam("duration") int duration,
            @QueryParam("num") int num ) {
        List dataList = null;
        if(endDate != null) {
            startDate = startDate.replace("T", " ");
            endDate = endDate.replace("T", " ");
            logger.info("Searching Sensor Data...\nfrom\t{}\nuntil\t{}", startDate, endDate );
            dataList = searchDAO.searchSensorData(startDate, endDate);
        }
        else if(unit != null && duration > 0) {
            startDate = startDate.replace("T", " ");
            logger.info("Searching Sensor Data From :{} \n {} {}", startDate, duration, unit);
            dataList = searchDAO.searchSensorData(startDate, unit, duration);
        }
        else {
            logger.info("Most Recent {} Sensor Data ...");
            dataList = searchDAO.searchSensorData(num);
        }

        if(dataList != null) {
            try { logger.info("Data which is sent: {}", mapper.writeValueAsString(dataList)); } catch (JsonProcessingException e) { e.printStackTrace(); }
            return Response.ok(dataList, MediaType.APPLICATION_JSON).build();
        }
        else
            throw new NotFoundException();
    }

}
