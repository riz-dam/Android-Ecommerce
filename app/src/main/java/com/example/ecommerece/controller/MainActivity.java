package com.example.ecommerece.controller;// MainActivity.java

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerece.R;
import com.example.ecommerece.helper.ProductAdapter;
import com.example.ecommerece.model.Product;
import com.example.ecommerece.model.ProductListResponse;
import com.example.ecommerece.utility.PaginationScrollListener;
import com.example.ecommerece.utility.RetrofitClient;
import com.example.ecommerece.utility.Session;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final int PAGE_SIZE = 10;

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private LinearLayoutManager layoutManager;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = 0;
    private int totalPageCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        productAdapter = new ProductAdapter(this);
        recyclerView.setAdapter(productAdapter);

        // Add PaginationScrollListener for infinite scrolling
        recyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                if (!isLoading && !isLastPage) {
                    currentPage++;
                    fetchAllProducts(currentPage);
                }
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public int getTotalPageCount() {
                // Implement this based on your API or data source
                return totalPageCount;
            }
        });

        // Fetch the initial data
        fetchAllProducts(currentPage);

        //Proses Search
        EditText searchEditText = findViewById(R.id.searchProductTextField); // Replace with your EditText's ID

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // This block is executed when the "Enter" key is pressed
                    String query = textView.getText().toString();
                    // You can perform actions, such as searching, here
                    fetchProducts(query);
                    return true; // Return true to indicate that you've handled the event
                }
                return false; // Return false for other actions
            }
        });

        //Ny Catagory
        Button catagoryButton1 = findViewById(R.id.catagory1Button); // Replace with your Button's ID
        catagoryButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // This block is executed when the button is clicked
                fetchProductsByCategory(catagoryButton1.getText().toString());
            }
        });

        Button catagoryButton2 = findViewById(R.id.catagory2Button); // Replace with your Button's ID
        catagoryButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // This block is executed when the button is clicked
                fetchProductsByCategory(catagoryButton2.getText().toString());
            }
        });

        Button catagoryButton3 = findViewById(R.id.catagory3Button); // Replace with your Button's ID
        catagoryButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // This block is executed when the button is clicked
                fetchProductsByCategory(catagoryButton3.getText().toString());
            }
        });


    }

    private void fetchAllProducts(int page) {
        isLoading = true;

        // Make an API request using Retrofit to fetch products
        ApiService apiService = RetrofitClient.getClient(Session.url).create(ApiService.class);
        Call<ProductListResponse> call = apiService.getProducts(PAGE_SIZE, page * PAGE_SIZE);

        call.enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {
                isLoading = false;
                if (response.isSuccessful()) {
                    ProductListResponse productListResponse = response.body();
                    if (productListResponse != null) {
                        List<Product> newProducts = productListResponse.getProducts();
                        productAdapter.addProducts(newProducts);

                        // Check if this is the last page
                        if (newProducts.size() < PAGE_SIZE) {
                            isLastPage = true;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductListResponse> call, Throwable t) {
                isLoading = false;
                Toast.makeText(getApplicationContext(), "No Internet Connection/ Server Issue!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void fetchProducts(String query) {
        isLoading = true;

        // Make an API request using Retrofit to fetch products
        ApiService apiService = RetrofitClient.getClient(Session.url).create(ApiService.class);
        Call<ProductListResponse> call = apiService.getProductsBySearch(query);

        call.enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {
                isLoading = false;
                if (response.isSuccessful()) {
                    ProductListResponse productListResponse = response.body();
                    if (productListResponse != null) {
                        List<Product> newProducts = productListResponse.getProducts();

                        productAdapter.clearProducts(); // Clear existing data
                        productAdapter.addProducts(newProducts);

                        // Check if this is the last page
                        if (newProducts.size() < PAGE_SIZE) {
                            isLastPage = true;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductListResponse> call, Throwable t) {
                isLoading = false;
                Toast.makeText(getApplicationContext(), "No Internet Connection/ Server Issue!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void fetchProductsByCategory(String category) {
        isLoading = true;

        // Make an API request using Retrofit to fetch products
        ApiService apiService = RetrofitClient.getClient(Session.url).create(ApiService.class);
        Call<ProductListResponse> call = apiService.getProductsByCategory(category);

        call.enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {
                isLoading = false;
                if (response.isSuccessful()) {
                    ProductListResponse productListResponse = response.body();
                    if (productListResponse != null) {
                        List<Product> newProducts = productListResponse.getProducts();

                        productAdapter.clearProducts(); // Clear existing data
                        productAdapter.addProducts(newProducts);

                        // Check if this is the last page
                        if (newProducts.size() < PAGE_SIZE) {
                            isLastPage = true;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductListResponse> call, Throwable t) {
                isLoading = false;
                Toast.makeText(getApplicationContext(), "No Internet Connection/ Server Issue!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
