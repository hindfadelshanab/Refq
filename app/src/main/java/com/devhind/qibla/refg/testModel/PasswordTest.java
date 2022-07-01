package com.devhind.qibla.refg.testModel;

public class PasswordTest {

    public boolean validatePassword(String value , int minLength){

        if (value.length()<minLength){
            return false;
        }else {
            return true;
        }
    }
}
