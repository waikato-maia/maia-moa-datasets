/*
 * MOADataRow.kt
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
import com.yahoo.labs.samoa.instances.Instance
import maia.ml.dataset.DataRow
import maia.ml.dataset.WithColumns
import maia.ml.dataset.WithWeight
import maia.ml.dataset.headers.DataColumnHeaders
import maia.ml.dataset.headers.ensureOwnership
import maia.ml.dataset.type.DataRepresentation
import maia.ml.dataset.type.EntropicRepresentation
import maia.ml.dataset.type.standard.Nominal
import maia.ml.dataset.type.standard.NominalCanonicalRepresentation
import maia.ml.dataset.type.standard.NominalIndexRepresentation
import maia.ml.dataset.type.standard.NumericCanonicalRepresentation
import maia.ml.dataset.type.standard.UntypedRepresentation
import maia.util.assertType
import maia.util.error.UNREACHABLE_CODE

/**
 * Represents an instance from a MOA data-stream in MĀIA as a [DataRow].
 * Constructor is internal so that only [MOADataStream] can create instances.
 *
 * @param source
 *          The source instance.
 * @param headers
 *          The headers for this row.
 * @param stream
 *          The [MOADataStream] that created this row (if any).
 *
 * @author Corey Sterling (csterlin at waikato dot ac dot nz)
 */
class MOADataRow internal constructor(
    internal val source: Instance,
    headers: DataColumnHeaders,
    internal val stream : MOADataStream?
) : DataRow, WithWeight, WithColumns by headers {

    /**
     * Constructor for stand-alone instances.
     *
     * @param source
     *          The source instance.
     * @param assertPresent
     *          Passed to [parseMOAHeaders].
     * @param untypedForUnrecognised
     *          Passed to [parseMOAHeaders].
     */
    constructor(
        source: Instance,
        assertPresent: (Int, Attribute, Boolean) -> Boolean = { _, _, _ -> false },
        untypedForUnrecognised: Boolean = false
    ): this(
        source,
        parseMOAHeaders(source.dataset(), assertPresent, untypedForUnrecognised),
        null
    )

    override val weight : Double = source.weight()

    override fun <T> getValue(
        representation : DataRepresentation<*, *, out T>
    ) : T = headers.ensureOwnership(representation) {
        val value = source.value(columnIndex)
        representationParseMOAValue(this, value)
    }
}
