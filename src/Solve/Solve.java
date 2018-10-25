package Solve;

import Input.Data;
import main.MOVMain;

public class Solve {
	private int m;// Լ�������ĸ���
	private int fm;// Լ�������ĸ���(����)
	private int n; // ��������
	private int m1; // <=��Լ����������
	private int fm1; // <=��Լ����������(����)
	private int m2;// =��Լ����������
	private int fm2;// =��Լ����������(����)
	private int m3;// >=��Լ����������
	private int fm3;// >=��Լ����������(����)
	private double a[][]; // Լ��������ϵ������
	private double fa[][]; // Լ��������ϵ������(����)
	private double minmax; // Ŀ�꺯�������ֵ����Сֵ
	private double result[][];// ���
	private double fresult[][];// ���(����)
	private double ffresult[][][] = new double[10][][];// �������(����)
	private int x[];
	public int Uz;// Ŀ�꺯��ֵ�Ͻ�
	public int Lz;// Ŀ�꺯��ֵ�½�
	
	public static int OBJ = 0;

	public Solve() {
		// TODO Auto-generated constructor stub
	}

	//
	public double[][] compute(int minmax, int m, int n, int m1, int m2, int m3, double a[][], int x[],
			boolean Used0_1) {
		this.m = m;
		this.n = n;
		this.m1 = m1;
		this.m2 = m2;
		this.m3 = m3;
		this.a = a;
		this.minmax = minmax;
		this.x = x;
		// ����Ŀ�꺯��ֵ�Ͻ�
		this.result = new LinearProgram().Run(-this.minmax, this.m, this.n, this.m1, this.m2, this.m3, this.a, this.x);
		this.Uz = (int) result[1][0];// Ŀ�꺯��ֵ�Ͻ�
		// ����Ŀ�꺯��ֵ�Ͻ�
		this.result = new LinearProgram().Run(this.minmax, this.m, this.n, this.m1, this.m2, this.m3, this.a, this.x);
		this.Lz = (int) result[1][0] + 1;// Ŀ�꺯��ֵ�½�
		fdatainit();// ��������
		while (this.Lz <= this.Uz) {
			// resultshow(this.result);//��ʾ���
			// System.out.println("Lz="+this.Lz+" "+"Uz="+this.Uz);//��ʾ���½�
			datareset();// �������ݻ�ԭ����
			boolean flag = true;// �жϱ�־��trueΪ�����жϣ�falseΪ�쳣�ж�
			int ErrorCount = 0;
			int k = 0;
			this.result = new LinearProgram().Run(this.minmax, this.m, this.n, this.m1, this.m2, this.m3, this.a, x);
			// resultshow(this.result);
			if (this.result[1][0] == 0)// �޽⣬�ж�ѭ��
				break;
			if (!IntCheck(this.result))// �н⣬���������⣬����Ŀ��ֵ����
			{
				this.a = resultlimitr(this.a, this.Lz);
				this.m = this.m + 1;
				this.m3 = this.m3 + 1;
				if (Used0_1) {
					this.a = resultlimitl(this.a);
					this.m = this.m + this.n;
					this.m1 = this.m1 + this.n;
				} else {
					this.a = Qtyresultlimitl(this.a);
					this.m = this.m + this.n;
					this.m1 = this.m1 + this.n;
				}

			}
			// show(this.a);
			this.result = new LinearProgram().Run(this.minmax, this.m, this.n, this.m1, this.m2, this.m3, this.a, x);
			while (!IntCheck(this.result))// ��������
			{
				// show(this.a);
				// resultshow(this.result);
				this.fresult = this.result;
				this.m = this.m + 1;
				int index = NoInt(this.result);
				double[][] l = la(this.a, index, (int) (this.result[0][index]));
				double[][] r = ra(this.a, index, (int) (this.result[0][index]) + 1);
				double result1[][] = new LinearProgram().Run(this.minmax, this.m, this.n, this.m1 + 1, this.m2, this.m3,
						l, this.x);
				double result2[][] = new LinearProgram().Run(this.minmax, this.m, this.n, this.m1, this.m2, this.m3 + 1,
						r, this.x);
				if ((result1[1][0] <= result2[1][0] && this.minmax == -1 && result1[1][0] > 0)
						|| (result1[1][0] > result2[1][0] && this.minmax == 1)
						|| (result1[1][0] > 0 && result2[1][0] <= 0)) {
					this.result = result1;
					this.a = l;
					this.m1 = this.m1 + 1;
				} else {
					this.result = result2;
					this.a = r;
					this.m3 = this.m3 + 1;
				}
				// �������ڽ�
				if (k == 10)
					k = 0;
				FresultInit(k, this.result);
				k++;
				// ȡĿ��ֵ�����Ƶ�Ƭ�Ų���ʱ��
				if (Data.OrderData.WaveDisk && result[1][0] != 0 && Object(result) > OBJ)
					OBJ = QuZheng(Object(result));
				// �����������ۼ�
				if (ResultError(this.result[1][0], this.Lz) || SameResultCheck(this.fresult, this.result)
						|| PeriodSameResultCheck(this.result))
					ErrorCount++;
				if (ErrorCount >= 100) {
					this.Lz++;
					flag = false;
					break;
				}
			}
			if (flag) {
				if (this.result[1][0] != 0 && QtyCheck(result) && WaveDiskCheck(result))
					break;
				else
					this.Lz++;
			}
		}
		// System.out.println();
		return this.result;
	}

