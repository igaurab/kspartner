package com.example.kspartner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.concurrent.CountedCompleter;

public class Add_category extends AppCompatActivity {
    // The CardLayoutCount is used as indexing for dynamic generation of card view
    // Every item are added to the list and the list is pushed on to the database

    private int CardLayoutCount = 0;
    private int EditTextCount = 50;
    private List<ItemClass> listOfItems;

    //widgets
    private TextInputEditText category_name;
    private ImageView img_add_new_item;
    private Button save;

    //Widgets for your dynamic Layout
    private LinearLayout linearLayout;
    private LinearLayout linearLayoutLocalCardView;
    private Context context;
    private ViewGroup.LayoutParams layoutParameters;
    private CardView cardView;
   // private TextView tv_itemName, tv_Price;
    private EditText et_itemName, et_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        //Initialize widgets
        Toolbar toolbar = findViewById(R.id.toolbar_add_category);
        category_name = findViewById(R.id.tet_category_name);
        img_add_new_item = findViewById(R.id.img_add_item_list);
        save = findViewById(R.id.btn_add_category_save);

        linearLayout = findViewById(R.id.ll_container);
        context = getApplicationContext();

        //Set toolbar and actionbar (menu)
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Listeners:
        img_add_new_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Call the function:
                createItemCardLayout();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateToDatabase();
            }
        });
    }

    public void createItemCardLayout() {
        layoutParameters = new ActionBar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
               ViewGroup.LayoutParams.MATCH_PARENT
        );

        cardView = new CardView(context);
        cardView.setLayoutParams(layoutParameters);
        cardView.setRadius(2);
        cardView.setMaxCardElevation(2);
        cardView.setId(CardLayoutCount);

        linearLayoutLocalCardView = new LinearLayout(context);
        linearLayoutLocalCardView.setLayoutParams(layoutParameters);
        linearLayoutLocalCardView.setOrientation(LinearLayout.VERTICAL);
        cardView.addView(linearLayoutLocalCardView);

        et_itemName = new EditText(context);
        et_itemName.setLayoutParams(layoutParameters);
        et_itemName.setHint("Item Name");
        et_itemName.setInputType(InputType.TYPE_CLASS_TEXT);
        et_itemName.setMaxLines(1);

        et_price = new EditText(context);
        et_price.setLayoutParams(layoutParameters);
        et_price.setHint("Item Price");
        et_price.setInputType(InputType.TYPE_CLASS_NUMBER);
        et_price.setMaxLines(1);

        linearLayoutLocalCardView.addView(et_itemName);
        linearLayoutLocalCardView.addView(et_price);

        linearLayout.addView(cardView);
        CardLayoutCount++;
    }

    public void updateToDatabase() {

    }
    @Override
    protected void onStart() {
        super.onStart();
    }
}
