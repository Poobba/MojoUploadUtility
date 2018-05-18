package com.redhat.techbase.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.redhat.techbase.service.MojoService;
import com.redhat.techbase.util.PropertyFileReader;

public class MojoServiceImpl implements MojoService {

	private static final Logger LOG = Logger.getLogger(MojoServiceImpl.class);

	String ticket = null;

	public String getResponse(String restContext, String url) {
		// LOG.debug("Start getresponse() method");
		String response = null;
		try {
			// URL requestURL = new URL(restContext);
			Credentials defaultcreds = new UsernamePasswordCredentials(
					"sa_techbase", "+guMHKNEyxk=");
			HttpClient client = new HttpClient();
			client.getParams().setAuthenticationPreemptive(true);
			client.getState().setCredentials(AuthScope.ANY, defaultcreds);
			System.out.println(restContext + url);
			GetMethod getMethod = new GetMethod(restContext + url);
			getMethod.setDoAuthentication(true);

			// System.out.println(getMethod);
			client.executeMethod(getMethod);
			response = getMethod.getResponseBodyAsString();

			if (response.toString().contains("Missing content ID ")) {
				response = null;
			} else if (response.toString().contains(
					"You are not allowed to perform this request")) {
				response = null;
			} else if (response.toString().contains(
					"The specified place does not exist")) {
				response = null;
			} else if (response.toString().contains(
					"An input field is malformed")) {
				response = null;
			} else if (response
					.toString()
					.contains(
							"You are not allowed to access the specified content object")) {
				response = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return response;
	}

	/*
	 * @Override public void writePropertiesToExcel(List<Contentdto>
	 * contentList) throws IOException {
	 * 
	 * // Create a Workbook Workbook workbook = new XSSFWorkbook(); // new
	 * HSSFWorkbook() for // generating `.xls` file
	 * 
	 * 
	 * CreationHelper helps us create instances for various things like
	 * DataFormat, Hyperlink, RichTextString etc, in a format (HSSF, XSSF)
	 * independent way
	 * 
	 * CreationHelper createHelper = workbook.getCreationHelper();
	 * 
	 * // Create a Sheet Sheet sheet = workbook.createSheet("MojoContents");
	 * 
	 * // Create a Row Row headerRow = sheet.createRow(0);
	 * 
	 * // Creating cells for (int i = 0; i < columns.length; i++) { Cell cell =
	 * headerRow.createCell(i); cell.setCellValue(columns[i]); }
	 * 
	 * // Create Other rows and cells with employees data int rowNum = 1;
	 * 
	 * for (Contentdto contentdto : contentList) { Row row =
	 * sheet.createRow(rowNum++);
	 * 
	 * row.createCell(0).setCellValue(contentdto.getType());
	 * 
	 * row.createCell(1).setCellValue(contentdto.getTitle());
	 * 
	 * row.createCell(2).setCellValue(contentdto.getDocumentID());
	 * 
	 * row.createCell(3).setCellValue(contentdto.getDlLink());
	 * 
	 * row.createCell(4).setCellValue(contentdto.getLikesCnt());
	 * 
	 * row.createCell(5).setCellValue(contentdto.getCreatedDate()); }
	 * 
	 * // Resize all columns to fit the content size for (int i = 0; i <
	 * columns.length; i++) { sheet.autoSizeColumn(i); }
	 * 
	 * // Write the output to a file // FileOutputStream fileOut = new //
	 * FileOutputStream("poi-generated-file.xlsx"); OutputStream fileOut = new
	 * FileOutputStream(
	 * "C:/Users/poanand/Desktop/Migration Project/AlfrescoMigration/work/output/mojoExcelReport.xlsx"
	 * ); workbook.write(fileOut); fileOut.close(); }
	 */

	@Override
	public String getAlfrescoTicket() {

		if (this.ticket != null)
			return this.ticket;

		String alfTicket = null;
		String getTicketURL = PropertyFileReader.getInstance().getProperty(
				PropertyFileReader.ALFRESCO_SERVER_URL)
				+ "/service/api/login";
		LOG.info("Getting the alfresco ticket from - " + getTicketURL);

		Map<String, String> credentialsMap = new HashMap<String, String>(2);
		credentialsMap.put(
				"u",
				PropertyFileReader.getInstance().getProperty(
						PropertyFileReader.ALFRESCO_USERNAME));
		credentialsMap.put(
				"pw",
				PropertyFileReader.getInstance().getProperty(
						PropertyFileReader.ALFRESCO_PASSWORD));

		String responseStr = invokeGetService(getTicketURL, credentialsMap);
		if (responseStr != null) {
			alfTicket = StringUtils.substringBetween(responseStr, "<ticket>",
					"</ticket>");
		}
		LOG.info("Alfresco ticket is :" + alfTicket);
		this.ticket = alfTicket;
		return alfTicket;
	}

	private String invokeGetService(String url, Map<String, String> urlParams) {

		GetMethod getMethod = new GetMethod(url);
		getMethod.setQueryString(convertMapToQueryParams(urlParams));

		return this.getResponse(getMethod);
	}

	private String convertMapToQueryParams(Map<String, String> inputMap) {

		StringBuilder queryString = new StringBuilder();

		if (null != inputMap && !inputMap.isEmpty()) {
			for (Entry<String, String> mapEntry : inputMap.entrySet()) {

				if (queryString.length() > 0) {
					queryString.append("&");
				}
				queryString.append(mapEntry.getKey() + "="
						+ mapEntry.getValue());
			}
		}
		return queryString.toString();
	}

	private String getResponse(HttpMethodBase method) {

		String response = null;
		HttpClient client = new HttpClient();
		client.getHttpConnectionManager().getParams()
				.setConnectionTimeout(60000);
		try {
			client.executeMethod(method);
			response = method.getResponseBodyAsString();
		} catch (Exception e) {

		} finally {
			method.releaseConnection();
		}

		return response;
	}

	@Override
	public String getResponse(String url) {
		String attresponse = null;
		try {
			Credentials defaultcreds = new UsernamePasswordCredentials(
					"sa_techbase", "+guMHKNEyxk=");
			HttpClient client = new HttpClient();
			client.getParams().setAuthenticationPreemptive(true);
			client.getState().setCredentials(AuthScope.ANY, defaultcreds);
			GetMethod getMethod = new GetMethod(url);
			getMethod.setDoAuthentication(true);
			client.executeMethod(getMethod);
			attresponse = getMethod.getResponseBodyAsString();

			if (attresponse.toString().contains("Missing content ID ")) {
				attresponse = null;
			} else if (attresponse.toString().contains(
					"You are not allowed to perform this request")) {
				attresponse = null;
			} else if (attresponse.toString().contains(
					"The specified place does not exist")) {
				attresponse = null;
			} else if (attresponse.toString().contains(
					"An input field is malformed")) {
				attresponse = null;
			} else if (attresponse
					.toString()
					.contains(
							"You are not allowed to access the specified content object")) {
				attresponse = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return attresponse;
	}

	@Override
	public HashMap<String, String> checkParent(String Url, String type) {

		HashMap<String, String> parentdtl = new HashMap<String, String>();
		String firstResponse = this.getResponse(Url);
		if (firstResponse != null) {
			JsonObject jsonobj = (JsonObject) new JsonParser()
					.parse(firstResponse);

			if (jsonobj.has("parent")) {
				String secondResponse = this.getResponse(jsonobj.get("parent")
						.toString());
				if (secondResponse != null) {
					JsonObject jobj = (JsonObject) new JsonParser()
							.parse(secondResponse);
					
					if (jobj.has("type")) {
						parentdtl.put("name", jobj.get("name").getAsString());
						parentdtl.put("url", jobj.get("resources")
								.getAsJsonObject().get("html")
								.getAsJsonObject().get("ref").getAsString());
						parentdtl.put("type", jobj.get("type").getAsString());
					}
				}
			}
		}
		return parentdtl;
	}
}