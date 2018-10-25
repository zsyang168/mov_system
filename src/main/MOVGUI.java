package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.UIManager;

import Input.Data;
import Output.Output;

public class MOVGUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// ��������ʱ���ڵĴ�С
	private int Width = 480;
	private int Height = 300;
	Dimension faceSize = new Dimension(Width, Height);
	// �����Ļ�Ĵ�С
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	// ���ܼ�
	private JButton Confirm = new JButton();// ȷ��
	private JButton Revocation = new JButton();// ����
	private JButton Reread = new JButton();// �ض�
	// ����
	private Font cfont = new Font("΢���ź�", Font.BOLD, 14);
	// ������
	private static JProgressBar progressbar;
	// ����ֵ
	private TextField limited = new TextField();
	// order����Ϣ
	private static JLabel ordermessage[] = new JLabel[7];
	// ���ȼ�
	private static JPanel Priority = new JPanel();
	public static JRadioButton def = new JRadioButton("def");
	public static JRadioButton opdef = new JRadioButton("opdef");
	public static JRadioButton Ckeys[] = new JRadioButton[7];
	//������
	public static Data data; 
	private MOVMain Main;
	public MOVGUI() {
		// TODO Auto-generated constructor stub
		super("SSAL MOV Configuration");
		// ��ʼ�����
		PanelInit();
		//��ʼ������
		data=new Data();
		//��ʼ������������
		Main=new MOVMain();
		// ȷ�ϰ�ť�ļ�����
		Confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean MovRVError=Data.OrderData.MovRVError;
				if (!MovRVError) {
					if (MOVMain.RebSumit) {
						 Main.Solve();
					} else {
						setProgressError("δ�ض�����,���ض����ݺ�����!");
					}
				} else {
					double MinRV=Data.OrderData.FMinRV;
					double MaxRV=Data.OrderData.MaxRV;
					JOptionPane.showMessageDialog(null,
							"<html><p>��ѹֵ�������������!</p><p>MinRV=" + MinRV + "&nbsp;>&nbsp;MaxRV=" + MaxRV + "</html>",
							"��ʾ", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		// �ض����ݼ�����
		Reread.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				data.Init();
				Main.Init();
			}
		});
		// ���ذ���������
		Revocation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (Output.ResultOutputRowCount == 0) {
					JOptionPane.showMessageDialog(null, "��ǰûд�����ݣ��޷����أ�\n", "��ʾ", JOptionPane.WARNING_MESSAGE);
				} else if (MOVMain.RestoreFlag) {
					JOptionPane.showMessageDialog(null, "��������һ�������޷����أ�\n", "��ʾ", JOptionPane.WARNING_MESSAGE);
				} else {
					if (Output.Restore()) {
						MOVMain.RestoreFlag = true;
						JOptionPane.showMessageDialog(null, "���سɹ���\n", "��ʾ", JOptionPane.WARNING_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, "����ʧ�ܣ�\n", "��ʾ", JOptionPane.WARNING_MESSAGE);
					}

				}
			}
		});
		// Ĭ�ϰ�ť������
		def.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (def.isSelected()) {
					opdef.setSelected(false);
					for (int i = 0; i < 7; i++)
						Ckeys[i].setSelected(false);
				}
			}
		});
		//
		opdef.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (opdef.isSelected()) {
					for (int i = 0; i < 7; i++)
						Ckeys[i].setSelected(false);
					def.setSelected(false);
				}
			}
		});
		// ȱʡֵ�仯������
		limited.addTextListener(new TextListener() {
			@Override
			public void textValueChanged(TextEvent arg0) {
				// TODO Auto-generated method stub
				int num;
				if (limited.getText().trim().equals("") || limited.getText() == null)
					num = 0;
				else
					num= Integer.parseInt(limited.getText());
				MOVMain.LimitedErrorCheck(num);
			}
		});
	}

	// MOV���ȼ���������
	class PriAdapter implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			def.setSelected(false);
			opdef.setSelected(false);
		}
	}

	// ����ʼ��
	public void PanelInit() {
		JPanel box = new JPanel();
		box.setLayout(null);
		box.setOpaque(false);
		/************* ������ *************/
		BoxPanel left = new BoxPanel("��������");
		Priority.setLayout(new GridLayout(6, 1));
		Priority.setOpaque(false);
		def.setOpaque(false);
		Priority.add(def);
		opdef.setOpaque(false);
		Priority.add(opdef);
		for (int i = 0; i < 7; i++) {
			Ckeys[i] = new JRadioButton("");
			Ckeys[i].setOpaque(false);
		}
		left.add(Priority);
		Priority.setBounds(40, 25, 100, 130);
		/************* �м���� *************/
		BoxPanel middle = new BoxPanel("����Ҫ��");
		String message[]={"PO: ","PlanID: ","Type: ","MaxHeight: ","MOV: ","MinRV: ","MaxRV: "};
		for(int i=0;i<7;i++)
		{	
			ordermessage[i]=new JLabel(message[i]);
			middle.add(ordermessage[i]);
			ordermessage[i].setBounds(20,30+i*16,140,15);
		}
		JLabel ml1 =new JLabel("ȱʡֵ:");
		box.add(ml1);
		ml1.setBounds(172,175,50,20);
		box.add(limited);
		limited.setBounds(222,175,80,20);
		/************* �Ҳ���� *************/
		BoxPanel right = new BoxPanel("����ѡ��");
		// ��ť���
		Confirm = new JButton("ȷ��");
		Confirm.setFont(cfont);
		Confirm.setBorder(BorderFactory.createEmptyBorder());
		Reread = new JButton("�ض�");
		Reread.setFont(cfont);
		Reread.setBorder(BorderFactory.createEmptyBorder());
		Revocation = new JButton("����");
		Revocation.setFont(cfont);
		Revocation.setBorder(BorderFactory.createEmptyBorder());
		right.add(Reread);
		Reread.setBounds(35, 35, 80, 30);
		right.add(Confirm);
		Confirm.setBounds(35, 75, 80, 40);
		right.add(Revocation);
		Revocation.setBounds(35, 125, 80, 30);
		/** ��������״̬�� **/
		// ��壬����װ������
		JPanel foot = new JPanel();
		// ���ñ���ɫ
		foot.setOpaque(false);
		UIManager.put("ProgressBar.selectionForeground", Color.BLACK); // ������������ɫ
		progressbar = new JProgressBar(0, 100);
		progressbar.setStringPainted(true);// ���ý������ϵ��ַ�����ʾ��false������ʾ
		progressbar.setPreferredSize(new Dimension(Width * 3 / 4, 25));// ���ý�������С
		progressbar.setBackground(Color.WHITE);// ���ý���������ɫ
		progressbar.setForeground(Color.GREEN);// ���ý�����ǰ��ɫ
		progressbar.setString("");
		foot.add(progressbar);
		/************* ������� *************/
		BackgroundPanel bg = new BackgroundPanel();
		box.add(left);
		left.setBounds(5, 30, 155, 170);
		box.add(middle);
		middle.setBounds(162, 30, 155, 145);
		box.add(right);
		right.setBounds(319, 30, 150, 170);
		box.add(foot);
		foot.setBounds(80, 200, 320, 30);
		box.setBackground(null);
		box.setOpaque(false);
		foot.setBounds(60, 200, Width * 3 / 4, 30);
		bg.setLayout(new BorderLayout(5, 5));
		bg.add("Center", box);
		//
		// ���ô��ھ���
		setLocation((int) (screenSize.width - faceSize.getWidth()) / 2,
				(int) (screenSize.height - faceSize.getHeight()) / 2);
		// ���ô��ڴ�С
		setSize(faceSize);
		// ����Ϊ�Ҽ�
		setVisible(true);
		// ����Ϊ�������
		setResizable(false);
		// ���ÿɹر�
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(bg);
		validate();
	}

	// ��������ѡ��
	public static void setOptions(String[] message) {
		if (message.length > 7)
			return;
		if(message.length>4)
			Priority.setLayout(new GridLayout(2+message.length, 1));
		Priority.removeAll();// �Ƴ���������
		Priority.add(def);
		Priority.add(opdef);
		for (int i = 0; i < message.length; i++) {
			Ckeys[i].setText(message[i]);
			Priority.add(Ckeys[i]);
			Ckeys[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					def.setSelected(false);
					opdef.setSelected(false);
				}
			});
		}
		Priority.repaint();
	}
	
	// ���ö�����Ϣ
	public static void setOrderMessage(String[] message) {
		if (message.length > 7)
			return;
		String fmessage[]={"PO: ","PlanID: ","Type: ","MaxHeight: ","MOV: ","MinRV: ","MaxRV: "};
		for (int i = 0; i < message.length; i++) {
			ordermessage[i].setText(fmessage[i]+message[i]);
		}
	
	}

	// ���ó���������
	public static void setProgressError(String message) {
		Dimension d = progressbar.getSize();
		Rectangle rect = new Rectangle(0, 0, d.width, d.height);
		// ���������ǰ��ɫΪ��ɫ
		progressbar.setForeground(Color.RED);
		// ���ý���ֵ
		progressbar.setValue(100);
		// ��ʾ��״̬
		progressbar.setString(message);
		// �����ػ�
		progressbar.paintImmediately(rect);
	}

	// ���ý�������ʾ��ֵ
	public static void setProgressString(int value, String text) {
		Dimension d = progressbar.getSize();
		Rectangle rect = new Rectangle(0, 0, d.width, d.height);
		// �ж���ǰ��ɫ�Ƿ�Ϊ��ɫ���������Ϊ��ɫ���������ܳ����ǰ��ɫ��Ϊ��ɫ
		if (progressbar.getForeground() != Color.GREEN)
			progressbar.setForeground(Color.GREEN);
		// ���ý���ֵ
		progressbar.setValue(value);
		// ��ʾ��״̬
		progressbar.setString(text);
		// �����ػ�
		progressbar.paintImmediately(rect);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new MOVGUI();
	}

	// �������
	class BackgroundPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		// ����ͼƬ
		private Image BackgroubdImage = Toolkit.getDefaultToolkit().getImage(MOVGUI.class.getResource("logo.png"));

		// ���췽��
		public BackgroundPanel() {
			super();
			setOpaque(false);
			setLayout(null);
			JLabel title = new JLabel("SSAL MOV Configuration");
			title.setFont(new Font("Arial", Font.BOLD, 20));
			add(title);
			title.setBounds(120, 0, 240, 40);
		}

		public void paintComponent(Graphics g) {
			super.paintComponents(g);
			if (BackgroubdImage != null) {
				int width = getWidth();// ��ȡ�����С
				int height = getHeight();
				// ���Ʊ���ͼƬ
				g.drawImage(BackgroubdImage, 0, 0, width, height, this);// ����ͼƬ�������С��ͬ
			}
		}

	}

	// �������
	class BoxPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		// ���췽��
		public BoxPanel(String message) {
			super();
			setOpaque(false);
			setLayout(null);
			JLabel title = new JLabel(message);
			title.setFont(new Font("΢���ź�", Font.BOLD, 12));
			add(title);
			title.setBounds(53, 0, 60, 30);
		}

		protected void paintComponent(Graphics g) {// ��д����������
			int width = getWidth();// ��ȡ�����С
			int height = getHeight();
			g.drawLine(5, 15, 5, height - 5);
			g.drawLine(5, 15, width / 2 - 30, 15);
			g.drawLine(width / 2 + 30, 15, width - 5, 15);
			g.drawLine(width - 5, 15, width - 5, height - 5);
			g.drawLine(5, height - 5, width - 5, height - 5);
			super.paintComponent(g);// ִ�г��෽��
		}
	}
}
