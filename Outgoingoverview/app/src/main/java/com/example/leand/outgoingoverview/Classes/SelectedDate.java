package com.example.leand.outgoingoverview.Classes;

import java.text.SimpleDateFormat;
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


    // Deklaration
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Formatter

    //Date formatter
    private SimpleDateFormat sdf_DateInNumbers = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat sdf_DateWithoutTime = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat sdf_Day = new SimpleDateFormat("dd");
    private SimpleDateFormat sdf_Month = new SimpleDateFormat("MM");
    private SimpleDateFormat sdf_MonthWriteOut = new SimpleDateFormat("MMMM");
    private SimpleDateFormat sdf_Year = new SimpleDateFormat("yyyy");
    private SimpleDateFormat sdf_WeekdayOfDate = new SimpleDateFormat("EEEE");



    // Formatter
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Constructor

    //constructor
    public SelectedDate() {
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
        string_Month = sdf_MonthWriteOut.format(long_Date);
        return string_Month;
    }

    public Integer getInteger_Year() {
        integer_Year = Integer.valueOf(sdf_Year.format(long_Date));
        return integer_Year;
    }

    public Integer getInteger_Month() {
        integer_Month = Integer.valueOf(sdf_Month.format(long_Date));
        return integer_Month;
    }

    public Integer getInteger_day() {
        integer_day = Integer.valueOf(sdf_Day.format(long_Date));
        return integer_day;
    }


    public Integer getInteger_DateWithoutTime() {
        integer_DateWithoutTime = Integer.parseInt(sdf_DateWithoutTime.format(long_Date));
        return integer_DateWithoutTime;
    }

    public String getString_Date() {
        string_Date = sdf_DateInNumbers.format(long_Date);
        return string_Date;
    }

    public String getString_WeekDayOfDate() {
        string_Date = sdf_WeekdayOfDate.format(long_Date);
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
    // OnCreate


}
