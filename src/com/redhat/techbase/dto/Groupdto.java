package com.redhat.techbase.dto;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Groupdto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List<Integer> groupId;//placeId
	private List<String> name;
	private List<Integer> childCount;
	private List<String> link;

	
	public List<String> getLink() {
		return link;
	}


	public void setLink(List<String> link) {
		this.link = link;
	}


	public List<Integer> getGroupId() {
		return groupId;
	}


	public void setGroupId(List<Integer> groupId) {
		this.groupId = groupId;
	}


	public List<String> getName() {
		return name;
	}


	public void setName(List<String> name) {
		this.name = name;
	}


	public List<Integer> getChildCount() {
		return childCount;
	}


	public void setChildCount(List<Integer> childCount) {
		this.childCount = childCount;
	}


	public Projectdto getProjectdto() {
		return projectdto;
	}


	public void setProjectdto(Projectdto projectdto) {
		this.projectdto = projectdto;
	}


	@JsonProperty("project")
	private Projectdto projectdto = new Projectdto();
	
	
	
	

}
