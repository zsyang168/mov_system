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
			// ����һ����д���excel�ļ�,���õ�"������"����
			WritableWorkbook w_workbook = Workbook.createWorkbook(file);
			// ʹ�õ�һ��"������"(�±�Ϊ0)
			WritableSheet w_sheet = w_workbook.createSheet("Sheet1",0);
			// ���һ�еĵ�Ԫ����������������ñ�ͷ
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
			// �رն����ͷ���Դ
			w_workbook.write();
			w_workbook.close();
		} catch (Exception e) {
			flag=false;
			JOptionPane.showMessageDialog(null, "����д��ʧ�ܣ�\n"+e, "��ʾ",
					JOptionPane.WARNING_MESSAGE);
		}
		return flag;
	}
}
