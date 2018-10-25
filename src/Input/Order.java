package Input;

public class Order {
	public String PONumber; // ������,ֻ�����������ʹ��
	public String Item; // ����item,ֻ���ڽ������ʹ��
	public String UnitMLFB; // mlfb,ֻ���ڽ������ʹ��
	public int PlaQty; // ������������,��Ҫ����Ŀ�꺯������
	public double MinRV; // ��С��ѹֵ,��Ҫ����Լ����������
	public double FMinRV; // ��С��ѹֵ���ݣ����ڸ�����С��ѹֵ��ָ���
	public double MaxRV; // ����ѹֵ,����Լ����������
	public double P_MaxHeight; // ���߶�,����Լ����������
	public String MOVType; // mov�ͺű�,��Ӧ��inventory�����б���
	public String PlanID; // planId��¼ÿ����������Ҫ����,ÿ���������´�һ��excel��,������planid��ͬ
	public String fillngpart;// �������ݼ�¼txt�ļ���,��¼����߶���Ϣ
	public boolean WaveDisk;// �Ƿ�ʹ�ò��Ƶ�Ƭ
	public int WaveNum;// ���ò��Ƶ�Ƭ��
	public double WaveHeight;// ���Ƶ�Ƭ�߶�
	public double NewP_MaxHeight; // ���߶�,����Լ����������(���ڷŲ��²��Ƶ�Ƭʱ��)
	public String Description;// ժҪ
	public boolean MovRVError = false;// MOV��ѹ�쳣��־
	public Order(String data[]) {
		// TODO Auto-generated constructor stub
		PONumber = data[0];
		Item = data[1];
		UnitMLFB = data[2];
		PlaQty = Integer.parseInt(data[3]);
		MinRV = Double.parseDouble(data[4]);
		FMinRV = MinRV;// ��С��ѹֵ����
		MaxRV = Double.parseDouble(data[5]);
		if (MinRV > MaxRV) {
			MovRVError = true;
		} else {
			MovRVError = false;
			// ������С��ѹֵ
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
