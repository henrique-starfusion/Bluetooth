package br.com.starfusion.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import br.com.starfusion.R;
import br.com.starfusion.useful.BluetoothConnector;
import butterknife.ButterKnife;
import butterknife.OnTouch;

public class MainActivity extends AppCompatActivity
        implements Runnable {

    private static BluetoothConnector.BluetoothSocketWrapper mSocket;
    private InputStream mInput;
    private OutputStream mOutput;

    private Thread mThread;
    private boolean mConectado;
    long mUltimaConexao = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mThread = new Thread(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        sendMessageBluetooth("connected:1");
        mConectado = true;
        mThread.start();
    }

    @Override
    public void onDestroy(){
        try {
            mConectado = false;
            sendMessageBluetooth("connected:0");
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            mSocket = null;
            mThread = null;
            super.onDestroy();
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        this.finish();
    }

    @Override
    public void run() {
        while(mConectado){
            String message = getMessageBluetooth();

            if(message.isEmpty()) {
                long m = Calendar.getInstance().getTime().getTime() - mUltimaConexao;
                if(m > 10000){
                    mConectado = false;
                    finish();
                }
            }else{
                mUltimaConexao =  Calendar.getInstance().getTime().getTime();
            }
        }
    }

    public static void setSocket(BluetoothConnector.BluetoothSocketWrapper socket){
        mSocket = socket;
    }

    private String getMessageBluetooth(){
        byte[] buffer = new byte[256];
        int bytes = 0;

        try{
            if(mInput == null)
                mInput = mSocket.getInputStream();

            bytes = mInput.read(buffer);
        }catch (IOException e){
            e.printStackTrace();
        }
        return new String(buffer, 0, bytes);
    }

    private void sendMessageBluetooth(String message){
        try {
            mUltimaConexao =  Calendar.getInstance().getTime().getTime();
            if(mOutput == null)
                mOutput = mSocket.getOutputStream();
            mOutput.write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnTouch(R.id.btnFrente)
    boolean onFrente(View view, MotionEvent event){
        return selectedButton(1, view, event);
    }

    @OnTouch(R.id.btnRe)
    boolean onRe(View view, MotionEvent event){
        return selectedButton(2, view, event);
    }

    @OnTouch(R.id.btnDireita)
    boolean onDireita(View view, MotionEvent event){
        return selectedButton(3, view, event);
    }

    @OnTouch(R.id.btnEsquerda)
    boolean onEsquerda(View view, MotionEvent event){
        return selectedButton(4, view, event);
    }

    private boolean selectedButton(int action, View view, MotionEvent event){
        switch (event.getAction()){
            case 2:
                view.setAlpha(0.3f);
                sendMessageBluetooth("mov:"+action);
                break;
            default:
                view.setAlpha(1);
                sendMessageBluetooth("mov:0");
                break;
        }
        return true;
    }
}
