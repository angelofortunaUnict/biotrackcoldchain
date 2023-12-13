package com.unict.caennfcsdk;

import android.util.Log;

public class CaenNfcStatus {
    private static final int _UNDEFINED_BATTERY_LEVEL = -1;
    private boolean _RFU_mkt_alarm;
    private boolean _RFU_shl_alarm;
    private int _battery_level = -1;
    private boolean _bin_alarm;
    private boolean _brs_alarm;
    private boolean _eta_alarm;
    private boolean _mem_over_endurance;
    private boolean _memory_full;
    private String _raw_hex_data;

    public static final byte X_DUAL = 16;
    public static final byte Y_DUAL = 32;

    public CaenNfcStatus(byte[] bArr) {
        boolean z = false;
        this._memory_full = false;
        this._eta_alarm = false;
        this._bin_alarm = false;
        this._RFU_mkt_alarm = false;
        this._RFU_shl_alarm = false;
        this._mem_over_endurance = false;
        this._brs_alarm = false;
        this._raw_hex_data = "";
        if (bArr != null && bArr.length > 1) {
            this._battery_level = bArr[1] & 3;
            this._memory_full = (bArr[1] & 4) != 0;
            this._eta_alarm = (bArr[1] & 8) != 0;
            this._bin_alarm = (bArr[1] & X_DUAL) != 0;
            this._RFU_mkt_alarm = (bArr[1] & Y_DUAL) != 0;
            this._RFU_shl_alarm = (bArr[1] & 64) != 0;
            this._mem_over_endurance = (bArr[1] & 128) != 0;
            this._brs_alarm = (bArr[0] & 1) != 0 ? true : z;
            this._raw_hex_data = C0421util.byteArrayToHex(bArr);
            Log.d("CNDEBUG", "[CaenNfcStatus] status bytes:" + this._raw_hex_data);
        }
    }

    public String description() {
        StringBuilder sb = new StringBuilder();
        sb.append("Battery[");
        sb.append(this._battery_level);
        sb.append("] Alarms: MEM[");
        String str = "X";
        sb.append(this._memory_full ? str : "_");
        sb.append("] ETA[");
        sb.append(this._eta_alarm ? str : "_");
        sb.append("] BIN[");
        sb.append(this._bin_alarm ? str : "_");
        sb.append("] MOE[");
        sb.append(this._mem_over_endurance ? str : "_");
        sb.append("] BRT[");
        if (!this._brs_alarm) {
            str = "_";
        }
        sb.append(str);
        sb.append("] RAW:");
        sb.append(this._raw_hex_data);
        return sb.toString();
    }

    public boolean isValid() {
        return this._battery_level != -1;
    }

    public int batteryLevel() {
        return this._battery_level;
    }

    public boolean isMemoryFull() {
        return this._memory_full;
    }

    public boolean etaAlarm() {
        return this._eta_alarm;
    }

    public boolean binAlarm() {
        return this._bin_alarm;
    }

    public boolean moeAlarm() {
        return this._mem_over_endurance;
    }

    public boolean batteryResetAlarm() {
        return this._brs_alarm;
    }

    public String description_pdf() {
        int i = this._battery_level;
        String str = i != 1 ? i != 2 ? i != 3 ? "empty" : "Full" : "Normal" : "Low";
        StringBuilder sb = new StringBuilder();
        sb.append("Battery[");
        sb.append(str);
        sb.append("] Alarms: MEM[");
        String str2 = "FULL";
        sb.append(this._memory_full ? str2 : "_");
        sb.append("] ETA[");
        sb.append(this._eta_alarm ? str2 : "_");
        sb.append("] BIN[");
        if (!this._bin_alarm) {
            str2 = "_";
        }
        sb.append(str2);
        sb.append("]");
        return sb.toString();
    }
}
