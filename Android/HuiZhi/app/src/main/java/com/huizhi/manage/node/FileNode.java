package com.huizhi.manage.node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CL on 2017/12/18.
 */

public class FileNode implements Serializable {
    private int id;
    private String FileId;
    private String FolderId;
    private String FileName;
    private int fileType = 1; //1:video 2:picture 3:word
    private String filePath;
    private String fileSize;
    private int sequcenceNumber;
    private String description;
    private String strCreateTime;
    private String strFileType;
    private boolean isFavorite = false;
    private List<FileNode> fileList = new ArrayList<>();
    private List<FileNode> fileInfos = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int type) {
        this.fileType = type;
    }

    public String getFileId() {
        return FileId;
    }

    public void setFileId(String fileId) {
        FileId = fileId;
    }

    public String getFolderId() {
        return FolderId;
    }

    public void setFolderId(String folderId) {
        FolderId = folderId;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getSequcenceNumber() {
        return sequcenceNumber;
    }

    public void setSequcenceNumber(int sequcenceNumber) {
        this.sequcenceNumber = sequcenceNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStrCreateTime() {
        return strCreateTime;
    }

    public void setStrCreateTime(String strCreateTime) {
        this.strCreateTime = strCreateTime;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getStrFileType() {
        return strFileType;
    }

    public void setStrFileType(String strFileType) {
        this.strFileType = strFileType;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public List<FileNode> getFileList() {
        return fileList;
    }

    public void setFileList(List<FileNode> fileList) {
        this.fileList = fileList;
    }

    public List<FileNode> getFileInfos() {
        return fileInfos;
    }

    public void setFileInfos(List<FileNode> fileInfos) {
        this.fileInfos = fileInfos;
    }
}
