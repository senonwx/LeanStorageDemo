package com.senon.leanstoragedemo.greendaoentity;

import com.senon.leanstoragedemo.greendao.DaoSession;
import com.senon.leanstoragedemo.greendao.UserDetailsDao;
import com.senon.leanstoragedemo.greendao.UserReviewDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;


/**
 * 学员详情dao实体
 */

@Entity
public class UserDetails {

    @Id(autoincrement = true)//必须使用包装类对象类型Long，而非基本类型long
    private Long id;
    private String time;//时间
    private int money;//金额
    private int count;//次数
    private int flag;//标记是否为  签到:1  充值:2
    private String content;//补课内容
    private String comments;//老师评价
    private int level;//上课评价等级（优良中差等）
    @NotNull
    private String name;//姓名
    @ToOne(joinProperty = "id")//一对一关系,id为UserReview的外键
    private UserReview userReview;//一个补课记录只能对应一个学生


    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1181202576)
    private transient UserDetailsDao myDao;
    @Generated(hash = 996166284)
    public UserDetails(Long id, String time, int money, int count, int flag, String content,
            String comments, int level, @NotNull String name) {
        this.id = id;
        this.time = time;
        this.money = money;
        this.count = count;
        this.flag = flag;
        this.content = content;
        this.comments = comments;
        this.level = level;
        this.name = name;
    }
    @Generated(hash = 64089743)
    public UserDetails() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public int getMoney() {
        return this.money;
    }
    public void setMoney(int money) {
        this.money = money;
    }
    public int getCount() {
        return this.count;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public int getFlag() {
        return this.flag;
    }
    public void setFlag(int flag) {
        this.flag = flag;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Generated(hash = 1151776432)
    private transient Long userReview__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1761948946)
    public UserReview getUserReview() {
        Long __key = this.id;
        if (userReview__resolvedKey == null
                || !userReview__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserReviewDao targetDao = daoSession.getUserReviewDao();
            UserReview userReviewNew = targetDao.load(__key);
            synchronized (this) {
                userReview = userReviewNew;
                userReview__resolvedKey = __key;
            }
        }
        return userReview;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 545890971)
    public void setUserReview(UserReview userReview) {
        synchronized (this) {
            this.userReview = userReview;
            id = userReview == null ? null : userReview.getId();
            userReview__resolvedKey = id;
        }
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
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getComments() {
        return this.comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }
    public int getLevel() {
        return this.level;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1485137714)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserDetailsDao() : null;
    }
}
