package com.redhat.techbase.dto;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class BaseDocumentdto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String type;
	private String typeName;
	private String typeLink;
	private String projectName;
	private String projectLink;
	private String title;
	private Integer documentID;
	private String dlLink; // Download Link
	private String createdDate;
	private Integer shareCnt;
	private Integer followCnt;
	private Integer viewsCnt;
	private Integer bookmarkCnt;//favcont
	private Integer commentsCnt;
	private Integer RepliesCnt;
	private String authorName;
	private String role;
	private String authorEmail;
	private String location;
	private Integer likesCnt;
	private Integer helpfulCnt;
	
	
	private List<String> technology;
	private List<String> product;
	private List<String> businessFunction;
	private List<String> businessActivity;
	
	private String content;
	private String format;
	
	
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public List<String> getTechnology() {
		return technology;
	}
	public void setTechnology(List<String> technology) {
		this.technology = technology;
	}
	public List<String> getProduct() {
		return product;
	}
	public void setProduct(List<String> product) {
		this.product = product;
	}
	public List<String> getBusinessFunction() {
		return businessFunction;
	}
	public void setBusinessFunction(List<String> businessFunction) {
		this.businessFunction = businessFunction;
	}
	public List<String> getBusinessActivity() {
		return businessActivity;
	}
	public void setBusinessActivity(List<String> businessActivity) {
		this.businessActivity = businessActivity;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getTypeLink() {
		return typeLink;
	}
	public void setTypeLink(String typeLink) {
		this.typeLink = typeLink;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getProjectLink() {
		return projectLink;
	}
	public void setProjectLink(String projectLink) {
		this.projectLink = projectLink;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getDocumentID() {
		return documentID;
	}
	public void setDocumentID(Integer documentID) {
		this.documentID = documentID;
	}
	public String getDlLink() {
		return dlLink;
	}
	public void setDlLink(String dlLink) {
		this.dlLink = dlLink;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public Integer getShareCnt() {
		return shareCnt;
	}
	public void setShareCnt(Integer shareCnt) {
		this.shareCnt = shareCnt;
	}
	public Integer getFollowCnt() {
		return followCnt;
	}
	public void setFollowCnt(Integer followCnt) {
		this.followCnt = followCnt;
	}
	public Integer getViewsCnt() {
		return viewsCnt;
	}
	public void setViewsCnt(Integer viewsCnt) {
		this.viewsCnt = viewsCnt;
	}
	public Integer getBookmarkCnt() {
		return bookmarkCnt;
	}
	public void setBookmarkCnt(Integer bookmarkCnt) {
		this.bookmarkCnt = bookmarkCnt;
	}
	public Integer getCommentsCnt() {
		return commentsCnt;
	}
	public void setCommentsCnt(Integer commentsCnt) {
		this.commentsCnt = commentsCnt;
	}
	public Integer getRepliesCnt() {
		return RepliesCnt;
	}
	public void setRepliesCnt(Integer repliesCnt) {
		RepliesCnt = repliesCnt;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getAuthorEmail() {
		return authorEmail;
	}
	public void setAuthorEmail(String authorEmail) {
		this.authorEmail = authorEmail;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Integer getLikesCnt() {
		return likesCnt;
	}
	public void setLikesCnt(Integer likesCnt) {
		this.likesCnt = likesCnt;
	}
	public Integer getHelpfulCnt() {
		return helpfulCnt;
	}
	public void setHelpfulCnt(Integer helpfulCnt) {
		this.helpfulCnt = helpfulCnt;
	}
}
