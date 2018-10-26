package br.com.stanzione.iddog.doglist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
import br.com.stanzione.iddog.RecyclerViewItemCountAssertion;
import br.com.stanzione.iddog.di.AndroidModule;
import br.com.stanzione.iddog.di.ApplicationComponent;
import br.com.stanzione.iddog.di.DaggerApplicationComponent;
import br.com.stanzione.iddog.di.MockNetworkModule;
import br.com.stanzione.iddog.main.MainActivity;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okio.BufferedSource;
import okio.Okio;
import okio.Source;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class DogListActivityTest {

    @Rule
    public ActivityTestRule<DogListActivity> activityRule = new ActivityTestRule<>(DogListActivity.class, true, false);

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
    public void withTokenAndValidDogTypeShouldShowList() throws IOException {

        clearSharedPreferences();
        setPreferencesToken();

        server.enqueue(new MockResponse()
                .setBody(readFile("fetch_husky_response.json")));

        activityRule.launchActivity(new Intent());

        onView(withId(R.id.dogRecyclerView))
                .check(new RecyclerViewItemCountAssertion(4));

        onView(withId(R.id.emptyListTextView))
                .check(matches(not(isDisplayed())));

    }

    @Test
    public void withTokenAndInvalidDogTypeShouldShowEmptyState() throws IOException, InterruptedException {

        clearSharedPreferences();
        setPreferencesToken();

        server.enqueue(new MockResponse()
                .setBody(readFile("fetch_husky_response.json")));

        server.enqueue(new MockResponse()
                .setBody(readFile("fetch_pound_response.json")));

        activityRule.launchActivity(new Intent());

        Thread.sleep(2000);

        onView(withId(R.id.navigationPound))
                .perform(click());

        onView(withId(R.id.dogRecyclerView))
                .check(new RecyclerViewItemCountAssertion(0));

        onView(withId(R.id.emptyListTextView))
                .check(matches(isDisplayed()));

    }

    @Test
    public void withNoTokenShouldShowMessage() throws InterruptedException {

        clearSharedPreferences();

        activityRule.launchActivity(new Intent());

        Thread.sleep(1000);

        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText("Error getting dog list!")))
                .check(matches(isDisplayed()));

    }

    private String readFile(String fileName) throws IOException {
        InputStream stream = InstrumentationRegistry.getContext()
                .getAssets()
                .open(fileName);

        Source source = Okio.source(stream);
        BufferedSource buffer = Okio.buffer(source);

        return buffer.readUtf8();
    }

    private SharedPreferences getSharedPreferences() {
        Context context = InstrumentationRegistry.getTargetContext();
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private void clearSharedPreferences() {
        getSharedPreferences()
                .edit()
                .clear()
                .apply();
    }

    private void setPreferencesToken(){
        getSharedPreferences()
                .edit()
                .putString("token", "fake-token")
                .apply();
    }

}