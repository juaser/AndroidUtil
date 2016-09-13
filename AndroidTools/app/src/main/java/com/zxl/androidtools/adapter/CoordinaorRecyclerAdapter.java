package com.zxl.androidtools.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxl.androidtools.R;

import java.util.List;
import java.util.Random;

/**
 * @Description:
 * @Author: zxl
 * @Date: 13/9/16 PM2:47.
 */
public class CoordinaorRecyclerAdapter extends RecyclerView.Adapter<CoordinaorRecyclerAdapter.CoordinatorHolder> {
    private Context mContext;
    public List<String> data;

    private int[] backgroudcolor = {R.color.color_red_fe4365,
            R.color.color_red_fc9d9a, R.color.color_yellow_f9cdad,
            R.color.color_green_f9cdad, R.color.color_green_83af9b};
    private int length;

    public CoordinaorRecyclerAdapter(Context mContext, List<String> data) {
        this.mContext = mContext;
        this.data = data;
        length = backgroudcolor.length;
    }

    @Override
    public CoordinatorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CoordinatorHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_card_sample, parent, false));
    }

    @Override
    public void onBindViewHolder(CoordinatorHolder holder, int position) {
        holder.tv.setText(data.get(position));
        int index = new Random().nextInt(length - 1);//new Random().nexInt(10) 0-10之内的随机数
        holder.tv.setBackgroundColor(ContextCompat.getColor(mContext, backgroudcolor[index]));
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class CoordinatorHolder extends RecyclerView.ViewHolder {
        TextView tv;

        public CoordinatorHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv_sample);
            int width = ((Activity) mContext).getWindowManager().getDefaultDisplay().getWidth();
            ViewGroup.LayoutParams layoutParams = tv.getLayoutParams();
            layoutParams.width = width / 3;
            layoutParams.height = (int) (120 + new Random().nextInt(200));
            tv.setLayoutParams(layoutParams);

        }
    }
}
