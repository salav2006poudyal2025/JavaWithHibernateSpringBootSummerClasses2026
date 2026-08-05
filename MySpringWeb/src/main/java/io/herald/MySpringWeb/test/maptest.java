package io.herald.MySpringWeb.test;

import com.cloudinary.utils.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

public class maptest {
    public static void main(String[] args) {

//        Map<Integer,String> map = new HashMap<>();
//
//        map.put(1,"apple");
//        map.put(2,"banana");
//
//        System.out.println(map);


       Map a= ObjectUtils.asMap(

                "1","apple"
        );

        System.out.println(a);


    }
}
