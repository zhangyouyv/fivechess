package com.fivechess.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

public class Server {
	 private ServerSocket server;
	    public static ArrayList<Socket> socketgroup;
	    public static final int port1=8089;
	    Map map = new HashMap<>();
	    public Server() throws IOException
	    {
	        server=new ServerSocket(port1);
	        socketgroup=new ArrayList<Socket>();
	    }
	    //服务器获取的服务器的ip和房间号的映射
	    public void running() throws IOException
	    {
	    	String str=null,str1=null,str2=null,str3 = null;
	        while(true)
	        {
	        	
	        	//等待客户端连入
	        	  System.out.println("等待客户端连入......");
	            Socket connection=server.accept();
	            System.out.println("客户端连接成功！");
	            
	            //看流传过来的数据
	            InputStream  mString = connection.getInputStream();
	            ByteArrayOutputStream bos = new ByteArrayOutputStream();  
	          //读取缓存  
	          byte[] buffer = new byte[2048];  
	          int length = 0;  
	          if((length = mString.read(buffer)) != -1) {  
	              bos.write(buffer, 0, length);//写入输出流  
	          }  
	          mString.close();//读取完毕，关闭输入流  
	            
	          // 根据得到流创建字符串对象  
	         str = new String(bos.toByteArray(), "UTF-8");  
	            System.out.println(str);  
	            //System.out.println(ip);
	            if(str.contains("i")){
	            	str3 = str.substring(str.indexOf("i")+1,str.indexOf("!"));
	            }
	            
	            if(str.contains("o")){
	            	str1 = str.substring(str.indexOf("o")+1,str.indexOf(" "));
	            	System.out.println("房主的号："+str1);
	            	
	            	map.put(str1, str3);
	            }
	            	if(str.contains("f")){
		            	str2 = str.substring(str.indexOf("f")+1,str.indexOf(" "));
		            	
	            	String clientip = str.substring(str.indexOf("p")+1,str.indexOf("r"));
	            	System.out.println(clientip);
	            	System.out.println("房主房间号str1："+str1+",被邀请人的房间号str2:"+str2+"房主的ip是："+str3+"客户端的ip是："+clientip);
	            	//服务端首先判定，房主这边已经准备好
	            	NetTool.sendUDPBroadCast(clientip,"ready, ");
	            	String gameState = "ready";
	            	System.out.println("房主已经准备好！！！");
	            	if(str1 != null && str2 != null){
	            		if(str1.equals(str2)){
	            			//获取房主的ip
	            			String value = map.get(str1).toString();
	            			//将ip发送给客户端
	            			System.out.println("房主的ip在map中储存的是："+value);
	        				//启动一条线程，把ip发送给FOLLOW客户端
	            			Socket socket = new Socket(clientip,8090);
	        				new Thread(){
	        					public void run(){
	        						OutputStream os= null;
	        			            try {
	        			                os= socket.getOutputStream();
	        			                String in="Q"+value+"W"+"E"+clientip+"R";
	        			                os.write((""+in).getBytes());
	        			                os.flush();
	        			                System.out.println("发送成功！！！");
	        			            } catch (IOException e) {
	        			                e.printStackTrace();
	        			            }
	        					}
	        					}.start();
	        					
	        					//启动一条线程，把ip发送给OWNER客户端
		            			
		        				new Thread(){
		        					public void run(){
		        						OutputStream os= null;
		        						Socket socket2 = null;
										try {
											socket2 = new Socket(value,8095);
										} catch (UnknownHostException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										} catch (IOException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
		        			            try {
		        			                os= socket2.getOutputStream();
		        			                String in="Q"+value+"W"+"E"+clientip+"R";
		        			                os.write((""+in).getBytes());
		        			                os.flush();
		        			                System.out.println("发送成功！！！");
		        			            } catch (IOException e) {
		        			                e.printStackTrace();
		        			            }
		        					}
		        					}.start();
	            		
		            		
	            		}
	            		else{
	            			JOptionPane.showConfirmDialog(null, "邀请码码不存在！！！");
	            		}
	            	}
	            	}
	            	
	          	 //System.out.println("值为："+value);
	            
	        }
	       
	    }
	    
	    
	    public static void main(String[] args) throws IOException
	    {
	        new Server().running();
	        System.out.println("启动成功");
	    }
      
}
