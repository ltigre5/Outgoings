package com.example.leand.outgoingoverview;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// TO USE:
// Change the package (at top) to match your project.
// Search for "TODO", and make the appropriate changes.
public class DBAdapter {

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


    // TODO: Setup your field numbers here (0 = KEY_ROWID, 1=...)
    public static final int COL_VALUE = 1;
    public static final int COL_DATE = 2;
    public static final int COL_DAY = 3;
    public static final int COL_MONTH = 4;
    public static final int COL_YEAR = 5;
    public static final int COL_DESCRIPTION = 6;
    public static final int COL_TITEL = 7;


    public static final String[] ALL_KEYS = new String[]{KEY_ROWID, KEY_VALUE, KEY_DATE, KEY_DAY, KEY_MONTH, KEY_YEAR, KEY_DESCRIPTION, KEY_TITEL};

    // DB info: it's name, and the table we are using (just one).
    public static final String DATABASE_NAME = "MyDb";
    public static final String DATABASE_TABLE = "mainTable";
    // Track DB version if a new version of your app changes the format.
    public static final int DATABASE_VERSION = 5;

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
                    + KEY_TITEL + " string not null "


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
    public long insertRow(long dateInInt, double value, int day, int month, int year, String description, String titel) {
        /*
         * CHANGE 3:
         */
        // TODO: Update data in the row with new fields.
        // TODO: Also change the function's arguments to be what you need!
        // Create row's data:
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_DATE, dateInInt);
        initialValues.put(KEY_VALUE, value);
        initialValues.put(KEY_DAY, day);
        initialValues.put(KEY_MONTH, month);
        initialValues.put(KEY_YEAR, year);
        initialValues.put(KEY_DESCRIPTION, description);
        initialValues.put(KEY_TITEL, titel);

        // Insert it into the database.
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    // Delete a row from the database, by rowId (primary key)
    public boolean deleteRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
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

    public Cursor getRowWithDate(long longRowDate) {
        String where = KEY_DATE + "=" + longRowDate;
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getRowWithMonthYear(int intMonth, int int_Year) {
        String where = KEY_MONTH + "=" + intMonth + " AND " + KEY_YEAR + "=" + int_Year;
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, KEY_DATE, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Change an existing row to be equal to new data.
    public boolean updateRow(long rowId, double value, int date, int day, int month, int year, String description, String titel) {
        String where = KEY_ROWID + "=" + rowId;

        /*
         * CHANGE 4:
         */
        // TODO: Update data in the row with new fields.
        // TODO: Also change the function's arguments to be what you need!
        // Create row's data:
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_VALUE, value);
        newValues.put(KEY_DATE, date);
        newValues.put(KEY_DAY, day);
        newValues.put(KEY_MONTH, month);
        newValues.put(KEY_YEAR, year);
        newValues.put(KEY_DESCRIPTION, description);
        newValues.put(KEY_TITEL, titel);


        // Insert it into the database.
        return db.update(DATABASE_TABLE, newValues, where, null) != 0;
    }

    // Change an existing row to be equal to new data.
    public boolean updateRow(long rowId, double value, String description, String titel) {
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
        newValues.put(KEY_TITEL, titel);


        // Insert it into the database.
        return db.update(DATABASE_TABLE, newValues, where, null) != 0;
    }

    public void updateValue(double newValue, int id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBAdapter.KEY_VALUE, newValue);
        db.update(DBAdapter.DATABASE_TABLE, contentValues, DBAdapter.KEY_ROWID + " = " + id, null);
    }

    public void updateDescription(String newDescriptioon, int id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBAdapter.KEY_DESCRIPTION, newDescriptioon);
        db.update(DBAdapter.DATABASE_TABLE, contentValues, DBAdapter.KEY_ROWID + " = " + id, null);
    }

    public void updateTitel(String newTitel, int id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBAdapter.KEY_TITEL, newTitel);
        db.update(DBAdapter.DATABASE_TABLE, contentValues, DBAdapter.KEY_ROWID + " = " + id, null);
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
