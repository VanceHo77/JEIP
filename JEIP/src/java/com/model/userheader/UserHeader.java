package com.model.userheader;

import com.model.depheader.DepHeader;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

/**
 *
 * @author Vance
 */
@Entity
public class UserHeader implements Serializable {

    @Id
    @GeneratedValue(generator = "userHeader_seq")
    @SequenceGenerator(name = "userHeader_seq", sequenceName = "userHeader_seq", allocationSize = 1)
    private Integer userID;
    private String userName = "";
    private String account = "";
    private String password = "";
    private String email = "";
    private String userLevel = "";
    private String openAuth = "";
    private String authCode = "";
    private byte[] photo;
    private Integer modUserID;
    private Timestamp modtime;
    private Timestamp cretime;


    /*
    一、fetch = FetchType.LAZY => 延遲載入資料，也就是說被join的table資料不會被馬上讀取，
    但要注意會有 LazyInitializationException 大魔王級的問題，原因：Hibernate Session會在transaction結束後關閉，而此時若要再載入被延遲載入的資料時，就會發生例外錯誤
    解決方案：
    採用 OpenSessionInView ，透過Spring提供的Filter在讓 Hibernate 的 Session 物件，在 View 可能還需要用到它的時候，是活著的。
    等到 View 做完所有的事情之後，才去關閉 Hibernate 的 Session 物件。
    缺點：
    會將事務橫跨request 到 response 的整個HTTP 生命週期，長時間占用資料庫連接，可能在用時很方便，但是這一種非常消耗系統資源的解決方案。
    
    二、fetch = FetchType.EAGER => 即時載入資料，可以很簡單的解決上述例外問題，但是有效能問題
     */
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinTable(name = "TestTable",
//            joinColumns = @JoinColumn(name = "userID"),
//            inverseJoinColumns = @JoinColumn(name = "testID"))
//    private List<TestTable> testTables;
//    public List<TestTable> getTestTable() {
//        return testTables;
//    }
//
//    public void setTestTable(List<TestTable> testTables) {
//        this.testTables = testTables;
//    }
    @OneToOne()
    @JoinColumn(name = "depID", referencedColumnName = "depID")
    private DepHeader depHeader;

    public DepHeader getDepHeader() {
        return depHeader;
    }

    public void setDepHeader(DepHeader depHeader) {
        this.depHeader = depHeader;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public Timestamp getModTime() {
        return modtime;
    }

    public void setModTime(Timestamp modtime) {
        this.modtime = modtime;
    }

    public Timestamp getCreTime() {
        return cretime;
    }

    public void setCreTime(Timestamp cretime) {
        this.cretime = cretime;
    }

    public String getOpenAuth() {
        return openAuth;
    }

    public void setOpenAuth(String openAuth) {
        this.openAuth = openAuth;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public Integer getModUserID() {
        return modUserID;
    }

    public void setModUserID(Integer modUserID) {
        this.modUserID = modUserID;
    }

    @Override
    public String toString() {
        return "UserHeader{" + "userID=" + userID + ", userName=" + userName + ", account=" + account + ", password=" + password + ", email=" + email + ", userLevel=" + userLevel + ", openAuth=" + openAuth + ", authCode=" + authCode + ", photo=" + photo + ", modUserID=" + modUserID + ", modtime=" + modtime + ", cretime=" + cretime + ", depHeader=" + depHeader + '}';
    }

}
