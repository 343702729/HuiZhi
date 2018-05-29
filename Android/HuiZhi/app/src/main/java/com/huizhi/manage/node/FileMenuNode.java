package com.huizhi.manage.node;

import java.util.List;

/**
 * Created by CL on 2018/2/2.
 */

public class FileMenuNode {
    private List<FolderNode> folders;
    private List<FileNode> files;

    public List<FolderNode> getFolders() {
        return folders;
    }

    public void setFolders(List<FolderNode> folders) {
        this.folders = folders;
    }

    public List<FileNode> getFiles() {
        return files;
    }

    public void setFiles(List<FileNode> files) {
        this.files = files;
    }
}
