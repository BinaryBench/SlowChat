package me.binarybench.slowchat;


import java.util.HashMap;

/**
 * Created by Bench on 1/27/2016.
 */
public class Test {


    public static void main(String[] args)
    {
        String message = "3.2";

        int l = (int) (Float.parseFloat(message) * 1000);

        System.out.println(l);
    }


}
