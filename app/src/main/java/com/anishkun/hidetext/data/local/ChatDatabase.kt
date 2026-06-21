package com.anishkun.hidetext.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.anishkun.hidetext.data.local.dao.MessageDao
import com.anishkun.hidetext.data.local.entity.MessageEntity

@Database(entities = [MessageEntity::class], version = 1, exportSchema = false)
abstract class ChatDatabase : RoomDatabase() {
    abstract val messageDao: MessageDao
}
