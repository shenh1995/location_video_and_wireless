package com.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

public class VideoServer {

	public void start(int port) throws Exception {
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
		public void initChannel(SocketChannel ch) throws Exception {
			ch.pipeline().addLast("http-decoder", new HttpRequestDecoder());
			/**usually we receive http message infragment,if we want full http message,
			 * we should bundle HttpObjectAggregator and we can get FullHttpRequest。
			 * 我们通常接收到的是一个http片段，如果要想完整接受一次请求的所有数据，我们需要绑定HttpObjectAggregator，然后我们
			 * 就可以收到一个FullHttpRequest-是一个完整的请求信息。
			**/
			/*
			 * 注意排序的问题
			 */
			ch.pipeline().addLast("http-encoder", new HttpResponseEncoder());
			ch.pipeline().addLast("http-aggegator", new HttpObjectAggregator(65536 * 1000));
			ch.pipeline().addLast("client-codec", new HttpClientCodec());
			ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
			ch.pipeline().addLast("servercodec",new HttpServerCodec());
			
			ch.pipeline().addLast(new VideoServerHandler());
	}
}
	
	public static void main(String[] args) throws Exception {
		new VideoServer().start(8091);
	}


}
