package app.note;

import android.test.ActivityInstrumentationTestCase2;

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
		getActivity().runOnUiThread( new Runnable() {
				@Override
				public void run() {
				}
			});

		getInstrumentation().waitForIdleSync();

		int count = getActivity().getListView().getAdapter().getCount();
		android.util.Log.i("Test", "### count " + count);

		assertTrue(count > 0);
	}

}
