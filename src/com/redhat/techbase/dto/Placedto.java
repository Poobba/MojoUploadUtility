package com.redhat.techbase.dto;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;


public class Placedto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List<String> name;
	private List<Integer> placeId;
	private List<Integer> childCount;// space count
	private List<String> link;
	
	@JsonProperty("project")
	private Projectdto projectdto = new Projectdto();
	
	@JsonProperty("space")
	private Spacedto spacedto = new Spacedto();
	
	@JsonProperty("InSpace")
	private IncludedSubSpacedto inSpace = new IncludedSubSpacedto();
	
	
	
	
	public List<String> getLink() {
		return link;
	}


	public void setLink(List<String> link) {
		this.link = link;
	}


	public IncludedSubSpacedto getInSpace() {
		return inSpace;
	}


	public void setInSpace(IncludedSubSpacedto inSpace) {
		this.inSpace = inSpace;
	}


	public List<String> getName() {
		return name;
	}


	public void setName(List<String> name) {
		this.name = name;
	}


	public List<Integer> getPlaceId() {
		return placeId;
	}


	public void setPlaceId(List<Integer> placeId) {
		this.placeId = placeId;
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


	public Spacedto getSpacedto() {
		return spacedto;
	}


	public void setSpacedto(Spacedto spacedto) {
		this.spacedto = spacedto;
	}
	
}
