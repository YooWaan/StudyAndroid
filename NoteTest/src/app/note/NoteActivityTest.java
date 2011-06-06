package app.note;

import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Button;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.util.Log;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class app.note.NoteActivityTest \
 * app.note.tests/android.test.InstrumentationTestRunner
 */
public class NoteActivityTest extends ActivityInstrumentationTestCase2<NoteActivity> {

    public NoteActivityTest() {
        super("app.note", NoteActivity.class);
    }


	public void testList() {
		try {
			ActivityMonitor monitor = new ActivityMonitor(app.note.EditActivity.class.getName(), null, false);
			Instrumentation inst = getInstrumentation();
			inst.addMonitor(monitor);
			NoteActivity note = (NoteActivity)getActivity();

			inst.waitForIdleSync();

			int startCount = getActivity().getListView().getAdapter().getCount();
			Log.i("Test", "### count " + startCount);

			assertTrue(startCount >= 0);

			final Button btn = (Button)note.findViewById(R.id.btn_add);
			//TouchUtils.clickView(this ,btn);
			note.runOnUiThread( new Runnable() {
					@Override
						public void run() {
						btn.performClick();
					}
				});


			EditActivity edit = (EditActivity)getInstrumentation().waitForMonitorWithTimeout(monitor, 2000); 

			assertNotNull(edit);

			final EditText title = (EditText)edit.findViewById(R.id.title);
			final EditText body = (EditText)edit.findViewById(R.id.body);

			edit.runOnUiThread( new Runnable() {
					@Override
					public void run() {
						title.setText("test title");
						body.setText("body text");
					}
				});


			inst.waitForIdleSync();

			final Button save = (Button)edit.findViewById(R.id.btn_save);

			//TouchUtils.clickView(this ,save);
			edit.runOnUiThread( new Runnable() {
					@Override
					public void run() {
						save.performClick();
					}
				});

			//edit.finish();

			inst.waitForIdleSync();
			//note.refreshList();

			int insertCount = getActivity().getListView().getAdapter().getCount();
			Log.i("Test", "### update count " + insertCount);

			assertTrue(insertCount > startCount);

		} catch (Exception e) {
			e.printStackTrace();
			Log.i("### Test ###", "Err --> " + e);
			fail();
		}
	}

}
