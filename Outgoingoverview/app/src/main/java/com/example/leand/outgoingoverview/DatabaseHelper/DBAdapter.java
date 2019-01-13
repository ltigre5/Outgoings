package com.example.leand.outgoingoverview.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.leand.outgoingoverview.GeneralHelperClasses.SelectedDate;

// TO USE:
// Change the package (at top) to match your project.
// Search for "TODO", and make the appropriate changes.
public class DBAdapter {

    //-----------------------------------------------------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //First Table Start

    public static final String ASCENDING = " DESC";
    public static final String DESCENDING = " ASC";
    public static final String SEARCH_REPEATED = "= 'REPEATED'";
    public static final String SEARCH_SINGLE = "= 'SINGLE'";
    public static final String REPEATED = "REPEATED";
    public static final String SINGLE = "SINGLE";

    SelectedDate selectedDate = new SelectedDate();
    SelectedDate selectedStartDate = new SelectedDate();
    SelectedDate selectedEndDate = new SelectedDate();

    /////////////////////////////////////////////////////////////////////
    //	Constants & Data
    /////////////////////////////////////////////////////////////////////
    // For logging:
    private static final String TAG = "DBAdapter";

    // DB Fields
    public static final String KEY_ROWID = "_id";
    public static final int COL_ROWID = 0;
    /*
     * CHANGE 1:
     */
    // TODO: Setup your fields here:
    public static final String KEY_TITLE = "title";
    public static final String KEY_TITLE_COLOR = "titleColor";
    public static final String KEY_VALUE = "value";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_DATE = "dateInInt";
    public static final String KEY_DATE_WITHOUT_TIME = "dateWithoutTime";
    public static final String KEY_DAY = "dayInInt";
    public static final String KEY_MONTH = "monthInInt";
    public static final String KEY_YEAR = "yearInInt";

    public static final String KEY_REPEATED_BY_TIMES = "repeatedBy";
    public static final String KEY_EVERY = "every";

    public static final String KEY_END_DATE = "endDateLong";
    public static final String KEY_END_DATE_WITHOUT_TIME = "endDateWithoutTime";

    public static final String KEY_START_DATE = "startDateLong";
    public static final String KEY_START_DATE_WITHOUT_TIME = "startDateWithoutTime";

    public static final String KEY_SINGLE_OR_REPEATED = "singleOrRepeated";


    // TODO: Setup your field numbers here (0 = KEY_ROWID, 1=...)
    public static final int COL_TITLE = 1;
    public static final int COL_TITLE_COLOR = 2;
    public static final int COL_VALUE = 3;
    public static final int COL_DESCRIPTION = 4;
    public static final int COL_DATE = 5;
    public static final int COL_DATE_WITHOUT_TIME = 6;
    public static final int COL_DAY = 7;
    public static final int COL_MONTH = 8;
    public static final int COL_YEAR = 9;

    public static final int COL_REPEATED_BY_TIMES = 10;
    public static final int COL_EVERY = 11;

    public static final int COL_START_DATE = 12;
    public static final int COL_START_DATE_WITHOUT_TIME = 13;

    public static final int COL_END_DATE = 14;
    public static final int COL_END_DATE_WITHOUT_TIME = 15;

    public static final int COL_SINGLE_OR_REPEATED = 16;


    public static final String[] ALL_KEYS = new String[]{KEY_ROWID, KEY_VALUE, KEY_DATE, KEY_DAY, KEY_MONTH, KEY_YEAR, KEY_DESCRIPTION, KEY_TITLE, KEY_DATE_WITHOUT_TIME, KEY_END_DATE, KEY_EVERY, KEY_START_DATE, KEY_END_DATE_WITHOUT_TIME, KEY_START_DATE_WITHOUT_TIME, KEY_TITLE_COLOR,KEY_SINGLE_OR_REPEATED, KEY_REPEATED_BY_TIMES};

    // DB info: it's name, and the table we are using (just one).
    public static final String DATABASE_NAME = "MyDb";
    public static final String DATABASE_TABLE = "VariableItems";
    // Track DB version if a new version of your app changes the format.
    public static final int DATABASE_VERSION = 17;

    private static final String DATABASE_CREATE_SQL =
            "create table " + DATABASE_TABLE
                    + " (" + KEY_ROWID + " integer primary key autoincrement, "

