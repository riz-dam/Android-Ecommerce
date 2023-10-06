package com.example.ecommerece.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.ecommerece.R;
import com.example.ecommerece.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<Product> productList;
    private Context context;

    public ProductAdapter(Context context) {
        this.productList = new ArrayList<>();
        this.context = context;
    }

    public void addProducts(List<Product> products) {
        productList.addAll(products);
        notifyDataSetChanged();
    }

    public void clearProducts() {
        productList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);

        // Bind product data to the view
        holder.productNameTextView.setText(product.getTitle());
        holder.productPriceTextView.setText("$" + product.getPrice());
        holder.productRatingTextView.setText(Double.toString(product.getRating()));

        // Load product thumbnail image using Glide
        Glide.with(context)
                .load(product.getThumbnail())
                .into(holder.productImageView);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageView;
        TextView productNameTextView;
        TextView productPriceTextView;
        TextView productRatingTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.productImageView);
            productNameTextView = itemView.findViewById(R.id.titleText);
            productPriceTextView = itemView.findViewById(R.id.priceText);
            productRatingTextView = itemView.findViewById(R.id.ratingText);
        }
    }
}
