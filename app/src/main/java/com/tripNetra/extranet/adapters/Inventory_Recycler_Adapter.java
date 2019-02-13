package com.tripNetra.extranet.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tripNetra.extranet.R;
import com.tripNetra.extranet.utils.Image_Loader;
import com.tripNetra.extranet.inventory.Manage_Inventry_Act;

import java.util.ArrayList;

public class Inventory_Recycler_Adapter extends RecyclerView.Adapter<Inventory_Recycler_Adapter.ViewHolder> {

    private ArrayList<DataAdapter> listadapter;

    public Inventory_Recycler_Adapter(ArrayList<DataAdapter> list) {
        this.listadapter = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_roomtype, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataAdapter getData = listadapter.get(position);

        String imageurl = getData.getRImage();
        if(!imageurl.equals( "")){
            new Image_Loader(holder.HotelIV).execute("https://tripnetra.com/cpanel_admin/uploads/hotel_images/"+imageurl);
        }
        holder.RTypeTv.setText(getData.getRType());
        holder.RDescTv.setText(getData.getCinType());

    }

    class ViewHolder extends RecyclerView.ViewHolder  {
        ImageView HotelIV;
        TextView RTypeTv,RDescTv;
        Button ManageBtn;
        Context context;

        ViewHolder(View iv) {
            super(iv);

            HotelIV = iv.findViewById(R.id.roomIV);
            RTypeTv = iv.findViewById(R.id.roomtypeTV);
            RDescTv = iv.findViewById(R.id.roomdescTV);
            ManageBtn = iv.findViewById(R.id.manageBtn);
            context = iv.getContext();

            ManageBtn.setOnClickListener(v ->  {

                DataAdapter dapap = listadapter.get(getAdapterPosition());
                Intent intent = new Intent(context, Manage_Inventry_Act.class);
                intent.putExtra("hotelname", dapap.getHName());
                intent.putExtra("hotelid", dapap.getHotelId());
                intent.putExtra("roomname", dapap.getRType());
                intent.putExtra("hotelroomid", dapap.getRTypeId());

                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {return listadapter.size();}
}