                    /*
                     * CHANGE 2:
                     */
                    // TODO: Place your fields here!
                    // + KEY_{...} + " {type} not null"
                    //	- Key is the column name you created above.
                    //	- {type} is one of: text, integer, real, blob
                    //		(http://www.sqlite.org/datatype3.html)
                    //  - "not null" means it is a required field (must be given a value).
                    // NOTE: All must be comma separated (end of line!) Last one must have NO comma!!
                    + KEY_TITLE + " string not null, "
                    + KEY_TITLE_COLOR + " integer ,"
                    + KEY_VALUE + " double not null, "
                    + KEY_DESCRIPTION + " string, "
                    + KEY_DATE + " integer not null, "
                    + KEY_DATE_WITHOUT_TIME + " integer not null, "
                    + KEY_DAY + " integer not null, "
                    + KEY_MONTH + " integer not null, "
                    + KEY_YEAR + " integer not null, "

                    + KEY_REPEATED_BY_TIMES + " integer ,"
                    + KEY_EVERY + " integer, "

                    + KEY_START_DATE + " integer ,"
                    + KEY_START_DATE_WITHOUT_TIME + " integer, "

                    + KEY_END_DATE + " integer ,"
                    + KEY_END_DATE_WITHOUT_TIME + " integer, "

                    + KEY_SINGLE_OR_REPEATED + " string not null "

                    // Rest  of creation:
                    + ");";

    // Context of application who uses us.
    private final Context context;

    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;

    // definitions
    //-----------------------------------------------------------------------------------------------------------------------------------------
    // Initialize, open, close Database


    public DBAdapter(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }

    // Open the database connection.
    public void open() {
        db = myDBHelper.getWritableDatabase();
    }

    // Close the database connection.
    public void close() {
        myDBHelper.close();
    }

    // Initialize, open, close Database
    //-----------------------------------------------------------------------------------------------------------------------------------------
    // Insert Rows

