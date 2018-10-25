package Input;

public class Order {
	public String PONumber; // 订单号,只需在最后结表中使用
	public String Item; // 订单item,只需在结果表中使用
	public String UnitMLFB; // mlfb,只需在结果表中使用
	public int PlaQty; // 订单生产数量,需要加入目标函数计算
	public double MinRV; // 最小残压值,需要加入约束条件计算
	public double FMinRV; // 最小残压值备份，用于更改最小残压值后恢复用
	public double MaxRV; // 最大残压值,加入约束条件考虑
	public double P_MaxHeight; // 最大高度,加入约束条件考虑
	public String MOVType; // mov型号表,对应于inventory库存表中表名
	public String PlanID; // planId记录每次运算结果主要内容,每次运算结果新存一个excel表,表名与planid相同
	public String fillngpart;// 填充块数据记录txt文件名,记录填充块高度信息
	public boolean WaveDisk;// 是否使用波纹垫片
	public int WaveNum;// 多用波纹垫片数
	public double WaveHeight;// 波纹垫片高度
	public double NewP_MaxHeight; // 最大高度,加入约束条件考虑(用于放不下波纹垫片时用)
	public String Description;// 摘要
	public boolean MovRVError = false;// MOV残压异常标志
	public Order(String data[]) {
		// TODO Auto-generated constructor stub
		PONumber = data[0];
		Item = data[1];
		UnitMLFB = data[2];
		PlaQty = Integer.parseInt(data[3]);
		MinRV = Double.parseDouble(data[4]);
		FMinRV = MinRV;// 最小残压值备份
		MaxRV = Double.parseDouble(data[5]);
		if (MinRV > MaxRV) {
			MovRVError = true;
		} else {
			MovRVError = false;
			// 调整最小残压值
			if (MinRV <= 50)
				MinRV = MinRV + 0.3;
			else
				MinRV = MinRV + 0.5;
			if (MinRV > MaxRV)
				MinRV = MaxRV - 0.000001;
		}
		P_MaxHeight = Double.parseDouble(data[6]);
		NewP_MaxHeight = P_MaxHeight;
		MOVType = data[7];
		PlanID = data[8];
		fillngpart = data[9];
		if (data[10].equals("0")) {
			WaveDisk = false;
			WaveHeight = 0;
			WaveNum = 0;
		} else {
			WaveDisk = true;
			WaveHeight = Double.parseDouble(data[11]);
			WaveNum = Integer.parseInt(data[10]);
		}
		Description = data[12];
	}

}
