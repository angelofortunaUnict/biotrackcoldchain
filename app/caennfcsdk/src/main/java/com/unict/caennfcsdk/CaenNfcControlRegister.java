package com.unict.caennfcsdk;

public class CaenNfcControlRegister {
    private static final int _CONTROL_BIT_00_RESET = 1;
    private static final int _CONTROL_BIT_02_LOGGING_ENABLE = 4;
    private static final int _CONTROL_BIT_03_DELAY_ENABLE = 8;
    private static final int _CONTROL_BIT_04_HIGH_SENS = 16;
    private static final int _CONTROL_BIT_08_CLEAR_BATT_ERR = 256;
    public static final String GLYPH_ORIENTATION_VERTICAL_180_DEGREES = "180";
    public static final String GLYPH_ORIENTATION_VERTICAL_270_DEGREES = "270";
    public static final String GLYPH_ORIENTATION_VERTICAL_360_DEGREES = "360";
    public static final String GLYPH_ORIENTATION_VERTICAL_90_DEGREES = "90";
    public static final String GLYPH_ORIENTATION_VERTICAL_AUTO = "Auto";
    public static final String GLYPH_ORIENTATION_VERTICAL_MINUS_180_DEGREES = "-180";
    public static final String GLYPH_ORIENTATION_VERTICAL_MINUS_90_DEGREES = "-90";
    public static final String GLYPH_ORIENTATION_VERTICAL_ZERO_DEGREES = "0";
    private boolean _b00_reset;
    private boolean _b02_samples_log_enable;
    private boolean _b03_sample_delay_enable;
    private boolean _b04_high_sens;
    private boolean _b08_clear_battery_error;

    public CaenNfcControlRegister() {
        this(0);
    }

    public CaenNfcControlRegister(int i) {
        boolean z = false;
        this._b00_reset = (i & 1) != 0;
        this._b02_samples_log_enable = (i & 4) != 0;
        this._b03_sample_delay_enable = (i & 8) != 0;
        this._b04_high_sens = (i & 16) != 0;
        this._b08_clear_battery_error = (i & 256) != 0 ? true : z;
    }

    public short raw() {
        short s = this._b00_reset ? (short) 1 : 0;
        if (this._b02_samples_log_enable) {
            s = (short) (s | 4);
        }
        if (this._b03_sample_delay_enable) {
            s = (short) (s | 8);
        }
        if (this._b04_high_sens) {
            s = (short) (s | 16);
        }
        return this._b08_clear_battery_error ? (short) (s | 256) : s;
    }

    public String description() {
        StringBuilder sb = new StringBuilder();
        sb.append("rst(");
        String str = "1";
        sb.append(this._b00_reset ? str : GLYPH_ORIENTATION_VERTICAL_ZERO_DEGREES);
        sb.append(") log(");
        sb.append(this._b02_samples_log_enable ? str : GLYPH_ORIENTATION_VERTICAL_ZERO_DEGREES);
        sb.append(") dly(");
        sb.append(this._b03_sample_delay_enable ? str : GLYPH_ORIENTATION_VERTICAL_ZERO_DEGREES);
        sb.append(") hhs(");
        sb.append(this._b04_high_sens ? str : GLYPH_ORIENTATION_VERTICAL_ZERO_DEGREES);
        sb.append(") clr(");
        if (!this._b08_clear_battery_error) {
            str = GLYPH_ORIENTATION_VERTICAL_ZERO_DEGREES;
        }
        sb.append(str);
        sb.append(")");
        return sb.toString();
    }

    public boolean reset() {
        return this._b00_reset;
    }

    public void setReset(boolean z) {
        this._b00_reset = z;
    }

    public boolean samplesLoggingEnabled() {
        return this._b02_samples_log_enable;
    }

    public void enableSamplesLogging(boolean z) {
        this._b02_samples_log_enable = z;
    }

    public boolean delayEnabled() {
        return this._b03_sample_delay_enable;
    }

    public void enableSampleDelay(boolean z) {
        this._b03_sample_delay_enable = z;
    }

    public boolean highSensitivity() {
        return this._b04_high_sens;
    }

    public void setHighSensitivity(boolean z) {
        this._b04_high_sens = z;
    }

    public boolean clearBatteryError() {
        return this._b08_clear_battery_error;
    }

    public void setClearBatteryError(boolean z) {
        this._b08_clear_battery_error = z;
    }
}
