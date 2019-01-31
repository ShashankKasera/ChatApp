package com.shashank.root.myapp.common;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.shashank.root.myapp.R;

public class Loader {

    private Dialog dialog;

    public Loader(Context context){
            dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_loader);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            LottieAnimationView animationView = dialog.findViewById(R.id.animation_view);
            animationView.setSpeed(0.9f);
            animationView.setRepeatMode(LottieDrawable.REVERSE);
    }


    public  void show(){
        dialog.show();
    }

    public  void dismiss(){
        dialog.dismiss();
    }



}
