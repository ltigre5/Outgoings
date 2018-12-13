package com.example.leand.outgoingoverview.Classes;

import java.util.Calendar;

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


    // Deklaration
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Constructor

    //constructor
    public SelectedDate() {
        generalHelper = new GeneralHelper();
        calendar = Calendar.getInstance();
        this.long_Date = calendar.getTimeInMillis();
    }

    public SelectedDate(long long_Date) {
        this();
        this.long_Date = long_Date;
    }

    public SelectedDate(Integer year, Integer month, Integer dayOfMonth) {
        this();
        calendar.set(year, month, dayOfMonth);
        this.long_Date = calendar.getTimeInMillis();
    }

    // Constructor
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Getter

    public Long getLong_Date() {
        return long_Date;
    }

    public String getString_Month() {
        string_Month = generalHelper.longDateToMonthWriteOut.format(long_Date);
        return string_Month;
    }

    public Integer getInteger_Year() {
        integer_Year = Integer.valueOf(generalHelper.longDateToYearNumber.format(long_Date));
        return integer_Year;
    }

    public Integer getInteger_Month() {
        integer_Month = Integer.valueOf(generalHelper.longDateToMonthNumber.format(long_Date));
        return integer_Month;
    }

    public Integer getInteger_day() {
        integer_day = Integer.valueOf(generalHelper.longDateToDayNumber.format(long_Date));
        return integer_day;
    }


    public Integer getInteger_DateWithoutTime() {
        integer_DateWithoutTime = Integer.parseInt(generalHelper.longDateToDateWithoutTime.format(long_Date));
        return integer_DateWithoutTime;
    }

    public String getString_Date() {
        string_Date = generalHelper.longDateToDDMMYYYY.format(long_Date);
        return string_Date;
    }

    public String getString_WeekDayOfDate() {
        string_Date = generalHelper.longDateToWeekday.format(long_Date);
        return string_Date;
    }

    // Getter
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Setter

    public void setLong_Date(Long long_Date) {
        this.long_Date = long_Date;
    }

    public void setLong_Date(Integer year, Integer month, Integer dayOfMonth) {
        calendar.set(year, month, dayOfMonth);
        this.long_Date = calendar.getTimeInMillis();
    }

    // Setter
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // End

}
