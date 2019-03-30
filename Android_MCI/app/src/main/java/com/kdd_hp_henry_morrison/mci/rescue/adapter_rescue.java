package com.kdd_hp_henry_morrison.mci.rescue;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.kdd_hp_henry_morrison.mci.R;

import java.util.Collections;
import java.util.List;

public class adapter_rescue extends RecyclerView.Adapter<adapter_rescue.setviewholder>{
    String[] city = {"台北市","新北市","桃園市","台中市","台南市",
            "高雄市","基隆市","新竹市","嘉義市","新竹縣",
            "苗栗縣","彰化縣","南投縣","雲林縣","嘉義縣",
            "屏東縣","宜蘭縣","花蓮縣","台東縣","澎湖縣"};
    private Context context;
    private List<rescueclass> my_data;
    RequestQueue mQueue ;
    private MListener mlistener;
    public static interface MListener{
        public void onClick(int postition);
    }
    public adapter_rescue(Context context,List<rescueclass> mydata){
        this.context = context;
        this.my_data = mydata;
        this.mQueue = Volley.newRequestQueue(context);
    }


    @Override
    public setviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rescue_card,parent,false);

        return new setviewholder(cv);
    }
    public void add(int position, rescueclass item) {
        my_data.add(position, item);
        notifyItemInserted(position);
    }
    @Override
    public void onBindViewHolder(setviewholder holder, final int position) {
        CardView cardView = holder.cardView;

        holder.rescue_card_name.setText(my_data.get(position).name);
        holder.rescue_card_time.setText("時間:"+my_data.get(position).timestart);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mlistener!=null)
                {
                    mlistener.onClick(position);
                    Log.d("click",""+position);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }
    public void setListnear(adapter_rescue.MListener mlistnear) {
        this.mlistener = mlistnear;
    }
    public static class setviewholder extends  RecyclerView.ViewHolder{
        private CardView cardView;
        private TextView rescue_card_name, rescue_card_time;
        public setviewholder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.rescue_card);
            rescue_card_name = (TextView) cardView.findViewById(R.id.rescue_card_name);
            rescue_card_time = (TextView) cardView.findViewById(R.id.rescue_card_time);


        }
    }
}


