package main;

import javax.swing.JOptionPane;

import Input.Data;
import Output.Output;
import Solve.Solve;

public class MOVMain {

	public static String[][] data;
	public static double ClassRv[];
	public static double MOVHeight[];
	public static double[] StockQty;
	public static String[] FStockQty;// ��汸��
	public static int[] SortIndex;// ��������к��±�
	public static int[] Used;// ʹ������±�
	private static double[] SortStockQty;// ���Ӵ�С����
	// ����
	private int rate = 0;
	//
	public static boolean SolveFaile = false;// �޽��־����������MinRV���ӵ��µ�
	public static boolean LimitedError = false;// ����ֵ�쳣��־
	public static int LimitedNum = 0;
	// ���ظ��ύ������־
	public static boolean RebSumit = true;
	public static boolean ReSubmitConfirm = false;
	public static boolean RestoreFlag = false;
	//
	public static double MaxQty;
	public static double MeanQty;
	public double NewP_MaxHeight;
	public double P_MaxHeight;
	public double MinRV;
	public double FMinRV;
	public double MaxRV;
	//
	private Solve solve;

	public MOVMain() {
		// TODO Auto-generated constructor stub
		Init();
		solve=new Solve();
	}
	//���ݳ�ʼ��
	public void Init() {
		data = Data.Tabledata;
		P_MaxHeight = Data.OrderData.P_MaxHeight;
		MinRV = Data.OrderData.MinRV;
		MaxRV = Data.OrderData.MaxRV;
		FMinRV = Data.OrderData.FMinRV;
		QtySort();
		MOVMain.RebSumit = true;
	}

	// ���Ӵ�С����
	public static void QtySort() {
		int l = MOVMain.data.length;
		double temp;
		// double DataTurnCheck;
		double sum = 0.0;
		double Sto[] = new double[l - 1];
		for (int i = 1; i < l; i++) {
			Sto[i - 1] = Double.parseDouble(MOVMain.data[i][5]);
			sum += Sto[i - 1];
			// ����Ƿ�����ȷת����ʽ������ʽ�Ƿ���ȷ��
			Double.parseDouble(MOVMain.data[i][2]);
			Double.parseDouble(MOVMain.data[i][4]);
		}
		for (int j = 0; j < l - 2; j++)
			for (int k = j + 1; k < l - 1; k++)
				if (Sto[k] > Sto[j]) {
					temp = Sto[j];
					Sto[j] = Sto[k];
					Sto[k] = temp;
				}

		MOVMain.SortStockQty = Sto;
		MaxQty = SortStockQty[0];
		MOVMain.MeanQty = sum / (l - 1);
	}

	// ��⺯��
	public void Solve() {
		if (!LimitedError){
			if (MOVGUI.def.isSelected()) {
				rate = 5;
				MOVGUI.setProgressString(rate, "�������..." + rate + "%");
				DefSolve();
			} else if (MOVGUI.opdef.isSelected()) {
				rate = 5;
				MOVGUI.setProgressString(rate, "�������..." + rate + "%");
				DesDefSolve();
			} else if (PriIsSelectCheck()) {
				rate = 5;
				MOVGUI.setProgressString(rate, "�������..." + rate + "%");
				PriSolve();
			} else {
				JOptionPane.showMessageDialog(null, "��ѡ��һ��ѡ�\n", "��ʾ", JOptionPane.WARNING_MESSAGE);
			}
		}
		else
			MOVGUI.setProgressError("ȱʡֵ�����޿��ÿ��!");
		
	}

	// AMatrix����A��ʼ��
	public double[][] AMatrixInit(int r) {
		// int r=this.Used.length;
		double a[][] = new double[3][r + 1];
		for (int i = 0; i < r; i++) {
			a[0][i] = Double.parseDouble(MOVMain.data[MOVMain.Used[i]][4]);// <=MaxRV
			a[1][i] = Double.parseDouble(MOVMain.data[MOVMain.Used[i]][2]);// <=P_MaxHeight
			a[2][i] = Double.parseDouble(MOVMain.data[MOVMain.Used[i]][4]);// >=MinRV
		}
		if (MaxRV == MinRV)
			MinRV = MinRV - 0.000001;
		a[0][r] = MaxRV;
		a[1][r] = NewP_MaxHeight;
		a[2][r] = MinRV;
		return a;
	}

