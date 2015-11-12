package com.dingding.ddframe.ddbase.net;

import android.os.Handler;

import com.alibaba.fastjson.JSON;
import com.dingding.ddframe.ddbase.commons.ParamBuild;
import com.dingding.ddframe.ddbase.utils.StringUtils;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okio.BufferedSink;

/**
 * Created by zzz on 15/7/28.
 */
public class HttpClient {
    private static final OkHttpClient client = new OkHttpClient();
//    private static BaseInfos mInfos ;

    public HttpClient(/*BaseInfos mInfos*/) {
        super();
//        this.mInfos = mInfos;
        client.setConnectTimeout(20, TimeUnit.SECONDS);
        client.setWriteTimeout(20, TimeUnit.SECONDS);
        client.setReadTimeout(30, TimeUnit.SECONDS);
    }



    public String postEntity(String url, Object entity ,LinkedHashMap baseMap) throws IOException {
//        LinkedHashMap baseMap = mInfos.getBase();
        String param = JSON.toJSONString(entity);
        String sign = ParamBuild.getSign(entity, baseMap);
        baseMap.put("sign", sign);
        String base = JSON.toJSONString(baseMap);
        RequestBody formBody = new FormEncodingBuilder()
                .add("base", base)
                .add("param", param)
                .build();
        URL myurl = new URL(url);
        Request request = new Request.Builder()
                .url(myurl)
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String postMap(String url, Map map,LinkedHashMap baseMap) throws IOException {

        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry= (Map.Entry) iterator.next();
            if (entry.getValue()==null || StringUtils.isNull(entry.getValue().toString())){
                iterator.remove();
            }
        }

//        LinkedHashMap baseMap = mInfos.getBase();
        Iterator it = baseMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry= (Map.Entry) it.next();
            if (entry.getValue()==null || StringUtils.isNull(entry.getValue().toString())){
                it.remove();
            }
        }
        String param = JSON.toJSONString(map);
        String sign = ParamBuild.getSign(map, baseMap);
        baseMap.put("sign", sign);
        String base = JSON.toJSONString(baseMap);

        RequestBody formBody = new FormEncodingBuilder()
                .add("base", base)
                .add("param", param)
                .build();
        URL myurl = new URL(url);
        Request request = new Request.Builder()
                .url(myurl)
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }


    public String postRemove(String url, Object entity,LinkedHashMap baseMap) throws IOException {

        Map entityMap = (Map) JSON.toJSON(entity);

        return postMap(url ,entityMap,baseMap);
    }

    public static final MediaType MEDIA_TYPE_MARKDOWN  = MediaType.parse("text/x-markdown; charset=utf-8");

    /**
     * 上传图片流
     * @param url
     * @param content
     * @param fileTag
     * @param businessType
     * @param ext
     * @throws Exception
     */
    public void postStream(String url , byte[] content, String fileTag, String businessType,String ext) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ext", ext); //文件扩展名
        jsonObject.put("businessType", businessType); //业务类型
        jsonObject.put("fileTag", fileTag);    //上传文件类型。   枚举值
        jsonObject.put("sign", "12312313");
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN ,content))
                .header("UPLOAD-JSON", jsonObject.toString())
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        System.out.println(response.body().string());
    }

    /**
     * Posting a File
     */
    public String postFile(String url, String filePath, String fileTag, String businessType) {
        String string = "";
        File file = new File(filePath);
        if (!file.exists() || file.isDirectory()) {
            return string;
        }
        try {
            OkHttpClient client = new OkHttpClient();
            MediaType MEDIA_TYPE_POSTFILE = MediaType.parse("text/x-markdown; charset=utf-8");
            Request.Builder builder = new Request.Builder();
            String fileName = file.getName();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ext", fileName.substring(fileName.lastIndexOf(".") + 1));
            jsonObject.put("businessType", businessType);
            jsonObject.put("fileTag", fileTag);
            jsonObject.put("sign", "12312313");
            builder.header("UPLOAD-JSON", jsonObject.toString());
            builder.url(url);
            builder.post(RequestBody.create(MEDIA_TYPE_POSTFILE, file));
            Request request = builder.build();
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            string = response.body().string();
        } catch (Exception e) {
        }
        return string;
    }


    public String uploadPic(String url, final String filePath, String fileTag, String businessType ,Handler handler) throws Exception{
        final File file = new File(filePath);

        String fileName = file.getName();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ext", fileName.substring(fileName.lastIndexOf(".") + 1));
        jsonObject.put("businessType", businessType);
        jsonObject.put("fileTag", fileTag);
        jsonObject.put("sign", "12312313");


        RequestBody requestBody = new RequestBody() {
            @Override
            public MediaType contentType() {
                return MEDIA_TYPE_MARKDOWN;
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                OutputStream outputStream = sink.outputStream();
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] bytes=new byte[1024];
                int pos = 0;
                while((pos = fileInputStream.read(bytes, 0, bytes.length)) != -1){
                    fileInputStream.read(bytes, 0, pos);
                    outputStream.write(bytes);
                }

            }
        };

        Request request = new Request.Builder()
                .header("UPLOAD-JSON", jsonObject.toString())
                .url(url)
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response.body().string();
    }
}
