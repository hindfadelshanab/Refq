package com.devhind.qibla.refg;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import android.content.Context;

import com.devhind.qibla.refg.View.SignScreen.SignInActivity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LoginUnitTest {

    private static final String FAKE_STRING = "Login was successful";

    @Mock
    Context mMockContext;

    @Test
    public void readStringFromContext_LocalizedString() {

        SignInActivity myObjectUnderTest = new SignInActivity(mMockContext);

        // ...when the string is returned from the object under test...
        String result = myObjectUnderTest.validate("old@gmail.com","123456");

        // ...then the result should be the expected one.
        assertThat(result, is(FAKE_STRING));
    }
}
