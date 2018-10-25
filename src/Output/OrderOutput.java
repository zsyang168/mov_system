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
	// excel������(order)
		private String PONumber; // ������,ֻ�����������ʹ��
		private String Item; // ����item,ֻ���ڽ������ʹ��
		private String UnitMLFB; // mlfb,ֻ���ڽ������ʹ��
		public int PlaQty; // ������������,��Ҫ����Ŀ�꺯������
		private String PlanID; // planId��¼ÿ����������Ҫ����,ÿ���������´�һ��excel��,������planid��ͬ
		private double WaveHeight;// ���Ƶ�Ƭ�߶�
		private String Description;// ժҪ
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
			POIFSFileSystem ps = new POIFSFileSystem(fs); // ʹ��POI�ṩ�ķ����õ�excel����Ϣ
			HSSFWorkbook wb = new HSSFWorkbook(ps);
			HSSFSheet sheet = wb.getSheetAt(0); // ��ȡ����������Ϊһ��excel�����ж��������
			FileOutputStream out = new FileOutputStream(file);
			// PlanId
			HSSFRow row = sheet.getRow(5);
			HSSFCell cell = row.getCell(11);
			cell.setCellValue(this.PlanID);
			// �������ͺ�
			row = sheet.getRow(7);
			cell = row.getCell(4);
			cell.setCellValue(this.Description);
			// ���ڱ������ͺ�
			row = sheet.getRow(8);
			cell = row.getCell(4);
			cell.setCellValue(this.UnitMLFB);
			// ������
			row = sheet.getRow(9);
			cell = row.getCell(4);
			cell.setCellValue(this.PONumber);
			// ����λ��
			row = sheet.getRow(10);
			cell = row.getCell(4);
			cell.setCellValue(this.Item);
			// ��������
			row = sheet.getRow(11);
			cell = row.getCell(4);
			cell.setCellValue(this.PlaQty);
			// MOV
			for (int i = 0; i < this.OrderOutput.length; i++) // ��ĳһ��д��,�������
			{
				row = sheet.getRow(i + 8);
				cell = row.getCell(7);
				cell.setCellValue(this.OrderOutput[i][0]); // ����ֵ,��add
				cell = row.getCell(8);
				cell.setCellValue(this.OrderOutput[i][1]); // ����ֵ,��add
				cell = row.getCell(9);
				cell.setCellValue(this.OrderOutput[i][2]); // ����ֵ,��add
				cell = row.getCell(10);
				cell.setCellValue(Double.parseDouble(this.OrderOutput[i][3])); // ����ֵ,��add
				cell = row.getCell(12);
				cell.setCellValue(Double.parseDouble(this.OrderOutput[i][4])); // ����ֵ,��add
			}
			for (int i = this.OrderOutput.length; i < 15; i++) // ��ĳһ��д��,�������
			{
				row = sheet.getRow(i + 8);
				cell = row.getCell(7);
				cell.setCellValue(""); // ����ֵ,��add
				cell = row.getCell(8);
				cell.setCellValue(""); // ����ֵ,��add
				cell = row.getCell(9);
				cell.setCellValue(""); // ����ֵ,��add
				cell = row.getCell(10);
				cell.setCellType(HSSFCell.CELL_TYPE_BLANK);
				cell = row.getCell(12);
				cell.setCellType(HSSFCell.CELL_TYPE_BLANK);
			}
			// ���Ƶ�Ƭ
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
			// ����
			for (int i = 0; i < this.OrderFillingParts.length; i++) {
				row = sheet.getRow(i + 25);
				cell = row.getCell(9);
				cell.setCellValue(this.OrderFillingParts[i][0]); // ����ֵ,��add
				cell = row.getCell(10);
				cell.setCellValue(Double.parseDouble(this.OrderFillingParts[i][1])); // ����ֵ,��add
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
			out.flush(); // ����
			wb.write(out); // ����д��
			fs.close();// �ر�������
			out.close();// �ر������
			wb.close();
		} catch (Exception e) {
			flag = false;
			JOptionPane.showMessageDialog(null, "����д��ʧ�ܣ�\n" + e, "��ʾ", JOptionPane.WARNING_MESSAGE);
		}
		return flag;
	}
}
