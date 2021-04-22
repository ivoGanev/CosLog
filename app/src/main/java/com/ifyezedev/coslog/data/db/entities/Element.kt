package com.ifyezedev.coslog.data.db.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*
import com.ifyezedev.coslog.data.db.entities.*
import androidx.room.ForeignKey.CASCADE

@Entity(tableName = "elements")
data class Element(
    @PrimaryKey(autoGenerate = true)
    val eid: Long = 0L,

    val cosplay_id: Long,

    var name: String,

    @ColumnInfo(name = "is_buy")
    var isBuy: Boolean,

    var cost: Double,

    var time: Long,

    var source: String,

    var notes: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readDouble(),
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString()!!) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(eid)
        parcel.writeLong(cosplay_id)
        parcel.writeString(name)
        parcel.writeByte(if (isBuy) 1 else 0)
        parcel.writeDouble(cost)
        parcel.writeLong(time)
        parcel.writeString(source)
        parcel.writeString(notes)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Element> {
        override fun createFromParcel(parcel: Parcel): Element {
            return Element(parcel)
        }

        override fun newArray(size: Int): Array<Element?> {
            return arrayOfNulls(size)
        }
    }
}