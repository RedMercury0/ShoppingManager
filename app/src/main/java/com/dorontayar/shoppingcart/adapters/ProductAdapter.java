package com.dorontayar.shoppingcart.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dorontayar.shoppingcart.R;
import com.dorontayar.shoppingcart.model.Product;


import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private OnItemClickListener listener;

    public ProductAdapter(List<Product> productList, OnItemClickListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product, listener);
        holder.textViewProductQuantity.setText(String.valueOf(product.getQuantity()));

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Product product);
        void onUpdateClick(Product product, int quantity);
        void onDeleteClick(Product product);
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewProductName;
        private TextView textViewProductQuantity;
        private Button buttonIncrease;
        private Button buttonDecrease;
        private Button buttonDelete;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewProductName = itemView.findViewById(R.id.textViewProductName);
            textViewProductQuantity = itemView.findViewById(R.id.textViewProductQuantity);
            buttonIncrease = itemView.findViewById(R.id.buttonIncrease);
            buttonDecrease = itemView.findViewById(R.id.buttonDecrease);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }

        void bind(final Product product, final OnItemClickListener listener) {
            textViewProductName.setText(product.getName());
            textViewProductQuantity.setText(String.valueOf(product.getQuantity()));

            buttonIncrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onUpdateClick(product, product.getQuantity() + 1);
                }
            });

            buttonDecrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onUpdateClick(product, product.getQuantity() - 1);
                }
            });

            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDeleteClick(product);
                }
            });
        }
    }

}


