package com.example.kspartner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class UpdateProfile extends AppCompatActivity {
    String rid;
    EditText rest_name;
    EditText rest_contact;
    EditText rest_address;
    Button btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("Restaurant_Pref",0);
        rid = preferences.getString("rid",null);
        rest_name = findViewById(R.id.txt_restaurant_name_update);
        rest_contact = findViewById(R.id.txt_restaurant_contact_update);
        rest_address = findViewById(R.id.txt_restaurant_address_update);
        btn_next = findViewById(R.id.btn_next_form_main_activity_update);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Restaurant restaurant = new Restaurant();
                restaurant.setR_name(rest_name.getText().toString());
                restaurant.setR_contact(rest_contact.getText().toString());
                restaurant.setR_address(rest_address.getText().toString());

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Restaurants");
                reference.child(rid).child("name").setValue(restaurant.getname());
                reference.child(rid).child("contact").setValue(restaurant.getcontact());
                reference.child(rid).child("address").setValue(restaurant.getaddress());

                Intent intent = new Intent(UpdateProfile.this, RestaurantDetail.class);
                intent.putExtra("RESTAURANT_DATA", (Serializable) restaurant);
                startActivity(intent);
            }
        });
    }
}
