package com.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

import org.apache.log4j.Logger;

import com.Main.Main;
import com.db.ConnectionRedis;
import com.google.gson.Gson;
import com.pojo.CameraWarnFaceInformation;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.CharsetUtil;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

public class VideoServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	private Logger logger = Logger.getLogger(VideoServerHandler.class);
	
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		logger.info(cause.getMessage() + "  error");
		ctx.close();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
		ByteBuf jsonBuf = msg.content();
		String jsonStr = jsonBuf.toString(CharsetUtil.UTF_8);
		JSONObject jsonObject = JSONObject.fromObject(jsonStr);
		if ((Integer) jsonObject.get("type") == 1) {
			parseFaceInformation(jsonStr);
		}
		ctx.channel().writeAndFlush("{\"msg\":\"OK\",\"code\":200}");
		ctx.close();
	}

	/**
	 * @param postData
	 * @throws IOException 
	 * @throws InterruptedException 
	 * 将人脸数据解析为jpg文件，然后按照每个设备一个文件夹存储到对应的文件夹中
	 *   存储的格式为设备名/时间.jpg
	 *   并且在redis中存储一个 （设备名+时间，0）的键值对，代表图片已经存在
	 */
	private void parseFaceInformation(String postData) throws IOException, InterruptedException {
		CameraWarnFaceInformation faceInformation = new Gson().fromJson(postData, CameraWarnFaceInformation.class);
		logger.info("采集到一张人脸");
		for (int i = 0; i < faceInformation.faces.size(); i++) {
			String base64FaceData = faceInformation.faces.get(i);
			byte[] bitmapBytes = Base64.getDecoder().decode(base64FaceData);
			File fileDirectory = new File(faceInformation.cid);

			if (!fileDirectory.exists()) {
				fileDirectory.mkdirs();
			}

			FileOutputStream write = new FileOutputStream(
					new File(fileDirectory + "/" + faceInformation.time + ".jpg"));
			write.write(bitmapBytes);
			write.close();
			
			Jedis jedis = ConnectionRedis.getJedis();
			jedis.hset(faceInformation.cid, "" + faceInformation.time, "0");
			ConnectionRedis.releaseJedis(jedis);
		}
	}
}
