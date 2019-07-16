package com.example.commonlibray.utils.time;

import android.app.Activity;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;


/**
 * 倒计时
 */
public class CodeTimeUtils {

    public static final long COUNT_DOWN_TIME = 1000;
    private TimeCount time;
    private TextView sendCodeBtn;
    private Activity activity;

    public CodeTimeUtils(Activity activity, TextView sendBtn, long remainTime) {
        this.activity = activity;
        this.sendCodeBtn = sendBtn;
        this.time = new TimeCount(remainTime * COUNT_DOWN_TIME, COUNT_DOWN_TIME);
    }

    public void start() {
        if (sendCodeBtn != null && time != null) {
            time.start();
            sendCodeBtn.setEnabled(false);
        }
    }

    public class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    sendCodeBtn.setText("重新发送");
                    sendCodeBtn.setEnabled(true);
                }
            });
        }

        @Override
        public void onTick(final long millisUntilFinished) {//计时过程显示
            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    sendCodeBtn.setVisibility(View.VISIBLE);
                    sendCodeBtn.setText(millisUntilFinished / 1000 + "s后重新获取");
                }
            });

        }
    }

    public void cancel() {
        if (sendCodeBtn != null && time != null) {
            time.cancel();
        }
    }
}