	// QtyXMAtrixInit
	public int[] QtyXMAtrixInit(int r) {
		int x[] = new int[r];
		for (int i = 0; i < r; i++) {
			if (MOVMain.SortStockQty[i] > 10 * MOVMain.MeanQty)
				x[i] = 2;
			else
				x[i] = 1;
		}
		return x;
	}

	// XMatrix����X��ʼ��
	public int[] XMatrixInit(int r) {
		int x[] = new int[r];
		for (int i = 0; i < r; i++) {
			x[i] = 1;
		}
		return x;
	}

	// PriXMatrix����X��ʼ��
	public int[] PriXMatrixInit(int r, int pri) {
		int x[] = new int[r];
		for (int i = 0; i < r; i++) {
			if (i < pri)
				x[i] = 1;
			else
				x[i] = 50;
		}
		return x;
	}

	//
	// PriDefXMatrix����X��ʼ��
	public int[] PriDefXMatrixInit(int r) {
		int x[] = new int[r];
		for (int i = 0; i < r; i++) {
			x[i] = 1 + i;
		}
		return x;
	}

	// QtyDefSolve
	public void QtyDefSolve() {
		Sort();
		NewP_MaxHeight = P_MaxHeight;
		int l = MOVMain.data.length - 1;
		int i = 3;
		int d = 1;
		double a[][] = AMatrixInit(i);
		int x[] = QtyXMAtrixInit(i);
		int n = i;
		double result[][] = solve.compute(-1, 3, n, 2, 0, 1, a, x, false);
		while (!IntCheck(result) || result[1][0] == 0 || !QtyCheck(result) || !WaveDiskCheck(result)) {
			i = i + d;
			if (i > l)
				break;
			a = AMatrixInit(i);
			x = QtyXMAtrixInit(i);
			n = i;
			result = solve.compute(-1, 3, n, 2, 0, 1, a, x, false);
			// ���ȸ���
			RateChange(i, l, "QtyDef");
		}
		for (int j = 0; j < 2; j++) {
			if (!WaveDiskCheck(result)) {
				NewP_MaxHeight = NewP_MaxHeight - solve.OBJ * Data.OrderData.WaveHeight;
				i = 3;
				a = AMatrixInit(i);
				x = QtyXMAtrixInit(i);
				n = i;
				d = 1;
				result = solve.compute(-1, 3, n, 2, 0, 1, a, x, false);
				while (!IntCheck(result) || result[1][0] == 0 || !QtyCheck(result) || !WaveDiskCheck(result)) {
					i = i + d;
					if (i > l)
						break;
					a = AMatrixInit(i);
					x = QtyXMAtrixInit(i);
					n = i;
					result = solve.compute(-1, 3, n, 2, 0, 1, a, x, false);
					// ���ȸ���
					RateChange(i, l, "QtyDef");
				}

			}
		}
		if (IntCheck(result) && result[1][0] != 0 && WaveDiskCheck(result) && QtyCheck(result)) {
			new Output().Write(result);
		} // if(IntCheck(result)&&result[1][0]!=0&&WaveDiskCheck(result)&&QtyCheck(result))
		else {
			NormalDefSolve();
		}
	}

	// QtyDefSolve
	public void DesDefSolve() {
		int start = DesSort();
		NewP_MaxHeight = P_MaxHeight;
		int l = Used.length;
		int i = start > 3 ? start : 3;
		int d = 1;
		double a[][] = AMatrixInit(i);
		int x[] = PriXMatrixInit(i, start);
		int n = i;
		double result[][] = solve.compute(-1, 3, n, 2, 0, 1, a, x, false);
		while (!IntCheck(result) || result[1][0] == 0 || !QtyCheck(result) || !WaveDiskCheck(result)) {
			i = i + d;
			if (i > l)
				break;
			a = AMatrixInit(i);
			x = PriXMatrixInit(i, start);
			n = i;
			result = solve.compute(-1, 3, n, 2, 0, 1, a, x, false);
			// ���ȸ���
			RateChange(i, l, "DesDef");
		}
		for (int j = 0; j < 2; j++) {
			if (!WaveDiskCheck(result)) {
				NewP_MaxHeight = NewP_MaxHeight - solve.OBJ * Data.OrderData.WaveHeight;
				i = start > 3 ? start : 3;
				a = AMatrixInit(i);
				x = PriXMatrixInit(i, start);
				n = i;
				d = 1;
				result = solve.compute(-1, 3, n, 2, 0, 1, a, x, false);
				while (!IntCheck(result) || result[1][0] == 0 || !QtyCheck(result) || !WaveDiskCheck(result)) {
					i = i + d;
					if (i > l)
						break;
					a = AMatrixInit(i);
					x = PriXMatrixInit(i, start);
					n = i;
					result = solve.compute(-1, 3, n, 2, 0, 1, a, x, false);
					// ���ȸ���
					RateChange(i, l, "DesDef");
				}

			}
		}
		if (IntCheck(result) && result[1][0] != 0 && WaveDiskCheck(result) && QtyCheck(result)) {
			new Output().Write(result);
		} // if(IntCheck(result)&&result[1][0]!=0&&WaveDiskCheck(result)&&QtyCheck(result))
		else {
			NormalDefSolve();
		}
	}

