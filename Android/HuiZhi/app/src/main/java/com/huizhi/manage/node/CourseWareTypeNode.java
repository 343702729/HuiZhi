package com.huizhi.manage.node;

import java.util.List;

public class CourseWareTypeNode {
    private ObjCategoryItem ObjCategory;
    private List<ObjTypeItem> ObjTypeList;

    public ObjCategoryItem getObjCategory() {
        return ObjCategory;
    }

    public void setObjCategory(ObjCategoryItem objCategory) {
        ObjCategory = objCategory;
    }

    public List<ObjTypeItem> getObjTypeList() {
        return ObjTypeList;
    }

    public void setObjTypeList(List<ObjTypeItem> objTypeList) {
        ObjTypeList = objTypeList;
    }

    public class ObjCategoryItem{
        private String CategoryId;
        private String CategoryName;
        private String CategoryCover;
        private String FullCategoryCover;
        private String TrialAge;
        private String StudyDesc;
        private String EnhanceDesc;
        private int TypeCount;
        private String CoursewareUrl;

        public String getCategoryId() {
            return CategoryId;
        }

        public void setCategoryId(String categoryId) {
            CategoryId = categoryId;
        }

        public String getCategoryName() {
            return CategoryName;
        }

        public void setCategoryName(String categoryName) {
            CategoryName = categoryName;
        }

        public String getCategoryCover() {
            return CategoryCover;
        }

        public void setCategoryCover(String categoryCover) {
            CategoryCover = categoryCover;
        }

        public String getFullCategoryCover() {
            return FullCategoryCover;
        }

        public void setFullCategoryCover(String fullCategoryCover) {
            FullCategoryCover = fullCategoryCover;
        }

        public String getTrialAge() {
            return TrialAge;
        }

        public void setTrialAge(String trialAge) {
            TrialAge = trialAge;
        }

        public String getStudyDesc() {
            return StudyDesc;
        }

        public void setStudyDesc(String studyDesc) {
            StudyDesc = studyDesc;
        }

        public String getEnhanceDesc() {
            return EnhanceDesc;
        }

        public void setEnhanceDesc(String enhanceDesc) {
            EnhanceDesc = enhanceDesc;
        }

        public int getTypeCount() {
            return TypeCount;
        }

        public void setTypeCount(int typeCount) {
            TypeCount = typeCount;
        }

        public String getCoursewareUrl() {
            return CoursewareUrl;
        }

        public void setCoursewareUrl(String coursewareUrl) {
            CoursewareUrl = coursewareUrl;
        }
    }

    public class ObjTypeItem{
        private int Code;
        private String Name;
        private String Desc;
        private String CoursewareUrl;

        public int getCode() {
            return Code;
        }

        public void setCode(int code) {
            Code = code;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getDesc() {
            return Desc;
        }

        public void setDesc(String desc) {
            Desc = desc;
        }

        public String getCoursewareUrl() {
            return CoursewareUrl;
        }

        public void setCoursewareUrl(String coursewareUrl) {
            CoursewareUrl = coursewareUrl;
        }
    }
}