    // Add a new set of values to the database.
    public void insertRow(String title, int titleColour, double value, String description, long dateInLong) {
        /*
         * CHANGE 3:
         */
        // TODO: Update data in the row with new fields.
        // TODO: Also change the function's arguments to be what you need!
        // Create row's data:

        selectedDate.setLong_Date(dateInLong);
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_DATE, dateInLong);
        initialValues.put(KEY_VALUE, value);
        initialValues.put(KEY_DAY, selectedDate.getInteger_day());
        initialValues.put(KEY_MONTH, selectedDate.getInteger_Month());
        initialValues.put(KEY_YEAR, selectedDate.getInteger_Year());
        initialValues.put(KEY_DESCRIPTION, description);
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_DATE_WITHOUT_TIME, selectedDate.getInteger_DateWithoutTime());
        initialValues.put(KEY_TITLE_COLOR, titleColour);
        initialValues.put(KEY_SINGLE_OR_REPEATED, SINGLE);


        // Insert it into the database.
        db.insert(DATABASE_TABLE, null, initialValues);
    }


    // Add a new sets of Repeated Item to the database.
    public void insertRowRepeatedItem(String title, int titleColour, double value, String description, long dateInLong, long startDate, long endDate, int repeatedByTimes, int every) {
        /*
         * CHANGE 3:
         */
        // TODO: Update data in the row with new fields.
        // TODO: Also change the function's arguments to be what you need!
        // Create row's data:

        selectedDate.setLong_Date(dateInLong);
        selectedStartDate.setLong_Date(startDate);
        selectedEndDate.setLong_Date(endDate);

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_DATE, dateInLong);
        initialValues.put(KEY_VALUE, value);
        initialValues.put(KEY_DAY, selectedDate.getInteger_day());
        initialValues.put(KEY_MONTH, selectedDate.getInteger_Month());
        initialValues.put(KEY_YEAR, selectedDate.getInteger_Year());
        initialValues.put(KEY_DESCRIPTION, description);
        initialValues.put(KEY_DATE_WITHOUT_TIME, selectedDate.getInteger_DateWithoutTime());
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_END_DATE, endDate);
        initialValues.put(KEY_EVERY, every);
        initialValues.put(KEY_START_DATE, startDate);
        initialValues.put(KEY_END_DATE_WITHOUT_TIME, selectedEndDate.getInteger_DateWithoutTime());
        initialValues.put(KEY_START_DATE_WITHOUT_TIME, selectedStartDate.getInteger_DateWithoutTime());
        initialValues.put(KEY_TITLE_COLOR, titleColour);
        initialValues.put(KEY_SINGLE_OR_REPEATED, REPEATED);
        initialValues.put(KEY_REPEATED_BY_TIMES, repeatedByTimes);

        Log.d("Repeated Start", " ");
        Log.d("Saved Values: ", "Title" + " = " + title);
        Log.d("Saved Values: ", "Value" + " = " + value);
        Log.d("Saved Values: ", "Date" + " = " + selectedDate.getString_Date());
        Log.d("Saved Values: ", "Start Date" + " = " + selectedStartDate.getString_Date());
        Log.d("Saved Values: ", "End Date" + " = " + selectedEndDate.getString_Date());
        Log.d("Repeated End", " ");

        // Insert it into the database.
        db.insert(DATABASE_TABLE, null, initialValues);
    }

    // Insert Rows
    //-----------------------------------------------------------------------------------------------------------------------------------------
    // delete Rows

    // Delete a row from the database, by rowId (primary key)
    public void deleteRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        db.delete(DATABASE_TABLE, where, null);
    }

    // Delete rows from the database, by repeated title
    public void deleteRowWithRepeatedItem(String title, double value, int startDateWithoutTime, int endDateWithoutTime, int repeatedByTimes, int every) {
        String where = KEY_TITLE + "='" + title + "' AND " + KEY_VALUE + "=" + value + " AND " + KEY_START_DATE_WITHOUT_TIME + "=" + startDateWithoutTime + " AND " + KEY_END_DATE_WITHOUT_TIME + "=" + endDateWithoutTime + " AND " + KEY_REPEATED_BY_TIMES + "=" + repeatedByTimes + " AND " + KEY_EVERY + "=" + every;
        db.delete(DATABASE_TABLE, where, null);
    }

    public void deleteAll() {
        Cursor c = getAllRows();
        long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
        if (c.moveToFirst()) {
            do {
                deleteRow(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
        c.close();
    }

    // delete Rows
    //-----------------------------------------------------------------------------------------------------------------------------------------
    // Check Methods

    // Checks if Repeated Item already exists
    public boolean checkRowRepeatedItem(String title, double value, int startDayWithoutTime, int endDayWithoutTime, int repeatedByTimes, int every) {
        String where = KEY_TITLE + "='" + title + "' AND " + KEY_VALUE + "=" + value + " AND " + KEY_START_DATE_WITHOUT_TIME + "=" + startDayWithoutTime + " AND " + KEY_END_DATE_WITHOUT_TIME + "=" + endDayWithoutTime + " AND " + KEY_REPEATED_BY_TIMES + "=" + repeatedByTimes + " AND " + KEY_EVERY + "=" + every;
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        boolean exists;
        exists = c.moveToFirst();
        c.close();
        return exists;
    }

    // Checks if Item already exists
    public boolean checkRowItem(String title, double value, int dateWithoutTime) {
        String where = KEY_DATE_WITHOUT_TIME + "=" + dateWithoutTime + " AND " + KEY_TITLE + "='" + title + "' AND " + KEY_VALUE + "=" + value;
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        boolean exists;
        exists = c.moveToFirst();
        c.close();
        return exists;
    }

    // Check Methods
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //getRows Methods

    // Return all data in the database.
    public Cursor getAllRows() {
        String orderBy = KEY_DATE + DESCENDING;
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS,
                null, null, null, null, orderBy, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Get a specific row (by rowId)
    public Cursor getRowWithID(long longRowId) {
        String where = KEY_ROWID + "=" + longRowId;
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Get row with Repeated Title
    public Cursor getAllRowsRepeatedNotDuplicated() {
        String where = KEY_SINGLE_OR_REPEATED + SEARCH_REPEATED;
        String groupBy = KEY_TITLE + ", " + KEY_VALUE + ", " + KEY_START_DATE_WITHOUT_TIME + ", " + KEY_END_DATE_WITHOUT_TIME + ", " + KEY_REPEATED_BY_TIMES + ", " + KEY_EVERY;
        String orderBy = KEY_START_DATE + ASCENDING;
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, groupBy, null, orderBy, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getRowWithDate(int dateWithoutTime) {
        String where = KEY_DATE_WITHOUT_TIME + "=" + dateWithoutTime;
        String orderBy = KEY_VALUE + ASCENDING;
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, orderBy, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getRowWithYear(int int_Year, String string_OrderBy, String string_AscendingDescending, String hide) {
        String where;
        if (hide!=null){
            where = KEY_SINGLE_OR_REPEATED + hide + " AND " + KEY_YEAR + "=" + int_Year;
        } else  {
            where = KEY_YEAR + "=" + int_Year;
        }
        String orderBy = string_OrderBy + string_AscendingDescending;
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, orderBy, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getRowWithYear(int int_Year, String hide) {
        return getRowWithYear(int_Year, DBAdapter.KEY_DATE, ASCENDING, hide);
    }

    public Cursor getRowWithMonthYear(int intMonth, int int_Year, String string_OrderBy, String string_AscendingDescending, String hide) {
        String where;
        if (hide!=null){
            where = KEY_SINGLE_OR_REPEATED + hide + " AND " + KEY_MONTH + "=" + intMonth + " AND " + KEY_YEAR + "=" + int_Year;
        } else  {
            where = KEY_MONTH + "=" + intMonth + " AND " + KEY_YEAR + "=" + int_Year;
        }
        String orderBy = string_OrderBy + string_AscendingDescending;
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, orderBy, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getRowWithMonthYear(int intMonth, int int_Year, String hide) {
        return getRowWithMonthYear(intMonth, int_Year, DBAdapter.KEY_DATE, ASCENDING, hide);
    }

    public Cursor getRowWithStartEndDay(int startDateWithoutTime, int endDateWithoutTime, String string_OrderBy, String string_AscendingDescending, String hide) {
        String where;
        if (hide!=null){
            where = KEY_SINGLE_OR_REPEATED + hide + " AND " + KEY_DATE_WITHOUT_TIME + " BETWEEN " + startDateWithoutTime + " AND " + endDateWithoutTime;
        } else  {
            where = KEY_DATE_WITHOUT_TIME + " BETWEEN " + startDateWithoutTime + " AND " + endDateWithoutTime;
        }
        String orderBy = string_OrderBy + string_AscendingDescending;
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, orderBy, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    //getRows Methods
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //Update Methods

    // Change an existing row to be equal to new data.
    public void updateRow(long rowId, String title, int titleColor, double value, String description) {
        String where = KEY_ROWID + "=" + rowId;

        /*
         * CHANGE 4:
         */
        // TODO: Update data in the row with new fields.
        // TODO: Also change the function's arguments to be what you need!
        // Create row's data:
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_VALUE, value);
        newValues.put(KEY_DESCRIPTION, description);
        newValues.put(KEY_TITLE, title);
        newValues.put(KEY_TITLE_COLOR, titleColor);


        // Insert it into the database.
        db.update(DATABASE_TABLE, newValues, where, null);
    }


    // First Table End
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------------------------------------------------------
    // Second Table Start


    // TODO: Setup your fields here:
    public static final String KEY_CURRENCY = "currency";


    // TODO: Setup your field numbers here (0 = KEY_ROWID, 1=...)
    public static final int COL_Currency = 1;

    public static final String[] ALL_KEYS2 = new String[]{KEY_ROWID, KEY_CURRENCY};

    // DB info: it's name, and the table we are using (just one).

    public static final String DATABASE_TABLE2 = "StaticItems";
    // Track DB version if a new version of your app changes the format.

    private static final String DATABASE_CREATE_SQL2 =
            "create table " + DATABASE_TABLE2
                    + " (" + KEY_ROWID + " integer primary key autoincrement, "

                    /*
                     * CHANGE 2:
                     */
                    // TODO: Place your fields here!
                    // + KEY_{...} + " {type} not null"
                    //	- Key is the column name you created above.
                    //	- {type} is one of: text, integer, real, blob
                    //		(http://www.sqlite.org/datatype3.html)
                    //  - "not null" means it is a required field (must be given a value).
                    // NOTE: All must be comma separated (end of line!) Last one must have NO comma!!

                    + KEY_CURRENCY + " string not null "


                    // Rest  of creation:
                    + ");";


    // definitions
    //-----------------------------------------------------------------------------------------------------------------------------------------
    // Insert Rows

    // Add a new set of values to the database.
    public void insertCurrency(String currency) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_CURRENCY, currency);

        // Insert it into the database.
        db.insert(DATABASE_TABLE2, null, initialValues);
    }

    // Insert Rows
    //-----------------------------------------------------------------------------------------------------------------------------------------
    // check Methods

    // Checks if Item already exists
    public boolean checkCurrencyExists() {
        boolean exists;
        Cursor c = db.query(true, DATABASE_TABLE2, ALL_KEYS2,
                null, null, null, null, null, null);

        exists=c.moveToFirst();
        c.close();
        return exists;
    }

    // check Methods
    //-----------------------------------------------------------------------------------------------------------------------------------------
    // getRows Methods

    // Return all data in the database.
    public Cursor getCurrency() {
        Cursor c = db.query(true, DATABASE_TABLE2, ALL_KEYS2,
                null, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // getRows Methods
    //-----------------------------------------------------------------------------------------------------------------------------------------
    // Update Currency

    // Change an existing row to be equal to new data.
    public void updateCurrency(String Currency) {

        ContentValues newValues = new ContentValues();
        newValues.put(KEY_CURRENCY, Currency);

        // Insert it into the database.
        db.update(DATABASE_TABLE2, newValues, null, null);
    }

    // Second Table End
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------------------------------------------------------
    // Create Database

    /**
     * Private class which handles database creation and upgrading.
     * Used to handle low-level database access.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(DATABASE_CREATE_SQL);
            _db.execSQL(DATABASE_CREATE_SQL2);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            _db.execSQL("DROP TABLE IF EXISTS " +DATABASE_TABLE2);

            // Recreate new database:
            onCreate(_db);
        }
    }
}
