package com.huizhi.manage.node;

import java.util.List;

public class TeacherTrainingNode {
	private List<BannerNode> ObjBanner;
	private ObjProgress ObjProgress;
	private List<ObjTrainingItem> ObjTraining;
	private List<ObjTeachingTrainingItem> ObjTeachingTraining;

	public List<BannerNode> getObjBanner() {
		return ObjBanner;
	}

	public void setObjBanner(List<BannerNode> objBanner) {
		ObjBanner = objBanner;
	}

	public TeacherTrainingNode.ObjProgress getObjProgress() {
		return ObjProgress;
	}

	public void setObjProgress(TeacherTrainingNode.ObjProgress objProgress) {
		ObjProgress = objProgress;
	}

	public List<ObjTrainingItem> getObjTraining() {
		return ObjTraining;
	}

	public void setObjTraining(List<ObjTrainingItem> objTraining) {
		ObjTraining = objTraining;
	}

	public List<ObjTeachingTrainingItem> getObjTeachingTraining() {
		return ObjTeachingTraining;
	}

	public void setObjTeachingTraining(List<ObjTeachingTrainingItem> objTeachingTraining) {
		ObjTeachingTraining = objTeachingTraining;
	}

	public class ObjProgress{
		private int TotLessonNum;
		private int DoneLessonNum;
		private int DoneLessonPrepareNum;
		private int CommentedNum;
		private int ToBeCommentNum;
		private String LessonFinishPercent;
		private String LessonPreparedPercent;
		private String CommentFinishPercent;

		public int getTotLessonNum() {
			return TotLessonNum;
		}

		public void setTotLessonNum(int totLessonNum) {
			TotLessonNum = totLessonNum;
		}

		public int getDoneLessonNum() {
			return DoneLessonNum;
		}

		public void setDoneLessonNum(int doneLessonNum) {
			DoneLessonNum = doneLessonNum;
		}

		public int getDoneLessonPrepareNum() {
			return DoneLessonPrepareNum;
		}

		public void setDoneLessonPrepareNum(int doneLessonPrepareNum) {
			DoneLessonPrepareNum = doneLessonPrepareNum;
		}

		public int getCommentedNum() {
			return CommentedNum;
		}

		public void setCommentedNum(int commentedNum) {
			CommentedNum = commentedNum;
		}

		public int getToBeCommentNum() {
			return ToBeCommentNum;
		}

		public void setToBeCommentNum(int toBeCommentNum) {
			ToBeCommentNum = toBeCommentNum;
		}

		public String getLessonFinishPercent() {
			return LessonFinishPercent;
		}

		public void setLessonFinishPercent(String lessonFinishPercent) {
			LessonFinishPercent = lessonFinishPercent;
		}

		public String getLessonPreparedPercent() {
			return LessonPreparedPercent;
		}

		public void setLessonPreparedPercent(String lessonPreparedPercent) {
			LessonPreparedPercent = lessonPreparedPercent;
		}

		public String getCommentFinishPercent() {
			return CommentFinishPercent;
		}

		public void setCommentFinishPercent(String commentFinishPercent) {
			CommentFinishPercent = commentFinishPercent;
		}
	}

	public class ObjTrainingItem{
		private String TrainingId;
		private String ParentId;
		private String Title;
		private String CoverImg;
		private int Type;			//1:选修 2：必修
		private String Content;
		private int Points;
		private String Description;
		private String CreateTime;
		private boolean ActiveFlg;
		private String StrCreateTime;
		private String ParentTitle;
		private int SubCount;

		public String getTrainingId() {
			return TrainingId;
		}

		public void setTrainingId(String trainingId) {
			TrainingId = trainingId;
		}

		public String getParentId() {
			return ParentId;
		}

		public void setParentId(String parentId) {
			ParentId = parentId;
		}

		public String getTitle() {
			return Title;
		}

		public void setTitle(String title) {
			Title = title;
		}

		public String getCoverImg() {
			return CoverImg;
		}

		public void setCoverImg(String coverImg) {
			CoverImg = coverImg;
		}

		public int getType() {
			return Type;
		}

		public void setType(int type) {
			Type = type;
		}

		public String getContent() {
			return Content;
		}

		public void setContent(String content) {
			Content = content;
		}

		public int getPoints() {
			return Points;
		}

		public void setPoints(int points) {
			Points = points;
		}

		public String getDescription() {
			return Description;
		}

		public void setDescription(String description) {
			Description = description;
		}

		public String getCreateTime() {
			return CreateTime;
		}

		public void setCreateTime(String createTime) {
			CreateTime = createTime;
		}

		public boolean isActiveFlg() {
			return ActiveFlg;
		}

		public void setActiveFlg(boolean activeFlg) {
			ActiveFlg = activeFlg;
		}

		public String getStrCreateTime() {
			return StrCreateTime;
		}

		public void setStrCreateTime(String strCreateTime) {
			StrCreateTime = strCreateTime;
		}

		public String getParentTitle() {
			return ParentTitle;
		}

		public void setParentTitle(String parentTitle) {
			ParentTitle = parentTitle;
		}

		public int getSubCount() {
			return SubCount;
		}

		public void setSubCount(int subCount) {
			SubCount = subCount;
		}
	}

	public class ObjTeachingTrainingItem{
		private String TrainingId;
		private String Title;
		private String CoverImg;
		private String ProjectType;
		private String StrProjectType;
		private int ContentType;
		private String VideoFilePath;
		private String Content;
		private String CreateTime;
		private boolean ActiveFlg;
		private String StrCreateTime;

		public String getTrainingId() {
			return TrainingId;
		}

		public void setTrainingId(String trainingId) {
			TrainingId = trainingId;
		}

		public String getTitle() {
			return Title;
		}

		public void setTitle(String title) {
			Title = title;
		}

		public String getCoverImg() {
			return CoverImg;
		}

		public void setCoverImg(String coverImg) {
			CoverImg = coverImg;
		}

		public String getProjectType() {
			return ProjectType;
		}

		public void setProjectType(String projectType) {
			ProjectType = projectType;
		}

		public String getStrProjectType() {
			return StrProjectType;
		}

		public void setStrProjectType(String strProjectType) {
			StrProjectType = strProjectType;
		}

		public int getContentType() {
			return ContentType;
		}

		public void setContentType(int contentType) {
			ContentType = contentType;
		}

		public String getVideoFilePath() {
			return VideoFilePath;
		}

		public void setVideoFilePath(String videoFilePath) {
			VideoFilePath = videoFilePath;
		}

		public String getContent() {
			return Content;
		}

		public void setContent(String content) {
			Content = content;
		}

		public String getCreateTime() {
			return CreateTime;
		}

		public void setCreateTime(String createTime) {
			CreateTime = createTime;
		}

		public boolean isActiveFlg() {
			return ActiveFlg;
		}

		public void setActiveFlg(boolean activeFlg) {
			ActiveFlg = activeFlg;
		}

		public String getStrCreateTime() {
			return StrCreateTime;
		}

		public void setStrCreateTime(String strCreateTime) {
			StrCreateTime = strCreateTime;
		}
	}
}
