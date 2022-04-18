package com.codegym.validate;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateHelper {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static final Pattern VALID_PHONE_REGEX =
            Pattern.compile("^0[0-9]{9}");

    public static boolean validEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public static boolean validPhone(String phone) {
        Matcher matcher = VALID_PHONE_REGEX.matcher(phone);
        return matcher.find();
    }

    public static boolean notNegative(int number) {
        return number >= 0;
    }

    public static boolean greaterThanZero(int number) {
        return number > 0;
    }

    public static boolean notBlank(String string){
        return !string.trim().isEmpty();
    }

}
