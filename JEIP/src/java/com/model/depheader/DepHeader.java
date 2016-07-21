package com.model.depheader;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author Vance
 */
@Entity
public class DepHeader implements Serializable {

    @Id
    private String depID = "";
    private String depName = "";
    private int unitLevel = -1;
    private Timestamp modTime;
    private Integer modUserID;
    private Timestamp creTime;

    public String getDepID() {
        return depID;
    }

    public void setDepID(String depID) {
        this.depID = depID;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public int getUnitLevel() {
        return unitLevel;
    }

    public void setUnitLevel(int unitLevel) {
        this.unitLevel = unitLevel;
    }

    public Timestamp getModTime() {
        return modTime;
    }

    public void setModTime(Timestamp modtime) {
        this.modTime = modtime;
    }

    public Timestamp getCreTime() {
        return creTime;
    }

    public void setCreTime(Timestamp cretime) {
        this.creTime = cretime;
    }

    public Integer getModUserID() {
        return modUserID;
    }

    public void setModUserID(Integer modUserID) {
        this.modUserID = modUserID;
    }

    @Override
    public String toString() {
        return "DepHeader{" + "depID=" + depID + ", depName=" + depName + ", unitLevel=" + unitLevel + ", modTime=" + modTime + ", modUserID=" + modUserID + ", creTime=" + creTime + '}';
    }

}
