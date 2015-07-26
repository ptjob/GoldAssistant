package com.parttime.constants;

/**
 *
 * Created by luhua on 15/7/12.
 */
public class ActivityExtraAndKeys {

    public static final String USER_ID = "user_id";

    public static final int LOAD_PICTURE = 0x0010;

    public static class ExtraLogin{
        public static String key = "";
    }

    public static class ChatGroupNotice{
        public static String GROUP_NOTICE_CONTENT="Group_Notice_Content";
    }

    public static class GroupSetting{
        public static String ADMITTED = "Admitted"; //已录取
        public static String PENDING = "pending"; //待处理
        public static String GROUPID = "groupId";
        public static String GROUPOWNER = "group_OWNER";
    }

    /**
     * 通讯录
     */
    public static class Addressbook{
        public static String MEMBER = "member";
    }


}
