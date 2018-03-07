package com.wuxiao.yourday.model;

/**
 * 统计列表对象
 * Created by lihuabin on 2016/11/18.
 */
public class StatementListItem {
    String categoryName;
    int categoryColor;
    double moneyOfCategory;//金额
    float percentage;//百分比 小数形式

    public StatementListItem() {
    }

    public StatementListItem( String categoryName,int categoryColor, double moneyOfCategory, float percentage) {
        this.categoryColor = categoryColor;
        this.categoryName = categoryName;
        this.moneyOfCategory = moneyOfCategory;
        this.percentage = percentage;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public double getMoneyOfCategory() {
        return moneyOfCategory;
    }

    public void setMoneyOfCategory(double moneyOfCategory) {
        this.moneyOfCategory = moneyOfCategory;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public int getCategoryColor() {
        return categoryColor;
    }

    public void setCategoryColor(int categoryColor) {
        this.categoryColor = categoryColor;
    }
}
