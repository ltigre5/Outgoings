package com.example.leand.outgoingoverview;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SelectedDate {
    private Integer integer_Year;
    private Integer integer_Month;
    private Integer integer_day;
    private String string_Date;
    private Long long_Date;
    private Date date_Date;

    // Deklaration
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Formatter

    //Date formatter
    private SimpleDateFormat sdf_DateInNumbers = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat sdf_Day = new SimpleDateFormat("dd");
    private SimpleDateFormat sdf_Month = new SimpleDateFormat("MM");
    private SimpleDateFormat sdf_Year = new SimpleDateFormat("yyyy");

    // Formatter
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Constructor

    //constructor
    public SelectedDate(long long_Date) {
        this.long_Date = long_Date;
    }

    public SelectedDate(Date date_Date) {
        this(date_Date.getTime());
    }

    // Constructor
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Getter and Setter

    public Long getLong_Date() {
        return long_Date;
    }

    public void setLong_Date(Long long_Date) {
        this.long_Date = long_Date;
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

    public String getString_Date() {
        string_Date = sdf_DateInNumbers.format(long_Date);
        return string_Date;
    }

    public void setDate_Date(Date date_Date) {
        long_Date=date_Date.getTime();
    }

    // Getter and Setter
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // OnCreate


}
