package com.devhind.qibla.refg.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.devhind.qibla.refg.model.Alert;

import java.util.List;

@Dao
public interface OnDataBaseAction {

    @Query("SELECT * FROM Alert")
    List<Alert> getAllTasksList();

    @Query("DELETE FROM Alert")
    void truncateTheList();

    @Insert
    void insertDataIntoTaskList(Alert task);

    @Query("DELETE FROM Alert WHERE taskId = :taskId")
    void deleteTaskFromId(int taskId);

    @Query("SELECT * FROM Alert WHERE taskId = :taskId")
    Alert selectDataFromAnId(int taskId);

    @Query("UPDATE Alert SET  date = :taskDate, " +
            "lastAlarm = :taskTime, event = :taskEvent WHERE taskId = :taskId")
    void updateAnExistingRow(int taskId,   String taskDate, String taskTime,
                            String taskEvent);

}
