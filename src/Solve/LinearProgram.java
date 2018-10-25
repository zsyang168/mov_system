package Solve;
public class LinearProgram {

	private int m; // Լ�������ĸ���
	private int n; // ��������
	private int m1; // <=��Լ����������
	private int m2; // =��Լ����������
	private int m3; // >=��Լ����������
	private int error; // �ж��Ƿ��Ǵ����
	private int basic[];// ������
	private int nonbasic[];// �ǻ�����
	private double a[][]; // Լ��������ϵ������
	private double minmax; // Ŀ�꺯�������ֵ����Сֵ
	private double result[][];// ���

	public double[][] Run(double minmax, int m, int n, int m1, int m2, int m3, double a[][], int x[]) {
		double value;
		this.error = 0;
		this.minmax = minmax;
		this.m = m;
		this.n = n;
		this.m1 = m1;
		this.m2 = m2;
		this.m3 = m3;
		result = new double[2][];
		result[0] = new double[n];
		result[1] = new double[1];
		if (m != m1 + m2 + m3) {
			this.error = 1;
		}
		this.a = new double[m + 2][];
		for (int i = 0; i < m + 2; i++) {
			this.a[i] = new double[n + m + m3 + 1];
		}
		this.basic = new int[m + 2];
		this.nonbasic = new int[n + m3 + 1];
		for (int i = 0; i <= m + 1; i++) {
			for (int j = 0; j <= n + m + m3; j++) {
				this.a[i][j] = 0.0;
			}
		}
		for (int j = 0; j <= n + m3; j++) {
			nonbasic[j] = j;
		}
		for (int i = 1, j = n + m3 + 1; i <= m; i++, j++) {
			basic[i] = j;
		}
		for (int i = m - m3 + 1, j = n + 1; i <= m; i++, j++) {
			this.a[i][j] = -1;
			this.a[m + 1][j] = -1;
		}
		for (int i = 1; i <= m; i++) {
			for (int j = 1; j <= n; j++) {
				value = a[i - 1][j - 1];
				this.a[i][j] = value;
			}
			value = a[i - 1][n];
			if (value < 0) {
				error = 1;
			}
			this.a[i][0] = value;
		}

		for (int j = 1; j <= n; j++) // ��ֵϵ��
		{
			value = x[j - 1];
			this.a[0][j] = value * minmax;
		}
		for (int j = 1; j <= n; j++) {
			value = 0;
			for (int i = m1 + 1; i <= m; i++)
				value += this.a[i][j];
			this.a[m + 1][j] = value;
		}
		solve();
		return result;
	}

	public void show() {
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < result[i].length; j++) {
				if (i == 0)
					System.out.println("x" + (j + 1) + "=" + result[i][j]);
				else
					System.out.println("obj=" + result[i][j]);
			}
		}
	}

	public int enter(int objrow) {
		int col = 0;
		for (int j = 1; j <= this.n + this.m3; j++) {
			if (this.nonbasic[j] <= this.n + this.m1 + this.m3 && this.a[objrow][j] > 10e-8) {
				col = j;
				break;
			}
		}
		return col;
	}

	public int leave(int col) {
		double temp = -1;
		int row = 0;
		for (int i = 1; i <= this.m; i++) {
			double val = this.a[i][col];
			if (val > 10e-8) {
				val = this.a[i][0] / val;
				if (val < temp || temp == -1) {
					row = i;
					temp = val;
				}
			}
		}
		return row;
	}

	public void swapbasic(int row, int col) {
		int temp = this.basic[row];
		this.basic[row] = this.nonbasic[col];
		this.nonbasic[col] = temp;
	}

	public void pivot(int row, int col) {
		for (int j = 0; j <= this.n + this.m3; j++) {
			if (j != col) {
				this.a[row][j] = this.a[row][j] / this.a[row][col];
			}
		}
		this.a[row][col] = 1.0 / this.a[row][col];
		for (int i = 0; i <= this.m + 1; i++) {
			if (i != row) {
				for (int j = 0; j <= this.n + this.m3; j++) {
					if (j != col) {
						this.a[i][j] = this.a[i][j] - this.a[i][col] * this.a[row][j];
						if (Math.abs(this.a[i][j]) < 10e-8)
							this.a[i][j] = 0;
					}
				}
				this.a[i][col] = -this.a[i][col] * this.a[row][col];
			}
		}
		swapbasic(row, col);
	}

	public int simplex(int objrow) {
		int row = 0;
		while (true) {
			int col = enter(objrow);
			if (col > 0) {
				row = leave(col);
			} else {
				return 0;
			}
			if (row > 0) {
				pivot(row, col);
			} else {
				return 2;
			}
		}
	}

	public int phase1() {
		this.error = simplex(this.m + 1);
		if (this.error > 0) {
			return this.error;
		}
		for (int i = 1; i <= this.m; i++) {
			if (this.basic[i] > this.n + this.m1 + this.m3) {
				if (this.a[i][0] > 10e-8) {
					return 3;
				}
				for (int j = 1; j <= this.n; j++) {
					if (Math.abs(this.a[i][j]) >= 10e-8) {
						pivot(i, j);
						break;
					}
				}
			}
		}
		return 0;
	}

	public int phase2() {
		return simplex(0);
	}

	public int compute() {
		if (this.error > 0)
			return this.error;
		if (this.m != this.m1) {
			this.error = phase1();
			if (this.error > 0)
				return this.error;
		}
		return phase2();
	}

	public void solve() {
		error = compute();
		switch (error) {
		case 0:
			Result();
			break;
		case 1:
			System.out.println("�������ݴ���!");
			// JOptionPane.showMessageDialog(null, "�������ݴ���!",
			// "��ʾ",JOptionPane.WARNING_MESSAGE);
			break;
		case 2:
			// System.out.println("�޽��!");
			// JOptionPane.showMessageDialog(null, "�޽��!",
			// "��ʾ",JOptionPane.WARNING_MESSAGE);
			break;
		case 3:
			// System.out.println("�޿��н�!");
			// JOptionPane.showMessageDialog(null, "�޿��н�!",
			// "��ʾ",JOptionPane.WARNING_MESSAGE);
			break;
		default:
			break;
		}
	}

	public void Result() {
		int basicp[] = new int[n + 1];
		// double[][] result=new double[2][];
		for (int i = 0; i <= n; i++) {
			basicp[i] = 0;
		}
		for (int i = 1; i <= m; i++) {
			if (basic[i] >= 1 && basic[i] <= n) {
				basicp[basic[i]] = i;
			}
		}
		for (int i = 1; i <= n; i++) {
			if (basicp[i] != 0) {
				result[0][i - 1] = a[basicp[i]][0];
			} else {
				result[0][i - 1] = 0;
			}
		}
		result[1][0] = -minmax * a[0][0];
		// return result;
	}
}
