package com.dingding.ddframe.ddbase.utils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okio.BufferedSink;

/**
 * Created by zzz on 15/8/7.
 */
public class UploadUtil {

    private static final OkHttpClient client = new OkHttpClient();
    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");

    public String uploadPic(String url, Bitmap bitmap, final Handler handler, JSONObject jsonObject) throws Exception {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        final byte[] byteArray = stream.toByteArray();
//        JSONObject jsonObject = new JSONObject();
        RequestBody requestBody = new RequestBody() {
            @Override
            public MediaType contentType() {
                return MEDIA_TYPE_MARKDOWN;
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
                byte[] bytes = new byte[1024];

                int pos;
                int curLen = 0;
                Message msg = Message.obtain();

                while ((pos = bais.read(byteArray)) != -1) {
                    curLen += pos;
                    sink.write(bytes, 0, pos);
                    msg.obj = new int[]{curLen, byteArray.length};
                    handler.sendMessage(msg);

                }
                bais.close();
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
