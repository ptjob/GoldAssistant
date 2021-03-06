package com.parttime.net;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.parttime.pojo.JobAuthType;
import com.parttime.pojo.PartJob;
import com.parttime.pojo.SalaryUnit;
import com.parttime.publish.vo.JobBrokerChartsFragmentVo;
import com.parttime.publish.vo.JobBrokerListVo;
import com.parttime.publish.vo.JobManageListVo;
import com.parttime.publish.vo.JobPlazaActivityListVo;
import com.parttime.publish.vo.JobPlazaListVo;
import com.parttime.publish.vo.PublishActivityListVo;
import com.parttime.utils.ApplicationUtils;
import com.parttime.utils.CheckUtils;
import com.qingmu.jianzhidaren.R;
import com.quark.common.Url;
import com.quark.jianzhidaren.ApplicationControl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 发布相关接口
 * Created by wyw on 2015/7/25.
 */
public class PublishRequest extends BaseRequest {

    public final static int PUBLISH_ACTIVITY_LIST_TYPE_RECRUIT = 1;
    public final static int PUBLISH_ACTIVITY_LIST_TYPE_AUDITING = 2;
    public final static int PUBLISH_ACTIVITY_LIST_TYPE_UNDERCARRIAGE = 3;

    public void publish(PartJob partJob, RequestQueue requestQueue, DefaultCallback callback) {
        HashMap<String, String> reqParams = new HashMap<>();
        reqParams.put("company_id", String.valueOf(partJob.companyId));
        reqParams.put("type", partJob.type);
        reqParams.put("title", partJob.title);
        reqParams.put("start_time", partJob.beginTime);
        reqParams.put("end_time", partJob.endTime);
        reqParams.put("city", partJob.city);
        reqParams.put("county", partJob.area);
        reqParams.put("address", partJob.address);
        reqParams.put("pay", String.valueOf(partJob.salary));
        reqParams.put("pay_type", String.valueOf(partJob.salaryUnit.ordinal()));
        reqParams.put("pay_form", partJob.payType);
        int apartSexInt = partJob.apartSex ? 1 : 0;
        reqParams.put("apart_sex", String.valueOf(apartSexInt));
        if (partJob.apartSex) {
            reqParams.put("male_count", String.valueOf(partJob.maleNum));
            reqParams.put("female_count", String.valueOf(partJob.femaleNum));
        } else {
            reqParams.put("head_count", String.valueOf(partJob.headSum));
        }
        reqParams.put("require_info", partJob.workRequire);
        int isShowTelInt = partJob.isShowTel ? 1 : 0;
        reqParams.put("show_telephone", String.valueOf(isShowTelInt));
        if (partJob.isHasMoreRequire()) {
            if (partJob.height != null) {
                reqParams.put("require_height", String.valueOf(partJob.height));
            }
            if (partJob.isHasMeasurements()) {
                reqParams.put("require_bust", String.valueOf(partJob.bust));
                reqParams.put("require_beltline", String.valueOf(partJob.beltline));
                reqParams.put("require_hipline", String.valueOf(partJob.hipline));
            }
            if (partJob.healthProve != null) {
                int healthProveInt = partJob.healthProve ? 1 : 0;
                reqParams.put("require_health_rec", String.valueOf(healthProveInt));
            }
            if (CheckUtils.isEmpty(partJob.language)) {
                reqParams.put("require_language", partJob.language);
            }
        }

//        String url = Url.COMPANY_publish + "?token=" + MainTabActivity.token;
        String url = Url.COMPANY_publish;

        request(url, reqParams, requestQueue, callback);
    }

    /**
     * 我的已发布活动列表
     *
     * @param page  页码号
     * @param count 分页大小
     * @param type  类型（1-招人中，2-审核中，3已下架，null是全部）
     */
    public void publishActivityList(int page, int count, Integer type,
                                    RequestQueue requestQueue, final DefaultCallback callback) {
        int companyId = ApplicationUtils.getLoginId();
        publishActivityList(companyId, page, count, type, requestQueue, callback);
    }

