package Input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import main.MOVGUI;

public class Data {
	// 文件路径
	public static String OrderPath;
	public static String InventoryPath;
	public static String MovResultPath;
	public static String PlanIdPath;
	public static String FillinpartsPath;
	public static String OrderOutputPath;
	// 表数据
	public static Order OrderData;// order表数据
	public static String[] LOrderData;// order表数据
	public static String[][] Tabledata;// 库存表数据

	public Data() {
		// TODO Auto-generated constructor stub
		//路径初始化
		FilePathInit();
		//数据初始化
		Init();
	}

	//数据初始化
	public void Init() {
		Tabledata=exceldataRead();
		String PL[] = PLInit(Tabledata);
		String message[]=OrderInformationInit();
		MOVGUI.setOptions(PL);
		MOVGUI.setOrderMessage(message);
		MOVGUI.setProgressString(100, "数据读入完毕！"+100+"%");
	}

	// excel表格数据读入
	public String[][] exceldataRead() {

		File file = new File(Data.OrderPath + "order.xls");// 读入order表，获取相应数据
		MOVGUI.setProgressString(10, "正在读入order表数据" + 10 + "%");
		String[] OrderData = new Read().XlsRead(file, 1);
		LOrderData=OrderData;
		NullValueCheck(OrderData);
		Data.OrderData = new Order(OrderData);
		File fr = new File(Data.InventoryPath + Data.OrderData.MOVType + ".xls");// 读取对应excel表
		int row = new Read().Row(fr);// 取得表格行数
		MOVGUI.setProgressString(20, "正在读入库存表数据" + 20 + "%");
		String[][] data2 = new String[row][];
		// 按行读取表格
		for (int i = 0; i < row; i++) {
			data2[i] = new Read().XlsRead(fr, i);
			MOVGUI.setProgressString(20 + 70 * i / row, "正在读入库存表数据" + (20 + 70 * i / row) + "%");
		}
		return data2;

	}

	// 订单信息列表初始化
	public String[] OrderInformationInit() {
		String[] ordermessage =new String[7];
		ordermessage[0] = OrderData.PONumber;
		ordermessage[1] = OrderData.PlanID;
		String str=OrderData.UnitMLFB;
		ordermessage[2] =str.substring(0, 4)+"-"+str.charAt(11) ;
		ordermessage[3] = String.valueOf(OrderData.P_MaxHeight);
		ordermessage[4] = OrderData.MOVType;
		ordermessage[5] = String.valueOf(OrderData.FMinRV);
		ordermessage[6] =String.valueOf(OrderData.MaxRV);
		return ordermessage;
	}

	// 优先级列表初始化
	public String[] PLInit(String[][] data) {
		int l = data.length;
		LinkedList<String> PL = new LinkedList<String>();
		PL.add(data[1][1]);
		// 计算不同MOV类型数
		for (int i = 1; i < l; i++) {
			if (!PL.contains(data[i][1])) {
				PL.add(data[i][1]);
			}
		}
		String[] result = {};
		return PL.toArray(result);
	}

	// order表空值检测
	public void NullValueCheck(String data[]) {
		String Header[] = { "PONumber", "Item", "UnitMLFB", "PlaQty", "MinRV", "MaxRV", "P_MaxHeight", "MOVType",
				"PlanID", "Fillngpart", "Wavedisk", "Waveheight", "Description" };
		for (int i = 0; i < 13; i++) {
			if (data[i].isEmpty()) {
				JOptionPane.showMessageDialog(null, "Order表 " + Header[i] + "不能为空，请填上后再试！\n", "提示",
						JOptionPane.WARNING_MESSAGE);
				System.exit(0);
			}
		}
	}

	// 文件路径初始化
	public void FilePathInit() {
		String FilePath[] = FilePathRead();
		OrderPath = FilePath[1];
		InventoryPath = FilePath[3];
		MovResultPath = FilePath[5];
		PlanIdPath = FilePath[7];
		FillinpartsPath = FilePath[9];
		OrderOutputPath = FilePath[11];
	}

	// 路径文件读入
	public String[] FilePathRead() {
		String[] str = new String[20];
		try {
			FileReader fileReader1 = new FileReader("path.txt");
			BufferedReader buf1 = new BufferedReader(fileReader1);
			String readLine = "";
			int i = 0;
			while ((readLine = buf1.readLine()) != null) {
				str[i] = readLine;
				i++;
			}
			buf1.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "路径文件文件读入失败！\n" + e, "提示", JOptionPane.WARNING_MESSAGE);
		}
		return str;

	}
}
