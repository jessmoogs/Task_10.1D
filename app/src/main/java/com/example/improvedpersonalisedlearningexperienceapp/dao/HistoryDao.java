package com.example.improvedpersonalisedlearningexperienceapp.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.improvedpersonalisedlearningexperienceapp.model.HistoryEntry;

import java.util.List;

@Dao
public interface HistoryDao {

    @Insert
    void insert(HistoryEntry entry);

    @Query("SELECT * FROM history WHERE username = :username ORDER BY timestamp DESC")
    List<HistoryEntry> getHistoryForUser(String username);

    @Query("DELETE FROM history WHERE username = :username")
    void deleteAllForUser(String username);
}
