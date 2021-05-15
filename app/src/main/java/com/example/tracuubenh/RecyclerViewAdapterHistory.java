package com.example.tracuubenh;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapterHistory extends RecyclerView.Adapter<RecyclerViewAdapterHistory.HistoryViewHolder> {

    private ArrayList<History> histories;
    private Context context;
    public RecyclerViewAdapterHistory(Context context, ArrayList<History> histories)
    {
        this.histories = histories;
        this.context = context;
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder{
        TextView tenBenh;

        public HistoryViewHolder(View v)
        {
            super(v);
            tenBenh = (TextView) v.findViewById(R.id.tenbenh);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    String text = histories.get(position).getTenbenh();

                    Intent intent = new Intent(context, DinhNghiaBenh.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("tenbenh",text);
                    intent.putExtras(bundle);
                    context.startActivity(intent);

                }
            });
        }

    }
    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item_layout, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, final int position)
    {
        holder.tenBenh.setText(histories.get(position).getTenbenh());
    }
    @Override
    public int getItemCount()
    {
        return  histories.size();
    }
}
