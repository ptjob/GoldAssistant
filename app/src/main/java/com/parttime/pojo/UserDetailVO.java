package com.parttime.pojo;

/**
 *
 * Created by dehua on 15/7/31.
 */
public class UserDetailVO {

   public String identity_verso;// 14327791466188mwf9x.jpg, varchar(255) COMMENT '身份证照片反面'
    public String note; //: null, 声请加入活动群填写的内容
    public String birthdate; //: 2015-05-28, date COMMENT '生日'
    public String education; //: 初中, varchar(255) COMMENT '学历:小学，初中，高中，职业高中，技校，中专，大专，本科
            //sophistication_before; // : 0,int(11) COMMENT '资料完善度：0-100.上一个记录'
           // app_version: , varchar(255) COMMENT 'app版本'
           // city: 深圳, varchar(255) COMMENT '所属城市'
           // achievement: 0, 用户成就 暂时没有启用
            //sophistication: 92, int(11) UNSIGNED COMMENT '资料完善度：0-100'
            //run_over_activity_title: , varchar(255) COMMENT '放飞机活动标题'
            //language: , varchar(255) COMMENT '擅长语言：普通话，粤语，英语，德语，韩语，法语，日语，
    public int creditworthiness; //: 20, int(11) COMMENT '信誉值:步长为5,半个心'
    public int hipline ; //: -1, int(11) COMMENT '臀围'
    public int  beltline ; //: -1, int(11) COMMENT '腰围'
    public int bust ; //: -1, int(11) COMMENT '胸围'
            //identity: ,varchar(255) COMMENT '身份号'
            //graduate: ggf, varchar(255) COMMENT '毕业学校'
    public String picture_1;//: 14327786399503ia69l.jpg, varchar(255) COMMENT '我的简历第一张照片正面近脸,作为头像'
    public String picture_2; //: 1432778666363mdztgm.jpg,varchar(255) COMMENT '我的简历第2张照片,正面半身'
    public String picture_3; //: ,varchar(255) COMMENT '我的简历第3张照片,正面全身'
    public String picture_4; //: ,varchar(255) COMMENT '我的简历第4张照片,任意个照'
    public String picture_5; //: ,varchar(255) COMMENT '我的简历第5张照片,任意个照'
    public String picture_6; //: ,varchar(255) COMMENT '我的简历第6张照片,任意个照'
            //shoe_weight: 0,int(11) COMMENT '鞋码：33-45间'
            //tip: 1,int(11) COMMENT '每日活动提醒：0-关，1-开'
    public int id; //: 100000724, int(11) COMMENT '用户id，作为数据库id以及环信用户系统id'
    public int earnest_money;//: 0, int(11) COMMENT '诚意金,0-未交，1-已交'
    public int height ;//: 180,int(11) COMMENT '身高:140cm-200cm'
    public String summary; //: gfdffgggtyyhyhhjjjjhgfffffghgfggghhhghhhhh,varchar(10240) COMMENT '经历简述:包括工作经验+性格特长等，认真填写，会增大你的
            //phone_type: ,varchar(255) COMMENT '手机型号'
    public int apply; //: 1, 录取状态（0-没查看，1-已录取，2-、已拒绝，3-已查看）
    public int sex; //: 1,int(11) COMMENT '-1:未知，0：女，1：男'
            //os_version: ,varchar(255) COMMENT '系统版本'
    public String bbh; //: ,varchar(255) COMMENT '三围'
    public String telephone; //: 13112340005,varchar(255) COMMENT '手机号码'
           // head_count: 100,
            //post_time: 2015-05-26 20:26:42, datetime COMMENT '注册时间'
            //check_comment: ,varchar(255) COMMENT '实名认证审核意见'
    public int certification;//: 0,int(11) COMMENT '实名认证，0-未认证，1-已提交认证，2-认证通过,3-认证不通过'
            // run_over_activity_id: null, int(11) COMMENT '放飞机活动id'
            //identity_font: 1432779137471suyg3s.jpg, varchar(255) COMMENT '身份证照片正面'
    public int health_record; //: -1, int(11) COMMENT '是否有健康证：0-无，1-有'
            //money: 0, //decimal(11,2) COMMENT '钱包余额'
    public String name; //: 云测5,
            //cloth_weight: , varchar(11) COMMENT '衣服码：S．M．L．XL．XXL．XXXL'
            //recently_login_time: 2015-07-30 16:03:28, datetime COMMENT '最近登录时间'
    public String comment;//: , 评价：优秀，中评，差评，放飞机 就对应现在的  4-1星
    //status: 2 int(11) COMMENT '0:删除(即为黑户),2-正常使用,3-放飞机,'

}
