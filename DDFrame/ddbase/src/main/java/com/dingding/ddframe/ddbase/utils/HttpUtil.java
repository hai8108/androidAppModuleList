package com.dingding.ddframe.ddbase.utils;

import android.content.Context;

import com.dingding.ddframe.ddbase.DDApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by wqk on 15/7/30.
 */
public class HttpUtil {
    private final static int CACHE_TIME = 0x10000; //单位毫秒

    /**
     * 保存从网络取回的数据
     *
     * @param ser  数据
     * @param file key
     * @return 是否缓存成功
     */
    public static boolean saveObject(Serializable ser, String file) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = DDApplication.getInstance().openFileOutput(file, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(ser);
            oos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                oos.close();
            } catch (Exception e) {
            }
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }


    /**
     * 读取缓存到本地的对象
     *
     * @param file key
     * @return
     */
    public static Serializable readObject(String file) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = DDApplication.getInstance().openFileInput(file);
            ois = new ObjectInputStream(fis);
            return (Serializable) ois.readObject();
        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ois.close();
            } catch (Exception e) {
            }
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return null;
    }


    /**
     * 在请求接口时调用
     * 检测是否在规定时间内调用相同的接口，用于判断是否需要请求网络
     *
     * @param cacheFile key
     * @return 是否需要请求网络
     */
    public static boolean isCacheDataFailure(String cacheFile) {
        boolean failure = false;
        File data = DDApplication.getInstance().getFileStreamPath(cacheFile);
        if (data.exists() && (System.currentTimeMillis() - data.lastModified()) > CACHE_TIME)
            failure = true;
        else if (!data.exists())
            failure = true;
        return failure;
    }

}
