package com.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class Netty {

	public void bind(int port) throws Exception {
		//配置服务端的NIO线程组，一个用于服务端接受客户端的连接，另一个用于进行sokcetchannel的网络读写
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024).childHandler(
					new ChildChannelHandler()).childOption(ChannelOption.SO_KEEPALIVE, true);
			
			//绑定端口，b是一个boss线程，负责处理来自端口的socket请求
			ChannelFuture f = b.bind(port).sync();
			//等待服务器监听端口关闭
			f.channel().closeFuture().sync();
			
		}finally {
			//退出，释放线程池资源
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
	
	private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

		@Override
		protected void initChannel(SocketChannel arg0) throws Exception {
			//解决粘包和拆包问题，]结束了就行，设置一个包的最大长度为2048
			ByteBuf delimiter = Unpooled.copiedBuffer("c".getBytes());
			arg0.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
			arg0.pipeline().addLast(new StringDecoder());
			arg0.pipeline().addLast(new ServerHandler());
		}
	}
}
