package com.infantry.milscanner.Utils;

/**
 * Created by MKinG on 11/2/2015.
 */
public enum Enum {

    /**
     * SharedPreferences
     */
    SHARED_PREF_NAME("SHARED_PREF"),
    SHARED_PREF_KEY_TOKEN("KEY_TOKEN"),
    SHARED_PREF_KEY_API_PATH("KEY_API_PATH"),
    SHARED_PREF_USER("USER"),

    /**
     * MODE
     */
    MODE_LOGIN("LOGIN"),
    MODE_USER("USER"),
    MODE_WEAPON("WEAPON"),
    MODE_WITHDRAW("WITHDRAW"),
    MODE_DEPOSIT("DEPOSIT"),
    MODE_INSERT("INSERT"),

//    API_PATH("192.168.1.254"),
    API_PATH("infantry43.dyndns.org"),

    /**
     * MENU
     */
    MENU_MAIN("เบิก/คืน สป."),
    MENU_SETTING("ตั้งค่า"),
    MENU_LOGOUT("ล็อคเอ้า"),

    /**
     * TITLE
     */
    TITLE_CONFIRM_LOGOUT("ล็อคเอ้า ?"),
    TITLE_WARNING_RETURN("คำเตือน"),
    TITLE_TAB_WITHDRAW("เบิก"),
    TITLE_TAB_DEPOSIT("ส่งคืน"),


    /**
     * TEXT
     */
    CONTENT_CONFIRM_LOGOUT("คุณมั่นใจว่าจะล็อคเอ้า ?"),
    EXIT("ออก"),
    OK("ตกลง"),
    CANCEL("ยกเลิก"),
    TEXT_SCAN_PERSON("แสกนบัตรทหารเพื่อแสดงตน"),
    TEXT_SCAN_WEAPON("แสกนบาโค้ดของอาวุธ"),
    TEXT_KEY_WEAPON_NUMBER("ใส่หมายเลขอาวุธ"),
    TEXT_KEY_IDENTITY_ID("ใส่เลขบัตรประชาชน"),


    ;

    private String stringValue;
    private int intValue;

    Enum(String toString) {
        stringValue = toString;
    }

    Enum(int intValue) {
        this.intValue = intValue;
    }

    public String getStringValue() {
        return stringValue;
    }
    public int getIntValue() {
        return intValue;
    }
}
