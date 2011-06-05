package app.ws;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;


public class WidgetAndService extends Activity {

	private static final int SERVICE_START = Menu.FIRST;
	private static final int SERVICE_STOP = Menu.FIRST + 1;



    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		Button btn = (Button)findViewById(R.id.btn_start);
		btn.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					serviceEnabled(true);
				}
			});

		btn = (Button)findViewById(R.id.btn_stop);
		btn.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					serviceEnabled(false);
				}
			});

    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, SERVICE_START, 0, getString(R.string.menu_start)).setIcon(android.R.drawable.ic_media_play);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		int menuIndex = 0;
		MenuItem item = menu.getItem(menuIndex);
		menu.removeItem(item.getItemId());
		if (isServiceRunning()) {
			menu.add(0, SERVICE_STOP, 0, getString(R.string.menu_stop)).setIcon(android.R.drawable.ic_media_pause);
		} else {
			menu.add(0, SERVICE_START, 0, getString(R.string.menu_start)).setIcon(android.R.drawable.ic_media_play);
		}
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		serviceEnabled(item.getItemId() == SERVICE_START);
		return true;
	}

	private void serviceEnabled(boolean enable) {
		Intent intent = new Intent(this,TimerService.class);
		if (enable) {
			startService(intent);
		} else {
			stopService(intent);
		}
	}

	protected boolean isServiceRunning() {
		ActivityManager am = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
		ComponentName serviceComponent = new ComponentName(this, TimerService.class);
		// android.util.Log.i("isMonitorServiceRunning ????" , monitorService.toString() ) ;
		try {
			List<RunningServiceInfo> infos = am.getRunningServices(Integer.MAX_VALUE);
			for (int i = 0, len = infos.size(); i < len; i++) {
				RunningServiceInfo info = infos.get(i);
				if (serviceComponent.equals(info.service)) {
					return info.started;
				}
			}
		} catch (Throwable e) {
			// ignore
		}
		return false;
	}

}
