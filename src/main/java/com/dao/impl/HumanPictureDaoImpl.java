package com.dao.impl;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.dao.HumanPictureDao;
import com.db.ConnectionDB;
import com.pojo.HumanPicture;

public class HumanPictureDaoImpl implements HumanPictureDao{

	Logger logger = Logger.getLogger(HumanPictureDaoImpl.class);
	
	/* (non-Javadoc)
	 * @see com.dao.HumanPictureDao#insertHumanPicture(java.util.List)
	 * 将一组人脸存储到数据库中
	 */
	public void insertHumanPicture(List<HumanPicture> humanPictures) {
		try {
			Connection connection = ConnectionDB.getConnection();
			
			String insert_wifi_sql = "insert into HumanPicture (picture_path, time, device_id) values(?,?,?)";
			
			PreparedStatement psts = connection.prepareStatement(insert_wifi_sql); 
			
			for (HumanPicture humanPicture : humanPictures) {
				psts.setString(1, humanPicture.getPicture_path());
				psts.setString(2, humanPicture.getTime());
				psts.setString(3, humanPicture.getDevice_id());
				psts.addBatch();
			}
			psts.executeBatch();
			
			logger.info("HumanPicture 数据库插入成功");
			psts.close();
			connection.close();
			
		} catch (SQLException e) {
			logger.error("HumanPicture 数据库插入失败");
			e.printStackTrace();
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
	}
}