	// С�ڵ� ��Լ���������
	public double[][] la(double a[][], int index, double num) {
		int m = a.length;
		int n = a[0].length;
		double la[][] = new double[m + 1][n];
		for (int i = 0; i < m + 1; i++) {
			for (int j = 0; j < n; j++) {
				if (i == 0) {
					if (j == index)
						la[i][j] = 1;
					else if (j == n - 1)
						la[i][j] = num;
					else
						la[i][j] = 0;
				} else
					la[i][j] = a[i - 1][j];
			}
		}
		return la;
	}

	// ���ڵ� ��Լ���������
	public double[][] ra(double a[][], int index, double num) {
		int m = a.length;
		int n = a[0].length;
		double la[][] = new double[m + 1][n];
		for (int i = 0; i < m + 1; i++) {
			for (int j = 0; j < n; j++) {
				if (i == m) {
					if (j == index)
						la[i][j] = 1;
					else if (j == n - 1)
						la[i][j] = num;
					else
						la[i][j] = 0;
				} else
					la[i][j] = a[i][j];
			}
		}
		return la;
	}

	// ȡ�ý��ֵ
	public double Object(double result[][]) {
		double sum = 0;
		for (int i = 0; i < result[0].length; i++)
			sum += result[0][i];
		return sum;
	}

	// �������±���ȡ
	public int NoInt(double[][] result) {
		int num = 0;
		double max = 0;
		double min = 0;
		for (int i = 0; i < result[0].length; i++)// �ҵ���һ�������
		{
			if (Math.abs(result[0][i] - QuZheng(result[0][i])) > 0.0001) {
				min = (result[0][i] - (int) result[0][i]) < ((int) result[0][i] + 1 - result[0][i])
						? (result[0][i] - (int) result[0][i]) : ((int) result[0][i] + 1 - result[0][i]);
				if (min > max) {
					num = i;
					max = min;
				}
			}
		}
		return num;
	}

	// ���ݱ���
	public void fdatainit() {
		this.fm = this.m;
		this.fm1 = this.m1;
		this.fm2 = this.m2;
		this.fm3 = this.m3;
		this.fa = this.a;
	}

	// ����쳣���
	public boolean ResultError(double x1, int x2) {
		boolean flag = true;
		if (Math.abs(x1 - x2) > 0.0001)
			flag = false;
		return flag;
	}

	// ������鱸��
	public void FresultInit(int k, double result[][]) {
		ffresult[k] = result;
	}

