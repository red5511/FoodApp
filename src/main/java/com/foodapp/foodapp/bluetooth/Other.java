package com.foodapp.foodapp.bluetooth;


/* loaded from: classes.dex */
public class Other {
    private static final int WIDTH_58 = 384;
    private static final int WIDTH_80 = 576;
    public byte[] buf;
    public int index = 0;
    private static int[] p0 = {0, 128};
    private static int[] p1 = {0, 64};
    private static int[] p2 = {0, 32};
    private static int[] p3 = {0, 16};
    private static int[] p4 = {0, 8};
    private static int[] p5 = {0, 4};
    private static int[] p6 = {0, 2};
    private static final byte[] chartobyte = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 0, 0, 0, 0, 0, 0, 10, 11, 12, 13, 14, 15};

    public Other(int length) {
        this.buf = new byte[length];
    }

    public static byte[] byteArraysToBytes(byte[][] data) {
        int length = 0;
        for (byte[] bArr : data) {
            length += bArr.length;
        }
        byte[] send = new byte[length];
        int k = 0;
        for (int i = 0; i < data.length; i++) {
            int j = 0;
            while (j < data[i].length) {
                send[k] = data[i][j];
                j++;
                k++;
            }
        }
        return send;
    }
}
