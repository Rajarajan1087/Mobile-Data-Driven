package com.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;

import org.omg.CORBA.StringHolder;

import com.Engine.Reporter;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ReadExcelSheet{
	public Reporter Report;
	public ReadExcelSheet(Reporter report) {
		Report=report;
	}
	//String sheetPath = LoadEnvironment.workingDir+LoadEnvironment.INPUTSHEET;

	public Sheet Excel(String filename, String sheetname) throws Exception {
		Sheet sheet = null;
		try {
			//System.out.println("input file name  "+filename);System.out.println("InputSheet name  "+sheetname);
			WorkbookSettings settings = new WorkbookSettings();
			settings.setLocale(new Locale("en", "EN"));

			settings.setNamesDisabled(true);
			settings.setFormulaAdjust(true);
			settings.setMergedCellChecking(true);
			settings.setCellValidationDisabled(true);
			settings.setSuppressWarnings(true);

			Workbook workbook = Workbook.getWorkbook(new File(filename),
					settings);
			sheet = workbook.getSheet(sheetname);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("Failed to read from Excel file", "ReadExcel");
			e.printStackTrace();
		}

		if (sheet == null) {
			System.out.println("NULL SHEET");
		}
		return sheet;
	}
	public String ReadFromExcel(String WorkbookLocation,String SheetName, String TestScript,
			String PARAMETER) throws Exception {

		Sheet sheet = Excel(WorkbookLocation, SheetName);

		int row = 0;
		int column = 0;
		row = sheet.findCell(TestScript, 0, 0, sheet.getColumns(),
				sheet.getRows(), false).getRow();
		column = sheet.findCell(PARAMETER, 0, 0, sheet.getColumns(),
				sheet.getRows(), false).getColumn();
		String iterations = sheet.getCell(column, row).getContents();
		System.out.println("the Column is ->"+PARAMETER+" the parameter value is ->"+iterations);
		return iterations;

	}
	public String ReadFromExcelWithRows(String WorkbookLocation,String SheetName, String Row,
			String PARAMETER) throws Exception {
		try
		{
			Sheet sheet = Excel(WorkbookLocation, SheetName);
			int column = 0;
			int row = Integer.parseInt(Row);
			column = sheet.findCell(PARAMETER, 0, 0, sheet.getColumns(),sheet.getRows(), false).getColumn();
			System.out.println("The column named "+PARAMETER+" is present in the sheet "+SheetName);
			String iterations = sheet.getCell(column,row).getContents();
			return iterations;
		}
		catch(Exception E)
		{
			E.printStackTrace();
			Report.fnReportFailAndTerminateTest("Excel Reading", "Exception in Reading Excel File");
			return null;
		}
	}
	public void WriteToExcelWithRows(String WorkbookLocation,String SheetName,String COLUMN, String ROW,String PARAMETER) throws Exception {

		Workbook existingWorkbook = null;
		int Row=Integer.valueOf(ROW);
		try {
			
			WorkbookSettings settings = new WorkbookSettings();
			settings.setLocale(new Locale("en", "EN"));

			settings.setNamesDisabled(true);
			settings.setFormulaAdjust(true);
			settings.setMergedCellChecking(true);
			settings.setCellValidationDisabled(true);
			settings.setSuppressWarnings(true);
			existingWorkbook = Workbook.getWorkbook(new File(WorkbookLocation),settings);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("WorkBook Not Found at "+WorkbookLocation, "ReadExcel");
			e.printStackTrace();
		}
		String Sample=System.getProperty("user.dir")+"/DataProvider/Samplekjh.xls";
		WritableWorkbook workbookCopy = Workbook.createWorkbook(new File(Sample), existingWorkbook);
		existingWorkbook.close();
		WritableSheet sheetToEdit = workbookCopy.getSheet(SheetName);
		WritableCell cell;
		String[] Columns=COLUMN.split(",");
		for(int i=0;i<Columns.length;i++)
		{
			System.out.println("SheetToEdit:"+sheetToEdit.getName());
			int column = sheetToEdit.findCell(Columns[i], 0, 0, sheetToEdit.getColumns(),
				sheetToEdit.getRows(), false).getColumn();
			column=1;
			System.out.println("("+Row+","+column+")");
			Label l = new Label(column, Row, PARAMETER);
			cell = (WritableCell) l;
			sheetToEdit.addCell(cell);
			workbookCopy.write();
		}
		existingWorkbook.close();
		workbookCopy.close();
		new File(WorkbookLocation).delete();
		File f1=new File(WorkbookLocation);
		File f2=new File(Sample);
		f2.renameTo(f1);
	}
	public String[][] ReadFile(String FileLocation,String Delimiter)
	{
		String[][] Output = null;
		BufferedReader br = null;
		String line = "";
		int X=0,Y=0;
		try {

			br = new BufferedReader(new FileReader(FileLocation));
			while ((line=br.readLine()) != null) {
				X++;
				if(Y==0)
				{
					Y=line.split(Delimiter).length;
				}
			}
			Output=new String[X][Y];
			br = new BufferedReader(new FileReader(FileLocation));
//				System.out.println(line);
				for(int i=0;i<X;i++)
				{
					line=br.readLine();
					for(int j=0;j<Y;j++)
					{
						String[] Data = line.split(Delimiter);
						Output[i][j]=Data[j];
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		return Output;
	}
}
