package com.model.annatadetail;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 提供存取AnnAtaDetailDAO的服務
 * 使用servive的好處在於，未來若是DAO層因為DB的廠商不同而有不同實作的時候，controller層可以不用異動
 *
 * @author Vance
 */
@Service
public class AnnAtaDetailService implements AnnAtaDetailServiceInterface {

    @Autowired
    private AnnAtaDetailDao annAtaDetailDao;

    private final Logger log = Logger.getLogger(this.getClass());

    /**
     *
     * @return
     */
    public AnnAtaDetailDao getAnnAtaDetailDao() {
        return annAtaDetailDao;
    }

    /**
     *
     * @param annAtaDetailDao
     */
    public void setAnnAtaDetailDao(AnnAtaDetailDao annAtaDetailDao) {
        this.annAtaDetailDao = annAtaDetailDao;
    }

    /**
     * 新增
     *
     * @param annAtaDetail
     */
    @Override
    public boolean addAnnAtaDetail(AnnAtaDetail annAtaDetail) {
        boolean flag = true;
        try {
            getAnnAtaDetailDao().insert(annAtaDetail);
        } catch (Exception ex) {
            flag = false;
            log.error("新增附件[" + annAtaDetail.getAtaFileName()+ "]時發生錯誤。", ex);
        }
        return flag;
    }

    /**
     * 更新
     *
     * @param annAtaDetail
     */
    @Override
    public boolean updateAnnAtaDetail(AnnAtaDetail annAtaDetail) {
        boolean flag = true;
        try {
            getAnnAtaDetailDao().update(annAtaDetail);
        } catch (Exception ex) {
            flag = false;
            log.error("更新附件[" + annAtaDetail.getAtaFileName()+ "]時發生錯誤。", ex);
        }
        return flag;
    }

    /**
     * 刪除
     *
     * @param annAtaDetail
     */
    @Override
    public boolean deleteAnnAtaDetail(AnnAtaDetail annAtaDetail) {
        boolean flag = true;
        try {
            getAnnAtaDetailDao().remove(annAtaDetail);
        } catch (Exception ex) {
            flag = false;
            log.error("刪除附件[" + annAtaDetail.getAtaFileName() + "]時發生錯誤。", ex);
        }
        return flag;
    }

    /**
     * 查詢單一附件
     *
     * @param annAtaDetail
     * @return
     */
    @Override
    public AnnAtaDetail findByOne(AnnAtaDetail annAtaDetail) {
        AnnAtaDetail rtnAnnAtaDetail = null;
        try {
            rtnAnnAtaDetail = getAnnAtaDetailDao().findByOne(annAtaDetail);
        } catch (Exception ex) {
        }
        return rtnAnnAtaDetail;
    }
}
