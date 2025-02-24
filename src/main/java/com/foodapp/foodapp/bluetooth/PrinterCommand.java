package com.foodapp.foodapp.bluetooth;

import java.io.UnsupportedEncodingException;

public class PrinterCommand {
    public static byte[] POS_Print_Text(String pszString, String encoding, int codepage, int nWidthTimes, int nHeightTimes,
                                        int nFontType) {
        if (codepage < 0 || codepage > 255 || pszString == null || "".equals(pszString) || pszString.length() < 1) {
            return null;
        }
        try {
            byte[] pbString = pszString.getBytes(encoding);
            byte[] intToWidth = {0, 16, 32, 48};
            byte[] intToHeight = {0, 1, 2, 3};
            Command.GS_ExclamationMark[2] = (byte) (intToWidth[nWidthTimes] + intToHeight[nHeightTimes]);
            Command.ESC_t[2] = (byte) codepage;
            Command.ESC_M[2] = (byte) nFontType;
            if (codepage == 0) {
                return Other.byteArraysToBytes(
                        new byte[][]{Command.GS_ExclamationMark, Command.ESC_t, Command.FS_and, Command.ESC_M, pbString});
            }
            return Other.byteArraysToBytes(
                    new byte[][]{Command.GS_ExclamationMark, Command.ESC_t, Command.FS_dot, Command.ESC_M, pbString});
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}
