package com.golan.amit.guessmynumber;

public class GussNumberHelper {

    public static final int LOW = 50;
    public static final int HIGH = 5000;

    public static final int LOWBEGINNER = 0;
    public static final int HIGHBEGINNER = 100;

    public static final int ALLOWEDATTEMPTS = 20;
    private int _number;
    private int _attempts;

    public GussNumberHelper() {
        gen();
        set_attempts(1);
    }

    public void gen() {
        this._number = (int) (Math.random() * (HIGH - LOW) + LOW);
    }

    public void genBeginner() {
        this._number = (int)(Math.random() * (HIGHBEGINNER - LOWBEGINNER) + LOWBEGINNER);
    }

    public int get_number() {
        return _number;
    }

    public void set_number(int _number) {
        this._number = _number;
    }

    public int get_attempts() {
        return _attempts;
    }

    public void set_attempts(int _attempts) {
        this._attempts = _attempts;
    }

    public void resetAttempts() {
        set_attempts(1);
    }

    public void increaseAttempts() {
        this._attempts++;
    }

    @Override
    public String toString() {
        return "" + get_number();
    }
}
