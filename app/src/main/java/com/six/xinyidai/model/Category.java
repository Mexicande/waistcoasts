package com.six.xinyidai.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import java.io.Serializable;

/**
 * 分类表model
 * Created by lihuabin on 2016/10/25.
 */
@Entity
public class Category implements Serializable{

    @Id(autoincrement = true)
    private Long categoryId;
    @NotNull
    private String categoryName;
    @NotNull
    private int categoryIconRId;
    @NotNull
    private int categoryColor;
    @NotNull
    private boolean isActivate = true;//是否被激活
    @NotNull
    private int serialNumber;//序号 排序用

    private String remark;//备用

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getSerialNumber() {
        return this.serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public boolean getIsActivate() {
        return this.isActivate;
    }

    public void setIsActivate(boolean isActivate) {
        this.isActivate = isActivate;
    }

    public int getCategoryColor() {
        return this.categoryColor;
    }

    public void setCategoryColor(int categoryColor) {
        this.categoryColor = categoryColor;
    }

    public int getCategoryIconRId() {
        return this.categoryIconRId;
    }

    public void setCategoryIconRId(int categoryIconRId) {
        this.categoryIconRId = categoryIconRId;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @Generated(hash = 998352280)
    public Category(Long categoryId, @NotNull String categoryName,
            int categoryIconRId, int categoryColor, boolean isActivate,
            int serialNumber, String remark) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryIconRId = categoryIconRId;
        this.categoryColor = categoryColor;
        this.isActivate = isActivate;
        this.serialNumber = serialNumber;
        this.remark = remark;
    }

    @Generated(hash = 1150634039)
    public Category() {
    }

}
