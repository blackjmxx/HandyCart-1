    package com.example.handycart;


    import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

    public class FirstView extends Activity{

        private static final int ON_BIND = 1;
        private ServiceConnection mServiceConnection;
        private Handler mHandler;
        private Intent intent;
        public static final int AUTH  = 0;
        public static final int MESSAGE_TYPE_REGISTER = 1;
        public static final int AUTHANDLIST= 5;
        public static final int CODE_RETOUR = 7;
        public static Context mContext;

        final Messenger mMessenger = new Messenger(new IncomingHandler());
        /**
         * Messenger used for communicating with service.
         */
        Messenger mService = null;
        boolean mServiceConnected = false;




        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.firstview);
            mContext = getBaseContext();
            bindService(new Intent(this,BluetoothService.class), mConn, Context.BIND_AUTO_CREATE);


        }

        public static Context getContext() {
            return mContext;
        }

        class IncomingHandler extends Handler {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == BluetoothService.MESSAGE_TYPE_TEXT) {
                    Bundle b = msg.getData();
                    CharSequence text = null;
                    if (b != null) {
                        text = b.getCharSequence("data");
                        Intent monIntent = new Intent(mContext,MainActivity.class);
                        monIntent.putExtra("data",text);
                        startActivityForResult(monIntent,CODE_RETOUR);
                        finish();
                    } else {
                        text = "Service responded with empty message";
                    }
                    Log.d("MessengerActivity", "Response: " + text);
                } else {
                    super.handleMessage(msg);
                }
            }
        }

        public void onDestroy() {
            super.onDestroy();
            //on supprimer le binding entre l'activit� et le service.
            if (mServiceConnected) {
                unbindService(mConn);
                // stopService(new Intent(this, BluetoothService.class));
                mServiceConnected = false;
            }

        }


        private ServiceConnection mConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName className, IBinder service) {
                Log.d("MessengerActivity", "Connected to service. Registering our Messenger in the Service...");
                mService = new Messenger(service);
                mServiceConnected = true;

                // Register our messenger also on Service side:
                Message msg = Message.obtain(null, BluetoothService.MESSAGE_TYPE_REGISTER);
                msg.replyTo = mMessenger;
                try {
                    mService.send(msg);
                } catch (RemoteException e) {
                    // We always have to trap RemoteException (DeadObjectException
                    // is thrown if the target Handler no longer exists)
                    e.printStackTrace();
                }
            }

            /**
             * Connection dropped.
             */
            @Override
            public void onServiceDisconnected(ComponentName className) {
                Log.d("MessengerActivity", "Disconnected from service.");
                mService = null;
                mServiceConnected = false;
            }
        };

        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        protected void onStop() {
            super.onStop();

        }


    }
