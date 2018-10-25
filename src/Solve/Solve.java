package Solve;

import Input.Data;
import main.MOVMain;

public class Solve {
	private int m;// 约束条件的个数
	private int fm;// 约束条件的个数(备份)
	private int n; // 变量个数
	private int m1; // <=的约束条件个数
	private int fm1; // <=的约束条件个数(备份)
	private int m2;// =的约束条件个数
	private int fm2;// =的约束条件个数(备份)
	private int m3;// >=的约束条件个数
	private int fm3;// >=的约束条件个数(备份)
	private double a[][]; // 约束条件的系数矩阵
	private double fa[][]; // 约束条件的系数矩阵(备份)
	private double minmax; // 目标函数的最大值或最小值
	private double result[][];// 结果
	private double fresult[][];// 结果(备份)
	private double ffresult[][][] = new double[10][][];// 结果数组(备份)
	private int x[];
	public int Uz;// 目标函数值上界
	public int Lz;// 目标函数值下界
	
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
		// 计算目标函数值上界
		this.result = new LinearProgram().Run(-this.minmax, this.m, this.n, this.m1, this.m2, this.m3, this.a, this.x);
		this.Uz = (int) result[1][0];// 目标函数值上界
		// 计算目标函数值上界
		this.result = new LinearProgram().Run(this.minmax, this.m, this.n, this.m1, this.m2, this.m3, this.a, this.x);
		this.Lz = (int) result[1][0] + 1;// 目标函数值下界
		fdatainit();// 备份数据
		while (this.Lz <= this.Uz) {
			// resultshow(this.result);//显示结果
			// System.out.println("Lz="+this.Lz+" "+"Uz="+this.Uz);//显示上下界
			datareset();// 备份数据还原数据
			boolean flag = true;// 中断标志，true为正常中断，false为异常中断
			int ErrorCount = 0;
			int k = 0;
			this.result = new LinearProgram().Run(this.minmax, this.m, this.n, this.m1, this.m2, this.m3, this.a, x);
			// resultshow(this.result);
			if (this.result[1][0] == 0)// 无解，中断循环
				break;
			if (!IntCheck(this.result))// 有解，但非整数解，加入目标值界限
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
			while (!IntCheck(this.result))// 非整数解
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
				// 备份周期解
				if (k == 10)
					k = 0;
				FresultInit(k, this.result);
				k++;
				// 取目标值，波纹垫片放不下时用
				if (Data.OrderData.WaveDisk && result[1][0] != 0 && Object(result) > OBJ)
					OBJ = QuZheng(Object(result));
				// 计算错误次数累计
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

	// 小于等 于约束条件添加
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

	// 大于等 于约束条件添加
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

	// 取得结果值
	public double Object(double result[][]) {
		double sum = 0;
		for (int i = 0; i < result[0].length; i++)
			sum += result[0][i];
		return sum;
	}

	// 非整数下标提取
	public int NoInt(double[][] result) {
		int num = 0;
		double max = 0;
		double min = 0;
		for (int i = 0; i < result[0].length; i++)// 找到第一个非零解
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

	// 数据备份
	public void fdatainit() {
		this.fm = this.m;
		this.fm1 = this.m1;
		this.fm2 = this.m2;
		this.fm3 = this.m3;
		this.fa = this.a;
	}

	// 结果异常检测
	public boolean ResultError(double x1, int x2) {
		boolean flag = true;
		if (Math.abs(x1 - x2) > 0.0001)
			flag = false;
		return flag;
	}

	// 结果数组备份
	public void FresultInit(int k, double result[][]) {
		ffresult[k] = result;
	}

	// 周期相同结果检测
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

	// 单一相同结果检测
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

	// 数据重置函数
	public void datareset() {
		this.m = this.fm;
		this.m1 = this.fm1;
		this.m2 = this.fm2;
		this.m3 = this.fm3;
		this.a = this.fa;
		// this.x=this.fx;
	}

	// 最小目标值矩阵限制
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

	// 0-1规划矩阵限制
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

	// 库存量矩阵限制
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

	// 非整数解检测
	public boolean IntCheck(double[][] result) {
		boolean flag = true;
		for (int i = 0; i < result[0].length; i++) {
			if (Math.abs(result[0][i] - QuZheng(result[0][i])) > 0.0001)
				flag = false;
		}
		return flag;
	}

	// 取整
	public int QuZheng(double d) {
		int t = (int) d;
		if (Math.abs(t + 1 - d) < 0.00001)
			return t + 1;
		else
			return t;
	}
	//求三者最小
	public int Min(int x, int y, int z) {
		int t = x < y ? x : y;
		t = t < z ? t : z;
		return t;
	}
	// MOV最大用量限制
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

	// 波纹垫片检测(满足为真，不满足为假)
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

	// 实际高度差
	public double ActualHeightDifference(double[][] result) {
		double height = 0;
		int row = result[0].length;
		// data[i][2] 高度， data[i][4] 残压
		for (int i = 0; i < row; i++) {
			height = height + Double.parseDouble(MOVMain.data[MOVMain.Used[i]][2]) * result[0][i];
		}
		// 数据转String型
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
