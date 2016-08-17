package com.model.annatadetail;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.LockTimeoutException;
import javax.persistence.PersistenceContext;
import javax.persistence.PessimisticLockException;
import javax.persistence.Query;
import javax.persistence.QueryTimeoutException;
import javax.persistence.TransactionRequiredException;
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
     * 使用AnnAtaDetail查詢
     *
     * @param annAtaDetail
     * @return
     */
    public List<AnnAtaDetail> findAnnAtaDetails(AnnAtaDetail annAtaDetail) {
        String queryStr = "Select t From AnnAtaDetail t ";
        String subStr = "";
        List<AnnAtaDetail> list = new ArrayList<>();
        try {
            if (annAtaDetail.getAnnID() > 0) {
                subStr += " and t.annID = :annID";
            }
            if (annAtaDetail.getSeqNO() > 0) {
                subStr += " and t.seqNO = :seqNO";
            }
            if (!annAtaDetail.getAtaFileName().isEmpty()) {
                subStr += " and t.ataFileName = :ataFileName";
            }
            if (!subStr.isEmpty()) {
                subStr = subStr.substring(5, subStr.length());
            }
            queryStr += " Where " + subStr + " Order By seqNO";
            Query query = entityManager.createQuery(queryStr);
            if (annAtaDetail.getAnnID() > 0) {
                query.setParameter("annID", annAtaDetail.getAnnID());
            }
            if (annAtaDetail.getSeqNO() > 0) {
                query.setParameter("seqNO", annAtaDetail.getSeqNO());
            }
            if (!annAtaDetail.getAtaFileName().isEmpty()) {
                query.setParameter("ataFileName", annAtaDetail.getAtaFileName());
            }
            list = query.getResultList();
        } catch (IllegalStateException | QueryTimeoutException | TransactionRequiredException | PessimisticLockException | LockTimeoutException e) {
            throw e;
        }
        return list;
    }
}