    /**
     * 已发布活动列表
     *
     * @param companyId 指定商家
     * @param page  页码号
     * @param count 分页大小
     * @param type  类型（1-招人中，2-审核中，3已下架，null是全部）
     */
    public void publishActivityList(int companyId, int page, int count, Integer type, RequestQueue requestQueue, final DefaultCallback callback) {
        HashMap<String, String> reqParams = new HashMap<>();
        reqParams.put("company_id", String.valueOf(companyId));
        reqParams.put("pn", String.valueOf(page));
        reqParams.put("page_size", String.valueOf(count));
        if (type != null) {
            reqParams.put("type", String.valueOf(type));
        }

        String url = Url.COMPANY_MyJianzhi_List;
        request(url, reqParams, requestQueue, new Callback() {
            @Override
            public void success(Object obj) throws JSONException {
                JSONObject jsonObject = (JSONObject) obj;
                JSONObject activityPage = jsonObject.getJSONObject("activityPage");
                PublishActivityListVo publishActivityListVo = new PublishActivityListVo();
                publishActivityListVo.pageNumber = activityPage.getInt("pageNumber");
                publishActivityListVo.pageSize = activityPage.getInt("pageSize");
                publishActivityListVo.totlePage = activityPage.getInt("totalPage");
                publishActivityListVo.totleRow = activityPage.getInt("totalRow");
                JSONArray list = activityPage.getJSONArray("list");
                List<JobManageListVo> jobManageListVoList = new ArrayList<>();

                if (list != null) {
                    for (int i = 0; i < list.length(); ++i) {
                        JSONObject listItem = list.getJSONObject(i);
                        JobManageListVo jobManageListVo = new JobManageListVo();
                        jobManageListVo.jobId = listItem.getInt("activity_id");
                        jobManageListVo.jobTitle = listItem.getString("title");
                        jobManageListVo.view = listItem.getInt("view_count");
                        jobManageListVo.hire = listItem.getInt("confirmed_count");
                        jobManageListVoList.add(jobManageListVo);
                    }
                }

                publishActivityListVo.jobManageListVoList = jobManageListVoList;

                callback.success(publishActivityListVo);
            }

            @Override
            public void failed(Object obj) {
                callback.failed(obj);
            }
        });
    }

    public void publishActivityDetail(int jobId, String groupId, RequestQueue requestQueue, final DefaultCallback callback) {
        HashMap<String, String> reqParams = new HashMap<>();
        reqParams.put("company_id", String.valueOf(ApplicationUtils.getLoginId()));
        if (jobId > 0) {
            reqParams.put("activity_id", String.valueOf(jobId));
        } else {
            reqParams.put("group_id", groupId);
        }

        String url = Url.COMPANY_MyJianzhi_detail;

        request(url, reqParams, requestQueue, new Callback() {
            @Override
            public void success(Object obj) throws JSONException {
                JSONObject activityDetail = ((JSONObject) obj).getJSONObject("activityDetail");
                PartJob partJob = new PartJob();
                partJob.id = activityDetail.getInt("activity_id");
                partJob.companyId = activityDetail.getInt("company_id");
                partJob.isStart = activityDetail.getInt("isStart") != 0;
                partJob.companyName = activityDetail.getString("company_name");
                partJob.type = activityDetail.getString("type");
                partJob.title = activityDetail.getString("title");
                partJob.beginTime = activityDetail.getString("start_time");
                partJob.endTime = activityDetail.getString("end_time");
                partJob.area = activityDetail.getString("county");
                partJob.address = activityDetail.getString("address");
                partJob.salary = activityDetail.getInt("pay");
                partJob.salaryUnit = SalaryUnit.parse(activityDetail.getInt("pay_type"));
                partJob.payType = activityDetail.getString("pay_form");
                partJob.apartSex = activityDetail.getInt("apart_sex") != 0;
                if (partJob.apartSex) {
                    partJob.maleNum = activityDetail.getInt("male_count");
                    partJob.femaleNum = activityDetail.getInt("female_count");
                } else {
                    partJob.headSum = activityDetail.getInt("head_count");
                }
                partJob.workRequire = activityDetail.getString("require_info");
                // partJob.isShowTel = activityDetail.get
                partJob.jobAuthType = JobAuthType.parse(activityDetail.getInt("status"));
                partJob.viewCount = activityDetail.getInt("view_count");
                partJob.handCount = activityDetail.getInt("apply_count");
                callback.success(partJob);
            }

            @Override
            public void failed(Object obj) {
                callback.failed(obj);
            }
        });
    }

    public void preRefresh(int jobId, RequestQueue requestQueue, final DefaultCallback callback) {
        HashMap<String, String> reqParams = new HashMap<>();
        reqParams.put("company_id", String.valueOf(ApplicationUtils.getLoginId()));
        reqParams.put("activity_id", String.valueOf(jobId));

        String url = Url.COMPANY_MyJianzhi_previewReflesh;

        request(url, reqParams, requestQueue, callback);
    }

