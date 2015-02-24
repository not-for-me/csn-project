package org.csn.rest.data;

import java.util.Map;
import java.util.Set;

public class NetworkSeed {
	private String name;
	private Set<String> members;
	private Set<Map<String, String>> metadata;
	private Set<String> tags;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<String> getMembers() {
		return members;
	}

	public void setMembers(Set<String> members) {
		this.members = members;
	}

	public Set<Map<String, String>> getMetadata() {
		return metadata;
	}

	public void setMetadata(Set<Map<String, String>> metadata) {
		this.metadata = metadata;
	}

	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	@Override
	public String toString() {
		return "NetworkSeed [name=" + name + ", members=" + members
				+ ", metadata=" + metadata + ", tags=" + tags + "]";
	}
}
