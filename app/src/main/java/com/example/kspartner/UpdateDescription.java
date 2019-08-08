package com.example.kspartner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateDescription extends AppCompatActivity {

    TextView txt_description;
    Button btn_done;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__description);

        txt_description = findViewById(R.id.txt_description_update);
        btn_done = findViewById(R.id.btn_done_update);
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("Func", "onClick: Entered OnClick");
                SharedPreferences preferences = getApplicationContext().getSharedPreferences("Restaurant_Pref",0);


                Description description = new Description();
                description.setDescription(txt_description.getText().toString());
                description.setRid(preferences.getString("rid",null));

                Log.d("Func", "onClick: "+ preferences.getString("rid",null));
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Description");
                databaseReference.child(preferences.getString("rid",null)).setValue(description.getDescription());

                Intent intent = new Intent(UpdateDescription.this, Home.class);
                startActivity(intent);
            }
        });
    }
}
