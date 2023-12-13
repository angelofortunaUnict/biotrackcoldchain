package com.unict.caennfcsdk;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SensorSample {
    private static final long MIN_VALID_DATE = 874195200;
    private Date _tstamp = new Date(0);
    private float _value = 0.0f;

    public SensorSample setValue(float f) {
        this._value = f;
        return this;
    }

    public float value() {
        return this._value;
    }

    public SensorSample setTimestamp(int i) {
        this._tstamp = new Date(((long) i) * 1000);
        return this;
    }

    public Date timestamp() {
        return this._tstamp;
    }

    public String tstampToString() {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(this._tstamp);
    }
}
