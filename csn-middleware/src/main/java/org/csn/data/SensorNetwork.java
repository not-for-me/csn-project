package org.csn.data;

import java.util.Set;

public class SensorNetwork {
	private String id;
	private String name;
	private String regTime;
	private String deRegTime;
	private String status;
	private int topicID;
	private String topicPath;
	private int memberCount;
	Set<String> memberList;
	Set<String> tagSet;
	Set<NetworkMetadata> metadataSet;

	public SensorNetwork(String id, String name, String regTime,
			String deRegTime, String status, int topicID, String topicPath,
			int memberCount, Set<String> memberList, Set<String> tagSet,
			Set<NetworkMetadata> metadataSet) {
		this.id = id;
		this.name = name;
		this.regTime = regTime;
		this.deRegTime = deRegTime;
		this.status = status;
		this.topicID = topicID;
		this.topicPath = topicPath;
		this.memberCount = memberCount;
		this.memberList = memberList;
		this.tagSet = tagSet;
		this.metadataSet = metadataSet;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRegTime() {
		return regTime;
	}

	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}

	public String getDeRegTime() {
		return deRegTime;
	}

	public void setDeRegTime(String deRegTime) {
		this.deRegTime = deRegTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getTopicID() {
		return topicID;
	}

	public void setTopicID(int topicID) {
		this.topicID = topicID;
	}

	public String getTopicPath() {
		return topicPath;
	}

	public void setTopicPath(String topicPath) {
		this.topicPath = topicPath;
	}

	public int getMemberCount() {
		return memberCount;
	}

	public void setMemberCount(int memberCount) {
		this.memberCount = memberCount;
	}

	public Set<String> getMemberList() {
		return memberList;
	}

	public void setMemberList(Set<String> memberList) {
		this.memberList = memberList;
	}

	public Set<String> getTagSet() {
		return tagSet;
	}

	public void setTagSet(Set<String> tagSet) {
		this.tagSet = tagSet;
	}

	public Set<NetworkMetadata> getMetadataSet() {
		return metadataSet;
	}

	public void setMetadataSet(Set<NetworkMetadata> metadataSet) {
		this.metadataSet = metadataSet;
	}

	@Override
	public String toString() {
		return "SensorNetwork [id=" + id + ", name=" + name + ", regTime="
				+ regTime + ", deRegTime=" + deRegTime + ", status=" + status
				+ ", topicID=" + topicID + ", topicPath=" + topicPath
				+ ", memberCount=" + memberCount + ", memberList=" + memberList
				+ ", tagSet=" + tagSet + ", metadataSet=" + metadataSet + "]";
	}

}
