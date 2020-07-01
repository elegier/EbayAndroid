package web.tech.homework9;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText keyWordsText;
    private EditText minPriceText;
    private EditText maxPriceText;
    private TextView keyWordsError;
    private TextView priceError;
    private Button clearButton;
    Button searchButton;
    private CheckBox newCheckBox;
    private CheckBox usedCheckBox;
    private CheckBox unspecifiedCheckBox;
    private Spinner sortBy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

        searchButton.setOnClickListener(v -> {
            if (validateFields()) {
                Log.d("Main", "=======Success");
                String query = constructQuery();
                Intent intent = new Intent(this, CatalogActivity.class);
                intent.putExtra("query", query);
                intent.putExtra("keywords", keyWordsText.getText().toString());
                startActivity(intent);
            } else {
                Log.d("Main", "=======Fail");
                Toast.makeText(this, "Please fix all fields with errors", Toast.LENGTH_SHORT).show();
            }
        });

        clearButton.setOnClickListener(v -> {
            maxPriceText.getText().clear();
            minPriceText.getText().clear();
            keyWordsText.getText().clear();

            sortBy.setSelection(0);

            newCheckBox.setChecked(false);
            usedCheckBox.setChecked(false);
            unspecifiedCheckBox.setChecked(false);
            keyWordsError.setVisibility(View.INVISIBLE);
            priceError.setVisibility(View.INVISIBLE);
        });
    }

    private boolean validateFields() {
        Log.d("Main", "=======Validating......");
        boolean isValid = true;

        String keywords = keyWordsText.getText().toString();

        if (keywords == null || keywords.isEmpty()) {
            Log.d("Main", "Bad keyword");
            keyWordsError.setVisibility(View.VISIBLE);
            isValid = false;
        } else {
            keyWordsError.setVisibility(View.INVISIBLE);
        }

        String minPrice = minPriceText.getText().toString();
        String maxPrice = maxPriceText.getText().toString();

        if (! minPrice.isEmpty() && ! maxPrice.isEmpty() && Integer.parseInt(minPrice) > Integer.parseInt(maxPrice)) {
            Log.d("Main", "Bad price");
            priceError.setVisibility(View.VISIBLE);
            isValid = false;
        } else {
            priceError.setVisibility(View.INVISIBLE);
        }

        return isValid;
    }

    private String constructQuery() {
        int itemIndex = 0;
        List<String> params = new ArrayList<>();
        try {
            String keyWords = URLEncoder.encode(keyWordsText.getText().toString(), StandardCharsets.UTF_8.toString());
            params.add("?keywords=" + keyWords);
            params.add("paginationInput.entriesPerPage=50");

            String sortByValue = sortBy.getSelectedItem().toString();
            Log.d("Main", "Sort By: " + sortByValue);

            switch (sortByValue) {
                case "Best Match":
                    params.add("sortOrder=BestMatch");
                    Log.d("Main", "Best Match selected");
                    break;
                case "Price: highest first":
                    params.add("sortOrder=CurrentPriceHighest");
                    Log.d("Main", "Highest first");
                    break;
                case "Price + Shipping: Highest first":
                    params.add("sortOrder=PricePlusShippingHighest");
                    Log.d("Main", "price + shipping highest");
                    break;
                case "Price + Shipping: Lowest first":
                    params.add("sortOrder=PricePlusShippingLowest");
                    Log.d("Main", "price + shipping highest");
                    break;
                default:
                    Log.d("Main", "No sort by used");
                    break;
            }

            if (! minPriceText.getText().toString().isEmpty()) {
                params.add("itemFilter(" + itemIndex + ").name=MinPrice");
                params.add("itemFilter(" + itemIndex + ").value=" + minPriceText.getText().toString());
                params.add("itemFilter(" + itemIndex + ").paramName=Currency");
                params.add("itemFilter(" + itemIndex + ").paramValue=USD");
                itemIndex++;
            }

            if (! maxPriceText.getText().toString().isEmpty()) {
                params.add("itemFilter(" + itemIndex + ").name=MaxPrice");
                params.add("itemFilter(" + itemIndex + ").value=" + maxPriceText.getText().toString());
                params.add("itemFilter(" + itemIndex + ").paramName=Currency");
                params.add("itemFilter(" + itemIndex + ").paramValue=USD");
                itemIndex = itemIndex + 1;
            }

            if (newCheckBox.isChecked() || usedCheckBox.isChecked() || unspecifiedCheckBox.isChecked()){
                params.add("itemFilter(" + itemIndex + ").name=Condition");

                int conditionIndex = 0;

                if (newCheckBox.isChecked()) {
                    params.add("itemFilter(" + itemIndex + ").value(" + conditionIndex + ")=New");
                    conditionIndex = conditionIndex + 1;
                }

                if (usedCheckBox.isChecked()) {
                    params.add("itemFilter(" + itemIndex + ").value(" + conditionIndex + ")=Used");
                    conditionIndex = conditionIndex + 1;
                }

                if (unspecifiedCheckBox.isChecked()) {
                    params.add("itemFilter(" + itemIndex + ").value(" + conditionIndex + ")=Unspecified");
                    conditionIndex = conditionIndex + 1;
                }
            }

        } catch (UnsupportedEncodingException e) {
            Log.d("Main", "error occurred", e);
        }

        return String.join("&", params);
    }

    private void initialize() {
        keyWordsText = findViewById(R.id.keyword);
        minPriceText = findViewById(R.id.min_price);
        maxPriceText = findViewById(R.id.max_price);
        priceError = findViewById(R.id.price_error);
        keyWordsError = findViewById(R.id.keyword_error);
        clearButton = findViewById(R.id.clear_button);
        searchButton = findViewById(R.id.search_button);
        newCheckBox = findViewById(R.id.newCheckBox);
        usedCheckBox = findViewById(R.id.usedCheckBox);
        unspecifiedCheckBox = findViewById(R.id.unspecifiedCheckBox);
        sortBy = findViewById(R.id.sortby);
    }

}