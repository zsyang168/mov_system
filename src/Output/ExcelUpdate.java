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
		     POIFSFileSystem ps=new POIFSFileSystem(fs);  //使用POI提供的方法得到excel的信息  
		     HSSFWorkbook wb=new HSSFWorkbook(ps);    
		     HSSFSheet sheet=wb.getSheetAt(0);  //获取到工作表，因为一个excel可能有多个工作表 
		     //设置单元格格式
		     HSSFCellStyle cellStyle = wb.createCellStyle();  
		     cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.0"));
			 int b = sheet.getLastRowNum();//获取总行数
			 FileOutputStream out=new FileOutputStream(file); 
		     for(int i=1;i<=b;i++)  //对某一列写人,如第五列
		     {
			    HSSFRow row=sheet.getRow(i);
			    HSSFCell cell=row.getCell(5);
			    cell.setCellValue(Double.valueOf(data[i-1]));    //更改值,如add
			    cell.setCellStyle(cellStyle);   //更改值,如add
			    
		     }
		     out.flush();  //缓冲
		     wb.write(out); //数据写入 
		     out.close();//关闭输出流
		     wb.close();
		     fs.close();//关闭输入流
		  }catch (Exception e) {
			    flag=false;
			    e.printStackTrace();
				JOptionPane.showMessageDialog(null, "数据更新失败！\n"+e, "提示",
						JOptionPane.WARNING_MESSAGE);
		 }
		return flag;
	}
}
