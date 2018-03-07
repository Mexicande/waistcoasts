package com.wuxiao.yourday.model;


import com.wuxiao.yourday.util.greenDAO.CategoryDao;
import com.wuxiao.yourday.util.greenDAO.DaoSession;
import com.wuxiao.yourday.util.greenDAO.RecordDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;

import java.io.Serializable;

/**
 * 账本记录表model
 * Created by lihuabin on 2016/10/25.
 */
@Entity
public class Record implements Serializable {
    public static final int OUT = 1;
    public static final int IN = 0;

    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private Double money;//金额
    @NotNull
    private String createDate;//创建日期
    @NotNull
    private String createTime;//创建时间
    @NotNull
    private int outOrIn;//收入或支出 0：收入  1：支出
    @NotNull
    private Long categoryId;//分类id
    @ToOne(joinProperty = "categoryId")
    private Category category;
    private String remark;//备注

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1173781595)
    public void setCategory(@NotNull Category category) {
        if (category == null) {
            throw new DaoException(
                    "To-one property 'categoryId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.category = category;
            categoryId = category.getCategoryId();
            category__resolvedKey = categoryId;
        }
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 234631651)
    public Category getCategory() {
        long __key = this.categoryId;
        if (category__resolvedKey == null || !category__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CategoryDao targetDao = daoSession.getCategoryDao();
            Category categoryNew = targetDao.load(__key);
            synchronized (this) {
                category = categoryNew;
                category__resolvedKey = __key;
            }
        }
        return category;
    }

    @Generated(hash = 1372501278)
    private transient Long category__resolvedKey;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 765166123)
    private transient RecordDao myDao;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public int getOutOrIn() {
        return this.outOrIn;
    }

    public void setOutOrIn(int outOrIn) {
        this.outOrIn = outOrIn;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Double getMoney() {
        return this.money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Generated(hash = 215299880)
    public Record(Long id, @NotNull Double money, @NotNull String createDate,
                  @NotNull String createTime, int outOrIn, @NotNull Long categoryId,
                  String remark) {
        this.id = id;
        this.money = money;
        this.createDate = createDate;
        this.createTime = createTime;
        this.outOrIn = outOrIn;
        this.categoryId = categoryId;
        this.remark = remark;
    }

    @Generated(hash = 477726293)
    public Record() {
    }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", money=" + money +
                ", createDate='" + createDate + '\'' +
                ", createTime='" + createTime + '\'' +
                ", outOrIn=" + outOrIn +
                ", remark='" + remark + '\'' +
                ", categoryId=" + categoryId +
                ",category=" + category.toString() +
                '}';
    }

    public Category getCategorySimple() {
        return category;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1505145191)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRecordDao() : null;
    }
}
