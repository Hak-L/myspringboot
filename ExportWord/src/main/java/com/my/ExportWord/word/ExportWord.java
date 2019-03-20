package com.my.ExportWord.word;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;

public class ExportWord {

	private static String fontFamily = "微软雅黑";
	private static int fontSize = 11;

	private static String filePath = "J://z_docx/exportWord.docx";
	
	private static String filePathFromDB = "J://z_docx/exportWordFromDB.docx";

	public static void main(String[] args) throws Exception {

//		getWord();

		System.out.println("-------------------------------------");
		
		createWordFromDB();
		
		System.out.println("end");
	}

	public static void createWordFromDB() throws Exception {
		
		String[] tables	= {
				   "aniu_user",
				   "aniu_yrym_code"
				  };
		
		XWPFDocument xdoc = new XWPFDocument();
		
		for(String table : tables){
			
			System.out.println(">>>>>>>>>> table = " + table);
			
			createWordFromDBContent(xdoc, table);
		}
		
		// 在服务器端生成
		FileOutputStream fos = new FileOutputStream(filePathFromDB);
		xdoc.write(fos);
		fos.close();
	}

	public static void createWordFromDBContent(XWPFDocument xdoc, String table) throws Exception {
		
		List<TableDto> tableBoList = searchTableColumn(table);
		if(tableBoList == null || tableBoList.isEmpty()){
			return;
		}
		
		XWPFParagraph headLine1 = xdoc.createParagraph();
		headLine1.setAlignment(ParagraphAlignment.LEFT);
		
		XWPFRun runHeadLine1 = headLine1.createRun();
		runHeadLine1.setText(table);
		runHeadLine1.setFontSize(fontSize);
		runHeadLine1.setFontFamily(fontFamily);
		runHeadLine1.setColor("FF0000");
		
		XWPFTable dTable = xdoc.createTable();
		
		String bgColor = "111111";
		CTTbl ttbl = dTable.getCTTbl();
		CTTblPr tblPr = ttbl.getTblPr() == null ? ttbl.addNewTblPr() : ttbl.getTblPr();
		CTTblWidth tblWidth = tblPr.isSetTblW() ? tblPr.getTblW() : tblPr.addNewTblW();
		tblWidth.setW(new BigInteger("8600"));
		tblWidth.setType(STTblWidth.DXA);
		
		XWPFTableRow row = dTable.getRow(0);
		setCellText(xdoc, row.getCell(0)	, "字段名称"	, bgColor, 1000);
		setCellText(xdoc, row.createCell()	, "类型"		, bgColor, 1000);
		setCellText(xdoc, row.createCell()	, "主键"		, bgColor, 1000);
		setCellText(xdoc, row.createCell()	, "非空"		, bgColor, 1000);
		setCellText(xdoc, row.createCell()	, "备注"		, bgColor, 1000);
//		setCellText(xdoc, row.createCell()	, "默认值"	, bgColor, 1000);
		
		for (TableDto tableBo : tableBoList) {
			XWPFTableRow row_ = dTable.createRow();
			setCellText(xdoc, row_.getCell(0)	, tableBo.getField()					, bgColor, 1000);
			setCellText(xdoc, row_.getCell(1)	, tableBo.getType()						, bgColor, 1000);
			setCellText(xdoc, row_.getCell(2)	, tableBo.isPrimaryKey() ? "是" : "", bgColor, 1000);
			setCellText(xdoc, row_.getCell(3)	, tableBo.getNull_()					, bgColor, 1000);
			setCellText(xdoc, row_.getCell(4)	, tableBo.getComment()					, bgColor, 1000);
//			setCellText(xdoc, row_.getCell(5)	, tableBo.getDefault_()					, bgColor, 1000);
		}
		
		setEmptyRow(xdoc, runHeadLine1);
		
	}

