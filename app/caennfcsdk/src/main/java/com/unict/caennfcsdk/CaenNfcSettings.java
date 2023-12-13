package com.unict.caennfcsdk;

import java.util.Date;

public class CaenNfcSettings {
    public static final int BIN_NUM = 16;
    public static final int SETTINGS_REGISTER_LEN = 57;
    public static final int SETTINGS_REGISTER_LEN_BYTES = 114;
    private short _bin_enable = 1;
    private short[] _bin_sampling_time = {30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30};
    private short[] _bin_threshold = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
    private short[] _bin_upper_limit = {2720, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private CaenNfcControlRegister _control;
    private short _enable_samples_store = -1;
    private short _enable_time_store = 0;
    private long _estimated_time_arrival = 0;
    private long _init_date = 0;
    private short _sampling_delay = 0;
    private long _shipping_date = 0;
    private long _stop_date = 0;

    public CaenNfcSettings(byte[] bArr) {
        if (bArr != null && bArr.length >= 114) {
            this._control = new CaenNfcControlRegister(C0421util.bytesToInt16(bArr, 0));
            this._sampling_delay = C0421util.bytesToInt16(bArr, 2);
            this._init_date = (long) C0421util.bytesToInt32(bArr, 4, true);
            this._estimated_time_arrival = (long) C0421util.bytesToInt32(bArr, 8, true);
            this._bin_enable = C0421util.bytesToInt16(bArr, 12);
            this._enable_samples_store = C0421util.bytesToInt16(bArr, 14);
            this._enable_time_store = C0421util.bytesToInt16(bArr, 16);
            int i = 18;
            for (int i2 = 0; i2 < 16; i2++) {
                this._bin_upper_limit[i2] = C0421util.bytesToInt16(bArr, i);
                i += 2;
            }
            for (int i3 = 0; i3 < 16; i3++) {
                this._bin_sampling_time[i3] = C0421util.bytesToInt16(bArr, i);
                i += 2;
            }
            for (int i4 = 0; i4 < 16; i4++) {
                this._bin_threshold[i4] = C0421util.bytesToInt16(bArr, i);
                i += 2;
            }
        }
    }

    public byte[] raw() {
        RegisterAccumulator registerAccumulator = new RegisterAccumulator();
        registerAccumulator.addShort(this._control.raw());
        registerAccumulator.addShort(this._sampling_delay);
        registerAccumulator.addInt((int) this._init_date, true);
        registerAccumulator.addInt((int) this._estimated_time_arrival, true);
        registerAccumulator.addShort(this._bin_enable);
        registerAccumulator.addShort(this._enable_samples_store);
        registerAccumulator.addShort(this._enable_time_store);
        for (int i = 0; i < 16; i++) {
            registerAccumulator.addShort(this._bin_upper_limit[i]);
        }
        for (int i2 = 0; i2 < 16; i2++) {
            registerAccumulator.addShort(this._bin_sampling_time[i2]);
        }
        for (int i3 = 0; i3 < 16; i3++) {
            registerAccumulator.addShort(this._bin_threshold[i3]);
        }
        return registerAccumulator.bytes();
    }

    public String description() {
        return " CONTROL[" + this._control.description() + "] SMP_DELAY[" + Integer.toHexString(this._sampling_delay) + "] INIT_DATE[" + Long.toHexString(this._init_date) + "] ETA[" + Long.toHexString(this._estimated_time_arrival) + "] BIN_ENA[" + Integer.toHexString(this._bin_enable) + "] ENA_SMP[" + Integer.toHexString(this._enable_samples_store) + "] ENA_TIME[" + Integer.toHexString(this._enable_time_store) + "]";
    }

    public CaenNfcControlRegister controlRegister() {
        return this._control;
    }

    public void setInitDate(Date date) {
        if (date == null) {
            this._init_date = 0;
        } else {
            this._init_date = date.getTime() / 1000;
        }
    }

    public long initDate() {
        return this._init_date * 1000;
    }

    public void setETADate(long j) {
        this._estimated_time_arrival = j;
    }

    public long ETADate() {
        return this._estimated_time_arrival;
    }

    public void enableBin(int i, boolean z) {
        if (z) {
            this._bin_enable = (short) ((1 << i) | this._bin_enable);
            return;
        }
        this._bin_enable = (short) (((1 << i) ^ -1) & this._bin_enable);
    }

    public boolean isBinEnabled(int i) {
        return ((1 << i) & this._bin_enable) != 0;
    }

    public short getBinEnabled() {
        return this._bin_enable;
    }

    public boolean storeSamples(int i) {
        return ((1 << i) & this._enable_samples_store) != 0;
    }

    public void enableStoreSamples(int i, boolean z) {
        if (z) {
            this._enable_samples_store = (short) ((1 << i) | this._enable_samples_store);
            return;
        }
        this._enable_samples_store = (short) (((1 << i) ^ -1) & this._enable_samples_store);
    }

    public boolean storeTime(int i) {
        return ((1 << i) & this._enable_time_store) != 0;
    }

    public void enableStoreTime(int i, boolean z) {
        if (z) {
            this._enable_time_store = (short) ((1 << i) | this._enable_time_store);
            return;
        }
        this._enable_time_store = (short) (((1 << i) ^ -1) & this._enable_time_store);
    }

    public short upperLimit(int i) {
        return this._bin_upper_limit[i];
    }

    public void setUpperLimit(int i, short s) {
        this._bin_upper_limit[i] = s;
    }

    public short samplingTime(int i) {
        return this._bin_sampling_time[i];
    }

    public void setSamplingTime(int i, short s) {
        this._bin_sampling_time[i] = s;
    }

    public short threshold(int i) {
        return this._bin_threshold[i];
    }

    public void setThreshold(int i, short s) {
        this._bin_threshold[i] = s;
    }

    public short samplingDelay() {
        return this._sampling_delay;
    }

    public void setSamplingDelay(short s) {
        this._sampling_delay = s;
    }

    public boolean[] getBinsStatus() {
        boolean[] zArr = new boolean[16];
        for (int i = 0; i < 16; i++) {
            zArr[i] = isBinEnabled(i);
        }
        return zArr;
    }

    public boolean sampleBinHasTimestamp(float f) {
        int findBinFor = findBinFor(f);
        return findBinFor >= 0 && isBinStoringTime(findBinFor);
    }

    public int findBinFor(float f) {
        float f2;
        for (int i = 0; i < 16; i++) {
            if (isBinStoringSample(i)) {
                if (i == 0) {
                    f2 = -30.0f;
                } else {
                    f2 = C0421util.getFloatTemperature(this._bin_upper_limit[i - 1]);
                }
                float floatTemperature = C0421util.getFloatTemperature(this._bin_upper_limit[i]);
                if (f > f2 && f <= floatTemperature) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void setShippingDate(Date date) {
        if (date == null) {
            this._shipping_date = 0;
        } else {
            this._shipping_date = date.getTime() / 1000;
        }
    }

    public long shippingDate() {
        return this._shipping_date * 1000;
    }

    public void setStopDate(Date date) {
        if (date == null) {
            this._stop_date = 0;
        } else {
            this._stop_date = date.getTime() / 1000;
        }
    }

    public long stopDate() {
        return this._stop_date * 1000;
    }

    private boolean isBinStoringSample(int i) {
        return ((1 << i) & this._enable_samples_store) != 0;
    }

    private boolean isBinStoringTime(int i) {
        return ((1 << i) & this._enable_time_store) != 0;
    }
}
