package com.model.autouseraccount;

import javax.persistence.EntityExistsException;
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
 * AutoUserAccount對資料庫的存取操作
 *
 * @author Vance
 */
@Repository("autoUserAccountDao")
@Transactional(propagation = Propagation.REQUIRED)
public class AutoUserAccountDao {

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
     * @param autoUserAccount
     */
    @Transactional
    public void insert(AutoUserAccount autoUserAccount) {
        try {
            entityManager.persist(autoUserAccount);
        } catch (EntityExistsException | TransactionRequiredException e) {
            throw e;
        }
    }

    /**
     * 更新
     *
     * @param autoUserAccount
     */
    @Transactional
    public void update(AutoUserAccount autoUserAccount) {
        try {
            entityManager.merge(autoUserAccount);
        } catch (IllegalArgumentException | TransactionRequiredException e) {
            throw e;
        }
    }

    /**
     * 刪除
     *
     * @param autoUserAccount
     */
    public void remove(AutoUserAccount autoUserAccount) {
        try {
            entityManager.remove(entityManager.merge(autoUserAccount));
        } catch (IllegalArgumentException | TransactionRequiredException e) {
            throw e;
        }
    }

    /**
     * 查詢預設帳號
     *
     * @return
     */
    public AutoUserAccount findByOne() {
        String queryStr = "Select u From AutoUserAccount u";
        AutoUserAccount autoUserAccount;
        try {
            Query query = entityManager.createQuery(queryStr);

            autoUserAccount = (AutoUserAccount) query.getSingleResult();
        } catch (IllegalStateException | QueryTimeoutException | TransactionRequiredException | PessimisticLockException | LockTimeoutException e) {
            throw e;
        }
        return autoUserAccount;
    }
}
