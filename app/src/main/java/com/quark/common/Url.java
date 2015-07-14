package com.quark.common;

/**
 * 
 * @ClassName: Url
 * @Description: 所有接口的地址都用这个类来拼接，以免服务器地址变动，需要修改所有的地址
 * @author howe
 * @date 2015-1-9 上午10:18:35
 * 
 */

public class Url {

	// 服务器地址及端口
	// public static String HOST = "http://app.jobdiy.cn/";
	public static String HOST = "http://120.24.215.117/";
	public static String HOST2 = "https://120.24.215.117/";
	// public static String HOST2 = "https://app.jobdiy.cn/";
	// 项目名称
	private static final String SUBJECT = "app/v2_0";
	// 获取图片url
	public static final String GETPIC = "http://jzdr-test.oss-cn-shenzhen.aliyuncs.com/";
	//public static final String GETPIC = "http://jzdr-pic.oss-cn-shenzhen.aliyuncs.com/";
	// 8 用户登录
	public static final String USER_LOGIN = HOST + SUBJECT + "/UserCenter/in";
	// 9 用户注册
	public static final String USER_REGIST = HOST + SUBJECT
			+ "/UserCenter/registe";
	// 用户 注册发送验证码
	public static final String USER_SENDMSM = HOST + SUBJECT
			+ "/Message/send_user_regist_code";
	// 9 商家注册
	public static final String COMPANY_REGIST = HOST + SUBJECT
			+ "/AgentCenter/registe";
	// 8 商家登录 and 经纪人登陆
    public static final String COMPANY_LOGIN = HOST + SUBJECT
			+"/CompanyCenter/in";
	// 商家 注册发送验证码
	public static final String COMPANY_SENDMSM = HOST + SUBJECT
			+ "/Message/send_company_regist_code";

	// 商家忘记密码
	public static final String COMPANY_FORGETPASSWORD = HOST + SUBJECT
			+ "/AgentCenter/forgetPassword";
	// 用户忘记密码
	public static final String USER_FORGETPASSWORD = HOST + SUBJECT
			+ "/UserCenter/forgetPassword";
	// 用户忘记密码 发送验证码
	public static final String USER_SENDMSM_FORGETPASSWORD = HOST + SUBJECT
			+ "/Message/send_user_code";
	// 商家忘记密码 发送验证码
	public static final String COMPANY_SENDMSM_FORGETPASSWORD = HOST + SUBJECT
			+ "/Message/send_company_code";
	// 商家修改密码
	public static final String COMPANY_EDITPASSWORD = HOST + SUBJECT
			+ "/AgentCenter/modifyPassword";
	// 用户修改密码
	public static final String USER_EDITPASSWORD = HOST + SUBJECT
			+ "/UserCenter/modifyPassword";
	// 用户修改密码 发送验证码
	public static final String USER_SENDMSM_EDITPASSWORD = HOST + SUBJECT
			+ "/Message/send_company_code";
	// 商家修改密码 发送验证码
	public static final String COMPANY_SENDMSM_EDITPASSWORD = HOST + SUBJECT
			+ "/Message/send_user_code";

	// 商家修改手机号码
	public static final String COMPANY_modifyTelephon = HOST + SUBJECT
			+ "/AgentCenter/modifyTelephone";
	// 用户修改手机号码
	public static final String USER_modifyTelephon = HOST + SUBJECT
			+ "/UserCenter/modifyTelephone";
	// 商家修改手机发送验证码
	public static final String COMPANY_SEND_TEL_CODE = HOST + SUBJECT
			+ "/Message/send_company_tel_code";
	// 用户修改手机发送验证码
	public static final String USER_SEND_TEL_CODE = HOST + SUBJECT
			+ "/Message/send_user_tel_code";

	// 商家反馈建议
	public static final String COMPANY_coment = HOST + SUBJECT
			+ "/AgentCenter/feed_back";
	// 用户反馈建议
	public static final String USER_coment = HOST + SUBJECT
			+ "/UserCenter/feed_back";

	// 商家验证资料
	public static final String COMPANY_yanzheng = HOST + SUBJECT
			+ "/AgentCenter/company_certificatoin";
	// 用户验证资料
	public static final String USER_yanzheng = HOST + SUBJECT
			+ "/UserCenter/show_authentication";

