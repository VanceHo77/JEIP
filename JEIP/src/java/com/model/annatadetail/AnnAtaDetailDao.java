package com.model.annatadetail;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;

/**
 * annAtaDetail對資料庫的存取操作
 *
 * @author Vance
 */
@Repository("annAtaDetailDao")
//@Transactional 表示將資料庫的交易交給 spring framework 處理
@Transactional(propagation = Propagation.REQUIRED)
public class AnnAtaDetailDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     *
     * @return
     */
    public EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     *
     * @param entityManager
     */
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * 新增
     *
     * @param annAtaDetail
     */
    @Transactional
    public void insert(AnnAtaDetail annAtaDetail) {
        try {
            entityManager.persist(annAtaDetail);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 更新
     *
     * @param annAtaDetail
     */
    @Transactional
    public void update(AnnAtaDetail annAtaDetail) {
        try {
            entityManager.merge(annAtaDetail);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 刪除
     *
     * @param annAtaDetail
     */
    public void remove(AnnAtaDetail annAtaDetail) {
        try {
            entityManager.remove(entityManager.merge(annAtaDetail));
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 使用annAtaDetail查詢單一附件
     *
     * @param annAtaDetail
     * @return
     */
    public AnnAtaDetail findByOne(AnnAtaDetail annAtaDetail) {
        String queryStr = "Select t From AnnAtaDetail t";
        String subStr = "";
        try {

////            if (annAtaDetail.getAnnID() > 0) {
////                subStr += " and t.annID = :annID";
////            }
////            if (annAtaDetail.getSeqNO() > 0) {
////                subStr += " and t.seqNO = :seqNO";
////            }
////            if (!annAtaDetail.getAtaType().isEmpty()) {
////                subStr += " and t.ataType = :ataType";
////            }
////            if (annAtaDetail.getAtaFileName() != null) {
////                subStr += " and t.ataFileName = :ataFileName";
////            }
////            if (!subStr.isEmpty()) {
////                subStr = subStr.substring(5, subStr.length());
////            }
////            queryStr += " Where " + subStr;
////            Query query = entityManager.createQuery(queryStr);
////
////            if (annAtaDetail.getAnnID() > 0) {
////                query.setParameter("annID", annAtaDetail.getAnnID());
////            }
////            if (annAtaDetail.getSeqNO() > 0) {
////                query.setParameter("seqNO", annAtaDetail.getSeqNO());
////            }
//            if (!annAtaDetail.getAtaType().isEmpty()) {
//                query.setParameter("ataType", annAtaDetail.getAtaType());
//            }
//            if (!annAtaDetail.getAtaFileName().isEmpty()) {
//                subStr += "t.ataFileName = :ataFileName";
//                query.setParameter("ataFileName", annAtaDetail.getAtaFileName());
//            }
//            annAtaDetail = (AnnAtaDetail) query.getSingleResult();
        } catch (Exception e) {
            throw e;
        }
        return annAtaDetail;
    }
}
