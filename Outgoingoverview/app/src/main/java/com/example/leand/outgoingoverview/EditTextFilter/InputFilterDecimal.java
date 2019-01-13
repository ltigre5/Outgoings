package com.example.leand.outgoingoverview.EditTextFilter;

import android.text.Spanned;
import android.text.method.DigitsKeyListener;

/**
 * With this filter, the entered value in a editText will be in following format.
 * 0.00, 12.30, 0.80, 67.00
 *
 */

public class InputFilterDecimal extends DigitsKeyListener {
    private final int beforeDecimal;
    private final int afterDecimal;

    // Declaration
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Constructor

    public InputFilterDecimal(int beforeDecimal, int afterDecimal) {
        super(Boolean.FALSE, Boolean.TRUE);
        this.beforeDecimal = beforeDecimal;
        this.afterDecimal = afterDecimal;
    }

    // Constructor
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // set filter

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        StringBuilder builder = new StringBuilder(dest);
        builder.insert(dstart, source);
        String temp = builder.toString();

        if (temp.equals(".")) {
            return "0.";
        } else if (temp.indexOf('.') == -1) {
            if (temp.length() > beforeDecimal) {
                return "";
            }
        } else {
            if (temp.substring(0, temp.indexOf('.')).length() > beforeDecimal || temp.substring(temp.indexOf('.') + 1, temp.length()).length() > afterDecimal) {
                return "";
            }
        }

        return super.filter(source, start, end, dest, dstart, dend);
    }

    // set filter
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // End

}