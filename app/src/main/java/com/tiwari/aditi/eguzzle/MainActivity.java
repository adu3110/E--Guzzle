package com.tiwari.aditi.eguzzle;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(isServicesOK()){
            init();
        }
    }

    private void init(){
        Button btnBlr = (Button) findViewById(R.id.btnBangalore);
        btnBlr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intent  = new Intent(MainActivity.this, MapActivity.class);
                intent.putExtra("cityname", "bangalore");
                startActivity(intent);
            }
        });

        Button btnDelhi = (Button) findViewById(R.id.btnDelhi);
        btnDelhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intent  = new Intent(MainActivity.this, MapActivity.class);
                intent.putExtra("cityname", "delhi");
                startActivity(intent);
            }
        });

        Button btnHyd = (Button) findViewById(R.id.btnHyd);
        btnHyd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intent  = new Intent(MainActivity.this, MapActivity.class);
                intent.putExtra("cityname", "hyderabad");
                startActivity(intent);
            }
        });

        Button btnChennai = (Button) findViewById(R.id.btnChennai);
        btnChennai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intent  = new Intent(MainActivity.this, MapActivity.class);
                intent.putExtra("cityname", "chennai");
                startActivity(intent);
            }
        });

        Button btnMumbai = (Button) findViewById(R.id.btnMumbai);
        btnMumbai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intent  = new Intent(MainActivity.this, MapActivity.class);
                intent.putExtra("cityname", "mumbai");
                startActivity(intent);
            }
        });

        Button btnKol = (Button) findViewById(R.id.btnKolkata);
        btnKol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intent  = new Intent(MainActivity.this, MapActivity.class);
                intent.putExtra("cityname", "kolkata");
                startActivity(intent);
            }
        });

        Button btnPune = (Button) findViewById(R.id.btnPune);
        btnPune.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intent  = new Intent(MainActivity.this, MapActivity.class);
                intent.putExtra("cityname", "pune");
                startActivity(intent);
            }
        });


    }

    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if(available == ConnectionResult.SUCCESS){
            Log.d(TAG, "isServicesOK: checking google services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //resolvable error
            Log.d(TAG, "isServicesOK: error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else{
            Toast.makeText(this, "You can't make Map request", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
