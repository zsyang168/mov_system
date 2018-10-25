package Output;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import Input.Data;

public class OrderOutput {
	// excel表数据(order)
		private String PONumber; // 订单号,只需在最后结表中使用
		private String Item; // 订单item,只需在结果表中使用
		private String UnitMLFB; // mlfb,只需在结果表中使用
		public int PlaQty; // 订单生产数量,需要加入目标函数计算
		private String PlanID; // planId记录每次运算结果主要内容,每次运算结果新存一个excel表,表名与planid相同
		private double WaveHeight;// 波纹垫片高度
		private String Description;// 摘要
		private int OrderWaveDiskNum;
		private String[][] OrderFillingParts;
		private String OrderOutput[][];
	public OrderOutput() {
		// TODO Auto-generated constructor stub
		PONumber=Data.OrderData.PONumber;
		Item=Data.OrderData.Item;
		UnitMLFB=Data.OrderData.UnitMLFB;
		PlaQty=Data.OrderData.PlaQty;
		PlanID=Data.OrderData.PlanID;
		WaveHeight=Data.OrderData.WaveHeight;
		Description=Data.OrderData.Description;
		OrderWaveDiskNum=Output.OrderWaveDiskNum;
		OrderFillingParts=Output.OrderFillingParts;
		OrderOutput=Output.OrderOutput;
		Output();
	}
	@SuppressWarnings("deprecation")
	public boolean Output() {
		boolean flag = true;
		try {
			File file = new File(Data.OrderOutputPath + ".xls");
			FileInputStream fs = new FileInputStream(file); //
			POIFSFileSystem ps = new POIFSFileSystem(fs); // 使用POI提供的方法得到excel的信息
			HSSFWorkbook wb = new HSSFWorkbook(ps);
			HSSFSheet sheet = wb.getSheetAt(0); // 获取到工作表，因为一个excel可能有多个工作表
			FileOutputStream out = new FileOutputStream(file);
			// PlanId
			HSSFRow row = sheet.getRow(5);
			HSSFCell cell = row.getCell(11);
			cell.setCellValue(this.PlanID);
			// 避雷器型号
			row = sheet.getRow(7);
			cell = row.getCell(4);
			cell.setCellValue(this.Description);
			// 单节避雷器型号
			row = sheet.getRow(8);
			cell = row.getCell(4);
			cell.setCellValue(this.UnitMLFB);
			// 订单号
			row = sheet.getRow(9);
			cell = row.getCell(4);
			cell.setCellValue(this.PONumber);
			// 订单位置
			row = sheet.getRow(10);
			cell = row.getCell(4);
			cell.setCellValue(this.Item);
			// 订单总数
			row = sheet.getRow(11);
			cell = row.getCell(4);
			cell.setCellValue(this.PlaQty);
			// MOV
			for (int i = 0; i < this.OrderOutput.length; i++) // 对某一列写人,如第五列
			{
				row = sheet.getRow(i + 8);
				cell = row.getCell(7);
				cell.setCellValue(this.OrderOutput[i][0]); // 更改值,如add
				cell = row.getCell(8);
				cell.setCellValue(this.OrderOutput[i][1]); // 更改值,如add
				cell = row.getCell(9);
				cell.setCellValue(this.OrderOutput[i][2]); // 更改值,如add
				cell = row.getCell(10);
				cell.setCellValue(Double.parseDouble(this.OrderOutput[i][3])); // 更改值,如add
				cell = row.getCell(12);
				cell.setCellValue(Double.parseDouble(this.OrderOutput[i][4])); // 更改值,如add
			}
			for (int i = this.OrderOutput.length; i < 15; i++) // 对某一列写人,如第五列
			{
				row = sheet.getRow(i + 8);
				cell = row.getCell(7);
				cell.setCellValue(""); // 更改值,如add
				cell = row.getCell(8);
				cell.setCellValue(""); // 更改值,如add
				cell = row.getCell(9);
				cell.setCellValue(""); // 更改值,如add
				cell = row.getCell(10);
				cell.setCellType(HSSFCell.CELL_TYPE_BLANK);
				cell = row.getCell(12);
				cell.setCellType(HSSFCell.CELL_TYPE_BLANK);
			}
			// 波纹垫片
			if (this.OrderWaveDiskNum != 0) {
				row = sheet.getRow(24);
				cell = row.getCell(9);
				cell.setCellValue(this.WaveHeight + "mm");
				cell = row.getCell(10);
				cell.setCellValue(this.OrderWaveDiskNum);
			} else {
				row = sheet.getRow(24);
				cell = row.getCell(9);
				cell.setCellType(HSSFCell.CELL_TYPE_BLANK);
				cell = row.getCell(10);
				cell.setCellValue(this.OrderWaveDiskNum);
				cell.setCellType(HSSFCell.CELL_TYPE_BLANK);
			}
			// 填充块
			for (int i = 0; i < this.OrderFillingParts.length; i++) {
				row = sheet.getRow(i + 25);
				cell = row.getCell(9);
				cell.setCellValue(this.OrderFillingParts[i][0]); // 更改值,如add
				cell = row.getCell(10);
				cell.setCellValue(Double.parseDouble(this.OrderFillingParts[i][1])); // 更改值,如add
			}
			if (this.OrderFillingParts.length < 3) {
				for (int i = this.OrderFillingParts.length; i < 3; i++) {
					row = sheet.getRow(i + 25);
					cell = row.getCell(9);
					cell.setCellType(HSSFCell.CELL_TYPE_BLANK);
					cell = row.getCell(10);
					cell.setCellType(HSSFCell.CELL_TYPE_BLANK);
				}
			}
			sheet.setForceFormulaRecalculation(true);
			out.flush(); // 缓冲
			wb.write(out); // 数据写入
			fs.close();// 关闭输入流
			out.close();// 关闭输出流
			wb.close();
		} catch (Exception e) {
			flag = false;
			JOptionPane.showMessageDialog(null, "数据写入失败！\n" + e, "提示", JOptionPane.WARNING_MESSAGE);
		}
		return flag;
	}
}
