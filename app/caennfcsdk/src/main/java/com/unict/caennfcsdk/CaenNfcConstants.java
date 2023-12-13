package com.unict.caennfcsdk;

import java.util.HashMap;
import java.util.Map;

public final class CaenNfcConstants {
    public static final short BIN_NUMBER = 16;
    public static final int GS1_COMPANY_PREFIX = 803404109;
    public static final int GS1_EPC_HEADER = 48;
    public static final int GS1_FILTER = 0;
    public static final Map<String, Integer> GS1_FORMAT;
    public static final int GS1_ITEM_REFERENCE = 112;
    public static final int GS1_PARTITION = 3;
    public static final short LEN_BIN_HLIMIT = 16;
    public static final short LEN_BIN_SAMPLETIME = 16;
    public static final short LEN_BIN_THRESHOLD = 16;
    public static final short LEN_LOG_AREA = 4096;
    public static final short LEN_USER_AREA = 28;
    public static final short REGISTER_BIN_ALARM = 85;
    public static final short REGISTER_BIN_ENABLE = 16;
    public static final short REGISTER_BIN_HLIMIT_0 = 19;
    public static final short REGISTER_BIN_SAMPLETIME_0 = 35;
    public static final short REGISTER_BIN_THRESHOLD_0 = 51;
    public static final short REGISTER_CONTROL = 10;
    public static final short REGISTER_COUNTER_0 = 86;
    public static final short REGISTER_ENA_SAMPLE_STORE = 17;
    public static final short REGISTER_ENA_TIME_STORE = 18;
    public static final short REGISTER_ETA_H = 15;
    public static final short REGISTER_ETA_L = 14;
    public static final short REGISTER_INIT_DATE_H = 13;
    public static final short REGISTER_INIT_DATE_L = 12;
    public static final short REGISTER_LAST_SAMPLE = 102;
    public static final short REGISTER_LOG_AREA_0 = 138;
    public static final short REGISTER_MKT_ACTV_ENERGY_H = 68;
    public static final short REGISTER_MKT_ACTV_ENERGY_L = 67;
    public static final short REGISTER_MKT_THRESHOLD_TEMP = 69;
    public static final short REGISTER_MTK_VAL = 82;
    public static final short REGISTER_SAMPLES_NUM = 103;
    public static final short REGISTER_SAMPLING_DELAY = 11;
    public static final short REGISTER_SHIPPING_DATE_H = 107;
    public static final short REGISTER_SHIPPING_DATE_L = 106;
    public static final short REGISTER_SHL_Q10_TEMP_L = 71;
    public static final short REGISTER_SHL_Q10_TEMP_h = 72;
    public static final short REGISTER_SHL_REF_TEMP = 70;
    public static final short REGISTER_SHL_REF_TIME = 73;
    public static final short REGISTER_SHL_THRESHOLD = 74;
    public static final short REGISTER_SHL_TIME_H = 84;
    public static final short REGISTER_SHL_TIME_L = 83;
    public static final short REGISTER_STATUS = 81;
    public static final short REGISTER_STOP_DATE_H = 109;
    public static final short REGISTER_STOP_DATE_L = 108;
    public static final short REGISTER_USER_AREA_0 = 110;

    static {
        HashMap hashMap = new HashMap();
        GS1_FORMAT = hashMap;
        hashMap.put("GS1_HEADER", 8);
        GS1_FORMAT.put("GS1_FILTER", 3);
        GS1_FORMAT.put("GS1_PARTITION", 3);
        GS1_FORMAT.put("GS1_COMPANY_PREFIX", 30);
        GS1_FORMAT.put("GS1_ITEM_REFERENCE", 14);
        GS1_FORMAT.put("GS1_SERIAL", 38);
    }

    private CaenNfcConstants() {
    }
}
