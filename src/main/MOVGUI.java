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
	// 设置运行时窗口的大小
	private int Width = 480;
	private int Height = 300;
	Dimension faceSize = new Dimension(Width, Height);
	// 获得屏幕的大小
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	// 功能键
	private JButton Confirm = new JButton();// 确认
	private JButton Revocation = new JButton();// 撤回
	private JButton Reread = new JButton();// 重读
	// 字体
	private Font cfont = new Font("微软雅黑", Font.BOLD, 14);
	// 进度条
	private static JProgressBar progressbar;
	// 限制值
	private TextField limited = new TextField();
	// order表信息
	private static JLabel ordermessage[] = new JLabel[7];
	// 优先级
	private static JPanel Priority = new JPanel();
	public static JRadioButton def = new JRadioButton("def");
	public static JRadioButton opdef = new JRadioButton("opdef");
	public static JRadioButton Ckeys[] = new JRadioButton[7];
	//数据类
	public static Data data; 
	private MOVMain Main;
	public MOVGUI() {
		// TODO Auto-generated constructor stub
		super("SSAL MOV Configuration");
		// 初始化面板
		PanelInit();
		//初始化数据
		data=new Data();
		//初始化主函数数据
		Main=new MOVMain();
		// 确认按钮的监听器
		Confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean MovRVError=Data.OrderData.MovRVError;
				if (!MovRVError) {
					if (MOVMain.RebSumit) {
						 Main.Solve();
					} else {
						setProgressError("未重读数据,请重读数据后重试!");
					}
				} else {
					double MinRV=Data.OrderData.FMinRV;
					double MaxRV=Data.OrderData.MaxRV;
					JOptionPane.showMessageDialog(null,
							"<html><p>残压值有误！请检查后再试!</p><p>MinRV=" + MinRV + "&nbsp;>&nbsp;MaxRV=" + MaxRV + "</html>",
							"提示", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		// 重读数据监听器
		Reread.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				data.Init();
				Main.Init();
			}
		});
		// 撒回按键监听器
		Revocation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (Output.ResultOutputRowCount == 0) {
					JOptionPane.showMessageDialog(null, "当前没写入数据，无法撒回！\n", "提示", JOptionPane.WARNING_MESSAGE);
				} else if (MOVMain.RestoreFlag) {
					JOptionPane.showMessageDialog(null, "已撒回上一订单，无法撒回！\n", "提示", JOptionPane.WARNING_MESSAGE);
				} else {
					if (Output.Restore()) {
						MOVMain.RestoreFlag = true;
						JOptionPane.showMessageDialog(null, "撒回成功！\n", "提示", JOptionPane.WARNING_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, "撒回失败！\n", "提示", JOptionPane.WARNING_MESSAGE);
					}

				}
			}
		});
		// 默认按钮监听器
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
		// 缺省值变化监听器
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

	// MOV优先级监听器类
	class PriAdapter implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			def.setSelected(false);
			opdef.setSelected(false);
		}
	}

	// 面板初始化
	public void PanelInit() {
		JPanel box = new JPanel();
		box.setLayout(null);
		box.setOpaque(false);
		/************* 左侧面板 *************/
		BoxPanel left = new BoxPanel("配置设置");
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
		/************* 中间面板 *************/
		BoxPanel middle = new BoxPanel("配置要求");
		String message[]={"PO: ","PlanID: ","Type: ","MaxHeight: ","MOV: ","MinRV: ","MaxRV: "};
		for(int i=0;i<7;i++)
		{	
			ordermessage[i]=new JLabel(message[i]);
			middle.add(ordermessage[i]);
			ordermessage[i].setBounds(20,30+i*16,140,15);
		}
		JLabel ml1 =new JLabel("缺省值:");
		box.add(ml1);
		ml1.setBounds(172,175,50,20);
		box.add(limited);
		limited.setBounds(222,175,80,20);
		/************* 右侧面板 *************/
		BoxPanel right = new BoxPanel("功能选择");
		// 按钮面板
		Confirm = new JButton("确认");
		Confirm.setFont(cfont);
		Confirm.setBorder(BorderFactory.createEmptyBorder());
		Reread = new JButton("重读");
		Reread.setFont(cfont);
		Reread.setBorder(BorderFactory.createEmptyBorder());
		Revocation = new JButton("撤回");
		Revocation.setFont(cfont);
		Revocation.setBorder(BorderFactory.createEmptyBorder());
		right.add(Reread);
		Reread.setBounds(35, 35, 80, 30);
		right.add(Confirm);
		Confirm.setBounds(35, 75, 80, 40);
		right.add(Revocation);
		Revocation.setBounds(35, 125, 80, 30);
		/** 进度条，状态栏 **/
		// 面板，用来装进度条
		JPanel foot = new JPanel();
		// 设置背景色
		foot.setOpaque(false);
		UIManager.put("ProgressBar.selectionForeground", Color.BLACK); // 进度条字体颜色
		progressbar = new JProgressBar(0, 100);
		progressbar.setStringPainted(true);// 设置进度条上的字符串显示，false则不能显示
		progressbar.setPreferredSize(new Dimension(Width * 3 / 4, 25));// 设置进度条大小
		progressbar.setBackground(Color.WHITE);// 设置进度条背景色
		progressbar.setForeground(Color.GREEN);// 设置进度条前景色
		progressbar.setString("");
		foot.add(progressbar);
		/************* 背景面板 *************/
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
		// 设置窗口居中
		setLocation((int) (screenSize.width - faceSize.getWidth()) / 2,
				(int) (screenSize.height - faceSize.getHeight()) / 2);
		// 设置窗口大小
		setSize(faceSize);
		// 设置为右见
		setVisible(true);
		// 设置为不可最大化
		setResizable(false);
		// 设置可关闭
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(bg);
		validate();
	}

	// 设置配置选项
	public static void setOptions(String[] message) {
		if (message.length > 7)
			return;
		if(message.length>4)
			Priority.setLayout(new GridLayout(2+message.length, 1));
		Priority.removeAll();// 移除所有内容
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
	
	// 设置订单信息
	public static void setOrderMessage(String[] message) {
		if (message.length > 7)
			return;
		String fmessage[]={"PO: ","PlanID: ","Type: ","MaxHeight: ","MOV: ","MinRV: ","MaxRV: "};
		for (int i = 0; i < message.length; i++) {
			ordermessage[i].setText(fmessage[i]+message[i]);
		}
	
	}

	// 设置程序出错情况
	public static void setProgressError(String message) {
		Dimension d = progressbar.getSize();
		Rectangle rect = new Rectangle(0, 0, d.width, d.height);
		// 程序出错，设前景色为红色
		progressbar.setForeground(Color.RED);
		// 设置进度值
		progressbar.setValue(100);
		// 显示运状态
		progressbar.setString(message);
		// 立即重绘
		progressbar.paintImmediately(rect);
	}

	// 设置进度条显示的值
	public static void setProgressString(int value, String text) {
		Dimension d = progressbar.getSize();
		Rectangle rect = new Rectangle(0, 0, d.width, d.height);
		// 判断是前景色是否为绿色，不是则改为绿色，因程序可能出错把前景色设为红色
		if (progressbar.getForeground() != Color.GREEN)
			progressbar.setForeground(Color.GREEN);
		// 设置进度值
		progressbar.setValue(value);
		// 显示运状态
		progressbar.setString(text);
		// 立即重绘
		progressbar.paintImmediately(rect);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new MOVGUI();
	}

	// 背景面板
	class BackgroundPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		// 背景图片
		private Image BackgroubdImage = Toolkit.getDefaultToolkit().getImage(MOVGUI.class.getResource("logo.png"));

		// 构造方法
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
				int width = getWidth();// 获取组件大小
				int height = getHeight();
				// 绘制背景图片
				g.drawImage(BackgroubdImage, 0, 0, width, height, this);// 绘制图片与组件大小相同
			}
		}

	}

	// 功能面板
	class BoxPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		// 构造方法
		public BoxPanel(String message) {
			super();
			setOpaque(false);
			setLayout(null);
			JLabel title = new JLabel(message);
			title.setFont(new Font("微软雅黑", Font.BOLD, 12));
			add(title);
			title.setBounds(53, 0, 60, 30);
		}

		protected void paintComponent(Graphics g) {// 重写绘制组件外观
			int width = getWidth();// 获取组件大小
			int height = getHeight();
			g.drawLine(5, 15, 5, height - 5);
			g.drawLine(5, 15, width / 2 - 30, 15);
			g.drawLine(width / 2 + 30, 15, width - 5, 15);
			g.drawLine(width - 5, 15, width - 5, height - 5);
			g.drawLine(5, height - 5, width - 5, height - 5);
			super.paintComponent(g);// 执行超类方法
		}
	}
}
