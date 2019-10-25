package com.huizhi.manage.node;

import java.util.List;

public class HomeOperateNode {
    private List<ObjTask> ObjTask;
    private List<BannerNode> ObjBanner;
    private List<ObjNew> ObjNews;
    private List<ObjKnowledge> ObjKnowledge;
    private List<ObjAsk> ObjAsk;

    public List<HomeOperateNode.ObjTask> getObjTask() {
        return ObjTask;
    }

    public void setObjTask(List<HomeOperateNode.ObjTask> objTask) {
        ObjTask = objTask;
    }

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

    public List<HomeOperateNode.ObjKnowledge> getObjKnowledge() {
        return ObjKnowledge;
    }

    public void setObjKnowledge(List<HomeOperateNode.ObjKnowledge> objKnowledge) {
        ObjKnowledge = objKnowledge;
    }

    public List<HomeOperateNode.ObjAsk> getObjAsk() {
        return ObjAsk;
    }

    public void setObjAsk(List<HomeOperateNode.ObjAsk> objAsk) {
        ObjAsk = objAsk;
    }

    public class ObjTask{
        private String TaskId;
        private String TaskName;
        private int ExecuteType;
        private int ExecuteStatus;
        private String BlockingDate;
        private String StrBlockingDate;
        private String Details;
        private String CreateTime;
        private String StrCreateTime;
        private boolean ActiveFlg;

        public String getTaskId() {
            return TaskId;
        }

        public void setTaskId(String taskId) {
            TaskId = taskId;
        }

        public String getTaskName() {
            return TaskName;
        }

        public void setTaskName(String taskName) {
            TaskName = taskName;
        }

        public int getExecuteType() {
            return ExecuteType;
        }

        public void setExecuteType(int executeType) {
            ExecuteType = executeType;
        }

        public String getBlockingDate() {
            return BlockingDate;
        }

        public void setBlockingDate(String blockingDate) {
            BlockingDate = blockingDate;
        }

        public String getStrBlockingDate() {
            return StrBlockingDate;
        }

        public void setStrBlockingDate(String strBlockingDate) {
            StrBlockingDate = strBlockingDate;
        }

        public String getDetails() {
            return Details;
        }

        public void setDetails(String details) {
            Details = details;
        }

        public String getCreateTime() {
            return CreateTime;
        }

        public void setCreateTime(String createTime) {
            CreateTime = createTime;
        }

        public String getStrCreateTime() {
            return StrCreateTime;
        }

        public void setStrCreateTime(String strCreateTime) {
            StrCreateTime = strCreateTime;
        }

        public boolean isActiveFlg() {
            return ActiveFlg;
        }

        public void setActiveFlg(boolean activeFlg) {
            ActiveFlg = activeFlg;
        }

        public int getExecuteStatus() {
            return ExecuteStatus;
        }

        public void setExecuteStatus(int executeStatus) {
            ExecuteStatus = executeStatus;
        }
    }

    public class ObjBanner{
        private String NewsId;
        private String ImgUrl;

        public String getNewsId() {
            return NewsId;
        }

        public void setNewsId(String newsId) {
            NewsId = newsId;
        }

        public String getImgUrl() {
            return ImgUrl;
        }

        public void setImgUrl(String imgUrl) {
            ImgUrl = imgUrl;
        }
    }

    public class  ObjNew{
        private String NewsId;
        private String NewsTitle;
        private String NewsContent;
        private int ReferenceNewsId;
        private String ThumbImgUrl;
        private String strCreateTime1;
        private boolean IsNotPreview;

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

        public int getReferenceNewsId() {
            return ReferenceNewsId;
        }

        public void setReferenceNewsId(int referenceNewsId) {
            ReferenceNewsId = referenceNewsId;
        }

        public String getThumbImgUrl() {
            return ThumbImgUrl;
        }

        public void setThumbImgUrl(String thumbImgUrl) {
            ThumbImgUrl = thumbImgUrl;
        }

        public String getStrCreateTime1() {
            return strCreateTime1;
        }

        public void setStrCreateTime1(String strCreateTime1) {
            this.strCreateTime1 = strCreateTime1;
        }

        public boolean isNotPreview() {
            return IsNotPreview;
        }

        public void setNotPreview(boolean notPreview) {
            IsNotPreview = notPreview;
        }
    }

    public class ObjAsk{
        private String AskId;
        private int CategoryId;
        private String UserId;
        private String TeacherId;
        private String Question;
        private String Reply;
        private String shortReply;
        private String shortReply1;
        private String ReplyTime;
        private String strReplyTime;
        private boolean IsSolve;
        private String CreateTime;
        private String strCreateTime;
        private String CategoryName;
        private String UserName;
        private String TeacherName;

        public String getAskId() {
            return AskId;
        }

        public void setAskId(String askId) {
            AskId = askId;
        }

        public int getCategoryId() {
            return CategoryId;
        }

        public void setCategoryId(int categoryId) {
            CategoryId = categoryId;
        }

        public String getUserId() {
            return UserId;
        }

        public void setUserId(String userId) {
            UserId = userId;
        }

        public String getTeacherId() {
            return TeacherId;
        }

