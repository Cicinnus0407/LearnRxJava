package com.example.cicinnus.learnrxjava;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void operator1(View view) {
        startActivity(new Intent(MainActivity.this,OperatorActivity1.class));
    }

    public void operator2(View view) {
        startActivity(new Intent(MainActivity.this,OperatorActivity2.class));
    }

    public void operator3(View view) {
        startActivity(new Intent(MainActivity.this,OperatorActivity3.class));
    }

    public void operator4(View view) {
        startActivity(new Intent(MainActivity.this,OperatorActivity4.class));

    }

    public void operator5(View view) {
        startActivity(new Intent(MainActivity.this,OperatorActivity5.class));
    }
}
