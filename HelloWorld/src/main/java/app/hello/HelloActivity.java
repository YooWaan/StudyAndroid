package app.hello;

import android.app.Activity;
import android.os.Bundle;

// add import
import android.widget.TextView;
import android.content.pm.PackageManager;
import android.content.pm.PackageInfo;


public class HelloActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // add code
        // set version code, name
		TextView txt = (TextView)findViewById(R.id.versionCode);
		txt.setText("VERSION Code [" + getVersionCode() + "]");

		txt = (TextView)findViewById(R.id.versionName);
		txt.setText("VERSION Name [" + getVersionName() + "]");
    }

	/**
	 * @return version Code
	 */
	public int getVersionCode()
	{
		try {
			PackageManager manager = getPackageManager();
			PackageInfo packInfo = manager.getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
			return packInfo.versionCode;
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * @return version Name
	 */
	public String getVersionName()
	{
		try {
			PackageManager manager = getPackageManager();
			PackageInfo packInfo = manager.getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
			return packInfo.versionName;
		} catch (Exception e) {
			return "---";
		}
	}

}
