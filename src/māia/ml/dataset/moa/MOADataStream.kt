/*
 * MOADataStream.kt
 * Copyright (C) 2021 University of Waikato, Hamilton, New Zealand
 *
 * This file is part of MĀIA.
 *
 * MĀIA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MĀIA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MĀIA.  If not, see <https://www.gnu.org/licenses/>.
 */
package māia.ml.dataset.moa

import com.yahoo.labs.samoa.instances.Attribute
import com.yahoo.labs.samoa.instances.InstancesHeader
import moa.streams.InstanceStream
import māia.ml.dataset.DataColumnHeader
import māia.ml.dataset.DataMetadata
import māia.ml.dataset.DataStream

/**
 * TODO: What class does.
 *
 * @author Corey Sterling (csterlin at waikato dot ac dot nz)
 */
class MOADataStream(
    internal val source : InstanceStream,
    assertPresent: (Int, Attribute, Boolean) -> Boolean = { _, _, _ -> false }
) : DataStream<MOADataRow> {

    private val header : InstancesHeader = source.header

    override val metadata: DataMetadata = object : DataMetadata {
        override val name: String = header.toString()
    }

    private val rowIterator = object : Iterator<MOADataRow> {
        override fun hasNext(): Boolean {
            return source.hasMoreInstances()
        }

        override fun next(): MOADataRow {
            return MOADataRow(source.nextInstance(), this@MOADataStream)
        }
    }

    override fun rowIterator(): Iterator<MOADataRow> = rowIterator

    override val numColumns : Int = header.numAttributes()

    private val columnHeaders = Array(numColumns) { columnIndex ->
        val attribute = header.attribute(columnIndex)
        val isTarget = columnIndex == header.classIndex()
        MOADataColumnHeader(
            attribute,
            isTarget,
            assertPresent(columnIndex, attribute, isTarget)
        )
    }

    override fun getColumnHeader(columnIndex : Int) : MOADataColumnHeader = columnHeaders[columnIndex]

    override fun iterateColumnHeaders() : Iterator<DataColumnHeader> {
        return columnHeaders.iterator()
    }

}
