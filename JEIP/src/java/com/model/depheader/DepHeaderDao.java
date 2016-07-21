package com.model.depheader;

import com.model.userheader.*;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;

/**
 * DepHeader對資料庫的存取操作
 *
 * @author Vance
 */
@Repository("depHeaderDao")
@Transactional(propagation = Propagation.REQUIRED)
public class DepHeaderDao {

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
     * @param depHeader
     */
    @Transactional
    public void insert(DepHeader depHeader) {
        try {
            entityManager.persist(depHeader);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 更新
     *
     * @param depHeader
     */
    @Transactional
    public void update(DepHeader depHeader) {
        try {
            entityManager.merge(depHeader);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 刪除
     *
     * @param depHeader
     */
    public void remove(DepHeader depHeader) {
        try {
            entityManager.remove(entityManager.merge(depHeader));
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 使用DepHeader查詢單一單位
     *
     * @param depHeader
     * @return
     */
    public DepHeader findByOne(DepHeader depHeader) {
        String queryStr = "from DepHeader d ";
        String subStr = "";
        try {
            if (!depHeader.getDepID().isEmpty()) {
                subStr += " and d.depID = :depID";
            }
            if (!depHeader.getDepName().isEmpty()) {
                subStr += " and d.depName = :depName";
            }
            if (depHeader.getUnitLevel() != -1) {
                subStr += " and d.unitLevel = :unitLevel";
            }

            if (!subStr.isEmpty()) {
                subStr = subStr.substring(5, subStr.length());
            }
            queryStr += " Where " + subStr;
            Query query = entityManager.createQuery(queryStr);

            if (!depHeader.getDepID().isEmpty()) {
                query.setParameter("depID", depHeader.getDepID());
            }
            if (!depHeader.getDepName().isEmpty()) {
                query.setParameter("depName", depHeader.getDepName());
            }
            if (depHeader.getUnitLevel() != -1) {
                query.setParameter("unitLevel", depHeader.getUnitLevel());
            }
            depHeader = (DepHeader) query.getSingleResult();
        } catch (Exception e) {
            throw e;
        }
        return depHeader;
    }

    /**
     * 查詢全部
     *
     * @return
     */
    public List<DepHeader> findAll() {
        List<DepHeader> depHeaders = null;
        try {
            Query query = entityManager.createQuery("Select d from DepHeader d Order By d.depID");
            depHeaders = query.getResultList();
        } catch (Exception e) {
            throw e;
        }
        return depHeaders;
    }
}
