package com.s22010695.limitly.db_helpers;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.s22010695.limitly.R;

import java.util.List;

public class ZoneListAdapter extends RecyclerView.Adapter<ZoneListAdapter.ZoneViewHolder> {

    //declare objects
    private List<ZoneInfoModel> zoneList;
    private ZonesTableHandler dbHelper;

    public ZoneListAdapter(List<ZoneInfoModel> zoneList, ZonesTableHandler dbHelper) {
        this.zoneList = zoneList;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public ZoneListAdapter.ZoneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //create new item for each row
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.zone_row, parent, false);

        return new ZoneListAdapter.ZoneViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ZoneListAdapter.ZoneViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //set data into view given position (zone title, remove icon)
        ZoneInfoModel zone = zoneList.get(position);

        holder.title.setText(zone.getTitle());

        //set delete each zone
        holder.removeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = zone.getId();
                boolean res = dbHelper.deleteZone(id);
                //remove item from list
                if(res){
                    zoneList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, zoneList.size());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return zoneList.size();
    }

    static class ZoneViewHolder extends RecyclerView.ViewHolder{

        //declare objects
        TextView title;
        ImageView removeIcon;

        public ZoneViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            removeIcon = itemView.findViewById(R.id.removeIcon);
        }
    }
}
