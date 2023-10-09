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
import com.example.ecommerece.helper.CategoryButtonAdapter;
import com.example.ecommerece.helper.ProductAdapter;
import com.example.ecommerece.model.Product;
import com.example.ecommerece.model.ProductListResponse;
import com.example.ecommerece.utility.PaginationScrollListener;
import com.example.ecommerece.utility.RetrofitClient;
import com.example.ecommerece.utility.Session;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final int PAGE_SIZE = 10;

    private RecyclerView recyclerView;
    private RecyclerView recycleCatView;
    private ProductAdapter productAdapter;
    private CategoryButtonAdapter categoryButtonAdapter;
    private LinearLayoutManager layoutManager;
    private LinearLayoutManager layoutCatManager;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = 0;
    private int totalPageCount;
    private List<String> categoriesList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recycleCatView = findViewById(R.id.recyclerCatView);
        layoutManager = new LinearLayoutManager(this);
        layoutCatManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recycleCatView.setLayoutManager(layoutCatManager);
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
        fectchAllCategory();

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

         categoryButtonAdapter = new CategoryButtonAdapter(categoriesList, new CategoryButtonAdapter.CategoryClickListener() {
            @Override
            public void onCategoryClick(String category) {
                // Handle category button click
                // You can perform actions based on the selected category here
                if(category.equalsIgnoreCase("ALL")) {
                    fetchAllProducts(0);
                }
                else{
                    fetchProductsByCategory(category);
                }
            }
        });

        recycleCatView.setAdapter(categoryButtonAdapter);
    }

    private void fectchAllCategory(){
        // Make an API request using Retrofit to fetch products
        ApiService apiService = RetrofitClient.getClient(Session.url).create(ApiService.class);
        Call<List<String>> call = apiService.getCategories();
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful()) {
                    List<String> categories = response.body();
                    // Update the categoriesList with the fetched data
                    categoriesList.clear();
                    categories.add(0,"ALL");
                    categoriesList.addAll(categories);
                    categoryButtonAdapter.notifyDataSetChanged(); // Notify the adapter of data change
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "No Internet Connection/ Server Issue!", Toast.LENGTH_LONG).show();
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
