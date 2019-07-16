package com.example.commonlibray.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import skin.support.content.res.SkinCompatResources;

public class ResUtils {
    public static boolean isDefaultSkin(){
        return SkinCompatResources.getInstance().isDefaultSkin();
    }

    public static int getSkinColor(Context context,int colorRes){
        if(context==null)
            return Color.WHITE;
        return SkinCompatResources.getColorStateList(context, colorRes).getDefaultColor();
    }

    public static Drawable getSkinDrawable(Context context,int drawableId){
        if(context==null)
            return null;
        return SkinCompatResources.getDrawable(context,drawableId);
    }
}
