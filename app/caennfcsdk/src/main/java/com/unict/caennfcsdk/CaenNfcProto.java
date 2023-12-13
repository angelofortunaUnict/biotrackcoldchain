package com.unict.caennfcsdk;

import android.nfc.Tag;
import android.util.Log;

import java.io.IOException;
import java.util.Arrays;

public class CaenNfcProto {
    private static final byte ADDRESS_REGISTER_ID = 5;
    private static final byte CMD_ACK = -84;
    private static final byte CMD_ID__READ = 18;
    private static final byte CMD_ID__WRITE = 19;
    private static final byte CMD_NACK = -4;
    private static final byte COMMAND_REGISTER_ID = 4;
    private static final byte DATASTART_REGISTER_ID = 8;
    private static final int EPC_CODE_PAGE_0 = 135;
    private static final byte REPLY_REGISTER_ID = 7;
    private static final byte SIZE_REGISTER_ID = 6;
    private static final byte TRIGGER_REGISTER_ID = -123;
    private static final int _MAX_ALLOWED_REG_LENGTH = 200;
    private static final int _READ_RESULTS_DELAY_MS = 200;
    private byte _msg_counter = 13;
    private CaenNfcLowLevel _nfc_lowlevel;
    private Tag _tag;

    public CaenNfcProto(Tag tag) {
        this._tag = null;
        this._nfc_lowlevel = null;
        this._tag = tag;
        this._nfc_lowlevel = new CaenNfcLowLevel(this._tag);
    }

    public boolean isCaenRFIDTag() {
        try {
            byte[] read = this._nfc_lowlevel.read(EPC_CODE_PAGE_0, 3);
            if (read[0] == 48 && (read[1] & 12) == 12 && (read[1] & 3) == 2 && (read[2] & 255) == 254 && (read[3] & 255) == 47 && (read[4] & 255) == 148 && (read[5] & 240) == 208 && (read[5] & 15) == 0 && (read[6] & 255) == 28) {
                if ((read[7] & 240) == 0) {
                    return true;
                }
            }
        } catch (Exception unused) {
        }
        return false;
    }

    public boolean clean() {
        try {
            this._nfc_lowlevel.write(7, new byte[]{0, 0, 0, 0});
            this._nfc_lowlevel.write(8, new byte[]{0, 0, 0, 0});
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public byte[] readRegister(short s) throws IOException {
        return readRegisters(s, 1, false);
    }

    public byte[] readRegisters(short s, int i, boolean z) throws IOException {
        int i2 = 200;
        if (i <= 200) {
            i2 = i;
        }
        byte b = (byte) i2;
        boolean z2 = true;
        byte[] int32ToBytes = C0421util.int32ToBytes((z ? (byte) 2 : (byte) 1) * b);
        byte[] int16ToBytes = C0421util.int16ToBytes(s);
        byte b2 = (byte) (((this._msg_counter + 1) % 251) + 1);
        this._msg_counter = b2;
        if (!this._nfc_lowlevel.write(4, new byte[]{b2, CMD_ID__READ, 0, 0}) || !this._nfc_lowlevel.write(5, new byte[]{int16ToBytes[0], int16ToBytes[1], 0, 0}) || !this._nfc_lowlevel.write(6, new byte[]{int32ToBytes[2], int32ToBytes[3], 0, 0}) || !this._nfc_lowlevel.write(-123, new byte[]{this._msg_counter, 0, 0, 0})) {
            z2 = false;
        } else {
            Log.d("CNDEBUG", "[CaenNfcProto.readRegister(send)] reg:" + ((int) s) + " mc: " + ((int) this._msg_counter) + " OK");
        }
        if (!z2) {
            Log.d("CNDEBUG", "[CaenNfcProto.readRegister] reg:" + ((int) s) + " FAILED");
            throw new IOException("Tag cannot trigger read register command");
        } else if (!waitForReply()) {
            Log.d("CNDEBUG", "[CaenNfcProto.readRegister(return)] reg:" + ((int) s) + " mc: " + ((int) this._msg_counter) + " FAILED");
            throw new IOException("Tag cannot get reply for read register command.");
        } else {
            Log.d("CNDEBUG", "[CaenNfcProto.readRegister(return)] reg:" + ((int) s) + " mc: " + ((int) this._msg_counter) + " OK");
            byte[] read = this._nfc_lowlevel.read(8, b);
            Log.d("CNDEBUG", "[CaenNfcProto.readRegister] reg:" + ((int) s) + "  reg data:" + C0421util.byteArrayToHex(read));
            return read;
        }
    }

    public boolean writeRegister(short s, byte[] bArr) throws IOException {
        return writeRegisters(s, 1, bArr);
    }

    public boolean writeRegisters(short s, int i, byte[] bArr) throws IOException {
        if (i > 200) {
            i = 200;
        }
        byte b = (byte) i;
        if (!this._nfc_lowlevel.write(8, bArr, b)) {
            throw new IOException("[writeRegisters] Cannot write DATASTART_REGISTER");
        }
        this._nfc_lowlevel.read(8, b);
        byte[] int16ToBytes = C0421util.int16ToBytes(s);
        byte b2 = (byte) (((this._msg_counter + 1) % 251) + 1);
        this._msg_counter = b2;
        boolean z = false;
        if (this._nfc_lowlevel.write(4, new byte[]{b2, CMD_ID__WRITE, 0, 0}) && this._nfc_lowlevel.write(5, new byte[]{int16ToBytes[0], int16ToBytes[1], 0, 0}) && this._nfc_lowlevel.write(6, new byte[]{0, b, 0, 0}) && this._nfc_lowlevel.write(-123, new byte[]{this._msg_counter, 0, 0, 0})) {
            z = true;
        }
        if (!z) {
            Log.d("CNDEBUG", "[CaenNfcProto.writeRegisters] reg:" + ((int) s) + " FAILED");
            throw new IOException("[writeRegisters] Cannot trigger the write register command");
        } else if (waitForReply()) {
            return true;
        } else {
            Log.d("CNDEBUG", "[CaenNfcProto.writeRegisters] reg:" + ((int) s) + " FAILED");
            throw new IOException("[writeRegisters] Cannot read reply for write register command");
        }
    }

    private boolean waitForReply() throws IOException {
        for (int i = 0; i < 3; i++) {
            try {
                Thread.sleep(200L);
            } catch (InterruptedException unused) {
            }
            byte[] read = this._nfc_lowlevel.read(7);
            if (read != null && read[0] == this._msg_counter && read[1] == -84) {
                return true;
            }
            Log.d("CNDEBUG", "[CaenNfcProto.waitForReply] reply:" + i + " mc " + ((int) this._msg_counter) + " reply array-> " + Arrays.toString(read) + " FAILED");
        }
        return false;
    }

}

