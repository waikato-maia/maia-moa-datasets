/*
 * withColumnHeadersToInstancesHeader.kt
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

import com.yahoo.labs.samoa.instances.Instances
import com.yahoo.labs.samoa.instances.InstancesHeader
import maia.ml.dataset.WithColumns
import maia.ml.dataset.WithMetadata

/**
 * TODO
 */
fun withColumnHeadersToInstancesHeader(
    source: WithColumns,
    classIndex : Int? = null
): InstancesHeader {
    // If the source is a wrapper around a MOA data-set already, internally
    // inspect it for the MOA header
    if (source is MOADataStream)
        return source.source.header
    if (source is MOADataRow && source.stream != null)
        return source.stream.source.header

    // Create the header manually
    val result = InstancesHeader(
        Instances(
            if (source is WithMetadata) source.name else "",
            Array(source.numColumns) {
                dataColumnHeaderToAttribute(source.headers[it])
            },
            0
        )
    )

    // Either use the given class index, or find the first target header and use that
    if (classIndex !== null) {
        result.setClassIndex(classIndex)
    } else {
        for (header in source.headers) {
            if (header.isTarget) {
                result.setClassIndex(header.index)
                break
            }
        }
    }

    return result
}
