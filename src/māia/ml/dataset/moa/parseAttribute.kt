/*
 * parseAttribute.kt
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
import māia.ml.dataset.type.DataType
import māia.ml.dataset.type.standard.Nominal
import māia.ml.dataset.type.standard.Numeric
import māia.ml.dataset.type.standard.UntypedData
import java.util.*

/**
 * Parses a MOA [Attribute] into a name and [DataType] for use in MĀIA.
 *
 * @param attribute
 *          The attribute to convert.
 * @param assertPresent
 *          Whether the MĀIA data-type should assert that no values will be
 *          missing.
 * @param untypedForUnrecognised
 *          Whether an untyped data-type should be produced for unrecognised
 *          attribute types. Currently only recognises nominal/numeric types.
 *          If false (the default), an exception is raised instead.
 * @param block
 *          The code to process the name/type of the attribute.
 * @return The result of [block].
 *
 * @param R The return type of [block] and consequently this function.
 *
 * @throws Exception If [untypedForUnrecognised] is false and the attribute's
 *                   type is unrecognised.
 */
fun <R> parseMOAAttribute(
    attribute: Attribute,
    assertPresent: Boolean = false,
    untypedForUnrecognised: Boolean = false,
    block: (String, DataType<*, *>) -> R
): R {
    // The data-type should support missing values if we're not asserting that
    // all values will be present
    val supportsMissingValues = !assertPresent

    // Get the attribute's name for our name
    val name = attribute.name()

    // Parse the attribute's data-type
    val type = when {
        attribute.isNumeric -> Numeric.PlaceHolder(supportsMissingValues)
        attribute.isNominal -> {
            val classes = attribute.enumerateValues() as Enumeration<String>
            Nominal.PlaceHolder(
                supportsMissingValues,
                *Array<String>(attribute.numValues()) {
                    classes.nextElement()
                }
            )
        }
        untypedForUnrecognised -> UntypedData.PlaceHolder(supportsMissingValues)
        else -> throw Exception("MOA data-stream wrappers can only handle nominal/numeric attributes")
    }

    // Call the receiving code with the name and type
    return block(name, type)
}
