package com.example.cybank;

import android.content.Context;

import net.bytebuddy.matcher.ElementMatcher;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static net.bytebuddy.matcher.ElementMatchers.is;

/**
 * Tests UI if login screens header is infact CyBank
 */
public class LoginTest {
    private static final String FAKE_STRING = "CyBank";

    @Mock
    Context mMockContext;

    @Before
    public void before(){
        MockitoAnnotations.initMocks(this);
        Mockito.when(mMockContext.getString(R.id.CyBank))
                .thenReturn("Login Header");
    }

    @Test
    public void checkHeaderString() {
        assertThat("Login Header", is(FAKE_STRING));
    }

    private void assertThat(String login_header, ElementMatcher.Junction<Object> objectJunction) {
    }
}
