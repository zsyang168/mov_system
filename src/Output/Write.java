package Output;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

public class Write{
	public boolean XlsWrite(String[][] data,String path){
		boolean flag=true;
		File file=new File(path+".xls");
		if(!file.exists())
		{
			// ��һ��������һ��webbook����Ӧһ��Excel�ļ�  
	        HSSFWorkbook wb = new HSSFWorkbook();  
	        // �ڶ�������webbook�����һ��sheet,��ӦExcel�ļ��е�sheet  
	        HSSFSheet sheet = wb.createSheet("Sheet1");  
	        // ����������sheet����ӱ�ͷ��0��,ע���ϰ汾poi��Excel����������������short  
	        HSSFRow row = sheet.createRow(0);  
	        HSSFCell cell = row.createCell(1);  
	        // ���Ĳ���������Ԫ�񣬲�����ֵ��ͷ ���ñ�ͷ����  
	        HSSFCellStyle style = wb.createCellStyle();  
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // ����һ�����и�ʽ 
	        style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //�±߿�    
	        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//��߿�    
	        style.setBorderTop(HSSFCellStyle.BORDER_THIN);//�ϱ߿�    
	        style.setBorderRight(HSSFCellStyle.BORDER_THIN);//�ұ߿�
	        //���ñ�ͷ
	        cell.setCellValue("Order Info.");
		    cell.setCellStyle(style);  
		    cell = row.createCell((short) 8);
		    cell.setCellValue("MOV Config.");
		    cell.setCellStyle(style);  
		    CellRangeAddress region = new CellRangeAddress(0,0,1,7);  
		    CellRangeAddress region2 = new CellRangeAddress(0,0,8,15);
	        sheet.addMergedRegion(region);  
	       
	        RegionUtil.setBorderBottom(1,region, sheet, wb);   
	        RegionUtil.setBorderLeft(1,region, sheet, wb);   
	        RegionUtil.setBorderRight(1,region, sheet, wb);   
	        RegionUtil.setBorderTop(1,region, sheet, wb); 
	        
	        sheet.addMergedRegion(region2);    
	        RegionUtil.setBorderBottom(1,region2, sheet, wb);   
	        RegionUtil.setBorderLeft(1,region2, sheet, wb);   
	        RegionUtil.setBorderRight(1,region2, sheet, wb);   
	        RegionUtil.setBorderTop(1,region2, sheet, wb);
	        String Header[]={"Planid","Order","Item","MLFB","OrderQty","Min RV","Max RV","MaxHeight",
	        "MOV","MOV type","MOVHeight","Class","RV","MOVQty","ActualHeight","Actual RV","Description"};
	        row = sheet.createRow(1);
	        cell = row.createCell(0);
	        for(int i=0;i<17;i++)
	        {
	        	cell.setCellValue(Header[i]);  
	 	        cell.setCellStyle(style);  
	 	        cell = row.createCell(i+1);
	        } 
	         //д������
	        HSSFCellStyle style2 = wb.createCellStyle();  
	        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER); // ����һ�����и�ʽ
	        for(int i=0;i<data.length;i++)
			{
	        	row = sheet.createRow(i+2);
	        	cell = row.createCell(0);
				for(int j=0;j<data[i].length;j++)
				{
					cell.setCellValue(data[i][j]);  
		 	        cell.setCellStyle(style2);  
		 	        cell = row.createCell(j+1); 
		 	        sheet.autoSizeColumn(j); //�Զ������п�
				}
			}
	        try  
	        {  
	            FileOutputStream fout = new FileOutputStream(file);  
	            wb.write(fout);  
	            fout.close();  
	            JOptionPane.showMessageDialog(null, "���д����ϣ�\n", "��ʾ",
						JOptionPane.WARNING_MESSAGE);
	        }  
	        catch (Exception e)  
	        {  
	        	flag=false;
				JOptionPane.showMessageDialog(null, "���д��ʧ�ܣ�\n"+e, "��ʾ",
						JOptionPane.WARNING_MESSAGE);
	        }  
		}
		else
		{
			try {
		        //FileInputStream fs=new FileInputStream(path+"result.xls");  //��ȡresult.xls  
				FileInputStream fs=new FileInputStream(file);  //��ȡresult.xls  
		        POIFSFileSystem ps=new POIFSFileSystem(fs);  //ʹ��POI�ṩ�ķ����õ�excel����Ϣ  
		        HSSFWorkbook wb=new HSSFWorkbook(ps);    
		        HSSFSheet sheet=wb.getSheetAt(0);  //��ȡ����������Ϊһ��excel�����ж��������  
		        //HSSFRow row=sheet.getRow(0);  //��ȡ��һ�У�excel�е���Ĭ�ϴ�0��ʼ�����������Ϊʲô��һ��excel�������ֶ���ͷ���������ֶ���ͷ�����ڸ�ֵ  
		        HSSFCellStyle style = wb.createCellStyle();  
		        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // ����һ�����и�ʽ 
		       // FileOutputStream out=new FileOutputStream(path+"result.xls");  //��result.xls��д����  
		        FileOutputStream out=new FileOutputStream(file);  //��result.xls��д����  
		        for(int i=0;i<data.length;i++)
				{
		        	HSSFRow row=sheet.createRow((sheet.getLastRowNum()+1)); //�������кź�׷������
		        	HSSFCell cell = row.createCell(0);
		        	for(int j=0;j<data[i].length;j++)
					{
						cell.setCellValue(data[i][j]);  
			 	        cell = row.createCell(j+1); 
			 	        cell.setCellStyle(style);  
					}
				}
		        out.flush();  //����
		        wb.write(out); //����д�� 
		        fs.close();//�ر�������
		        out.close();//�ر������
		        wb.close();
			} catch (Exception e) {
				flag=false;
				JOptionPane.showMessageDialog(null, "���д��ʧ�ܣ�\n"+e, "��ʾ",
						JOptionPane.WARNING_MESSAGE);
			}
		}
		return flag;
	}
}
