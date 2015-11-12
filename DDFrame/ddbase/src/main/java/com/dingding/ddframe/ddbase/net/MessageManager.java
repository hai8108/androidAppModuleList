package com.dingding.ddframe.ddbase.net;

import android.os.Handler;
import android.os.Message;

/**
 * 消息管理
 *
 * @author wujh
 */
public class MessageManager {

    private Handler handler = null;
    private Object result;

    public MessageManager(Handler handler) {
        this.handler = handler;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public Object getResult() {
        return result;
    }

    private void setResult(Object result) {
        this.result = result;
    }

    public void sendHandlerMessage(int msgid, Object obj) {
        if (handler != null) {
            Message message = new Message();
            message.what = msgid;
            message.obj = obj;
            handler.sendMessage(message);
        }
        setResult(obj);
    }

    public static void sendMessage(Handler handler, int msgid, Object obj) {
        if (handler != null) {
            Message message = new Message();
            message.what = msgid;
            message.obj = obj;
            handler.sendMessage(message);
        }
    }
}