	// NormalDefSolve
	public void NormalDefSolve() {
		Sort();
		NewP_MaxHeight = P_MaxHeight;
		int l = MOVMain.data.length - 1;
		int i = GetInitValue();
		int d = 1;
		double a[][] = AMatrixInit(i);
		int x[] = XMatrixInit(i);
		int n = i;
		double result[][] = solve.compute(-1, 3, n, 2, 0, 1, a, x, false);
		while (!IntCheck(result) || result[1][0] == 0 || !QtyCheck(result) || !WaveDiskCheck(result)) {
			i = i + d;
			if (i > l)
				break;
			a = AMatrixInit(i);
			x = XMatrixInit(i);
			n = i;
			result = solve.compute(-1, 3, n, 2, 0, 1, a, x, false);
			// ���ȸ���
			RateChange(i, l, "NormalDef");
		}
		for (int j = 0; j < 2; j++) {
			if (!WaveDiskCheck(result)) {
				NewP_MaxHeight = NewP_MaxHeight - solve.OBJ * Data.OrderData.WaveHeight;
				i = GetInitValue();
				a = AMatrixInit(i);
				x = XMatrixInit(i);
				n = i;
				d = 1;
				result = solve.compute(-1, 3, n, 2, 0, 1, a, x, false);
				while (!IntCheck(result) || result[1][0] == 0 || !QtyCheck(result) || !WaveDiskCheck(result)) {
					i = i + d;
					if (i > l)
						break;
					a = AMatrixInit(i);
					x = XMatrixInit(i);
					n = i;
					result = solve.compute(-1, 3, n, 2, 0, 1, a, x, false);
					// ���ȸ���
					RateChange(i, l, "NormalDef");
				}

			}
		}
		if (IntCheck(result) && result[1][0] != 0 && WaveDiskCheck(result) && QtyCheck(result)) {
			new Output().Write(result);
		} // if(IntCheck(result)&&result[1][0]!=0&&WaveDiskCheck(result)&&QtyCheck(result))
		else {
			Used0_1DefSolve();
		}
	}

	// 0_1DefSolve
	public void Used0_1DefSolve() {
		Sort();
		NewP_MaxHeight = P_MaxHeight;
		int l = data.length - 1;
		int i = 3;
		int d = 1;
		double a[][] = AMatrixInit(i);
		int x[] = XMatrixInit(i);
		int n = i;
		double result[][] = solve.compute(-1, 3, n, 2, 0, 1, a, x, false);
		while (!IntCheck(result) || result[1][0] == 0 || !QtyCheck(result) || !WaveDiskCheck(result)) {
			i = i + d;
			if (i > l)
				break;
			a = AMatrixInit(i);
			x = XMatrixInit(i);
			n = i;
			result = solve.compute(-1, 3, n, 2, 0, 1, a, x, true);
			// ���ȸ���
			RateChange(i, l, "0-1");
		}
		for (int j = 0; j < 2; j++) {
			if (!WaveDiskCheck(result)) {
				NewP_MaxHeight = NewP_MaxHeight - solve.OBJ * Data.OrderData.WaveHeight;
				i = 3;
				a = AMatrixInit(i);
				x = XMatrixInit(i);
				n = i;
				d = 1;
				result = solve.compute(-1, 3, n, 2, 0, 1, a, x, true);
				while (!IntCheck(result) || result[1][0] == 0 || !QtyCheck(result) || !WaveDiskCheck(result)) {
					i = i + d;
					if (i > l)
						break;
					a = AMatrixInit(i);
					x = XMatrixInit(i);
					n = i;
					result = solve.compute(-1, 3, n, 2, 0, 1, a, x, true);
					// ���ȸ���
					RateChange(i, l, "0-1");
				}

			}
		} // for(int j=0;j<2;j++)
		if (IntCheck(result) && result[1][0] != 0 && WaveDiskCheck(result)) {
			if (!QtyCheck(result)) {
				PriDefSolve();
			} else {
				new Output().Write(result);
			}
		} // if(IntCheck(result))
		else {
			PriDefSolve();
		}
	}

