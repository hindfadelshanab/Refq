package com.devhind.qibla.refg.testModel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public   class CleanText {



 public static String EMPTY_TEXT_ERROR = "String can't be empty";



        public String removeSpacesInString(String textToClean) {
            if (textToClean.equals("")) return EMPTY_TEXT_ERROR;
            int count = 0;
            String result = "";
            String textTrim = textToClean.trim();

            for (int i = 0; i < textTrim.length(); i++) {

                if (textTrim.charAt(i) == ' ') {
                    count++;
                    if (count == 1) {
                        result += textTrim.charAt(i);
                    }
                } else {
                    count = 0;
                    result += textTrim.charAt(i);
                }
            }

            return result;
        }

     public static   String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

     Pattern pattern  = Pattern.compile(EMAIL_PATTERN);
     private Matcher matcher  = null;

     public boolean validateEmail(String hex ) {
         matcher = pattern.matcher(hex);
         return matcher.matches();
     }
}
