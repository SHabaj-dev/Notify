package com.sbz.noteify.util

import java.util.Date

object Converter {

    fun dateToLong(date: Date): Long {
        return date.time
    }

    fun longToDate(longValue: Long): Date {
        return Date(longValue)
    }

}