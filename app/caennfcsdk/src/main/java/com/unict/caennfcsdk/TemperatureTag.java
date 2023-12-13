package com.unict.caennfcsdk;

import android.nfc.Tag;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class TemperatureTag {
    static final int INVALID_TEMPERATURE_THRESHOLD = 99;
    private static final int MAX_LOG_AREA_SIZE = 4096;
    static final int MAX_TEMPERATURE = 85;
    private static final int MIN_NFC_READ_BYTE_SIZE = 4;
    static final int MIN_TEMPERATURE = -30;
    private static final int SAMPLE_PAGE_LENGTH = 63;
    private static final float _TIMESTAMP_VALUE_INDICATOR = 400.0f;
    private CaenNfcProto _nfc_proto;
    private Tag _tag;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class OutputRegisters {
        boolean[] binAlarms = new boolean[16];
        short[] binCounters = new short[16];
        float lastSampleValue;
        short samplesNumber;

        OutputRegisters() {
        }

        void setBinAlarms(short s) {
            for (int i = 0; i < 16; i++) {
                this.binAlarms[i] = ((((short) ((int) Math.pow(2.0d, (double) i))) & s) & 65535) != 0;
            }
        }

        void setBinCounters(short[] sArr) {
            if (sArr.length == 16) {
                for (int i = 0; i < 16; i++) {
                    this.binCounters[i] = sArr[i];
                }
                return;
            }
            throw new IllegalArgumentException("Counters registers must be 16");
        }

        void setLastSampleValue(short s) {
            this.lastSampleValue = C0421util.getFloatTemperature(s);
        }

        void setSamplesNumber(short s) {
            this.samplesNumber = s;
        }

        boolean[] getBinAlarms() {
            return this.binAlarms;
        }

        short[] getBinCounters() {
            return this.binCounters;
        }

        float getLastSampleValue() {
            return this.lastSampleValue;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public short getSamplesNumber() {
            return this.samplesNumber;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TemperatureTag(Tag tag) {
        this._tag = null;
        this._nfc_proto = null;
        this._tag = tag;
        this._nfc_proto = new CaenNfcProto(tag);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CaenNfcStatus getStatus() throws IOException {
        return new CaenNfcStatus(this._nfc_proto.readRegister((short) 81));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CaenNfcSettings getAllSettings() throws IOException {
        return new CaenNfcSettings(this._nfc_proto.readRegisters((short) 10, 57, false));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean writeAllSettings(CaenNfcSettings caenNfcSettings) throws IOException {
        return this._nfc_proto.writeRegisters((short) 10, 57, caenNfcSettings.raw());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getLastSample() throws IOException {
        return C0421util.getFloatTemperature(this._nfc_proto.readRegister(CaenNfcConstants.REGISTER_LAST_SAMPLE));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Date getStopDate() throws IOException {
        byte[] readRegister = this._nfc_proto.readRegister(CaenNfcConstants.REGISTER_STOP_DATE_L);
        byte[] readRegister2 = this._nfc_proto.readRegister(CaenNfcConstants.REGISTER_STOP_DATE_H);
        return new Date(C0421util.bytesToInt32(new byte[]{readRegister2[0], readRegister2[1], readRegister[0], readRegister[1]}, 0) * 1000);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Date getShippingDate() throws IOException {
        byte[] readRegister = this._nfc_proto.readRegister(CaenNfcConstants.REGISTER_SHIPPING_DATE_L);
        byte[] readRegister2 = this._nfc_proto.readRegister(CaenNfcConstants.REGISTER_SHIPPING_DATE_H);
        return new Date(C0421util.bytesToInt32(new byte[]{readRegister2[0], readRegister2[1], readRegister[0], readRegister[1]}, 0) * 1000);
    }

    @Deprecated
    public Float[] OLDVER_getAllSamples(int i) throws IOException {
        short s = CaenNfcConstants.REGISTER_LOG_AREA_0;
        int i2 = i + 138;
        ArrayList arrayList = new ArrayList();
        while (arrayList.size() < i2 - 138) {
            ArrayList<Float> samplePage = getSamplePage(s, i2);
            if (samplePage != null && samplePage.size() > 0) {
                arrayList.addAll(samplePage);
                s = (short) (s + samplePage.size());
            }
        }
        return (Float[]) arrayList.toArray(new Float[0]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SensorSample[] getAllSamples(OutputRegisters outputRegisters) throws IOException {
        try {
            float floatTemperature = 0;
            int findBinFor = 0;
            SensorSample value = null;
            Exception e = null;
            int i = 0;
            ArrayList arrayList = new ArrayList();
            CaenNfcSettings tagSettings = gdata.instance().getTagSettings(true);
            ArrayList<Short> allSamplesRaw = getAllSamplesRaw(outputRegisters, tagSettings);
            int time = (int) (getShippingDate().getTime() / 1000);
            int[] iArr = new int[16];
            for (int i2 = 0; i2 < 16; i2++) {
                iArr[i2] = -1;
            }
            short samplingDelay = tagSettings.controlRegister().delayEnabled() ? tagSettings.samplingDelay() : (short) 0;
            int i3 = 0;
            while (i3 < allSamplesRaw.size()) {
                int i4 = i3 + 1;
                try {
                    floatTemperature = C0421util.getFloatTemperature(C0421util.int16ToBytes(allSamplesRaw.get(i3).shortValue()));
                    findBinFor = tagSettings.findBinFor(floatTemperature);
                    iArr[findBinFor] = iArr[findBinFor] + 1;
                    value = new SensorSample().setValue(floatTemperature);
                } catch (Exception e1) {
                    e = e1;
                }
                if (tagSettings.sampleBinHasTimestamp(floatTemperature)) {
                    if (i4 < allSamplesRaw.size() - 1) {
                        int i5 = i4 + 1;
                        try {
                            i = i5 + 1;
                        } catch (Exception e2) {
                            i4 = i5;
                            e = e2;
                        }
                        try {
                            value.setTimestamp((allSamplesRaw.get(i5).shortValue() << 16) | (allSamplesRaw.get(i4).shortValue() & 65535));
                            i3 = i;
                            arrayList.add(value);
                        } catch (Exception e3) {
                            e = e3;
                            i4 = i;
                            Log.e(getClass().getName(), e.getMessage() == null ? e.toString() : e.getMessage());
                            i3 = i4;
                        }
                    }
                } else {
                    value.setTimestamp((tagSettings.samplingTime(findBinFor) * iArr[findBinFor]) + samplingDelay + time);
                }
                i3 = i4;
                arrayList.add(value);
            }
            return (SensorSample[]) arrayList.toArray(new SensorSample[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Short> getAllSamplesRaw(OutputRegisters outputRegisters, CaenNfcSettings caenNfcSettings) throws IOException {
        int i;
        ArrayList<Short> arrayList = new ArrayList<>();
        short[] binCounters = outputRegisters.getBinCounters();
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        while (true) {
            int i5 = 2;
            if (i2 >= 16) {
                break;
            }
            if (caenNfcSettings.isBinEnabled(i2)) {
                if (!caenNfcSettings.storeSamples(i2)) {
                    i5 = 0;
                }
                if (caenNfcSettings.storeTime(i2)) {
                    i5 += 4;
                }
                i = i5 * binCounters[i2];
            } else {
                i = 0;
            }
            i3 += i;
            i4 += (caenNfcSettings.storeSamples(i2) || caenNfcSettings.storeTime(i2)) ? binCounters[i2] : (short) 0;
            i2++;
        }
        int min = Math.min(8192, i3);
        if (i4 != outputRegisters.getSamplesNumber()) {
            throw new IOException("[getAllSamplesRaw] Error reading samples number from bin");
        }
        int i6 = min / 252;
        int i7 = min % 252;
        int i8 = (i7 / 4) + (i7 % 4 == 0 ? 0 : 1);
        ByteBuffer allocate = ByteBuffer.allocate((i6 * 252) + (i8 * 4));
        if (i6 != 0 || i7 != 0) {
            short s = CaenNfcConstants.REGISTER_LOG_AREA_0;
            for (int i9 = 0; i9 < i6; i9++) {
                byte[] readRegisters = this._nfc_proto.readRegisters(s, 63, true);
                if (readRegisters == null) {
                    throw new IOException("[getAllSamplesRaw] error reading register ");
                }
                allocate.put(readRegisters);
                s = (short) (s + 126);
            }
            byte[] readRegisters2 = this._nfc_proto.readRegisters(s, i8, true);
            if (readRegisters2 == null) {
                throw new IOException("[getAllSamplesRaw] error reading register ");
            }
            allocate.put(readRegisters2);
            for (int i10 = 0; i10 < min; i10 += 2) {
                arrayList.add(Short.valueOf(C0421util.bytesToInt16(new byte[]{allocate.get(i10), allocate.get(i10 + 1)})));
            }
            allocate.clear();
        }
        return arrayList;
    }

    private short[] getBinCounters() throws IOException {
        short[] sArr = new short[16];
        byte[] readRegisters = this._nfc_proto.readRegisters((short) 86, 16, false);
        for (int i = 0; i < 16; i++) {
            int i2 = i * 2;
            sArr[i] = (short) ((readRegisters[i2] << 8) + (readRegisters[i2 + 1] & 255));
        }
        return sArr;
    }

    public OutputRegisters getOutputRegisters() throws IOException {
        short[] sArr = new short[19];
        byte[] readRegisters = this._nfc_proto.readRegisters((short) 85, 19, false);
        for (int i = 0; i < 19; i++) {
            int i2 = i * 2;
            sArr[i] = (short) ((readRegisters[i2] << 8) + (readRegisters[i2 + 1] & 255));
        }
        OutputRegisters outputRegisters = new OutputRegisters();
        outputRegisters.setBinAlarms(sArr[0]);
        outputRegisters.setBinCounters(Arrays.copyOfRange(sArr, 1, 17));
        outputRegisters.setLastSampleValue(sArr[17]);
        outputRegisters.setSamplesNumber(sArr[18]);
        return outputRegisters;
    }

    public ArrayList<Float> getSamplePage(short s, int i) throws IOException {
        ArrayList<Float> arrayList = new ArrayList<>();
        int min = Math.min(i - s, 63);
        byte[] readRegisters = this._nfc_proto.readRegisters(s, min, true);
        if (readRegisters != null) {
            for (int i2 = 0; i2 < min; i2 += 2) {
                arrayList.add(Float.valueOf(C0421util.getFloatTemperature(new byte[]{readRegisters[i2], readRegisters[i2 + 1]})));
            }
            return arrayList;
        }
        throw new IOException("[getAllSamples] error reading register ");
    }

    public byte[] getRegister(short s) throws IOException {
        return this._nfc_proto.readRegister(s);
    }

    public short getRegisterAsShort(short s) throws IOException {
        return C0421util.bytesToInt16(this._nfc_proto.readRegister(s));
    }

    public Tag getTag() {
        return this._tag;
    }

    public boolean isCAENRFIDTag() {
        return this._nfc_proto.isCaenRFIDTag();
    }
}
