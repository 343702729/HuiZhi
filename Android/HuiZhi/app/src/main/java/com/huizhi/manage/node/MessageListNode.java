package com.huizhi.manage.node;

import java.util.List;

public class MessageListNode {
    private int TotCount;
    private List<MessageItemNode> ItemList;

    public int getTotCount() {
        return TotCount;
    }

    public void setTotCount(int totCount) {
        TotCount = totCount;
    }

    public List<MessageItemNode> getItemList() {
        return ItemList;
    }

    public void setItemList(List<MessageItemNode> itemList) {
        ItemList = itemList;
    }

    public class MessageItemNode{
        private String MessageId;
        private String TeacherId;
        private int FunctionType;
        private String BizId;
        private int MessageType;
        private String Message;
        private boolean IsRead;
        private int PhoneType;
        private int OpenType;
        private String TeacherName;
        private String StrReadTime;
        private String StrCreateTime;

        public String getMessageId() {
            return MessageId;
        }

        public void setMessageId(String messageId) {
            MessageId = messageId;
        }

        public String getTeacherId() {
            return TeacherId;
        }

        public void setTeacherId(String teacherId) {
            TeacherId = teacherId;
        }

        public int getFunctionType() {
            return FunctionType;
        }

        public void setFunctionType(int functionType) {
            FunctionType = functionType;
        }

        public String getBizId() {
            return BizId;
        }

        public void setBizId(String bizId) {
            BizId = bizId;
        }

        public int getMessageType() {
            return MessageType;
        }

        public void setMessageType(int messageType) {
            MessageType = messageType;
        }

        public String getMessage() {
            return Message;
        }

        public void setMessage(String message) {
            Message = message;
        }

        public boolean isRead() {
            return IsRead;
        }

        public void setRead(boolean read) {
            IsRead = read;
        }

        public int getPhoneType() {
            return PhoneType;
        }

        public void setPhoneType(int phoneType) {
            PhoneType = phoneType;
        }

        public int getOpenType() {
            return OpenType;
        }

        public void setOpenType(int openType) {
            OpenType = openType;
        }

        public String getTeacherName() {
            return TeacherName;
        }

        public void setTeacherName(String teacherName) {
            TeacherName = teacherName;
        }

        public String getStrReadTime() {
            return StrReadTime;
        }

        public void setStrReadTime(String strReadTime) {
            StrReadTime = strReadTime;
        }

        public String getStrCreateTime() {
            return StrCreateTime;
        }

        public void setStrCreateTime(String strCreateTime) {
            StrCreateTime = strCreateTime;
        }
    }
}