        public void setTeacherId(String teacherId) {
            TeacherId = teacherId;
        }

        public String getQuestion() {
            return Question;
        }

        public void setQuestion(String question) {
            Question = question;
        }

        public String getReply() {
            return Reply;
        }

        public void setReply(String reply) {
            Reply = reply;
        }

        public String getShortReply() {
            return shortReply;
        }

        public void setShortReply(String shortReply) {
            this.shortReply = shortReply;
        }

        public String getShortReply1() {
            return shortReply1;
        }

        public void setShortReply1(String shortReply1) {
            this.shortReply1 = shortReply1;
        }

        public String getReplyTime() {
            return ReplyTime;
        }

        public void setReplyTime(String replyTime) {
            ReplyTime = replyTime;
        }

        public String getStrReplyTime() {
            return strReplyTime;
        }

        public void setStrReplyTime(String strReplyTime) {
            this.strReplyTime = strReplyTime;
        }

        public boolean isSolve() {
            return IsSolve;
        }

        public void setSolve(boolean solve) {
            IsSolve = solve;
        }

        public String getCreateTime() {
            return CreateTime;
        }

        public void setCreateTime(String createTime) {
            CreateTime = createTime;
        }

        public String getStrCreateTime() {
            return strCreateTime;
        }

        public void setStrCreateTime(String strCreateTime) {
            this.strCreateTime = strCreateTime;
        }

        public String getCategoryName() {
            return CategoryName;
        }

        public void setCategoryName(String categoryName) {
            CategoryName = categoryName;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String userName) {
            UserName = userName;
        }

        public String getTeacherName() {
            return TeacherName;
        }

        public void setTeacherName(String teacherName) {
            TeacherName = teacherName;
        }
    }

    public class ObjKnowledge{
        private String month;
        private List<KnowledgeItem> items;

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public List<KnowledgeItem> getItems() {
            return items;
        }

        public void setItems(List<KnowledgeItem> items) {
            this.items = items;
        }
    }

    public class KnowledgeItem{
        private String KnowledgeId;
        private String Title;
        private int VisitCount;
        private String SharerName;
        private String FullAudioFileUrl;
        private String FullTopImgUrl;
        private String FullSharerHeadImgUrl;
        private String strCreateTime;
        private List<Accessory> Accessorys;
        private boolean IsPreview;
        private int FitMonth;
        private int FitFestival;
        private String StrFitFestival;

        public String getKnowledgeId() {
            return KnowledgeId;
        }

        public void setKnowledgeId(String knowledgeId) {
            KnowledgeId = knowledgeId;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }

        public int getVisitCount() {
            return VisitCount;
        }

        public void setVisitCount(int visitCount) {
            VisitCount = visitCount;
        }

        public String getSharerName() {
            return SharerName;
        }

        public void setSharerName(String sharerName) {
            SharerName = sharerName;
        }

        public String getFullAudioFileUrl() {
            return FullAudioFileUrl;
        }

        public void setFullAudioFileUrl(String fullAudioFileUrl) {
            FullAudioFileUrl = fullAudioFileUrl;
        }

        public String getFullTopImgUrl() {
            return FullTopImgUrl;
        }

        public void setFullTopImgUrl(String fullTopImgUrl) {
            FullTopImgUrl = fullTopImgUrl;
        }

        public String getFullSharerHeadImgUrl() {
            return FullSharerHeadImgUrl;
        }

        public void setFullSharerHeadImgUrl(String fullSharerHeadImgUrl) {
            FullSharerHeadImgUrl = fullSharerHeadImgUrl;
        }

        public String getStrCreateTime() {
            return strCreateTime;
        }

        public void setStrCreateTime(String strCreateTime) {
            this.strCreateTime = strCreateTime;
        }

        public List<Accessory> getAccessorys() {
            return Accessorys;
        }

        public void setAccessorys(List<Accessory> accessorys) {
            Accessorys = accessorys;
        }

        public boolean isPreview() {
            return IsPreview;
        }

        public void setPreview(boolean preview) {
            IsPreview = preview;
        }

        public int getFitMonth() {
            return FitMonth;
        }

        public void setFitMonth(int fitMonth) {
            FitMonth = fitMonth;
        }

        public int getFitFestival() {
            return FitFestival;
        }

        public void setFitFestival(int fitFestival) {
            FitFestival = fitFestival;
        }

        public String getStrFitFestival() {
            return StrFitFestival;
        }

        public void setStrFitFestival(String strFitFestival) {
            StrFitFestival = strFitFestival;
        }
    }

    public class Accessory{
        private String FileUrl;
        private String Title;
        private String FullFileUrl;
        private String NewTitle;

        public String getFileUrl() {
            return FileUrl;
        }

        public void setFileUrl(String fileUrl) {
            FileUrl = fileUrl;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }

        public String getFullFileUrl() {
            return FullFileUrl;
        }

        public void setFullFileUrl(String fullFileUrl) {
            FullFileUrl = fullFileUrl;
        }

        public String getNewTitle() {
            return NewTitle;
        }

        public void setNewTitle(String newTitle) {
            NewTitle = newTitle;
        }
    }
}
