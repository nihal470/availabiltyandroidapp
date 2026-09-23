package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.BookingDao
import com.example.data.dao.CustomerDao
import com.example.data.dao.LocationDao
import com.example.data.dao.MaintenanceDao
import com.example.data.dao.PaymentDao
import com.example.data.dao.StaffDao
import com.example.data.dao.VehicleDao
import com.example.data.model.BookingEntity
import com.example.data.model.CustomerEntity
import com.example.data.model.LocationEntity
import com.example.data.model.MaintenanceEntity
import com.example.data.model.PaymentEntity
import com.example.data.model.StaffUserEntity
import com.example.data.model.VehicleEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        VehicleEntity::class,
        BookingEntity::class,
        CustomerEntity::class,
        MaintenanceEntity::class,
        StaffUserEntity::class,
        LocationEntity::class,
        PaymentEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class TravzDatabase : RoomDatabase() {

    abstract fun vehicleDao(): VehicleDao
    abstract fun bookingDao(): BookingDao
    abstract fun maintenanceDao(): MaintenanceDao
    abstract fun customerDao(): CustomerDao
    abstract fun staffDao(): StaffDao
    abstract fun locationDao(): LocationDao
    abstract fun paymentDao(): PaymentDao

    companion object {
        @Volatile
        private var INSTANCE: TravzDatabase? = null

        fun getInstance(context: Context): TravzDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TravzDatabase::class.java,
                    "travz_car_rental.db"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Populate seed data asynchronously
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    DatabaseSeeder.seedInitialData(database)
                                }
                            }
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // Also check if data exists; if empty, seed immediately
                CoroutineScope(Dispatchers.IO).launch {
                    if (instance.vehicleDao().getVehicleCount() == 0) {
                        DatabaseSeeder.seedInitialData(instance)
                    }
                }
                instance
            }
        }
    }
}
