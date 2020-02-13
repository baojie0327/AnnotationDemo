package com.jackson.bindviewapi;

import android.app.Activity;
import android.util.Log;

/*
 * ViewBinder  2019-12-09
 * Copyright (c) 2019 KL Co.Ltd. All right reserved.
 *
 */
/*
 * class description here
 * @author Jackson
 * @version 1.0.0
 * since 2019 12 09
 */
public class ViewBinder {

    public static void bind(Activity activity){

        try {
            Class clazz=Class.forName(activity.getClass().getCanonicalName()+"$$ViewBinder");
            IViewBind<Activity> iViewBinder= (IViewBind<Activity>) clazz.newInstance();
            iViewBinder.bind(activity);
        } catch (ClassNotFoundException e) {
            Log.d("hbj--exp",e.getException()+"");
            Log.d("hbj--cause",e.getCause()+"");
            Log.d("hbj--mess",e.getMessage()+"");
            Log.d("hbj--class",e.getClass()+"");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }
}