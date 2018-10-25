package Output;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;

import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import Input.Data;
import main.MOVGUI;
import main.MOVMain;

public class Output {
	// 结果
	private static String[][] resultoutput1;
	private static String[][] resultoutput2;
	public static String OrderOutput[][];
	public static String OrderFillingParts[][];
	public static int OrderWaveDiskNum;
	public static int ResultOutputRowCount = 0;
	//
	private String[][] data;
	private double[] StockQty;
	private static String[] FStockQty;// 库存备份
	private int[] SortIndex;// 按库存排列后下标
	private int[] Used;// 使用求解下标
	private String[] OrderData;
	public Output() {
		// TODO Auto-generated constructor stub
		OrderData=Data.LOrderData;
		data=Data.Tabledata;
		StockQty=MOVMain.StockQty;
		SortIndex=MOVMain.SortIndex;
		Used=MOVMain.Used;	
	}
	//
	public void Write(double result[][]) {
		boolean flag = false;
		ResultOutput(result);
		MOVGUI.setProgressString(95, "正在导出结果...95%");
		if (new Write().XlsWrite(Output.resultoutput1, Data.MovResultPath))// 结果写入表格
		{
			if (QtyUpdate()) {
				if (new OrderOutput().Output()) {
					File file = new File(Data.PlanIdPath + Data.OrderData.PlanID + ".xls");
					if (new PlanIdWrite().XlsWrite(Output.resultoutput2, file)) {
						flag = true;
						MOVMain.RebSumit = false;
						MOVMain.RestoreFlag = false;
					}
				} // if(Output())
				else {
					OutputRemove();// 撒回写入
					QtyRestore();// 库存还原
				} // if(Output())

			} // if(QtyUpdate())
			else {
				OutputRemove();// 撒回写入
			} // if(QtyUpdate())
		} // if(new Write().XlsWrite(this.resultoutput1,MovResultPath))//结果写入表格
		if (flag) {
			MOVGUI.setProgressString(100, "结果导出完毕!");
			// 求解完成，求解失败为假
			MOVMain.SolveFaile = false;
		}

		else
			MOVGUI.setProgressError("结果导出失败！");
		// JOptionPane.showMessageDialog(null, "结果导出失败！\n", "提示",
		// JOptionPane.WARNING_MESSAGE);
	}

