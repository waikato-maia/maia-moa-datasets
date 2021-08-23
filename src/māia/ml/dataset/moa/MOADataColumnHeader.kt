/*
 * MOADataColumnHeader.kt
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
import māia.ml.dataset.type.DataType
import māia.ml.dataset.type.WithFiniteMissingValues
import māia.ml.dataset.type.WithMissingValues
import māia.ml.dataset.type.standard.NominalDoubleIndexImpl
import māia.ml.dataset.type.standard.NullMissingValuesConverter
import māia.ml.dataset.type.standard.NumericDoubleImpl
import java.util.*

/**
 * TODO: What class does.
 *
 * @author Corey Sterling (csterlin at waikato dot ac dot nz)
 */
class MOADataColumnHeader(
    internal val source : Attribute,
    override val isTarget : Boolean,
    assertPresent: Boolean = false
) : DataColumnHeader {

    override val name : String = source.name()

    override val type : DataType<*, *> = when {
        source.isNumeric -> if (assertPresent)
            NumericDoubleImpl
        else
            WithMissingValues(
                NumericDoubleImpl,
                DoubleNaNMissingValuesConverter,
                DoubleNaNMissingValuesConverter
            )
        source.isNominal -> {
            val classes = source.enumerateValues() as Enumeration<String>
            val nominal = NominalDoubleIndexImpl(
                *Array<String>(source.numValues()) {
                    classes.nextElement()
                }
            )
            if (assertPresent)
                nominal
            else
                WithFiniteMissingValues(
                    nominal,
                    DoubleNaNMissingValuesConverter,
                    NullMissingValuesConverter()
                )
        }
        else -> throw Exception("MOA data-stream wrappers can only handle nominal/numeric attributes")
    }
}
