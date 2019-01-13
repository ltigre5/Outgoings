package com.example.leand.outgoingoverview.GeneralHelperClasses;

import android.content.Context;

import java.util.Calendar;

/**
 * With this class you can format a long Date (milliseconds since epoch) into various formats
 *
 */

public class SelectedDate {
    private Integer integer_Year;
    private Integer integer_Month;
    private Integer integer_day;
    private String string_Date;
    private Integer integer_DateWithoutTime;
    private String string_Month;
    private Long long_Date;
    private Calendar calendar;
    private GeneralHelper generalHelper;
    private Context context;


    // Deklaration
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Constructor

    /**
     *Get today's Date to format
     *
     */

    public SelectedDate() {
        generalHelper = new GeneralHelper();
        calendar = Calendar.getInstance();
        this.long_Date = calendar.getTimeInMillis();
    }

    /**
     * Enter date in long to format
     *
     * @param date date in long
     */

    public SelectedDate(long date) {
        this();
        this.long_Date = date;
    }

    /**
     * Enter date with year, month and dayOfMonth to format
     *
     * @param year
     * @param month
     * @param dayOfMonth
     */

    public SelectedDate(Integer year, Integer month, Integer dayOfMonth) {
        this();
        calendar.set(year, month, dayOfMonth);
        this.long_Date = calendar.getTimeInMillis();
    }

    // Constructor
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Getter

    /**
     * get the date in long (milliseconds since epoch)
     *
     * @return date in long (milliseconds since epoch)
     */

    public Long getLong_Date() {
        return long_Date;
    }

    /**
     * get month in string, exp: december
     *
     * @return month in string
     */

    public String getString_Month() {
        string_Month = generalHelper.longDateToMonthWriteOut.format(long_Date);
        return string_Month;
    }

    /**
     * get year in Integer, exp: 2018
     *
     * @return year in Integer
     */

    public Integer getInteger_Year() {
        integer_Year = Integer.valueOf(generalHelper.longDateToYearNumber.format(long_Date));
        return integer_Year;
    }

    /**
     * get month in Integer, exp: 12
     *
     * @return month in Integer
     */

    public Integer getInteger_Month() {
        integer_Month = Integer.valueOf(generalHelper.longDateToMonthNumber.format(long_Date));
        return integer_Month;
    }

    /**
     * get day in Integer, exp: 01
     *
     * @return day in Integer
     */

    public Integer getInteger_day() {
        integer_day = Integer.valueOf(generalHelper.longDateToDayNumber.format(long_Date));
        return integer_day;
    }

    /**
     * get date in Integer, exp: 20181201
     *
     * @return date in Integer
     */

    public Integer getInteger_DateWithoutTime() {
        integer_DateWithoutTime = Integer.parseInt(generalHelper.longDateToDateWithoutTime.format(long_Date));
        return integer_DateWithoutTime;
    }

    /**
     * get date in string, exp: 01/12/2018
     *
     * @return date in string
     */

    public String getString_Date() {
        string_Date = generalHelper.longDateToDDMMYYYY.format(long_Date);
        return string_Date;
    }

    /**
     * get day in string, exp: monday
     *
     * @return day in string
     */

    public String getString_WeekDayOfDate() {
        string_Date = generalHelper.longDateToWeekday.format(long_Date);
        return string_Date;
    }

    // Getter
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Setter

    /**
     * set the date with long (milliseconds since epoch)
     *
     * @param date date in long (milliseconds since epoch)
     */

    public void setLong_Date(Long date) {
        this.long_Date = date;
    }

    /**
     * set date with int year, month, dayOfMonth
     *
     * @param year year in int
     * @param month month in int
     * @param dayOfMonth dayOfMonth in int
     */

    public void setLong_Date(Integer year, Integer month, Integer dayOfMonth) {
        calendar.set(year, month, dayOfMonth);
        this.long_Date = calendar.getTimeInMillis();
    }

    /**
     * set the date to be today
     *
     */

    public void setLong_DateToday() {
        calendar = Calendar.getInstance();
        this.long_Date = calendar.getTimeInMillis();
    }

    // Setter
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // End

}
