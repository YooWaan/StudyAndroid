package app.ws;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.ComponentName;
import android.graphics.Color;
import android.widget.Toast;
import android.os.IBinder;
import android.util.Log;

public class TimerService extends Service {

	private final static Object LOCK = new Object();
	private Timer mTimer = null;
	private int mCancelId = 1;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		Log.i("Timer" , "onStart()");

		synchronized (LOCK) {
			if (startTimer()) {
				mCancelId = startId;
				Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i("Timer" , "onDestroy()");

		synchronized (LOCK) {
			if (stopTimer()) {
				Toast.makeText(this, "Service stoped", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private boolean startTimer() {
		if (this.mTimer != null) {
			return false;
		}
		NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		mTimer = new Timer();
		mTimer.schedule(new SampleTimerTask(this, nm), 1000, 3000);
		return true;
	}

	private boolean stopTimer() {
		if (this.mTimer == null) {
			return false;
		}
		mTimer.cancel();
		mTimer = null;
		return true;
	}

	protected int getCancelId() {
		return mCancelId;
	}

	protected PendingIntent getLaunchIntent() {
		Context ctx = this;
		Intent launchIntent = new Intent();
		launchIntent.setComponent(new ComponentName(ctx, WidgetAndService.class));
		launchIntent.setAction(Intent.ACTION_MAIN);
		launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		return PendingIntent.getActivity(ctx, 0, launchIntent, 0);
	}

	class SampleTimerTask extends TimerTask {

		private TimerService mService;
		private NotificationManager mNotifier;
		private int mPrevColor = -1;

		public SampleTimerTask(TimerService s, NotificationManager nmgr) {
			mService = s;
			mNotifier = nmgr;
		}

		@Override
		public void run() {
			long now = System.currentTimeMillis();
			long colorIndex = now % 3;
			int color = Color.GRAY;
			if (colorIndex == 0) {
				color = Color.GREEN;
			} else if (colorIndex == 1) {
				color = Color.RED;
			} else {
				color = Color.BLUE;
			}
			if (mPrevColor != color) {
				notifyMessage(color, now);
			}
		}

		private void notifyMessage(int color, long now) {
			TimerService sv = mService;
			Notification notify = new Notification(R.drawable.icon, sv.getString(R.string.app_name), now);
			PendingIntent intent = sv.getLaunchIntent();
			String title = "Service Notification";
			String message = "Color change [" + color + "]";
			notify.setLatestEventInfo(sv, title, message, intent);
			int cancelId = sv.getCancelId();
			mNotifier.cancel(cancelId);
			mNotifier.notify(cancelId, notify);
		}

	}
}