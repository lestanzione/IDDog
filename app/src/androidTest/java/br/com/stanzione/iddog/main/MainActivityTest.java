package br.com.stanzione.iddog.main;

import android.content.Intent;
import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;

import br.com.stanzione.iddog.App;
import br.com.stanzione.iddog.R;
import br.com.stanzione.iddog.di.AndroidModule;
import br.com.stanzione.iddog.di.ApplicationComponent;
import br.com.stanzione.iddog.di.DaggerApplicationComponent;
import br.com.stanzione.iddog.di.MockNetworkModule;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okio.BufferedSource;
import okio.Okio;
import okio.Source;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class, true, false);

    private MockWebServer server = new MockWebServer();

    @Before
    public void setUp() throws Exception {
        setUpServer();
        server.start();
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }

    public void setUpServer() {
        ApplicationComponent applicationComponent = DaggerApplicationComponent.builder()
                .networkModule(new MockNetworkModule(server))
                .androidModule(new AndroidModule(getTargetContext()))
                .build();

        ((App) getTargetContext().getApplicationContext())
                .setApplicationComponent(applicationComponent);
    }

    @Test
    public void withInvalidEmailShouldShowMessage() {

        activityRule.launchActivity(new Intent());

        onView(withId(R.id.emailEditText))
                .perform(typeText("testmail.com"));

        pressBack();

        onView(withId(R.id.loginButton))
                .perform(click());

        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText("Invalid email")))
                .check(matches(isDisplayed()));

    }

    @Test
    public void withValidEmailShouldPass() throws IOException, InterruptedException {

        server.enqueue(new MockResponse()
                .setBody(readFile("token_response.json")));

        activityRule.launchActivity(new Intent());

        onView(withId(R.id.emailEditText))
                .perform(typeText("test@mail.com"));

        pressBack();

        onView(withId(R.id.loginButton))
                .perform(click());

    }

    private String readFile(String fileName) throws IOException {
        InputStream stream = InstrumentationRegistry.getContext()
                .getAssets()
                .open(fileName);

        Source source = Okio.source(stream);
        BufferedSource buffer = Okio.buffer(source);

        return buffer.readUtf8();
    }

}