	// 用户上传正面身份证
	public static final String USER_uploadIdcard_zheng = HOST + SUBJECT
			+ "/UserCenter/upload_identity_font";
	// 用户上传反面身份证图片
	public static final String USER_uploadIdcard_fan = HOST + SUBJECT
			+ "/UserCenter/upload_identity_verso";
	// 用户审核提交
	public static final String USER_shenheSubmit = HOST + SUBJECT
			+ "/UserCenter/commit_authentication";

	// 商家身份证正面图片
	public static final String COMPANY_uploadIdcard_zheng = HOST + SUBJECT
			+ "/AgentCenter/upload_identity_front";
	// 商家身份证反面图片
	public static final String COMPANY_uploadIdcard_fan = HOST + SUBJECT
			+ "/AgentCenter/upload_identity_verso";
	// 商家营业执照图片
	public static final String COMPANY_uploadyinyzz = HOST + SUBJECT
			+ "/AgentCenter/upload_company_picture";
	// 商家审核提交
	public static final String COMPANY_shenheSubmit = HOST + SUBJECT
			+ "/AgentCenter/commit_authentication";
	// 31 展示我的简历
	public static final String USER_jianli_show = HOST + SUBJECT
			+ "/UserCenter/show_resume";
	// 32 用户更新简历
	public static final String USER_jianli_submit = HOST + SUBJECT
			+ "/UserCenter/update_resume";
	// 29 上传我的照片
	public static final String USER_jianli_uploadmypic = HOST + SUBJECT
			+ "/UserCenter/upload_my_avatar";
	// 30 删除我的照片
	public static final String USER_jianli_deletemypic = HOST + SUBJECT
			+ "/UserCenter/delete_my_avatar";
	// 38 简历预览 诚意金 心数
	public static final String USER_jianli_scan_nav = HOST + SUBJECT
			+ "/UserCenter/snapshot";
	// 37 简历预览 评论列表
	public static final String USER_jianli_scan_comment = HOST + SUBJECT
			+ "/UserCenter/comment";

