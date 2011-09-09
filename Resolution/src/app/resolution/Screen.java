package app.resolution;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

public class Screen extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		int id = R.id.ldpi;
		if (metrics.densityDpi <= 120) {
			id = R.id.ldpi;
		} else if (120 < metrics.densityDpi && metrics.densityDpi <= 160) {
			id = R.id.mdpi;
		} else if (160 < metrics.densityDpi && metrics.densityDpi <= 240) {
			id = R.id.hdpi;
		} else if (120 < metrics.densityDpi) {
			id = R.id.xhdpi;
		}

		TextView dp = (TextView)findViewById(id);
		dp.setTextColor(android.graphics.Color.GREEN);
		dp.setBackgroundColor(android.graphics.Color.MAGENTA);

		// DPI
		StringBuilder txt = new StringBuilder();
		txt.append("DPI = ").append(metrics.densityDpi).append("\nSTRING: getString(R.string.resolution) --> ").append(getString(R.string.resolution));

		((TextView)findViewById(android.R.id.text1)).setText(txt.toString());

		// Info
		txt.setLength(0);
		txt.append("density=").append(metrics.density).append('\n');
		txt.append("scaleDensity=").append(metrics.scaledDensity).append('\n');
		txt.append("widthPixels=").append(metrics.widthPixels).append('\n');
		txt.append("hightPixels=").append(metrics.heightPixels).append('\n');
		txt.append("xDpi=").append(metrics.xdpi).append('\n');
		txt.append("yDpi=").append(metrics.ydpi).append('\n');

		((TextView)findViewById(android.R.id.text2)).setText(txt.toString());
     }
}
