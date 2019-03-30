package com.kdd_hp_henry_morrison.mci.rescue;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kdd_hp_henry_morrison.mci.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Morrison on 2018/3/26.
 */

public class rescue_adapter extends RecyclerView.Adapter<rescue_adapter.setviewholder> {
    String[] city = {"台北市","新北市","桃園市","台中市","台南市",
                     "高雄市","基隆市","新竹市","嘉義市","新竹縣",
                     "苗栗縣","彰化縣","南投縣","雲林縣","嘉義縣",
                     "屏東縣","宜蘭縣","花蓮縣","台東縣","澎湖縣"};
    private Context context;
    private MListener mlistener;
    private List<rescueclass> data = Collections.emptyList();
    public rescue_adapter(Context context) {
        this.context = context;
    }

    public static interface MListener{
        public void onClick(int postition);
    }
    @Override
    public setviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rescue_card,parent,false);
        return new setviewholder(cardView);
    }

    @Override
    public void onBindViewHolder(setviewholder holder, final int position) {
        CardView cardView = holder.cardView;
        TextView rescue_card_city = cardView.findViewById(R.id.rescue_card_name);
        rescue_card_city.setText(city[position]);
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
        return city.length;
    }
    public void setListnear(MListener mlistnear) {
        this.mlistener = mlistnear;
    }
    public static class setviewholder extends  RecyclerView.ViewHolder{
        private CardView cardView;
        public setviewholder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.rescue_card);

        }
    }
}
