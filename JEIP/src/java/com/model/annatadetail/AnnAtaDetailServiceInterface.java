package com.model.annatadetail;

import java.util.List;

/**
 * AnnAtaDetailService提供的標準服務介面定義
 *
 * @author Vance
 */
public interface AnnAtaDetailServiceInterface {

    /**
     * 新增附件
     *
     * @param annAtaDetail
     * @return
     */
    public boolean addAnnAtaDetail(AnnAtaDetail annAtaDetail);

    /**
     * 更新附件
     *
     * @param annAtaDetail
     * @return
     */
    public boolean updateAnnAtaDetail(AnnAtaDetail annAtaDetail);

    /**
     * 刪除附件
     *
     * @param annAtaDetail
     * @return
     */
    public boolean deleteAnnAtaDetail(AnnAtaDetail annAtaDetail);

    /**
     * 使用AnnAtaDetail物件查詢單一附件
     *
     * @param annAtaDetail
     * @return
     */
    public List<AnnAtaDetail> findAnnAtaDetails(AnnAtaDetail annAtaDetail);

}
