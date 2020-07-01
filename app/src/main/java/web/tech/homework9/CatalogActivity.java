package web.tech.homework9;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class CatalogActivity extends AppCompatActivity {
    private RelativeLayout loadingLayout;
    private RelativeLayout noRecordsLayout;
    private RecyclerView recyclerView;
    private RelativeLayout itemsLayout;
    private RelativeLayout errorLayout;
    private SwipeRefreshLayout swipeRefreshLayout;

    private TextView numResultsView;
    private TextView keyWordsLabelView;

    private String keywords;
    private String query;
    private ArrayList<ItemEntry> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        query = getIntent().getStringExtra("query");
        keywords = getIntent().getStringExtra("keywords");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.catalog_activity);
        initialize();

        fetchResults();
    }

    private void initialize() {
        //getSupportActionBar().hide();
        loadingLayout = findViewById(R.id.loading_layout);
        noRecordsLayout = findViewById(R.id.no_records_layout);
        swipeRefreshLayout = findViewById(R.id.swiperefresh_items);

        recyclerView = findViewById(R.id.recyclerView);
        itemsLayout = findViewById(R.id.items_layout);
        errorLayout = findViewById(R.id.error_layout);

        numResultsView = findViewById(R.id.number_results);
        keyWordsLabelView = findViewById(R.id.keyword_header);
        items = new ArrayList<>();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchResults();
            }
        });
    }

    private void fetchResults() {
        String url = "https://web-tech-hw8-server.wl.r.appspot.com/search" + query;
        Log.d("Catalog", "url====== " + url);

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Catalog", "Successfully completed request");
                        //Log.d("Catalog", response.toString());
                        handleResults(response);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadingLayout.setVisibility(View.INVISIBLE);
                        swipeRefreshLayout.setVisibility(View.VISIBLE);
                        errorLayout.setVisibility(View.VISIBLE);
                        Log.d("Catalog", "An error occurred");
                        Log.d("Catalog", error.getMessage(), error.getCause());
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });

        queue.add(request);
    }

    private void handleResults(JSONObject obj) {
        Log.d("Catalog", "Started parsing...");
        items = new ArrayList<>();

        try {
            JSONObject findItemsAdvancedResponse = obj.getJSONArray("findItemsAdvancedResponse").getJSONObject(0);
            Log.d("Catalog", "Status ===== " + findItemsAdvancedResponse.getJSONArray("ack").getString(0));
            JSONArray numResultsObj = findItemsAdvancedResponse.getJSONArray("paginationOutput").getJSONObject(0).getJSONArray("totalEntries"); //findItemsAdvancedResponse[0].paginationOutput[0].totalEntries[0];
            String numResults = numResultsObj.getString(0);
            Log.d("Catalog", "Total items: " + numResults);

            String value = Integer.parseInt(numResults) > 50 ? "50" : numResults;
            numResultsView.setText(value);

            keyWordsLabelView.setText(keywords);

            if (Integer.parseInt(numResults) > 0) {
                JSONArray itemsArray = (JSONArray) findItemsAdvancedResponse.getJSONArray("searchResult").getJSONObject(0).get("item"); //findItemsAdvancedResponse[0].searchResult[0].item

                for (int idx = 0; idx < itemsArray.length(); idx++) {
                    JSONObject item = itemsArray.getJSONObject(idx); //value.title[0]
                    String title = item.getJSONArray("title").getString(0);

                    JSONArray priceObj = item.getJSONArray("sellingStatus").getJSONObject(0).getJSONArray("convertedCurrentPrice");
                    String price = priceObj.getJSONObject(0).getString("__value__");   //value.sellingStatus[0].convertedCurrentPrice[0].__value__

                    String condition;

                    if (item.has("condition")) {
                        condition = item.getJSONArray("condition").getJSONObject(0).getJSONArray("conditionDisplayName").getString(0);
                    } else {
                        condition = "N/A";
                    }

                    String imageUrl = item.getJSONArray("galleryURL").getString(0).trim(); //value.galleryURL[0]

                    JSONObject shippingInfoObj = item.getJSONArray("shippingInfo").getJSONObject(0); //value.shippingInfo[0].shippingServiceCost[0].__value__
                    String shipping;

                    if (shippingInfoObj.has("shippingServiceCost")) {
                        shipping = item.getJSONArray("shippingInfo").getJSONObject(0).getJSONArray("shippingServiceCost").getJSONObject(0).getString("__value__");;
                    } else {
                        shipping = "0.0";
                    }

                    String topRated = item.getJSONArray("topRatedListing").getString(0);
                    String id = item.getJSONArray("itemId").getString(0);

                    String itemUrl = item.getJSONArray("viewItemURL").getString(0); // value.viewItemURL[0]

                    if (! isValidUrl(itemUrl)) {
                        itemUrl = "https://ebay.com";
                    }

                    String oneDayShip = item.getJSONArray("shippingInfo").getJSONObject(0).getJSONArray("oneDayShippingAvailable").getString(0); // value.shippingInfo[0].oneDayShippingAvailable[0]
                    oneDayShip = oneDayShip.equals("true") ? "Yes" : "No";

                    String expeditedShip = item.getJSONArray("shippingInfo").getJSONObject(0).getJSONArray("expeditedShipping").getString(0); // value.shippingInfo[0].expeditedShipping[0]
                    expeditedShip = expeditedShip.equals("true") ? "Yes" : "No";

                    String shipType = item.getJSONArray("shippingInfo").getJSONObject(0).getJSONArray("shippingType").getString(0); //value.shippingInfo[0].shippingType[0]

                    String shipFrom = item.getJSONArray("location").getString(0); // value.location[0]

                    String shipTo = item.getJSONArray("shippingInfo").getJSONObject(0).getJSONArray("shipToLocations").getString(0); //value.shippingInfo[0].shipToLocations[0]

                    ItemEntry itemEntry = new ItemEntry(title, price, imageUrl, shipping, condition, Boolean.valueOf(topRated), id, itemUrl, oneDayShip, expeditedShip, shipType, shipFrom, shipTo);
                    items.add(itemEntry);
                }

                // Create adapter passing in the sample user data
                ItemsAdapter adapter = new ItemsAdapter(items);
                // Attach the adapter to the recyclerview to populate items
                recyclerView.setAdapter(adapter);
                // Set layout manager to position the items
                recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
                recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.HORIZONTAL));
                loadingLayout.setVisibility(View.INVISIBLE);
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                itemsLayout.setVisibility(View.VISIBLE);
            } else {
                loadingLayout.setVisibility(View.INVISIBLE);
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                noRecordsLayout.setVisibility(View.VISIBLE);
                Toast.makeText(this, "No Records", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Log.d("Catalog", "Error parsing results", e);

            loadingLayout.setVisibility(View.INVISIBLE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            errorLayout.setVisibility(View.VISIBLE);
        }

    }

    private boolean isValidUrl(String uri) {
        final URL url;
        try {
            url = new URL(uri);
        } catch (Exception e1) {
            return false;
        }
        return true;
    }

}
