package com.anishkun.hidetext.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.anishkun.hidetext.data.local.dao.ContactDao
import com.anishkun.hidetext.data.local.dao.MessageDao
import com.anishkun.hidetext.data.local.entity.ContactEntity
import com.anishkun.hidetext.data.local.entity.MessageEntity

@Database(entities = [MessageEntity::class, ContactEntity::class], version = 2, exportSchema = false)
abstract class ChatDatabase : RoomDatabase() {
    abstract val messageDao: MessageDao
    abstract val contactDao: ContactDao
}
