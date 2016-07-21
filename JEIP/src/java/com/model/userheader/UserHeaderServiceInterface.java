/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.model.userheader;

import java.util.List;

/**
 * UserHeaderService提供的標準服務介面定義
 * @author Vance
 */
public interface UserHeaderServiceInterface {

    /**
     * 新增使用者
     * @param userHeader
     * @return 
     */
    public boolean addUserHeader(UserHeader userHeader);

    /**
     * 更新使用者
     * @param userHeader
     * @return 
     */
    public boolean updateUserHeader(UserHeader userHeader);

    /**
     * 刪除使用者
     * @param userHeader
     * @return 
     */
    public boolean deleteUserHeader(UserHeader userHeader);
    
    /**
     * 使用UserHeader物件查詢單一使用者
     * @param userHeader
     * @return
     */
    public UserHeader findByOne(UserHeader userHeader);
    
    
    /**
     * 查詢全部的使用者
     * @return
     */
    public List<UserHeader> findAll();
    
}
