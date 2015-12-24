package app.note;

import android.test.ActivityInstrumentationTestCase2;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class app.note.NoteTest \
 * app.note.tests/android.test.InstrumentationTestRunner
 */
public class NoteTest extends ActivityInstrumentationTestCase2<Note> {

    public NoteTest() {
        super("app.note", Note.class);
    }

}
