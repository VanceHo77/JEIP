package com.model.autouseraccount;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 * 系統預設帳號
 *
 * @author Vance
 */
@Entity
public class AutoUserAccount implements Serializable {

    @Id
    @GeneratedValue(generator = "userHeader_seq")
    @SequenceGenerator(name = "userHeader_seq", sequenceName = "userHeader_seq", allocationSize = 1)
    private Integer userID;
    private String account;
    private Integer modUserID;
    private Timestamp modTime;
    private Timestamp creTime;

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getModUserID() {
        return modUserID;
    }

    public void setModUserID(Integer modUserID) {
        this.modUserID = modUserID;
    }

    public Timestamp getModtime() {
        return modTime;
    }

    public void setModtime(Timestamp modtime) {
        this.modTime = modtime;
    }

    public Timestamp getCretime() {
        return creTime;
    }

    public void setCretime(Timestamp cretime) {
        this.creTime = cretime;
    }

    @Override
    public String toString() {
        return "AutoUserAccount{" + "userID=" + userID + ", account=" + account + ", modUserID=" + modUserID + ", modTime=" + modTime + ", creTime=" + creTime + '}';
    }

}
