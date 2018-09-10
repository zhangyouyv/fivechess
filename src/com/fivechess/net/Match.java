package com.fivechess.net;

public class Match {
	//获取随机房间号的方法
	public Integer getHouseNumber(){
		Integer houseNumber = (int)(Math.random()*1000000);
		return houseNumber;
	}
	//连接服务器，把本机的ip和生成的房间号发送给服务器
	
	
}
