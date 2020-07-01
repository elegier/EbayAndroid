package web.tech.homework9;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class DetailedProductsAdapter extends FragmentStateAdapter {

    public DetailedProductsAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.d("Adapter", "Position: " + position);

        if (position == 0) {
            return ProductViewFragment.getInstance(position);
        } else if (position == 1) {
            return SellerInfoFragment.getInstance(position);
        } else {
            return ShippingFragment.getInstance(position);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
