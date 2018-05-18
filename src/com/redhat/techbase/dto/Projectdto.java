package com.redhat.techbase.dto;

import java.io.Serializable;
import java.util.List;

public class Projectdto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List<String> name;
	private List<Integer> projectId;//placeId
	private List<String> link;
	
	public List<String> getLink() {
		return link;
	}
	public void setLink(List<String> link) {
		this.link = link;
	}
	public List<String> getName() {
		return name;
	}
	public void setName(List<String> name) {
		this.name = name;
	}
	public List<Integer> getProjectId() {
		return projectId;
	}
	public void setProjectId(List<Integer> projectId) {
		this.projectId = projectId;
	}
	
 
}
