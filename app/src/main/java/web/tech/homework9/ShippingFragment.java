package web.tech.homework9;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

public class ShippingFragment extends Fragment {
    int position;

    TextView handlingTimeView;
    TextView oneDayShipView;
    TextView shipTypeView;
    TextView shipFromView;
    TextView shipToView;
    TextView expeditedShipView;

    public static Fragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("counter", position);
        ShippingFragment tabFragment = new ShippingFragment();
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("counter");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.shipping_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DetailedItemActivity activity = (DetailedItemActivity) getActivity();
        JSONObject response = activity.getResponse();

        handlingTimeView = (TextView) view.findViewById(R.id.handling_time);
        oneDayShipView = (TextView) view.findViewById(R.id.one_day_ship);
        shipTypeView = (TextView) view.findViewById(R.id.shippingtype);
        shipFromView = (TextView) view.findViewById(R.id.ship_from);
        shipToView = (TextView) view.findViewById(R.id.ship_to);
        expeditedShipView = (TextView) view.findViewById(R.id.expedited_shipping);

        handleResponse(response);
    }

    private void handleResponse(JSONObject response) {
        try {
            Log.d("ShippingFragment", response.toString());
            JSONObject item = response.getJSONObject("Item");

            String status = response.getString("Ack");
            Log.d("ShippingFragment", "Status ===== " + status);

            String handlingTime = item.getString("HandlingTime");
            handlingTimeView.setText(handlingTime);

            DetailedItemActivity activity = (DetailedItemActivity) getActivity();

            oneDayShipView.setText(activity.getOneDayShip());

            expeditedShipView.setText(activity.getExpeditedShip());

            shipTypeView.setText(activity.getShipType());

            shipFromView.setText(activity.getShipFrom());

            shipToView.setText(activity.getShipTo());

        } catch (JSONException e) {
            Log.d("ShippingFragment", "Error parsing results", e);
            Toast.makeText(getActivity(), "Error occurred parsing item shipping info", Toast.LENGTH_SHORT).show();

        }
    }
}