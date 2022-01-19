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
package maia.ml.dataset.moa

import com.yahoo.labs.samoa.instances.Attribute
import maia.ml.dataset.headers.header.DataColumnHeader
import maia.ml.dataset.type.standard.Nominal
import maia.ml.dataset.type.standard.Numeric
import maia.util.collect

/**
 * TODO
 */
fun dataColumnHeaderToAttribute(
    source: DataColumnHeader
): Attribute {
    val type = source.type

    return when (type) {
        is Nominal<*, *, *, *> -> Attribute(source.name, type.iterator().collect(ArrayList()))
        is Numeric<*, *> -> Attribute(source.name)
        else -> throw Exception("Column must be numeric or nominal")
    }
}
