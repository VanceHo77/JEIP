package com.model.testtable;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import javax.persistence.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;

/**
 *
 * @author Vance
 */
@Repository("TestTableDao")
@Transactional(propagation = Propagation.REQUIRED)
public class TestTableDao {

    /*在HQL，from UserHeader映射的是com.model.userheader.UserHeader的類別名稱，所以盡量將類別名稱和table名稱做對應，避免日後維護混淆*/
    private static final String SELECT_QUERY = "from TestTable";

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
    public void insert(TestTable userHeader) {
        entityManager.persist(userHeader);
    }

    /**
     * 更新
     *
     * @param userHeader
     */
    @Transactional
    public void update(TestTable userHeader) {
        entityManager.merge(userHeader);
    }

    /**
     * 刪除
     *
     * @param userHeader
     */
    public void remove(TestTable userHeader) {
        entityManager.remove(entityManager.merge(userHeader));
    }

    /**
     * 查詢單筆資料
     *
     * @param indexKey
     * @return
     */
    public TestTable findByKey(int indexKey) {
        TestTable userHeader = entityManager.find(TestTable.class, indexKey);
        return userHeader;
    }

    /**
     * 查詢全部
     *
     * @return
     */
    public List<TestTable> findAll() {
        Query query = entityManager.createQuery(SELECT_QUERY);
        List<TestTable> userHeaders = (List<TestTable>) query.getResultList();
        return userHeaders;
    }

}
