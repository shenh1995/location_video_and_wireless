package com.faceComparison;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FaceComparisonTest {
	
    /**
    * 重要提示代码中所需工具类
    * FileUtil,Base64Util,HttpUtil,GsonUtils请从
    * https://ai.baidu.com/file/658A35ABAB2D404FBF903F64D47C1F72
    * https://ai.baidu.com/file/C8D81F3301E24D2892968F09AE1AD6E2
    * https://ai.baidu.com/file/544D677F5D4E4F17B4122FBD60DB82B3
    * https://ai.baidu.com/file/470B3ACCA3FE43788B5A963BF0B625F3
    * 下载
     * @throws InterruptedException 
    */
    public static double match(String imagePath1, String imagePath2) throws InterruptedException {
	
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/face/v3/match";
        
        String result = "";
        
        try {
            byte[] bytes1 = FileUtil.readFileByBytes(imagePath1);
            byte[] bytes2 = FileUtil.readFileByBytes(imagePath2);
            String image1 = Base64Util.encode(bytes1);
            String image2 = Base64Util.encode(bytes2);

            List<Map<String, Object>> images = new ArrayList<Map<String, Object>>();

            Map<String, Object> map1 = new HashMap<String, Object>();
            map1.put("image", image1);
            map1.put("image_type", "BASE64");
            map1.put("face_type", "LIVE");
            map1.put("quality_control", "LOW");
            map1.put("liveness_control", "NONE");

            Map<String, Object> map2 = new HashMap<String, Object>();
            map2.put("image", image2);
            map2.put("image_type", "BASE64");
            map2.put("face_type", "LIVE");
            map2.put("quality_control", "LOW");
            map2.put("liveness_control", "NONE");

            images.add(map1);
            images.add(map2);

            String param = GsonUtils.toJson(images);

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = AuthService.getAccessToken();

            result = HttpUtil.post(url, accessToken, "application/json", param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Thread.sleep(500);
        
        if (result.contains("score")) {
            String [] resStrings = result.split("e\":");
            return Double.parseDouble(resStrings[2].substring(0, 7));
		}else {
			return 0;
		}
    }

    public static void main(String[] args) throws InterruptedException {
    	
    	String imagePath1 = "C:\\Users\\Dell\\Desktop\\1.png";
    	String imagePath2 = "C:\\Users\\Dell\\Desktop\\2.jpg";
        System.out.println(imagePath1 + " " + imagePath2);
    	double result = FaceComparisonTest.match(imagePath1, imagePath2);
    	System.out.println(result);
    }
}
