package com.huizhi.manage.node;

import java.util.List;

public class HomeInfoNode {
    private List<BannerNode> ObjBanner;
    private List<ObjNew> ObjNews;
    private List<TeacherTrainingNode.ObjTeachingTrainingItem> ObjTeachingTraining;
    private List<HomeOperateNode.ObjNew> ObjBusinessNews;

    public List<BannerNode> getObjBanner() {
        return ObjBanner;
    }

    public void setObjBanner(List<BannerNode> objBanner) {
        ObjBanner = objBanner;
    }

    public List<ObjNew> getObjNews() {
        return ObjNews;
    }

    public void setObjNews(List<ObjNew> objNews) {
        ObjNews = objNews;
    }

    public List<TeacherTrainingNode.ObjTeachingTrainingItem> getObjTeachingTraining() {
        return ObjTeachingTraining;
    }

    public void setObjTeachingTraining(List<TeacherTrainingNode.ObjTeachingTrainingItem> objTeachingTraining) {
        ObjTeachingTraining = objTeachingTraining;
    }

    public List<HomeOperateNode.ObjNew> getObjBusinessNews() {
        return ObjBusinessNews;
    }

    public void setObjBusinessNews(List<HomeOperateNode.ObjNew> objBusinessNews) {
        ObjBusinessNews = objBusinessNews;
    }

    public class ObjNew{
        private String NewsId;
        private String NewsTitle;
        private String NewsContent;
        private String ReferenceNewsId;
        private String ThumbImgUrl;
        private String strCreateTime;
        private String strCreateTime1;
        private String IsNotPreview;

        public String getNewsId() {
            return NewsId;
        }

        public void setNewsId(String newsId) {
            NewsId = newsId;
        }

        public String getNewsTitle() {
            return NewsTitle;
        }

        public void setNewsTitle(String newsTitle) {
            NewsTitle = newsTitle;
        }

        public String getNewsContent() {
            return NewsContent;
        }

        public void setNewsContent(String newsContent) {
            NewsContent = newsContent;
        }

        public String getReferenceNewsId() {
            return ReferenceNewsId;
        }

        public void setReferenceNewsId(String referenceNewsId) {
            ReferenceNewsId = referenceNewsId;
        }

        public String getThumbImgUrl() {
            return ThumbImgUrl;
        }

        public void setThumbImgUrl(String thumbImgUrl) {
            ThumbImgUrl = thumbImgUrl;
        }

        public String getStrCreateTime() {
            return strCreateTime;
        }

        public void setStrCreateTime(String strCreateTime) {
            this.strCreateTime = strCreateTime;
        }

        public String getStrCreateTime1() {
            return strCreateTime1;
        }

        public void setStrCreateTime1(String strCreateTime1) {
            this.strCreateTime1 = strCreateTime1;
        }

        public String getIsNotPreview() {
            return IsNotPreview;
        }

        public void setIsNotPreview(String isNotPreview) {
            IsNotPreview = isNotPreview;
        }
    }

}