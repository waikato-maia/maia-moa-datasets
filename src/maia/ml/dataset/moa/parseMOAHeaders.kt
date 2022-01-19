/*
 * parseMOAHeaders.kt
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
import com.yahoo.labs.samoa.instances.Instances
import maia.ml.dataset.headers.MutableDataColumnHeaders

/**
 * Parses the headers of a MOA data-set for MĀIA.
 *
 * @param instances
 *          The source MOA data-set to wrap.
 * @param assertPresent
 *          Function which takes the index of an attribute, the attribute
 *          itself, and whether the attribute is a target, and determines
 *          whether the resulting headers should assert that all values will
 *          be present (no missing values).
 * @param untypedForUnrecognised
 *          Whether an untyped data-type should be produced for unrecognised
 *          attribute types. Currently only recognises nominal/numeric types.
 *          If false (the default), an exception is raised instead.
 *
 * @return The constructed headers.
 *
 * @throws Exception If [untypedForUnrecognised] is false and any attribute's
 *                   type is unrecognised.
 *
 * @author Corey Sterling (csterlin at waikato dot ac dot nz)
 */
fun parseMOAHeaders(
    instances : Instances,
    assertPresent: (Int, Attribute, Boolean) -> Boolean = { _, _, _ -> false },
    untypedForUnrecognised: Boolean = false
): MutableDataColumnHeaders {
    // Get the number of headers to create
    val size = instances.numAttributes()

    // Create the empty result to parse into
    val result = MutableDataColumnHeaders(size)

    // For each column...
    repeat(size) { columnIndex ->
        // Get the MOA attribute
        val attribute = instances.attribute(columnIndex)

        // Determine its feature/target status
        val isTarget = columnIndex == instances.classIndex()

        // Let the caller decide if we should assert that all values are present
        val shouldAssertPresent = assertPresent(columnIndex, attribute, isTarget)

        // Parse the attribute and append the resulting column to our result
        parseMOAAttribute(attribute, shouldAssertPresent, untypedForUnrecognised) { name, type ->
            result.append(name, type, isTarget)
        }
    }

    return result
}
