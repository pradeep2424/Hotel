package com.miracle.dronam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.miracle.dronam.R;
import com.miracle.dronam.listeners.OnRecyclerViewClickListener;
import com.miracle.dronam.model.AddressDetails;
import com.miracle.dronam.model.ProfileObject;

import java.util.ArrayList;


/**
 * Created by Pradeep on 01-Sep-2019.
 */

public class RecycleAdapterAddresses extends RecyclerView.Adapter<RecycleAdapterAddresses.ViewHolder> {

    private Context context;
    private ArrayList<AddressDetails> listAddress;

    private OnRecyclerViewClickListener clickListener;

    public RecycleAdapterAddresses(Context context, ArrayList<AddressDetails> listAddress) {
        this.context = context;
        this.listAddress = listAddress;
    }

    public void setClickListener(OnRecyclerViewClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_manage_addresses,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AddressDetails addressDetails = listAddress.get(position);

//        holder.ivIcon.setImageResource(addressDetails.getIcon());

        holder.tvAddressTitle.setText("Home");
        holder.tvAddressText.setText(addressDetails.getAddress());

    }

    @Override
    public int getItemCount() {return listAddress.size();}

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivIcon;
        TextView tvAddressTitle;
        TextView tvAddressText;
        TextView tvEdit;
        TextView tvDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvAddressTitle = itemView.findViewById(R.id.tv_addressTitle);
            tvAddressText = itemView.findViewById(R.id.tv_addressText);
            tvEdit = itemView.findViewById(R.id.tv_editAddress);
            tvDelete = itemView.findViewById(R.id.tv_deleteAddress);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.onClick(view, getAdapterPosition());
            }
        }
    }
}
