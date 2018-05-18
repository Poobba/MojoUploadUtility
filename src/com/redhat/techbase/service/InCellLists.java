/* ====================================================================
Licensed to the Apache Software Foundation (ASF) under one or more
contributor license agreements.  See the NOTICE file distributed with
this work for additional information regarding copyright ownership.
The ASF licenses this file to You under the Apache License, Version 2.0
(the "License"); you may not use this file except in compliance with
the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==================================================================== */

package com.redhat.techbase.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.redhat.techbase.dto.Contentdto;

/**
 * This class contains code that demonstrates how to insert plain, numbered and
 * bulleted lists into an Excel spreadsheet cell.
 * 
 * Look at the code contained in the demonstrateMethodCalls() method. It calls
 * other methods that create plain, numbered and bulleted single and multi-level
 * lists. The demonstrateMethodCalls() method appears at the top of the class
 * definition.
 * 
 * Though different methods are provided to construct single and multi-level
 * plain, numbered and bulleted lists, close examination will reveal that they
 * are not strictly necessary. If the inputs to the listInCell() and
 * multilLevelListInCell() methods are constructed to include the bullet
 * character or the item numbers then these methods alone may be sufficient.
 * 
 * @author Mark Beardsley [msb at apache.org]
 */
public class InCellLists {

	// This character looks like a solid, black, loser case letter 'o'
	// positioned up from the base line of the text.
	private static final char BULLET_CHARACTER = '\u2022';

	// The tab character - \t - cannot be used to create a tab space
	// within a cell as it is rendered as a square. Therefore, four
	// spaces are used to simulate that character.
	private static final String TAB = "    ";
	private static final String COMMA = ",";

	private static String[] columns = { "Type", "Space Name/Group Name",
			"Link", "Project Name", "Project Link", "Doc Name", "Document ID",
			"Download Link", "Created date", "Share Count", "Follow Count",
			"Views Count", "Attachments", "Attachment ID", "Attachment Link",
			"Attachement Format", "Bookmarks Count", "Comments Count",
			"Replies Count", "Likes Count", "Tags", "Outcome types",
			"Created by", "Author Name", "Author email ID", "Role",
			"modified date", "Helpful Count","Categories",
			"Technology", "Product", "Business Function", "Business Activity","Doc Format","Content" };

	/**
	 * Call each of the list creation methods.
	 * 
	 * @param contentList
	 * 
	 * @param outputFilename
	 *            A String that encapsulates the name of and path to the Excel
	 *            spreadsheet file this code will create.
	 */
	public void demonstrateMethodCalls(List<Contentdto> contentList,
			String outputFilename)  {
		try {
			 Workbook workbook = new HSSFWorkbook();
			 Sheet sheet = workbook.createSheet("Mojo content");
	
			// Create a Row
			 Row headRow = sheet.createRow(0);
			
			// Creating cells
			for (int i = 0; i < columns.length; i++) {
				Cell cell = headRow.createCell(i);
				cell.setCellValue(columns[i]);
			}
	
			//contentList.removeAll(null);  
			// Create Other rows and cells
			int rowNum = 1;
			for (Contentdto contentdto : contentList) {
	
				Row row = sheet.createRow(rowNum++);
				
				//System.out.print(contentdto.getTitle()+ contentdto.getDlLink());
	
				// The row height and cell width are set here to ensure that the
				// list may be seen.
				row.setHeight((short) 1100);
				sheet.setColumnWidth(0, 9500);
	
				row.createCell(0).setCellValue(contentdto.getType());
	
				row.createCell(1).setCellValue(contentdto.getTypeName());
	
				row.createCell(2).setCellValue(contentdto.getTypeLink());
				
				row.createCell(3).setCellValue(contentdto.getProjectName());
				
				row.createCell(4).setCellValue(contentdto.getProjectLink());
				
				row.createCell(5).setCellValue(contentdto.getTitle());
	
				row.createCell(6).setCellValue(contentdto.getDocumentID());
	
				row.createCell(7).setCellValue(contentdto.getDlLink());
	
				row.createCell(8).setCellValue(contentdto.getCreatedDate());
	
				row.createCell(9).setCellValue(contentdto.getShareCnt());
	
				row.createCell(10).setCellValue(contentdto.getFollowCnt());
	
				row.createCell(11).setCellValue(contentdto.getViewsCnt());
	
				this.multiLevelBulletedListInCell(workbook,
						contentdto.getAttachmentName(), row.createCell(12));
	
				this.numberBulletedListInCell(workbook,
						contentdto.getAttachmentID(), row.createCell(13));
	
				this.multiLevelBulletedListInCell(workbook,
						contentdto.getAttachmentLink(), row.createCell(14));
				
				this.multiLevelBulletedListInCell(workbook, contentdto.getDocFormat(),
						row.createCell(15));
	
				row.createCell(16).setCellValue(contentdto.getBookmarkCnt());
	
				row.createCell(17).setCellValue(contentdto.getCommentsCnt());
	
				row.createCell(18).setCellValue(contentdto.getRepliesCnt());
	
				row.createCell(19).setCellValue(contentdto.getLikesCnt());
				
				this.multiLevelBulletedListInCell(workbook, contentdto.getTags(),
						row.createCell(20));
	
				
				this.multiLevelBulletedListInCell(workbook,
						contentdto.getOutcomeTypes(), row.createCell(21));
	
				row.createCell(22).setCellValue(contentdto.getLastModifiedBy());
				
				row.createCell(23).setCellValue(contentdto.getAuthorName());
	
				row.createCell(24).setCellValue(contentdto.getAuthorEmail());
				
				row.createCell(25).setCellValue(contentdto.getRole());
	
				row.createCell(26).setCellValue(contentdto.getLastModifiedDate());
	
				row.createCell(27).setCellValue(contentdto.getHelpfulCnt());
	
	
				this.multiLevelBulletedListInCell(workbook,
						contentdto.getCategories(), row.createCell(28));
	
				this.multiLevelBulletedListInCell(workbook,
						contentdto.getTechnology(), row.createCell(29));
	
				this.multiLevelBulletedListInCell(workbook,
						contentdto.getProduct(), row.createCell(30));
	
				this.multiLevelBulletedListInCell(workbook,
						contentdto.getBusinessFunction(), row.createCell(31));
	
				this.multiLevelBulletedListInCell(workbook,
						contentdto.getBusinessActivity(), row.createCell(32));
				
				row.createCell(33).setCellValue(contentdto.getFormat());
				row.createCell(34).setCellValue(contentdto.getContent());
				
	
				// Resize all columns to fit the content size
				for (int i = 0; i < columns.length; i++) {
					sheet.autoSizeColumn(i);
				}
	
				// Save the completed workbook
	
				OutputStream fileOut = new FileOutputStream(outputFilename);
				workbook.write(fileOut);
				
					fileOut.close();
			}
		}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			/*
			 * // Save the completed workbook FileOutputStream fos = new
			 * FileOutputStream(new File( outputFilename)) {
			 * workbook.write(fos); }
			 */


	}

