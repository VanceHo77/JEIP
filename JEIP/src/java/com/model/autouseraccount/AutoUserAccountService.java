package com.model.autouseraccount;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 提供存取AutoUserAccountDAO的服務
 * 使用servive的好處在於，未來若是DAO層因為DB的廠商不同而有不同實作的時候，controller層可以不用異動
 * 
 * @author Vance
 */
@Service
public class AutoUserAccountService implements AutoUserAccountServiceInterface {

    @Autowired
    private AutoUserAccountDao AutoUserAccounDao;

    private final Logger log = Logger.getLogger(this.getClass());

    /**
     *
     * @return
     */
    public AutoUserAccountDao getAutoUserAccounDao() {
        return AutoUserAccounDao;
    }

    /**
     *
     * @param AutoUserAccounDao
     */
    public void setAutoUserAccounDao(AutoUserAccountDao AutoUserAccounDao) {
        this.AutoUserAccounDao = AutoUserAccounDao;
    }

    /**
     * 新增
     *
     * @param autoUserAccount
     */
    @Override
    public boolean addAutoUserAccount(AutoUserAccount autoUserAccount) {
        boolean flag = true;
        try {
            getAutoUserAccounDao().insert(autoUserAccount);
        } catch (Exception ex) {
            flag = false;
            log.error("新增系統預設帳號[" + autoUserAccount.getAccount()+ "]時發生錯誤。", ex);
        }
        return flag;
    }

    /**
     * 更新
     *
     * @param autoUserAccount
     * @return 
     */
    @Override
    public boolean updateAutoUserAccount(AutoUserAccount autoUserAccount) {
        boolean flag = true;
        try {
            getAutoUserAccounDao().update(autoUserAccount);
        } catch (Exception ex) {
            flag = false;
            log.error("更新系統預設帳號[" + autoUserAccount.getAccount() + "]時發生錯誤。", ex);
        }
        return flag;
    }

    /**
     * 刪除
     *
     * @param autoUserAccount
     */
    @Override
    public boolean deleteAutoUserAccount(AutoUserAccount autoUserAccount) {
        boolean flag = true;
        try {
            getAutoUserAccounDao().remove(autoUserAccount);
        } catch (Exception ex) {
            flag = false;
            log.error("刪除系統預設帳號[" + autoUserAccount.getAccount() + "]時發生錯誤。", ex);
        }
        return flag;
    }
    
    /**
     * 查詢單一系統預設帳號
     *
     * @return
     */
    @Override
    public AutoUserAccount findByOne() {
        AutoUserAccount rtnAutoUserAccount= null;
        try {
            rtnAutoUserAccount= getAutoUserAccounDao().findByOne();
        } catch (Exception ex) {
        }
        return rtnAutoUserAccount;
    }
    

}
