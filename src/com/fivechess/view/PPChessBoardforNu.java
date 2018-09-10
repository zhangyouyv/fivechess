package com.fivechess.view;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.mail.Session;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;

import com.fivechess.judge.Judge;
import com.fivechess.model.Chess;
import com.fivechess.net.EmailHelper;
import com.fivechess.net.NetTool;

/**
 * 人人对战棋盘
 * @author admin
 */
public class PPChessBoardforNu extends ChessBoard{
    private int role; //角色
    String str=null,owner2=null,follow2=null;
    private JTextArea talkArea; //交流信息
    private PPMainBoardforNu mb;
    private PPMainBoard mbn;
    private int step[][]=new int[30*30][2];//定义储存步数数组
    private int stepCount=0;//初始化数组
    //加载黑白棋子，用于判定玩家所执棋
    private ImageIcon imageIcon1= new ImageIcon(blackChess);
    private ImageIcon imageIcon2= new ImageIcon(whiteChess);
    private Logger logger = Logger.getLogger("棋盘");
    /**
     * 构造函数，初始化棋盘的图片，初始化数组
     * @param mb 人人对战页面
     */
    public PPChessBoardforNu(PPMainBoardforNu mb) {
        super();
        this.mb=mb;
        //设置先开始游戏的玩家执白
        setRole(Chess.WHITE);
        
      //启动一条线程监听服务器发来的消息
		new Thread(){
			public void run(){
		//获取服务器发送过来的对面的ip
		 ServerSocket server = null;
		int port1=8095;
		try {
			server=new ServerSocket(port1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			
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
	         owner2 = str.substring(str.indexOf("Q")+1, str.indexOf("W"));
	         //被邀请人的ip为：
	         follow2 = str.substring(str.indexOf("E")+1, str.indexOf("R"));
	         
	         System.out.println("拿到的IP地址为："+owner2+"---"+follow2);
	         //判断本机是不是房主
                InetAddress addr = null;
				try {
					addr = InetAddress.getLocalHost();
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}  
                String local=addr.getHostAddress().toString(); 
                if(local.equals(owner2)){
                	mb.getstart().setEnabled(false);
                	mb.getTf_number().setEditable(false);
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
			
			
    }
   
    /**
     * 设置聊天窗口
     * @param area 聊天窗口
     */
    public void setInfoBoard(JTextArea area)
    {
        talkArea=area;
    }
    /**
     * 保存黑白棋子的坐标于二维数组中
     * @param posX
     * @param posY
     */
    private void saveStep(int posX,int posY)
    {
        stepCount++;
        step[stepCount][0]=posX;
        step[stepCount][1]=posY;
    }
    /**
     * 悔棋，去掉黑白棋子各一个
     */
    public void backstep() {
		// TODO Auto-generated method stub
    	if(stepCount>=2)
        {
            chess[step[stepCount][0]][step[stepCount][1]]=0;
            chess[step[stepCount-1][0]][step[stepCount-1][1]]=0;
            stepCount=stepCount-2;
        }
	}
    /**
     * 设置棋子横坐标
     * @param x,y,r 横坐标,纵坐标,对方的角色黑/白
     */
    public void setCoord(int x,int y,int r)
    {
    	//对方执白，自己执黑
        if(r==Chess.WHITE)
        {
            role=Chess.BLACK;
            mb.getLabel1().setIcon(imageIcon2);
            mb.getLabel2().setIcon(imageIcon1);
        }
        //对方执黑，自己执白
        else
        {
            role = Chess.WHITE;
            mb.getLabel1().setIcon(imageIcon1);
            mb.getLabel2().setIcon(imageIcon2);
        }
        chess[x][y]=r;
        saveStep(x, y); //保存坐标
        int winner=Judge.whowin(x,y,chess,r);
        WinEvent(winner);
        setClickable(MainBoard.CAN_CLICK_INFO);
        repaint();
    }
    /**
     * 设置角色
     * @param role 角色
     */
    public void setRole(int role)
    {
        this.role=role;
    }
    /**
     * 获得角色
     * @return 角色
     */
    public int getRole()
    {
        return role;
    }
    /**
     * 从父类继承的方法，自动调用，绘画图形
     * @param g 该参数是绘制图形的句柄
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
    /**
     * 获得结果
     * @return 结果
     */
    public int getResult()
    {
        return result;
    }
    /**
     * 胜利事件
     * @param winner 胜方
     */
    public void WinEvent(int winner)
    {
        //白棋赢
        if(winner == Chess.WHITE) {
        	//中断线程
            try {
                mb.getTimer().interrupt();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            mb.getstart().setText("开始游戏");
            mb.getstart().setEnabled(true);
            result=Chess.WHITE;
            ScreenShoot cam= new ScreenShoot("d:\\Hello", "png");
            cam.snapShot();
            EmailHelper emailHelper = new EmailHelper();
            Session session = emailHelper.getSession();
            emailHelper.sendEmail("514306210@qq.com", "五子棋胜利分享", "<img src='d:\\Hello1'>");
            JOptionPane.showMessageDialog(mb,"恭喜！白棋获胜");
            logger.info("白棋获胜！初始化页面");
            setClickable(PPMainBoard.CAN_NOT_CLICK_INFO);
            //初始化页面
            initArray();
            mb.getLabel().setText(null);

        }
        //黑棋赢
        else if(winner==Chess.BLACK){
        	//中断线程
            try {
                mb.getTimer().interrupt();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            mb.getstart().setText("开始游戏");
            mb.getstart().setEnabled(true);
            result=Chess.BLACK;
            setClickable(MainBoard.CAN_NOT_CLICK_INFO);
            //获胜的时候屏幕截图，分享棋盘
            ScreenShoot cam= new ScreenShoot("d:\\Hello", "png");
            cam.snapShot();
            JOptionPane.showMessageDialog(mb,"恭喜！黑棋获胜");
            //调用邮箱分享
            EmailHelper emailHelper = new EmailHelper();
            Session session = emailHelper.getSession();
            emailHelper.sendEmail("514306210@qq.com", "五子棋胜利分享", "<img src='d:\\Hello1'>");
            logger.info("黑棋获胜！初始化页面");
            //初始化页面
            initArray();
            mb.getLabel().setText(null);
        }
    }
    /**
     * 按下鼠标时，记录鼠标的位置，并改写数组的数值，重新绘制图形
     * @param e
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if(clickable==PPMainBoard.CAN_CLICK_INFO) {
            chessX = e.getX();
            chessY = e.getY();
            //限定点击区域为棋盘区域
            if (chessX < 524 && chessX > 50 && chessY < 523 && chessY > 50) {
                float x = (chessX - 49) / 25;
                float y = (chessY - 50) / 25;
                int x1 = (int) x;
                int y1 = (int) y;
                //如果这个地方没有棋子
                if (chess[x1][y1] == Chess.BLANK) {
                    
                    if(role== Chess.WHITE) {
                        logger.info("白棋落子:"+x1+","+y1);
                        mb.getSituation1().setText("    状态:下棋...");
                        mb.getSituation2().setText("    状态:等待...");
                    }
                    else if(role==Chess.BLANK){
                        logger.info("黑棋落子:"+x1+","+y1);
                        mb.getSituation1().setText("    状态:下棋...");
                        mb.getSituation2().setText("    状态:等待...");
                    }
                    //计时线程中断
                    try {
                        mb.getTimer().interrupt();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    chess[x1][y1] = role;
                    saveStep(x1, y1);
                    //判断本机是不是房主
                    InetAddress addr = null;
					try {
						addr = InetAddress.getLocalHost();
					} catch (UnknownHostException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}  
                    String local=addr.getHostAddress().toString(); 
                    System.out.println("本地ip"+local);
                    
                  //发送棋坐标
                    if(mb.getFollow() == null && mb.getOwner1() == null){

                        if(local.equals(owner2)){
                        	NetTool.sendUDPBroadCast(follow2,"POS"+","+x1 + "," + y1 + "," + role);
                        }else{
                        	NetTool.sendUDPBroadCast(owner2,"POS"+","+x1 + "," + y1 + "," + role);
                        }
                    }else{
                    	
                    
                    if(local.equals(mb.getOwner1())){
                    	NetTool.sendUDPBroadCast(mb.getFollow(),"POS"+","+x1 + "," + y1 + "," + role);
                    }else{
                    	NetTool.sendUDPBroadCast(mb.getOwner1(),"POS"+","+x1 + "," + y1 + "," + role);
                    }
                    }
                    
                    //判断输赢
                    int winner=Judge.whowin(x1,y1,chess,role);
                    WinEvent(winner);
                    setClickable(MainBoard.CAN_NOT_CLICK_INFO);
                }
            }
        }
    }
    
    /**
     *鼠标点击事件
     *@param e
     **/
     @Override
     public void mouseMoved(MouseEvent e) {
     	if(clickable==MainBoard.CAN_CLICK_INFO) {
     		 mousex=e.getX();
     		 mousey=e.getY();
     		 repaint();
     	}
     }

}
