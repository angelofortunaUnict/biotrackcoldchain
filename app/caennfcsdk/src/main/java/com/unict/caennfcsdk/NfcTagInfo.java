package com.unict.caennfcsdk;

import android.nfc.Tag;

import java.util.Arrays;
import java.util.List;

public class NfcTagInfo {
    private Tag _tag = null;

    public NfcTagInfo(Tag tag) {
        this._tag = tag;
    }

    public Tag tag() {
        return this._tag;
    }

    public String getInfoForTech() {
        String str = "\n";
        for (String replace : this._tag.getTechList()) {
            for (String str2 : getTagInfo(this._tag, replace.replace("android.nfc.tech.", ""))) {
                str = str + str2 + "\n";
            }
        }
        return str;
    }

    public boolean hasTech(String str) {
        List<String> asList = Arrays.asList(this._tag.getTechList());
        return asList.contains("android.nfc.tech." + str);
    }

    private final List<String> getTagInfo(Tag tag, String str) {
        return getTagInfo(tag, str, false);
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final List<String> getTagInfo(Tag r10, String r11, boolean r12) {
        /*
            r9 = this;
            java.lang.String r0 = "  record["
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
            int r2 = r11.hashCode()
            r3 = 0
            switch(r2) {
                case -2095458966: goto L_0x004c;
                case -484702680: goto L_0x0042;
                case 2424854: goto L_0x0038;
                case 2424859: goto L_0x002e;
                case 2424875: goto L_0x0024;
                case 850504820: goto L_0x001a;
                case 1201714490: goto L_0x0010;
                default: goto L_0x000f;
            }
        L_0x000f:
            goto L_0x0056
        L_0x0010:
            java.lang.String r2 = "NdefParcelable"
            boolean r2 = r11.equals(r2)
            if (r2 == 0) goto L_0x0056
            r2 = 3
            goto L_0x0057
        L_0x001a:
            java.lang.String r2 = "MifareUltralight"
            boolean r2 = r11.equals(r2)
            if (r2 == 0) goto L_0x0056
            r2 = 5
            goto L_0x0057
        L_0x0024:
            java.lang.String r2 = "NfcV"
            boolean r2 = r11.equals(r2)
            if (r2 == 0) goto L_0x0056
            r2 = 2
            goto L_0x0057
        L_0x002e:
            java.lang.String r2 = "NfcF"
            boolean r2 = r11.equals(r2)
            if (r2 == 0) goto L_0x0056
            r2 = 1
            goto L_0x0057
        L_0x0038:
            java.lang.String r2 = "NfcA"
            boolean r2 = r11.equals(r2)
            if (r2 == 0) goto L_0x0056
            r2 = 0
            goto L_0x0057
        L_0x0042:
            java.lang.String r2 = "NdefFormatable"
            boolean r2 = r11.equals(r2)
            if (r2 == 0) goto L_0x0056
            r2 = 4
            goto L_0x0057
        L_0x004c:
            java.lang.String r2 = "IsoDep"
            boolean r2 = r11.equals(r2)
            if (r2 == 0) goto L_0x0056
            r2 = 6
            goto L_0x0057
        L_0x0056:
            r2 = -1
        L_0x0057:
            java.lang.String r4 = "  type: "
            java.lang.String r5 = "  maxTransceiveLength: "
            java.lang.String r6 = "Tech:"
            switch(r2) {
                case 0: goto L_0x03c6;
                case 1: goto L_0x035a;
                case 2: goto L_0x02f5;
                case 3: goto L_0x0178;
                case 4: goto L_0x016f;
                case 5: goto L_0x0113;
                case 6: goto L_0x0076;
                default: goto L_0x0060;
            }
        L_0x0060:
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r12 = "Unknown Tech:"
            r10.append(r12)
            r10.append(r11)
            java.lang.String r10 = r10.toString()
            r1.add(r10)
            goto L_0x042d
        L_0x0076:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r0.append(r6)
            r0.append(r11)
            java.lang.String r11 = r0.toString()
            r1.add(r11)
            java.lang.String r11 = "  aka ISO 14443-4"
            r1.add(r11)
            android.nfc.tech.IsoDep r10 = android.nfc.tech.IsoDep.get(r10)
            if (r12 == 0) goto L_0x00fb
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r12 = "  historicalBytes: "
            r11.append(r12)
            byte[] r12 = r10.getHistoricalBytes()
            java.lang.String r12 = com.caenrfid.caennfc.C0421util.byteArrayToHex(r12)
            r11.append(r12)
            java.lang.String r11 = r11.toString()
            r1.add(r11)
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r12 = "  hiLayerResponse: "
            r11.append(r12)
            byte[] r12 = r10.getHiLayerResponse()
            java.lang.String r12 = com.caenrfid.caennfc.C0421util.byteArrayToHex(r12)
            r11.append(r12)
            java.lang.String r11 = r11.toString()
            r1.add(r11)
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r12 = "  timeout: "
            r11.append(r12)
            int r12 = r10.getTimeout()
            r11.append(r12)
            java.lang.String r11 = r11.toString()
            r1.add(r11)
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r12 = "  extendedLengthApduSupported: "
            r11.append(r12)
            boolean r12 = r10.isExtendedLengthApduSupported()
            r11.append(r12)
            java.lang.String r11 = r11.toString()
            r1.add(r11)
        L_0x00fb:
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            r11.append(r5)
            int r10 = r10.getMaxTransceiveLength()
            r11.append(r10)
            java.lang.String r10 = r11.toString()
            r1.add(r10)
            goto L_0x042d
        L_0x0113:
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            r12.append(r6)
            r12.append(r11)
            java.lang.String r11 = r12.toString()
            r1.add(r11)
            android.nfc.tech.MifareUltralight r10 = android.nfc.tech.MifareUltralight.get(r10)
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            r11.append(r4)
            int r12 = r10.getType()
            r11.append(r12)
            java.lang.String r11 = r11.toString()
            r1.add(r11)
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r12 = "  tiemout: "
            r11.append(r12)
            int r12 = r10.getTimeout()
            r11.append(r12)
            java.lang.String r11 = r11.toString()
            r1.add(r11)
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            r11.append(r5)
            int r10 = r10.getMaxTransceiveLength()
            r11.append(r10)
            java.lang.String r10 = r11.toString()
            r1.add(r10)
            goto L_0x042d
        L_0x016f:
            if (r12 == 0) goto L_0x042d
            java.lang.String r10 = "  NdefFormatable: nothing to read"
            r1.add(r10)
            goto L_0x042d
        L_0x0178:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append(r6)
            r2.append(r11)
            java.lang.String r11 = r2.toString()
            r1.add(r11)
            android.nfc.tech.Ndef r10 = android.nfc.tech.Ndef.get(r10)
            if (r12 == 0) goto L_0x02db
            r10.connect()     // Catch:{ Exception -> 0x0233 }
            android.nfc.NdefMessage r11 = r10.getNdefMessage()     // Catch:{ Exception -> 0x0233 }
            r10.close()     // Catch:{ Exception -> 0x0233 }
            android.nfc.NdefRecord[] r12 = r11.getRecords()     // Catch:{ Exception -> 0x0233 }
            int r2 = r12.length     // Catch:{ Exception -> 0x0233 }
        L_0x019f:
            if (r3 >= r2) goto L_0x021a
            r5 = r12[r3]     // Catch:{ Exception -> 0x0233 }
            byte[] r6 = r5.getId()     // Catch:{ Exception -> 0x0233 }
            int r6 = r6.length     // Catch:{ Exception -> 0x0233 }
            if (r6 != 0) goto L_0x01ad
            java.lang.String r6 = "null"
            goto L_0x01b5
        L_0x01ad:
            byte[] r6 = r5.getId()     // Catch:{ Exception -> 0x0233 }
            java.lang.String r6 = com.caenrfid.caennfc.C0421util.byteArrayToHex(r6)     // Catch:{ Exception -> 0x0233 }
        L_0x01b5:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0233 }
            r7.<init>()     // Catch:{ Exception -> 0x0233 }
            r7.append(r0)     // Catch:{ Exception -> 0x0233 }
            r7.append(r6)     // Catch:{ Exception -> 0x0233 }
            java.lang.String r8 = "].tnf: "
            r7.append(r8)     // Catch:{ Exception -> 0x0233 }
            short r8 = r5.getTnf()     // Catch:{ Exception -> 0x0233 }
            r7.append(r8)     // Catch:{ Exception -> 0x0233 }
            java.lang.String r7 = r7.toString()     // Catch:{ Exception -> 0x0233 }
            r1.add(r7)     // Catch:{ Exception -> 0x0233 }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0233 }
            r7.<init>()     // Catch:{ Exception -> 0x0233 }
            r7.append(r0)     // Catch:{ Exception -> 0x0233 }
            r7.append(r6)     // Catch:{ Exception -> 0x0233 }
            java.lang.String r8 = "].type: "
            r7.append(r8)     // Catch:{ Exception -> 0x0233 }
            byte[] r8 = r5.getType()     // Catch:{ Exception -> 0x0233 }
            java.lang.String r8 = com.caenrfid.caennfc.C0421util.byteArrayToHex(r8)     // Catch:{ Exception -> 0x0233 }
            r7.append(r8)     // Catch:{ Exception -> 0x0233 }
            java.lang.String r7 = r7.toString()     // Catch:{ Exception -> 0x0233 }
            r1.add(r7)     // Catch:{ Exception -> 0x0233 }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0233 }
            r7.<init>()     // Catch:{ Exception -> 0x0233 }
            r7.append(r0)     // Catch:{ Exception -> 0x0233 }
            r7.append(r6)     // Catch:{ Exception -> 0x0233 }
            java.lang.String r6 = "].payload: "
            r7.append(r6)     // Catch:{ Exception -> 0x0233 }
            byte[] r5 = r5.getPayload()     // Catch:{ Exception -> 0x0233 }
            java.lang.String r5 = com.caenrfid.caennfc.C0421util.byteArrayToHex(r5)     // Catch:{ Exception -> 0x0233 }
            r7.append(r5)     // Catch:{ Exception -> 0x0233 }
            java.lang.String r5 = r7.toString()     // Catch:{ Exception -> 0x0233 }
            r1.add(r5)     // Catch:{ Exception -> 0x0233 }
            int r3 = r3 + 1
            goto L_0x019f
        L_0x021a:
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0233 }
            r12.<init>()     // Catch:{ Exception -> 0x0233 }
            java.lang.String r0 = "  messageSize: "
            r12.append(r0)     // Catch:{ Exception -> 0x0233 }
            int r11 = r11.getByteArrayLength()     // Catch:{ Exception -> 0x0233 }
            r12.append(r11)     // Catch:{ Exception -> 0x0233 }
            java.lang.String r11 = r12.toString()     // Catch:{ Exception -> 0x0233 }
            r1.add(r11)     // Catch:{ Exception -> 0x0233 }
            goto L_0x024f
        L_0x0233:
            r11 = move-exception
            r11.printStackTrace()
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r0 = "  error reading message: "
            r12.append(r0)
            java.lang.String r11 = r11.toString()
            r12.append(r11)
            java.lang.String r11 = r12.toString()
            r1.add(r11)
        L_0x024f:
            java.util.HashMap r11 = new java.util.HashMap
            r11.<init>()
            java.lang.String r12 = "org.nfcforum.ndef.type1"
            java.lang.String r0 = "typically Innovision Topaz"
            r11.put(r12, r0)
            java.lang.String r12 = "org.nfcforum.ndef.type2"
            java.lang.String r0 = "typically NXP MIFARE Ultralight"
            r11.put(r12, r0)
            java.lang.String r12 = "org.nfcforum.ndef.type3"
            java.lang.String r0 = "typically Sony Felica"
            r11.put(r12, r0)
            java.lang.String r12 = "org.nfcforum.ndef.type4"
            java.lang.String r0 = "typically NXP MIFARE Desfire"
            r11.put(r12, r0)
            java.lang.String r12 = r10.getType()
            java.lang.Object r0 = r11.get(r12)
            if (r0 == 0) goto L_0x0299
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r0.append(r12)
            java.lang.String r2 = " ("
            r0.append(r2)
            java.lang.Object r11 = r11.get(r12)
            java.lang.String r11 = (java.lang.String) r11
            r0.append(r11)
            java.lang.String r11 = ")"
            r0.append(r11)
            java.lang.String r12 = r0.toString()
        L_0x0299:
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            r11.append(r4)
            r11.append(r12)
            java.lang.String r11 = r11.toString()
            r1.add(r11)
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r12 = "  canMakeReadOnly: "
            r11.append(r12)
            boolean r12 = r10.canMakeReadOnly()
            r11.append(r12)
            java.lang.String r11 = r11.toString()
            r1.add(r11)
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r12 = "  isWritable: "
            r11.append(r12)
            boolean r12 = r10.isWritable()
            r11.append(r12)
            java.lang.String r11 = r11.toString()
            r1.add(r11)
        L_0x02db:
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r12 = "  maxSize: "
            r11.append(r12)
            int r10 = r10.getMaxSize()
            r11.append(r10)
            java.lang.String r10 = r11.toString()
            r1.add(r10)
            goto L_0x042d
        L_0x02f5:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r0.append(r6)
            r0.append(r11)
            java.lang.String r11 = r0.toString()
            r1.add(r11)
            java.lang.String r11 = "  aka ISO 15693"
            r1.add(r11)
            android.nfc.tech.NfcV r10 = android.nfc.tech.NfcV.get(r10)
            if (r12 == 0) goto L_0x0342
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r12 = "  dsfId: "
            r11.append(r12)
            byte r12 = r10.getDsfId()
            r11.append(r12)
            java.lang.String r11 = r11.toString()
            r1.add(r11)
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r12 = "  responseFlags: "
            r11.append(r12)
            byte r12 = r10.getResponseFlags()
            r11.append(r12)
            java.lang.String r11 = r11.toString()
            r1.add(r11)
        L_0x0342:
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            r11.append(r5)
            int r10 = r10.getMaxTransceiveLength()
            r11.append(r10)
            java.lang.String r10 = r11.toString()
            r1.add(r10)
            goto L_0x042d
        L_0x035a:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r0.append(r6)
            r0.append(r11)
            java.lang.String r11 = r0.toString()
            r1.add(r11)
            java.lang.String r11 = "  aka JIS 6319-4"
            r1.add(r11)
            android.nfc.tech.NfcF r10 = android.nfc.tech.NfcF.get(r10)
            if (r12 == 0) goto L_0x03af
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r12 = "  manufacturer: "
            r11.append(r12)
            byte[] r12 = r10.getManufacturer()
            java.lang.String r12 = com.caenrfid.caennfc.C0421util.byteArrayToHex(r12)
            r11.append(r12)
            java.lang.String r11 = r11.toString()
            r1.add(r11)
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r12 = "  systemCode: "
            r11.append(r12)
            byte[] r12 = r10.getSystemCode()
            java.lang.String r12 = com.caenrfid.caennfc.C0421util.byteArrayToHex(r12)
            r11.append(r12)
            java.lang.String r11 = r11.toString()
            r1.add(r11)
        L_0x03af:
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            r11.append(r5)
            int r10 = r10.getMaxTransceiveLength()
            r11.append(r10)
            java.lang.String r10 = r11.toString()
            r1.add(r10)
            goto L_0x042d
        L_0x03c6:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r0.append(r6)
            r0.append(r11)
            java.lang.String r11 = r0.toString()
            r1.add(r11)
            java.lang.String r11 = "  aka ISO 14443-3A"
            r1.add(r11)
            android.nfc.tech.NfcA r10 = android.nfc.tech.NfcA.get(r10)
            if (r12 == 0) goto L_0x0417
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r12 = "  atqa: "
            r11.append(r12)
            byte[] r12 = r10.getAtqa()
            java.lang.String r12 = com.caenrfid.caennfc.C0421util.byteArrayToHex(r12)
            r11.append(r12)
            java.lang.String r11 = r11.toString()
            r1.add(r11)
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r12 = "  sak: "
            r11.append(r12)
            short r12 = r10.getSak()
            r11.append(r12)
            java.lang.String r11 = r11.toString()
            r1.add(r11)
        L_0x0417:
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            r11.append(r5)
            int r10 = r10.getMaxTransceiveLength()
            r11.append(r10)
            java.lang.String r10 = r11.toString()
            r1.add(r10)
        L_0x042d:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.caenrfid.caennfc.NfcTagInfo.getTagInfo(android.nfc.Tag, java.lang.String, boolean):java.util.List");
    }
}