	// 检查是否可以发布兼职
	public static final String COMPANY_availability = HOST + SUBJECT
			+ "/Activity/availability";
	// 发布兼职
	public static final String COMPANY_publish = HOST + SUBJECT
			+ "/Activity/publish";
	// 兼职管理列表
	public static final String COMPANY_MyJianzhi_List = HOST + SUBJECT
			+ "/Activity/publishActivityList";
	// 管理 –兼职达人列表 – 兼职详情
	public static final String COMPANY_MyJianzhi_detail = HOST + SUBJECT
			+ "/Activity/publishActivityDetail";
	// 管理 –刷新前
	public static final String COMPANY_MyJianzhi_previewReflesh = HOST
			+ SUBJECT + "/Activity/preReflesh";
	// 管理 –刷新
	public static final String COMPANY_MyJianzhi_reflesh = HOST + SUBJECT
			+ "/Activity/reflesh";
	// 管理 –取消兼职
	public static final String COMPANY_MyJianzhi_cancelActivity = HOST
			+ SUBJECT + "/Activity/cancelActivity";
	// 管理 –重新上架
	public static final String COMPANY_MyJianzhi_republish = HOST + SUBJECT
			+ "/Activity/republish";
	// 管理 –修改兼职
	public static final String COMPANY_MyJianzhi_modify = HOST + SUBJECT
			+ "/Activity/modify";
	// 管理 –修改发布兼职
	public static final String COMPANY_MyJianzhi_modifyCommit = HOST + SUBJECT
			+ "/Activity/modifyCommit";
	// 充值记录
	public static final String COMPANY_recharge_log = HOST + SUBJECT
			+ "/AgentCenter/chargeLog";
	// 获取订单号
	public static final String COMPANY_recharge_lproduct = HOST + SUBJECT
			+ "/AgentCenter/product";
	// 同步充值 结果
	public static final String COMPANY_recharge_AliPay = HOST + SUBJECT
			+ "/AgentCenter/AliPay";
	// 异步通知
	public static final String COMPANY_recharge_AliPayAsynNotify = HOST
			+ SUBJECT + "/AgentCenter/AliPayAsynNotify";
	// 广场->兼职详细
	public static final String COMPANY_applyActivityDetail = HOST + SUBJECT
			+ "/Activity/applyActivityDetail";
	// 广场列表
	public static final String COMPANY_applyActivityList = HOST + SUBJECT
			+ "/Activity/list";
	// 广场列表 搜索
	public static final String COMPANY_applysearchActivityList = HOST + SUBJECT
			+ "/Activity/search";
	// 商家花名册列表
	public static final String COMPANY_roster = HOST + SUBJECT
			+ "/Activity/facebook";
	// 广场 筛选
	public static final String COMPANY_filter = HOST + SUBJECT
			+ "/Activity/filter";
	// 申请兼职
	public static final String COMPANY_apply = HOST + SUBJECT
			+ "/Activity/apply";
	// 查看报名
	public static final String COMPANY_checkApply = HOST + SUBJECT
			+ "/Activity/checkApply";
	// 查看报名
	public static final String COMPANY_applicantInfo = HOST + SUBJECT
			+ "/Activity/applicantInfo";
	// 通过报名
	public static final String COMPANY_approveActivity = HOST + SUBJECT
			+ "/Activity/approveActivity";
	// 拒绝报名
	public static final String COMPANY_rejectActivity = HOST + SUBJECT
			+ "/Activity/rejectActivity";
	// 花名册 取消报名
	public static final String COMPANY_cancelApply = HOST + SUBJECT
			+ "/Activity/cancelApply";
	// 花名册 商家端：花名册 – 提交评论/取消报名申请
	public static final String COMPANY_commentRequirer = HOST + SUBJECT
			+ "/Activity/commentRequirer";
	// 花名册 – 花名册列表 –活动人员列表/评价人员
	public static final String COMPANY_activityFaceBook = HOST + SUBJECT
			+ "/Activity/activityFaceBook";
	// 商家端 二维码签到
	public static final String COMPANY_sign = HOST + SUBJECT + "/Activity/sign";
	// 拒绝报名
	public static final String COMPANY_userCancelActivityApply = HOST + SUBJECT
			+ "/Activity/userCancelActivityApply";
	// 我的兼职列表
	public static final String COMPANY_requireActivity = HOST + SUBJECT
			+ "/Activity/requireActivity";

	// 用户端每日提醒
	public static final String USE_setTip = HOST + SUBJECT
			+ "/UserCenter/setTip";
	// 用户端我的个人资料
	public static final String USE_me = HOST + SUBJECT + "/UserCenter/me";

	// 获取发送短信内容
	public static final String COMPANY_sendMSM = HOST + SUBJECT
			+ "/UserCenter/sendMSM";

	// 获取订单号
	public static final String USER__recharge_product = HOST + SUBJECT
			+ "/UserCenter/product";
	// 同步调用
	public static final String USER__recharge_AliPay = HOST + SUBJECT
			+ "/UserCenter/AliPay";
	// 异步调用
	public static final String USER__recharge_AliPayAsynNotify = HOST + SUBJECT
			+ "/UserCenter/AliPayAsynNotify";
	// 商家端 -功能
	public static final String COMPANY_function = HOST + SUBJECT
			+ "/AgentCenter/function";
	// 放飞机基金
	public static final String moneyPool = HOST + SUBJECT
			+ "/Activity/moneyPool";
	// 用户端每日提醒
	public static final String USE_setting = HOST + SUBJECT
			+ "/UserCenter/setting";
	// 用户二维码签到
	public static final String USE_signUp = HOST + SUBJECT + "/Activity/signUp";
	// 商家上传头像
	public static final String COMPANY_upload_avatar = HOST + SUBJECT
			+ "/AgentCenter/upload_avatar";

	// 商家端 二维码签到列表
	public static final String COMPANY_signUpList = HOST + SUBJECT
			+ "/Activity/signUpList";

	// 商家具体一个活动的签到列表(carson add)
	public static final String COMPANY_ACTIVITY_SIGNLIST = HOST + SUBJECT
			+ "/Activity/signList";

