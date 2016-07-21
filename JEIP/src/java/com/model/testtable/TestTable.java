package com.model.testtable;

import com.model.userheader.UserHeader;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Vance
 */
@Entity
public class TestTable implements Serializable {

    @Id
    private Integer testID;
    private String testNam;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "userID")
    private UserHeader userHeader;

//    public TestTable() {
//        super();
//    }
//
//    public TestTable(Integer testID, String testNam, UserHeader userHeader) {
//        this.testID = testID;
//        this.testNam = testNam;
//        this.userHeader = userHeader;
//    }

    public UserHeader getUserHeader() {
        return userHeader;
    }

    public void setUserHeader(UserHeader userHeader) {
        this.userHeader = userHeader;
    }

    public Integer getTestID() {
        return testID;
    }

    public void setTestID(Integer testID) {
        this.testID = testID;
    }

    public String getTestNam() {
        return testNam;
    }

    public void setTestNam(String testNam) {
        this.testNam = testNam;
    }

    @Override
    public String toString() {
        return "TestTable{" + "testID=" + testID + ", testNam=" + testNam + '}';
    }

}
