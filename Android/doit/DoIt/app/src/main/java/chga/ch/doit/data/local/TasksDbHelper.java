package chga.ch.doit.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by chsc on 15.04.17.
 *
 * A Helper class which generates the sql-lite database according to our contract
 * {@link TasksPersistenceContract}
 */

public class TasksDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;

    public static final String DATABASE_NAME = "DoIt-DB2.db";



    public TasksDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format(
                "create table %s (%s TEXT PRIMARY KEY,%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s INTEGER,%s INTEGER)",
                TasksPersistenceContract.TaskEntry.TABLE_NAME,
                TasksPersistenceContract.TaskEntry._ID,
                TasksPersistenceContract.TaskEntry.COLUMN_NAME_ENTRY_ID,
                TasksPersistenceContract.TaskEntry.COLUMN_NAME_TITLE,
                TasksPersistenceContract.TaskEntry.COLUMN_NAME_DESCRIPTION,
                TasksPersistenceContract.TaskEntry.COLUMN_NAME_GROUP,
                TasksPersistenceContract.TaskEntry.COLUMN_NAME_COMPLETED,
                TasksPersistenceContract.TaskEntry.COLUMN_NAME_TASK_DATE

        ));
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }

}
