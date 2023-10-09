package com.example.ecommerece.helper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerece.R;

import java.util.List;

public class CategoryButtonAdapter extends RecyclerView.Adapter<CategoryButtonAdapter.CategoryButtonViewHolder> {
    private List<String> categories;
    private CategoryClickListener categoryClickListener;

    public CategoryButtonAdapter(List<String> categories, CategoryClickListener listener) {
        this.categories = categories;
        this.categoryClickListener = listener;
    }

    @Override
    public CategoryButtonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_category_layout, parent, false);
        return new CategoryButtonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryButtonViewHolder holder, int position) {
        final String category = categories.get(position);
        holder.categoryButton.setText(category);

        holder.categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (categoryClickListener != null) {
                    categoryClickListener.onCategoryClick(category);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public interface CategoryClickListener {
        void onCategoryClick(String category);
    }

    public class CategoryButtonViewHolder extends RecyclerView.ViewHolder {
        public Button categoryButton;

        public CategoryButtonViewHolder(View itemView) {
            super(itemView);
            categoryButton = itemView.findViewById(R.id.categoryButton);
        }
    }

}
