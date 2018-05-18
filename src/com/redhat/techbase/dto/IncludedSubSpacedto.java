package com.redhat.techbase.dto;

import java.io.Serializable;
import java.util.List;

public class IncludedSubSpacedto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List<String> name;
	private List<Integer> spaceId;//placeId
	private List<String> linkspace;
	
	
	public List<String> getLinkspace() {
		return linkspace;
	}
	public void setLinkspace(List<String> linkspace) {
		this.linkspace = linkspace;
	}
	public List<String> getName() {
		return name;
	}
	public void setName(List<String> name) {
		this.name = name;
	}
	public List<Integer> getSpaceId() {
		return spaceId;
	}
	public void setSpaceId(List<Integer> spaceId) {
		this.spaceId = spaceId;
	}
	
}
	