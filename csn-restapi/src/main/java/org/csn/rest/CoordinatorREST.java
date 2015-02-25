package org.csn.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.csn.CSNProvider;
import org.csn.component.Coordinator;
import org.csn.component.impl.CoordinatorImpl;
import org.csn.data.CSNConfiguration;
import org.csn.rest.data.JsonKeyName;
import org.csn.util.TimeGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Singleton
@Path("/")
public class CoordinatorREST {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private ObjectMapper mapper = new ObjectMapper();
	private Map<String, Object> jsonDataMap = new HashMap<String, Object>();
	private Map<String, Map<String, Object>> retJsonMap = new HashMap<String, Map<String, Object>>();
	private Coordinator coordinator;
	private CSNConfiguration configuration = null;

	public CoordinatorREST() {
		logger.info("CSN Service API!!!");
		jsonDataMap.put(JsonKeyName.CATEGORY_NAME, "CSN");
		jsonDataMap = new HashMap<String, Object>();
		retJsonMap = new HashMap<String, Map<String, Object>>();
	}

	public CSNConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(CSNConfiguration configuration) {
		this.configuration = configuration;
	}

	public void setCurrentTime() {
		this.configuration.setCreationTime(TimeGenerator.getCurrentTimestamp());
	}

	private void makeResponseData(String method) {
		jsonDataMap.put(JsonKeyName.METHOD_NAME, method);
		jsonDataMap.put(JsonKeyName.SCOPE_NAME, "ALL");
		jsonDataMap.put(JsonKeyName.RESULT_NAME, "OK");
		jsonDataMap.put(JsonKeyName.RET_DATA_NAME, this.getConfiguration());
		retJsonMap.put(JsonKeyName.RET_KEY_NAME, jsonDataMap);
		try {
			logger.info("Data which is sent: {}",
					mapper.writeValueAsString(retJsonMap));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	@Path("/networks")
	public NetworkREST getSensorNetworkAPI() {
		return new NetworkREST(coordinator.getSensorNetworkManager());
	}

	@Path("/tags")
	public TagREST getTagAPI() {
		logger.info("Tag REST Call ...");
		return new TagREST(coordinator);
	}

	@Path("/search")
	public SearchREST getSearchAPI() {
		return new SearchREST();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response setCSNConfigMetadata(@QueryParam("action") String action,
			CSNConfiguration input) {
		if (action != null) {
			logger.info("Input Action: {}", action);
			if (input != null)
				logger.info("Configuration Data: {}", input.toString());
			switch (action) {
			case "restart":
				if (coordinator != null)
					coordinator.terminateCSN();
			case "start":
				if (coordinator == null) {
					this.setConfiguration(input);
					this.setCurrentTime();
					
					coordinator = CSNProvider.getCSNCoreService( input.getCsnName(), getConfiguration());
					
					coordinator = new CoordinatorImpl(input.getCsnName(), getConfiguration());
					
					coordinator.initCSN();
					coordinator.startCSN();
				}
				break;
			case "stop":
				if (coordinator != null) {
					coordinator.terminateCSN();
					coordinator = null;
					this.setConfiguration(null);
				}
			}
			this.makeResponseData("POST");
			return Response.ok(retJsonMap, MediaType.APPLICATION_JSON).build();
		} else {
			this.setConfiguration(input);
			this.setCurrentTime();
			URI createdUri = null;
			try {
				createdUri = new URI("http://128.199.186.224:8080/csn");
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			this.makeResponseData("POST");
			return Response.created(createdUri).entity(retJsonMap)
					.type(MediaType.APPLICATION_JSON).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCSNConfigMetadata() {
		logger.info("[GET] CSN Configuration");
		if (coordinator != null)
			configuration = coordinator.getCsnConfiguration();

		if (this.getConfiguration() == null)
			throw new NotFoundException();
		else {
			try {
				logger.info("Data which is sent: {}",
						mapper.writeValueAsString(this.getConfiguration()));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			return Response.ok(this.getConfiguration(),
					MediaType.APPLICATION_JSON).build();
		}
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeCSNConfigMetadata() {
		if (coordinator != null) {
			coordinator.terminateCSN();
			coordinator = null;
		}
		this.makeResponseData("DELETE");
		this.setConfiguration(null);
		return Response.ok(retJsonMap, MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Path("/status")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCSNStatus() {
		Map<String, Object> jsonDataMap = new HashMap<String, Object>();

		if (coordinator != null && coordinator.isWorking() == true)
			jsonDataMap.put("working", "OK");
		else
			jsonDataMap.put("working", "FAIL");

		try {
			logger.info("Data which is sent: {}",
					mapper.writeValueAsString(jsonDataMap));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return Response.ok(jsonDataMap, MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Path("/broker/status")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBrokerStatus() {
		Map<String, Object> jsonDataMap = new HashMap<String, Object>();
		jsonDataMap.put("status", coordinator.getMessageQueueManager()
				.getCurrentHealthStatus());
		jsonDataMap.put("inNum", coordinator.getMessageQueueManager()
				.getTotalMSGEnqueueCount());
		jsonDataMap.put("outNum", coordinator.getMessageQueueManager()
				.getTotalMSGDequeueCount());
		jsonDataMap.put("proNum", coordinator.getMessageQueueManager()
				.getTotalProducerCount());
		jsonDataMap.put("conNum", coordinator.getMessageQueueManager()
				.getTotalConsumerCount());
		jsonDataMap.put("storage", coordinator.getMessageQueueManager()
				.getStoreUsagePercentage());
		jsonDataMap.put("mem", coordinator.getMessageQueueManager()
				.getMemoryUsagePercentage());

		try {
			logger.info("Data which is sent: {}",
					mapper.writeValueAsString(jsonDataMap));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return Response.ok(jsonDataMap, MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Path("/broker/topics")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBrokerMessageNum() {
		Map<String, Set<Map<String, Object>>> retMap = new HashMap<>();
		retMap.put("data", coordinator.getMessageQueueManager()
				.getAllTopicStatus());
		try {
			logger.info("Data which is sent: {}",
					mapper.writeValueAsString(retMap));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return Response.ok(retMap, MediaType.APPLICATION_JSON).build();
	}
}
