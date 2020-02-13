package com.jackson.annotationdemo;

import com.jackson.bindviewannotation.MyAnnotation;

/*
 * t  2019-12-11
 * Copyright (c) 2019 KL Co.Ltd. All right reserved.
 *
 */
/*
 * class description here
 * @author Jackson
 * @version 1.0.0
 * since 2019 12 11
 */
@MyAnnotation(id=1,name="jack")
public class MyTest {

    public static void main(String[] args){

        boolean hasAnnotation=MyTest.class.isAnnotationPresent(MyAnnotation.class);

        if (hasAnnotation){
            MyAnnotation myAnnotation=MyTest.class.getAnnotation(MyAnnotation.class);

            System.out.println("id=="+myAnnotation.id());
            System.out.println("name=="+myAnnotation.name());
        }
    }

}