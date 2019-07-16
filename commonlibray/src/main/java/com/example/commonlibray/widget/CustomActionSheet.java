package com.example.commonlibray.widget;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.commonlibray.R;
import com.example.commonlibray.utils.density.DensityUtil;


public class CustomActionSheet extends PopupWindow {

    private ReportListener listener;
    private View contentView;
    private TextView tvCancel;
    private Activity context;
    private boolean isDismissed = false;
    private View layoutContent;
    private String title;
    private TextView tvTitle;
    private int num = 100;

    private CustomActionSheet(View contentView, int width, int height,
                              boolean focusable) {
        super(contentView, width, height, focusable);
    }


    public static CustomActionSheet getInstance(Activity baseActivity) {
        View contentView = LayoutInflater.from(baseActivity).inflate(
                R.layout.action_report_sheet, null);
        final CustomActionSheet actionSheet = new CustomActionSheet(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);

        actionSheet.contentView = contentView;
        actionSheet.context = baseActivity;
        actionSheet.tvTitle = contentView.findViewById(R.id.tv_title);
        actionSheet.layoutContent = contentView.findViewById(R.id.layout_content);
        actionSheet.tvCancel = contentView.findViewById(R.id.tv_cancel);
        actionSheet.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionSheet.dismiss();
            }
        });
        actionSheet.contentView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    actionSheet.dismiss();
                }
                return true;
            }
        });
        actionSheet.layoutContent
                .setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });

        actionSheet.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return actionSheet;
    }

    public void show() {
        if (context.getWindow().isActive()) {
            showAtLocation(context.getWindow().getDecorView(), Gravity.BOTTOM,
                    0, 0);

            Animation animation = AnimationUtils.loadAnimation(context,
                    R.anim.actionsheet_in);
            animation.setFillEnabled(true);
            animation.setFillAfter(true);
            layoutContent.startAnimation(animation);
        } else {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    if (context.getWindow().isActive()) {
                        showAtLocation(context.getWindow().getDecorView(),
                                Gravity.BOTTOM, 0, 0);

                        Animation animation = AnimationUtils.loadAnimation(
                                context, R.anim.actionsheet_in);
                        animation.setFillEnabled(true);
                        animation.setFillAfter(true);
                        layoutContent.startAnimation(animation);
                    }
                }
            }, 600);
        }

    }

    public void dropCancle(){
        tvCancel.setVisibility(View.GONE);
    }

    /**
     * 必须在setItems之前调用
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
        tvTitle.setText(title);
        LinearLayout layoutBtn = contentView.findViewById(R.id.layout_btn);
        layoutBtn.setBackgroundColor(context.getResources().getColor(R.color.white_color));
        tvTitle.setVisibility(View.VISIBLE);
    }

    public void setNum(int num){
        this.num = num;
    }

    public void setItems(String[] items) {
        LinearLayout layoutBtn = contentView.findViewById(R.id.layout_btn);
        int size = items.length;

        int btnHeight = DensityUtil.dp2px(context, 50);

        for (int i = 0; i < size; i++) {
            final int index = i;
            final String item = items[i];
            LayoutParams p = new LayoutParams(LayoutParams.MATCH_PARENT, btnHeight);
            LinearLayout ll = new LinearLayout(context);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            ll.setGravity(Gravity.CENTER);
            ll.setLayoutParams(p);

            TextView btn = new TextView(context);
            btn.setGravity(Gravity.CENTER);
            btn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            btn.setEllipsize(TruncateAt.END);
            btn.setSingleLine(true);

            btn.setText(item);

            if(num == i) {
                btn.setTextColor(context.getResources().getColor(R.color.theme_color));
            }

            LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, btnHeight);
            btn.setLayoutParams(lp);

            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    dismiss();
                    if (listener != null) {
                        listener.onItemClicked(index, item);
                    }
                }
            });

            View view = new View(context);
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    DensityUtil.dp2px(context, 1));
            view.setBackgroundColor(context.getResources().getColor(R.color.divider_color));
            view.setLayoutParams(params);

            ll.addView(btn);
            layoutBtn.addView(ll);
            layoutBtn.addView(view);
        }
    }

    public interface ReportListener {
        void onItemClicked(int index, String item);
    }

    public void setListener(ReportListener listener) {
        this.listener = listener;
    }

    @Override
    public void dismiss() {
        if (isDismissed) {
            return;
        }
        isDismissed = true;
        Animation animation = AnimationUtils.loadAnimation(context,
                R.anim.actionsheet_out);
        animation.setFillEnabled(true);
        animation.setFillAfter(true);
        animation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {

            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                layoutContent.setVisibility(View.INVISIBLE);
                new Handler().post(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            superDismiss();
                        } catch (Exception e) {
                        }
                    }
                });
            }
        });
        layoutContent.startAnimation(animation);
    }

    public void superDismiss() {
        super.dismiss();
    }
}
