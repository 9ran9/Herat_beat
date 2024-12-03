package com.example.herat_beat.view.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.herat_beat.R;

//自定义CustomProgressDialog
public class CustomProgressDialog extends ProgressDialog {

    private Context context;

    public CustomProgressDialog(Context context) {
        super(context);
        this.context = context;
    }

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.dialoga, null);
        ImageView progress_img = (ImageView)view. findViewById(R.id.iv_bg);
        Animation operatingAnim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_upload_progress);
        //LinearInterpolator lin = new LinearInterpolator();
        // operatingAnim.setInterpolator(lin);
        progress_img.setAnimation(operatingAnim);
        setContentView(view);
    }

    @Override
    public void show() {
        //setCancelable(false);;
        super.show();
    }

}