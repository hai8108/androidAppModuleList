package com.dingding.ddframe.ddbase.tests;

import com.alibaba.fastjson.JSON;
import com.dingding.ddframe.ddbase.commons.Entitys.User;
import com.dingding.ddframe.ddbase.net.HttpClient;

import java.io.IOException;

/**
 * Created by zzz on 15/8/19.
 */
public class TestBaseInfos {

    public static void main(String[] args) {

        /*User user = new User();
        user.setName("name");
        HttpClient client = new HttpClient();
        try {
            client.postRemove("ss", user);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

//        User


        String s = "{\"name\":1111,\"email\":222}";
        User user = JSON.parseObject(s, User.class);
        String email = user.getEmail();


        double a =22544.15 , x = 0 , y = 0;
        a = x + y;

        if ((x - 3500) > 0 && y > 0) {
            double cal1 = cal(x - 3500);
            double cal2 = cal(y);
            System.out.print(cal1 + cal2);
        }



    }


    private static double cal(double n) {
        if (n > 9000) {
            return (n - 9000) * 0.25 + 4500 * 0.2 + 3000 * 0.1 + 1500 * 0.03;
        }else if (n > 4500) {
            return (n-4500)*0.2 +3000 * 0.1 + 1500 * 0.03;
        }else if (n > 1500) {
            return (n -1500)*0.1 +1500 * 0.03;
        }else if (n > 0) {
            return (n-1500)*0.03;
        }
        return 0;
    }

}
