package com.model.depheader;

import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 提供存取DepHeaderDAO的服務
 * 使用servive的好處在於，未來若是DAO層因為DB的廠商不同而有不同實作的時候，controller層可以不用異動
 * 
 * @author Vance
 */
@Service
public class DepHeaderService implements DepHeaderServiceInterface {

    @Autowired
    private DepHeaderDao depHeaderDao;

    private final Logger log = Logger.getLogger(this.getClass());

    /**
     *
     * @return
     */
    public DepHeaderDao getDepHeaderDAO() {
        return depHeaderDao;
    }

    /**
     *
     * @param depHeaderDao
     */
    public void setDepHeaderDAO(DepHeaderDao depHeaderDao) {
        this.depHeaderDao = depHeaderDao;
    }

    /**
     * 新增
     *
     * @param depHeader
     */
    @Override
    public boolean addDepHeader(DepHeader depHeader) {
        boolean flag = true;
        try {
            getDepHeaderDAO().insert(depHeader);
        } catch (Exception ex) {
            flag = false;
            log.error("新增單位[" + depHeader.getDepName() + "]時發生錯誤。", ex);
        }
        return flag;
    }

    /**
     * 更新
     *
     * @param depHeader
     */
    @Override
    public boolean updateDepHeader(DepHeader depHeader) {
        boolean flag = true;
        try {
            getDepHeaderDAO().update(depHeader);
        } catch (Exception ex) {
            flag = false;
            log.error("更新單位[" + depHeader.getDepName() + "]時發生錯誤。", ex);
        }
        return flag;
    }

    /**
     * 刪除
     *
     * @param depHeader
     */
    @Override
    public boolean deleteDepHeader(DepHeader depHeader) {
        boolean flag = true;
        try {
            getDepHeaderDAO().remove(depHeader);
        } catch (Exception ex) {
            flag = false;
            log.error("刪除單位[" + depHeader.getDepName() + "]時發生錯誤。", ex);
        }
        return flag;
    }
    
    /**
     * 查詢單一單位
     *
     * @param depHeader
     * @return
     */
    @Override
    public DepHeader findByOne(DepHeader depHeader) {
        DepHeader rtnDepHeader= null;
        try {
            rtnDepHeader = getDepHeaderDAO().findByOne(depHeader);
        } catch (Exception ex) {
        }
        return rtnDepHeader;
    }
    
    /**
     * 查詢全部單位
     *
     * @return
     */
    @Override
    public List<DepHeader> findAll() {
        List<DepHeader> list = null;
        try {
            list = getDepHeaderDAO().findAll();
        } catch (Exception ex) {
        }
        return list;
    }
}
