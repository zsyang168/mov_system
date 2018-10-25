package Input;
import java.io.File;

import javax.swing.JOptionPane;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

//excel������ݶ���
	public class Read{
        // ��ȡexcel��ָ����Ԫ������
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

		// ��ȡexcel�ļ�һ������
		public String[] readLine(Sheet sheet, int row) {
			try {
				// ��ȡ���ݱ�����
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
		//���ر������
		public int Row(File file)
		{
			 Workbook wb = null;
			 Sheet mysheet;
			 try{
			// ��һ��excel�ļ�,���õ�"������"����
				wb = Workbook.getWorkbook(file);
				// �õ���һ��������,�±�Ϊ0
			mysheet = wb.getSheet(0);
			int row=mysheet.getRows();
			return row;
			 } catch (Exception e) {
					JOptionPane.showMessageDialog(null, "������ʧ�ܣ�\n"+e, "��ʾ",
							JOptionPane.WARNING_MESSAGE);
					
			} finally {
					wb.close();// �رչ�����
					
			}
			 return 0;	
		}
		//���ر��ÿһ�е�����
		public String[] XlsRead(File file,int r) {
			// ������������������
			 Workbook wb = null;
			 Sheet mysheet;
			 String ssTemp[]=null;
			try {
				// ��һ��excel�ļ�,���õ�"������"����
				wb = Workbook.getWorkbook(file);
				// �õ���һ��������,�±�Ϊ0
				mysheet = wb.getSheet(0);
				ssTemp=readLine(mysheet, r);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "������ʧ�ܣ�\n"+e, "��ʾ",
						JOptionPane.WARNING_MESSAGE);
			} finally {
				wb.close();// �رչ�����
			}
			return ssTemp;
		}
}