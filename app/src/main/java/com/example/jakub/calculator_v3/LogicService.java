package com.example.jakub.calculator_v3;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class LogicService extends Service {
    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        LogicService getService() {
            return LogicService.this;
        }
    }

    public double add(double n1, double n2){
        return n1 + n2;
    }

    public double subtract(double n1, double n2){
        return n1 - n2;
    }

    public double multiply(double n1, double n2){
        return n1 * n2;
    }

    public double divide(double n1, double n2){
        if (n2 != 0.0){
            return n1 / n2;
        }
        else
            return 0;
    }


    public LogicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
