package com.ccis.seafoodtrans.utils;

public class InputValidation {
    public static boolean isValidName(String word) {
        if (word.matches("^([a-zA-Z',.-]+( [a-zA-Z',.-]+)*){2,30}$")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isValidEmail(String word) {
        if (word.matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Password must contain at least one letter, at least one number, and be longer than six characters.
     */
    public static boolean isValidPassword(String word) {
        if (word.matches("^(?=.*[0-9]+.*)(?=.*[a-zA-Z]+.*)[0-9a-zA-Z]{2,}$")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isValidPhoneNumber(String word) {
        if (word.matches("^[0-9]+$")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isArabic(String word) {
        if (word.matches("^[\\u0621-\\u064A]+$")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isContainsSpecialCharacter(String word) {
            if (word.matches("^[\\w&.\\-]+$")) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isValidAddress(String word) {
            if (!word.isEmpty() && !word.startsWith(" ") && word.length()>8) {
            return true;
        } else {
            return false;
        }
    }


}
