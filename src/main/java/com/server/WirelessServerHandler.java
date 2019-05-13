package com.server;

import com.Main.Main;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.CharsetUtil;

public class WirelessServerHandler extends SimpleChannelInboundHandler<FullHttpRequest>{
	
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		System.out.println(cause.getMessage() + "  error");
		ctx.close();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {

		ByteBuf jsonBuf = msg.content();
		String jsonStr = jsonBuf.toString(CharsetUtil.UTF_8);
		if(!jsonStr.contains("blue")) {
			//把采集的无线数据信息放入到无线数据仓库中
			Main.storage.push(jsonStr); 
		}
		ctx.close();
	}
}
