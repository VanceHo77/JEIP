/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.model.announcementheader;

import java.util.List;

/**
 * AnnouncementHeaderService提供的標準服務介面定義
 * @author Vance
 */
public interface AnnouncementHeaderServiceInterface {

    /**
     * 新增公告
     * @param announcementHeader
     * @return 
     */
    public boolean addAnnouncementHeader(AnnouncementHeader announcementHeader);
    
    /**
     * 新增公告並回傳ID
     * @param announcementHeader
     * @return 
     */
    public Integer addAnnAndGetID(AnnouncementHeader announcementHeader);

    /**
     * 更新公告
     * @param announcementHeader
     * @return 
     */
    public boolean updateAnnouncementHeader(AnnouncementHeader announcementHeader);

    /**
     * 刪除公告
     * @param announcementHeader
     * @return 
     */
    public boolean deleteAnnouncementHeader(AnnouncementHeader announcementHeader);
    
    /**
     * 使用AnnouncementHeader物件查詢單一公告
     * @param announcementHeader
     * @return
     */
    public AnnouncementHeader findByOne(AnnouncementHeader announcementHeader);
    
    
    /**
     * 查詢全部的公告
     * @return
     */
    public List<AnnouncementHeader> findAll();
    
    /**
     * 查詢Top3
     * @return
     */
    public List<AnnouncementHeader> findBegTimeBetween(String begTime);
}
