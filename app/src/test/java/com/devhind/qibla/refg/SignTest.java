package com.devhind.qibla.refg;

import static com.devhind.qibla.refg.testModel.CleanText.EMPTY_TEXT_ERROR;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import androidx.annotation.NonNull;

import com.devhind.qibla.refg.testModel.CleanText;
import com.devhind.qibla.refg.testModel.PasswordTest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SignTest {


    @Test
    public void removeSpacesInString() {
        CleanText cleanText  = new CleanText();
        String normalExpected = "Android Developer";
        String noSpaceExpected = "AndroidDeveloper";

        assertEquals(noSpaceExpected, cleanText.removeSpacesInString(" AndroidDeveloper"));
        assertEquals(noSpaceExpected, cleanText.removeSpacesInString("AndroidDeveloper "));
        assertEquals(normalExpected, cleanText.removeSpacesInString("Android Developer"));

        assertEquals(noSpaceExpected, cleanText.removeSpacesInString("  AndroidDeveloper"));
        assertEquals(noSpaceExpected, cleanText.removeSpacesInString("AndroidDeveloper  "));
        assertEquals(normalExpected, cleanText.removeSpacesInString("Android  Developer"));

        assertEquals(EMPTY_TEXT_ERROR, cleanText.removeSpacesInString(""));
        assertEquals(noSpaceExpected,cleanText.removeSpacesInString("AndroidDeveloper"));

    }

    @Test
    public  void validatePassword(){
        String password  ="123456";

        PasswordTest passwordTest = new PasswordTest();
        boolean isValid = passwordTest.validatePassword(password , 6);
        assert  isValid;

    }



    @Test
    public void emailValidator_CorrectEmail_ReturnsTrue() {
        CleanText cleanText  = new CleanText();
       assertTrue(cleanText.validateEmail("testname@email.com"));
    }
    @Test
    public void emailValidator_CorrectEmail_ReturnsFalse() {
        CleanText cleanText  = new CleanText();
        assertFalse(cleanText.validateEmail("@email.com"));

    }
}
