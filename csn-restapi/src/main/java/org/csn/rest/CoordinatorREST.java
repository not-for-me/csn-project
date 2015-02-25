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

import org.csn.component.Coordinator;
import org.csn.data.CSNConfiguration;
import org.csn.rest.data.JsonKeyName;
import org.csn.util.LogPrinter;
import org.csn.util.TimeGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Singleton
@Path("/")
public class CoordinatorREST {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CoordinatorREST.class.getClass());
	private ObjectMapper mapper = new ObjectMapper();
	private Coordinator coordinator;
	private CSNConfiguration configuration = null;

	public CoordinatorREST() {
		LOGGER.info("CSN Service API!!!");
	}

	public void setCurrentTime() {
		this.configuration.setCreationTime(TimeGenerator.getCurrentTimestamp());
	}

	private Map<String, Map<String, Object>> makeConfigurationResponse(
			String method) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put(JsonKeyName.METHOD_NAME, method);
		dataMap.put(JsonKeyName.SCOPE_NAME, "ALL");
		dataMap.put(JsonKeyName.RESULT_NAME, "OK");
		dataMap.put(JsonKeyName.RET_DATA_NAME, configuration);

		Map<String, Map<String, Object>> finalReturnMap = new HashMap<String, Map<String, Object>>();
		finalReturnMap.put(JsonKeyName.RET_KEY_NAME, dataMap);
		try {
			LOGGER.info("Data which is sent: {}",
					mapper.writeValueAsString(finalReturnMap));
		} catch (JsonProcessingException e) {
			LogPrinter.printErrorLog(LOGGER, e.getClass().toString(),
					e.getMessage());
		}
		return finalReturnMap;
	}

	@Path("/networks")
	public NetworkREST getSensorNetworkAPI() {
		return new NetworkREST(coordinator.getSensorNetworkManager());
	}

	@Path("/tags")
	public TagREST getTagAPI() {
		LOGGER.info("Tag REST Call ...");
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
			final CSNConfiguration inputConfig) {
		if (action != null) {
			LOGGER.info("Input Action: {}", action);
			if (inputConfig != null)
				LOGGER.info("Configuration Data: {}", inputConfig.toString());
			switch (action) {
			case "restart":
				if (coordinator != null)
					coordinator.terminateCSN();
			case "start":
				if (coordinator == null) {
					configuration = inputConfig;
					this.setCurrentTime();

					coordinator = new Coordinator(inputConfig);

					coordinator.initCSN();
					coordinator.startCSN();
				}
				break;
			case "stop":
				if (coordinator != null) {
					coordinator.terminateCSN();
					coordinator = null;
					configuration = null;
				}
			}
			return Response.ok(makeConfigurationResponse("POST"),
					MediaType.APPLICATION_JSON).build();
		} else {
			configuration = inputConfig;
			this.setCurrentTime();
			URI createdUri = null;
			try {
				createdUri = new URI("http://128.199.186.224:8080/csn");
			} catch (URISyntaxException e) {
				LogPrinter.printErrorLog(LOGGER, e.getClass().toString(),
						e.getMessage());
			}
			return Response.created(createdUri)
					.entity(makeConfigurationResponse("POST"))
					.type(MediaType.APPLICATION_JSON).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCSNConfigMetadata() {
		LOGGER.info("[GET] CSN Configuration");
		if (coordinator != null)
			configuration = coordinator.getCsnConfiguration();

		if (configuration == null)
			throw new NotFoundException();
		else {
			try {
				LOGGER.info("Data which is sent: {}",
						mapper.writeValueAsString(configuration));
			} catch (JsonProcessingException e) {
				LogPrinter.printErrorLog(LOGGER, e.getClass().toString(),
						e.getMessage());
			}
			return Response.ok(configuration, MediaType.APPLICATION_JSON)
					.build();
		}
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeCSNConfigMetadata() {
		if (coordinator.isRunning()) {
			coordinator.terminateCSN();
			configuration = null;
		}
		return Response.ok(makeConfigurationResponse("DELETE"),
				MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Path("/status")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCSNStatus() {
		Map<String, Object> jsonDataMap = new HashMap<String, Object>();

		if (coordinator != null && coordinator.isRunning() == true)
			jsonDataMap.put("working", "OK");
		else
			jsonDataMap.put("working", "FAIL");

		try {
			LOGGER.info("Data which is sent: {}",
					mapper.writeValueAsString(jsonDataMap));
		} catch (JsonProcessingException e) {
			LogPrinter.printErrorLog(LOGGER, e.getClass().toString(),
					e.getMessage());
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
			LOGGER.info("Data which is sent: {}",
					mapper.writeValueAsString(jsonDataMap));
		} catch (JsonProcessingException e) {
			LogPrinter.printErrorLog(LOGGER, e.getClass().toString(),
					e.getMessage());
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
			LOGGER.info("Data which is sent: {}",
					mapper.writeValueAsString(retMap));
		} catch (JsonProcessingException e) {
			LogPrinter.printErrorLog(LOGGER, e.getClass().toString(),
					e.getMessage());
		}
		return Response.ok(retMap, MediaType.APPLICATION_JSON).build();
	}
}
