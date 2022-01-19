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
package maia.ml.dataset.moa

import com.yahoo.labs.samoa.instances.Attribute
import moa.streams.InstanceStream
import maia.ml.dataset.DataMetadata
import maia.ml.dataset.DataStream

/**
 * Wraps a MOA [InstanceStream] as a MĀIA [DataStream].
 *
 * @param source
 *          The source [InstanceStream] to wrap.
 * @param assertPresent
 *          Function which takes the index of an attribute, the attribute
 *          itself, and whether the attribute is a target, and determines
 *          whether this [DataStream] should assert that all values will
 *          be present (no missing values).
 * @param untypedForUnrecognised
 *          Whether an untyped data-type should be produced for unrecognised
 *          attribute types. Currently only recognises nominal/numeric types.
 *          If false (the default), an exception is raised instead.
 *
 * @throws Exception If [untypedForUnrecognised] is false and any attribute's
 *                   type is unrecognised.
 *
 * @author Corey Sterling (csterlin at waikato dot ac dot nz)
 */
class MOADataStream(
    internal val source : InstanceStream,
    untypedForUnrecognised: Boolean = false,
    assertPresent: (Int, Attribute, Boolean) -> Boolean = { _, _, _ -> false }
) : DataStream<MOADataRow> {

    override val numColumns : Int = source.header.numAttributes()

    private val headersInternal = parseMOAHeaders(
        source.header,
        assertPresent,
        untypedForUnrecognised
    )

    override val headers = headersInternal.readOnlyView

    override val metadata: DataMetadata = object : DataMetadata {
        override val name: String = source.header.toString()
    }

    private val rowIterator = object : Iterator<MOADataRow> {
        override fun hasNext(): Boolean = source.hasMoreInstances()
        override fun next(): MOADataRow = MOADataRow(source.nextInstance().data, headers, this@MOADataStream)
    }

    override fun rowIterator(): Iterator<MOADataRow> = rowIterator

}
