package org.csn.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.csn.component.SensorNetworkManager;
import org.csn.data.ReturnType;
import org.csn.data.SensorNetwork;
import org.csn.rest.data.JsonKeyName;
import org.csn.rest.data.NetworkSeed;
import org.csn.rest.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NetworkREST {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	ObjectMapper mapper = new ObjectMapper();
	Map<String, Object> jsonDataMap = new HashMap<String, Object>();
	Map<String, Map<String, Object>> retJsonMap = new HashMap<String, Map<String, Object>>();
	private SensorNetworkManager sensorNetworkManger;

	public NetworkREST(SensorNetworkManager sensorNetworkManger) {
		this.sensorNetworkManger = sensorNetworkManger;
		jsonDataMap.put(JsonKeyName.CATEGORY_NAME, "Network");
	}

	// {"name":"TestNet3"}
	// {"name":"TestNet3", "member":["31","32","33"]}
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createNetwork(NetworkSeed input) {
		logger.info(input.toString());

		String id = sensorNetworkManger.registerNetwork(input.getName()
				.replaceAll("\\p{Space}", "").replace(".", "_"),
				input.getMembers(), input.getMetadata(), input.getTags());
		jsonDataMap.put(JsonKeyName.METHOD_NAME, "POST");
		jsonDataMap.put(JsonKeyName.SCOPE_NAME, "ONE");
		jsonDataMap.put(JsonKeyName.RET_DATA_NAME, id);
		if (id != null) {
			jsonDataMap.put(JsonKeyName.RESULT_NAME, "OK");
			SensorNetwork metadata = sensorNetworkManger.getNetwork(id);
			jsonDataMap.put(JsonKeyName.RET_DATA_NAME, metadata);
		} else {
			jsonDataMap.put(JsonKeyName.RESULT_NAME, "FAIL");
			jsonDataMap.put(JsonKeyName.RET_DATA_NAME, null);
		}

		retJsonMap.put(JsonKeyName.RET_KEY_NAME, jsonDataMap);
		try {
			logger.info("Data which is sent: {}",
					mapper.writeValueAsString(retJsonMap));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return Response.ok(retJsonMap, MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNetworkInfo(@QueryParam("select") String select,
			@QueryParam("index") int index, @QueryParam("num") int num) {
		if (select == null) {
			logger.info("Get All Network Data");
			Set<SensorNetwork> networkSet = null;
			logger.info("Index: {}, Num: {}", index, num);

			if (num > 0)
				networkSet = sensorNetworkManger
						.getNetworkResources(index, num);

			else
				networkSet = sensorNetworkManger.getAllNetworkResources();

			if (networkSet != null) {
				Map<String, Set<SensorNetwork>> retMap = new HashMap<String, Set<SensorNetwork>>();
				retMap.put("networks", networkSet);
				try {
					logger.info("Data which is sent: {}",
							mapper.writeValueAsString(retMap));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				return Response.ok(retMap, MediaType.APPLICATION_JSON).build();
			}
		} else {
			logger.info("Network Data Selection: {} ", select);
			switch (select) {
			case "ids":
			case "topics":
				Set<String> set = null;
				logger.info("Index: {}, Num: {}", index, num);

				if (num > 0)
					set = (select.equals("ids")) ? sensorNetworkManger
							.getNetworkIDs(index, num) : sensorNetworkManger
							.getNetworkTopicPaths(index, num);

				else
					set = (select.equals("ids")) ? sensorNetworkManger
							.getAllNetworkIDs() : sensorNetworkManger
							.getAllNetworkTopicPaths();

				if (set != null) {
					Map<String, Set<String>> retMap = new HashMap<String, Set<String>>();
					retMap.put("ids", set);
					try {
						logger.info("Data which is sent: {}",
								mapper.writeValueAsString(retMap));
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}
					return Response.ok(retMap, MediaType.APPLICATION_JSON)
							.build();
				}
			case "members":
				logger.info("Network Members API");
				Map<String, Set<String>> membersMap = sensorNetworkManger
						.getAllNetworkAndMemberIDs();
				if (membersMap != null) {
					try {
						logger.info("Data which is sent: {}",
								mapper.writeValueAsString(membersMap));
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}
					return Response.ok(membersMap, MediaType.APPLICATION_JSON)
							.build();
				}
			case "counts":
				logger.info("Network Count API");
				int totalNetworkCNT = sensorNetworkManger.getAllNetworkCount();
				int operatingNetworkCNT = sensorNetworkManger
						.getOperatingNetworkCount();
				int stoppedNetworkCNT = sensorNetworkManger
						.getStoppedNetworkCount();
				int faultedNetworkCNT = sensorNetworkManger
						.getFaultedNetworkCount();

				if (totalNetworkCNT > -1) {
					Map<String, Integer> dataMap = new HashMap<String, Integer>();
					dataMap.put("totalCNT", totalNetworkCNT);
					dataMap.put("operatingCNT", operatingNetworkCNT);
					dataMap.put("stoppedCNT", stoppedNetworkCNT);
					dataMap.put("faultedCNT", faultedNetworkCNT);
					try {
						logger.info("Data which is sent: {}",
								mapper.writeValueAsString(dataMap));
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}
					return Response.ok(dataMap, MediaType.APPLICATION_JSON)
							.build();
				}
			}
		}

		// When Data is null
		jsonDataMap.put(JsonKeyName.METHOD_NAME, "GET");
		jsonDataMap.put(JsonKeyName.SCOPE_NAME, "ALL");
		jsonDataMap.put(JsonKeyName.RET_DATA_NAME, null);
		jsonDataMap.put(JsonKeyName.RESULT_NAME, "FAIL");
		try {
			logger.info("Data which is sent: {}",
					mapper.writeValueAsString(retJsonMap));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return Response.ok(retJsonMap, MediaType.APPLICATION_JSON).build();
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeAllNetwork() {
		ReturnType retType = sensorNetworkManger.deactivateAllNetworks();
		jsonDataMap.put(JsonKeyName.METHOD_NAME, "DELETE");
		jsonDataMap.put(JsonKeyName.SCOPE_NAME, "ALL");

		if (retType == ReturnType.Done)
			jsonDataMap.put(JsonKeyName.RESULT_NAME, "OK");
		else
			jsonDataMap.put(JsonKeyName.RESULT_NAME, "FAIL");

		jsonDataMap.put(JsonKeyName.RET_DATA_NAME, null);
		retJsonMap.put(JsonKeyName.RET_KEY_NAME, jsonDataMap);
		try {
			logger.info("Data which is sent: {}",
					mapper.writeValueAsString(retJsonMap));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return Response.ok(retJsonMap, MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNetwork(@PathParam("id") String id) {
		logger.info("Input  Network ID: {}", id);
		SensorNetwork network = sensorNetworkManger.getNetwork(id);
		if (network != null) {
			try {
				logger.info("Data which is sent: {}",
						mapper.writeValueAsString(network));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			return Response.ok(network, MediaType.APPLICATION_JSON).build();
		} else
			throw new NotFoundException();
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeNetwork(@PathParam("id") String id) {
		logger.info("Input  Network ID: {}", id);
		SensorNetwork network = sensorNetworkManger.getNetwork(id);
		jsonDataMap.put(JsonKeyName.RET_DATA_NAME, network);

		ReturnType retType = sensorNetworkManger.removeNetwork(id);
		jsonDataMap.put(JsonKeyName.METHOD_NAME, "DELETE");
		jsonDataMap.put(JsonKeyName.SCOPE_NAME, "ALL");

		if (retType == ReturnType.Done)
			jsonDataMap.put(JsonKeyName.RESULT_NAME, "OK");
		else
			jsonDataMap.put(JsonKeyName.RESULT_NAME, "FAIL");

		retJsonMap.put(JsonKeyName.RET_KEY_NAME, jsonDataMap);
		try {
			logger.info("Data which is sent: {}",
					mapper.writeValueAsString(retJsonMap));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return Response.ok(retJsonMap, MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Path("/{id}/parents")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getParents(@PathParam("id") String id,
			@QueryParam("select") String select) {
		logger.info("Input Network ID: {}", id);
		Set set = null;

		if (select == null) {
			logger.info("Get Parent Network IDs");
			set = sensorNetworkManger.getParentNetworks(id);
		} else {
			logger.info("Get Parent Network Topic Paths");
			set = sensorNetworkManger.getParentNetworkTopicPaths(id);
		}

		if (set != null) {
			try {
				logger.info("Data which is sent: {}",
						mapper.writeValueAsString(set));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			return Response.ok(set, MediaType.APPLICATION_JSON).build();
		} else
			throw new NotFoundException();
	}

	@GET
	@Path("/{id}/members")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMemberIDs(@PathParam("id") String id) {
		logger.info("Input  Network ID: {}", id);
		Set<String> memberSet = sensorNetworkManger.getMemberIDs(id);
		if (memberSet != null) {
			try {
				logger.info("Data which is sent: {}",
						mapper.writeValueAsString(memberSet));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			return Response.ok(memberSet, MediaType.APPLICATION_JSON).build();
		} else
			throw new NotFoundException();
	}

	@GET
	@Path("/{id}/topic")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTopicPath(@PathParam("id") String id) {
		logger.info("Input  Network ID: {}", id);
		String topicName = sensorNetworkManger.getTopicPath(id);
		if (topicName != null) {
			String input = "{\"topic_name\":\"" + topicName + "\"}";
			logger.info("Data which is sent: {}", input);
			return Response.ok(input, MediaType.APPLICATION_JSON).build();
		} else
			throw new NotFoundException();
	}

	@POST
	@Path("/{id}/tags")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addConcepts(Set<String> input, @PathParam("id") String id) {
		logger.info(input.toString());
		logger.info("Input  Network ID: {}", id);
		ReturnType retType = sensorNetworkManger.addTags(input, id);

		jsonDataMap.put(JsonKeyName.METHOD_NAME, "POST");
		jsonDataMap.put(JsonKeyName.SCOPE_NAME, "ONE");

		if (retType == ReturnType.Done) {
			jsonDataMap.put(JsonKeyName.RESULT_NAME, "OK");
			SensorNetwork network = sensorNetworkManger.getNetwork(id);
			jsonDataMap.put(JsonKeyName.RET_DATA_NAME, network);
		} else {
			jsonDataMap.put(JsonKeyName.RESULT_NAME, "FAIL");
			jsonDataMap.put(JsonKeyName.RET_DATA_NAME, null);
		}

		retJsonMap.put(JsonKeyName.RET_KEY_NAME, jsonDataMap);
		try {
			logger.info("Data which is sent: {}",
					mapper.writeValueAsString(retJsonMap));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return Response.ok(retJsonMap, MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Path("/{id}/tags")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllConcepts(@PathParam("id") String id) {
		logger.info("Input  Network ID: {}", id);
		Set<String> tagSet = sensorNetworkManger.getAllTags(id);
		if (tagSet != null) {
			try {
				logger.info("Data which is sent: {}",
						mapper.writeValueAsString(tagSet));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			return Response.ok(tagSet, MediaType.APPLICATION_JSON).build();
		} else
			throw new NotFoundException();
	}

	@DELETE
	@Path("/{id}/tags")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeNetworkAllTag(@PathParam("id") String id) {
		logger.info("Input  Network ID: {}", id);
		ReturnType retType = sensorNetworkManger.removeAllTags(id);
		jsonDataMap.put(JsonKeyName.METHOD_NAME, "DELETE");
		jsonDataMap.put(JsonKeyName.SCOPE_NAME, "ONE");

		if (retType == ReturnType.Done) {
			jsonDataMap.put(JsonKeyName.RESULT_NAME, "OK");
			SensorNetwork network = sensorNetworkManger.getNetwork(id);
			jsonDataMap.put(JsonKeyName.RET_DATA_NAME, network);
		} else {
			jsonDataMap.put(JsonKeyName.RESULT_NAME, "FAIL");
			jsonDataMap.put(JsonKeyName.RET_DATA_NAME, null);
		}

		retJsonMap.put(JsonKeyName.RET_KEY_NAME, jsonDataMap);
		try {
			logger.info("Data which is sent: {}",
					mapper.writeValueAsString(retJsonMap));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return Response.ok(retJsonMap, MediaType.APPLICATION_JSON).build();
	}

	@DELETE
	@Path("/{id}/tags/{tag}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeNetworkTag(@PathParam("id") String id,
			@PathParam("tag") String tag) {
		logger.info("Input  Network ID: {}", id);
		logger.info("Input tag: {}", tag);

		ReturnType retType = sensorNetworkManger.removeTag(tag, id);

		jsonDataMap.put(JsonKeyName.METHOD_NAME, "DELETE");
		jsonDataMap.put(JsonKeyName.SCOPE_NAME, "ONE");

		if (retType == ReturnType.Done) {
			jsonDataMap.put(JsonKeyName.RESULT_NAME, "OK");
			SensorNetwork network = sensorNetworkManger.getNetwork(id);
			jsonDataMap.put(JsonKeyName.RET_DATA_NAME, network);
		} else {
			jsonDataMap.put(JsonKeyName.RESULT_NAME, "FAIL");
			jsonDataMap.put(JsonKeyName.RET_DATA_NAME, null);
		}

		retJsonMap.put(JsonKeyName.RET_KEY_NAME, jsonDataMap);
		try {
			logger.info("Data which is sent: {}",
					mapper.writeValueAsString(retJsonMap));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return Response.ok(retJsonMap, MediaType.APPLICATION_JSON).build();
	}
}
