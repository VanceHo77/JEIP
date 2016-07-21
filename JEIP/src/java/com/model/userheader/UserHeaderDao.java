package com.model.userheader;

import com.model.depheader.DepHeader;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;

/**
 * UserHeader對資料庫的存取操作
 *
 * @author Vance
 */
@Repository("userHeaderDao")
//@Transactional 表示將資料庫的交易交給 spring framework 處理
@Transactional(propagation = Propagation.REQUIRED)
public class UserHeaderDao {

    /*
    在HQL，from UserHeader映射的是com.model.userheader.UserHeader的類別名稱，所以盡量將類別名稱和table名稱做對應，避免日後維護混淆
     */
    //SQL語法: seletct * from UserHeader u , TestTable t where u.userid=t.testid
    //查詢條件：JOIN TestTable 查詢全部
//    private static final String SELECT_QUERY = "from UserHeader u , TestTable t where u.userID=t.testID";
    //放unitName指向的DataBase對應的EntityBean實例集合，以及對這些實例進行生命週期管理
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
     * @param userHeader
     */
    @Transactional
    public void insert(UserHeader userHeader) {
        try {
            entityManager.persist(userHeader);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 更新
     *
     * @param userHeader
     */
    @Transactional
    public void update(UserHeader userHeader) {
        try {
            entityManager.merge(userHeader);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 刪除
     *
     * @param userHeader
     */
    public void remove(UserHeader userHeader) {
        try {
            entityManager.remove(entityManager.merge(userHeader));
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 使用UserHeader查詢單一使用者
     *
     * @param userHeader
     * @return
     */
    public UserHeader findByOne(UserHeader userHeader) {
        String queryStr = "Select u From UserHeader u Left Join u.depHeader d ";
        String subStr = "";
        try {
            if (!userHeader.getAccount().isEmpty()) {
                subStr += " and u.account = :account";
            }
            if (!userHeader.getEmail().isEmpty()) {
                subStr += " and u.email = :email";
            }
            if (!userHeader.getUserName().isEmpty()) {
                subStr += " and u.userName = :userName";
            }
            if (!userHeader.getAuthCode().isEmpty()) {
                subStr += " and u.authCode = :authCode";
            }
            if (userHeader.getDepHeader() != null) {
                subStr += " and u.depHeader = :depHeader";
            }
            if (!subStr.isEmpty()) {
                subStr = subStr.substring(5, subStr.length());
            }
            queryStr += " Where " + subStr;
            Query query = entityManager.createQuery(queryStr);

            if (!userHeader.getAccount().isEmpty()) {
                query.setParameter("account", userHeader.getAccount());
            }
            if (!userHeader.getEmail().isEmpty()) {
                query.setParameter("email", userHeader.getEmail());
            }
            if (!userHeader.getUserName().isEmpty()) {
                query.setParameter("userName", userHeader.getUserName());
            }
            if (!userHeader.getAuthCode().isEmpty()) {
                subStr += "u.authCode = :authCode";
                query.setParameter("authCode", userHeader.getAuthCode());
            }
            if (userHeader.getDepHeader() != null) {
                subStr += "u.depHeader = :depHeader";
                query.setParameter("depHeader", userHeader.getDepHeader());
            }
            userHeader = (UserHeader) query.getSingleResult();
        } catch (Exception e) {
            throw e;
        }
        return userHeader;
    }


    /**
     * 查詢全部
     *
     * @return
     */
    public List<UserHeader> findAll() {
        List<UserHeader> userHeaders = null;
        try {
            Query query = entityManager.createQuery("Select u From UserHeader u Left Join u.depHeader d  Order By u.account DESC");
            userHeaders = query.getResultList();
        } catch (Exception e) {
            throw e;
        }
        return userHeaders;
    }
}
