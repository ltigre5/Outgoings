package com.example.leand.outgoingoverview.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.leand.outgoingoverview.Classes.SelectedDate;

// TO USE:
// Change the package (at top) to match your project.
// Search for "TODO", and make the appropriate changes.
public class DBAdapter {
    public static final String ASCENDING = " ASC";
    public static final String DESCENDING = " DESC";

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
    public static final String KEY_VALUE = "value";
    public static final String KEY_DATE = "dateInInt";
    public static final String KEY_DAY = "dayInInt";
    public static final String KEY_MONTH = "monthInInt";
    public static final String KEY_YEAR = "yearInInt";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_TITEL = "titel";
    public static final String KEY_CURRENCY = "currency";
    public static final String KEY_DATE_WITHOUT_TIME = "dateWithoutTime";
    public static final String KEY_TITLE_REPEATED = "titleRepeated";
    public static final String KEY_END_DATE = "endDateLong";
    public static final String KEY_END_DATE_WITHOUT_TIME = "endDateWithoutTime";
    public static final String KEY_EVERY = "every";
    public static final String KEY_START_DATE = "startDateLong";
    public static final String KEY_START_DATE_WITHOUT_TIME = "startDateWithoutTime";
    public static final String KEY_TITLE_COLOR = "titleColor";


    // TODO: Setup your field numbers here (0 = KEY_ROWID, 1=...)
    public static final int COL_VALUE = 1;
    public static final int COL_DATE = 2;
    public static final int COL_DAY = 3;
    public static final int COL_MONTH = 4;
    public static final int COL_YEAR = 5;
    public static final int COL_DESCRIPTION = 6;
    public static final int COL_TITEL = 7;
    public static final int COL_Currency = 8;
    public static final int COL_DATE_WITHOUT_TIME = 9;
    public static final int COL_TITLE_REPEATED = 10;
    public static final int COL_END_DATE = 11;
    public static final int COL_EVERY = 12;
    public static final int COL_START_DATE = 13;
    public static final int COL_END_DATE_WITHOUT_TIME = 14;
    public static final int COL_START_DATE_WITHOUT_TIME = 15;
    public static final int COL_TITLE_COLOR = 16;


    public static final String[] ALL_KEYS = new String[]{KEY_ROWID, KEY_VALUE, KEY_DATE, KEY_DAY, KEY_MONTH, KEY_YEAR, KEY_DESCRIPTION, KEY_TITEL, KEY_CURRENCY, KEY_DATE_WITHOUT_TIME, KEY_TITLE_REPEATED, KEY_END_DATE, KEY_EVERY, KEY_START_DATE, KEY_END_DATE_WITHOUT_TIME, KEY_START_DATE_WITHOUT_TIME, KEY_TITLE_COLOR};

    // DB info: it's name, and the table we are using (just one).
    public static final String DATABASE_NAME = "MyDb";
    public static final String DATABASE_TABLE = "mainTable";
    // Track DB version if a new version of your app changes the format.
    public static final int DATABASE_VERSION = 13;

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
                    + KEY_DATE + " integer not null, "
                    + KEY_VALUE + " double not null, "
                    + KEY_DAY + " integer not null, "
                    + KEY_MONTH + " integer not null, "
                    + KEY_YEAR + " integer not null, "
                    + KEY_DESCRIPTION + " string, "
                    + KEY_TITEL + " string not null, "
                    + KEY_CURRENCY + " string not null, "
                    + KEY_DATE_WITHOUT_TIME + " integer not null, "
                    + KEY_TITLE_REPEATED + " string, "
                    + KEY_END_DATE + " integer ,"
                    + KEY_EVERY + " string, "
                    + KEY_START_DATE + " integer ,"
                    + KEY_START_DATE_WITHOUT_TIME + " integer, "
                    + KEY_END_DATE_WITHOUT_TIME + " integer, "
                    + KEY_TITLE_COLOR + " integer "

                    // Rest  of creation:
                    + ");";

    // Context of application who uses us.
    private final Context context;

    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;

    /////////////////////////////////////////////////////////////////////
    //	Public methods:
    /////////////////////////////////////////////////////////////////////

    public DBAdapter(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }

    // Open the database connection.
    public DBAdapter open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    // Close the database connection.
    public void close() {
        myDBHelper.close();
    }

    // Add a new set of values to the database.
    public long insertRow(long dateInLong, double value, String description, String title, String currency, int titleColour) {
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
        initialValues.put(KEY_TITEL, title);
        initialValues.put(KEY_CURRENCY, currency);
        initialValues.put(KEY_DATE_WITHOUT_TIME, selectedDate.getInteger_DateWithoutTime());
        initialValues.put(KEY_TITLE_COLOR, titleColour);


        // Insert it into the database.
        return db.insert(DATABASE_TABLE, null, initialValues);
    }


