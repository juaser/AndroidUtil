package com.zxl.androidtools.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxl.androidtools.R;
import com.zxl.androidtools.inter.OnClickPositonListerner;

import java.util.List;

/**
 * @description：
 * @author：zxl
 * @CreateTime 2016/8/22.
 */
public class ShowAppInfoRecyclerAdapter extends RecyclerView.Adapter<ShowAppInfoRecyclerAdapter.MainViewHolder> {
    private List<String> data;
    private Context context;

    public ShowAppInfoRecyclerAdapter(List<String> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_showappinfo_recycle, parent, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, final int position) {
        holder.tv_title.setText(data.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickPositonListerner != null)
                    onClickPositonListerner.clickPosition(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class MainViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private TextView tv_title;

        public MainViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }

    private OnClickPositonListerner onClickPositonListerner;

    public void setOnItemClickListerner(OnClickPositonListerner onClickPositonListerner) {
        this.onClickPositonListerner = onClickPositonListerner;
    }
}
