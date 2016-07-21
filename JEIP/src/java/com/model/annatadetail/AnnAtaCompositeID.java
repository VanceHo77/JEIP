/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.model.annatadetail;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Id;

/**
 * 系統公告附件明細的複合主鍵類別<br>
 *
 * 備註一：<br>
 * 1.必須實現Serializable接口。<br>
 * 2.必須有默認的public無參數的構造方法。<br>
 * 3.必須覆蓋equals和hashCode方法。<br>
 *
 * 備註二：<br>
 * 有二種解決方法，<br>
 * 1.使用IdClass(本專案採用此法)。查詢方式[select t.annID,t.seqNO from annAtaDetail t]<br>
 * 2.使用EmbeddedId<br>
 *
 *
 * @author Vance
 */
public class AnnAtaCompositeID implements Serializable {
    private Integer annID;
    private Integer seqNO;

    public AnnAtaCompositeID() {
    }

    public AnnAtaCompositeID(Integer annID, Integer seqNO) {
        this.annID = annID;
        this.seqNO = seqNO;
    }

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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + Objects.hashCode(this.annID);
        hash = 73 * hash + Objects.hashCode(this.seqNO);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AnnAtaCompositeID other = (AnnAtaCompositeID) obj;
        if (!Objects.equals(this.annID, other.annID)) {
            return false;
        }
        if (!Objects.equals(this.seqNO, other.seqNO)) {
            return false;
        }
        return true;
    }

}
