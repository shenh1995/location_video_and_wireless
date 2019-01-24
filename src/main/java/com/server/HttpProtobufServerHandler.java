package com.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.CharsetUtil;


public class HttpProtobufServerHandler extends SimpleChannelInboundHandler<FullHttpRequest>{
	
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		System.out.println(cause.getMessage() + "  error");
		ctx.close();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
		ByteBuf jsonBuf = msg.content();
		String jsonStr = jsonBuf.toString(CharsetUtil.UTF_8);
		System.out.println(jsonStr);
	}
}
