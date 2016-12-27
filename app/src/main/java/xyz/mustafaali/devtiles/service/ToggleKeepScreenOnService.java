package xyz.mustafaali.devtiles.service;

import android.provider.Settings;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;
import android.widget.Toast;

import xyz.mustafaali.devtiles.R;

public class ToggleKeepScreenOnService extends TileService {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void onStartListening() {
        super.onStartListening();
        updateTile();
    }

    @Override
    public void onClick() {
        int newValue = isKeepScreenOnEnabled() ? 0 : 1;

        try {
            Settings.Global.putInt(getContentResolver(), Settings.Global.STAY_ON_WHILE_PLUGGED_IN, newValue);
        } catch (SecurityException se) {
            String message = getString(R.string.permission_required_toast);
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            Log.e(TAG, message);
        }

        updateTile();
    }

    private boolean isKeepScreenOnEnabled() {
        try {
            return Settings.Global.getInt(getContentResolver(), Settings.Global.STAY_ON_WHILE_PLUGGED_IN) == 1;
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
        return false;
    }

    private void updateTile() {
        final Tile tile = getQsTile();

        if (isKeepScreenOnEnabled()) {
            tile.setState(Tile.STATE_ACTIVE);
        } else {
            tile.setState(Tile.STATE_INACTIVE);
        }

        tile.updateTile();

    }
}
