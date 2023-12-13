package com.unict.caennfcsdk;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class C0421util {
    public static short getFixedPointTemperature(float f) {
        return (short) ((int) (f >= 0.0f ? f * 32.0f : (f * 32.0f) + 8192.0f));
    }

    public static float getFloatTemperature(short s) {
        float f = (float) (s >> 5);
        float f2 = (float) (s & 31);
        if (f > 127.0f) {
            f = -(255.0f - f);
        }
        return f + (f2 / 32.0f);
    }

    public static String byteArrayToHex(byte[] bArr) {
        if (bArr == null) {
            return "n/a";
        }
        StringBuilder sb = new StringBuilder(bArr.length * 2);
        int length = bArr.length;
        for (int i = 0; i < length; i++) {
            sb.append(String.format("%02x", new Object[]{Byte.valueOf(bArr[i])}));
        }
        return sb.toString();
    }

    public static byte[] int16ToBytes(short s) {
        ByteBuffer allocate = ByteBuffer.allocate(2);
        allocate.putShort(s);
        return allocate.array();
    }

    public static byte[] int32ToBytes(int i) {
        ByteBuffer allocate = ByteBuffer.allocate(4);
        allocate.putInt(i);
        return allocate.array();
    }

    public static byte[] int32ToBytes(int i, boolean z) {
        byte[] int32ToBytes = int32ToBytes(i);
        if (!z) {
            return int32ToBytes;
        }
        return new byte[]{int32ToBytes[2], int32ToBytes[3], int32ToBytes[0], int32ToBytes[1]};
    }

    public static short bytesToInt16(byte[] bArr) {
        return bytesToInt16(bArr, 0);
    }

    public static short bytesToInt16(byte[] bArr, int i) {
        if (bArr == null || bArr.length < i + 2) {
            return 0;
        }
        ByteBuffer allocate = ByteBuffer.allocate(2);
        allocate.order(ByteOrder.LITTLE_ENDIAN);
        allocate.put(bArr[i + 1]);
        allocate.put(bArr[i]);
        return allocate.getShort(0);
    }

    public static int bytesToInt32(byte[] bArr, int i, boolean z) {
        if (bArr == null || bArr.length < i + 4) {
            return 0;
        }
        ByteBuffer allocate = ByteBuffer.allocate(4);
        allocate.order(ByteOrder.LITTLE_ENDIAN);
        if (z) {
            allocate.put(bArr[i + 1]);
            allocate.put(bArr[i]);
            allocate.put(bArr[i + 3]);
            allocate.put(bArr[i + 2]);
        } else {
            allocate.put(bArr[i + 3]);
            allocate.put(bArr[i + 2]);
            allocate.put(bArr[i + 1]);
            allocate.put(bArr[i]);
        }
        return allocate.getInt(0);
    }

    public static int bytesToInt32(byte[] bArr, int i) {
        return bytesToInt32(bArr, i, false);
    }

    public static float getFloatTemperature(byte[] bArr) {
        return getFloatTemperature(bytesToInt16(bArr));
    }
}
