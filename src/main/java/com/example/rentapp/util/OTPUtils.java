package com.example.rentapp.util;

import java.util.Random;

public class OTPUtils {

    public static String randomCode(){
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }

}