    public void refresh(int jobId, RequestQueue requestQueue, final DefaultCallback callback) {
        HashMap<String, String> reqParams = new HashMap<>();
        reqParams.put("company_id", String.valueOf(ApplicationUtils.getLoginId()));
        reqParams.put("activity_id", String.valueOf(jobId));

        String url = Url.COMPANY_MyJianzhi_reflesh;

        request(url, reqParams, requestQueue, callback);
    }

    public void preUrgent(int jobId, RequestQueue requestQueue, final DefaultCallback callback) {
        HashMap<String, String> reqParams = new HashMap<>();
        reqParams.put("company_id", String.valueOf(ApplicationUtils.getLoginId()));
        reqParams.put("activity_id", String.valueOf(jobId));

        String url = Url.COMPANY_MyJianzhi_preUrgent;

        request(url, reqParams, requestQueue, callback);
    }

    public void setUrgent(int jobId, RequestQueue requestQueue, final DefaultCallback callback) {
        HashMap<String, String> reqParams = new HashMap<>();
        reqParams.put("company_id", String.valueOf(ApplicationUtils.getLoginId()));
        reqParams.put("activity_id", String.valueOf(jobId));

        String url = Url.COMPANY_MyJianzhi_setUrgent;

        request(url, reqParams, requestQueue, callback);
    }

    public void modify(PartJob partJob, RequestQueue requestQueue, DefaultCallback callback) {
        HashMap<String, String> reqParams = new HashMap<>();
        reqParams.put("type", partJob.type);
        reqParams.put("title", partJob.title);
        reqParams.put("start_time", partJob.beginTime);
        reqParams.put("end_time", partJob.endTime);
        reqParams.put("city", partJob.city);
        reqParams.put("county", partJob.area);
        reqParams.put("address", partJob.address);
        reqParams.put("pay", String.valueOf(partJob.salary));
        reqParams.put("pay_type", String.valueOf(partJob.salaryUnit.ordinal()));
        reqParams.put("pay_form", partJob.payType);
        int apartSexInt = partJob.apartSex ? 1 : 0;
        reqParams.put("apart_sex", String.valueOf(apartSexInt));
        if (partJob.apartSex) {
            reqParams.put("male_count", String.valueOf(partJob.maleNum));
            reqParams.put("female_count", String.valueOf(partJob.femaleNum));
        } else {
            reqParams.put("head_count", String.valueOf(partJob.headSum));
        }
        reqParams.put("require_info", partJob.workRequire);
        int isShowTelInt = partJob.isShowTel ? 1 : 0;
        reqParams.put("show_telephone", String.valueOf(isShowTelInt));
        if (partJob.isHasMoreRequire()) {
            if (partJob.height != null) {
                reqParams.put("require_height", String.valueOf(partJob.height));
            }
            if (partJob.isHasMeasurements()) {
                reqParams.put("require_bust", String.valueOf(partJob.bust));
                reqParams.put("require_beltline", String.valueOf(partJob.beltline));
                reqParams.put("require_hipline", String.valueOf(partJob.hipline));
            }
            if (partJob.healthProve != null) {
                int healthProveInt = partJob.healthProve ? 1 : 0;
                reqParams.put("require_health_rec", String.valueOf(healthProveInt));
            }
            if (CheckUtils.isEmpty(partJob.language)) {
                reqParams.put("require_language", partJob.language);
            }
        }

//        String url = Url.COMPANY_publish + "?token=" + MainTabActivity.token;
        String url = Url.COMPANY_MyJianzhi_modifyCommit;

        request(url, reqParams, requestQueue, callback);
    }

    public void shelve(int jobId, RequestQueue requestQueue, final DefaultCallback callback) {
        HashMap<String, String> reqParams = new HashMap<>();
        reqParams.put("company_id", String.valueOf(ApplicationUtils.getLoginId()));
        reqParams.put("activity_id", String.valueOf(jobId));

        String url = Url.COMPANY_MyJianzhi_cancelActivity;

        request(url, reqParams, requestQueue, callback);
    }

