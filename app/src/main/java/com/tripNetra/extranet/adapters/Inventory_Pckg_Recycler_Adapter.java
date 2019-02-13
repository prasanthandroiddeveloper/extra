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
import com.tripNetra.extranet.inventory.Manage_Inventry_Act;
import com.tripNetra.extranet.inventory.pack_manage_inventry_Act;
import com.tripNetra.extranet.utils.Image_Loader;

import java.util.ArrayList;

public class Inventory_Pckg_Recycler_Adapter extends RecyclerView.Adapter<Inventory_Pckg_Recycler_Adapter.ViewHolder> {

    private ArrayList<DataAdapter> listadapter1;

    public Inventory_Pckg_Recycler_Adapter(ArrayList<DataAdapter> list1) {
        this.listadapter1 = list1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_roomtype, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataAdapter getData = listadapter1.get(position);

       /* String imageurl = getData.getRImage();
        if(!imageurl.equals( "")){
            new Image_Loader(holder.HotelIV).execute("https://tripnetra.com/cpanel_admin/uploads/hotel_images/"+imageurl);
        }*/
        holder.RTypeTv.setText(getData.getDPrice());
        holder.RDescTv.setText(getData.getDCount());

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

                DataAdapter dapap = listadapter1.get(getAdapterPosition());
                Intent intent = new Intent(context, pack_manage_inventry_Act.class);
                intent.putExtra("darshanprice", dapap.getDPrice());
                intent.putExtra("darshancunt", dapap.getDCount());
                intent.putExtra("sightseenid", dapap.getPId());
                intent.putExtra("sightseenname", dapap.getPkgName());

                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {return listadapter1.size();}
}
