package com.ifyezedev.coslog.data.db.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*

@Entity(tableName = "elements")
data class Element(
    @PrimaryKey(autoGenerate = true)
    val eid: Long = 0L,

    val cosplay_id: Long = 0,

    var name: String = "",

    @ColumnInfo(name = "is_buy")
    var isBuy: Boolean = false,

    var cost: Double = 0.00,

    var time: Long = 0,

    var source: String = "",

    var notes: String = "",

    var progress: Float = 0.0F,

    var images: ArrayList<String> = arrayListOf(),

    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readDouble(),
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readFloat(),
        parcel.createStringArrayList()!!) {
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
        parcel.writeFloat(progress)
        parcel.writeStringList(images)
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


fun elementsBuilder(block: Element.() -> Unit): Element {
    val e = Element()
    e.block()
    return e
}