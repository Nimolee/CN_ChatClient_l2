package com.example.nimolee.chatclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    int port = 2170;
    DataOutputStream _DOS;
    DataInputStream _DIS;
    ListAdapterForSelectUser listAdapterForSelectUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }

    public void login(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket;
                InetAddress inetAddress;
                try {
                    String ip = ((TextView) findViewById(R.id.login_ip)).getText().toString();
                    inetAddress = InetAddress.getByName(ip);
                    try {
                        socket = new Socket(inetAddress, port);
                        socket.setSoTimeout(0);
                    } catch (Exception e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getBaseContext(), "Сервер недоступний.", Toast.LENGTH_LONG).show();
                            }
                        });
                        return;
                    }
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    String loginData = "l\n";
                    loginData += ((EditText) findViewById(R.id.login_username)).getText().toString() + "\n";
                    loginData += ((EditText) findViewById(R.id.login_password)).getText().toString() + "\n";
                    dataOutputStream.writeUTF(loginData);
                    String answer = dataInputStream.readUTF();
                    if (answer.equals("l-ok")) {
                        _DIS = dataInputStream;
                        _DOS = dataOutputStream;
                        listAdapterForSelectUser = new ListAdapterForSelectUser(getBaseContext(), _DOS);
                        final ListAdapterForMassage listAdapterForMassage = new ListAdapterForMassage(getBaseContext());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setContentView(R.layout.drawable_layout);
                                ((ListView) findViewById(R.id.main_LV)).setAdapter(listAdapterForMassage);
                                ((ListView) findViewById(R.id.drawer_LV)).setAdapter(listAdapterForSelectUser);
                            }
                        });
                        LoadMessageService.listAdapterForMassage = listAdapterForMassage;
                        LoadMessageService.listAdapterForSelectUser = listAdapterForSelectUser;
                        LoadMessageService._DIS = _DIS;
                        startService(new Intent(getBaseContext(), LoadMessageService.class));
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getBaseContext(), "Неправильне співвідношення логіна та паролю", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void openLogin(View view) {
        setContentView(R.layout.login);
    }

    public void openSignUp(View view) {
        setContentView(R.layout.sign_up);
    }

    public void sign_up(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket;
                InetAddress inetAddress;
                try {
                    inetAddress = InetAddress.getByName(((EditText) findViewById(R.id.sign_up_ip)).getText().toString());
                    socket = new Socket(inetAddress, port);
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    String signUpData = "r\n";
                    signUpData += ((EditText) findViewById(R.id.sign_up_username)).getText().toString() + "\n";
                    if (!signUpData.contains(" ")) {
                        String signUpPassword = ((EditText) findViewById(R.id.sign_up_password)).getText().toString();
                        if (signUpPassword.length() > 3 && signUpPassword.length() < 33) {
                            if (!signUpPassword.contains(" ")) {
                                if (signUpPassword.equals(((EditText) findViewById(R.id.sign_up_confPassword)).getText().toString())) {
                                    signUpData += signUpPassword + "\n";
                                    dataOutputStream.writeUTF(signUpData);
                                    String answer = dataInputStream.readUTF();
                                    if (answer.equals("s-ok")) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getBaseContext(), "Реєстрація пройшла успішно.", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    } else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getBaseContext(), "Даний логін вже зайнято.", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getBaseContext(), "Не співпадають паролі.", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getBaseContext(), "Пароль не повинен містити пробіли.", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getBaseContext(), "Неприпустима довжина паролю.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getBaseContext(), "Логін не повинен містити пробіли.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } catch (IOException e) {
                    Toast.makeText(getBaseContext(), "Сервер недоступний.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void send(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    _DOS.writeUTF("m\n" + ((TextView) findViewById(R.id.main_writeM)).getText().toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((TextView) findViewById(R.id.main_writeM)).setText("");
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
