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
import com.miracle.dronam.model.DishObject;

import java.util.List;

public class RecycleAdapterDish extends RecyclerView.Adapter<RecycleAdapterDish.MyViewHolder> {
    Context context;
    private OnRecyclerViewClickListener clickListener;

    private List<DishObject> listDish;

    public RecycleAdapterDish(Context context, List<DishObject> listDish) {
        this.listDish = listDish;
        this.context = context;
    }

    public void setClickListener(OnRecyclerViewClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView dish_name;
        TextView dish_type;
        TextView price;


        public MyViewHolder(View view) {
            super(view);

            image = (ImageView) view.findViewById(R.id.image);
            dish_name = (TextView) view.findViewById(R.id.tv_dishName);
            dish_type = (TextView) view.findViewById(R.id.tv_dishType);
            price = (TextView) view.findViewById(R.id.tv_price);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.onClick(view, getAdapterPosition());
            }
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_dish_user_may_like, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        DishObject movie = listDish.get(position);

        holder.dish_name.setText(movie.getDishName());
        holder.dish_type.setText(movie.getDishCategory());
        holder.price.setText(movie.getDishAmount());
        holder.image.setImageResource(Integer.parseInt(movie.getDishImage()));

//        Glide.with(context)
//                .load(R.drawable.resource_id)
//                .into(imageView);

//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
//                // You can pass your own memory cache implementation
//                .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
//                .build();
//
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .displayer(new RoundedBitmapDisplayer(10)) //rounded corner bitmap
//                .cacheInMemory(true)
//                .cacheOnDisc(true)
//                .build();
//
//        ImageLoader imageLoader = ImageLoader.getInstance();
//        imageLoader.init(config);
//        imageLoader.displayImage("drawable://" + movie.getImage(), holder.image, options);


    }

    @Override
    public int getItemCount() {
        return listDish.size();
    }


}


