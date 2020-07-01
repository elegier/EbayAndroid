package web.tech.homework9;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductViewFragment extends Fragment {
    int counter;

    LinearLayout linearLayout;
    LinearLayout freeShippingLayout;
    LinearLayout notFreeShippingLayout;
    LinearLayout productDetailsLayout;
    LinearLayout specsLayout;
    LinearLayout subTitleLayout;
    LinearLayout brandLayout;

    TextView titleView;
    TextView priceView;
    TextView shippingView;
    TextView brandView;
    TextView subtitleView;


    public static Fragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("counter", position);
        ProductViewFragment tabFragment = new ProductViewFragment();
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        counter = getArguments().getInt("counter");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.product_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        linearLayout = (LinearLayout) view.findViewById(R.id.linear);

        titleView = (TextView) view.findViewById(R.id.detailed_item_title);
        priceView = (TextView) view.findViewById(R.id.detail_item_price);
        shippingView = (TextView) view.findViewById(R.id.detailed_item_shippingprice);

        notFreeShippingLayout = (LinearLayout) view.findViewById(R.id.detailed_item_notfreeshipping);
        freeShippingLayout = (LinearLayout) view.findViewById(R.id.detailed_item_freeshipping);
        brandView = (TextView) view.findViewById(R.id.detailed_item_brand);
        subtitleView = (TextView) view.findViewById(R.id.detailed_item_subtitle);

        productDetailsLayout = (LinearLayout) view.findViewById(R.id.detailed_item_product_features_layout);
        subTitleLayout = (LinearLayout) view.findViewById(R.id.subtitle_layout);
        brandLayout = (LinearLayout) view.findViewById(R.id.brand_layout);

        specsLayout = (LinearLayout) view.findViewById(R.id.detailed_item_spec_layout);

        DetailedItemActivity activity = (DetailedItemActivity) getActivity();
        JSONObject response = activity.getResponse();

        titleView.setText(activity.getItemTitle());

        priceView.setText(activity.getPrice());

        String shippingText = activity.getShipping();

        if (shippingText.equals("$0.0")) {
            freeShippingLayout.setVisibility(View.VISIBLE);
        } else {
            shippingView.setText(shippingText);
            notFreeShippingLayout.setVisibility(View.VISIBLE);
        }

        handleResponse(response);
    }

    private void handleResponse(JSONObject response) {
        try {
            Log.d("ProductViewFragment", response.toString());
            JSONObject item = response.getJSONObject("Item");
            String status = response.getString("Ack");
            Log.d("ProductViewFragment", "Status ===== " + status);

            JSONArray imageArray = item.getJSONArray("PictureURL");
            for (int idx = 0; idx < imageArray.length(); idx++) {
                ImageView imageView = new ImageView(getContext());
                Picasso.get().load(imageArray.getString(idx)).resize(1000, 1000).into(imageView);

                linearLayout.addView(imageView);
            }


            JSONArray nameValueList = item.getJSONObject("ItemSpecifics").getJSONArray("NameValueList");

            String brand = null;
            for (int i = 0; i < nameValueList.length(); i++) {
                JSONObject obj = nameValueList.getJSONObject(i);

                if (obj.getString("Name").equals("Brand")) {
                    brand = obj.getJSONArray("Value").getString(0);
                    break;
                }
            }

            if (item.has("Subtitle") || brand != null) {
                productDetailsLayout.setVisibility(View.VISIBLE);

                if (item.has("Subtitle")) {
                    String subtitle = item.getString("Subtitle");
                    subtitleView.setText(subtitle);
                    subTitleLayout.setVisibility(View.VISIBLE);
                }

                if (brand != null) {
                    brandView.setText(brand);
                    brandLayout.setVisibility(View.VISIBLE);
                }
            }

            boolean areValues = false;
            for (int i = 0; i < nameValueList.length(); i++) {
                areValues = true;

                if (i > 4) {
                    break;
                }

                JSONObject obj = nameValueList.getJSONObject(i);

                if (! obj.getString("Name").equals("Brand")) {
                    LinearLayout tempLayout = new LinearLayout(getContext());
                    tempLayout.setOrientation(LinearLayout.HORIZONTAL);

                    TextView keyTextView = new TextView(getContext());

                    TextView valTextView = new TextView(getContext());

                    keyTextView.setText(obj.getString("Name") + ": ");
                    valTextView.setText(obj.getJSONArray("Value").getString(0));
                    tempLayout.addView(keyTextView);
                    tempLayout.addView(valTextView);
                    specsLayout.addView(tempLayout);
                }
            }

            if (areValues) {
                specsLayout.setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            Log.d("ProductViewFragment", "Error parsing results", e);
            Toast.makeText(getActivity(), "Error occurred parsing product info", Toast.LENGTH_SHORT).show();
        }


    }
}

