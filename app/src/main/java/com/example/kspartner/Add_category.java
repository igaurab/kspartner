package com.example.kspartner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    // ...

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
        ItemClass itemClass = new ItemClass();
        String Price;
        String Category = category_name.getText().toString();
        String rid,name,price;
        EditText et_Name;
        EditText et_Price;

        //Initialize
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("Restaurant_Pref",0);
        rid = preferences.getString("rid",null);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Menu");

        for (int i=0; i <= linearLayout.getChildCount()-1;i++) {
            Log.d("DB", "updateToDatabase: "+ linearLayout.getChildAt(i).toString());
            View view_CardLayout = linearLayout.getChildAt(i);
            if (view_CardLayout instanceof CardView) {
                Log.d("DB", "updateToDatabase: " + ((CardView) view_CardLayout).getChildCount());
                Log.d("DB", "updateToDatabase: " + ((CardView) view_CardLayout).getChildAt(0));

                View view_LinearLayout = ((CardView) view_CardLayout).getChildAt(0);
                if (view_LinearLayout instanceof LinearLayout) {
                    Log.d("DB", "updateToDatabase: " + ((LinearLayout) view_LinearLayout).getChildCount());
                    Log.d("DB", "updateToDatabase: " + ((LinearLayout) view_LinearLayout).getChildAt(0));

                    View et_name = ((LinearLayout) view_LinearLayout).getChildAt(0);
                    View et_price = ((LinearLayout) view_LinearLayout).getChildAt(1);

                    if (et_name instanceof EditText) {

                        et_Name = ((EditText) et_name);
                        name = et_Name.getText().toString();
                        itemClass.setName(name);
                        Log.d("Logs:", "updateToDatabase: " + et_Name.getText().toString());
                    }
                    if (et_price instanceof EditText) {
                       et_Price = ((EditText) et_price);
                        price = et_Price.getText().toString();
                        itemClass.setPrice(Integer.parseInt(price));
                        Log.d("Logs:", "updateToDatabase: " + et_Price.getText().toString());
                    }
                }
            }
            mDatabase.child(rid).child(Category).child("name"+i).child("name").setValue(itemClass.getName());
            mDatabase.child(rid).child(Category).child("name"+i).child("price").setValue(itemClass.getPrice());
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
}
