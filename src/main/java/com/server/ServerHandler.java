package com.server;

import java.io.UnsupportedEncodingException;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ServerHandler extends ChannelHandlerAdapter{
	
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException, Exception {
		String string = (String) msg;
		System.out.println(string);
	}
	
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		System.out.println(cause.getMessage() + "error");
		ctx.close();
	}
}
