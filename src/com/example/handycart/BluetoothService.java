package com.example.handycart;


import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class BluetoothService extends Service {
    // Constants describing types of message. Different types of messages can be
    // passed and this identifies them.

    private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();
    private BluetoothServerSocket _serverSocket;
    public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";
    private static final String TAG = BluetoothService.class.getSimpleName();
    private Handler _handler = new Handler();
    public static final int MESSAGE_TYPE_REGISTER_MAIN = 0;
    public static final int MESSAGE_TYPE_REGISTER = 1;
    public static final int MESSAGE_TYPE_TEXT = 2;
    public static final int MESSAGE_AUTH= 3;
    public static  final  int MESSAGE_LOC=4;
    public static final int MESSAGE_SCAN =5;


    public static final int LOC = 10;
    public static final int SCAN = 11;
    public static final int ERROR_AUTH = 12;

    String readMessage ="";
    Messenger msg ;
    private String mData;
    Handler hand ;
    int state = 0;
    BluetoothSocket socket;
    Messenger mResponseMessenger = null;
    final Messenger mMessenger = new Messenger(new IncomingHandler());




    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_TYPE_TEXT:
                    Bundle b = msg.getData();
                    if (b != null) {
                        Log.d("MessengerService",
                                "Service received message MESSAGE_TYPE_TEXT with: " + b.getCharSequence("data"));
                        sendToActivity("Who's there? You wrote: " + b.getCharSequence("data"));
                    } else {
                        Log.d("MessengerService", "Service received message MESSAGE_TYPE_TEXT with empty message");
                        sendToActivity("Who's there? Speak!");
                    }
                    break;
                case MESSAGE_TYPE_REGISTER:
                    Log.d("MessengerService", "Registered Activity's Messenger.");
                    mResponseMessenger = msg.replyTo;
                    _serverWorker.start();

                    if (!_bluetooth.isEnabled()) {
                        stopSelf();
                    }
                    break;
                case MESSAGE_TYPE_REGISTER_MAIN:
                    Log.d("MessengerService", "Registered MainActivity.");
                    mResponseMessenger = msg.replyTo;

                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }


    void sendToActivity(CharSequence text) {
        if (mResponseMessenger == null) {
            Log.d("MessengerService", "Cannot send message to activity - no activity registered to this service.");
        } else {
            //Recuperartion de l'entete

            String[] messageTosend = readMessage.split("-");

            String entete = messageTosend[0];
            Bundle data = new Bundle();
            data.putCharSequence("data", messageTosend[1]);

            if(entete.equals("AUT CLIENT"))
            {
                Log.d("MessengerService", text.toString());
                Message msg = Message.obtain(null, MESSAGE_AUTH);
                msg.setData(data);
                try {
                    mResponseMessenger.send(msg);
                } catch (RemoteException e) {
                    // We always have to trap RemoteException (DeadObjectException
                    // is thrown if the target Handler no longer exists)
                    e.printStackTrace();
                }

            }
            if(entete.equals("SCAN"))
            {
                Log.d("MessengerService", text.toString());
                Message msg = Message.obtain(null, MESSAGE_SCAN);
                msg.setData(data);
                try {
                    mResponseMessenger.send(msg);
                } catch (RemoteException e) {
                    // We always have to trap RemoteException (DeadObjectException
                    // is thrown if the target Handler no longer exists)
                    e.printStackTrace();
                }

            }
            if(entete.equals("LOC"))
            {
                Log.d("MessengerService", text.toString());
                Message msg = Message.obtain(null, MESSAGE_LOC);
                msg.setData(data);
                try {
                    mResponseMessenger.send(msg);
                } catch (RemoteException e) {
                    // We always have to trap RemoteException (DeadObjectException
                    // is thrown if the target Handler no longer exists)
                    e.printStackTrace();
                }

            }


        }
    }

    public IBinder onBind(Intent arg0) {
        Log.d("BluetoothService", "Binding messenger...");
        return mMessenger.getBinder();
    }


    public String getClientList(String s)
    {
        return s;
    }

    public String[] getProtocol(String s )
    {
        String[] parts = s.split("-");
        return parts;
    }

    public String getDataFromService() {
        return mData;
    }

    public void setDataToService(String data) {
        mData = data;
    }


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        stopSelf();
    }


    protected void listen() {
        try {
            _serverSocket = _bluetooth.listenUsingRfcommWithServiceRecord(PROTOCOL_SCHEME_RFCOMM,
                    UUID.fromString("a60f35f0-b93a-11de-8a39-08002009c666"));


            socket = _serverSocket.accept();
            Log.d("EF-BTBee", ">>Accept Client Request");

            while(true) {
                if (socket != null) {

                    try {
                        InputStream inputStream = socket.getInputStream();
                        byte[] buffer = new byte[1024];
                        int bytes;

                        bytes = inputStream.read(buffer);
                        readMessage = new String(buffer, 0, bytes);

                        if(readMessage!=null)
                        {
                            _handler.post(new Runnable() {
                                public void run() {
                                    Log.d("EF-BTBee", readMessage);
                                    sendToActivity(readMessage);
                                    state++;
                                }

                            });
                        }


                      /*  if (elt[0].equals("AUT CLIENT"))
                        {


                        }*/


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

        } catch (IOException e) {
            Log.e("EF-BTBee", "", e);
        } finally {
        }
    }
    private Thread _serverWorker = new Thread() {
        public void run() {
            Looper.prepare();
            listen();
        };
    };





}
