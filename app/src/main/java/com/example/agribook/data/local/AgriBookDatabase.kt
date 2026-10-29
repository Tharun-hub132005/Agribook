package com.example.agribook.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        UserEntity::class,
        CropDetailEntity::class,
        PostEntity::class,
        CommentEntity::class,
        PostLikeEntity::class,
        BookmarkEntity::class,
        ConnectionEntity::class,
        BlockEntity::class,
        MessageEntity::class,
        NotificationEntity::class,
        ReportEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AgriBookDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun cropDetailDao(): CropDetailDao
    abstract fun postDao(): PostDao
    abstract fun interactionDao(): InteractionDao
    abstract fun connectionDao(): ConnectionDao
    abstract fun messageDao(): MessageDao
    abstract fun notificationDao(): NotificationDao
    abstract fun reportDao(): ReportDao

    companion object {
        @Volatile
        private var INSTANCE: AgriBookDatabase? = null

        fun getDatabase(context: Context): AgriBookDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AgriBookDatabase::class.java,
                    "agribook_master.db"
                ).fallbackToDestructiveMigration(true).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
