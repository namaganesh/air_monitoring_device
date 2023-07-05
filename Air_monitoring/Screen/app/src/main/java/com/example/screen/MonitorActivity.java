package com.example.screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MonitorActivity extends AppCompatActivity {
ImageView left_arrow;
ImageView synchronize;

    ServerSocket serverSocket;
    Thread Thread1 = null;
    TextView box1, box2,box3,box4,box5,box6;
  
    public static String SERVER_IP = "";
    public static final int SERVER_PORT = 5000;
    String message;
    int numChars = 0;
    float valCO2 = 0f;
    float valPM2 = 0f;
    float valHumidity = 0f;
    float valTemp= 0f;
    float valVOC = 0f;
    float valHCHO = 0f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);

        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        box1= findViewById(R.id.box1);
        box2= findViewById(R.id.box2);
        box3= findViewById(R.id.box3);
        box4= findViewById(R.id.box4);
        box5= findViewById(R.id.box5);
        box6= findViewById(R.id.box6);

        left_arrow = (ImageView)findViewById(R.id.left_arrow);;
        left_arrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view1) {
                Intent i = new Intent(MonitorActivity.this, NotificationActivity.class);
                startActivity(i);
            }
        });


        try {
            SERVER_IP = getLocalIpAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        Thread1 = new Thread(new Thread1());
        Thread1.start();
      
    }


    private String getLocalIpAddress() throws UnknownHostException {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        assert wifiManager!= null;
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipInt = wifiInfo.getIpAddress();
        return InetAddress.getByAddress(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(ipInt).array()).getHostAddress();
    }
    private PrintWriter output;
    private DataInputStream input;
    class Thread1 implements Runnable {
        @Override
        public void run() {
            Socket socket;
            try {
                serverSocket = new ServerSocket(SERVER_PORT);
              
                try {
                    socket = serverSocket.accept();
                    output = new PrintWriter(socket.getOutputStream());

                    input = new DataInputStream(socket.getInputStream());
                   
                    new Thread(new Thread2()).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private class Thread2 implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    byte[] buffer = new byte[64];
                    
                    numChars = 0;
                  
                    numChars = input.read(buffer);
                  
                    if (numChars > 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final String bufArray = Integer.toString(buffer[0] & 0xff) + " " + Integer.toString(buffer[1] & 0xff) +
                                        " " + Integer.toString(buffer[2] & 0xff) + " " + Integer.toString(buffer[3] & 0xff) +
                                        " " + Integer.toString(buffer[4] & 0xff) + " " + Integer.toString(buffer[5] & 0xff) +
                                        " " + Integer.toString(buffer[6] & 0xff) + " " + Integer.toString(buffer[7] & 0xff) +
                                        " " + Integer.toString(buffer[8] & 0xff) + " " + Integer.toString(buffer[9] & 0xff) +
                                        " " + Integer.toString(buffer[10] & 0xff) + " " + Integer.toString(buffer[11] & 0xff) +
                                        " " + Integer.toString(buffer[12] & 0xff) + " " + Integer.toString(buffer[13] & 0xff) +
                                        " " + Integer.toString(buffer[14] & 0xff) + " " + Integer.toString(buffer[15] & 0xff) +
                                        " " + Integer.toString(buffer[16] & 0xff);

                                Log.v("MainActivity", "BufferArray: " + bufArray);
                                valCO2 = ((int)(buffer[3] & 0xff) * 256) + (int)(buffer[4] & 0xff);       
                                box1.setText("CO2 " + Float.toString(valCO2) + "ppm"); 
                               
                                valPM2 = ((int)(buffer[5] & 0xff) * 256) + (int)(buffer[6] & 0xff);
                                box2.setText("PM2.5 " + Float.toString(valPM2) + "ug/m3");
                                
                        

                                valHumidity = ((int)((buffer[11] & 0xff) * 256) + (int)(buffer[12] & 0xff))/(10);
                                box3.setText("Humidity " + Float.toString(valHumidity) + "%");
                                

                                valTemp = ((int)((buffer[13] & 0xff) * 256) + (int)(buffer[14] & 0xff) - 500)/(10);
                                box4.setText("Temperature " + Float.toString(valTemp) + (char) 0x00B0 + "C");
                               
                                valVOC = ((int)(buffer[7] & 0xff) * 256) + (int)(buffer[8] & 0xff);
                                box5.setText("VOC " + Float.toString(valVOC));

                                valHCHO = ((int)(buffer[9] & 0xff) * 256) + (int)(buffer[10] & 0xff);
                                box6.setText("HCHO " + Float.toString(valHCHO));

                            }
                        });
                    } else {
                        Thread1 = new Thread(new Thread1());
                        Thread1.start();
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
  

