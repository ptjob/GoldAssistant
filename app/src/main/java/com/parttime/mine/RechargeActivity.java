package com.parttime.mine;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.parttime.base.LocalInitActivity;
import com.parttime.net.BaseRequest;
import com.parttime.net.Callback;
import com.parttime.widget.SingleSelectLayout;
import com.qingmu.jianzhidaren.R;
import com.quark.common.Url;
import com.quark.volley.VolleySington;
import com.thirdparty.alipay.PayResult;
import com.thirdparty.alipay.SignUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cjz on 2015/7/31.
 */
public class RechargeActivity extends LocalInitActivity {
    @ViewInject(R.id.ssl_amount)
    protected SingleSelectLayout sslAmount;
    @ViewInject(R.id.btn_pay)
    protected Button btnPay;

    private String orderId;


    // 商户PID
    public static final String PARTNER = "2088811181647667";
    // 商户收款账号
    public static final String SELLER = "510445519@qq.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICXQIBAAKBgQDF7XxcSoSA/mB4fkiBuCxFk8XDuviW3le7RALN/fJfuvMQDwkNtfLa1xm4HUlIRL6W+kL6+JHDLvIjE+2+wKTS4//m96NwswZjYUO6mPwIfe5rjXdLIx6qDzmRjvZIbv6BlHsEGgAlq4NLnvEdFbnPVE32UvjB9ajhf7sM+Fs95QIDAQABAoGAb7s0rNTUIA15YAvJ2pChTVWyGl/93Qz+8ZPfEXH91NSwSaxzK+4+fhNXTXwa1lUYUhpMnWicwFZMEkk5uKj/YZ9Ly7uarJd4y4VEay3m1RG3BZJJapyunCNDVdUEv9OEafGpa6TbUNoFXpcVIzUVpTJYGBZX/zI2U60K+pvfvLECQQDpezSrF7ZSfzgqTvaTnAQfyxmnb4ym4lktD83wN7iFUnXQ+8UxL8ns3ZpWOi5T77CteDo3X+s7+s6jspNuD2jTAkEA2QRwtGg+HNgUnVcFmndUYgo/e7Igs85QtFS2vz+7j2kGmd9rihO3aD8qn8smYDBLKLjSSgqPTI9khBRhBl0LZwJBAJKLIB2a/nZ9HxV/BkjTjcsewPVUkGVWgD5GQy3Y61nSzdvjins60XR4CpzAW7+XG79lTLTg4VZ+LyCTvvE/fr0CQG3qEs880OC5DE/YaG0grStut1KGGIwZHcUH9vsMY4myDvbWMthfPhBdldATC1/Cdf6tBU0c5hFHuwgubinT7FcCQQCCA59CGj7rcgpqG/V5sIlLjAHyI4gZDU0cemR/e5vF90ZijNr5x2hOPy7a+A3LgfkaKsN95/+7U2nNVu30QzEU";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDF7XxcSoSA/mB4fkiBuCxFk8XDuviW3le7RALN/fJfuvMQDwkNtfLa1xm4HUlIRL6W+kL6+JHDLvIjE+2+wKTS4//m96NwswZjYUO6mPwIfe5rjXdLIx6qDzmRjvZIbv6BlHsEGgAlq4NLnvEdFbnPVE32UvjB9ajhf7sM+Fs95QIDAQAB";

    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_CHECK_FLAG = 2;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 调用服务器进行充值
                        // getServerPayResult();
                        Toast.makeText(RechargeActivity.this, "支付成功",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(RechargeActivity.this, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(RechargeActivity.this, "支付失败",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(RechargeActivity.this, "检查结果为：" + msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        };
    };

    private int rechargeAmount;

    private int[] yuans;
    private String yuan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_recharge);
        ViewUtils.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViews() {
        super.initViews();
        center(R.string.recharge);
        left(TextView.class, R.string.back);
        initRechargeAmount();
    }

    private void initRechargeAmount(){
        String[] amounts = getResources().getStringArray(R.array.recharge_amounts);
        if(amounts != null){
            yuan = getString(R.string.yuan);
            yuans = new int[amounts.length];
            for(int i = 0; i < amounts.length; ++i){
                yuans[i] = Integer.parseInt(amounts[i]);
                sslAmount.add(amounts[i] + yuan);
            }
        }
    }

    protected boolean validate(){
        if(sslAmount.getSelectedindex() < 0){
            showToast(R.string.please_select_recharge_amount);
            return false;
        }
        return true;
    }

    @OnClick(R.id.btn_pay)
    public void pay(View v){
        if(!validate()){
            return;
        }

        showWait(true);
        ;
        rechargeAmount = yuans[sslAmount.getSelectedindex()];
        Map<String, String> params = new HashMap<>();
        params.put("company_id", getCompanyId());
        params.put("charge_money", /*rechargeAmount*/0.1 + "");
        params.put("charge_type", 0 + "");
        new BaseRequest().request(Url.COMPANY_recharge_lproduct, params, VolleySington.getInstance().getRequestQueue(), new Callback() {
            @Override
            public void success(Object obj) throws JSONException {
                showWait(false);
                JSONObject json = (JSONObject) obj;
                orderId = json.getString("out_trade_no");
                aliPay();
            }

            @Override
            public void failed(Object obj) {
                showWait(false);
            }
        });
    }

    public void aliPay() {
        // 订单
        String orderInfo = getOrderInfo(getString(R.string.recharge), getString(R.string.jian_zhe_da_ren_recharge) + rechargeAmount + getString(R.string.yuan), rechargeAmount
                + "");

        // 对订单做RSA 签名
        String sign = sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(RechargeActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content
     *            待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     *
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }

    /**
     * create the order info. 创建订单信息
     *
     */
    public String getOrderInfo(String subject, String body, String price) {
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + orderId + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\""
                + Url.COMPANY_recharge_AliPayAsynNotify + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。

        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    @Override
    protected ViewGroup getLeftWrapper() {
        return null;
    }

    @Override
    protected ViewGroup getRightWrapper() {
        return null;
    }

    @Override
    protected TextView getCenter() {
        return null;
    }
}
