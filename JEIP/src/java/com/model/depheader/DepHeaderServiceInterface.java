/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.model.depheader;

import java.util.List;

/**
 * DepHeaderInterface提供的標準服務介面定義
 * @author Vance
 */
public interface DepHeaderServiceInterface {

    /**
     * 新增單位
     * @param depHeader
     * @return 
     */
    public boolean addDepHeader(DepHeader depHeader);

    /**
     * 更新單位
     * @param depHeader
     * @return 
     */
    public boolean updateDepHeader(DepHeader depHeader);

    /**
     * 刪除單位
     * @param depHeader
     * @return 
     */
    public boolean deleteDepHeader(DepHeader depHeader);
    
    /**
     * 使用DepHeader物件查詢單一單位
     * @param depHeader
     * @return
     */
    public DepHeader findByOne(DepHeader depHeader);
    
    /**
     * 查詢全部的單位
     * @return
     */
    public List<DepHeader> findAll();
    
}
