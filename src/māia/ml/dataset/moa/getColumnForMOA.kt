/*
 * getColumnForMOA.kt
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

import māia.ml.dataset.DataRow
import māia.ml.dataset.type.standard.Nominal
import māia.ml.dataset.type.standard.Numeric

/**
 * Gets the value from a data-row in the format that MOA expects.
 *
 * @param columnIndex
 *          The column from which to get the value.
 *
 * @return The double representation expected by MOA.
 *
 * @throws Exception
 *          If the data-type at [columnIndex] is not nominal/numeric.
 */
fun DataRow.getColumnForMOA(
    columnIndex: Int
): Double {
    return when (val type = headers[columnIndex].type) {
        is Nominal<*, *, *, *> -> getValue(type.indexRepresentation).toDouble()
        is Numeric<*, *> -> getValue(type.canonicalRepresentation)
        else -> throw Exception("Not nominal or numeric")
    }
}