	private static List<TableDto> searchTableColumn(String table){
		
		Connection conn = null;
		try {
			conn = DbUtils.getConn_test();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		String sql = "show full fields from " + table;
		
		PreparedStatement pstat		= null;
		ResultSet rs				= null;
		TableDto tableBo				= new TableDto();
		List<TableDto> tableBoList	= new ArrayList<>();
		try {
			
			pstat = conn.prepareStatement(sql);
			rs = pstat.executeQuery();
			
			while(rs.next()){
				
				String field	= rs.getString("Field");
				String type		= rs.getString("Type");
				String key		= rs.getString("Key");
				String null_	= rs.getString("Null");
				String default_	= rs.getString("Default");
				String comment	= rs.getString("Comment");
				
//				int index = type.indexOf("(");
//				if(index > 0){
//					type = type.substring(0, index);
//				}
				
				TableDto tb = CloneUtils.clone(tableBo);
				tb.setField(field);
				tb.setType(type);
				tb.setKey(key);
				tb.setNull_(null_);
				tb.setDefault_(default_);
				tb.setComment(comment);
				tableBoList.add(tb);
			}
		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			DbUtils.close(rs);
			DbUtils.close(pstat);
			DbUtils.close(conn);
		}
		
		return tableBoList;
	}

	/**
	 * -----------------------------------------------------------
	 */
	public static void getWord() throws Exception {

		XWPFDocument xdoc = new XWPFDocument();
		// 标题
		XWPFParagraph titleMes = xdoc.createParagraph();
		titleMes.setAlignment(ParagraphAlignment.CENTER);
		XWPFRun r1 = titleMes.createRun();
		r1.setBold(true);
		r1.setFontFamily(fontFamily);
		r1.setText("XXX的计划及进度报告");// 活动名称
		r1.setFontSize(fontSize);
		r1.setColor("333333");
		r1.setBold(true);
		
		// 標題信息
		XWPFParagraph userMes = xdoc.createParagraph();// 设置活动主题
		userMes.setAlignment(ParagraphAlignment.CENTER);
		XWPFRun r2 = userMes.createRun();
		r2.setText("標題信息");
		r2.setFontSize(fontSize);
		r2.setFontFamily(fontFamily);
		r2.setColor("a6a6a6");
		
		// 温馨提示
		XWPFParagraph hintMes = xdoc.createParagraph();
		hintMes.setAlignment(ParagraphAlignment.LEFT);
		XWPFRun runHint = hintMes.createRun();
		runHint.setText("温馨提示：本文档分为工作清单和工作明细两部分。");
		runHint.setFontSize(fontSize);
		runHint.setFontFamily(fontFamily);
		runHint.setColor("a6a6a6");

		// 标题一：计划清单
		XWPFParagraph headLine1 = xdoc.createParagraph();
		headLine1.setAlignment(ParagraphAlignment.LEFT);
		
		XWPFRun runHeadLine1 = headLine1.createRun();
		runHeadLine1.setText("一、计划清单");
		runHeadLine1.setFontSize(fontSize);
		runHeadLine1.setFontFamily(fontFamily);
		runHeadLine1.setColor("a6a6a6");
		XWPFTable dTable = xdoc.createTable(5, 3);
		createTable(dTable, xdoc);
		setEmptyRow(xdoc, r1);

		// 标题二：计划清单
		XWPFParagraph headLine2 = xdoc.createParagraph();
		headLine2.setAlignment(ParagraphAlignment.LEFT);
		
		XWPFRun runHeadLine2 = headLine2.createRun();
		runHeadLine2.setText("二、计划明细");
		runHeadLine2.setFontSize(fontSize);
		runHeadLine2.setFontFamily(fontFamily);
		runHeadLine2.setColor("a6a6a6");
		
		// 表格
		for (int i = 0; i < 4; i++) {
			XWPFTable xTable = xdoc.createTable(7, 2);
			createSimpleTable(xTable, xdoc);
			setEmptyRow(xdoc, r1);
		}
		
		// 在服务器端生成
		FileOutputStream fos = new FileOutputStream(filePath);
		xdoc.write(fos);
		fos.close();
	}

	public static void createTable(XWPFTable xTable, XWPFDocument xdoc) {
		String bgColor = "111111";
		CTTbl ttbl = xTable.getCTTbl();
		CTTblPr tblPr = ttbl.getTblPr() == null ? ttbl.addNewTblPr() : ttbl.getTblPr();
		CTTblWidth tblWidth = tblPr.isSetTblW() ? tblPr.getTblW() : tblPr.addNewTblW();
		tblWidth.setW(new BigInteger("8600"));
		tblWidth.setType(STTblWidth.DXA);
		setCellText(xdoc, getCellHight(xTable, 0, 0), "序号", bgColor, 1000);
		setCellText(xdoc, getCellHight(xTable, 0, 1), "阶段", bgColor, 3800);
		setCellText(xdoc, getCellHight(xTable, 0, 2), "计划工作任务", bgColor, 3800);
		int number = 0;
		for (int i = 1; i < 5; i++) {
			number++;
			setCellText(xdoc, getCellHight(xTable, number, 0), number + "", bgColor, 1000);
			setCellText(xdoc, getCellHight(xTable, number, 1), "階段名稱", bgColor, 3800);
			setCellText(xdoc, getCellHight(xTable, number, 2), "任務名稱", bgColor, 3800);
		}
	}

	// 设置表格高度
	private static XWPFTableCell getCellHight(XWPFTable xTable, int rowNumber, int cellNumber) {
		XWPFTableRow row = xTable.getRow(rowNumber);
		row.setHeight(100);
		XWPFTableCell cell = row.getCell(cellNumber);
		return cell;
	}

	/**
	 * 
	 * @param xDocument
	 * @param cell
	 * @param text
	 * @param bgcolor
	 * @param width
	 */
	private static void setCellText(XWPFDocument xDocument, XWPFTableCell cell, String text, String bgcolor, int width) {
		
//		System.out.println("setCellText------------------------------------------");
//		System.out.println("setCellText xDocument = " + xDocument);
//		System.out.println("setCellText cell = " + cell);
//		System.out.println("setCellText text = " + text);
//		System.out.println("setCellText bgcolor = " + bgcolor);
//		System.out.println("setCellText width = " + width);
		
		CTTc cttc = cell.getCTTc();
		CTTcPr cellPr = cttc.addNewTcPr();
		cellPr.addNewTcW().setW(BigInteger.valueOf(width));
		XWPFParagraph pIO = cell.addParagraph();
		cell.removeParagraph(0);
		XWPFRun rIO = pIO.createRun();
		rIO.setFontFamily(fontFamily);
		rIO.setColor("000000");
		rIO.setFontSize(fontSize);
		rIO.setText(text);
	}

	// 设置表格间的空行
	public static void setEmptyRow(XWPFDocument xdoc, XWPFRun r1) {
		XWPFParagraph p1 = xdoc.createParagraph();
		p1.setAlignment(ParagraphAlignment.CENTER);
		p1.setVerticalAlignment(TextAlignment.CENTER);
		r1 = p1.createRun();
	}

	/**
	 * 创建计划明细表
	 * 
	 * @param
	 * @param xTable
	 * @param xdoc
	 * @throws IOException
	 */
	public static void createSimpleTable(XWPFTable xTable, XWPFDocument xdoc) throws IOException {
		String bgColor = "FFFFFF";
		CTTbl ttbl = xTable.getCTTbl();
		CTTblPr tblPr = ttbl.getTblPr() == null ? ttbl.addNewTblPr() : ttbl.getTblPr();
		CTTblWidth tblWidth = tblPr.isSetTblW() ? tblPr.getTblW() : tblPr.addNewTblW();
		tblWidth.setW(new BigInteger("8600"));
		tblWidth.setType(STTblWidth.DXA);
		for (int i = 0; i < 7; i++) {
			setCellText(xdoc, getCellHight(xTable, i, 0), "阶段", bgColor, 3300);
			setCellText(xdoc, getCellHight(xTable, i, 1), "階段名稱", bgColor, 3300);
		}
	}
}
