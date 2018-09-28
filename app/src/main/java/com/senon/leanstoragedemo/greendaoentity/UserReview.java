package com.senon.leanstoragedemo.greendaoentity;


import com.senon.leanstoragedemo.greendao.DaoSession;
import com.senon.leanstoragedemo.greendao.UserDetailsDao;
import com.senon.leanstoragedemo.greendao.UserReviewDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;


/**
 * 学员概述dao实体
 */

@Entity
public class UserReview {

    @Id(autoincrement = true)//必须使用包装类对象类型Long，而非基本类型long
    private Long id;//主键
    @NotNull
    private String name;//姓名
    private int total_count;//总数
    private int last_count;//剩余数
    private int total_money;//总金额
    private int last_money;//剩余金额
    private String signTime;//最近签到时间
    @ToMany(joinProperties = {@JoinProperty(name = "id", referencedName = "id")})
    private List<UserDetails> userDetailsList;//一个学员可以对应多个补课数据


    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1038734393)
    private transient UserReviewDao myDao;
    @Generated(hash = 931053110)
    public UserReview(Long id, @NotNull String name, int total_count, int last_count, int total_money,
            int last_money, String signTime) {
        this.id = id;
        this.name = name;
        this.total_count = total_count;
        this.last_count = last_count;
        this.total_money = total_money;
        this.last_money = last_money;
        this.signTime = signTime;
    }
    @Generated(hash = 1007454340)
    public UserReview() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getTotal_count() {
        return this.total_count;
    }
    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }
    public int getLast_count() {
        return this.last_count;
    }
    public void setLast_count(int last_count) {
        this.last_count = last_count;
    }
    public int getTotal_money() {
        return this.total_money;
    }
    public void setTotal_money(int total_money) {
        this.total_money = total_money;
    }
    public int getLast_money() {
        return this.last_money;
    }
    public void setLast_money(int last_money) {
        this.last_money = last_money;
    }
    public String getSignTime() {
        return this.signTime;
    }
    public void setSignTime(String signTime) {
        this.signTime = signTime;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1442488896)
    public List<UserDetails> getUserDetailsList() {
        if (userDetailsList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserDetailsDao targetDao = daoSession.getUserDetailsDao();
            List<UserDetails> userDetailsListNew = targetDao
                    ._queryUserReview_UserDetailsList(id);
            synchronized (this) {
                if (userDetailsList == null) {
                    userDetailsList = userDetailsListNew;
                }
            }
        }
        return userDetailsList;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 190005747)
    public synchronized void resetUserDetailsList() {
        userDetailsList = null;
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
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1561422220)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserReviewDao() : null;
    }


}
