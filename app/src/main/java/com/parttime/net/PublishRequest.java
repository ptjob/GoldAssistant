package com.parttime.net;

import com.android.volley.RequestQueue;
import com.parttime.main.MainTabActivity;
import com.parttime.pojo.PartJob;
import com.parttime.utils.ApplicationUtils;
import com.parttime.utils.CheckUtils;
import com.parttime.utils.FormatUtils;
import com.quark.common.Url;

import java.util.HashMap;

/**
 * 发布相关接口
 * Created by wyw on 2015/7/25.
 */
public class PublishRequest extends BaseRequest {
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
}
