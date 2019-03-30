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

import java.util.List;

public class adapter_card_device extends RecyclerView.Adapter<adapter_card_device.setviewholder>{
    private Context context;
    private List<device_card> my_data;
    RequestQueue mQueue ;

    public void setListnear(adapter_rescue.MListener mlistnear) {
        this.mlistener = mlistnear;
    }

    public adapter_card_device(Context context, List<device_card> mydata){
        this.context = context;
        this.my_data = mydata;
        this.mQueue = Volley.newRequestQueue(context);

    }
    private adapter_rescue.MListener mlistener;
    public static interface MListener{
        public void onClick(int postition);
    }
    @Override
    public adapter_card_device.setviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rescuecard_device,parent,false);

        return new adapter_card_device.setviewholder(cv);
    }
    public void add(int position, device_card item) {
        my_data.add(position, item);
        notifyItemInserted(position);
    }
    @Override
    public void onBindViewHolder(adapter_card_device.setviewholder holder, final int position) {
        CardView cardView = holder.cardView;

        holder.rescue_card_devicename.setText(my_data.get(position).DeviceId);
        holder.rescue_card_statue.setText("狀態"+String.valueOf(my_data.get(position).status));
        cardView.setOnClickListener(new View.OnClickListener() {
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

    public static class setviewholder extends  RecyclerView.ViewHolder{
        private CardView cardView;
        private TextView rescue_card_devicename, rescue_card_statue;
        public setviewholder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.rescue_card_device);
            rescue_card_devicename = (TextView) cardView.findViewById(R.id.rescue_card_device_name);
            rescue_card_statue = (TextView) cardView.findViewById(R.id.rescue_card_device_status);


        }
    }
}
