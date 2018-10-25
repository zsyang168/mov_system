package Input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import main.MOVGUI;

public class Data {
	// �ļ�·��
	public static String OrderPath;
	public static String InventoryPath;
	public static String MovResultPath;
	public static String PlanIdPath;
	public static String FillinpartsPath;
	public static String OrderOutputPath;
	// ������
	public static Order OrderData;// order������
	public static String[] LOrderData;// order������
	public static String[][] Tabledata;// ��������

	public Data() {
		// TODO Auto-generated constructor stub
		//·����ʼ��
		FilePathInit();
		//���ݳ�ʼ��
		Init();
	}

	//���ݳ�ʼ��
	public void Init() {
		Tabledata=exceldataRead();
		String PL[] = PLInit(Tabledata);
		String message[]=OrderInformationInit();
		MOVGUI.setOptions(PL);
		MOVGUI.setOrderMessage(message);
		MOVGUI.setProgressString(100, "���ݶ�����ϣ�"+100+"%");
	}

	// excel������ݶ���
	public String[][] exceldataRead() {

		File file = new File(Data.OrderPath + "order.xls");// ����order����ȡ��Ӧ����
		MOVGUI.setProgressString(10, "���ڶ���order������" + 10 + "%");
		String[] OrderData = new Read().XlsRead(file, 1);
		LOrderData=OrderData;
		NullValueCheck(OrderData);
		Data.OrderData = new Order(OrderData);
		File fr = new File(Data.InventoryPath + Data.OrderData.MOVType + ".xls");// ��ȡ��Ӧexcel��
		int row = new Read().Row(fr);// ȡ�ñ������
		MOVGUI.setProgressString(20, "���ڶ����������" + 20 + "%");
		String[][] data2 = new String[row][];
		// ���ж�ȡ���
		for (int i = 0; i < row; i++) {
			data2[i] = new Read().XlsRead(fr, i);
			MOVGUI.setProgressString(20 + 70 * i / row, "���ڶ����������" + (20 + 70 * i / row) + "%");
		}
		return data2;

	}

	// ������Ϣ�б��ʼ��
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

	// ���ȼ��б��ʼ��
	public String[] PLInit(String[][] data) {
		int l = data.length;
		LinkedList<String> PL = new LinkedList<String>();
		PL.add(data[1][1]);
		// ���㲻ͬMOV������
		for (int i = 1; i < l; i++) {
			if (!PL.contains(data[i][1])) {
				PL.add(data[i][1]);
			}
		}
		String[] result = {};
		return PL.toArray(result);
	}

	// order���ֵ���
	public void NullValueCheck(String data[]) {
		String Header[] = { "PONumber", "Item", "UnitMLFB", "PlaQty", "MinRV", "MaxRV", "P_MaxHeight", "MOVType",
				"PlanID", "Fillngpart", "Wavedisk", "Waveheight", "Description" };
		for (int i = 0; i < 13; i++) {
			if (data[i].isEmpty()) {
				JOptionPane.showMessageDialog(null, "Order�� " + Header[i] + "����Ϊ�գ������Ϻ����ԣ�\n", "��ʾ",
						JOptionPane.WARNING_MESSAGE);
				System.exit(0);
			}
		}
	}

	// �ļ�·����ʼ��
	public void FilePathInit() {
		String FilePath[] = FilePathRead();
		OrderPath = FilePath[1];
		InventoryPath = FilePath[3];
		MovResultPath = FilePath[5];
		PlanIdPath = FilePath[7];
		FillinpartsPath = FilePath[9];
		OrderOutputPath = FilePath[11];
	}

	// ·���ļ�����
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
			JOptionPane.showMessageDialog(null, "·���ļ��ļ�����ʧ�ܣ�\n" + e, "��ʾ", JOptionPane.WARNING_MESSAGE);
		}
		return str;

	}
}
