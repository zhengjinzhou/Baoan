package com.zhou.baoan.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by zhou on 2017/12/11.
 */

public class ToastUtil {
    public static void show(Context context,String str){
        Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
    }
}
