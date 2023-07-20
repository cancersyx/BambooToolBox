package com.zsf.toolbox.ui.appmanage;

import android.graphics.drawable.Drawable;

import java.math.BigDecimal;

/**
 * Created by EWorld
 * 2022/6/9
 */
public class AppInfo {
    public int uid;
    public String label;//应用名称
    public String version;
    public String package_name;//应用包名
    public Drawable icon;//应用icon
    public BigDecimal size;

    public AppInfo() {
        uid = 0;
        label = "";
        package_name = "";
        icon = null;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

}
