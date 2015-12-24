package app.hello;

import android.test.ActivityInstrumentationTestCase2;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class app.hello.HelloActivityTest \
 * app.hello.tests/android.test.InstrumentationTestRunner
 */
public class HelloActivityTest extends ActivityInstrumentationTestCase2<HelloActivity> {

    public HelloActivityTest() {
        super("app.hello", HelloActivity.class);
    }

}
