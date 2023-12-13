package com.unict.caennfcsdk;

class OutputRegisters {
    boolean[] binAlarms = new boolean[16];
    short[] binCounters = new short[16];
    float lastSampleValue;
    short samplesNumber;

    OutputRegisters() {
    }

    /* access modifiers changed from: package-private */
    public void setBinAlarms(short s) {
        for (int i = 0; i < 16; i++) {
            this.binAlarms[i] = ((((short) ((int) Math.pow(2.0d, (double) i))) & s) & 65535) != 0;
        }
    }

    /* access modifiers changed from: package-private */
    public void setBinCounters(short[] sArr) {
        if (sArr.length == 16) {
            for (int i = 0; i < 16; i++) {
                this.binCounters[i] = sArr[i];
            }
            return;
        }
        throw new IllegalArgumentException("Counters registers must be 16");
    }

    /* access modifiers changed from: package-private */
    public void setLastSampleValue(short s) {
        this.lastSampleValue = C0421util.getFloatTemperature(s);
    }

    /* access modifiers changed from: package-private */
    public void setSamplesNumber(short s) {
        this.samplesNumber = s;
    }

    /* access modifiers changed from: package-private */
    public boolean[] getBinAlarms() {
        return this.binAlarms;
    }

    /* access modifiers changed from: package-private */
    public short[] getBinCounters() {
        return this.binCounters;
    }

    /* access modifiers changed from: package-private */
    public float getLastSampleValue() {
        return this.lastSampleValue;
    }

    /* access modifiers changed from: package-private */
    public short getSamplesNumber() {
        return this.samplesNumber;
    }
}