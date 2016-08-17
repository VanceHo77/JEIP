/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.filetransfer;

import com.enumset.AtaType;
import com.model.userheader.UserHeader;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 建立檔案路徑的物件
 *
 * @author Vance
 */
public class CreateFilePath {

    /**
     * 建立AP暫存檔目錄
     *
     * @param RootPath 根目錄
     * @param LOGININFO 使用者登錄物件
     * @param fileFunc 檔案類型enum
     * @param seqNO 序號(表單編號/系統公告流水號)
     * @param fileName 檔名
     * @return
     */
    public Path GetAPTempPath(String RootPath, UserHeader LOGININFO, AtaType fileFunc, String seqNO, String fileName) {
        return Paths.get(RootPath,//Root//JEIP\build\web\resources\temp\附件類型(form Or Ann)\UserID
                fileFunc.getName(),//FileFunc
                LOGININFO.getUserID().toString(),//UserID
                seqNO,//序號(表單編號/系統公告流水號)
                fileName);//檔名
    }

    /**
     * 建立附件目錄
     *
     * @param LOGININFO 使用者登錄物件
     * @param fileFunc 檔案類型enum
     * @param seqNO 序號(表單編號/系統公告流水號)
     * @param fileName 檔名
     * @return
     */
    public Path GetAtaFilePath(UserHeader LOGININFO, AtaType fileFunc, String seqNO, String fileName) {
        return Paths.get("C:/MyProject",
                "AtaFile",
                fileFunc.getName(),//FileFunc
                LOGININFO.getUserID().toString(),//UserID
                seqNO,//序號(表單編號/系統公告流水號)
                fileName);//檔名
    }
}
