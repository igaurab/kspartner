package com.example.kspartner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {

    EditText rest_name;
    EditText rest_contact;
    EditText rest_address;
    Button btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rest_name = findViewById(R.id.txt_restaurant_name);
        rest_contact = findViewById(R.id.txt_restaurant_contact);
        rest_address = findViewById(R.id.txt_restaurant_address);
        btn_next = findViewById(R.id.btn_next_form_main_activity);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Restaurant restaurant = new Restaurant();
                restaurant.setR_name(rest_name.getText().toString());
                restaurant.setR_contact(rest_contact.getText().toString());
                restaurant.setR_address(rest_address.getText().toString());

                Intent intent = new Intent(MainActivity.this, signup_choose_location.class);
                intent.putExtra("RESTAURANT_DATA", (Serializable) restaurant);
                startActivity(intent);
            }
        });
    }
}
