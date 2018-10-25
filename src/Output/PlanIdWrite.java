package Output;

import java.io.File;
import javax.swing.JOptionPane;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class PlanIdWrite{
	public boolean XlsWrite(String[][] data,File file){
		boolean flag=true;
		try {
			// 创建一个可写入的excel文件,并得到"工作簿"对象
			WritableWorkbook w_workbook = Workbook.createWorkbook(file);
			// 使用第一张"工作表"(下标为0)
			WritableSheet w_sheet = w_workbook.createSheet("Sheet1",0);
			// 向第一行的单元格添加数据用来设置表头
			Label label0 = new Label(0, 0, "PlanID");
			w_sheet.addCell(label0);
			Label label1 = new Label(1, 0, "MOVType");
			w_sheet.addCell(label1);
			Label label2 = new Label(2, 0, "subType");
			w_sheet.addCell(label2);
			Label label3 = new Label(3, 0, "class");
			w_sheet.addCell(label3);
			Label label4 = new Label(4, 0, "MOVQty");
			w_sheet.addCell(label4);
			Label label5 = new Label(5, 0, "Description");
			w_sheet.addCell(label5);
			for(int i=0;i<data.length;i++)
			{
				for(int j=0;j<data[i].length;j++)
				{
					Label label = new Label(j, i+1,data[i][j]);
					w_sheet.addCell(label);
				}
			}
			// 关闭对象，释放资源
			w_workbook.write();
			w_workbook.close();
		} catch (Exception e) {
			flag=false;
			JOptionPane.showMessageDialog(null, "数据写入失败！\n"+e, "提示",
					JOptionPane.WARNING_MESSAGE);
		}
		return flag;
	}
}
