package com.model.announcementheader;

import com.model.annatadetail.AnnAtaDetail;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

/**
 * 系統公告
 *
 * @author Vance
 */
@Entity
public class AnnouncementHeader implements Serializable {

    @Id
    @GeneratedValue(generator = "announcementheader_seq")
    @SequenceGenerator(name = "announcementheader_seq", sequenceName = "announcementheader_seq", allocationSize = 1)
    private Integer annID;
    private String ataType = "";
    private String begTime = "";
    private String endTime = "";
    private String announcementDesc = "";
    private Integer modUserID;
    private Timestamp modTime;
    private Timestamp creTime;

    @OneToMany(mappedBy="announcementHeader", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    //原本使用JoinColumn的方式join Detail的資料，但是在級聯刪除時會有annID=null的問題
//    @JoinColumn(name = "annID", referencedColumnName = "annID")
    private List<AnnAtaDetail> annAtaDetails;

    public Integer getAnnID() {
        return annID;
    }

    public void setAnnID(Integer annID) {
        this.annID = annID;
    }

    public String getBegTime() {
        return begTime;
    }

    public void setBegTime(String begTime) {
        this.begTime = begTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAtaType() {
        return ataType;
    }

    public void setAtaType(String ataType) {
        this.ataType = ataType;
    }

    public String getAnnouncementDesc() {
        return announcementDesc;
    }

    public void setAnnouncementDesc(String announcementDesc) {
        this.announcementDesc = announcementDesc;
    }

    public Integer getModUserID() {
        return modUserID;
    }

    public void setModUserID(Integer modUserID) {
        this.modUserID = modUserID;
    }

    public Timestamp getModTime() {
        return modTime;
    }

    public void setModTime(Timestamp modTime) {
        this.modTime = modTime;
    }

    public Timestamp getCreTime() {
        return creTime;
    }

    public void setCreTime(Timestamp creTime) {
        this.creTime = creTime;
    }

    public List<AnnAtaDetail> getAnnAtaDetails() {
        return annAtaDetails;
    }

    public void setAnnAtaDetails(List<AnnAtaDetail> annAtaDetails) {
        this.annAtaDetails = annAtaDetails;
    }

    @Override
    public String toString() {
        return "AnnouncementHeader{" + "annID=" + annID + ", ataType=" + ataType + ", begTime=" + begTime + ", endTime=" + endTime + ", announcementDesc=" + announcementDesc + ", modUserID=" + modUserID + ", modTime=" + modTime + ", creTime=" + creTime + ", annAtaDetails=" + annAtaDetails + '}';
    }

}
