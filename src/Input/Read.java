package Input;
import java.io.File;

import javax.swing.JOptionPane;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

//excel表格数据读入
	public class Read{
        // 读取excel的指定单元格数据
		public String read(Sheet sheet, int col, int row) {
			try {
				Cell cell = sheet.getCell(col, row);
				String rest = cell.getContents();
				return rest;
			} catch (Exception e) {
				System.out.println("1");
				System.out.println("read err:" + e);
				return null;
			}
		}

		// 读取excel文件一行数据
		public String[] readLine(Sheet sheet, int row) {
			try {
				// 获取数据表列数
				int colnum = sheet.getColumns();
				String[] rest = new String[colnum];
				for (int i = 0; i < colnum; i++) {
					String sTemp = read(sheet, i, row);
					if (sTemp != null)
						rest[i] = sTemp;
				}
				return rest;
			} catch (Exception e) {
				System.out.println("2");
				System.out.println("readLine err:" + e);
				return null;
			}
		}
		//返回表格行数
		public int Row(File file)
		{
			 Workbook wb = null;
			 Sheet mysheet;
			 try{
			// 打开一个excel文件,并得到"工作簿"对象
				wb = Workbook.getWorkbook(file);
				// 得到第一个工作表,下标为0
			mysheet = wb.getSheet(0);
			int row=mysheet.getRows();
			return row;
			 } catch (Exception e) {
					JOptionPane.showMessageDialog(null, "表格读入失败！\n"+e, "提示",
							JOptionPane.WARNING_MESSAGE);
					
			} finally {
					wb.close();// 关闭工作簿
					
			}
			 return 0;	
		}
		//返回表格每一行的数据
		public String[] XlsRead(File file,int r) {
			// 声明工作簿、工作表
			 Workbook wb = null;
			 Sheet mysheet;
			 String ssTemp[]=null;
			try {
				// 打开一个excel文件,并得到"工作簿"对象
				wb = Workbook.getWorkbook(file);
				// 得到第一个工作表,下标为0
				mysheet = wb.getSheet(0);
				ssTemp=readLine(mysheet, r);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "表格读入失败！\n"+e, "提示",
						JOptionPane.WARNING_MESSAGE);
			} finally {
				wb.close();// 关闭工作簿
			}
			return ssTemp;
		}
}