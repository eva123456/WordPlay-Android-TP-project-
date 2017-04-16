package com.example.eva.wordplay.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.Hashtable;
import java.util.Map;

public class NetworkHelper {

    private final static String TAG = NetworkHelper.class.getSimpleName();
    private int mIdCounter = 1;
    private final Map<Integer, ResultListener> mListeners = new Hashtable<>();

    private static NetworkHelper instance;

    private NetworkHelper() {
    }

    public synchronized static NetworkHelper getInstance(final Context context) {
        if (instance == null) {
            instance = new NetworkHelper();
            instance.initBroadcastReceiver(context);
        }
        return instance;
    }

    private void initBroadcastReceiver(Context context) {
        final IntentFilter filter = new IntentFilter();
        filter.addAction(NetworkIntentService.ACTION_WEB_RESULT_SUCCESS);
        filter.addAction(NetworkIntentService.ACTION_WEB_RESULT_ERROR);
        LocalBroadcastManager.getInstance(context).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                final int requestId = intent.getIntExtra(NetworkIntentService.EXTRA_REQUEST_ID, -1);
                final ResultListener listener = mListeners.remove(requestId);

                if (listener != null) { //TODO надо различать пришедшие ответы на разные запросы
                    final String result = intent.getStringExtra(NetworkIntentService.EXTRA_WEB_RESULT);
                    final boolean success = intent.getAction().equals(NetworkIntentService.ACTION_WEB_RESULT_SUCCESS);
                    listener.onResult(success, result);
                }
            }
        }, filter);
    }

    public int sendRequest(final Context context, final String text, final ResultListener listener) {
        mListeners.put(mIdCounter, listener);
        Log.d(TAG,"Get request " + text);
        Intent intent = new Intent(context, NetworkIntentService.class);
        intent.setAction(NetworkIntentService.ACTION_WEB);
        intent.putExtra(NetworkIntentService.EXTRA_WEB_TEXT, text);
        intent.putExtra(NetworkIntentService.EXTRA_REQUEST_ID, mIdCounter);
        context.startService(intent);

        return mIdCounter++;
    }

    int viewAllDecksRequest(final Context context, final ResultListener listener) {
        mListeners.put(mIdCounter, listener);
        Intent intent = new Intent(context, NetworkIntentService.class);
        intent.setAction(NetworkIntentService.ACTION_VIEW_DECKS);
        intent.putExtra(NetworkIntentService.EXTRA_REQUEST_ID, mIdCounter);
        context.startService(intent);

        return mIdCounter++;
    }

    public interface ResultListener {
        void onResult(final boolean success, final String result);
    }
}
