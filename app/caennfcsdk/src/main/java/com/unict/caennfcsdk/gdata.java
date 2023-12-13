package com.unict.caennfcsdk;

public class gdata {
    private static gdata _instance;
    private CaenNfcSettings _cn_settings = null;
    private TemperatureTag _cn_temperature = null;

    public static gdata instance() {
        if (_instance == null) {
            _instance = new gdata();
        }
        return _instance;
    }

    private gdata() {
    }

    public void resetInstance() {
        _instance = null;
    }

    public TemperatureTag getNfcTemperature() {
        return this._cn_temperature;
    }

    public void setNfcTemperature(TemperatureTag temperatureTag) {
        this._cn_temperature = temperatureTag;
        if (temperatureTag == null) {
            this._cn_settings = null;
        }
    }

    public boolean hasNfcTemperature() {
        return this._cn_temperature != null;
    }

    public CaenNfcSettings getTagSettings(boolean z) {
        if (z) {
            return readTagSettings();
        }
        CaenNfcSettings caenNfcSettings = this._cn_settings;
        return caenNfcSettings == null ? readTagSettings() : caenNfcSettings;
    }

    private CaenNfcSettings readTagSettings() {
        TemperatureTag temperatureTag = this._cn_temperature;
        if (temperatureTag != null) {
            try {
                this._cn_settings = temperatureTag.getAllSettings();
            } catch (Exception unused) {
                this._cn_settings = null;
            }
        } else {
            this._cn_settings = null;
        }
        return this._cn_settings;
    }

    public boolean writeTagSettings() {
        TemperatureTag temperatureTag = this._cn_temperature;
        if (temperatureTag == null) {
            return false;
        }
        try {
            return temperatureTag.writeAllSettings(this._cn_settings);
        } catch (Exception unused) {
            return false;
        }
    }

    public void updateEnableLogging(boolean enabled) {
        this._cn_settings.controlRegister().enableSamplesLogging(enabled);
        writeTagSettings();
    }

    public void reset() {
        this._cn_settings.controlRegister().setReset(true);
        writeTagSettings();
    }
}
