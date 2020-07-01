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

public class SellerInfoFragment extends Fragment {
    int position;

    TextView feedbackScoreView;
    TextView userIDView;
    TextView refundTypeView;
    TextView posFeedbackPercentView;
    TextView feedbackRatingStarView;
    TextView returnsWithinView;
    TextView costPaidByView;
    TextView returnsAcceptedView;

    public static Fragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("counter", position);
        SellerInfoFragment tabFragment = new SellerInfoFragment();
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
        return inflater.inflate(R.layout.seller_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DetailedItemActivity activity = (DetailedItemActivity) getActivity();

        feedbackScoreView = (TextView) view.findViewById(R.id.feedback_score);
        userIDView = (TextView) view.findViewById(R.id.userid);
        refundTypeView = (TextView) view.findViewById(R.id.refundtype);
        posFeedbackPercentView = (TextView) view.findViewById(R.id.pos_feedback_score);
        feedbackRatingStarView = (TextView) view.findViewById(R.id.feedback_rating_star);
        returnsWithinView = (TextView) view.findViewById(R.id.returns_within);
        costPaidByView = (TextView) view.findViewById(R.id.cost_paid_by);
        returnsAcceptedView = (TextView) view.findViewById(R.id.returns_accepted);

        JSONObject response = activity.getResponse();
        handleResponse(response);
    }

    private void handleResponse(JSONObject response) {
        try {
            Log.d("SellerInfoFragment", response.toString());
            JSONObject item = response.getJSONObject("Item");
            JSONObject seller = item.getJSONObject("Seller");
            JSONObject returns = item.getJSONObject("ReturnPolicy");
            String status = response.getString("Ack");
            Log.d("SellerInfoFragment", "Status ===== " + status);

            String feedbackScore = seller.getString("FeedbackScore");
            feedbackScoreView.setText(feedbackScore);

            String userId = seller.getString("UserID");
            userIDView.setText(userId);

            String posFeedbackPercent = seller.getString("PositiveFeedbackPercent");
            posFeedbackPercentView.setText(posFeedbackPercent);

            String feedbackRatingStar = seller.getString("FeedbackRatingStar");
            feedbackRatingStarView.setText(feedbackRatingStar);

            String refund = returns.getString("Refund");
            refundTypeView.setText(refund);

            String returnsWithin = returns.getString("ReturnsWithin");
            returnsWithinView.setText(returnsWithin);

            String costPaidBy = returns.getString("ShippingCostPaidBy");
            costPaidByView.setText(costPaidBy);

            String returnsAccepted = returns.getString("ReturnsAccepted");
            returnsAcceptedView.setText(returnsAccepted);

        } catch (JSONException e) {
            Log.d("SellerInfoFragment", "Error parsing results", e);
            Toast.makeText(getActivity(), "Error occurred parsing item seller info", Toast.LENGTH_SHORT).show();

        }
    }
}
