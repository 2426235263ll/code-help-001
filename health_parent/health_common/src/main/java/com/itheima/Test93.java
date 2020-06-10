package com.itheima;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;

public class Test93 {

        //使用七牛云提供的SDK实现将本地图片上传到七牛云服务器
        @Test
        public void test1() {
            //构造一个带指定Zone对象的配置类
            Configuration cfg = new Configuration(Zone.autoZone());
            //...其他参数参考类注释
            UploadManager uploadManager = new UploadManager(cfg);
            //...生成上传凭证，然后准备上传
            String accessKey = "aHuPsf4TU47uzu3Za-k0NeVUmri2IzTNpImQJTmM";
            String secretKey = "GvQu1uwGVLt2Eqamq4UUAJq52_5Rm3Z8DWwkQTU2";

            String bucket = "wh-93-1205";


            //如果是Windows情况下，格式是 D:\\qiniu\\test.png
            String localFilePath = "D:\\cad\\1.png";

            String key = new Date().getTime() + "1.png";

            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(localFilePath, key, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);

                System.out.println(putRet.key);
                System.out.println(putRet.hash);

            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        }

        @Test
        public void  test2() throws IOException {
            XSSFWorkbook excel = new XSSFWorkbook("C:\\Users\\张柚子\\Desktop\\93.xlsx");

            XSSFSheet sheetAt = excel.getSheetAt(0);


            //关闭资源
            excel.close();
        }
    }


