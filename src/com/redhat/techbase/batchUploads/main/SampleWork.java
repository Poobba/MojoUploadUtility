package com.redhat.techbase.batchUploads.main;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xslf.usermodel.DrawingParagraph;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFNotes;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFSlide;

import org.jopendocument.dom.ODPackage;
import org.openxmlformats.schemas.presentationml.x2006.main.CTNotesSlide;
import org.openxmlformats.schemas.presentationml.x2006.main.CTSlide;
import org.openxmlformats.schemas.presentationml.x2006.main.CTSlideIdListEntry;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.redhat.techbase.util.PropertyFileReader;
import com.redhat.techbase.dto.Attachmentdto;
import com.redhat.techbase.dto.Contentdto;
import com.redhat.techbase.service.MojoService;
import com.redhat.techbase.service.impl.MojoServiceImpl;

public class SampleWork {

	private static Logger log = Logger.getLogger(BatchDownloadMain.class);
	public static MojoService mojoservice = new MojoServiceImpl();
	private static List<Contentdto> contentList = new ArrayList<>();

	//private static List<Attachmentdto> AttachmentList = new ArrayList<>();

	public static void main(String[] args) throws IOException {

		log.info("BatchUploadMain start");
		PropertyFileReader.propertyFile = "batchupload.properties";
		PropertyFileReader batchUploadProperties = PropertyFileReader
				.getInstance();
		// get the root place details

		String rootUrl = batchUploadProperties
				.getProperty(PropertyFileReader.ROOT_URL);
		String restContext = batchUploadProperties
				.getProperty(PropertyFileReader.BASE_URL);

		String response = null;

		String tech = batchUploadProperties
				.getProperty(PropertyFileReader.TECHNOLOGY_CLASSIFICATION);

		String prod = batchUploadProperties
				.getProperty(PropertyFileReader.PRODUCT_CLASSIFICATION);

		String outputdirect = batchUploadProperties
				.getProperty(PropertyFileReader.DIRECTORY);

		String url = "places/1154944/contents?filter=type(document)&count=100";

		String contentPath = "contents/";
		String contentUrl = "";
		
		List<Attachmentdto> AttachmentList = new ArrayList<>();
		

		response = mojoservice.getResponse(restContext, url);
		

		ArrayList<String> techlist = getPlaces(tech);
		ArrayList<String> prodlist = getPlaces(prod);

		if (response != null) {

			
			JsonObject jsonobj = (JsonObject) new JsonParser().parse(response);
			JsonArray jsonObjArray = null;
			jsonObjArray = jsonobj.getAsJsonArray("list");
			//System.out.print("json Array size" + jsonObjArray.size());
			for (int i = 0; i < jsonObjArray.size(); i++) {

				JsonElement jsonElement = jsonObjArray.get(i);
				String contentResponse = null;
				JsonObject jsonObj = jsonElement.getAsJsonObject();
				// get the Content Id
				Contentdto contentdto = new Contentdto();
				if (jsonObj.get("contentID") != null) {
					contentdto.setDocumentID(jsonObj.get("id").getAsInt());

					// create the URL
					// contentUrl = contentPath + contentId;
					// System.out.print(contentUrl);
					// contentResponse = mojoservice.getResponse(restContext,
					// contentUrl);
					// contentUrl="";
					// JsonArray arrayJson = null;
					// if (!contentResponse.isEmpty()) {
					// JsonObject contjsonobj = (JsonObject) new
					// JsonParser().parse(contentResponse);
					// arrayJson = contjsonobj.getAsJsonArray();

					// System.out.print(jsonObj.get("subject")
					// .getAsString());
					//
					contentdto.setTitle(jsonObj.get("subject").getAsString());
					contentdto.setFollowCnt(jsonObj.get("followerCount")
							.getAsInt());// followerCount
					contentdto.setLikesCnt(jsonObj.get("likeCount").getAsInt());// likeCount
					// contentdto.setCreatedDate(jsonObj.get("published").getAsInt());
					// //published
					/*
					 * System.out.print(jsonObj.get("outcomeTypeNames")); if
					 * (jsonObj.has("outcomeTypeNames")) { JsonArray typeArray =
					 * jsonObj.getAsJsonArray("outcomeTypeNames");
					 * //System.out.print(typeArray); for (int
					 * t=0;t<typeArray.size();t++){
					 * System.out.print(typeArray.get(t).getAsString()); } }
					 */

					/*
					 * int techCount=0; for (String item : techlist){
					 * //System.out
					 * .print(jsonobj.get("content").getAsJsonObject(
					 * ).get("text").getAsString()); System.out.print(item);
					 * if(jsonobj
					 * .get("content").getAsJsonObject().get("text").getAsString
					 * ().contains(item) ||
					 * jsonobj.get("subject").getAsString().contains(item)){
					 * techCount++; } } System.out.print(techCount);
					 */
					// System.out.print(jsonObj.get("author").getAsJsonObject().get("jive").getAsJsonObject().get("profile").getAsJsonArray());
					/*
					 * if(jsonObj.has("author")){ JsonArray typeArray
					 * =jsonObj.get
					 * ("author").getAsJsonObject().get("jive").getAsJsonObject
					 * ().getAsJsonArray("profile"); int s =typeArray.size();
					 * for (int t=0;t<typeArray.size();t++){ JsonElement
					 * jsonElement1 = typeArray.get(t); JsonObject jsonObj1 =
					 * jsonElement1.getAsJsonObject(); if
					 * (jsonObj1.get("jive_label").getAsString()
					 * .equalsIgnoreCase("Title")) {
					 * System.out.print(jsonObj1.get("value") .getAsString()); }
					 * } }
					 */

					/*
					 * Date date; try { date = new
					 * SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ"
					 * ).parse(jsonObj.get("published").getAsString());
					 * System.out.print(date); String newDate = new
					 * SimpleDateFormat("MM/dd/yyyy").format(date);
					 * System.out.print(newDate);//2016-04-28T08:54:29.439+0000
					 * } catch (ParseException e) { // TODO Auto-generated catch
					 * block e.printStackTrace(); }
					 */

					/*
					 * if(jsonObj.get("author") != null){ JsonArray attArray
					 * =jsonObj
					 * .get("author").getAsJsonObject().getAsJsonArray("emails"
					 * ); System.out.print(attArray.size()); for (int j = 0; j <
					 * attArray.size(); j++) { JsonElement jsonElement1 =
					 * attArray.get(j); JsonObject jsonObj1 =
					 * jsonElement1.getAsJsonObject();
					 * if(jsonObj1.get("jive_label"
					 * ).getAsString().equalsIgnoreCase("Email")){
					 * System.out.print(jsonObj1.get("value").getAsString()); }
					 * }
					 * 
					 * 
					 * }
					 */

					/*
					 * if(jsonobj.get("tags") != null){
					 * System.out.print(jsonobj.get("tags")); JsonArray
					 * userArray = jsonobj.getAsJsonArray("tags");
					 * System.out.print(userArray.isJsonArray());
					 * System.out.print(userArray);
					 * //System.out.print(jsonObj.get
					 * ("author").getAsJsonObject().get("mentionName"));
					 * //System
					 * .out.print(jsonobj.get("author").getAsJsonObject()
					 * .get("emails"));
					 * //System.out.print(jsonobj.get("resources"
					 * ).getAsJsonObject
					 * ().get("html").getAsJsonObject().get("ref")); }
					 */
					// contentdto.setCategories(jsonobj.get("categories").getAsString());

					/*
					 * List<String> listdata = new ArrayList<String>();
					 * //System.
					 * out.print(jsonobj.get("categories").getAsJsonArray());
					 * if(jsonobj.get("categories") != null){
					 * //System.out.print(jsonobj.get("categories")); JsonArray
					 * typeArray = jsonobj.getAsJsonArray("categories");
					 * System.out.print(typeArray); int s =typeArray.size(); for
					 * (int i=0;i<typeArray.size();i++){
					 * listdata.add(typeArray.get(i).getAsString()); }
					 * System.out.print(listdata);
					 * contentdto.setCategories(listdata); }
					 */

					
					if (jsonObj.has("attachments")) {
						// System.out.print(jsonObj.get("attachments"));
						JsonArray userArray = jsonObj
								.getAsJsonArray("attachments");
						// System.out.print(userArray);
						int s = userArray.size();
						Attachmentdto attdtos = new Attachmentdto();
						for (int a = 0; a < s; a++) {
							JsonElement jsonatt = userArray.get(a);
							JsonObject jsonObjAtt = jsonatt.getAsJsonObject();
							System.out.print(jsonObjAtt.get("id"));
							//attachedId.add(jsonObjAtt.get("id").getAsInt());
							attdtos.setType(contentdto.getType());
							attdtos.setTypeName(contentdto.getTypeName());
							attdtos.setTypeLink(contentdto.getTypeLink());
							attdtos.setAttachmentName(jsonObjAtt.get("name")
									.getAsString());
							attdtos.setAttachmentID(jsonObjAtt.get("id")
									.getAsInt());
							attdtos.setAttachmentLink(jsonObjAtt.get("url")
									.getAsString());
							attdtos.setDocFormat(jsonObjAtt.get("contentType")
									.getAsString());
							AttachmentList.add(attdtos);

						}

					}

					// Write the data to excel
					// mojoservice.writePropertiesToExcel(contentdto,i,wb,sheet);
					contentList.add(contentdto);

					// }

					// }

				}
			}

		}
		//System.out.print(AttachmentList);
		boolean status;
		status = getAttachments(restContext, AttachmentList, outputdirect,
				techlist);
		// mojoservice.writePropertiesToExcel(contentList);

	}

