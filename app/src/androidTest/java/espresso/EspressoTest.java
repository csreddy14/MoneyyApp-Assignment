package espresso;

import static org.hamcrest.core.IsNot.not;
import static  androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.view.View;
import android.widget.EditText;

import androidx.annotation.StringRes;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.example.justin.Login;
import com.example.justin.MainActivity;
import com.example.justin.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class EspressoTest {
    @Rule
    public ActivityTestRule<Login> activityTestRule = new ActivityTestRule<>(Login.class);

    @Test
    public void emailIsEmpty() {
        onView(withId(R.id.email)).perform(clearText());
        onView(withId(R.id.loginBtn)).perform(click());
        onView(withId(R.id.email)).check(matches(withError(getString(R.string.error_field_required))));
    }

    @Test
    public void passwordIsEmpty() {
        onView(withId(R.id.email)).perform(typeText("csreddy@gmail.com"));
        onView(withId(R.id.password)).perform(clearText());
        onView(withId(R.id.loginBtn)).perform(click());
        onView(withId(R.id.password)).check(matches(withError(getString(R.string.error_field_required))));
    }

    @Test
    public void emailIsInvalid() {
        onView(withId(R.id.email)).perform(typeText("invalid"));
        onView(withId(R.id.loginBtn)).perform(click());
        onView(withId(R.id.email)).check(matches(withError(getString(R.string.error_invalid_email))));
    }

    @Test
    public void passwordIsTooShort() {
        onView(withId(R.id.email)).perform(typeText("email@email.com"));
        onView(withId(R.id.password)).perform(typeText("1234"));
        onView(withId(R.id.loginBtn)).perform(click());
        onView(withId(R.id.password)).check(matches(withError(getString(R.string.error_invalid_password))));
    }

    @Test
    public void loginFailed() {
        onView(withId(R.id.email)).perform(typeText("incorrect@email.com"));
        onView(withId(R.id.password)).perform(typeText("123456"));
        onView(withId(R.id.loginBtn)).perform(click());
        onView(withText(getString(R.string.error_login_failed)))
                .inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    public void loginSuccessfully_shouldShowWelcomeMessage() {
        onView(withId(R.id.email)).perform(typeText("csreddy@gmail.com"));
        onView(withId(R.id.password)).perform(typeText("123456"));
        onView(withId(R.id.loginBtn)).perform(click());
        onView(withId(R.id.tv_welcome)).check(matches(withText("Hi user@email.com!")));
    }

    @Test
    public void loginSuccessfully_shouldShowToast() {
        onView(withId(R.id.email)).perform(typeText("csreddy@gmail.com"));
        onView(withId(R.id.password)).perform(typeText("123456"));
        onView(withId(R.id.loginBtn)).perform(click());
        onView(withText(getString(R.string.login_successfully)))
                .inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    private String getString(@StringRes int resourceId) {
        return activityTestRule.getActivity().getString(resourceId);
    }

    private static Matcher<View> withError(final String expected) {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                if (item instanceof EditText) {
                    return ((EditText)item).getError().toString().equals(expected);
                }
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Not found error message" + expected + ", find it!");
            }
        };
    }
}
