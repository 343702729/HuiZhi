package com.huizhi.manage.request.common;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.huizhi.manage.data.Constants;
import com.huizhi.manage.http.AsyncFileUpload;
import com.huizhi.manage.http.HttpConnect;
import com.huizhi.manage.http.URLData;
import com.huizhi.manage.node.FileMenuNode;
import com.huizhi.manage.node.FolderNode;
import com.huizhi.manage.node.FileNode;
import com.huizhi.manage.node.ResultNode;
import com.huizhi.manage.util.JSONUtil;
import com.huizhi.manage.base.ThreadPoolDo;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CL on 2018/1/12.
 */

public class FileGetRequest {

    /**
     * 文件上传token(七牛)
     * @param handler
     */
    public void getFileUploadToken(Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_FILE_UPLOAD_TOKEN));
        ThreadPoolDo.getInstance().executeThread(new FileUploadTokenThread(params, handler));
    }

    /**
     * 根据文件名查询文件
     * @param teacherid
     * @param filename
     * @param handler
     */
    public void getFileSearch(String teacherid, String filename, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_FILE_SEARCH));
        params.add(new BasicNameValuePair("TeacherId", teacherid));
        params.add(new BasicNameValuePair("FileName", filename));
        ThreadPoolDo.getInstance().executeThread(new FileSearchThread(params, handler));
    }

    /**
     * 取文件目录
     * @param parentFolderId
     * @param handler
     */
    public void getFileMenu(String parentFolderId, String teacherid, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_FILE_MENU));
        params.add(new BasicNameValuePair("ParentFolderId", parentFolderId));
        params.add(new BasicNameValuePair("TeacherId", teacherid));
        ThreadPoolDo.getInstance().executeThread(new FileMenuThread(params, handler));
    }

    /**
     * 取文件夹下目录及文件
     * @param parentFolderId
     * @param handler
     */
    public void getFileFolderList(String parentFolderId, String teacherid, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_FILE_FOLDER_LIST));
        params.add(new BasicNameValuePair("ParentFolderId", parentFolderId));
        params.add(new BasicNameValuePair("TeacherId", teacherid));
        ThreadPoolDo.getInstance().executeThread(new FileFolderListThread(params, handler));
    }

    /**
     * 取文件列表
     * @param folderid
     * @param handler
     */
    public void getFileList(String folderid, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_FILE_LIST));
        params.add(new BasicNameValuePair("FolderId", folderid));
        ThreadPoolDo.getInstance().executeThread(new FileListThread(params, handler));
    }

    /**
     * 取文件信息
     * @param fileid
     * @param teacherid
     * @param handler
     */
    public void getFileInfo(String fileid, String teacherid, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_FILE_INFO));
        params.add(new BasicNameValuePair("FileId", fileid));
        params.add(new BasicNameValuePair("TeacherId", teacherid));
        ThreadPoolDo.getInstance().executeThread(new FileInfoThread(params, handler));
    }

    /**
     * 取收藏的文件列表
     * @param teacherid
     * @param handler
     */
    public void getFavoriteFiles(String teacherid, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_FILE_FAVORITE));
        params.add(new BasicNameValuePair("TeacherId", teacherid));
        ThreadPoolDo.getInstance().executeThread(new FileFavoriteThread(params, handler));
    }

    private class FileUploadTokenThread extends Thread{
        private List<BasicNameValuePair> params;
        private android.os.Handler handler;

        public FileUploadTokenThread(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            try {
                String result = HttpConnect.getHttpConnect(URLData.getUrlFileUploadToken(), params);
                Log.i("HuiZhi", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                JSONObject jsonOb = new JSONObject(result);
                String token = JSONUtil.parseString(jsonOb, "token");
                String domain = JSONUtil.parseString(jsonOb, "domain");
                AsyncFileUpload.getInstance().setToken(token);
                AsyncFileUpload.getInstance().setDomain(domain);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private class FileSearchThread extends Thread{
        private List<BasicNameValuePair> params;
        private android.os.Handler handler;

        public FileSearchThread(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.getHttpConnect(URLData.getUrlFileSearch(), params);
                Log.i("HuiZhi", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("HuiZhi", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    FileMenuNode node = parseReturn(resultNode.getReturnObj());
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS_ONE, node));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private FileMenuNode parseReturn(String jsonStr){
            if(TextUtils.isEmpty(jsonStr))
                return null;
            FileMenuNode node = new FileMenuNode();
            try {
                JSONArray fileAr = new JSONArray(jsonStr);
                List<FileNode> files = parseFilesReturn(fileAr);
                node.setFiles(files);
            }catch (Exception e){
                e.printStackTrace();
            }
            return node;
        }
    }

    private class FileMenuThread extends Thread{
        private List<BasicNameValuePair> params;
        private android.os.Handler handler;

        public FileMenuThread(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.getHttpConnect(URLData.getUrlFileMenu(), params);
                Log.i("Login", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("Login", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    List<FolderNode> nodes = parseReturn(resultNode.getReturnObj());
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS, nodes));
                }else
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_FAILURE, resultNode.getMessage()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private List<FolderNode> parseReturn(String jsonStr){
            if(TextUtils.isEmpty(jsonStr))
                return null;
            List<FolderNode> nodes = new ArrayList<>();
            try {
                FolderNode node = null;
                JSONObject jsonOb = null;
                JSONArray jsonAr = new JSONArray(jsonStr);
                for(int i=0; i<jsonAr.length(); i++){
                    jsonOb = jsonAr.getJSONObject(i);
                    node = new FolderNode();
                    node.setFolderId(JSONUtil.parseString(jsonOb, "FolderId"));
                    node.setParentFolderId(JSONUtil.parseString(jsonOb, "ParentFolderId"));
                    node.setFolderName(JSONUtil.parseString(jsonOb, "FolderName"));
                    node.setSequcenceNumber(JSONUtil.parseInt(jsonOb, "SequcenceNumber"));
                    node.setRoleType(JSONUtil.parseInt(jsonOb, "RoleType"));
                    node.setStrCreateTime(JSONUtil.parseString(jsonOb, "strCreateTime"));
                    node.setStrRoleType(JSONUtil.parseString(jsonOb, "StrRoleType"));
                    node.setSubFoldNum(JSONUtil.parseInt(jsonOb, "SubFoldNum"));
                    nodes.add(node);
                    node = null;
                    jsonOb = null;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return nodes;
        }

    }



    private class FileFolderListThread extends Thread{
        private List<BasicNameValuePair> params;
        private android.os.Handler handler;

        public FileFolderListThread(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.getHttpConnect(URLData.getUrlFileFolderList(), params);
                Log.i("HuiZhi", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("HuiZhi", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    FileMenuNode node = parseReturn(resultNode.getReturnObj());
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS, node));
                }else
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_FAILURE, resultNode.getMessage()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private FileMenuNode parseReturn(String jsonStr){
            if(TextUtils.isEmpty(jsonStr))
                return null;
            try {
                JSONObject jsonOb = new JSONObject(jsonStr);
                if(jsonOb==null)
                    return null;
                FileMenuNode node = new FileMenuNode();
                JSONArray folderAr = jsonOb.getJSONArray("Folers");
                List<FolderNode> folders = parseFoldersReturn(folderAr);
                node.setFolders(folders);
                JSONArray fileAr = jsonOb.getJSONArray("Files");
                List<FileNode> files = parseFilesReturn(fileAr);
                node.setFiles(files);
                return node;
            }catch (Exception e){

            }
            return null;
        }

        private List<FolderNode> parseFoldersReturn(JSONArray jsonAr){
            if(jsonAr==null)
                return null;
            List<FolderNode> nodes = new ArrayList<>();
            try {
                FolderNode node = null;
                JSONObject jsonOb = null;
                for(int i=0; i<jsonAr.length(); i++){
                    jsonOb = jsonAr.getJSONObject(i);
                    node = new FolderNode();
                    node.setFolderId(JSONUtil.parseString(jsonOb, "FolderId"));
                    node.setParentFolderId(JSONUtil.parseString(jsonOb, "ParentFolderId"));
                    node.setFolderName(JSONUtil.parseString(jsonOb, "FolderName"));
                    node.setSequcenceNumber(JSONUtil.parseInt(jsonOb, "SequcenceNumber"));
                    node.setRoleType(JSONUtil.parseInt(jsonOb, "RoleType"));
                    node.setStrCreateTime(JSONUtil.parseString(jsonOb, "strCreateTime"));
                    node.setStrRoleType(JSONUtil.parseString(jsonOb, "StrRoleType"));
                    node.setSubFoldNum(JSONUtil.parseInt(jsonOb, "SubFoldNum"));
                    nodes.add(node);
                    node = null;
                    jsonOb = null;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return nodes;
        }

    }

    private List<FileNode> parseFilesReturn(JSONArray jsonAr){
        if(jsonAr==null)
            return null;
        List<FileNode> nodes = new ArrayList<>();
        try {
            FileNode node = null;
            JSONObject jsonOb = null;
            for(int i=0; i<jsonAr.length(); i++){
                jsonOb = jsonAr.getJSONObject(i);
                node = new FileNode();
                node.setFileId(JSONUtil.parseString(jsonOb, "FileId"));
                node.setFolderId(JSONUtil.parseString(jsonOb, "FolderId"));
                node.setFileName(JSONUtil.parseString(jsonOb, "FileName"));
                node.setFileType(JSONUtil.parseInt(jsonOb, "FileType"));
//                node.setFilePath(JSONUtil.parseString(jsonOb, "FilePath"));
                node.setSequcenceNumber(JSONUtil.parseInt(jsonOb, "SequcenceNumber"));
                node.setDescription(JSONUtil.parseString(jsonOb, "Description"));
                node.setStrCreateTime(JSONUtil.parseString(jsonOb, "strCreateTime"));
                node.setStrFileType(JSONUtil.parseString(jsonOb, "StrFileType"));
                String fileL = JSONUtil.parseString(jsonOb, "FileList");
                if(!TextUtils.isEmpty(fileL))
                    node.setFileList(parseFileList(new JSONArray(fileL)));
                String fileI = JSONUtil.parseString(jsonOb, "FileInfo");
                if(!TextUtils.isEmpty(fileI))
                    node.setFileInfos(parseFileList(new JSONArray(fileI)));
                nodes.add(node);
                node = null;
                jsonOb = null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return nodes;
    }

    private class FileListThread extends Thread{
        private List<BasicNameValuePair> params;
        private android.os.Handler handler;

        public FileListThread(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.getHttpConnect(URLData.getUrlFileList(), params);
                Log.i("Login", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("Login", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    List<FileNode> nodes = parseReturn(resultNode.getReturnObj());
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS, nodes));
                }else
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_FAILURE, resultNode.getMessage()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    private List<FileNode> parseReturn(String jsonStr){
        if(TextUtils.isEmpty(jsonStr))
            return null;
        List<FileNode> nodes = new ArrayList<>();
        try {
            FileNode node = null;
            JSONObject jsonOb = null;
            JSONArray jsonAr = new JSONArray(jsonStr);
            for(int i=0; i<jsonAr.length(); i++){
                jsonOb = jsonAr.getJSONObject(i);
                node = new FileNode();
                node.setFileId(JSONUtil.parseString(jsonOb, "FileId"));
                node.setFolderId(JSONUtil.parseString(jsonOb, "FolderId"));
                node.setFileName(JSONUtil.parseString(jsonOb, "FileName"));
                node.setFileType(JSONUtil.parseInt(jsonOb, "FileType"));
//                node.setFilePath(JSONUtil.parseString(jsonOb, "FilePath"));
                node.setSequcenceNumber(JSONUtil.parseInt(jsonOb, "SequcenceNumber"));
                node.setDescription(JSONUtil.parseString(jsonOb, "Description"));
                node.setStrCreateTime(JSONUtil.parseString(jsonOb, "strCreateTime"));
                node.setStrFileType(JSONUtil.parseString(jsonOb, "StrFileType"));
                String fileL = JSONUtil.parseString(jsonOb, "FileList");
                if(!TextUtils.isEmpty(fileL))
                    node.setFileList(parseFileList(new JSONArray(fileL)));
                String fileI = JSONUtil.parseString(jsonOb, "FileInfo");
                if(!TextUtils.isEmpty(fileI))
                    node.setFileInfos(parseFileList(new JSONArray(fileI)));
                nodes.add(node);
                node = null;
                jsonOb = null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return nodes;
    }

    private List<FileNode> parseFileList(JSONArray jsonAr){
        if(jsonAr==null)
            return null;
        List<FileNode> nodes = new ArrayList<>();
        try {
            FileNode node;
            JSONObject jsonOb = null;
            for(int i=0; i<jsonAr.length(); i++){
                node = new FileNode();
                jsonOb = jsonAr.getJSONObject(i);
                node.setFilePath(JSONUtil.parseString(jsonOb, "FilePath"));
                node.setDescription(JSONUtil.parseString(jsonOb, "Description"));
                node.setFileSize(JSONUtil.parseString(jsonOb, "FileSize"));
                node.setFileName(JSONUtil.parseString(jsonOb, "FileName"));
                nodes.add(node);
                node = null;
                jsonOb = null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return nodes;
    }

    private class FileInfoThread extends Thread{
        private List<BasicNameValuePair> params;
        private android.os.Handler handler;

        public FileInfoThread(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.getHttpConnect(URLData.getUrlFileInfo(), params);
                Log.i("HuiZhi", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("HuiZhi", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    FileNode node = parseReturn(resultNode.getReturnObj());
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS_ONE, node));
                }else
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_FAILURE, resultNode.getMessage()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private FileNode parseReturn(String jsonStr){
            if(TextUtils.isEmpty(jsonStr))
                return null;
            FileNode node = new FileNode();
            try {
                JSONObject jsonOb = new JSONObject(jsonStr);
                node.setFileId(JSONUtil.parseString(jsonOb, "FileId"));
                node.setFolderId(JSONUtil.parseString(jsonOb, "FolderId"));
                node.setFileName(JSONUtil.parseString(jsonOb, "FileName"));
                node.setFileType(JSONUtil.parseInt(jsonOb, "FileType"));
//                node.setFilePath(JSONUtil.parseString(jsonOb, "FilePath"));
                node.setSequcenceNumber(JSONUtil.parseInt(jsonOb, "SequcenceNumber"));
                node.setDescription(JSONUtil.parseString(jsonOb, "Description"));
                node.setStrCreateTime(JSONUtil.parseString(jsonOb, "strCreateTime"));
                node.setStrFileType(JSONUtil.parseString(jsonOb, "StrFileType"));
                node.setFavorite(JSONUtil.parseBoolean(jsonOb, "IsFavorite"));
                String fileL = JSONUtil.parseString(jsonOb, "FileList");
                if(!TextUtils.isEmpty(fileL))
                    node.setFileList(parseFileList(new JSONArray(fileL)));
                String fileI = JSONUtil.parseString(jsonOb, "FileInfo");
                if(!TextUtils.isEmpty(fileI))
                    node.setFileInfos(parseFileList(new JSONArray(fileI)));
            }catch (Exception e){
                e.printStackTrace();
            }
            return node;
        }
    }

    private class FileFavoriteThread extends Thread{
        private List<BasicNameValuePair> params;
        private android.os.Handler handler;

        public FileFavoriteThread(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.getHttpConnect(URLData.getUrlFileFavorite(), params);
                Log.i("HuiZhi", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("HuiZhi", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    List<FileNode> nodes = parseReturn(resultNode.getReturnObj());
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS, nodes));
                }else
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_FAILURE, resultNode.getMessage()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
