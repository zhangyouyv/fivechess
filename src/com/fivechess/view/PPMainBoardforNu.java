package com.fivechess.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.omg.CORBA.PRIVATE_MEMBER;

import com.fivechess.model.TimeThread;
import com.fivechess.net.Client;
import com.fivechess.net.NetTool;

/**
 * 人人对战页面
 * 接收信息线程
 * @author admin
 */
public class PPMainBoardforNu extends MainBoard {
    private PPChessBoardforNu cb;
    private JButton startGame;
    private JButton exitGame;
    private String ip;
    private String str;
    private String owner1;
    private String follow;
    public JButton getStartGame() {
		return startGame;
	}
	public void setStartGame(JButton startGame) {
		this.startGame = startGame;
	}
	public String getOwner1() {
		return owner1;
	}
	public void setOwner1(String owner1) {
		this.owner1 = owner1;
	}
	public String getFollow() {
		return follow;
	}
	public void setFollow(String follow) {
		this.follow = follow;
	}

	
    
    public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}

	private JButton back;//悔棋按钮
    private JButton send; //聊天发送按钮
    private JLabel timecount;//计时器标签
    //双方状态
    private JLabel people1;//自己标签
    private JLabel people2;//对手标签
    private JLabel p1lv;//自己等级标签
    private JLabel p2lv;//对手等级标签
    private JLabel situation1;//自己状态标签
    private JLabel situation2;//对手状态标签
	private JLabel jLabel1;
	private JLabel jLabel2;//
    private JTextArea talkArea;
    private JTextField tf_number; //输入IP框
    public JTextField getTf_number() {
		return tf_number;
	}
	public void setTf_number(JTextField tf_number) {
		this.tf_number = tf_number;
	}

	private JTextField talkField; //聊天文本框
    private String number;
    private DatagramSocket socket;
    private String gameState;
    private String enemyGameState;//敌人状态
    private Logger logger = Logger.getLogger("游戏");
    public JButton getstart(){return startGame;}
    public String getNumber() {return number;}
    public JTextField getTf() {return tf_number;}
    public DatagramSocket getSocket() {return socket;}
    public JLabel getLabel1(){return jLabel1;}
    public JLabel getLabel2(){return jLabel2;}
    public JLabel getSituation1(){return situation1;}
    public JLabel getSituation2(){return situation2;}
    public PPMainBoardforNu()
    {
        init();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    /**
     * 初始化页面
     */
    public void init()
    {
        gameState="NOT_START";
        enemyGameState="NOT_START";
        cb=new PPChessBoardforNu(this);
        cb.setClickable(PPMainBoardforNu.CAN_NOT_CLICK_INFO);
        cb.setBounds(210, 40, 570, 585);
        cb.setVisible(true);
        cb.setInfoBoard(talkArea);
        tf_number=new JTextField("请输入邀请码！");
        tf_number.setBounds(780, 75, 200, 30);
        tf_number.addMouseListener(this);
        startGame=new JButton("准备游戏");//设置名称，下同
        startGame.setBounds(780,130, 200, 50);//设置起始位置，宽度和高度，下同
        startGame.setBackground(new Color(50,205,50));//设置颜色，下同
        startGame.setFont(new Font("宋体", Font.BOLD, 20));//设置字体，下同
        startGame.addActionListener(this);
        back=new JButton("悔  棋");
        back.setBounds(780, 185, 200, 50);
        back.setBackground(new Color(85,107,47));
        back.setFont(new Font("宋体", Font.BOLD, 20));
        back.addActionListener(this);
        send=new JButton("发送");
        send.setBounds(840, 550, 60, 30);
        send.setBackground(new Color(50,205,50));
        send.addActionListener(this);
        talkField=new JTextField("聊天");
        talkField.setBounds(780, 510, 200, 30);
        talkField.addMouseListener(this);
        exitGame=new JButton("返  回");
        exitGame.setBackground(new Color(218,165,32));
        exitGame.setBounds(780,240,200,50);
        exitGame.setFont(new Font("宋体", Font.BOLD, 20));//设置字体，下同
        exitGame.addActionListener(this);
        people1=new JLabel("    我:");
        people1.setOpaque(true); 
        people1.setBackground(new Color(82,109,165));
        people1.setBounds(10,410,200,50);
        people1.setFont(new Font("宋体", Font.BOLD, 20));
        people2=new JLabel("    对手:");
        people2.setOpaque(true); 
        people2.setBackground(new Color(82,109,165));
        people2.setBounds(10,75,200,50);
        people2.setFont(new Font("宋体", Font.BOLD, 20));
        timecount=new JLabel("    计时器:");
        timecount.setBounds(320,1,200,50);
        timecount.setFont(new Font("宋体", Font.BOLD, 30));
        p1lv=new JLabel("    等 级:LV.1");
        p1lv.setOpaque(true); 
        p1lv.setBackground(new Color(82,109,165));
        p1lv.setBounds(10,130,200,50);
        p1lv.setFont(new Font("宋体", Font.BOLD, 20));
        p2lv=new JLabel("    等 级:LV.1");
        p2lv.setOpaque(true); 
        p2lv.setBackground(new Color(82,109,165));
        p2lv.setBounds(10,465,200,50);
        p2lv.setFont(new Font("宋体", Font.BOLD, 20));
        situation1=new JLabel("    状态:");
        situation1.setOpaque(true); 
        situation1.setBackground(new Color(82,109,165));
        situation1.setBounds(10,185,200,50);
        situation1.setFont(new Font("宋体", Font.BOLD, 20));
        situation2=new JLabel("    状态:");
        situation2.setOpaque(true); 
        situation2.setBackground(new Color(82,109,165));
        situation2.setBounds(10,520,200,50);
        situation2.setFont(new Font("宋体", Font.BOLD, 20));
 	    jLabel1 = new JLabel();
		add(jLabel1); 
		jLabel1.setBounds(130,75,200,50);
 	    jLabel2 = new JLabel();
		add(jLabel2); 
		jLabel2.setBounds(130,410,200,50);
        timecount=new JLabel("    计时器:");
        timecount.setBounds(320,1,200,50);
        timecount.setFont(new Font("宋体", Font.BOLD, 30));
        talkArea=new JTextArea();  //对弈信息
        talkArea.setEnabled(false);
        talkArea.setBackground(Color.BLUE);
        //滑动条
        JScrollPane p = new JScrollPane(talkArea);
        p.setBounds(780, 295, 200, 200);
        
        add(tf_number);
        add(cb);
        add(startGame);
        add(back);
        add(exitGame);
        add(people1);
        add(people2);
        add(p1lv);
        add(p2lv);
        add(situation1);
        add(situation2);
        add(timecount);
        add(p);
        add(send);
        add(talkField);
        //加载线程
        ReicThread();
        repaint();
    }
    /**
     * 接收信息放在线程中
     */
    public void ReicThread()
    {
        new Thread(new Runnable()
        {
            public void run()
            {
                try
                {
                    byte buf[]=new byte[1024];
                    socket=new DatagramSocket(10086);
                    DatagramPacket dp=new DatagramPacket(buf, buf.length);
                    while(true)
                    {
                        socket.receive(dp);
                        //0.接收到的发送端的主机名
                        InetAddress ia=dp.getAddress();
                        //enemyMsg.add(new String(ia.getHostName()));  //对方端口
                        logger.info("对手IP："+ia.getHostName());
                        //1.接收到的内容
                        String data=new String(dp.getData(),0,dp.getLength());
                        if(data.isEmpty())
                        {
                            cb.setClickable(MainBoard.CAN_NOT_CLICK_INFO);
                        }
                        else {
                            String[] msg = data.split(",");
                            System.out.println(msg[0]+" "+msg[1]);
                            //接收到对面准备信息并且自己点击了准备
                            if(msg[0].equals("ready"))
                            {
                                enemyGameState="ready";
                                System.out.println("对方已准备");
                                if(gameState.equals("ready"))
                                {
                                    gameState="FIGHTING";
                                    cb.setClickable(MainBoard.CAN_CLICK_INFO);
                                    startGame.setText("正在游戏");                                    
                                    situation1.setText("    状态:等待...");
                                    situation2.setText("    状态:下棋...");
                                    logger.info("等待对方消息");
                                    timer=new TimeThread(label_timeCount);
                                    timer.start();
                                }
                            }
                            else if(msg[0].equals("POS")){
                                System.out.println("发送坐标");
                                //接受坐标以及角色
                                situation1.setText("    状态:等待...");
                                situation2.setText("    状态:下棋...");
                                //重新启动计时线程
                                timer=new TimeThread(label_timeCount); 
                                timer.start();
                                
                                cb.setCoord(Integer.parseInt(msg[1]), Integer.parseInt(msg[2]), Integer.parseInt(msg[3]));
                                
                            }
                            else if(msg[0].equals("enemy"))
                            {
                            	talkArea.append("对手："+msg[1]+"\n");
                            	logger.info("对手发送的消息"+msg[1]);
                            }
                            else if(msg[0].equals("back"))
                            {
                            	int n=JOptionPane.showConfirmDialog(cb, "是否同意对方悔棋", "选择",JOptionPane.YES_NO_OPTION);
                            	//点击确定按钮则可以悔棋
                            	if(n==JOptionPane.YES_OPTION)
                            	{
                            		cb.backstep();
                            		NetTool.sendUDPBroadCast(ia.getHostName(), "canBack"+", ");
                            	}
                            	else {
                            		NetTool.sendUDPBroadCast(ia.getHostName(), "noBack"+", ");
								}
                            	
                            }
                            //允许悔棋
                            else if(msg[0].equals("canBack"))
                            {
                            	JOptionPane.showMessageDialog(cb, "对方允许您悔棋");
                            	cb.backstep();
                            }
                            //不允许悔棋
                            else if(msg[0].equals("noBack"))
                            {
                            	JOptionPane.showMessageDialog(cb, "对方不允许您悔棋");
                            }

                        }
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==startGame)
        {
            if(!tf_number.getText().isEmpty()&&
                    !tf_number.getText().equals("不能为空")&&
                        !tf_number.getText().equals("请输入IP地址")&&
                            !tf_number.getText().equals("不能连接到此IP")) {
            	number=tf_number.getText();
                startGame.setEnabled(false);
                startGame.setText("等待对方准备");
                tf_number.setEditable(false);
                //把这个number发送到服务器进行比对并拿回对方的ip地址
                Client client = new Client("192.168.1.38",8089);
				//启动一条线程
				new Thread(){
					public void run(){
						OutputStream os= null;
			            try {
			                os= client.socket.getOutputStream();
			                String in=number;
			                //获取客户端的ip地址
			                String ip = InetAddress.getLocalHost().getHostAddress().toString();
			                //客户端发送的邀请码加上标记
			                String ins = "f"+in+" "+"p"+ip+"r";
			                os.write((""+ins).getBytes());
			                os.flush();
			            } catch (IOException e) {
			                e.printStackTrace();
			            }
					}
					}.start();
					
					//启动一条线程监听服务器发来的消息
					new Thread(){
						public void run(){
					//获取服务器发送过来的对面的ip
					 ServerSocket server = null;
					int port1=8090;
					try {
						server=new ServerSocket(port1);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						String str=null;
				        while(true)
				        {
				        	
				        	//等待客户端连入
				        	  System.out.println("等待服务器发送ip地址......");
				            Socket connection=server.accept();
				            System.out.println("连接成功！");
				            String ip = connection.getLocalAddress().getHostAddress();
				            //看流传过来的数据
				            InputStream  mString = connection.getInputStream();
				            ByteArrayOutputStream bos = new ByteArrayOutputStream();  
				          //读取缓存  
				          byte[] buffer = new byte[2048];  
				          int length = 0;  
				          if((length = mString.read(buffer)) != -1) {  
				        	  System.out.println("进入流操作！");
				              bos.write(buffer, 0, length);//写入输出流  
				          }  
				          mString.close();//读取完毕，关闭输入流  
				            System.out.println("..........................................");
				          // 根据得到流创建字符串对象  
				         str = new String(bos.toByteArray(), "UTF-8");
				         //房主的ip为：
				         owner1 = str.substring(str.indexOf("Q")+1, str.indexOf("W"));
				         //被邀请人的ip为：
				         follow = str.substring(str.indexOf("E")+1, str.indexOf("R"));
				         
				         System.out.println("拿到的IP地址为："+owner1+"---"+follow);
				         //判断本机是不是房主
		                    InetAddress addr = null;
							try {
								addr = InetAddress.getLocalHost();
							} catch (UnknownHostException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}  
		                    String local=addr.getHostAddress().toString(); 
				        //发送准备好信息
				         if(local.equals(owner1)){
			                NetTool.sendUDPBroadCast(follow,"ready, ");
			                gameState="ready";
				         }else{
				        	 NetTool.sendUDPBroadCast(owner1,"ready, ");
				                gameState="ready";
				         }
				        }
					} catch (UnknownHostException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
						}
						}.start();
                
                if(enemyGameState=="ready")
                {
                    gameState="FIGHTING";
                    cb.setClickable(CAN_CLICK_INFO);
                    startGame.setText("正在游戏");
                    situation1.setText("    状态:等待...");
                    situation2.setText("    状态:下棋...");
                    timer=new TimeThread(label_timeCount);
                    timer.start();
                }
            }
            else 
            {
                tf_number.setText("不能为空");
            }
        }
        //点击悔棋后的操作
        else if(e.getSource()==back)
        {
        	//发送悔棋信息
        	NetTool.sendUDPBroadCast(str,"back"+", ");	
        	logger.info("玩家选择悔棋");
        }
        // 聊天发送按钮
        else if(e.getSource()==send)
        {
        	if(!talkField.getText().isEmpty()&&!talkField.getText().equals("不能为空"))
        	{
        		//获得输入的内容
        		String msg=talkField.getText();
        		talkArea.append("我："+msg+"\n");
        		talkField.setText("");
        		ip=tf_number.getText();
        		NetTool.sendUDPBroadCast(str,"enemy"+","+msg);
        	}
        	else {
        		talkField.setText("不能为空");
			}
        	
        }
        //退出游戏，加载主菜单
        else if(e.getSource()==exitGame)
        {
            dispose();
            new SelectMenu();
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
    	if(e.getSource()==tf_number)
    	{
    		tf_number.setText("");
    	}
    	else if(e.getSource()==talkField)
    	{
    		talkField.setText("");
    	}
    }
}
