package com.Utils;

import java.io.File;
import jxl.Sheet;
import jxl.Workbook;
import com.relevantcodes.extentreports.ExtentTest;

public class DataProviderExcelReader {

	public ExtentTest test;
	String[][] tabArray = null;
	Workbook workbk;
	Sheet sheet;
	int rowCount, colCount;
//	String sheetPath = LoadEnvironment.workingDir+LoadEnvironment.INPUTSHEET;


	public  String[][] getExcelData(String WorkBookPath,String shtName,String ScriptName)
			throws Exception {
		Workbook workbk = Workbook.getWorkbook(new File(WorkBookPath));
		Sheet sht = workbk.getSheet(shtName);
		rowCount = sht.getRows();
		colCount = sht.getColumns();
		int counter=0;
		int counter2=0;
		ReadExcelSheet RX = new ReadExcelSheet(null);
		Sheet sheet = RX.Excel(WorkBookPath, shtName);
		int row = sheet.getRows();
		int column = 0;
		column = sheet.findCell("SCRIPT_ID", 0, 0, sheet.getColumns(),
				sheet.getRows(), false).getColumn();

		for(int i =0 ; i<row;i++){
			if(sht.getCell(column, i).getContents().equalsIgnoreCase(ScriptName)){
				counter+=1;
			}
		}
		tabArray = new String[counter][2];
		for(int i =0 ; i<row;i++){
			if(sht.getCell(column, i).getContents().equalsIgnoreCase(ScriptName)){
				tabArray[counter2][0]=ScriptName;
				tabArray[counter2][1]=Integer.toString(i);
				counter2++;
			}
		}
		return (tabArray);
	}}

