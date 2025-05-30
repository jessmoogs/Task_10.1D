package com.example.improvedpersonalisedlearningexperienceapp.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.improvedpersonalisedlearningexperienceapp.model.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insert(User user);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    User loginByUsername(String username, String password);

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    User findByUsername(String username);

    @Query("UPDATE users SET interests = :interests WHERE username = :username")
    void updateInterests(String username, String interests);

    @Query("UPDATE users SET totalQuestions = totalQuestions + :total, correctAnswers = correctAnswers + :correct WHERE username = :username")
    void updateQuizStats(String username, int total, int correct);

    @Query("UPDATE users SET totalQuestions = 0, correctAnswers = 0 WHERE username = :username")
    void resetQuizStats(String username);

    @Query("UPDATE users SET plan = :plan WHERE username = :username")
    void updatePlan(String username, String plan);

    @Query("SELECT username FROM users")
    List<String> getAllUsernames();


}





