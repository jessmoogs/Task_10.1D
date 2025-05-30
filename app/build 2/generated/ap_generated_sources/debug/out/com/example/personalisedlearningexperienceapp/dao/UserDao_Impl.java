package com.example.improvedpersonalisedlearningexperienceapp.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.improvedpersonalisedlearningexperienceapp.model.User;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class UserDao_Impl implements UserDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<User> __insertionAdapterOfUser;

  private final SharedSQLiteStatement __preparedStmtOfUpdateInterests;

  public UserDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUser = new EntityInsertionAdapter<User>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `users` (`id`,`firstName`,`lastName`,`username`,`email`,`password`,`phone`,`interests`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final User entity) {
        statement.bindLong(1, entity.id);
        if (entity.firstName == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.firstName);
        }
        if (entity.lastName == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.lastName);
        }
        if (entity.username == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.username);
        }
        if (entity.email == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.email);
        }
        if (entity.password == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.password);
        }
        if (entity.phone == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.phone);
        }
        if (entity.interests == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.interests);
        }
      }
    };
    this.__preparedStmtOfUpdateInterests = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE users SET interests = ? WHERE username = ?";
        return _query;
      }
    };
  }

  @Override
  public void insert(final User user) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfUser.insert(user);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateInterests(final String username, final String interests) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateInterests.acquire();
    int _argIndex = 1;
    if (interests == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, interests);
    }
    _argIndex = 2;
    if (username == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, username);
    }
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfUpdateInterests.release(_stmt);
    }
  }

  @Override
  public User loginByUsername(final String username, final String password) {
    final String _sql = "SELECT * FROM users WHERE username = ? AND password = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (username == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, username);
    }
    _argIndex = 2;
    if (password == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, password);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfFirstName = CursorUtil.getColumnIndexOrThrow(_cursor, "firstName");
      final int _cursorIndexOfLastName = CursorUtil.getColumnIndexOrThrow(_cursor, "lastName");
      final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
      final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
      final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
      final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
      final int _cursorIndexOfInterests = CursorUtil.getColumnIndexOrThrow(_cursor, "interests");
      final User _result;
      if (_cursor.moveToFirst()) {
        _result = new User();
        _result.id = _cursor.getInt(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfFirstName)) {
          _result.firstName = null;
        } else {
          _result.firstName = _cursor.getString(_cursorIndexOfFirstName);
        }
        if (_cursor.isNull(_cursorIndexOfLastName)) {
          _result.lastName = null;
        } else {
          _result.lastName = _cursor.getString(_cursorIndexOfLastName);
        }
        if (_cursor.isNull(_cursorIndexOfUsername)) {
          _result.username = null;
        } else {
          _result.username = _cursor.getString(_cursorIndexOfUsername);
        }
        if (_cursor.isNull(_cursorIndexOfEmail)) {
          _result.email = null;
        } else {
          _result.email = _cursor.getString(_cursorIndexOfEmail);
        }
        if (_cursor.isNull(_cursorIndexOfPassword)) {
          _result.password = null;
        } else {
          _result.password = _cursor.getString(_cursorIndexOfPassword);
        }
        if (_cursor.isNull(_cursorIndexOfPhone)) {
          _result.phone = null;
        } else {
          _result.phone = _cursor.getString(_cursorIndexOfPhone);
        }
        if (_cursor.isNull(_cursorIndexOfInterests)) {
          _result.interests = null;
        } else {
          _result.interests = _cursor.getString(_cursorIndexOfInterests);
        }
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public User findByUsername(final String username) {
    final String _sql = "SELECT * FROM users WHERE username = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (username == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, username);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfFirstName = CursorUtil.getColumnIndexOrThrow(_cursor, "firstName");
      final int _cursorIndexOfLastName = CursorUtil.getColumnIndexOrThrow(_cursor, "lastName");
      final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
      final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
      final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
      final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
      final int _cursorIndexOfInterests = CursorUtil.getColumnIndexOrThrow(_cursor, "interests");
      final User _result;
      if (_cursor.moveToFirst()) {
        _result = new User();
        _result.id = _cursor.getInt(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfFirstName)) {
          _result.firstName = null;
        } else {
          _result.firstName = _cursor.getString(_cursorIndexOfFirstName);
        }
        if (_cursor.isNull(_cursorIndexOfLastName)) {
          _result.lastName = null;
        } else {
          _result.lastName = _cursor.getString(_cursorIndexOfLastName);
        }
        if (_cursor.isNull(_cursorIndexOfUsername)) {
          _result.username = null;
        } else {
          _result.username = _cursor.getString(_cursorIndexOfUsername);
        }
        if (_cursor.isNull(_cursorIndexOfEmail)) {
          _result.email = null;
        } else {
          _result.email = _cursor.getString(_cursorIndexOfEmail);
        }
        if (_cursor.isNull(_cursorIndexOfPassword)) {
          _result.password = null;
        } else {
          _result.password = _cursor.getString(_cursorIndexOfPassword);
        }
        if (_cursor.isNull(_cursorIndexOfPhone)) {
          _result.phone = null;
        } else {
          _result.phone = _cursor.getString(_cursorIndexOfPhone);
        }
        if (_cursor.isNull(_cursorIndexOfInterests)) {
          _result.interests = null;
        } else {
          _result.interests = _cursor.getString(_cursorIndexOfInterests);
        }
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