	//// DefSolve Def��⺯��
	public void DefSolve() {
		if (UnusualQtyCheck())
			QtyDefSolve();
		else
			NormalDefSolve();
	}

	// ����쳣���
	public boolean UnusualQtyCheck() {
		boolean flag = false;
		for (int i = 0; i < SortStockQty.length; i++) {
			if (SortStockQty[i] > 10 * MeanQty) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	/// PriSolve ���ȼ���⺯��
	public void PriSolve() {
		if (!LimitedError) {
			int pri = PriSort();
			NewP_MaxHeight = P_MaxHeight;
			int l = data.length - 1;
			int i = 3;
			double a[][] = AMatrixInit(i);
			int x[] = PriXMatrixInit(i, pri);
			int n = i;
			int d = 1;
			double result[][] = solve.compute(-1, 3, n, 2, 0, 1, a, x, false);
			while (!IntCheck(result) || result[1][0] == 0 || !QtyCheck(result) || !WaveDiskCheck(result)) {
				i = i + d;
				if (i > l)
					break;
				a = AMatrixInit(i);
				x = PriXMatrixInit(i, pri);
				// show(x);
				n = i;
				result = solve.compute(-1, 3, n, 2, 0, 1, a, x, false);
				// ���ȸ���
				RateChange(i, l, "PRI");
			}
			for (int j = 0; j < 2; j++) {
				if (!WaveDiskCheck(result)) {
					NewP_MaxHeight = NewP_MaxHeight - solve.OBJ * Data.OrderData.WaveHeight;
					i = 3;
					a = AMatrixInit(i);
					x = PriXMatrixInit(i, pri);
					n = i;
					d = 1;
					result = solve.compute(-1, 3, n, 2, 0, 1, a, x, false);
					while (!IntCheck(result) || result[1][0] == 0 || !QtyCheck(result) || !WaveDiskCheck(result)) {
						i = i + d;
						if (i > l)
							break;
						a = AMatrixInit(i);
						x = PriXMatrixInit(i, pri);
						n = i;
						result = solve.compute(-1, 3, n, 2, 0, 1, a, x, false);
						// ���ȸ���
						RateChange(i, l, "PRI");
					}

				}
			}
			if (IntCheck(result) && result[1][0] != 0 && WaveDiskCheck(result)) {
				if (!QtyCheck(result)) {
					DefSolve();
				} else {
					new Output().Write(result);
				}
			} // if(IntCheck(result))
			else {
				DefSolve();
			}
		} // if(!LimitedError)
	}

	// ��ȡ��ʼ����MOV��
	public int GetInitValue() {
		int initvalue = 0;
		int length = SortStockQty.length;
		if (length < 4 && length > 0)
			initvalue = length;
		else if (length / 6 < 4)
			initvalue = 4;
		else
			initvalue = length / 6;
		return initvalue;
	}

	///
	public void PriDefSolve() {

		if (!LimitedError) {
			Sort();
			NewP_MaxHeight = P_MaxHeight;
			int l = MOVMain.data.length - 1;
			int n = l;
			double a[][] = AMatrixInit(l);
			int x[] = PriDefXMatrixInit(l);
			double result[][] = solve.compute(-1, 3, n, 2, 0, 1, a, x, false);
			for (int j = 0; j < 2; j++) {
				if (!WaveDiskCheck(result)) {
					NewP_MaxHeight = NewP_MaxHeight - solve.OBJ * Data.OrderData.WaveHeight;
					a = AMatrixInit(l);
					x = PriDefXMatrixInit(l);
					result = solve.compute(-1, 3, n, 2, 0, 1, a, x, false);
					// ���ȸ���
					RateChange(l, l, "PriDef");
				}
			}
			if (IntCheck(result) && result[1][0] != 0 && WaveDiskCheck(result)) {
				if (!QtyCheck(result)) {
					MOVGUI.setProgressError("��治�㣡");
				} else {
					new Output().Write(result);
				}
			} // if(IntCheck(result))
			else {
				if (SolveFaile)
					MOVGUI.setProgressError("�޽⣡");
				else if (MinRV - 0.1 > FMinRV) {
					// �޽⣬������С��ѹֵ
					MinRV = MinRV - 0.1;
					// �������һ��
					Solve();
				} else {
					// ���ʧ�ܱ�־Ϊ�棬��ʾ���ֹ�һ�����ʧ���ˣ���ֹ���������⣬��ѭ��
					SolveFaile = true;
					// �޽⣬����С��ѹֵ��ԭ
					MinRV = FMinRV;
					// �������һ��
					Solve();
				}
			}

		}
	}

	// Sort Def������򣬰�����С������
	public int DesSort() {
		int l = MOVMain.data.length;
		double temp;
		int t;
		double Sto[] = new double[l - 1];
		int Sort[] = new int[l - 1];
		for (int i = 1; i < l; i++) {
			Sto[i - 1] = Double.parseDouble(MOVMain.data[i][5]);
			Sort[i - 1] = i;
		}
		MOVMain.StockQty = Sto;
		for (int j = 0; j < l - 2; j++)
			for (int k = j + 1; k < l - 1; k++)
				if (MOVMain.StockQty[k] < MOVMain.StockQty[j]) {
					temp = MOVMain.StockQty[j];
					MOVMain.StockQty[j] = MOVMain.StockQty[k];
					MOVMain.StockQty[k] = temp;
					t = Sort[j];
					Sort[j] = Sort[k];
					Sort[k] = t;
				}
		int count = 0;
		for (int i = 0; i < l - 1; i++) {
			if (MOVMain.StockQty[i] < Data.OrderData.PlaQty)
				count++;
			else
				break;
		}
		int AvailableSort[] = new int[l - 1 - count];
		int count2 = 0;
		for (int i = count; i < l - 1; i++) {
			AvailableSort[i - count] = Sort[i];
			if (MOVMain.StockQty[i] < 0.5 * MOVMain.MeanQty)
				count2++;
		}
		MOVMain.SortIndex = Sort;
		MOVMain.Used = AvailableSort;
		return count2;
	}

	// Sort Def������򣬰����Ӵ�С��
	public void Sort() {
		int l = MOVMain.data.length;
		double temp;
		int t;
		double Sto[] = new double[l - 1];
		int Sort[] = new int[l - 1];
		for (int i = 1; i < l; i++) {
			Sto[i - 1] = Double.parseDouble(MOVMain.data[i][5]);
			Sort[i - 1] = i;
		}
		MOVMain.StockQty = Sto;
		for (int j = 0; j < l - 2; j++)
			for (int k = j + 1; k < l - 1; k++)
				if (MOVMain.StockQty[k] > MOVMain.StockQty[j]) {
					temp = MOVMain.StockQty[j];
					MOVMain.StockQty[j] = MOVMain.StockQty[k];
					MOVMain.StockQty[k] = temp;
					t = Sort[j];
					Sort[j] = Sort[k];
					Sort[k] = t;
				}
		MOVMain.SortIndex = Sort;
		MOVMain.Used = Sort;
	}

	// PriSort ���ȼ�������򣬰����ȼ�MOV���ɿ��Ӵ�С��
	public int PriSort() {
		int l = MOVMain.data.length;
		double temp;
		int t;
		double Sto[] = new double[l - 1];
		int Sort[] = new int[l - 1];
		for (int i = 1; i < l; i++) {
			Sto[i - 1] = Double.parseDouble(MOVMain.data[i][5]);
			Sort[i - 1] = i;
		}
		int PriMovNumCount = 0;
		for (int i = 0; i < 7; i++) {
			if (MOVGUI.Ckeys[i].isSelected()) {
				for (int j = 1; j < l; j++) {
					if (MOVMain.data[j][1].equals(MOVGUI.Ckeys[i].getText()))
						PriMovNumCount++;
				}
			}
		}
		int TheRest = l - 1 - PriMovNumCount;
		int PriMovIndex[] = new int[PriMovNumCount];
		int TheRestMovIndex[] = new int[TheRest];
		double PriMovQty[] = new double[PriMovNumCount];
		double TheRestMovQty[] = new double[TheRest];
		int n1 = 0;
		int n2 = 0;
		for (int i = 0; i < l - 1; i++) {
			boolean flag = false;
			for (int j = 0; j < 7; j++) {
				if (MOVGUI.Ckeys[j].isSelected() && MOVMain.data[i + 1][1].equals(MOVGUI.Ckeys[j].getText())) {
					flag = true;
					break;
				}
			}
			if (flag) {
				PriMovQty[n1] = Sto[i];
				PriMovIndex[n1] = Sort[i];
				n1++;
			} else {
				TheRestMovQty[n2] = Sto[i];
				TheRestMovIndex[n2] = Sort[i];
				n2++;
			}
		}
		for (int j = 0; j < PriMovNumCount - 1; j++)
			for (int k = j + 1; k < PriMovNumCount; k++)
				if (PriMovQty[k] > PriMovQty[j]) {
					temp = PriMovQty[j];
					PriMovQty[j] = PriMovQty[k];
					PriMovQty[k] = temp;
					t = PriMovIndex[j];
					PriMovIndex[j] = PriMovIndex[k];
					PriMovIndex[k] = t;
				}
		for (int j = 0; j < TheRest - 1; j++)
			for (int k = j + 1; k < TheRest; k++)
				if (TheRestMovQty[k] > TheRestMovQty[j]) {
					temp = TheRestMovQty[j];
					TheRestMovQty[j] = TheRestMovQty[k];
					TheRestMovQty[k] = temp;
					t = TheRestMovIndex[j];
					TheRestMovIndex[j] = TheRestMovIndex[k];
					TheRestMovIndex[k] = t;
				}
		for (int i = 0; i < l - 1; i++) {
			if (i < PriMovNumCount) {
				Sto[i] = PriMovQty[i];
				Sort[i] = PriMovIndex[i];
			} else {
				Sto[i] = TheRestMovQty[i - PriMovNumCount];
				Sort[i] = TheRestMovIndex[i - PriMovNumCount];
			}
		}
		MOVMain.Used = Sort;
		MOVMain.StockQty = Sto;
		MOVMain.SortIndex = Sort;
		return PriMovNumCount;
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

	// ȱʡֵ������
	public static void LimitedErrorCheck(int value) {
		LimitedNum = value;
		if (LimitedNum > MaxQty) {
			LimitedError = true;
			JOptionPane.showMessageDialog(null, "�޴˿���������ϵ�MOV��������������룡\n", "��ʾ", JOptionPane.WARNING_MESSAGE);
		} else if (QtyPriority(LimitedNum) <= 5) {
			int n = JOptionPane.showConfirmDialog(null, "ȱʡֵ����,����MOV������,�����޽�,�Ƿ����?", "����", JOptionPane.YES_NO_OPTION);
			if (n == 0)
				LimitedError = false;
			else
				LimitedError = true;
		} else {
			LimitedError = false;
		}

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
			height = height + Double.parseDouble(data[MOVMain.Used[i]][2]) * result[0][i];
		}
		// ����תString��
		return Data.OrderData.P_MaxHeight - height;
	}

	// QPriority ����ȱʡֵ���Ͽ������MOV��
	public static int QtyPriority(int Qty) {
		int count = 0;
		int l = MOVMain.SortStockQty.length;
		for (int i = 0; i < l; i++) {
			if (MOVMain.SortStockQty[i] >= Qty)
				count++;
			if (MOVMain.SortStockQty[i] < Qty)
				break;

		}
		return count;
	}

	// ���ȸ���
	public void RateChange(int n, int d, String way) {
		if (rate < 90) {
			rate = rate + 5 * n / d;
			MOVGUI.setProgressString(rate, "�������..." + rate + "%(" + way + ")");
		}

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

	// ���ȼ�ѡ�����
	public boolean PriIsSelectCheck() {
		boolean flag = false;
		for (int i = 0; i < 7; i++) {
			if (MOVGUI.Ckeys[i].isSelected()) {
				flag = true;
				break;
			}
		}
		return flag;
	}
}
