package com.example.jakub.calculator_v3;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText n1EditText;
    private EditText n2EditText;
    private EditText resultEditText;

    private Button additionButton;
    private Button subtractionButton;
    private Button multiplicationButton;
    private Button divisionButton;
    private Button piButton;

    private ProgressBar progressBar;

    private String errorMessage = "Podaj poprawne Liczba 1 oraz Liczba 2";
    LogicService logicService;
    boolean mBound = false;

    private ServiceConnection logicConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            LogicService.LocalBinder binder = (LogicService.LocalBinder) service;
            logicService = binder.getService();
            mBound = true;
            Toast.makeText(MainActivity.this, "Logic Service Connected!",Toast.LENGTH_SHORT).show();
        }

        public void onServiceDisconnected(ComponentName className) {
            logicService = null;
            mBound = false;
            Toast.makeText(MainActivity.this, "Logic Service Disconnected!",Toast.LENGTH_SHORT).show();
        }
    };

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mBound) {
            this.bindService(new Intent(MainActivity.this,LogicService.class),logicConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            mBound = false;this.unbindService(logicConnection);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        n1EditText = findViewById(R.id.n1EditText);
        n2EditText = findViewById(R.id.n2EditText);
        resultEditText = findViewById(R.id.resultEditText);

        additionButton = findViewById(R.id.additionButton);
        additionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBound){
                    String n1Text = n1EditText.getText().toString();
                    String n2Text = n2EditText.getText().toString();
                    if(isNumeric(n1Text) && isNumeric(n2Text)) {
                        Double result = logicService.add(Double.valueOf(n1Text), Double.valueOf(n2Text));
                        resultEditText.setText(result.toString());
                    }
                    else {
                        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        subtractionButton = findViewById(R.id.subtractionButton);
        subtractionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBound){
                    String n1Text = n1EditText.getText().toString();
                    String n2Text = n2EditText.getText().toString();
                    if(isNumeric(n1Text) && isNumeric(n2Text)) {
                        Double result = logicService.subtract(Double.valueOf(n1Text), Double.valueOf(n2Text));
                        resultEditText.setText(result.toString());
                    }
                    else {
                        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        multiplicationButton = findViewById(R.id.multiplicationButton);
        multiplicationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBound){
                    String n1Text = n1EditText.getText().toString();
                    String n2Text = n2EditText.getText().toString();
                    if(isNumeric(n1Text) && isNumeric(n2Text)) {
                        double result = logicService.multiply(Double.parseDouble(n1Text), Double.parseDouble(n2Text));
                        resultEditText.setText(Double.toString(result));
                    }
                    else {
                        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        divisionButton = findViewById(R.id.divisionButton);
        divisionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBound){
                    String n1Text = n1EditText.getText().toString();
                    String n2Text = n2EditText.getText().toString();
                    if(isNumeric(n1Text) && isNumeric(n2Text)) {
                        double result = logicService.divide(Double.parseDouble(n1Text), Double.parseDouble(n2Text));
                        resultEditText.setText(Double.toString(result));
                    }
                    else {
                        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        piButton = findViewById(R.id.piButton);
        progressBar = findViewById(R.id.progressBar);
        piButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mBound){
                    progressBar.setProgress(0);
                    new PiComputeTask().execute();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class PiComputeTask extends AsyncTask<Void, Integer, Double> {

        protected Double doInBackground(Void... voids) {
            double pi;
            int counter = 0;
            int iterationsNumber = 1000000;
            double x;
            double  y;
            for(int i = 0 ; i < iterationsNumber ; i++) {
                x = Math.random();
                y = Math.random();
                if(x*x+y*y<=1) counter++;
                publishProgress((int) ((i / (float) iterationsNumber) * 100));
            }
            pi = 4.*counter/iterationsNumber;


            return pi;
        }
        protected void onPostExecute(Double result) {
            n1EditText.setText(result.toString());
        }
        @Override
        protected void onProgressUpdate(Integer... values){
            super.onProgressUpdate(values);
            ProgressBar progressBar = findViewById(R.id.progressBar);
            progressBar.setProgress(values[0]);
        }

    }

}
