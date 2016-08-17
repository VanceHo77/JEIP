package com.model.annatadetail;

import com.model.announcementheader.AnnouncementHeader;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author Vance
 */
@Entity
@IdClass(AnnAtaCompositeID.class)//複合主鍵類別
public class AnnAtaDetail implements Serializable {

    @Id
    private Integer annID = -1;
    @Id
    private Integer seqNO = -1;
    private String ataFileName = "";
    private Integer modUserID;
    private Timestamp modtime;
    private Timestamp cretime;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "annID", insertable = false, updatable = false)
    private AnnouncementHeader announcementHeader;

    public Integer getAnnID() {
        return annID;
    }

    public void setAnnID(Integer annID) {
        this.annID = annID;
    }

    public Integer getSeqNO() {
        return seqNO;
    }

    public void setSeqNO(Integer seqNO) {
        this.seqNO = seqNO;
    }

    public String getAtaFileName() {
        return ataFileName;
    }

    public void setAtaFileName(String ataFileName) {
        this.ataFileName = ataFileName;
    }

    public Integer getModUserID() {
        return modUserID;
    }

    public void setModUserID(Integer modUserID) {
        this.modUserID = modUserID;
    }

    public Timestamp getModtime() {
        return modtime;
    }

    public void setModtime(Timestamp modtime) {
        this.modtime = modtime;
    }

    public Timestamp getCretime() {
        return cretime;
    }

    public void setCretime(Timestamp cretime) {
        this.cretime = cretime;
    }

    public AnnouncementHeader getAnnouncementHeader() {
        return announcementHeader;
    }

    public void setAnnouncementHeader(AnnouncementHeader announcementHeader) {
        this.announcementHeader = announcementHeader;
    }

    @Override
    public String toString() {
        return "AnnAtaDetail{" + "annID=" + annID + ", seqNO=" + seqNO + ", ataFileName=" + ataFileName + ", modUserID=" + modUserID + ", modtime=" + modtime + ", cretime=" + cretime + ", announcementHeader=" + announcementHeader + '}';
    }

}