	// ������ͬ������
	public boolean PeriodSameResultCheck(double[][] result) {
		boolean flag = false;
		for (int i = 0; i < this.ffresult.length; i++) {
			if (SameResultCheck(result, this.ffresult[i])) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	// ��һ��ͬ������
	public boolean SameResultCheck(double result1[][], double result2[][]) {
		boolean flag = true;
		for (int i = 0; i < result1[0].length; i++) {
			if (result1[0][i] != result2[0][i]) {
				flag = false;
				break;
			}
		}
		return flag;
	}

	// �������ú���
	public void datareset() {
		this.m = this.fm;
		this.m1 = this.fm1;
		this.m2 = this.fm2;
		this.m3 = this.fm3;
		this.a = this.fa;
		// this.x=this.fx;
	}

	// ��СĿ��ֵ��������
	public double[][] resultlimitr(double a[][], double num) {
		int m = a.length;
		int n = a[0].length;
		num = num - 0.0000001;
		double ta[][] = new double[m + 1][n];
		for (int i = 0; i < m + 1; i++) {
			for (int j = 0; j < n; j++) {
				if (i == m) {
					if (j == n - 1)
						ta[i][j] = num;
					else
						ta[i][j] = 1;
				} else
					ta[i][j] = a[i][j];
			}
		}
		return ta;
	}

	// 0-1�滮��������
	public double[][] resultlimitl(double a[][]) {
		int m = a.length;
		int n = a[0].length;
		double ta[][] = new double[m + n - 1][n];
		for (int i = 0; i < m + n - 1; i++) {
			for (int j = 0; j < n; j++) {
				if (i < n - 1) {
					if (j == i)
						ta[i][j] = 1;
					else if (j == n - 1)
						ta[i][j] = 1 + 0.0000001;
					else
						ta[i][j] = 0;
				} else
					ta[i][j] = a[i - n + 1][j];
			}
		}
		return ta;
	}

	// �������������
	public double[][] Qtyresultlimitl(double a[][]) {
		int m = a.length;
		int n = a[0].length;
		double ta[][] = new double[m + n - 1][n];
		for (int i = 0; i < m + n - 1; i++) {
			for (int j = 0; j < n; j++) {
				if (i < n - 1) {
					if (j == i)
						ta[i][j] = 1;
					else if (j == n - 1)
						// ta[i][j]=(int)((this.StockQty[i]-this.LimitedNum)/PlaQty>0?(this.StockQty[i]-this.LimitedNum)/PlaQty:0)+0.0000001;
						ta[i][j] = MaxMov(i);
					else
						ta[i][j] = 0;
				} else
					ta[i][j] = a[i - n + 1][j];
			}
		}
		return ta;
	}

	// ����������
	public boolean IntCheck(double[][] result) {
		boolean flag = true;
		for (int i = 0; i < result[0].length; i++) {
			if (Math.abs(result[0][i] - QuZheng(result[0][i])) > 0.0001)
				flag = false;
		}
		return flag;
	}

	// ȡ��
	public int QuZheng(double d) {
		int t = (int) d;
		if (Math.abs(t + 1 - d) < 0.00001)
			return t + 1;
		else
			return t;
	}
	//��������С
	public int Min(int x, int y, int z) {
		int t = x < y ? x : y;
		t = t < z ? t : z;
		return t;
	}
	// MOV�����������
	public double MaxMov(int index) {
		double max = 0;
		int RvMax, HeightMax, QtyMax;

		RvMax = (int) (Data.OrderData.MaxRV / Double.parseDouble(MOVMain.data[MOVMain.Used[index]][4]));
		HeightMax = (int) (Data.OrderData.P_MaxHeight / Double.parseDouble(MOVMain.data[MOVMain.Used[index]][2]));
		QtyMax = (int) ((MOVMain.StockQty[index] - MOVMain.LimitedNum) / Data.OrderData.PlaQty > 0
				? (MOVMain.StockQty[index] - MOVMain.LimitedNum) / Data.OrderData.PlaQty : 0);
		max = Min(RvMax, HeightMax, QtyMax) + 0.0000001;
		return max;
	}

	// ���Ƶ�Ƭ���(����Ϊ�棬������Ϊ��)
	public boolean WaveDiskCheck(double[][] result) {
		boolean flag;
		if (!Data.OrderData.WaveDisk)
			flag = true;
		else {
			if ((QuZheng(result[1][0]) + Data.OrderData.WaveNum)
					* Data.OrderData.WaveHeight > ActualHeightDifference(result)) {
				flag = false;
			} else
				flag = true;
		}
		return flag;
	}

	// ʵ�ʸ߶Ȳ�
	public double ActualHeightDifference(double[][] result) {
		double height = 0;
		int row = result[0].length;
		// data[i][2] �߶ȣ� data[i][4] ��ѹ
		for (int i = 0; i < row; i++) {
			height = height + Double.parseDouble(MOVMain.data[MOVMain.Used[i]][2]) * result[0][i];
		}
		// ����תString��
		return Data.OrderData.P_MaxHeight - height;
	}

	// QtyCheck
	public boolean QtyCheck(double[][] result) {
		boolean flag = true;
		for (int i = 0; i < result[0].length; i++) {
			if (Data.OrderData.PlaQty * result[0][i] > MOVMain.StockQty[i] - MOVMain.LimitedNum) {
				flag = false;
				break;
			}
		}
		return flag;
	}

}
