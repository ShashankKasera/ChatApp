package com.shashank.root.myapp.common;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shashank.root.myapp.R;
import com.shashank.root.myapp.ui.activity.ShowImageActivity;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.podcopic.animationlib.library.AnimationType;
import com.podcopic.animationlib.library.StartSmartAnimation;
import com.squareup.picasso.Picasso;

public class Profiledialog {

    public DialogPlus dialog;

    public Profiledialog(final Context context , final String image , String name){

        dialog = DialogPlus.newDialog(context)
                .setContentHolder(new ViewHolder(R.layout.profile_dialog_layout))
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {

                    }
                })
                .setGravity(Gravity.CENTER)
                .setContentWidth(CommonUtils.dpToPx(context,300))
                .setContentHeight(CommonUtils.dpToPx(context,350))
                .setExpanded(false)  // This will enable the expand feature, (similar to android L share dialog)
                .create();



        CardView view = (CardView) dialog.getHolderView();




        ImageView imageView = view.findViewById(R.id.profilephoto);
        Picasso.get()
                .load(image)
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ShowImageActivity.class);
                intent.putExtra("image",image);
                context.startActivity(intent);
            }
        });


        TextView Name = view.findViewById(R.id.profilephoto_name);
        Name.setText(name);

        ImageView cancel = view.findViewById(R.id.imageCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        StartSmartAnimation.startAnimation(view.findViewById(R.id.profiledialog),
                AnimationType.FlipInX , 2000 , 0 , true , 300 );

        dialog.show();



    }
    public  void show(){
        dialog.show();
    }

    public  void dismiss(){
        dialog.dismiss();
    }
}