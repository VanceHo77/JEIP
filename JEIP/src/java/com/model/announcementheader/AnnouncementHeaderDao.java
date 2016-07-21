package com.model.announcementheader;

import com.model.annatadetail.AnnAtaCompositeID;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;

/**
 * AnnouncementHeader對資料庫的存取操作
 *
 * @author Vance
 */
@Repository("announcementHeaderDao")
@Transactional(propagation = Propagation.REQUIRED)
public class AnnouncementHeaderDao {

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
     * @param anouncementHeader
     */
    @Transactional
    public void insert(AnnouncementHeader anouncementHeader) {
        try {
            entityManager.persist(anouncementHeader);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 新增並回傳ID
     *
     * @param anouncementHeader
     */
    @Transactional
    public Integer insertAndGetID(AnnouncementHeader anouncementHeader) {
        try {
            entityManager.persist(anouncementHeader);
            entityManager.flush();
        } catch (Exception e) {
            throw e;
        }
        return anouncementHeader.getAnnID();
    }

    /**
     * 更新
     *
     * @param anouncementHeader
     */
    @Transactional
    public void update(AnnouncementHeader anouncementHeader) {
        try {
            entityManager.merge(anouncementHeader);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 刪除
     *
     * @param anouncementHeader
     */
    public void remove(AnnouncementHeader anouncementHeader) {
        try {
            entityManager.remove(entityManager.merge(anouncementHeader));
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 使用AnnouncementHeader查詢單一公告
     *
     * @param anouncementHeader
     * @return
     */
    public AnnouncementHeader findByOne(AnnouncementHeader anouncementHeader) {
        String queryStr = "Select a From AnnouncementHeader a";
        String subStr = "";
        try {
            if (anouncementHeader.getAnnID() != -1) {
                subStr += " and a.annID = :annID";
            }
            if (!anouncementHeader.getBegTime().isEmpty()) {
                subStr += " and a.begTime = :begTime";
            }
            if (!anouncementHeader.getEndTime().isEmpty()) {
                subStr += " and a.endTime = :endTime";
            }
            if (!anouncementHeader.getAnnouncementDesc().isEmpty()) {
                subStr += " and a.announcementDesc = :announcementDesc";
            }
            if (!subStr.isEmpty()) {
                subStr = subStr.substring(5, subStr.length());
            }
            queryStr += " Where " + subStr;
            Query query = entityManager.createQuery(queryStr);

            if (anouncementHeader.getAnnID() != -1) {
                query.setParameter("annID", anouncementHeader.getAnnID());
            }
            if (!anouncementHeader.getBegTime().isEmpty()) {
                query.setParameter("begTime", anouncementHeader.getBegTime());
            }
            if (!anouncementHeader.getEndTime().isEmpty()) {
                query.setParameter("endTime", anouncementHeader.getEndTime());
            }
            if (!anouncementHeader.getAnnouncementDesc().isEmpty()) {
                query.setParameter("announcementDesc", anouncementHeader.getAnnouncementDesc());
            }
            anouncementHeader = (AnnouncementHeader) query.getSingleResult();
        } catch (Exception e) {
            throw e;
        }
        return anouncementHeader;
    }

    /**
     * 查詢全部
     *
     * @return
     */
    public List<AnnouncementHeader> findAll() {
        List<AnnouncementHeader> anouncementHeaders = null;
        try {
            Query query = entityManager.createQuery("Select a From AnnouncementHeader a Order By a.begTime DESC");
            anouncementHeaders = query.getResultList();
        } catch (Exception e) {
            throw e;
        }
        return anouncementHeaders;
    }

    /**
     * 查詢Top3
     *
     * @return
     */
    public List<AnnouncementHeader> findBegTimeBetween(String begTime) {
        List<AnnouncementHeader> anouncementHeaders = null;
        //因AnnouncementHeader有設定@OneToMany，所以不需要再join AnnAtaDetail的資料
        //，只需要下列查詢即可帶出detail資料
        String queryStr = "Select a From AnnouncementHeader a";
        String subStr = "";
        try {
            if (!begTime.isEmpty()) {
                subStr += " Where :begTime Between a.begTime And a.endTime";
            }
            queryStr += subStr;
            queryStr += " Order By SUBSTRING(a.begTime,0,10)  DESC, SUBSTRING(a.begTime,11,15) ASC";
            Query query = entityManager.createQuery(queryStr);
            if (!begTime.isEmpty()) {
                query.setParameter("begTime", begTime);
            }
            anouncementHeaders = query.getResultList();
        } catch (Exception e) {
            throw e;
        }
        return anouncementHeaders;
    }
}
