package com.coco.coupons.app.https;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import android.os.AsyncTask;
import android.util.Log;

public class ConnectionHandler extends AsyncTask<Void, Void, Void>{

public static String serverip = "211.147.70.11";
public static int serverport = 7926;
Socket s;
public DataInputStream dis;
public DataOutputStream dos;
public int message;

@Override
protected Void doInBackground(Void... params) {

    try {
        Log.i("AsyncTank", "doInBackgoung: Creating Socket");
        s = new Socket(serverip, serverport);
    } catch (Exception e) {
        Log.i("AsyncTank", "doInBackgoung: Cannot create Socket");
    }
    if (s.isConnected()) {
        try {
            dis = (DataInputStream) s.getInputStream();
            dos = (DataOutputStream) s.getOutputStream();
            Log.i("AsyncTank", "doInBackgoung: Socket created, Streams assigned");

        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.i("AsyncTank", "doInBackgoung: Cannot assign Streams, Socket not connected");
            e.printStackTrace();
        }
    } else {
        Log.i("AsyncTank", "doInBackgoung: Cannot assign Streams, Socket is closed");
    }
    return null;
}

public void writeToStream(String msg) {
    try {
        if (s.isConnected()){
            Log.i("AsynkTask", "writeToStream : Writing lat, lon");
            dos.write(msg.getBytes());
//            dos.writeDouble(lon);
        } else {
            Log.i("AsynkTask", "writeToStream : Cannot write to stream, Socket is closed");
        }
    } catch (Exception e) {
        Log.i("AsynkTask", "writeToStream : Writing failed");
    }
}

public int readFromStream() {
    try {
        if (s.isConnected()) {
            Log.i("AsynkTask", "readFromStream : Reading message");
            message = dis.readInt();
        } else {
            Log.i("AsynkTask", "readFromStream : Cannot Read, Socket is closed");
        }
    } catch (Exception e) {
        Log.i("AsynkTask", "readFromStream : Writing failed");
    }
    return message;
}

}