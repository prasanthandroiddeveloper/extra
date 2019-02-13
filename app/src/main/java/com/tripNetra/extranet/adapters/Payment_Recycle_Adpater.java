package com.tripNetra.extranet.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import com.tripNetra.extranet.R;
import com.tripNetra.extranet.payments.Payment_Voucher_Act;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Payment_Recycle_Adpater extends RecyclerView.Adapter<Payment_Recycle_Adpater.ViewHolder> {

    private List<DataAdapter> listAdapter;
    private List<DataAdapter> arrayadap;

    public Payment_Recycle_Adpater(List<DataAdapter> getDataAdter){
        super();
        this.listAdapter = getDataAdter;
        this.arrayadap = getDataAdter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_payment, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataAdapter dadap =  listAdapter.get(position);

        if(dadap.getRefId().equals( "null") || dadap.getRefId().equals("")) {
             holder.RefidTV.setText(R.string.refer);
        }else {
            holder.RefidTV.setText(dadap.getRefId());
        }

        if(dadap.getNeftImps().equals( "null") || dadap.getNeftImps().equals("")) {
            holder.TransferTv.setText(R.string.trans);
        }else {
            holder.TransferTv.setText(dadap.getNeftImps());
        }

        holder.priceTV.setText("₹ "+dadap.getPrice());
        holder.PNRnumTV.setText(dadap.getPnrNo());
        holder.RoomTypeTV.setText(dadap.getRType());

       try {
           SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

           Date d1 = sdf.parse(dadap.getCinDate());
           sdf.applyPattern("dd-MMM-yyyy");
           holder.CinTV.setText(sdf.format(d1));
       } catch (ParseException e) {
           //e.printStackTrace();
           holder.CinTV.setText(dadap.getCinDate());
       }

        if (!dadap.getBookStat().equals( "null") && !dadap.getBookStat().equals("")){
            holder.StatusTV.setTextColor(Color.parseColor("#FF1D7117"));
            holder.StatusTV.setText(R.string.paid);
        }else {
            holder.StatusTV.setTextColor(Color.parseColor("#FFFF0000"));
            holder.StatusTV.setText(R.string.process);
        }


    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView PNRnumTV, RoomTypeTV, CinTV, StatusTV, TransferTv, RefidTV, priceTV;
        private Context context;

        ViewHolder(View iv) {

            super(iv);

            StatusTV = iv.findViewById(R.id.statusTV);
            PNRnumTV = iv.findViewById(R.id.TripTV);
            RoomTypeTV = iv.findViewById(R.id.roomtypeTV);
            CinTV = iv.findViewById(R.id.cinTV);
            RefidTV = iv.findViewById(R.id.refidTV);
            priceTV = iv.findViewById(R.id.priceTV);
            TransferTv = iv.findViewById(R.id.TransferTV);
            context = iv.getContext();

            iv.setOnClickListener(v -> {
                Intent intent = new Intent(context, Payment_Voucher_Act.class);
                intent.putExtra("pnrnumber", listAdapter.get(getAdapterPosition()).getPnrNo());
                context.startActivity(intent);

            });
        }
    }

    @Override
    public int getItemCount() {return listAdapter.size();}

    public Filter getFilter() {

        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listAdapter = arrayadap;
                } else {
                    ArrayList<DataAdapter> filteredList = new ArrayList<>();
                    for (DataAdapter androidVersion : arrayadap) {
                        if (androidVersion.getPnrNo().toLowerCase().contains(charString) || androidVersion.getRefId().toLowerCase().contains(charString)) {
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
