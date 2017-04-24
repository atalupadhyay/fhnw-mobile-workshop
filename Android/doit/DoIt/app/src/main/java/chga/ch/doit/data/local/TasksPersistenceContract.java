package chga.ch.doit.data.local;

/**
 * Created by chsc on 15.04.17.
 */

import android.database.Cursor;
import android.provider.BaseColumns;

import java.util.Date;

import chga.ch.doit.model.Task;

/**
 * The contract used for the db to save the tasks locally.
 */
public final class TasksPersistenceContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private TasksPersistenceContract() {
    }

    /* Inner class that defines the table contents */
    public static abstract class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_COMPLETED = "completed";
        public static final String COLUMN_NAME_TASK_DATE = "taskdate";
        public static final String COLUMN_NAME_GROUP = "taskgroup";
    }

    public static Task parseCursor(Cursor cursor) {
        String itemId = cursor.getString(cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_ENTRY_ID));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_TITLE));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_DESCRIPTION));
        boolean completed =
                cursor.getInt(cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_COMPLETED)) == 1;
        Date date = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_TASK_DATE)));
        String group = cursor.getString(cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_GROUP));

        return new Task(itemId,title,description,group,date,completed);

    }
}