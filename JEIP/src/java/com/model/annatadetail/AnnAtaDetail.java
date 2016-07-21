package com.model.annatadetail;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

/**
 *
 * @author Vance
 */
@Entity
@IdClass(AnnAtaCompositeID.class)//複合主鍵類別
public class AnnAtaDetail implements Serializable {

    @Id
    private Integer annID;
    @Id
    private Integer seqNO;
    private String ataType = "";
    private String ataFileName = "";
    private Integer modUserID;
    private Timestamp modtime;
    private Timestamp cretime;

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

    public String getAtaType() {
        return ataType;
    }

    public void setAtaType(String ataType) {
        this.ataType = ataType;
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

    @Override
    public String toString() {
        return "AnnAtaDetail{" + "annID=" + annID + ", seqNO=" + seqNO + ", ataType=" + ataType + ", attFileName=" + ataFileName + ", modUserID=" + modUserID + ", modtime=" + modtime + ", cretime=" + cretime + '}';
    }

}
