package iii.org.tw.threadtest;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private TextView textView1;
    private OSHandler handler;
    private Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView1 = (TextView) findViewById(R.id.textView1);
        handler = new OSHandler();
        timer = new Timer();
    }

    public void test1(View v) {
        Thread1 thread = new Thread1();
        thread.start();
    }

    public void test2(View v) {
        MyTask myt = new MyTask();
        timer.schedule(myt , 200 , 200);
    }

    private class Thread1 extends Thread {
        @Override
        public void run() {
            for (int i=0 ; i<10 ; i++){
                Log.d("Abner","i=" + i);
                //textView1.setText("i=" + i);  //-----會出現錯誤
                //handler.sendEmptyMessage(i);
                Message mesg = new Message();
                Bundle data = new Bundle();
                data.putInt("i",i);
                mesg.setData(data);
                handler.sendMessage(mesg);

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                }
            }
        }
    }


    private class MyTask extends TimerTask {
        private int i;
        @Override
        public void run() {
            Log.d("Abner" , "i = " + i++);
            Message mesg = new Message();
            Bundle data = new Bundle();
            data.putInt("i",i);
            mesg.setData(data);
            handler.sendMessage(mesg);
        }
    }



    //-----在Android系統中 背景無法直接存取前景，需使用Handler來搭一個橋樑

    private class OSHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            int i = data.getInt("i");
            textView1.setText("i = " + i);
            //textView1.setText("i = " + msg.what);
        }
    }
}
