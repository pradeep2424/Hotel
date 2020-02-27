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
import com.miracle.dronam.model.CuisineObject;
import com.miracle.dronam.model.ReferralDetails;

import java.util.ArrayList;
import java.util.List;

public class RecycleAdapterReferralPoints extends RecyclerView.Adapter<RecycleAdapterReferralPoints.MyViewHolder> {
    Context context;

    private ArrayList<ReferralDetails> listReferralDetails;

    public RecycleAdapterReferralPoints(Context context, ArrayList<ReferralDetails>  listReferralDetails) {
        this.listReferralDetails = listReferralDetails;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvAmount;

        public MyViewHolder(View view) {
            super(view);
            tvName =  view.findViewById(R.id.tv_name);
            tvAmount = view.findViewById(R.id.tv_amount);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_referral_points_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ReferralDetails referralDetails = listReferralDetails.get(position);

        String name = referralDetails.getFirstName() + " " + referralDetails.getLastName();
        holder.tvName.setText(name);
        holder.tvAmount.setText(referralDetails.getTotalAmount() + " " + context.getString(R.string.rupees));
    }

    @Override
    public int getItemCount() {
        return listReferralDetails.size();
    }
}


