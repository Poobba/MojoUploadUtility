package com.redhat.techbase.batchUploads.main;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFNotes;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.jopendocument.dom.ODPackage;
import org.openxmlformats.schemas.presentationml.x2006.main.CTSlide;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.redhat.techbase.dto.Attachmentdto;
import com.redhat.techbase.dto.Contentdto;
import com.redhat.techbase.dto.Groupdto;
import com.redhat.techbase.dto.IncludedSubSpacedto;
import com.redhat.techbase.dto.Placedto;
import com.redhat.techbase.dto.Projectdto;
import com.redhat.techbase.dto.Spacedto;
import com.redhat.techbase.service.InCellLists;
import com.redhat.techbase.service.MojoService;
import com.redhat.techbase.service.impl.MojoServiceImpl;
import com.redhat.techbase.util.PropertyFileReader;

public class BatchDownloadMain {

	public static MojoService mojoservice = new MojoServiceImpl();
	private static List<Contentdto> contentList = new ArrayList<>();
	private static Placedto placedto = new Placedto();

	private static Spacedto spacedto = new Spacedto();
	private static IncludedSubSpacedto inSpace = new IncludedSubSpacedto();
	private static Groupdto groupdto = new Groupdto();

	private static List<Attachmentdto> AttachmentList = new ArrayList<>();

