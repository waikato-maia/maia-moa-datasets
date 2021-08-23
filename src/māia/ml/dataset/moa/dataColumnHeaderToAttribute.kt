/*
 * dataColumnHeaderToAttribute.kt
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
import māia.ml.dataset.DataColumnHeader
import māia.ml.dataset.type.Nominal
import māia.ml.dataset.type.Numeric
import māia.ml.dataset.util.ifIsPossiblyMissing
import māia.util.collect

fun dataColumnHeaderToAttribute(
    source: DataColumnHeader
): Attribute {
    if (source is MOADataColumnHeader) return source.source

    val type = source.type
    return ifIsPossiblyMissing<Numeric<*>>(type) then {
        Attribute(source.name)
    } otherwise {
        ifIsPossiblyMissing<Nominal<*>>(type) then {
            Attribute(source.name, it.iterateCategories().collect(ArrayList()))
        } otherwise {
            throw Exception("Column must be numeric or nominal")
        }
    }
}
