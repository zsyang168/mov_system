package Output;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class ExcelUpdate {
	public boolean  dataupdate(File file,String data[])
	{
		boolean flag=true;
		try {
			 FileInputStream fs=new FileInputStream(file);  //  
		     POIFSFileSystem ps=new POIFSFileSystem(fs);  //ʹ��POI�ṩ�ķ����õ�excel����Ϣ  
		     HSSFWorkbook wb=new HSSFWorkbook(ps);    
		     HSSFSheet sheet=wb.getSheetAt(0);  //��ȡ����������Ϊһ��excel�����ж�������� 
		     //���õ�Ԫ���ʽ
		     HSSFCellStyle cellStyle = wb.createCellStyle();  
		     cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.0"));
			 int b = sheet.getLastRowNum();//��ȡ������
			 FileOutputStream out=new FileOutputStream(file); 
		     for(int i=1;i<=b;i++)  //��ĳһ��д��,�������
		     {
			    HSSFRow row=sheet.getRow(i);
			    HSSFCell cell=row.getCell(5);
			    cell.setCellValue(Double.valueOf(data[i-1]));    //����ֵ,��add
			    cell.setCellStyle(cellStyle);   //����ֵ,��add
			    
		     }
		     out.flush();  //����
		     wb.write(out); //����д�� 
		     out.close();//�ر������
		     wb.close();
		     fs.close();//�ر�������
		  }catch (Exception e) {
			    flag=false;
			    e.printStackTrace();
				JOptionPane.showMessageDialog(null, "���ݸ���ʧ�ܣ�\n"+e, "��ʾ",
						JOptionPane.WARNING_MESSAGE);
		 }
		return flag;
	}
}