	/**
	 * Insert a bulleted list into a cell.
	 * 
	 * @param workbook
	 *            A reference to the HSSFWorkbook that 'contains' the cell.
	 * @param listItems
	 *            An List of Integer
	 * @param cell
	 *            An instance of the HSSFCell class that encapsulates a
	 *            reference to the spreadsheet cell into which the list will be
	 *            written.
	 */

	private void numberBulletedListInCell(Workbook workbook,
			List<Integer> listID, Cell cell) {
		StringBuilder buffer = new StringBuilder();
		// Note that again, an HSSFCellStye object is required and that
		// it's wrap text property should be set to 'true'
		/*HSSFCellStyle wrapStyle = workbook.createCellStyle();
		wrapStyle.setWrapText(true);*/
		//CellStyle cellStyle = workbook.createCellStyle();
		//.setAlignment(CellStyle.ALIGN_CENTER);
		//cellStyle.setWrapText(true);
		// Note that the basic method is identical to the listInCell() method
		// with one difference; the bullet character prefixed to the items text.
		if (listID != null) {
		for (Integer listItem : listID) {
			
			buffer.append(listItem);
			buffer.append(InCellLists.COMMA);
			
		} 
		}else {
			buffer.append(" ");
		}
		// The StringBuffer's contents are the source for the contents
		// of the cell.
		cell.setCellValue(new HSSFRichTextString(buffer.toString().trim()));
		//cell.setCellStyle(cellStyle);

	}

	/**
	 * Insert a bulleted multi-level list into a cell.
	 * 
	 * @param workbook
	 *            A reference to the HSSFWorkbook that 'contains' the cell.
	 * @param list
	 *            An ArrayList whose elements contain instances of the
	 *            MultiLevelListItem class. Each element encapsulates the text
	 *            for the high level item along with an ArrayList. Each element
	 *            of this ArrayList encapsulates the text for a lower level
	 *            item.
	 * @param cell
	 *            An instance of the HSSFCell class that encapsulates a
	 *            reference to the spreadsheet cell into which the list will be
	 *            written.
	 */
	public void multiLevelBulletedListInCell(Workbook workbook,
			List<String> list, Cell cell) {
		StringBuilder buffer = new StringBuilder();
		// Note that again, an HSSFCellStye object is required and that
		// it's wrap text property should be set to 'true'
		/*HSSFCellStyle wrapStyle = workbook.createCellStyle();
		wrapStyle.setWrapText(true);*/
		//CellStyle multicellStyle = workbook.createCellStyle();
		//multicellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		//multicellStyle.setWrapText(true);
		// Step through the ArrayList of MultilLevelListItem instances.
		if (list != null) {
			for (String multiLevelListItem : list) {

				buffer.append(multiLevelListItem);
				buffer.append(InCellLists.COMMA);
			}
		} else {
			buffer.append(" ");
		}
		// The StringBuffer's contents are the source for the contents
		// of the cell.
		cell.setCellValue(new HSSFRichTextString(buffer.toString().trim()));
		//cell.setCellStyle(multicellStyle);
	}
}
