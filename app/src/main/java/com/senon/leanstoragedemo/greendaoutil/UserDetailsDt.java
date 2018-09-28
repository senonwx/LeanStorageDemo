package com.senon.leanstoragedemo.greendaoutil;


import com.senon.leanstoragedemo.greendao.UserDetailsDao;
import com.senon.leanstoragedemo.greendaoentity.UserDetails;

import java.util.ArrayList;
import java.util.List;


public class UserDetailsDt {
    public UserDetailsDt() {
    }

    private UserDetails findByName(String name) {
        List<UserDetails> list_record = SqlEnu.Local.cn().getUserDetailsDao().queryBuilder()
                .where(UserDetailsDao.Properties.Name.eq(name)).list();
        return list_record.size() > 0 ? list_record.get(0) : null;
    }

    public UserDetails findByTime(String time) {
        List<UserDetails> list_record = SqlEnu.Local.cn().getUserDetailsDao().queryBuilder()
                .where(UserDetailsDao.Properties.Time.eq(time)).list();
        return list_record.size() > 0 ? list_record.get(0) : null;
    }

    public UserDetails findByName$Time(String name,String time) {
        List<UserDetails> list_record = SqlEnu.Local.cn().getUserDetailsDao().queryBuilder()
                .where(UserDetailsDao.Properties.Name.eq(name))
                .where(UserDetailsDao.Properties.Time.eq(time))
                .list();
        return list_record.size() > 0 ? list_record.get(0) : null;
    }

    public UserDetails findByName$Time$Flag(String name,String time,int flag) {
        List<UserDetails> list_record = SqlEnu.Local.cn().getUserDetailsDao().queryBuilder()
                .where(UserDetailsDao.Properties.Name.eq(name))
                .where(UserDetailsDao.Properties.Time.eq(time))
                .where(UserDetailsDao.Properties.Flag.eq(flag))
                .list();
        return list_record.size() > 0 ? list_record.get(0) : null;
    }

    private UserDetails findById(Long id) {
        List<UserDetails> list_record = SqlEnu.Local.cn().getUserDetailsDao().queryBuilder()
                .where(UserDetailsDao.Properties.Id.eq(id)).list();
        return list_record.size() > 0 ? list_record.get(0) : null;
    }

    private boolean equals(UserDetails bean) {
        for (int i = 0; i < getAll().size(); i++) {
            if (getAll().get(i).equals(bean)) {
                return true;
            }
        }
        return false;
    }

    public List<UserDetails> getAll() {
        return SqlEnu.Local.cn().getUserDetailsDao().queryBuilder().list();
    }

    public List<UserDetails> getAllByName(String name) {
        List<UserDetails> list_record = SqlEnu.Local.cn().getUserDetailsDao().queryBuilder()
                .where(UserDetailsDao.Properties.Name.eq(name)).list();
        List<UserDetails> list = new ArrayList<>();
        for (int i = 0; i < list_record.size(); i++) {
            list.add(list_record.get(list_record.size() - i - 1));
        }
        return list;
    }

    public void insert(UserDetails bean) {
        if (!equals(bean)) {
            SqlEnu.Local.cn().getUserDetailsDao().insert(bean);
        }
    }

    public void insertByName(UserDetails bean) {
        if (findByName(bean.getName()) == null) {
            SqlEnu.Local.cn().getUserDetailsDao().insert(bean);
        }
    }

    public void insertByTime(UserDetails bean) {
        if (findByTime(bean.getTime()) == null) {
            SqlEnu.Local.cn().getUserDetailsDao().insert(bean);
        }
    }

    public void update(UserDetails bean) {
        SqlEnu.Local.cn().getUserDetailsDao().update(bean);
    }

    public void delete(UserDetails bean) {
        if (findByName(bean.getName()) != null) {
            SqlEnu.Local.cn().getUserDetailsDao().delete(bean);
        }
    }

    public void delete(String name) {
        List<UserDetails> list = SqlEnu.Local.cn().getUserDetailsDao().queryBuilder()
                .where(UserDetailsDao.Properties.Name.eq(name)).list();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                SqlEnu.Local.cn().getUserDetailsDao().delete(list.get(i));
            }
        }
    }

    public void deleteAll() {
        SqlEnu.Local.cn().getUserDetailsDao().deleteAll();
    }

}
