package com.manimarank.websitemonitor.data.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "web_site_entry")
@Parcelize
data class WebSiteEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "url")
    val url: String,
    @ColumnInfo(name = "status")
    var status: Int? = null,
    @ColumnInfo(name = "is_paused")
    var isPaused: Boolean = false,
    @ColumnInfo(name = "updated_at")
    var updatedAt: String? = null,
    @ColumnInfo(name = "item_position")
    var itemPosition: Int? = null
) : Parcelable