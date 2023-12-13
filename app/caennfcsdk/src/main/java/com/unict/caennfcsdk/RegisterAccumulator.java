package com.unict.caennfcsdk;

import java.util.ArrayList;

public class RegisterAccumulator {
    private ArrayList<Byte> _reg_buffer;

    public RegisterAccumulator() {
        this._reg_buffer = null;
        this._reg_buffer = new ArrayList<>();
    }

    public byte[] bytes() {
        byte[] bArr = new byte[this._reg_buffer.size()];
        for (int i = 0; i < this._reg_buffer.size(); i++) {
            bArr[i] = this._reg_buffer.get(i).byteValue();
        }
        return bArr;
    }

    public void addShort(short s) {
        accumulate(C0421util.int16ToBytes(s));
    }

    public void addInt(int i, boolean z) {
        accumulate(C0421util.int32ToBytes(i, z));
    }

    private void accumulate(byte[] bArr) {
        for (byte valueOf : bArr) {
            this._reg_buffer.add(Byte.valueOf(valueOf));
        }
    }
}
