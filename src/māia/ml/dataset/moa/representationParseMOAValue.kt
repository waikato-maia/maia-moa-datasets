/*
 * representationParseMOAValue.kt
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

import māia.ml.dataset.type.DataRepresentation
import māia.ml.dataset.type.EntropicRepresentation
import māia.ml.dataset.type.standard.Nominal
import māia.ml.dataset.type.standard.NominalCanonicalRepresentation
import māia.ml.dataset.type.standard.NominalIndexRepresentation
import māia.ml.dataset.type.standard.NumericCanonicalRepresentation
import māia.ml.dataset.type.standard.UntypedRepresentation
import māia.util.assertType
import māia.util.error.UNREACHABLE_CODE

/**
 * Parses a value from MOA using the provided representation.
 *
 * @param representation
 *          The requested representation of the value.
 * @param value
 *          The MOA double representation of the value.
 * @return
 *          The representational-type of the value.
 */
fun <T> representationParseMOAValue(
    representation : DataRepresentation<*, *, T>,
    value: Double
): T {
    return when (representation) {
        is NominalCanonicalRepresentation -> assertType<Nominal<*, *, *, *>>(representation.dataType)[value.toInt()]
        is NominalIndexRepresentation -> value.toInt()
        is EntropicRepresentation -> value.toInt().toBigInteger()
        is NumericCanonicalRepresentation -> value
        is UntypedRepresentation -> value
        else -> UNREACHABLE_CODE("Header creation ensures that only these representations will reach here")
    } as T
}
