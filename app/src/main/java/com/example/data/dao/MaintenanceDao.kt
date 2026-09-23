package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.MaintenanceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MaintenanceDao {
    @Query("SELECT * FROM maintenance ORDER BY id DESC")
    fun getAllMaintenance(): Flow<List<MaintenanceEntity>>

    @Query("SELECT * FROM maintenance WHERE vehicleId = :vehicleId")
    fun getMaintenanceForVehicle(vehicleId: Long): Flow<List<MaintenanceEntity>>

    @Query("SELECT * FROM maintenance WHERE status != 'Completed'")
    suspend fun getActiveMaintenance(): List<MaintenanceEntity>

    @Query("SELECT COUNT(*) FROM maintenance")
    suspend fun getMaintenanceCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMaintenance(m: MaintenanceEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<MaintenanceEntity>)

    @Update
    suspend fun updateMaintenance(m: MaintenanceEntity)

    @Delete
    suspend fun deleteMaintenance(m: MaintenanceEntity)
}
