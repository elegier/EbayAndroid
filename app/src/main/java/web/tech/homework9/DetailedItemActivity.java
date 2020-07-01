package web.tech.homework9;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONObject;

public class DetailedItemActivity extends AppCompatActivity {

    private RelativeLayout loadingLayout;
    private LinearLayout itemDetailsLayout;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    private String itemTitle;
    private String shipping;
    private String price;
    private String itemUrl;
    private String expeditedShip;
    private String oneDayShip;
    private String shipType;
    private String shipFrom;
    private String shipTo;

    private JSONObject response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String productId = getIntent().getStringExtra("productId");
        itemTitle = getIntent().getStringExtra("title");
        shipping = getIntent().getStringExtra("shipping");
        price = getIntent().getStringExtra("price");
        itemUrl = getIntent().getStringExtra("itemUrl");
        expeditedShip = getIntent().getStringExtra("expeditedShip");
        oneDayShip = getIntent().getStringExtra("oneDayShip");
        shipType = getIntent().getStringExtra("shipType");
        shipFrom = getIntent().getStringExtra("shipFrom");
        shipTo = getIntent().getStringExtra("shipTo");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.detailed_item_activity);
        initialize();

        fetchResults(productId);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detailed_item_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_redirect:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(itemUrl));
                startActivity(browserIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initialize() {
        Toolbar toolbar = findViewById(R.id.detailed_item_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(itemTitle);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tablayout);

        loadingLayout = findViewById(R.id.loading_item_layout);
        itemDetailsLayout = findViewById(R.id.item_view_layout);
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    private DetailedProductsAdapter createCardAdapter() {
        DetailedProductsAdapter adapter = new DetailedProductsAdapter(this);
        return adapter;
    }

    private void fetchResults(String productId) {
        String url = "https://web-tech-hw8-server.wl.r.appspot.com/item?ItemID=" + productId;
        Log.d("DetailedItem", "url ====== " + url);

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("DetailedItem", "Successfully completed request");
                        Log.d("Catalog", response.toString());
                        handleResults(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadingLayout.setVisibility(View.INVISIBLE);
                        Log.d("DetailedItem", "An error occurred");
                        Log.d("DetailedItem", error.getMessage(), error.getCause());
                    }
                });

        queue.add(request);
    }

    private void handleResults(JSONObject response) {
        this.response = response;

        viewPager.setAdapter(createCardAdapter());
        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

                        if (position == 0) {
                            tab.setText("PRODUCT");
                            tab.setIcon(R.drawable.information_variant_selector);

                        } else if (position == 1) {
                            tab.setText("SELLER INFO");
                            tab.setIcon(R.drawable.ic_seller);

                        } else {
                            tab.setText("SHIPPING");
                            tab.setIcon(R.drawable.truck_delivery_selector);
                        }

                        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FF0000"));
                        tabLayout.setTabTextColors(Color.parseColor("#0063D1"), Color.parseColor("#0063D1"));
                    }
                }).attach();

        loadingLayout.setVisibility(View.INVISIBLE);
        itemDetailsLayout.setVisibility(View.VISIBLE);
    }

    public JSONObject getResponse() {
        return response;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public String getShipping() {
        return shipping;
    }

    public String getPrice() {
        return price;
    }

    public String getExpeditedShip() {
        return expeditedShip;
    }

    public String getOneDayShip() {
        return oneDayShip;
    }

    public String getShipType() {
        return shipType;
    }

    public String getShipFrom() {
        return shipFrom;
    }

    public String getShipTo() {
        return shipTo;
    }
}
