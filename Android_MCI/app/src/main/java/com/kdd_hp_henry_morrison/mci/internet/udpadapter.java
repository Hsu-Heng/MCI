package com.kdd_hp_henry_morrison.mci.internet;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.kdd_hp_henry_morrison.mci.R;
import com.kdd_hp_henry_morrison.mci.rescue.rescue_adapter;
import com.kdd_hp_henry_morrison.mci.rescue.rescueclass;

import java.util.Collections;
import java.util.List;

public class udpadapter extends RecyclerView.Adapter<udpadapter.setviewholder>  {
    private Context context;
    private rescue_adapter.MListener mlistener;
    private List<devices> data = Collections.emptyList();
    public udpadapter(Context context,List<devices> data ) {
        this.context = context;
        this.data = data;
    }

    @Override
    public udpadapter.setviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.uploadcard,parent,false);
        return new udpadapter.setviewholder(cardView);
    }

    @Override
    public void onBindViewHolder(udpadapter.setviewholder holder, int position) {
        CardView cardView = holder.cardView;
        TextView upload_devicestatus = cardView.findViewById(R.id.upload_devicestatus);
        switch(data.get(position).getStatus()){
            case 1:
                upload_devicestatus.setBackground(ContextCompat.getDrawable(context, R.drawable.status_green));
            case 2:
                upload_devicestatus.setBackground(ContextCompat.getDrawable(context, R.drawable.status_yellow));
            case 3:
                upload_devicestatus.setBackground(ContextCompat.getDrawable(context, R.drawable.status_red));
            case 4:
                upload_devicestatus.setBackground(ContextCompat.getDrawable(context, R.drawable.status_black));
            default:
                upload_devicestatus.setBackground(ContextCompat.getDrawable(context, R.drawable.status_green));
        }
        TextView upload_deviceid = cardView.findViewById(R.id.upload_deviceid);
        upload_deviceid.setText("裝置:"+data.get(position).id);
        TextView upload_name = cardView.findViewById(R.id.upload_name);
        if(data.get(position).getName()!=""){
            upload_name.setText("姓名:"+data.get(position).getName());
        }
        else if(data.get(position).getName()==""){
            upload_name.setText("姓名:請輸入");
        }
        TextView upload_sex = cardView.findViewById(R.id.upload_sex);
        if(data.get(position).getSex()!=""){
            upload_sex.setText("性別:"+data.get(position).getSex());
        }
        else if(data.get(position).getSex()==""){
            upload_sex.setText("性別:請輸入");
        }
        TextView upload_age = cardView.findViewById(R.id.upload_age);
        if(data.get(position).getAge()!=""){
            upload_age.setText("年齡:"+data.get(position).getAge());
        }
        else if(data.get(position).getAge()==""){
            upload_age.setText("年齡:請輸入");
        }
        TextView upload_perfusion = cardView.findViewById(R.id.upload_perfusion);
        if(data.get(position).getPerfusion()!=""){
            upload_perfusion.setText("血氧:"+data.get(position).getPerfusion());
        }
        TextView upload_respirations = cardView.findViewById(R.id.upload_respirations);
        if(data.get(position).getRespirations()!=""){
            upload_respirations.setText("心跳:"+data.get(position).getRespirations());
        }
        TextView upload_status = cardView.findViewById(R.id.upload_status);
        if(data.get(position).isUpload()){
            upload_status.setBackground(ContextCompat.getDrawable(context, R.drawable.upload_green));

        }
        else if(data.get(position).isUpload()==false){
            upload_status.setBackground(ContextCompat.getDrawable(context, R.drawable.upload_red));
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public static class setviewholder extends  RecyclerView.ViewHolder{
        private CardView cardView;
        public setviewholder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.upload_card);

        }
    }
}
