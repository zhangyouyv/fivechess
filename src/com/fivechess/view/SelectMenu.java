package com.fivechess.view;


import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileInputStream;
import java.net.URL;

import javax.naming.InitialContext;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;

import com.fivechess.net.EmailHelper;

import sun.audio.*;

/**
 * 登录页面
 * 选择人机对战还是人人对战
 * @author admin
 *
 */
public class SelectMenu extends JFrame implements MouseListener{
	private JTextArea talkArea;
	public static void main(String[] args) {
		new SelectMenu();
	}
	public SelectMenu()
	{
		
		setVisible(true);
		setLayout(null); //取消原来布局
		setBounds(580,185,290,420);
		setResizable(false);
		paintBg(); //页面
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addMouseListener(this);
		
	}
	
	/**
	 * 添加背景图片，设置位置
	 */
	private void paintBg() {
		// TODO Auto-generated method stub
		ImageIcon image = new ImageIcon("images/main.jpg");
		image.setImage(image.getImage().getScaledInstance(290, 420, Image.SCALE_DEFAULT));
        JLabel la = new JLabel(image);
        la.setBounds(0, 0, this.getWidth(), this.getHeight());//添加图片，设置图片大小为窗口的大小。
        this.getLayeredPane().add(la, new Integer(Integer.MAX_VALUE)); //将JLabel加入到面板容器的最上层
	}
	
	@Override
 	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		//日志
		Logger logger = Logger.getLogger("菜单");
		//获取点击坐标
		int x=e.getX();
		int y=e.getY();
		//System.out.println(x+" "+y);
		if(x>=70 && x<=255 && y>=336&& y<=383)
		{
			// 加载人机对战页
			dispose();
			logger.info("用户选择人机对战页面");
			new ChooseLevel();
		}
		//点击加载声音
		if(x>=70 && x<=200 && y>=383&& y<=500)
		{
			// 用户选择添加背景音乐
			logger.info("用户选择添加背景音乐");
			//模拟循环播放
			
				playMusic();
			
		
		}
		else if(x>=70 && x<=255 && y>=125 && y<=172)
		{
			//加载人人对战页面
			dispose();
			logger.info("用户选择人人对战页面");
			new PPMainBoard();
		}
		else if(x>=7 && x<=40 && y>=200&& y<=250)
		{
		//弹出意见框
			logger.info("用户提出意见！！！");
			//弹窗
			String suggestions = JOptionPane.showInputDialog(talkArea,"请填写建议！");
			 EmailHelper emailHelper = new EmailHelper();
			 if(suggestions!=null){
				 emailHelper.sendEmail("514306210@qq.com", "来自五子棋的玩家的建议！！", suggestions); 
			 }
		}
		else if(x>=7 && x<=40 && y>=250&& y<=300)
		{
			//当鼠标划过指定区域的时候，会弹出广告
			logger.info("用户触发广告！！！");
			//弹窗
			//声明一个图标
			ImageIcon icon = new ImageIcon("images/ad.gif");
			 JOptionPane.showMessageDialog(talkArea, null, "广告", 1, icon);
		}
		else if(x>=7 && x<=40 && y>=83&& y<=107)
		{
		//退出
			dispose();
			logger.info("用户点击退出游戏");
			System.exit(0);
		}
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	@Override
	public void mouseExited(MouseEvent e) {
	}
	@SuppressWarnings("restriction")
	public void playMusic()
	{
		try {
			/*FileInputStream fileau=new FileInputStream("E:/git/FiveChess/src/com/fivechess/music/music.wav" );
			@SuppressWarnings("restriction")
			AudioStream as=new AudioStream(fileau);
			AudioPlayer.player.start(as);*/
			//循环播放音乐
			URL url = new URL("http://fjdx.sc.chinaz.com/files/download/sound1/201202/667.wav");
			AudioClip player = Applet.newAudioClip(url);
			player.loop();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
}
