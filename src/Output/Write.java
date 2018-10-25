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
			// 第一步，创建一个webbook，对应一个Excel文件  
	        HSSFWorkbook wb = new HSSFWorkbook();  
	        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
	        HSSFSheet sheet = wb.createSheet("Sheet1");  
	        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
	        HSSFRow row = sheet.createRow(0);  
	        HSSFCell cell = row.createCell(1);  
	        // 第四步，创建单元格，并设置值表头 设置表头居中  
	        HSSFCellStyle style = wb.createCellStyle();  
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式 
	        style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框    
	        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
	        style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框    
	        style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
	        //设置表头
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
	         //写入数据
	        HSSFCellStyle style2 = wb.createCellStyle();  
	        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
	        for(int i=0;i<data.length;i++)
			{
	        	row = sheet.createRow(i+2);
	        	cell = row.createCell(0);
				for(int j=0;j<data[i].length;j++)
				{
					cell.setCellValue(data[i][j]);  
		 	        cell.setCellStyle(style2);  
		 	        cell = row.createCell(j+1); 
		 	        sheet.autoSizeColumn(j); //自动调整列宽
				}
			}
	        try  
	        {  
	            FileOutputStream fout = new FileOutputStream(file);  
	            wb.write(fout);  
	            fout.close();  
	            JOptionPane.showMessageDialog(null, "表格写入完毕！\n", "提示",
						JOptionPane.WARNING_MESSAGE);
	        }  
	        catch (Exception e)  
	        {  
	        	flag=false;
				JOptionPane.showMessageDialog(null, "表格写入失败！\n"+e, "提示",
						JOptionPane.WARNING_MESSAGE);
	        }  
		}
		else
		{
			try {
		        //FileInputStream fs=new FileInputStream(path+"result.xls");  //获取result.xls  
				FileInputStream fs=new FileInputStream(file);  //获取result.xls  
		        POIFSFileSystem ps=new POIFSFileSystem(fs);  //使用POI提供的方法得到excel的信息  
		        HSSFWorkbook wb=new HSSFWorkbook(ps);    
		        HSSFSheet sheet=wb.getSheetAt(0);  //获取到工作表，因为一个excel可能有多个工作表  
		        //HSSFRow row=sheet.getRow(0);  //获取第一行（excel中的行默认从0开始，所以这就是为什么，一个excel必须有字段列头），即，字段列头，便于赋值  
		        HSSFCellStyle style = wb.createCellStyle();  
		        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式 
		       // FileOutputStream out=new FileOutputStream(path+"result.xls");  //向result.xls中写数据  
		        FileOutputStream out=new FileOutputStream(file);  //向result.xls中写数据  
		        for(int i=0;i<data.length;i++)
				{
		        	HSSFRow row=sheet.createRow((sheet.getLastRowNum()+1)); //在现有行号后追加数据
		        	HSSFCell cell = row.createCell(0);
		        	for(int j=0;j<data[i].length;j++)
					{
						cell.setCellValue(data[i][j]);  
			 	        cell = row.createCell(j+1); 
			 	        cell.setCellStyle(style);  
					}
				}
		        out.flush();  //缓冲
		        wb.write(out); //数据写入 
		        fs.close();//关闭输入流
		        out.close();//关闭输出流
		        wb.close();
			} catch (Exception e) {
				flag=false;
				JOptionPane.showMessageDialog(null, "表格写入失败！\n"+e, "提示",
						JOptionPane.WARNING_MESSAGE);
			}
		}
		return flag;
	}
}