	private static ArrayList<String> getPlaces(String spaceList) {

		ArrayList<String> list = new ArrayList<String>();
		String splitList[] = spaceList.split(",");
		for (int i = 0; i < splitList.length; i++) {
			// System.out.print(splitList[i].toString());
			list.add(splitList[i].toString());
		}
		return list;
	}

	private static boolean getAttachments(String restContext,
			List<Attachmentdto> attList, String outputdirect,
			ArrayList<String> techlist) {
		// now add Attachment ID similarly as content ID
		try {

			for (Attachmentdto dto : attList) {
				Contentdto attachdto = new Contentdto();
				System.out.print(dto.getAttachmentID());
				String attachUrl = PropertyFileReader.ATTACH_PATH
						+ dto.getAttachmentID();
				String attachResponse = mojoservice.getResponse(restContext,
						attachUrl);
				if (attachResponse != null) {
					JsonObject jsonobj = (JsonObject) new JsonParser()
							.parse(attachResponse);
					attachdto.setType(dto.getType());
					attachdto.setTypeName(dto.getTypeName());
					attachdto.setTypeLink(dto.getTypeLink());
					attachdto.setSubtype(jsonobj.get("type").getAsString());
					attachdto.setDocumentID(jsonobj.get("id").getAsInt());
					attachdto.setContentType(jsonobj.get("contentType")
							.getAsString());
					attachdto.setTitle(jsonobj.get("name").getAsString());
					attachdto.setDlLink(jsonobj.get("url").getAsString());
					attachdto.setAuthorEmail("");
					attachdto.setAuthorName("");
					attachdto.setBookmarkCnt(0);
					attachdto.setCommentsCnt(0);
					attachdto.setContentType("");
					attachdto.setFollowCnt(0);
					attachdto.setHelpfulCnt(0);
					attachdto.setLastModifiedBy("");
					attachdto.setLikesCnt(0);
					attachdto.setLocation("");
					attachdto.setRepliesCnt(0);
					attachdto.setShareCnt(0);
					attachdto.setViewsCnt(0);

					// now to check the attachment classification
					// download the file in download folder
					File file = new File("C:\\output\\download\\"
							+ jsonobj.get("name").getAsString());
					 System.out.print(jsonobj.get("name").getAsString());
					boolean downloadchk = downloadAttachment(jsonobj.get("url")
							.getAsString(), file, outputdirect);
					if (downloadchk) {
						attachdto.setTechnology(getclassification(file,
								techlist));
					}
					contentList.add(attachdto);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
				System.out.print(p);
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
				System.out.println(doc.getTextContent());
				for (String item : list) {
					if (doc.getTextContent().contains(item)) {

						activeList.add(item);
					}

				}
			} else if (ext.equalsIgnoreCase("pptx")) {

				XMLSlideShow ppt = new XMLSlideShow(new FileInputStream(file));
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
			} else if (ext.equalsIgnoreCase(".zip")) {
				// contains the key
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
		// System.out.print(activeList);
		return activeList;
	}

	@SuppressWarnings("unused")
	private static boolean downloadAttachment(String atturl, File file,
			String outputdirect) throws IOException {
		boolean exists = false;
		URL myURL = new URL(atturl);
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
		BufferedInputStream bis = new BufferedInputStream(c.getInputStream());

		FileOutputStream fis = new FileOutputStream(file);
		byte[] buffer = new byte[4096];
		int count = 0;
		while ((count = bis.read(buffer, 0, 1024)) != -1) {
			fis.write(buffer, 0, count);
		}
		fis.close();
		bis.close();

		if (file.exists()) {
			exists = true;
		} else {
			exists = false;
		}

		return exists;
	}

}
