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

    API_PATH("192.168.1.254"),

    /**
     * MENU
     */
    MENU_MAIN("เบิก/คืน สป."),
    MENU_SETTING("ตั้งค่า"),
    MENU_LOGOUT("ล็อคเอ้า"),

    /**
     * TITLE
     */
    TITLE_CONFIRM_LOGOUT("Logout Confirmation"),
    TITLE_TAB_WITHDRAW("Withdraw"),
    TITLE_TAB_DEPOSIT("Deposit"),


    /**
     * TEXT
     */
    CONTENT_CONFIRM_LOGOUT("Do you want to Logout ?"),
    EXIT("Exit"),
    CANCEL("Cancel"),
    TEXT_SCAN_PERSON("แสกนบัตรทหารเพื่อแสดงตน"),
    TEXT_SCAN_WEAPON("แสกนบาโค้ดของอาวุธ"),


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
