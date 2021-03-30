package com.ccis.seafoodtrans.utils;

public class Code128BarcodeUtil {
    private static final String SSCC_CODE = "00";
    public static String getContainerCode(String barcode){
                switch (barcode.substring(0, 2)) {
                    case SSCC_CODE:
                       return barcode.substring(2);
                }
        return "";
    }
}
