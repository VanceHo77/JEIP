/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.model.autouseraccount;

/**
 * AutoUserAccountService提供的標準服務介面定義
 * @author Vance
 */
public interface AutoUserAccountServiceInterface {

    /**
     * 新增系統預設帳號
     * @param autoUserAccount
     * @return 
     */
    public boolean addAutoUserAccount(AutoUserAccount autoUserAccount);

    /**
     * 更新系統預設帳號
     * @param autoUserAccount
     * @return 
     */
    public boolean updateAutoUserAccount(AutoUserAccount autoUserAccount);

    /**
     * 刪除系統預設帳號
     * @param autoUserAccount
     * @return 
     */
    public boolean deleteAutoUserAccount(AutoUserAccount autoUserAccount);
    
    /**
     * 查詢單一系統預設帳號
     * @return
     */
    public AutoUserAccount findByOne();
    
    
}
