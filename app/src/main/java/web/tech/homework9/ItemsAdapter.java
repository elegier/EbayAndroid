package web.tech.homework9;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    private List<ItemEntry> items;

    public ItemsAdapter(List<ItemEntry> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View itemView = inflater.inflate(R.layout.item_entry, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data model based on position
        ItemEntry item = items.get(position);

        // Set item views based on your views and data model
        TextView titleView = holder.titleView;
        titleView.setText(item.getTitle());
        TextView shippingView = holder.shippingView;
        shippingView.setText(item.getShipping());
        TextView conditionView = holder.conditionView;
        conditionView.setText(item.getCondition());
        TextView priceView = holder.priceView;
        priceView.setText(item.getPrice());

        ImageView itemImage = holder.itemImage;
        if (! "https://thumbs1.ebaystatic.com/pict/04040_0.jpg".equals(item.getImageUrl())) {
            Picasso.get().load(item.getImageUrl()).into(itemImage);
        }

        if (item.isTopRated()) {
            TextView topRatedView = holder.topRatedView;
            topRatedView.setVisibility(View.VISIBLE);
        }

        if ("$0.0".equals(item.getShipping())) {
            LinearLayout freeShipping = holder.freeShippingView;
            freeShipping.setVisibility(View.VISIBLE);
        } else {
            LinearLayout notFreeShipping = holder.notFreeShippingView;
            notFreeShipping.setVisibility(View.VISIBLE);
        }

        CardView cardView = holder.cardView;
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productId = item.getId();
                String title = item.getTitle();

                Context context = view.getContext();
                Intent intent = new Intent(context, DetailedItemActivity.class);
                intent.putExtra("productId", productId);
                intent.putExtra("title", title);
                intent.putExtra("price", item.getPrice());
                intent.putExtra("shipping", item.getShipping());
                intent.putExtra("itemUrl", item.getItemUrl());
                intent.putExtra("oneDayShip", item.getOneDayShip());
                intent.putExtra("expeditedShip", item.getExpeditedShip());
                intent.putExtra("itemUrl", item.getItemUrl());
                intent.putExtra("oneDayShip", item.getOneDayShip());
                intent.putExtra("expeditedShip", item.getExpeditedShip());
                intent.putExtra("shipType", item.getShipType());
                intent.putExtra("shipFrom", item.getShipsFrom());
                intent.putExtra("shipTo", item.getShipsTo());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView titleView;
        public TextView shippingView;
        public TextView conditionView;
        public TextView priceView;
        public ImageView itemImage;
        public TextView topRatedView;
        public LinearLayout freeShippingView;
        public LinearLayout notFreeShippingView;
        public CardView cardView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            titleView = (TextView) itemView.findViewById(R.id.item_entry_title);
            shippingView = (TextView) itemView.findViewById(R.id.item_entry_shippingprice);
            conditionView = (TextView) itemView.findViewById(R.id.item_entry_condition);
            priceView = (TextView) itemView.findViewById(R.id.item_entry_price);
            itemImage = (ImageView) itemView.findViewById(R.id.item_entry_image);

            topRatedView = (TextView) itemView.findViewById(R.id.item_entry_toprated);

            freeShippingView = (LinearLayout) itemView.findViewById(R.id.item_entry_freeshipping);
            notFreeShippingView = (LinearLayout) itemView.findViewById(R.id.item_entry_notfreeshipping);
            cardView = (CardView) itemView.findViewById(R.id.item_entry_card);
        }
    }
}