	// FillinpartsRead
	public String[] FillinpartsRead(String filepath) {
		String[] arrs = null;
		try {
			FileReader fileReader1 = new FileReader(filepath);
			BufferedReader buf1 = new BufferedReader(fileReader1);
			String firstLine = buf1.readLine();
			arrs = firstLine.split(" ");
			buf1.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "填充块数据格式有误！\n" + e, "提示", JOptionPane.WARNING_MESSAGE);
		}
		return arrs;
	}

	// Fillinparts需要计算
	public int[] FillinpartsCount(int arr[], double DiffHeight, double obj) {
		int fillinparts[] = new int[arr.length];
		int Height = (int) (DiffHeight - (QuZheng(obj) + 1) * Data.OrderData.WaveHeight);
		for (int i = 0; i < arr.length; i++) {
			fillinparts[i] = Height / arr[i];
			Height = Height % arr[i];
		}
		return fillinparts;
	}

	// 结果格式化
	public void ResultOutput(double result[][]) {
		int count = 0;
		int temp;
		int indexdata[] = new int[100];// 非零值
		int index[] = new int[100];// 非零值下标
		// 计算非结果数，即解
		for (int i = 0; i < result[0].length; i++) {
			if (QuZheng(result[0][i]) != 0.0) {
				indexdata[count] = QuZheng(result[0][i]);
				index[count] = this.Used[i];
				// 库存更新
				this.StockQty[i] = this.StockQty[i] - indexdata[count] * Data.OrderData.PlaQty;
				count++;
			}

		}
		// 输出还原库存表顺序
		for (int i = 0; i < count - 1; i++)
			for (int j = i + 1; j < count; j++) {
				if (index[j] < index[i]) {
					temp = index[i];
					index[i] = index[j];
					index[j] = temp;
					temp = indexdata[i];
					indexdata[i] = indexdata[j];
					indexdata[j] = temp;

				}
			}
		// 计算实际残压值及实际高度
		String[] result2 = ResultCount(this.data, result);
		String filepath = Data.FillinpartsPath + Data.OrderData.fillngpart + ".txt";
		String[] fillinparts = FillinpartsRead(filepath);
		// 将字符数组转为double数组
		int[] arr = new int[fillinparts.length];
		try {
			for (int i = 0; i < arr.length; i++) {
				arr[i] = Integer.parseInt(fillinparts[i]);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "填充块数据格式有误！\n" + e, "提示", JOptionPane.WARNING_MESSAGE);
		}
		int[] FillinpartsResult = FillinpartsCount(arr, Data.OrderData.P_MaxHeight - Double.parseDouble(result2[0]),
				result[1][0]);
		int count2 = 0;
		int FillinpartsData[] = new int[10];
		int FillinpartsIndex[] = new int[10];
		// 计算非结果数，即解
		for (int i = 0; i < FillinpartsResult.length; i++) {
			if (FillinpartsResult[i] != 0) {
				FillinpartsData[count2] = FillinpartsResult[i];
				FillinpartsIndex[count2] = i;
				count2++;
			}

		}
		if (Data.OrderData.WaveDisk) {

			String output[][] = new String[count + count2 + 1][];
			Output.ResultOutputRowCount = count + count2 + 1;
			// 结果
			for (int i = 0; i < count + count2 + 1; i++) {
				output[i] = new String[17];
				output[i][0] = Data.OrderData.PlanID;// planid
				for (int j = 1; j < 8; j++) {
					// PONumber Item UnitMLFB PlaQty MinRV MaxRV P_MaxHeight
					output[i][j] = this.OrderData[j - 1];
				}
				if (i < count) {
					for (int j = 8; j < 13; j++) {
						// MOV MOV type MOVHeight Class RV
						output[i][j] = this.data[index[i]][j - 8];
					}
					output[i][13] = Double.toString(indexdata[i]);// 用料数量
					output[i][14] = result2[0];// 实际高度
					output[i][15] = result2[1];// 实际残压值
					output[i][16] = Data.OrderData.Description;// 摘要
				} else if (i < count + count2) {
					output[i][8] = output[0][8];// MOV
					output[i][9] = "fillingparts";
					output[i][10] = fillinparts[FillinpartsIndex[i - count]];
					output[i][11] = fillinparts[FillinpartsIndex[i - count]] + "mm";
					output[i][12] = "0";
					output[i][13] = String.valueOf(FillinpartsData[i - count]);
					output[i][14] = null;
					output[i][15] = null;
					output[i][16] = Data.OrderData.Description;// 摘要

				} else {
					output[i][8] = output[0][8];// MOV
					output[i][9] = "disk";
					output[i][10] = String.valueOf(Data.OrderData.WaveHeight);
					output[i][11] = String.valueOf(Data.OrderData.WaveHeight) + "mm";
					output[i][12] = "0";
					output[i][13] = String.valueOf(QuZheng(Object(result)) + Data.OrderData.WaveNum);
					output[i][14] = null;
					output[i][15] = null;
					output[i][16] = Data.OrderData.Description;// 摘要

				}
			} // for(int i=0;i<count+count2;i++)
			String output2[][] = new String[count + count2 + 1][];
			// 结果
			for (int i = 0; i < count + count2 + 1; i++) {
				output2[i] = new String[6];
				output2[i][0] = Data.OrderData.PlanID;// planid
				if (i < count) {

					output2[i][1] = this.data[index[i]][0];// MOVType
					output2[i][2] = this.data[index[i]][1];// subType
					output2[i][3] = this.data[index[i]][3];// class
					output2[i][4] = Double.toString(indexdata[i]);// 用料数量
					output2[i][5] = Data.OrderData.Description;// 摘要

				} else if (i < count + count2) {
					output2[i][1] = output2[0][1];// MOVType
					output2[i][2] = "fillingparts";
					output2[i][3] = fillinparts[FillinpartsIndex[i - count]] + "mm";
					output2[i][4] = String.valueOf(FillinpartsData[i - count]);
					output2[i][5] = Data.OrderData.Description;// 摘要

				} else {
					output2[i][1] = output2[0][1];// MOVType
					output2[i][2] = "disk";
					output2[i][3] = Data.OrderData.WaveHeight + "mm";
					output2[i][4] = String.valueOf(QuZheng(Object(result)) + Data.OrderData.WaveNum);
					output2[i][5] = Data.OrderData.Description;// 摘要
				}
			}
			String output3[][] = new String[count][];
			for (int i = 0; i < count; i++) {
				output3[i] = new String[5];
				output3[i][0] = this.data[index[i]][0];// MOVType
				output3[i][1] = this.data[index[i]][1];// subType
				output3[i][2] = this.data[index[i]][3];// class
				output3[i][3] = Double.toString(indexdata[i]);// 用料数量
				output3[i][4] = this.data[index[i]][4];// RV
			}
			String output4[][] = new String[count2][];
			for (int i = 0; i < count2; i++) {
				output4[i] = new String[2];
				output4[i][0] = fillinparts[FillinpartsIndex[i]] + "mm";
				output4[i][1] = String.valueOf(FillinpartsData[i]);
			}
			// 结果
			Output.resultoutput1 = output;
			Output.resultoutput2 = output2;
			Output.OrderOutput = output3;
			Output.OrderFillingParts = output4;
			Output.OrderWaveDiskNum = QuZheng(Object(result)) + Data.OrderData.WaveNum;

		} else {
			String output[][] = new String[count + count2][];
			Output.ResultOutputRowCount = count + count2;
			// 结果
			for (int i = 0; i < count + count2; i++) {
				output[i] = new String[17];
				output[i][0] = Data.OrderData.PlanID;// planid
				for (int j = 1; j < 8; j++) {
					// PONumber Item UnitMLFB PlaQty MinRV MaxRV P_MaxHeight
					output[i][j] = this.OrderData[j - 1];
				}
				if (i < count) {
					for (int j = 8; j < 13; j++) {
						// MOV MOV type MOVHeight Class RV
						output[i][j] = this.data[index[i]][j - 8];
					}
					output[i][13] = Double.toString(indexdata[i]);// 用料数量
					output[i][14] = result2[0];// 实际高度
					output[i][15] = result2[1];// 实际残压值
					output[i][16] = Data.OrderData.Description;// 摘要
				} else {
					output[i][8] = output[0][8];// MOV
					output[i][9] = "fillingparts";
					output[i][10] = fillinparts[FillinpartsIndex[i - count]];
					output[i][11] = fillinparts[FillinpartsIndex[i - count]] + "mm";
					output[i][12] = "0";
					output[i][13] = String.valueOf(FillinpartsData[i - count]);
					output[i][14] = null;
					output[i][15] = null;
					output[i][16] = Data.OrderData.Description;// 摘要
				}
			} // for(int i=0;i<count+count2;i++)
			String output2[][] = new String[count + count2][];
			// 结果
			for (int i = 0; i < count + count2; i++) {
				output2[i] = new String[6];
				output2[i][0] = Data.OrderData.PlanID;// planid
				if (i < count) {

					output2[i][1] = data[index[i]][0];// MOVType
					output2[i][2] = data[index[i]][1];// subType
					output2[i][3] = data[index[i]][3];// class
					output2[i][4] = Double.toString(indexdata[i]);// 用料数量
					output2[i][5] = Data.OrderData.Description;// 摘要

				} else {
					output2[i][1] = output2[0][1];// MOVType
					output2[i][2] = "fillingparts";
					output2[i][3] = fillinparts[FillinpartsIndex[i - count]] + "mm";
					output2[i][4] = String.valueOf(FillinpartsData[i - count]);
					output2[i][5] = Data.OrderData.Description;// 摘要

				}
			}
			String output3[][] = new String[count][];
			for (int i = 0; i < count; i++) {
				output3[i] = new String[5];
				output3[i][0] = this.data[index[i]][0];// MOVType
				output3[i][1] = this.data[index[i]][1];// subType
				output3[i][2] = this.data[index[i]][3];// class
				output3[i][3] = Double.toString(indexdata[i]);// 用料数量
				output3[i][4] = this.data[index[i]][4];// RV
			}
			String output4[][] = new String[count2][];
			for (int i = 0; i < count2; i++) {
				output4[i] = new String[2];
				output4[i][0] = fillinparts[FillinpartsIndex[i]] + "mm";
				output4[i][1] = String.valueOf(FillinpartsData[i]);
			}
			// 结果
			Output.resultoutput1 = output;
			Output.resultoutput2 = output2;
			Output.OrderOutput = output3;
			Output.OrderFillingParts = output4;
			Output.OrderWaveDiskNum = 0;
		}

	}

	// 高度&残压值计算
	public String[] ResultCount(String data[][], double data2[][]) {
		// data excel表格数据,data2 计算结果数据
		String result[] = new String[2];
		double sum[] = new double[2];// 计算实际残压值及实际高度，初始化置0
		sum[0] = 0.0;
		sum[1] = 0.0;
		int row = data2[0].length;
		// data[i][2] 高度， data[i][4] 残压
		for (int i = 0; i < row; i++) {
			sum[0] = sum[0] + Double.parseDouble(data[this.Used[i]][2]) * data2[0][i];
			sum[1] = sum[1] + Double.parseDouble(data[this.Used[i]][4]) * data2[0][i];
		}
		// 数据转String型
		sum[0] = Round(sum[0], 2);// 四舍五入保留2位小数
		sum[1] = Round(sum[1], 2);// 四舍五入保留2位小数
		result[0] = Double.toString(sum[0]);
		result[1] = Double.toString(sum[1]);
		return result;
	}

	// 四舍五入保留l位小数
	public double Round(double d, int l) {
		d = Math.round((d * Math.pow(10, l)));
		d = d / Math.pow(10, l);
		return d;
	}

	// 撒回功能
	public static boolean Restore() {
		boolean flag = false;
		if (OutputRemove())// 撒回写入
		{
			if (QtyRestore())// 库存还原
			{
				if (DeleteExcel())// 删除PLANID表
				{
					flag = true;
				}
			} else {
				new Write().XlsWrite(Output.resultoutput1, Data.MovResultPath);// 库存还原失败，重新写入
			}
		}
		return flag;
	}

	// 库存备份
	public void QtyBackups() {
		int l = this.data.length;
		String Sto[] = new String[l - 1];
		for (int i = 1; i < l; i++)
			Sto[i - 1] = this.data[i][5];
		// 备份当前库存
		Output.FStockQty = Sto;
	}

	// 库存更新
	public boolean QtyUpdate() {
		QtyBackups();
		int l = this.SortIndex.length;
		String update[] = new String[l];
		for (int i = 0; i < l; i++) {
			update[this.SortIndex[i] - 1] = String.valueOf(this.StockQty[i]);
		}
		File file = new File(Data.InventoryPath + Data.OrderData.MOVType + ".xls");// 读取对应excel表
		return new ExcelUpdate().dataupdate(file, update);
	}

	// 库存还原
	public static boolean QtyRestore() {
		File file = new File(Data.InventoryPath + Data.OrderData.MOVType + ".xls");// 读取对应excel表
		return new ExcelUpdate().dataupdate(file,FStockQty);
	}

	// 删除表格
	public static boolean DeleteExcel() {
		boolean flag = false;
		File file = new File(Data.PlanIdPath + Data.OrderData.PlanID + ".xls");
		// 判断目录或文件是否存在
		if (!file.exists()) { // 不存在返回 false
			return flag;
		} else {
			// 判断是否为文件
			if (file.isFile()) { // 为文件时调用删除文件方法
				file.delete();
				flag = true;
			}
		}
		return flag;
	}

	// 删除写入
	public static boolean OutputRemove() {
		boolean flag = true;
		try {
			File file = new File(Data.MovResultPath + ".xls");
			FileInputStream fs = new FileInputStream(file); // 获取result.xls
			POIFSFileSystem ps = new POIFSFileSystem(fs); // 使用POI提供的方法得到excel的信息
			HSSFWorkbook wb = new HSSFWorkbook(ps);
			HSSFSheet sheet = wb.getSheetAt(0);
			for (int i = 0; i < ResultOutputRowCount; i++) {
				HSSFRow row = sheet.createRow((sheet.getLastRowNum())); // 获取最后一行
				sheet.removeRow(row);// 删除最后一行
			}
			FileOutputStream out = new FileOutputStream(file); // 向result.xls中写数据
			out.flush(); // 缓冲
			wb.write(out); // 数据写入
			fs.close();// 关闭输入流
			out.close();// 关闭输出流
			wb.close();
		} catch (Exception ee) {
			flag = false;
		}
		return flag;
	}

	//
	public double Object(double result[][]) {
		double sum = 0;
		for (int i = 0; i < result[0].length; i++)
			sum += result[0][i];
		return sum;
	}

	//
	public int QuZheng(double d) {
		int t = (int) d;
		if (Math.abs(t + 1 - d) < 0.00001)
			return t + 1;
		else
			return t;
	}

}
