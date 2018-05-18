package com.redhat.techbase.dto;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;


@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Contentdto extends BaseDocumentdto {

	private static final long serialVersionUID = 1L;

	private List<String> attachmentName;
	private List<Integer> attachmentID;
	private List<String> attachmentLink;
	private List<String> docFormat;
	private List<String> tags;
	private List<String> outcomeTypes;// (Official/Outdated/Reserved/Final/Success)
	private String lastModifiedBy;// (Author)
	private String lastModifiedDate;
	private List<String> categories;	
	private String contentType;
	private String subtype;
	

   

	public String getSubtype() {
		return subtype;
	}


	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}


	public String getContentType() {
		return contentType;
	}


	public void setContentType(String contentType) {
		this.contentType = contentType;
	}


	public List<String> getTags() {
		return tags;
	}


	public void setTags(List<String> tags) {
		this.tags = tags;
	}


	public List<String> getOutcomeTypes() {
		return outcomeTypes;
	}


	public void setOutcomeTypes(List<String> outcomeTypes) {
		this.outcomeTypes = outcomeTypes;
	}


	public String getLastModifiedBy() {
		return lastModifiedBy;
	}


	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}


	public String getLastModifiedDate() {
		return lastModifiedDate;
	}


	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}


	public List<String> getCategories() {
		return categories;
	}


	public void setCategories(List<String> categories) {
		this.categories = categories;
	}
	public List<String> getAttachmentName() {
		return attachmentName;
	}


	public void setAttachmentName(List<String> attachmentName) {
		this.attachmentName = attachmentName;
	}


	public List<Integer> getAttachmentID() {
		return attachmentID;
	}


	public void setAttachmentID(List<Integer> attachmentID) {
		this.attachmentID = attachmentID;
	}


	public List<String> getAttachmentLink() {
		return attachmentLink;
	}


	public void setAttachmentLink(List<String> attachmentLink) {
		this.attachmentLink = attachmentLink;
	}


	public List<String> getDocFormat() {
		return docFormat;
	}


	public void setDocFormat(List<String> docFormat) {
		this.docFormat = docFormat;
	}	
}
