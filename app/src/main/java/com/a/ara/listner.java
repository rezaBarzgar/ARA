package com.a.ara;

import java.util.Date;

public class listner extends user{
    private int premium_type;
    private Date premium_trial;
    private Date birth_date;

    public static String key_premium_type = "premium_type";
    public static String key_premium_trial = "premium_trial";
    public static String key_birth_date = "birth_date";

    public int getPremium_type() {
        return premium_type;
    }

    public void setPremium_type(int premium_type) {
        this.premium_type = premium_type;
    }

    public Date getPremium_trial() {
        return premium_trial;
    }

    public void setPremium_trial(Date premium_trial) {
        this.premium_trial = premium_trial;
    }

    public Date getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(Date birth_date) {
        this.birth_date = birth_date;
    }
}
