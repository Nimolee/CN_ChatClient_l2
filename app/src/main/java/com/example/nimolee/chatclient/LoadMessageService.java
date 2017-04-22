package com.example.nimolee.chatclient;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

import java.io.DataInputStream;

public class LoadMessageService extends Service {
    public static ListAdapterForMassage listAdapterForMassage;
    public static DataInputStream _DIS;
    public static ListAdapterForSelectUser listAdapterForSelectUser;

    public LoadMessageService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        final String msg = _DIS.readUTF();
                        switch (msg.split("\n")[0]) {
                            case "m":
                                listAdapterForMassage.get_messages().add(new Message(msg));
                                Handler handler = new Handler(Looper.getMainLooper());
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        listAdapterForMassage.notifyDataSetChanged();
                                        NewMessageNotification newMessageNotification = new NewMessageNotification();
                                        synchronized (newMessageNotification) {
                                            NewMessageNotification.notify(getBaseContext(), msg, 0);
                                        }
                                    }
                                });
                                break;
                            case "u":
                                for (int i = 1; i < msg.split("\n").length; i++) {
                                    listAdapterForSelectUser.addUser(Integer.parseInt(msg.split("\n")[i].split(" ")[0]), msg.split("\n")[i].split(" ")[1]);
                                }
                                break;
                        }
                    }
                } catch (Exception e) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(), "Втрачено зв'язок з сервером.", Toast.LENGTH_LONG).show();
                        }
                    });
                    stopSelf();
                    e.printStackTrace();
                }
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }
}
