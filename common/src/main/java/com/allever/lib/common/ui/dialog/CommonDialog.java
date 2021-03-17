package com.allever.lib.common.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.allever.lib.common.R;


/**
 * description:自定义dialog
 * @author allever
 */

public class CommonDialog extends Dialog {
    /**
     * 显示的图片
     */
    private ImageView imageIv ;

    /**
     * 显示的标题
     */
    private TextView titleTv ;

    /**
     * 显示的消息
     */
    private TextView messageTv ;

    /**
     * 确认和取消按钮
     */
    private Button btnNegative, btnPositive;

    /**
     * 按钮之间的分割线
     */
    private View columnLineView ;
    public CommonDialog(Context context) {
        super(context, R.style.CommonCustomDialogStyle);
    }

    /**
     * 都是内容数据
     */
    private String message;
    private String title;
    private String positive, negative;
    private int imageResId = -1 ;

    /**
     * 底部是否只有一个按钮
     */
    private boolean isSingle = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_dialog);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        //初始化界面控件
        initView();
        //初始化界面数据
        refreshView();
        //初始化界面控件的事件
        initEvent();
    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( onClickBottomListener!= null) {
                    onClickBottomListener.onPositiveClick();
                }
            }
        });
        //设置取消按钮被点击后，向外界提供监听
        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( onClickBottomListener!= null) {
                    onClickBottomListener.onNegativeClick();
                }
            }
        });
    }

    /**
     * 初始化界面控件的显示数据
     */
    private void refreshView() {
        //如果用户自定了title和message
        if (!TextUtils.isEmpty(title)) {
            titleTv.setText(title);
            titleTv.setVisibility(View.VISIBLE);
        }else {
            titleTv.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(message)) {
            messageTv.setText(message);
        }
        //如果设置按钮的文字
        if (!TextUtils.isEmpty(positive)) {
            btnPositive.setText(positive);
        }else {
            btnPositive.setText("确定");
        }
        if (!TextUtils.isEmpty(negative)) {
            btnNegative.setText(negative);
        }else {
            btnNegative.setText("取消");
        }

        if (imageResId!=-1){
            imageIv.setImageResource(imageResId);
            imageIv.setVisibility(View.VISIBLE);
        }else {
            imageIv.setVisibility(View.GONE);
        }
        /**
         * 只显示一个按钮的时候隐藏取消按钮，回掉只执行确定的事件
         */
        if (isSingle){
            columnLineView.setVisibility(View.GONE);
            btnNegative.setVisibility(View.GONE);
        }else {
            btnNegative.setVisibility(View.VISIBLE);
            columnLineView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void show() {
        super.show();
        refreshView();
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        btnNegative = (Button) findViewById(R.id.negative);
        btnPositive = (Button) findViewById(R.id.positive);
        titleTv = (TextView) findViewById(R.id.title);
        messageTv = (TextView) findViewById(R.id.message);
        imageIv = (ImageView) findViewById(R.id.image);
        columnLineView = findViewById(R.id.column_line);
    }

    /**
     * 设置确定取消按钮的回调
     */
    public OnClickBottomListener onClickBottomListener;
    public CommonDialog setOnClickBottomListener(OnClickBottomListener onClickBottomListener) {
        this.onClickBottomListener = onClickBottomListener;
        return this;
    }
    public interface OnClickBottomListener{
        /**
         * 点击确定按钮事件
         */
        public void onPositiveClick();
        /**
         * 点击取消按钮事件
         */
        public void onNegativeClick();
    }

    public String getMessage() {
        return message;
    }

    public CommonDialog setMessage(String message) {
        this.message = message;
        return this ;
    }

    public String getTitle() {
        return title;
    }

    public CommonDialog setTitle(String title) {
        this.title = title;
        return this ;
    }

    public String getPositive() {
        return positive;
    }

    public CommonDialog setPositive(String positive) {
        this.positive = positive;
        return this ;
    }

    public String getNegative() {
        return negative;
    }

    public CommonDialog setNegative(String negative) {
        this.negative = negative;
        return this ;
    }

    public int getImageResId() {
        return imageResId;
    }

    public boolean isSingle() {
        return isSingle;
    }

    public CommonDialog setSingle(boolean single) {
        isSingle = single;
        return this ;
    }

    public CommonDialog setImageResId(int imageResId) {
        this.imageResId = imageResId;
        return this ;
    }

}