	// 我的兼职成就
	public static final String COMPANY_achievement = HOST + SUBJECT
			+ "/UserCenter/achievement";
	// 环信查找
	public static final String HUANXIN_search = HOST + SUBJECT
			+ "/Huanxin/search";
	// 环信头像昵称
	public static final String HUANXIN_avatars_pic = HOST + SUBJECT
			+ "/Huanxin/avatars";
	// 环信用户信息
	public static final String HUANXIN_user_info = HOST + SUBJECT
			+ "/Huanxin/user_info";
	// 检查是否放飞机
	public static final String USER_user_feiji = HOST + SUBJECT
			+ "/UserCenter/is_run_over";
	// 发送邮件
	public static final String USER_send_mail = HOST + SUBJECT
			+ "/Message/send_mail";
	// 切换城市,上传城市位置 用户
	public static final String CHANGE_CITY_USER = HOST + SUBJECT
			+ "/UserCenter/change_city";
	// 切换城市,上传城市位置商家
	public static final String CHANGE_CITY_CUSTOM = HOST + SUBJECT
			+ "/AgentCenter/change_city";
	// 投诉
	public static final String USER_TOUSU = HOST + SUBJECT
			+ "/Activity/complain";
	// 收藏兼职
	public static final String USER_COLLECT = HOST + SUBJECT
			+ "/Activity/collect";
	// 取消收藏
	public static final String USER_CANCEL_COLLECT = HOST + SUBJECT
			+ "/Activity/cancel_collect";
	// 收藏列表
	public static final String USER_COLLECT_ACTIVITY = HOST + SUBJECT
			+ "/Activity/collectedActivity";
	// 判断是否更新APK
	public static final String IS_UPDATE_APK = HOST + SUBJECT
			+ "/Activity/apkInfo";
	// 商家取消已录取人员
	public static final String COMPANY_CANCEL_REQUIRED = HOST + SUBJECT
			+ "/Activity/cancel_require";
	// 拉黑商家
	public static final String COMPANY_FORBIDDEN = HOST + SUBJECT
			+ "/AgentCenter/is_forbidden";
	// 设置商家消息免打扰
	public static final String COMPANY_MIANDARAO = HOST + SUBJECT
			+ "/AgentCenter/setDisturb";
	// 获取商家消息免打扰状态
	public static final String COMPANY_MIANDARAO_STATUS = HOST + SUBJECT
			+ "/AgentCenter/setting";
	// 校验验证码是否正确
	public static final String MESSAGE_VALIDATE = HOST + SUBJECT
			+ "/Message/validate";
	// 获取服务端好友列表
	public static final String FRIEND_LIST = HOST + SUBJECT
			+ "/Activity/friendList";
	// 经纪人首页获取红点状态
	public static final String BROKER_MAIN_PAGE = HOST + SUBJECT
			+ "/AgentCenter/mainPage";
	// 经纪人排行榜
	public static final String BROKER_LIST = HOST + SUBJECT
			+ "/AgentCenter/agentList";
	// 我的粉丝
	public static final String MY_FOLLOWERS_LIST = HOST + SUBJECT
			+ "/AgentCenter/myFollowers";
	// 接受活动
	public static final String ACCEPT_ACT_LIST = HOST + SUBJECT
			+ "/AgentCenter/activityList";
	// 导出报名人员名单
	public static final String EXPORT_GROUP_LIST = HOST + SUBJECT
			+ "/Activity/getGroupExcel";
	// 踢人、或者解散群组访问服务器给推送
	public static final String REMOVE_FROM_GROUP = HOST + SUBJECT
			+ "/Activity/kickNotify";

	// 我的钱包
	// 取回诚意金
	public static final String TAKE_EARNEST_MONEY = HOST2 + SUBJECT
			+ "/UserCenter/take_earnest_moeny";

	// 获取token
	public static final String USER_GET_TOKEN = HOST2 + SUBJECT
			+ "/UserCenter/getToken";
	// 账单流水记录
	public static final String USER_LIST_BILL = HOST2 + SUBJECT
			+ "/UserCenter/list_bill";
	// 账户列表
	public static final String USER_LIST_ACCOUNT = HOST2 + SUBJECT
			+ "/UserCenter/list_account";
	// 删除帐号
	public static final String USER_DELETE_ACCOUNT = HOST2 + SUBJECT
			+ "/UserCenter/delete_account";
	// 提现
	public static final String USER_DRAW_MONEY = HOST2 + SUBJECT
			+ "/UserCenter/draw_money";

}
