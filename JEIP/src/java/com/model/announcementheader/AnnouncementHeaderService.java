package com.model.announcementheader;

import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 提供存取AnnouncementHeaderDAO的服務
 * 使用servive的好處在於，未來若是DAO層因為DB的廠商不同而有不同實作的時候，controller層可以不用異動
 *
 * @author Vance
 */
@Service
public class AnnouncementHeaderService implements AnnouncementHeaderServiceInterface {

    @Autowired
    private AnnouncementHeaderDao announcementHeaderDAO;

    private final Logger log = Logger.getLogger(this.getClass());

    /**
     *
     * @return
     */
    public AnnouncementHeaderDao getAnnouncementHeaderDao() {
        return announcementHeaderDAO;
    }

    /**
     *
     * @param announcementHeaderDAO
     */
    public void setAnnouncementHeaderDao(AnnouncementHeaderDao announcementHeaderDAO) {
        this.announcementHeaderDAO = announcementHeaderDAO;
    }

    /**
     * 新增
     *
     * @param announcementHeader
     */
    @Override
    public boolean addAnnouncementHeader(AnnouncementHeader announcementHeader) {
        boolean flag = true;
        try {
            getAnnouncementHeaderDao().insert(announcementHeader);
        } catch (Exception ex) {
            flag = false;
            log.error("新增公告[" + announcementHeader.getAnnouncementDesc() + "]時發生錯誤。", ex);
        }
        return flag;
    }

    /**
     * 新增並回傳ID
     *
     * @param announcementHeader
     */
    @Override
    public Integer addAnnAndGetID(AnnouncementHeader announcementHeader) {
        Integer id = -1;
        try {
            id = getAnnouncementHeaderDao().insertAndGetID(announcementHeader);
        } catch (Exception ex) {
            log.error("新增公告[" + announcementHeader.getAnnouncementDesc() + "]時發生錯誤。", ex);
        }
        return id;
    }

    /**
     * 更新
     *
     * @param announcementHeader
     */
    @Override
    public boolean updateAnnouncementHeader(AnnouncementHeader announcementHeader) {
        boolean flag = true;
        try {
            getAnnouncementHeaderDao().update(announcementHeader);
        } catch (Exception ex) {
            flag = false;
            log.error("更新公告[" + announcementHeader.getAnnouncementDesc() + "]時發生錯誤。", ex);
        }
        return flag;
    }

    /**
     * 刪除
     *
     * @param announcementHeader
     */
    @Override
    public boolean deleteAnnouncementHeader(AnnouncementHeader announcementHeader) {
        boolean flag = true;
        try {
            getAnnouncementHeaderDao().remove(announcementHeader);
        } catch (Exception ex) {
            flag = false;
            log.error("刪除公告[" + announcementHeader.getAnnouncementDesc() + "]時發生錯誤。", ex);
        }
        return flag;
    }

    /**
     * 查詢單一公告
     *
     * @param announcementHeader
     * @return
     */
    @Override
    public AnnouncementHeader findByOne(AnnouncementHeader announcementHeader) {
        AnnouncementHeader rtnAnnouncementHeader = null;
        try {
            rtnAnnouncementHeader = getAnnouncementHeaderDao().findByOne(announcementHeader);
        } catch (Exception ex) {
        }
        return rtnAnnouncementHeader;
    }

    /**
     * 查詢全部公告
     *
     * @return
     */
    @Override
    public List<AnnouncementHeader> findAll() {
        List<AnnouncementHeader> list = null;
        try {
            list = getAnnouncementHeaderDao().findAll();
        } catch (Exception ex) {
        }
        return list;
    }

    /**
     * 使用BegTime查詢公告
     *
     * @param begTime
     * @return
     */
    @Override
    public List<AnnouncementHeader> findBegTimeBetween(String begTime) {
        List<AnnouncementHeader> list = null;
        try {
            list = getAnnouncementHeaderDao().findBegTimeBetween(begTime);
        } catch (Exception ex) {
        }
        return list;
    }
}
