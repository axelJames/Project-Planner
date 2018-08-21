package com.example.axel.projectplanner;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ProjectSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static ProjectSyncAdapter sProjectSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("ProjectSyncService", "onCreate - SunshineSyncService");
        synchronized (sSyncAdapterLock) {
            if (sProjectSyncAdapter == null) {
                sProjectSyncAdapter = new ProjectSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sProjectSyncAdapter.getSyncAdapterBinder();
    }
}
