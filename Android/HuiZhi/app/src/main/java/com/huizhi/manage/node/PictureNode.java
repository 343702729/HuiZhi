package com.huizhi.manage.node;

/**
 * Created by CL on 2018/1/20.
 */

public class PictureNode {
    private String imageId;
    private String worksId;
    private String path;
    private String url;
    private String description;
    private String fullImageUrl;
    private String thumbImageUrl190;
    private String thumbImageUrl400;
    private boolean isServer = false;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getWorksId() {
        return worksId;
    }

    public void setWorksId(String worksId) {
        this.worksId = worksId;
    }

    public String getFullImageUrl() {
        return fullImageUrl;
    }

    public void setFullImageUrl(String fullImageUrl) {
        this.fullImageUrl = fullImageUrl;
    }

    public String getThumbImageUrl190() {
        return thumbImageUrl190;
    }

    public void setThumbImageUrl190(String thumbImageUrl190) {
        this.thumbImageUrl190 = thumbImageUrl190;
    }

    public String getThumbImageUrl400() {
        return thumbImageUrl400;
    }

    public void setThumbImageUrl400(String thumbImageUrl400) {
        this.thumbImageUrl400 = thumbImageUrl400;
    }

    public boolean isServer() {
        return isServer;
    }

    public void setServer(boolean server) {
        isServer = server;
    }
}
