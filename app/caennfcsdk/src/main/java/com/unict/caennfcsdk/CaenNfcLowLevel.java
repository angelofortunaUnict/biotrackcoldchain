package com.unict.caennfcsdk;

import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.util.Log;

import java.io.IOException;

public class CaenNfcLowLevel {
    private static final byte NFCA_CMD_RES_OK = 10;
    private static final byte NFCA_FAST_READ_CMD_ID = 58;
    private static final byte NFCA_READ_CMD_ID = 48;
    private static final byte NFCA_WRITE_CMD_ID = -94;
    private NfcA _nfc_tag = null;
    private Tag _tag = null;

    public CaenNfcLowLevel(Tag tag) {
        this._tag = tag;
        this._nfc_tag = NfcA.get(tag);
    }

    public byte[] read(int i, int i2) throws IOException {
        if (i2 < 1) {
            return null;
        }
        this._nfc_tag.connect();
        byte[] transceive = this._nfc_tag.transceive(new byte[]{NFCA_FAST_READ_CMD_ID, (byte) (i & 255), (byte) (((i + i2) - 1) & 255)});
        this._nfc_tag.close();
        return transceive;
    }

    public byte[] read(int i) throws IOException {
        this._nfc_tag.connect();
        byte[] transceive = this._nfc_tag.transceive(new byte[]{NFCA_READ_CMD_ID, (byte) (i & 255)});
        this._nfc_tag.close();
        if (i == 8) {
            Log.d("CNDEBUG", "[CaenNfcLowLevel.read]   page:" + i + "   reg data:" + C0421util.byteArrayToHex(transceive));
        }
        return getSinglePage(0, transceive);
    }

    public boolean write(int i, byte[] bArr) throws IOException {
        try {
            if (bArr == null || bArr.length < 4) {
                return false;
            }
            this._nfc_tag.connect();
            byte[] transceive = this._nfc_tag.transceive(new byte[]{NFCA_WRITE_CMD_ID, (byte) (i & 255), bArr[0], bArr[1], bArr[2], bArr[3]});
            this._nfc_tag.close();
            return transceive != null && transceive[0] == 10;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean write(int i, byte[] bArr, int i2) throws IOException {
        int i3 = i2 * 2;
        if (bArr == null || bArr.length < i3) {
            return false;
        }
        int i4 = 0;
        while (i4 < i3) {
            if (write(i, i3 - i4 >= 4 ? new byte[]{bArr[i4], bArr[i4 + 1], bArr[i4 + 2], bArr[i4 + 3]} : new byte[]{bArr[i4], bArr[i4 + 1], -1, -1})) {
                i4 += 4;
                i++;
            } else {
                throw new IOException("ERROR WRITING PAGE:" + i);
            }
        }
        return true;
    }

    public Tag getTag() {
        return this._tag;
    }

    private byte[] getSinglePage(int i, byte[] bArr) {
        if (bArr == null || bArr.length < (i + 1) * 4) {
            return null;
        }
        int i2 = i * 4;
        return new byte[]{bArr[i2 + 0], bArr[i2 + 1], bArr[i2 + 2], bArr[i2 + 3]};
    }
}
