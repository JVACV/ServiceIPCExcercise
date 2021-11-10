package com.example.serviceipcexcercise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private final int JOB_1 = 1;
    private final int JOB_2 = 2;
    private final int JOB_1_RESPONSE = 3;
    private final int JOB_2_RESPONSE = 4;
    boolean isBind = false;
    Messenger messenger = null;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         Intent intent = new Intent(this,MyService.class);
         bindService(intent, serviceConnection, BIND_AUTO_CREATE);
         textView = (TextView) findViewById(R.id.textView);
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            messenger = new Messenger(iBinder);

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            messenger = null;
            isBind = false;
        }
    };

    public void getMessage(View view) {
        String button_text = (String) ((Button)view).getText();
        if (button_text.equals("GET FIRST MESSAGE")){
            Message msg = Message.obtain(null,JOB_1);
            msg.replyTo = new Messenger(new ResponseHandler());
            try {
                messenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        else if (button_text.equals("GET SECOND MESSAGE")){
            Message msg = Message.obtain(null,JOB_2);
            msg.replyTo = new Messenger(new ResponseHandler());
            try {
                messenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStop() {
        unbindService(serviceConnection);
        isBind = false;
        messenger = null;
        super.onStop();
    }

    class ResponseHandler extends Handler{

        @Override
        public void handleMessage(@NonNull Message msg) {

            String message;
            switch (msg.what){
                case JOB_1_RESPONSE:
                    message = msg.getData().getString("response_message");
                    textView.setText(message);
                case JOB_2_RESPONSE:
                    message = msg.getData().getString("response_message");
                    textView.setText(message);
            }



            super.handleMessage(msg);
        }
    }
}