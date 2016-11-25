package br.com.starfusion.ui;

import android.content.Context;
import android.content.Intent;

import br.com.starfusion.useful.BluetoothConnector;

/**
 * Created by Henrique on 09/08/2016.
 */
public class Activity {
    public static void openMain(Context context, BluetoothConnector.BluetoothSocketWrapper socket){
        MainActivity.setSocket(socket);
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void openBluetooth(Context context){
        Intent intent = new Intent(context, BluetoothActivity.class);
        context.startActivity(intent);
    }
}
