package com.tripNetra.extranet.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.tripNetra.extranet.R;
import com.tripNetra.extranet.bookings.Book_Voucher_Act;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Booking_Recycle_Adapter extends RecyclerView.Adapter<Booking_Recycle_Adapter.ViewHolder> implements Filterable {

    private List<DataAdapter> listAdapter;
    private List<DataAdapter> arrayadap;
    private Context context;

    public Booking_Recycle_Adapter(List<DataAdapter> apter){
        super();
        this.listAdapter = apter;
        this.arrayadap = apter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_booking, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder vh, int position) {

        final DataAdapter dadap =  listAdapter.get(position);

        vh.NameTV.setText(dadap.getFname());
        vh.MobileTv.setText(dadap.getMobile());
        vh.RoomTypeTV.setText(dadap.getRType());

        if(dadap.getRefId().equals( "COMP")){
            vh.CompTv.setText(R.string.comp);
        }else{
            vh.CompTv.setText("");
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            Date d1 = sdf.parse(dadap.getCinDate());
            Date d2 = sdf.parse(dadap.getCoutDate());
            sdf.applyPattern("dd-MMM-yyyy");
            vh.CinTV.setText(sdf.format(d1));
            vh.CoutTV.setText(sdf.format(d2));

        } catch (ParseException e) {
            //e.printStackTrace();
            vh.CinTV.setText(dadap.getCinDate());
            vh.CoutTV.setText(dadap.getCoutDate());
        }
        String status = dadap.getBookStat().toLowerCase();
        if (status.contains("confirm")) {
            vh.StatusTV.setTextColor(Color.parseColor("#FF1D7117"));
            vh.StatusTV.setText(R.string.confirmed);
        }else if(status.contains(  "cancel")){
            vh.StatusTV.setTextColor(Color.parseColor("#FFFF0000"));
            vh.StatusTV.setText(R.string.cancelled);
        } else{
            vh.StatusTV.setText(dadap.getBookStat());
        }

        vh.MobileTv.setOnClickListener(v ->
                new AlertDialog.Builder(context)
                .setMessage("Do you want to call "+dadap.getFname())
                .setPositiveButton("Yes", (dialog, id) -> {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+dadap.getMobile()));
                    context.startActivity(intent);
                })
                .setNegativeButton("No", (dialog, id) -> { })
                .setCancelable(true).create().show());
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView NameTV, MobileTv, RoomTypeTV,CinTV, CoutTV, StatusTV,CompTv;

        ViewHolder(View itemView) {

            super(itemView);

            NameTV = itemView.findViewById(R.id.nameTv);
            StatusTV = itemView.findViewById(R.id.statusTV);
            MobileTv = itemView.findViewById(R.id.mobileTV);
            RoomTypeTV = itemView.findViewById(R.id.roomtypeTV);
            CinTV = itemView.findViewById(R.id.cinTV);
            CoutTV = itemView.findViewById(R.id.coutTV);
            CompTv = itemView.findViewById(R.id.compTv);
            context = itemView.getContext();

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, Book_Voucher_Act.class);
                intent.putExtra("pnrnumber", listAdapter.get(getAdapterPosition()).getPnrNo());
                intent.putExtra("id", listAdapter.get(getAdapterPosition()).getHotelId());
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return listAdapter.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSeq) {
                String charString = charSeq.toString();
                if (charString.isEmpty()) {
                    listAdapter = arrayadap;
                } else {
                    ArrayList<DataAdapter> filteredList = new ArrayList<>();
                    for (DataAdapter androidVersion : arrayadap) {
                        if (androidVersion.getFname().toLowerCase().contains(charString) || androidVersion.getPnrNo().toLowerCase().contains(charString)
                                || androidVersion.getRefId().toLowerCase().contains(charString)) {
                            filteredList.add(androidVersion);
                        }
                    }
                    listAdapter = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listAdapter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listAdapter = (ArrayList<DataAdapter>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}