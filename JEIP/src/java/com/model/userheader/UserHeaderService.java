package com.model.userheader;

import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 提供存取UserHeaderDAO的服務
 * 使用servive的好處在於，未來若是DAO層因為DB的廠商不同而有不同實作的時候，controller層可以不用異動
 * 
 * @author Vance
 */
@Service
public class UserHeaderService implements UserHeaderServiceInterface {

    @Autowired
    private UserHeaderDao userHeaderDao;

    private final Logger log = Logger.getLogger(this.getClass());

    /**
     *
     * @return
     */
    public UserHeaderDao getUserHeaderDao() {
        return userHeaderDao;
    }

    /**
     *
     * @param userHeaderDao
     */
    public void setUserHeaderDao(UserHeaderDao userHeaderDao) {
        this.userHeaderDao = userHeaderDao;
    }

    /**
     * 新增
     *
     * @param userHeader
     */
    @Override
    public boolean addUserHeader(UserHeader userHeader) {
        boolean flag = true;
        try {
            getUserHeaderDao().insert(userHeader);
        } catch (Exception ex) {
            flag = false;
            log.error("新增使用者[" + userHeader.getUserName() + "]時發生錯誤。", ex);
        }
        return flag;
    }

    /**
     * 更新
     *
     * @param userHeader
     */
    @Override
    public boolean updateUserHeader(UserHeader userHeader) {
        boolean flag = true;
        try {
            getUserHeaderDao().update(userHeader);
        } catch (Exception ex) {
            flag = false;
            log.error("更新使用者[" + userHeader.getUserName() + "]時發生錯誤。", ex);
        }
        return flag;
    }

    /**
     * 刪除
     *
     * @param userHeader
     */
    @Override
    public boolean deleteUserHeader(UserHeader userHeader) {
        boolean flag = true;
        try {
            getUserHeaderDao().remove(userHeader);
        } catch (Exception ex) {
            flag = false;
            log.error("刪除使用者[" + userHeader.getUserName() + "]時發生錯誤。", ex);
        }
        return flag;
    }
    
    /**
     * 查詢單一使用者
     *
     * @param userHeader
     * @return
     */
    @Override
    public UserHeader findByOne(UserHeader userHeader) {
        UserHeader rtnUserHeader= null;
        try {
            rtnUserHeader = getUserHeaderDao().findByOne(userHeader);
        } catch (Exception ex) {
        }
        return rtnUserHeader;
    }
    
    /**
     * 查詢全部使用者
     *
     * @return
     */
    @Override
    public List<UserHeader> findAll() {
        List<UserHeader> list = null;
        try {
            list = getUserHeaderDao().findAll();
        } catch (Exception ex) {
        }
        return list;
    }
}
