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

    public Path GetAPTempPath(String RootPath, UserHeader LOGININFO, AtaType fileFunc) {
        return Paths.get(RootPath,//Root//JEIP\build\web\resources\temp\附件類型(form Or Ann)\UserID
                fileFunc.getName(),//FileFunc
                LOGININFO.getUserID().toString());//UserID
    }
}