    // Add a new sets of Repeated Item to the database.
    public long insertRowRepeatedItem(long dateInLong, double value, String description, String titleRepeated, String currency, long endDate, String every, long startDate, int titleColour) {
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
        initialValues.put(KEY_TITLE_REPEATED, titleRepeated);
        initialValues.put(KEY_CURRENCY, currency);
        initialValues.put(KEY_DATE_WITHOUT_TIME, selectedDate.getInteger_DateWithoutTime());
        initialValues.put(KEY_TITEL, titleRepeated);
        initialValues.put(KEY_END_DATE, endDate);
        initialValues.put(KEY_EVERY, every);
        initialValues.put(KEY_START_DATE, startDate);
        initialValues.put(KEY_END_DATE_WITHOUT_TIME, selectedEndDate.getInteger_DateWithoutTime());
        initialValues.put(KEY_START_DATE_WITHOUT_TIME, selectedStartDate.getInteger_DateWithoutTime());
        initialValues.put(KEY_TITLE_COLOR, titleColour);


        // Insert it into the database.
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    // Delete a row from the database, by rowId (primary key)
    public boolean deleteRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        return db.delete(DATABASE_TABLE, where, null) != 0;
    }

    // Delete rows from the database, by repeated title
    public boolean deleteRowWithRepeatedItem(String title, int startDateWithoutTime, int endDateWithoutTime, String description, double value) {
        String where = KEY_TITLE_REPEATED + "='" + title + "' AND " + KEY_START_DATE_WITHOUT_TIME + "=" + startDateWithoutTime + " AND " + KEY_END_DATE_WITHOUT_TIME + "=" + endDateWithoutTime + " AND " + KEY_DESCRIPTION + "='" + description + "' AND " + KEY_VALUE + "=" + value;
        return db.delete(DATABASE_TABLE, where, null) != 0;
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

    //-----------------------------------------------------------------------------------------------------------------------------------------
    //getRows Methods

    // Return all data in the database.
    public Cursor getAllRows() {
        String where = null;
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
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

    // Checks if Repeated Item already exists
    public boolean checkRowRepeatedItem(double value, String titleRepeated, int endDayWithoutTime, int startDayWithoutTime) {
        String where = KEY_START_DATE_WITHOUT_TIME + "=" + startDayWithoutTime + " AND " + KEY_END_DATE_WITHOUT_TIME + "=" + endDayWithoutTime + " AND " + KEY_TITLE_REPEATED + "='" + titleRepeated + "' AND " + KEY_VALUE + "=" + value;
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        boolean exists;
        if (c.moveToFirst()) {
            exists = true;
        } else {
            exists = false;
        }
        c.close();
        return exists;
    }

    // Checks if Item already exists
    public boolean checkRowItem(double value, String title, int dateWithoutTime) {
        String where = KEY_DATE_WITHOUT_TIME + "=" + dateWithoutTime + " AND " + KEY_TITEL + "='" + title + "' AND " + KEY_VALUE + "=" + value;
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        boolean exists;
        if (c.moveToFirst()) {
            exists = true;
        } else {
            exists = false;
        }
        c.close();
        return exists;
    }


    // Get row with Repeated Title
    public Cursor getAllRowsRepeatedItemNotDuplicated() {
        String where = KEY_END_DATE + " IS NOT NULL";
        String groupBy = KEY_START_DATE_WITHOUT_TIME + ", " + KEY_END_DATE_WITHOUT_TIME + ", " + KEY_EVERY + ", " + KEY_TITLE_REPEATED + ", " + KEY_DESCRIPTION + ", " + KEY_VALUE;
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, groupBy, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getRowWithDate(int dateWithoutTime) {
        String where = KEY_DATE_WITHOUT_TIME + "=" + dateWithoutTime;
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getRowWithYear(int int_Year, String string_OrderBy, String string_AscendingDescending) {
        String where = KEY_YEAR + "=" + int_Year;
        String orderBy = string_OrderBy + string_AscendingDescending;
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, orderBy, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getRowWithMonthYear(int intMonth, int int_Year, String string_OrderBy, String string_AscendingDescending) {
        String where = KEY_MONTH + "=" + intMonth + " AND " + KEY_YEAR + "=" + int_Year;
        String orderBy = string_OrderBy + string_AscendingDescending;
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, orderBy, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getRowWithMonthYear(int intMonth, int int_Year) {
        return getRowWithMonthYear(intMonth, int_Year, DBAdapter.KEY_DATE, ASCENDING);
    }

    public Cursor getRowWithStartEndDay(int startDateWithoutTime, int endDateWithoutTime, String string_OrderBy, String string_AscendingDescending) {
        String where = KEY_DATE_WITHOUT_TIME + " BETWEEN " + startDateWithoutTime + " AND " + endDateWithoutTime;
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

    // Change an existing row to be equal to new data.
    public boolean updateRow(long rowId, double value, String description, String title, int titleColor) {
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
        newValues.put(KEY_TITEL, title);
        newValues.put(KEY_TITLE_COLOR, titleColor);


        // Insert it into the database.
        return db.update(DATABASE_TABLE, newValues, where, null) != 0;
    }


    public void updateCurrency(String newCurrency) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBAdapter.KEY_CURRENCY, newCurrency);
        db.update(DBAdapter.DATABASE_TABLE, contentValues, null, null);
    }


    /////////////////////////////////////////////////////////////////////
    //	Private Helper Classes:
    /////////////////////////////////////////////////////////////////////

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
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);

            // Recreate new database:
            onCreate(_db);
        }
    }
}