    /**
     * 兼职广场
     *
     * @param page  页码号
     * @param count 分页大小
     * @param type  类型（1-招人中，2-审核中，3已下架）
     */
    public void plazaList(int page, int count, int type,
                          RequestQueue requestQueue, final DefaultCallback callback) {
        HashMap<String, String> reqParams = new HashMap<>();
        reqParams.put("city", ApplicationUtils.getCity());
        reqParams.put("pn", String.valueOf(page));
        reqParams.put("page_size", String.valueOf(count));

        String url = Url.COMPANY_Plaza_List;
        request(url, reqParams, requestQueue, new Callback() {
            @Override
            public void success(Object obj) throws JSONException {
                JSONObject jsonObject = (JSONObject) obj;
                JSONObject activityPage = jsonObject.getJSONObject("activityPage");
                JobPlazaActivityListVo publishActivityListVo = new JobPlazaActivityListVo();
                publishActivityListVo.pageNumber = activityPage.getInt("pageNumber");
                publishActivityListVo.pageSize = activityPage.getInt("pageSize");
                publishActivityListVo.totlePage = activityPage.getInt("totalPage");
                publishActivityListVo.totleRow = activityPage.getInt("totalRow");

                String now = jsonObject.getString("now");
                JSONArray list = activityPage.getJSONArray("list");
                List<JobPlazaListVo> jobManageListVoList = new ArrayList<>();

                if (list != null) {
                    for (int i = 0; i < list.length(); ++i) {
                        JSONObject listItem = list.getJSONObject(i);
                        JobPlazaListVo jobManageListVo = new JobPlazaListVo();
                        jobManageListVo.jobId = listItem.getInt("activity_id");
                        jobManageListVo.jobTitle = listItem.getString("title");
                        jobManageListVo.time = JobPlazaListVo.Convertor.convertTime(now, listItem.getString("publish_time"));
                        jobManageListVo.area = listItem.getString("county");
                        jobManageListVo.type = listItem.getString("type");
                        jobManageListVo.typeDrawableId = JobPlazaListVo.Convertor.convertTypeDrawableId(jobManageListVo.type);
                        jobManageListVo.salary = JobPlazaListVo.Convertor.convertSalary(listItem.getInt("pay_type"), listItem.getInt("pay"));
                        jobManageListVo.isGuarantee = listItem.getInt("guarantee") != 0;
                        jobManageListVo.isSuper = listItem.getInt("superJob") != 0;
                        jobManageListVo.isTime = listItem.getString("time_tag").equals(
                                ApplicationControl.getInstance().getString(R.string.job_plaza_time_tag));
                        jobManageListVo.isExpedited = listItem.getInt("urgent") != 0;

                        jobManageListVoList.add(jobManageListVo);
                    }
                }

                publishActivityListVo.jobPlazaListVoList = jobManageListVoList;

                callback.success(publishActivityListVo);
            }

            @Override
            public void failed(Object obj) {
                callback.failed(obj);
            }
        });
    }

    /**
     * 经纪人列表
     *
     * @param page  页码号
     * @param count 分页大小
     * @param type  类型（1-招人中，2-审核中，3已下架）
     */
    public void agentList(int page, int count, int type,
                          RequestQueue requestQueue, final DefaultCallback callback) {
        HashMap<String, String> reqParams = new HashMap<>();
        reqParams.put("city", ApplicationUtils.getCity());
        reqParams.put("pn", String.valueOf(page));
        reqParams.put("page_size", String.valueOf(count));

        String url = Url.COMPANY_MyJianzhi_agentList;
        request(url, reqParams, requestQueue, new Callback() {
            @Override
            public void success(Object obj) throws JSONException {
                JSONObject jsonObject = (JSONObject) obj;
                JSONObject activityPage = jsonObject.getJSONObject("agentPage");
                JobBrokerChartsFragmentVo publishActivityListVo = new JobBrokerChartsFragmentVo();
                publishActivityListVo.pageNumber = activityPage.getInt("pageNumber");
                publishActivityListVo.pageSize = activityPage.getInt("pageSize");
                publishActivityListVo.totlePage = activityPage.getInt("totalPage");
                publishActivityListVo.totleRow = activityPage.getInt("totalRow");

                JSONArray list = activityPage.getJSONArray("list");
                List<JobBrokerListVo> jobManageListVoList = new ArrayList<>();

                if (list != null) {
                    for (int i = 0; i < list.length(); ++i) {
                        JSONObject listItem = list.getJSONObject(i);
                        JobBrokerListVo jobManageListVo = new JobBrokerListVo();
                        jobManageListVo.companyId = listItem.getInt("company_id");
                        jobManageListVo.name = listItem.getString("company_name");
                        jobManageListVo.picInfo = listItem.getString("avatar");
                        jobManageListVo.hireType = listItem.getString("hire_type");
                        jobManageListVo.fans = listItem.getInt("fans");

                        jobManageListVoList.add(jobManageListVo);
                    }
                }

                publishActivityListVo.jobBrokerListVos = jobManageListVoList;

                callback.success(publishActivityListVo);
            }

            @Override
            public void failed(Object obj) {
                callback.failed(obj);
            }
        });
    }

}
