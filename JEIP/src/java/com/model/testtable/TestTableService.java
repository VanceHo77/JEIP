package com.model.testtable;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Vance
 */
@Service
public class TestTableService {

    @Autowired
    private TestTableDao testTableDao;

    /**
     *
     * @return
     */
    public TestTableDao getUserHeaderDao() {
        return testTableDao;
    }

    /**
     * 
     * @param userHeaderDao
     */
    @Autowired
    public void setUserHeaderDao(TestTableDao userHeaderDao) {
        this.testTableDao = userHeaderDao;
    }

    /**
     * 新增
     * @param userHeader
     */
    public void addUserHeader(TestTable userHeader) {
        getUserHeaderDao().insert(userHeader);
    }

    /**
     * 更新
     * @param userHeader
     */
    public void updateUserHeader(TestTable userHeader) {
        getUserHeaderDao().update(userHeader);
    }

    /**
     * 刪除
     * @param userHeader
     */
    public void deleteUserHeader(TestTable userHeader) {
        getUserHeaderDao().remove(userHeader);
    }

    /**
     * 查詢單筆資料
     * @param indexKey
     * @return
     */
    public TestTable findByKeyUserHeader(int indexKey) {
        return getUserHeaderDao().findByKey(indexKey);
    }

    /**
     * 查詢全部
     * @return
     */
    public List<TestTable> findAllUserHeaders() {
        return getUserHeaderDao().findAll();
    }
}
