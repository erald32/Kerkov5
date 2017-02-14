package com.jeddigital.kerkotaxi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Theodhori on 12/9/2016.
 */

public class RegisterClientActivity extends AppCompatActivity {

    EditText first_nameET;
    EditText last_nameET;
    EditText phone_nrET;
    EditText password_ET;
    EditText re_enter_passwordET;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_client);

        first_nameET = (EditText) findViewById(R.id.input_first_name);
        last_nameET = (EditText) findViewById(R.id.input_last_name);
        phone_nrET = (EditText) findViewById(R.id.input_phone_nr);
        password_ET = (EditText) findViewById(R.id.input_password);
        re_enter_passwordET = (EditText) findViewById(R.id.input_reenter_password);


        if(!first_nameET.getText().toString().equals("")){

            Toast.makeText(this, "Vendosni emrin", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!last_nameET.getText().toString().equals("")){

            Toast.makeText(this, "Vendosni mbiemrin", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!phone_nrET.getText().toString().equals("")){

            Toast.makeText(this, "Vendosni numrin e telefonit", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!password_ET.getText().toString().equals("")){

            Toast.makeText(this, "Vendosni passwordin", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!re_enter_passwordET.getText().toString().equals("")){

            Toast.makeText(this, "Ri-vendosni passwordin", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!password_ET.getText().toString().equals(re_enter_passwordET.getText().toString())){

            Toast.makeText(this, "Passwordet nuk përkojn", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
