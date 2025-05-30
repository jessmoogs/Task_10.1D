package com.example.improvedpersonalisedlearningexperienceapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.improvedpersonalisedlearningexperienceapp.dao.UserDao;
import com.example.improvedpersonalisedlearningexperienceapp.dao.HistoryDao;
import com.example.improvedpersonalisedlearningexperienceapp.model.User;
import com.example.improvedpersonalisedlearningexperienceapp.model.HistoryEntry;

@Database(entities = {User.class, HistoryEntry.class}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract HistoryDao historyDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "user_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}


