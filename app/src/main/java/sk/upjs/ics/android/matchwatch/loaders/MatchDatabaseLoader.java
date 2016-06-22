package sk.upjs.ics.android.matchwatch.loaders;


import android.content.AsyncTaskLoader;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.ArrayList;
import java.util.List;

import sk.upjs.ics.android.matchwatch.dbsEntity.MatchDatabase;
import sk.upjs.ics.android.matchwatch.openhelper.DatabaseOpenHelper;

public class MatchDatabaseLoader extends AsyncTaskLoader<List<MatchDatabase>> {

    /**
     * Helper class to look for interesting changes to the installed apps
     * so that the loader can be updated.
     */
    public static class PackageIntentReceiver extends BroadcastReceiver {
        final MatchDatabaseLoader mLoader;

        public PackageIntentReceiver(MatchDatabaseLoader loader) {
            mLoader = loader;
            IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
            filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
            filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
            filter.addDataScheme("package");
            mLoader.getContext().registerReceiver(this, filter);
            // Register for events related to sdcard installation.
            IntentFilter sdFilter = new IntentFilter();
            sdFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE);
            sdFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE);
            mLoader.getContext().registerReceiver(this, sdFilter);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            // Tell the loader about the change.
            mLoader.onContentChanged();
        }
    }

    private List<MatchDatabase> mData;
    private Context context;
    PackageIntentReceiver mPackageObserver;

    public MatchDatabaseLoader(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public List<MatchDatabase> loadInBackground() {
        List<MatchDatabase> data = new ArrayList<>();

        DatabaseOpenHelper dbHelper = new DatabaseOpenHelper(context);
        data = dbHelper.getMatchesJoin();

        return data;
    }

    @Override
    public void deliverResult(List<MatchDatabase> data) {
        if (isReset()) {
            // The Loader has been reset; ignore the result and invalidate the data.
            releaseResources(data);
            return;
        }

        // Hold a reference to the old data so it doesn't get garbage collected.
        // We must protect it until the new data has been delivered.
        List<MatchDatabase> oldData = mData;
        mData = data;

        if (isStarted()) {
            // If the Loader is in a started state, deliver the results to the
            // client. The superclass method does this for us.
            super.deliverResult(data);
        }

        // Invalidate the old data as we don't need it any more.
        if (oldData != null /*&& oldData != data*/) {
            releaseResources(oldData);
        }
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            // Deliver any previously loaded data immediately.
            deliverResult(mData);
        }

        if (mPackageObserver == null) {
            mPackageObserver = new PackageIntentReceiver(this);
        }

        // Begin monitoring the underlying data source.
        if (takeContentChanged() || mData == null || mData.size() == 0) {
            // mObserver = new SampleObserver();
            forceLoad();
            // TODO: register the observer
        }

        /*if (takeContentChanged() || mData == null) {
            // When the observer detects a change, it should call onContentChanged()
            // on the Loader, which will cause the next call to takeContentChanged()
            // to return true. If this is ever the case (or if the current data is
            // null), we force a new load.
            forceLoad();
        }*/
    }

    @Override
    protected void onStopLoading() {
        // The Loader is in a stopped state, so we should attempt to cancel the
        // current load (if there is one).
        cancelLoad();

        // Note that we leave the observer as is. Loaders in a stopped state
        // should still monitor the data source for changes so that the Loader
        // will know to force a new load if it is ever started again.
    }

    @Override
    protected void onReset() {
        // Ensure the loader has been stopped.
        super.onReset();

        onStopLoading();

        // At this point we can release the resources associated with 'mData'.
        if (mData != null /*&& mData.size() > 0*/) {
            releaseResources(mData);
        }

        // Stop monitoring for changes.
        if (mPackageObserver != null) {
            getContext().unregisterReceiver(mPackageObserver);
            mPackageObserver = null;
        }

        mData = null;
    }

    @Override
    public void onCanceled(List<MatchDatabase> data) {
        // Attempt to cancel the current asynchronous load.
        super.onCanceled(data);

        // The load has been canceled, so we should release the resources
        // associated with 'data'.
        releaseResources(data);
    }

    private void releaseResources(List<MatchDatabase> data) {
        // For a simple List, there is nothing to do. For something like a Cursor, we
        // would close it in this method. All resources associated with the Loader
        // should be released here.
        if (data != null && data.size() > 0) {
            data.clear();
        }
    }
}

