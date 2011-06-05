package app.ws;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.security.SecureRandom;

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
import android.widget.RemoteViews;
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
				updateWidget(Color.GRAY);
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

	protected void updateWidget(int color) {
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
		int widgetIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(this, StarWidget.class));
		if (widgetIds != null && widgetIds.length > 0) {
			appWidgetManager.updateAppWidget(widgetIds, newRemoteViews(color));
		}
	}

	protected RemoteViews newRemoteViews(int color) {
		RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.widget);
		int imgId = R.drawable.gray;
		if (color == Color.GREEN) {
			imgId = R.drawable.green;
		} else if (color == Color.RED) {
			imgId = R.drawable.red;
		} else if (color == Color.BLUE) {
			imgId = R.drawable.blue;
		}
		views.setImageViewResource(R.id.widget_image, imgId);
		views.setOnClickPendingIntent(R.id.widget_image, getLaunchIntent());
		return views;
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
			long colorIndex = new SecureRandom().nextLong() % 3;
			int color = Color.GRAY;
			if (colorIndex == 0) {
				color = Color.GREEN;
			} else if (colorIndex == 1) {
				color = Color.RED;
			} else {
				color = Color.BLUE;
			}
			if (mPrevColor != color) {
				notifyMessage(color, System.currentTimeMillis());
				mService.updateWidget(color);
			}
		}

		private void notifyMessage(int color, long now) {
			TimerService sv = mService;
			Notification notify = new Notification(R.drawable.icon, sv.getString(R.string.app_name), now);
			PendingIntent intent = sv.getLaunchIntent();
			String title = "Service Notification";
			StringBuilder message = new StringBuilder("Color change [");
			if (color == Color.GREEN) {
				message.append("GREEN");
			} else if (color == Color.RED) {
				message.append("RED");
			} else if (color == Color.BLUE) {
				message.append("BLUE");
			} else {
				message.append("GRAY");
			}
			message.append("]");
			notify.setLatestEventInfo(sv, title, message.toString(), intent);
			int cancelId = sv.getCancelId();
			mNotifier.cancel(cancelId);
			mNotifier.notify(cancelId, notify);
		}

	}
}