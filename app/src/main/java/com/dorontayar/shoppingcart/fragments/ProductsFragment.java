package com.dorontayar.shoppingcart.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dorontayar.shoppingcart.R;
import com.dorontayar.shoppingcart.adapters.ProductAdapter;
import com.dorontayar.shoppingcart.model.Product;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText editTextProductName;
    private EditText editTextQuantity;
    private Button buttonAdd;
    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    // Firebase
    private DatabaseReference databaseReference;
    private String username;



    public ProductsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductsFragment newInstance(String param1, String param2) {
        ProductsFragment fragment = new ProductsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // Ensure that the parent fragment is MainScreenFragment
        if (getParentFragment() != null && getParentFragment() instanceof MainScreenFragment) {
            // Retrieve the username from MainScreenFragment
            username = ((MainScreenFragment) getParentFragment()).getUsername();
        }

        // Initialize Firebase Database with the username if available
        if (username != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("users").child(username).child("groceryList");
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        editTextProductName = view.findViewById(R.id.editTextProductName);
        editTextQuantity = view.findViewById(R.id.editTextQuantity);
        buttonAdd = view.findViewById(R.id.buttonAdd);
        recyclerViewProducts = view.findViewById(R.id.recyclerViewProducts);

        // Check if the parent fragment is MainScreenFragment and it has a valid username
        if (getParentFragment() != null && getParentFragment() instanceof MainScreenFragment) {
            MainScreenFragment mainScreenFragment = (MainScreenFragment) getParentFragment();
            username = mainScreenFragment.getUsername();

            // Proceed with Firebase initialization if username is available
            if (username != null) {
                databaseReference = FirebaseDatabase.getInstance().getReference("users").child(username).child("groceryList");
            } else {
                // Handle the case where username is null
                Toast.makeText(getContext(), "Username is null", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle the case where MainScreenFragment is not found
            Toast.makeText(getContext(), "MainScreenFragment not found", Toast.LENGTH_SHORT).show();
        }

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList, new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                // Handle item click (optional)
            }

            @Override
            public void onUpdateClick(Product product, int quantity) {
                // Update the product quantity
                product.setQuantity(quantity);
                productAdapter.notifyDataSetChanged();
                saveProductsToFirebase();
            }

            @Override
            public void onDeleteClick(Product product) {
                // Delete the product from the list
                productList.remove(product);
                productAdapter.notifyDataSetChanged();
                saveProductsToFirebase();
            }

        });

        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewProducts.setAdapter(productAdapter);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productName = editTextProductName.getText().toString().trim();
                String quantityString = editTextQuantity.getText().toString().trim();

                if (!productName.isEmpty() && !quantityString.isEmpty()) {
                    int quantity = Integer.parseInt(quantityString);
                    Product product = new Product(productName, quantity);
                    productList.add(product);
                    productAdapter.notifyDataSetChanged();
                    saveProductsToFirebase();
                    editTextProductName.setText("");
                    editTextQuantity.setText("");
                } else {
                    Toast.makeText(getActivity(), "Please enter product name and quantity", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Load grocery list from Firebase
        loadGroceryList();


        return view;
    }
    private void loadGroceryList() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear(); // Clear existing list
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    productList.add(product);
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    // Method to save products to Firebase
    private void saveProductsToFirebase() {
        databaseReference.setValue(productList);
    }
}