	public static void main(String[] args) {

		System.out.println("BatchUploadMain start");
		PropertyFileReader.propertyFile = "batchupload.properties";
		PropertyFileReader batchUploadProperties = PropertyFileReader
				.getInstance();

		String outputfilename = batchUploadProperties
				.getProperty(PropertyFileReader.OUTPUT);
		// get the root place details

		String rootUrl = batchUploadProperties
				.getProperty(PropertyFileReader.ROOT_URL);
		String restContext = batchUploadProperties.getProperty(
				PropertyFileReader.BASE_URL).trim();

		String spaceName = batchUploadProperties
				.getProperty(PropertyFileReader.SPACE_NAME);

		String subSpaceName = batchUploadProperties
				.getProperty(PropertyFileReader.SUB_SPACE_NAME);

		String archiveSpace = batchUploadProperties
				.getProperty(PropertyFileReader.ARCHIVE_SUB_SPACE_NAME);

		String groupName = batchUploadProperties
				.getProperty(PropertyFileReader.GROUP_NAME);

		String date = batchUploadProperties
				.getProperty(PropertyFileReader.JOB_RUN_DATE);

		String tech = batchUploadProperties
				.getProperty(PropertyFileReader.TECHNOLOGY_CLASSIFICATION);

		String prod = batchUploadProperties
				.getProperty(PropertyFileReader.PRODUCT_CLASSIFICATION);

		String CONSULTANT = batchUploadProperties
				.getProperty(PropertyFileReader.CONSULTANT);

		String ENABLEMENT = batchUploadProperties
				.getProperty(PropertyFileReader.ENABLEMENT);

		String PRESALES = batchUploadProperties
				.getProperty(PropertyFileReader.PRESALES);

		String MANAGEMENT = batchUploadProperties
				.getProperty(PropertyFileReader.MANAGEMENT);

		String bactivity = batchUploadProperties
				.getProperty(PropertyFileReader.BA_CLASSIFICATION);

		String id = batchUploadProperties
				.getProperty(PropertyFileReader.CONTENT_ID);
		
		String downloadDir=batchUploadProperties
				.getProperty(PropertyFileReader.DIRECTORY);
		
		String doctype = batchUploadProperties
				.getProperty(PropertyFileReader.MOJODOCTYPE);
		
		try {

			if (!spaceName.isEmpty()) {

				getplaces(rootUrl, restContext, spaceName);
				// to get the project ?filter=type(project)
				getproject(placedto.getPlaceId(), restContext);

				//System.out.print(placedto.getPlaceId());

				getContentId(placedto.getPlaceId(), restContext, "place", "",
						date, prod, tech, CONSULTANT, ENABLEMENT, PRESALES,
						MANAGEMENT, bactivity, id,doctype);

				getContentId(placedto.getProjectdto().getProjectId(),
						restContext, "project", "place", date, prod, tech,
						CONSULTANT, ENABLEMENT, PRESALES, MANAGEMENT,
						bactivity, id,doctype);

			}
			// get the space whose Root space is excluded.
			if (!subSpaceName.isEmpty()) {
				getExSubSpace(subSpaceName, restContext);

				getproject(placedto.getInSpace().getSpaceId(), restContext);

				getContentId(placedto.getInSpace().getSpaceId(), restContext,
						"space", "", date, prod, tech, CONSULTANT, ENABLEMENT,
						PRESALES, MANAGEMENT, bactivity, id,doctype);

				getContentId(placedto.getProjectdto().getProjectId(),
						restContext, "space", "project", date, prod, tech,
						CONSULTANT, ENABLEMENT, PRESALES, MANAGEMENT,
						bactivity, id,doctype);
			}
			// to get the Space ?filter=type(space)
			if (!archiveSpace.isEmpty()) {
				getSubSpace(placedto.getPlaceId(), restContext, archiveSpace);

				getproject(placedto.getSpacedto().getSpaceId(), restContext);

				getContentId(placedto.getSpacedto().getSpaceId(), restContext,
						"space", "", date, prod, tech, CONSULTANT, ENABLEMENT,
						PRESALES, MANAGEMENT, bactivity, id,doctype);

				getContentId(placedto.getProjectdto().getProjectId(),
						restContext, "project", "Space", date, prod, tech,
						CONSULTANT, ENABLEMENT, PRESALES, MANAGEMENT,
						bactivity, id,doctype);
			}

			// to get the project search/places?filter=type(group)
			if (!groupName.isEmpty()) {
				getGroup(groupName, restContext);
				getGroupProject(groupdto.getGroupId(), restContext);

				getContentId(groupdto.getGroupId(), restContext, "group", "",
						date, prod, tech, CONSULTANT, ENABLEMENT, PRESALES,
						MANAGEMENT, bactivity, id,doctype);
				getContentId(groupdto.getProjectdto().getProjectId(),
						restContext, "project", "group", date, prod, tech,
						CONSULTANT, ENABLEMENT, PRESALES, MANAGEMENT,
						bactivity, id,doctype);
			}

			// add the attachments in the list
			boolean status;
			status = getAttachments(restContext, AttachmentList, prod, tech, bactivity,downloadDir);

			System.out.print(contentList);

			new InCellLists().demonstrateMethodCalls(contentList,
					outputfilename);

		} catch (ParseException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void getContentId(List<Integer> placeId, String restContext,
			String type, String typeProject, String Sdate, String prod,
			String tech, String consultant, String enablemet, String presales,
			String management, String bactivity, String id, String doctype)
			throws ParseException {

		List<Integer> contentId = new ArrayList<>();
		ArrayList<String> techlist = addPropList(tech);
		ArrayList<String> prodlist = addPropList(prod);
		ArrayList<String> consBF = addPropList(consultant);
		ArrayList<String> enabBF = addPropList(enablemet);
		ArrayList<String> presBF = addPropList(presales);
		ArrayList<String> mangBF = addPropList(management);
		ArrayList<String> businessActivity = addPropList(bactivity);

		ArrayList<Integer> docid = addPropListInt(id);
		ArrayList<String> dtype =addPropList(doctype);
		
		
   for (String t : dtype){
		for (Integer item : placeId) {
			String scUrl = "places/" + item
					+ "/contents?filter=type("+t+")&count=100";
			String scresponse = mojoservice.getResponse(restContext, scUrl);
			if (scresponse != null) {
				JsonArray jsonObjArray = null;
				jsonObjArray = getList(scresponse);
				for (int i = 0; i < jsonObjArray.size(); i++) {
					JsonElement jsonElement = jsonObjArray.get(i);
					JsonObject jsonObj = jsonElement.getAsJsonObject();

					Date date = new SimpleDateFormat(
							"yyyy-MM-dd'T'hh:mm:ss.SSSZ").parse(jsonObj.get(
							"published").getAsString());
					String cDate = new SimpleDateFormat("dd/MM/yyyy")
							.format(date);
					Date date1 = new SimpleDateFormat("dd/MM/yyyy")
							.parse(cDate);
					Date date2 = new SimpleDateFormat("dd/MM/yyyy")
							.parse(Sdate);

					// date1.compareTo(date2) > 0
					// && && jsonObj.get("viewCount").getAsInt() >= 0

					if (docid.contains(jsonObj.get("id").getAsInt())) {

						contentId.add(jsonObj.get("contentID").getAsInt());

					}
				}
			}
		}
    }	
		System.out.print(contentId);
		// add the content in the list
		for (Integer content : contentId) {

			List<String> actionType = new ArrayList<>();
			List<String> attachementList = new ArrayList<>();
			List<Integer> attachementId = new ArrayList<>();
			List<String> attachementLink = new ArrayList<>();

			List<String> attachementFormat = new ArrayList<>();

			List<String> technology = new ArrayList<>();
			List<String> product = new ArrayList<>();

			List<String> bA = new ArrayList<>();

			String contentUrl = PropertyFileReader.CONTENT_PATH + content;
			String contentResponse = mojoservice.getResponse(restContext,
					contentUrl);
			Contentdto contentdto = new Contentdto();

			if (contentResponse != null) {
				JsonObject jsonobj = (JsonObject) new JsonParser()
						.parse(contentResponse);

				contentdto.setDocumentID(jsonobj.get("id").getAsInt());

				if (!type.isEmpty()
						&& type.equalsIgnoreCase("Project")) {

					// contentdto.setType(typeProject);
					// get the parent of project details
					HashMap<String, String> projResponse = mojoservice
							.checkParent(
									jsonobj.get("parentPlace")
											.getAsJsonObject().get("uri")
											.getAsString(), type);

					contentdto.setType(projResponse.get("type"));
					contentdto.setTypeName(projResponse.get("name"));
					contentdto.setTypeLink(projResponse.get("url"));

					contentdto.setProjectName(jsonobj.get("parentPlace")
							.getAsJsonObject().get("name").getAsString());
					contentdto.setProjectLink(jsonobj.get("parentPlace")
							.getAsJsonObject().get("html").getAsString());
					// get Bussiness function from typename
					contentdto.setBusinessFunction(checkBF(consBF, enabBF,
							presBF, mangBF, projResponse.get("name")));
					//System.out.print(projResponse.get("name"));

				} else if (!type.isEmpty() && type.equalsIgnoreCase("Place")) {

					contentdto.setType(jsonobj.get("parentPlace")
							.getAsJsonObject().get("type").getAsString());
					contentdto.setTypeName(jsonobj.get("parentPlace")
							.getAsJsonObject().get("name").getAsString());
					contentdto.setTypeLink(jsonobj.get("parentPlace")
							.getAsJsonObject().get("html").getAsString());
					// get Bussiness function from typename
					contentdto.setBusinessFunction(checkBF(consBF, enabBF,
							presBF, mangBF, jsonobj.get("parentPlace")
							.getAsJsonObject().get("name").getAsString()));

				} else if (!type.isEmpty() && type.equalsIgnoreCase("Space")) {

					contentdto.setType(jsonobj.get("parentPlace")
							.getAsJsonObject().get("type").getAsString());
					contentdto.setTypeName(jsonobj.get("parentPlace")
							.getAsJsonObject().get("name").getAsString());
					contentdto.setTypeLink(jsonobj.get("parentPlace")
							.getAsJsonObject().get("html").getAsString());
					// get Bussiness function from typename
					contentdto.setBusinessFunction(checkBF(consBF, enabBF,
							presBF, mangBF, jsonobj.get("parentPlace")
							.getAsJsonObject().get("name").getAsString()));

				} else if (!type.isEmpty() && type.equalsIgnoreCase("Group")) {

					contentdto.setType(jsonobj.get("parentPlace")
							.getAsJsonObject().get("type").getAsString());
					contentdto.setTypeName(jsonobj.get("parentPlace")
							.getAsJsonObject().get("name").getAsString());
					contentdto.setTypeLink(jsonobj.get("parentPlace")
							.getAsJsonObject().get("html").getAsString());
					
					// get Bussiness function from typename
					contentdto.setBusinessFunction(checkBF(consBF, enabBF,
							presBF, mangBF, jsonobj.get("parentPlace")
							.getAsJsonObject().get("name").getAsString()));

				}

				contentdto.setTitle(jsonobj.get("subject").getAsString());
				contentdto
						.setDlLink(jsonobj.get("resources").getAsJsonObject()
								.get("html").getAsJsonObject().get("ref")
								.getAsString());

				// 2016-04-28T08:54:29.439+0000
				Date date = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ")
						.parse(jsonobj.get("published").getAsString());
				String newDate = new SimpleDateFormat("MM/dd/yyyy")
						.format(date);

				contentdto.setCreatedDate(newDate);
				contentdto.setShareCnt(0);
				if (jsonobj.has("followerCount")) {
					contentdto.setFollowCnt(jsonobj.get("followerCount")
							.getAsInt());// followerCount
				} else {
					contentdto.setFollowCnt(0);
				}

				if (jsonobj.has("viewCount")) {
					contentdto.setViewsCnt(jsonobj.get("viewCount").getAsInt());
				} else {
					contentdto.setViewsCnt(0);
				}
				
				if (jsonobj.has("favoriteCount")) {
					contentdto.setBookmarkCnt(jsonobj.get("favoriteCount")
							.getAsInt());
				} else {
					contentdto.setBookmarkCnt(0);
				}
				
				contentdto.setCommentsCnt(0);
				if (jsonobj.has("replyCount")) {
					contentdto.setRepliesCnt(jsonobj.get("replyCount").getAsInt());
				} else {
					contentdto.setRepliesCnt(0);
				}
				
				if (jsonobj.has("likeCount")) {
					contentdto.setLikesCnt(jsonobj.get("likeCount").getAsInt());// likeCount
				} else {
					contentdto.setLikesCnt(0);
				}
				
				List<String> tagList = new ArrayList<String>();
				if (jsonobj.has("tags")) {
					JsonArray typeArray = jsonobj.getAsJsonArray("tags");
					for (int i = 0; i < typeArray.size(); i++) {
						tagList.add(typeArray.get(i).getAsString());
					}
					contentdto.setTags(tagList);
				}

				if (jsonobj.has("outcomeTypeNames")) {
					JsonArray typeArray = jsonobj
							.getAsJsonArray("outcomeTypeNames");
					// System.out.print(typeArray);
					for (int t = 0; t < typeArray.size(); t++) {
						actionType.add(typeArray.get(t).getAsString());
					}
					contentdto.setOutcomeTypes(actionType);
				}

				// author
				if (jsonobj.has("author")) {
					contentdto
							.setAuthorName(jsonobj.get("author")
									.getAsJsonObject().get("displayName")
									.getAsString());
					JsonArray attArray = jsonobj.get("author")
							.getAsJsonObject().getAsJsonArray("emails");
					for (int i = 0; i < attArray.size(); i++) {
						JsonElement jsonElement = attArray.get(i);
						JsonObject jsonObj = jsonElement.getAsJsonObject();
						if (jsonObj.get("jive_label").getAsString()
								.equalsIgnoreCase("Email")) {
							contentdto.setAuthorEmail(jsonObj.get("value")
									.getAsString());
						}
					}

					JsonArray typeArray = jsonobj.get("author")
							.getAsJsonObject().get("jive").getAsJsonObject()
							.getAsJsonArray("profile");
					for (int t = 0; t < typeArray.size(); t++) {
						JsonElement jElem = typeArray.get(t);
						JsonObject jObj = jElem.getAsJsonObject();
						if (jObj.get("jive_label").getAsString()
								.equalsIgnoreCase("Title")) {
							if (jObj.get("value").getAsString()
									.contains("Manager"))
								contentdto.setRole("Project Manager");
							else if (jObj.get("value").getAsString()
									.contains("Consultant"))
								contentdto.setRole("Consultant");
							else if (jObj.get("value").getAsString()
									.contains("Architect"))
								contentdto.setRole("Soution Architect");
							else
								contentdto.setRole(jObj.get("value")
										.getAsString());
						}
					}
				}

				Date modDate = new SimpleDateFormat(
						"yyyy-MM-dd'T'hh:mm:ss.SSSZ").parse(jsonobj.get(
						"published").getAsString());
				String SMDate = new SimpleDateFormat("MM/dd/yyyy")
						.format(modDate);

				contentdto.setLastModifiedDate(SMDate);
				// helpfulCount
				
				if (jsonobj.has("helpfulCount")) {
					contentdto.setHelpfulCnt(jsonobj.get("helpfulCount").getAsInt());
				} else {
					contentdto.setHelpfulCnt(0);
				}
			

				List<String> catList = new ArrayList<String>();
				if (jsonobj.get("categories") != null) {
					JsonArray typeArray = jsonobj.getAsJsonArray("categories");
					int s = typeArray.size();
					for (int i = 0; i < typeArray.size(); i++) {
						catList.add(typeArray.get(i).getAsString());
					}
					contentdto.setCategories(catList);
				}

				if (jsonobj.has("attachments")) {
					JsonArray attArray = jsonobj.getAsJsonArray("attachments");

					int s = attArray.size();
					for (int a = 0; a < s; a++) {
						JsonElement jsonElement = attArray.get(a);
						JsonObject jsonObj = jsonElement.getAsJsonObject();
						Attachmentdto attdtos = new Attachmentdto();
						attachementList.add(jsonObj.get("name").getAsString());
						attachementId.add(jsonObj.get("id").getAsInt());
						attachementLink.add(jsonObj.get("url").getAsString());
						attachementFormat.add(jsonObj.get("contentType")
								.getAsString());
						
						attdtos.setType(contentdto.getType());
						attdtos.setTypeName(contentdto.getTypeName());
						attdtos.setTypeLink(contentdto.getTypeLink());
						attdtos.setTitle(contentdto.getTitle());
						attdtos.setDocumentID(contentdto.getDocumentID());
						attdtos.setDlLink(contentdto.getDlLink());
						attdtos.setProjectName(contentdto.getProjectName());
						attdtos.setProjectLink(contentdto.getProjectLink());
						attdtos.setAttachmentName(jsonObj.get("name")
								.getAsString());
						attdtos.setAttachmentID(jsonObj.get("id").getAsInt());
						attdtos.setAttachmentLink(jsonObj.get("url")
								.getAsString());
						attdtos.setDocFormat(jsonObj.get("contentType")
								.getAsString());
						attdtos.setBusinessFunction(contentdto.getBusinessFunction());
						attdtos.setAuthorName(contentdto.getAuthorName());
						attdtos.setAuthorEmail(contentdto.getAuthorEmail());
						attdtos.setRole(contentdto.getRole());
						attdtos.setShareCnt(contentdto.getShareCnt());
						attdtos.setFollowCnt(contentdto.getFollowCnt());
						attdtos.setViewsCnt(contentdto.getViewsCnt());
						attdtos.setBookmarkCnt(contentdto.getBookmarkCnt());
						attdtos.setCommentsCnt(contentdto.getCommentsCnt());
						attdtos.setRepliesCnt(contentdto.getRepliesCnt());
						attdtos.setHelpfulCnt(contentdto.getHelpfulCnt());
						attdtos.setTags(contentdto.getTags());
						attdtos.setOutcomeTypes(contentdto.getOutcomeTypes());
						attdtos.setCategories(contentdto.getCategories());
						attdtos.setLastModifiedDate(contentdto.getLastModifiedDate());
						attdtos.setLikesCnt(contentdto.getLikesCnt());
						attdtos.setCreatedDate(contentdto.getCreatedDate());
						AttachmentList.add(attdtos);
					}
					contentdto.setAttachmentID(attachementId);
					contentdto.setAttachmentName(attachementList);
					contentdto.setAttachmentLink(attachementLink);
					contentdto.setDocFormat(attachementFormat);

				}

				// check String contains technology
				int techCount = 0;
				for (String item : techlist) {
					if (jsonobj.get("content").getAsJsonObject().get("text")
							.getAsString().contains(item)
							|| jsonobj.get("subject").getAsString()
									.contains(item)) {
						techCount++;
						technology.add(item);
					}

				}
				contentdto.setTechnology(technology);

				// check String contains Product
				for (String p : prodlist) {
					if (jsonobj.get("content").getAsJsonObject().get("text")
							.getAsString().contains(p)
							|| jsonobj.get("subject").getAsString().contains(p)) {
						product.add(p);
					}

				}
				contentdto.setProduct(product);

				// check String contains Business Function
				/*
				 * for (String b : businessFunction) { if
				 * (jsonobj.get("content").getAsJsonObject().get("text")
				 * .getAsString().contains(b) ||
				 * jsonobj.get("subject").getAsString().contains(b)) {
				 * 
				 * bF.add(b); }
				 * 
				 * } contentdto.setBusinessFunction(bF);
				 */

				// check String contains Business Activity
				for (String a : businessActivity) {
					if (jsonobj.get("content").getAsJsonObject().get("text")
							.getAsString().contains(a)
							|| jsonobj.get("subject").getAsString().contains(a)) {

						bA.add(a);
					}

				}
				contentdto.setBusinessActivity(bA);
				
				// get the content and format
				if(jsonobj.has("content")){
					contentdto.setContent(jsonobj.get("content").getAsJsonObject().get("text")
							.getAsString());
				
					contentdto.setFormat(jsonobj.get("content").getAsJsonObject().get("type")
							.getAsString());
				}

				contentList.add(contentdto);
			}
		}

	}

	private static List<String> checkBF(ArrayList<String> consBF,
			ArrayList<String> enabBF, ArrayList<String> presBF,
			ArrayList<String> mangBF, String name) {
		// TODO Auto-generated method stub
		List<String> bF = new ArrayList<>();

		if (!consBF.isEmpty()) {
			for (int i = 0; i < consBF.size(); i++) {
				String item = consBF.get(i);
				if (item.equalsIgnoreCase(name) && !bF.contains("Consultant")) {
					bF.add("Consultant");
				}
			}
		} 
		if(!enabBF.isEmpty() && !enabBF.contains("Enablement")){
			for (int i = 0; i < enabBF.size(); i++) {
				String enabitem = enabBF.get(i);
				if (enabitem.equalsIgnoreCase(name)) {
					bF.add("Enablement");
				}
			}
		}
		if(!presBF.isEmpty() && !presBF.contains("Pre-sales")){
			for (int i = 0; i < presBF.size(); i++) {
				String preitem = presBF.get(i);
				if (preitem.equalsIgnoreCase(name)) {
					bF.add("Pre-sales");
				}
			}
		}
		if(!mangBF.isEmpty() && !mangBF.contains("Project Management")){
			for (int i = 0; i < mangBF.size(); i++) {
				String mangitem = mangBF.get(i);
				if (mangitem.equalsIgnoreCase(name)) {
					bF.add("Project Management");
				}
			}
		}
		return bF;
	}

	private static boolean getAttachments(String restContext,
			List<Attachmentdto> attList, String prod, String tech, String bactivity, String downloadDir) {
		// now add Attachment ID similarly as content ID

		
		
		ArrayList<String> techlist = addPropList(tech);
		ArrayList<String> prodlist = addPropList(prod);
	
		ArrayList<String> businessActivity = addPropList(bactivity);

		for (Attachmentdto dto : attList) {
			Contentdto attachdto = new Contentdto();
			List<String> attachementList = new ArrayList<>();
			List<Integer> attachementId = new ArrayList<>();
			List<String> attachementLink = new ArrayList<>();
			List<String> docformat = new ArrayList<>();
			String attachUrl = PropertyFileReader.ATTACH_PATH
					+ dto.getAttachmentID();
			String attachResponse = mojoservice.getResponse(restContext,
					attachUrl);
			if (attachResponse != null) {
				JsonObject jsonobj = (JsonObject) new JsonParser()
						.parse(attachResponse);
				attachdto.setType(dto.getType()+ "Attachment");
				attachdto.setTypeName(dto.getTypeName());
				attachdto.setTypeLink(dto.getTypeLink());
			
				attachdto.setTitle(dto.getTitle());
				attachdto.setDocumentID(dto.getDocumentID());
				attachdto.setDlLink(dto.getDlLink());
				attachdto.setProjectName(dto.getProjectName());
				attachdto.setProjectLink(dto.getProjectLink());
				attachementId.add(jsonobj.get("id").getAsInt());
				docformat.add(jsonobj.get("contentType").getAsString());
				attachdto.setDocFormat(docformat);
				attachementList.add(jsonobj.get("name").getAsString());
				attachementLink.add(jsonobj.get("url").getAsString());
				attachdto.setAuthorEmail(dto.getAuthorEmail());
				attachdto.setAuthorName(dto.getAuthorName());
				attachdto.setBookmarkCnt(dto.getBookmarkCnt());
				attachdto.setCommentsCnt(dto.getCommentsCnt());
				
				attachdto.setFollowCnt(dto.getFollowCnt());
				attachdto.setHelpfulCnt(dto.getHelpfulCnt());
				attachdto.setLastModifiedBy("");
				attachdto.setLikesCnt(dto.getLikesCnt());
				attachdto.setLocation("");
				attachdto.setRepliesCnt(dto.getRepliesCnt());
				attachdto.setShareCnt(dto.getShareCnt());
				attachdto.setViewsCnt(dto.getViewsCnt());
				attachdto.setAttachmentID(attachementId);
				attachdto.setAttachmentLink(attachementLink);
				attachdto.setAttachmentName(attachementList);
				attachdto.setBusinessFunction(dto.getBusinessFunction());
				attachdto.setLastModifiedDate(dto.getLastModifiedDate());
				attachdto.setTags(dto.getTags());
				attachdto.setRole(dto.getRole());
				attachdto.setOutcomeTypes(dto.getOutcomeTypes());
				attachdto.setCreatedDate(dto.getCreatedDate());
				// now to check the attachment classification
				// download the file in download folder
				File file = new File(downloadDir +"/"+ jsonobj.get("name").getAsString());
				boolean downloadchk = downloadAttachment(jsonobj.get("url")
						.getAsString(), file);

				if (downloadchk) {
					attachdto.setTechnology(getclassification(file, techlist));
					attachdto.setBusinessActivity(getclassification(file,
							businessActivity));
					attachdto.setProduct(getclassification(file, prodlist));
				}

				contentList.add(attachdto);
			}
		}
		return true;

	}

	private static List<String> getclassification(File file,
			ArrayList<String> list) {
		List<String> activeList = new ArrayList<>();
		// Read the file and check fro classification
		try {

			// check the extention
			String ext = FilenameUtils.getExtension(file.toString());

			if (ext.equalsIgnoreCase("odt")) {
				ODPackage p = new ODPackage(file);
				//System.out.print(p);
				for (String item : list) {
					if (p.getContent().asString().contains(item)) {

						activeList.add(item);
					}

				}
			}else if (ext.equalsIgnoreCase("ods") || ext.equalsIgnoreCase("odp") ) {
				ODPackage p = new ODPackage(file);
				//System.out.print(p);
				for (String item : list) {
					if (p.getContent().asString().contains(item)) {

						activeList.add(item);
					}

				}
			} else if (ext.equalsIgnoreCase("pdf")) {

				PdfReader reader = new PdfReader(file.toString());
				String textFromPage = PdfTextExtractor.getTextFromPage(reader,
						1);

				for (String item : list) {
					if (textFromPage.contains(item)) {

						activeList.add(item);
					}

				}
				reader.close();

			} else if (ext.equalsIgnoreCase("xml")) {

				DocumentBuilderFactory dbFactory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(file);
				// System.out.println(doc.getTextContent());
				for (String item : list) {
					if (doc.getTextContent() != null
							&& doc.getTextContent().contains(item)) {

						activeList.add(item);
					}

				}
			} else if (ext.equalsIgnoreCase("pptx")) {
				FileInputStream fis = new FileInputStream(file.getAbsolutePath());
				XMLSlideShow ppt = new XMLSlideShow(fis);
				XSLFSlide[] slides = ppt.getSlides();

				for (XSLFSlide slide : slides) {

					CTSlide xmlObj = slide.getXmlObject();
					XSLFNotes notes = slide.getNotes();
					for (String item : list) {
						if (xmlObj.toString().contains(item)
								&& !activeList.contains(item)) {

							activeList.add(item);
						}
					}
				}
				fis.close();
			} else if (ext.equalsIgnoreCase("zip")) {
				// contains the key
			}else if (ext.equalsIgnoreCase("txt")){
				 Scanner sc = new Scanner(file.getAbsolutePath());		 
				    while (sc.hasNextLine()){
				    	for (String item : list) {
							if (sc.hasNext(item)
									&& !activeList.contains(item)) {

								activeList.add(item);
							}
						}
				    	//System.out.println(sc.nextLine());
				      
				    }
			}else if(ext.equalsIgnoreCase("doc")){
				
				FileInputStream fis = new FileInputStream(file.getAbsolutePath());
				HWPFDocument doc = new HWPFDocument(fis);
				WordExtractor we = new WordExtractor(doc);
				String[] paragraphs = we.getParagraphText();
				//System.out.println("Total no of paragraph "+paragraphs.length);
				for (String para : paragraphs) {
					//System.out.println(para.toString());
					for (String item : list) {
						if (para.toString().contains(item)
								&& !activeList.contains(item)) {

							activeList.add(item);
						}
					}
				}
				fis.close();
			}else  if(ext.equalsIgnoreCase("docx")){
				
				FileInputStream fis = new FileInputStream(file.getAbsolutePath());
				XWPFDocument document = new XWPFDocument(fis);
				List<XWPFParagraph> paragraphs = document.getParagraphs();		
				//System.out.println("Total no of paragraph "+paragraphs.size());
				for (XWPFParagraph paradocx : paragraphs) {
					//System.out.println(paradocx.getText());
					for (String item : list) {
						if (paradocx.toString().contains(item)
								&& !activeList.contains(item)) {

							activeList.add(item);
						}
					}
				}
				fis.close();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(activeList);
		return activeList;
	}

	private static boolean downloadAttachment(String atturl, File file) {

		boolean exists = false;

		if (file.exists()) {
			return exists;
		}

		URL myURL;
		try {
			myURL = new URL(atturl);

			URLConnection c = myURL.openConnection();

			String authStr = Base64.getEncoder().encodeToString(
					"sa_techbase:+guMHKNEyxk=".getBytes());
			// setting Authorization header
			c.setRequestProperty("Authorization", "Basic " + authStr);

			InputStream is = c.getInputStream();
			/*
			 * BufferedReader in = new BufferedReader(new InputStreamReader(
			 * c.getInputStream()));
			 */
			BufferedInputStream bis = new BufferedInputStream(
					c.getInputStream());

			FileOutputStream fis = new FileOutputStream(file);
			byte[] buffer = new byte[4096];
			int count = 0;
			while ((count = bis.read(buffer, 0, 1024)) != -1) {
				fis.write(buffer, 0, count);
			}
			fis.close();
			bis.close();

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (file.exists()) {
			exists = true;
		}

		return exists;

	}

	private static void getGroup(String groupName, String restContext) {

		String splitList[] = groupName.split(",");

		List<String> groupList = new ArrayList<>(splitList.length);
		List<Integer> groupId = new ArrayList<>(splitList.length);
		List<String> groupLink = new ArrayList<>(splitList.length);

		for (int i = 0; i < splitList.length; i++) {
			String scUrl = "search/places?filter=type(group)&filter=search("
					+ splitList[i] + ")&filter=nameonly&count=100";
			String scresponse = mojoservice.getResponse(restContext, scUrl);
			if (scresponse != null) {
				JsonArray jsonObjArray = null;
				jsonObjArray = getList(scresponse);
				for (int j = 0; j < jsonObjArray.size(); j++) {
					JsonElement jsonElement = jsonObjArray.get(j);
					JsonObject jsonObj = jsonElement.getAsJsonObject();
					groupList.add(jsonObj.get("name").getAsString());
					groupId.add(jsonObj.get("placeID").getAsInt());
					groupLink.add(jsonObj.get("resources").getAsJsonObject()
							.get("html").getAsJsonObject().get("ref")
							.getAsString());
				}
			}

		}
		groupdto.setGroupId(groupId);
		groupdto.setName(groupList);
		groupdto.setLink(groupLink);
	}

	private static void getGroupProject(List<Integer> groupId,
			String restContext) {
		List<String> projectList = new ArrayList<>(groupId.size());
		List<Integer> projectId = new ArrayList<>(groupId.size());
		List<String> projectLink = new ArrayList<>(groupId.size());
		Projectdto projectdto = new Projectdto();
		for (Integer item : groupId) {
			String spaceUrl = "places/" + item
					+ "/places?filter=type(project)&count=100";
			String response = mojoservice.getResponse(restContext, spaceUrl);
			if (response != null) {
				JsonArray jsonObjArray = null;
				jsonObjArray = getList(response);
				for (int i = 0; i < jsonObjArray.size(); i++) {
					JsonElement jsonElement = jsonObjArray.get(i);
					JsonObject jsonObj = jsonElement.getAsJsonObject();
					if (jsonObj.has("name")) {
						projectList.add(jsonObj.get("name").getAsString());
						projectId.add(jsonObj.get("placeID").getAsInt());
						projectLink.add(jsonObj.get("resources")
								.getAsJsonObject().get("html")
								.getAsJsonObject().get("ref").getAsString());
					}
				}
			}
		}
		projectdto.setName(projectList);
		projectdto.setProjectId(projectId);
		projectdto.setLink(projectLink);
		groupdto.setProjectdto(projectdto);
	}

	private static void getExSubSpace(String subSpaceName, String restContext) {

		String splitList[] = subSpaceName.split(",");

		List<String> spaceName = new ArrayList<>(splitList.length);
		List<Integer> spaceId = new ArrayList<>(splitList.length);
		List<String> linkspace = new ArrayList<>(splitList.length);

		for (int i = 0; i < splitList.length; i++) {
			String scUrl = "search/places?filter=type(space)&filter=search("
					+ splitList[i] + ")&filter=nameonly";
			String scresponse = mojoservice.getResponse(restContext, scUrl);
			if (scresponse != null) {
				JsonArray jsonObjArray = null;
				jsonObjArray = getList(scresponse);
				for (int j = 0; j < jsonObjArray.size(); j++) {
					JsonElement jsonElement = jsonObjArray.get(j);
					JsonObject jsonObj = jsonElement.getAsJsonObject();
					spaceName.add(jsonObj.get("name").getAsString());
					spaceId.add(jsonObj.get("placeID").getAsInt());
					linkspace.add(jsonObj.get("resources").getAsJsonObject()
							.get("html").getAsJsonObject().get("ref")
							.getAsString());
				}
			}
		}
		inSpace.setName(spaceName);
		inSpace.setSpaceId(spaceId);
		inSpace.setLinkspace(linkspace);
		placedto.setInSpace(inSpace);

	}

	private static void getSubSpace(List<Integer> placeId, String restContext,
			String archiveSpace) {
		List<String> spaceName = new ArrayList<>(placeId.size());
		List<Integer> spaceId = new ArrayList<>(placeId.size());
		List<String> spaceLink = new ArrayList<>(placeId.size());
		ArrayList<String> list = addPropList(archiveSpace);

		for (Integer item : placeId) {
			String spaceUrl = "places/" + item
					+ "/places?filter=type(space)&count=100";
			String response = mojoservice.getResponse(restContext, spaceUrl);
			if (response != null) {
				JsonArray jsonObjArray = null;
				jsonObjArray = getList(response);
				for (int i = 0; i < jsonObjArray.size(); i++) {
					JsonElement jsonElement = jsonObjArray.get(i);
					JsonObject jsonObj = jsonElement.getAsJsonObject();
					if (jsonObj.get("name") != null
							&& !list.contains(jsonObj.get("name").getAsString())) {
						spaceName.add(jsonObj.get("name").getAsString());
						spaceId.add(jsonObj.get("placeID").getAsInt());
						spaceLink.add(jsonObj.get("resources")
								.getAsJsonObject().get("html")
								.getAsJsonObject().get("ref").getAsString());
					}
				}
			}
		}
		spacedto.setName(spaceName);
		spacedto.setSpaceId(spaceId);
		spacedto.setLinkspace(spaceLink);
		placedto.setSpacedto(spacedto);
	}

	private static void getproject(List<Integer> placeId, String restContext) {
		Projectdto projectdto = new Projectdto();

		List<String> projName = new ArrayList<>(placeId.size());
		List<Integer> projId = new ArrayList<>(placeId.size());
		List<String> projLink = new ArrayList<>(placeId.size());

		for (Integer item : placeId) {
			String projUrl = "places/" + item
					+ "/places?filter=type(project)&count=100";
			String response = mojoservice.getResponse(restContext, projUrl);
			if (response != null) {
				JsonArray jsonObjArray = null;
				jsonObjArray = getList(response);
				for (int i = 0; i < jsonObjArray.size(); i++) {
					JsonElement jsonElement = jsonObjArray.get(i);
					JsonObject jsonObj = jsonElement.getAsJsonObject();
					projName.add(jsonObj.get("name").getAsString());
					projId.add(jsonObj.get("placeID").getAsInt());
					projLink.add(jsonObj.get("resources").getAsJsonObject()
							.get("html").getAsJsonObject().get("ref")
							.getAsString());
				}
			}
		}
		projectdto.setName(projName);
		projectdto.setProjectId(projId);
		projectdto.setLink(projLink);
		placedto.setProjectdto(projectdto);

	}

	private static void getplaces(String rootUrl, String restContext,
			String spaceList) {
		// Convert String Array to List
		ArrayList<String> list = addPropList(spaceList);
		System.out.println("Include the list: " + list);
		String response = mojoservice.getResponse(restContext, rootUrl);
		if (response != null) {
			JsonArray jsonObjArray = null;
			jsonObjArray = getList(response);
			List<String> name = new ArrayList<>(jsonObjArray.size());
			List<Integer> placeId = new ArrayList<>(jsonObjArray.size());
			List<Integer> count = new ArrayList<>(jsonObjArray.size());
			List<String> link = new ArrayList<>(jsonObjArray.size());

			for (int i = 0; i < jsonObjArray.size(); i++) {
				JsonElement jsonElement = jsonObjArray.get(i);
				JsonObject jsonObj = jsonElement.getAsJsonObject();

				if (jsonObj.get("name") != null
						&& list.contains(jsonObj.get("name").getAsString())) {
					name.add(jsonObj.get("name").toString());
					placeId.add(jsonObj.get("placeID").getAsInt());
					count.add(jsonObj.get("childCount").getAsInt());
					link.add(jsonObj.get("resources").getAsJsonObject()
							.get("html").getAsJsonObject().get("ref")
							.getAsString());
				}
			}
			placedto.setName(name);
			placedto.setPlaceId(placeId);
			placedto.setChildCount(count);
			placedto.setLink(link);
		}

	}

	private static ArrayList<String> addPropList(String spaceList) {

		ArrayList<String> list = new ArrayList<String>();
		if(spaceList.isEmpty()){
			return list;
		}
		
		String splitList[] = spaceList.split(",");
		for (int i = 0; i < splitList.length; i++) {

			list.add(splitList[i].toString());
		}
		return list;
	}

	private static ArrayList<Integer> addPropListInt(String id) {

		ArrayList<Integer> list = new ArrayList<Integer>();
		String splitList[] = id.split(",");
		for (int i = 0; i < splitList.length; i++) {

			list.add(Integer.parseInt(splitList[i]));
		}
		return list;
	}

	private static JsonArray getList(String response) {
		JsonObject jsonobj = (JsonObject) new JsonParser().parse(response);
		JsonArray jsonObjArray = null;
		jsonObjArray = jsonobj.getAsJsonArray("list");

		return jsonObjArray;
	}

}
