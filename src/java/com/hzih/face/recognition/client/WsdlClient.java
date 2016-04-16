package com.hzih.face.recognition.client;

import com.hzih.face.recognition.client.entity.Data;
import com.hzih.face.recognition.client.entity.XmlString;
import com.hzih.face.recognition.utils.Configuration;
import org.apache.log4j.Logger;
import sun.misc.BASE64Encoder;

import java.io.*;

/**
 * Created by Administrator on 15-8-1.
 */
public class WsdlClient {
    final static Logger logger = Logger.getLogger(WsdlClient.class);
    public static void main(String[] args) {
        String faceServer = "192.168.1.2:8021";
        FARSWebCommonService service = new FARSWebCommonService();
        String targetUrl = "http://192.168.1.2:8081/ws/criminal?wsdl";

        FARSWebCommon common = service.getFARSWebCommonPort();
        String license = XmlString.makeLicense("admin", "0");
        InputStream in = null;
        String filePath = "D:/test/photo1_248_336.jpg";
        try {
            in = new FileInputStream(filePath);
            byte[] buff =  new byte[in.available()];
            in.read(buff);
            in.close();
            BASE64Encoder encoder = new BASE64Encoder();

            String image = encoder.encode(buff);
//            image = "xxx";
            String information = XmlString.makeInformation(
                    "1","0",
//                    "292,453,397,450,0,0",1,
                    "0,0,0,0,0,0",1,
                     10,0,
                    "","","","",
//                    "1960-01-01 00:00:00","2013-01-01 00:00:00","0","0",
                    image); //1.1 1比N比对接口  1.4  1比N比对接口(返回人员信息)
            String onResponse = common.fars1TON(license,information);
//            String onResponse = common.fars1TONRETURNALLINFO(license,information);

//            List<String> featureIds = new ArrayList<String>();
//            featureIds.add("112");
//            featureIds.add("111");
//            information = XmlString.makeFarsGetPersonInfo(featureIds); //1.2 获取人员信息接口
//            String onResponse = common.farsGETPERSONINFO(license,information);

//            information = XmlString.makeFarsGetLogicDbInfos();//1.3 获取逻辑分库接口
//            String onResponse = common.farsGETLOGICDBINFOS(license,information);

//            information = XmlString.makeFars1TONResultInformation(""+212,1,2);// 1.5 分页获取比对结果人员信息接口
//            String onResponse = common.fars1TONRETURNALLINFO(license,information);

//            information = XmlString.makeFarsAddPerson(image);//1.6 人员入库
//            String onResponse = common.farsADDPERSON(license,information);


//            information = XmlString.makeFarsAddImage("247",image);//1.7 新增图像
//            String onResponse = common.farsADDIMAGE(license, information);

//            information = XmlString.makeFarsModifyFacePoint();//1.8 修改特征点位置
//            String onResponse = common.farsMODIFYFACEPOINT(license,information);

//            information = XmlString.makeFarsDeleteImage();//1.9 删除图像
//            String onResponse = common.farsDELETEIMAGE(license, information);

//            information = XmlString.makeFarsModifyPerson();//1.10 修改人员信息
//            String onResponse = common.farsMODIFYPERSON(license,information);

//            information = XmlString.makeFarsDeletePerson();//1.11 删除人员
//            String onResponse = common.farsDELETEPERSON(license, information);


            System.out.println(onResponse);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static FARSWebCommon getFARSWebCommon(){
        FARSWebCommonService service = new FARSWebCommonService();
        FARSWebCommon common = service.getFARSWebCommonPort();
        return common;
    }


    public static Data getFars1ToN(String license, String information) throws UnsupportedEncodingException {
        FARSWebCommon common = getFARSWebCommon();
        String onResponse = common.fars1TON(license,information);
        logger.info(onResponse);
        Configuration config = new Configuration(onResponse.getBytes("utf-8"));
        return config.get1ToNData();
    }

    public static Data getFarsGETPERSONINFO(String license,String information) throws UnsupportedEncodingException {
        FARSWebCommon common = getFARSWebCommon();
        String onResponse = common.farsGETPERSONINFO(license,information);
        logger.info(onResponse);
        Configuration config = new Configuration(onResponse.getBytes("utf-8"));
        return config.getPersonInfoData();

    }
}
