package com.fivechess.net;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread{
	private Socket ThisSocket=null;
    private PrintWriter output=null;
    private DataInputStream input=null;
    public ServerThread(Socket This) throws IOException
    {
        ThisSocket=This;           
        output=new PrintWriter(ThisSocket.getOutputStream());
        input=new DataInputStream(ThisSocket.getInputStream());
    }
    public void run()
    {
        String recv;
        try
        {
            while((recv=input.readLine())!=null)
            {
         
                System.out.println("客户端发来信息:"+input);
                output.write("服务器端已接受信息"+input);
                if(recv.equals("end"));
                    break;
            }
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        finally
        {
            try 
            {
                input.close();
                output.close();
                
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

}
