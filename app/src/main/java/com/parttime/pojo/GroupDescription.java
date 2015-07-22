package com.parttime.pojo;

/**
 *
 * Created by dehua on 15/7/22.
 */
public class GroupDescription {
    public static final int ACTIVITY_GROUP = 1; //活动群
    public static final int ACTIVITY_CONSULTATION_GROUP = 2; //以前活动创建的群
    public static final int ACTIVITY_ADDRESSBOOK_GROUP = 3; //通讯录创建的群

    public int type; //1: 活动群
    public String info;
}
