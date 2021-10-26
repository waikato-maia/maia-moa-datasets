/*
 * dataRowToInstanceExample.kt
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

import com.yahoo.labs.samoa.instances.Instance
import com.yahoo.labs.samoa.instances.InstanceImpl
import com.yahoo.labs.samoa.instances.InstancesHeader
import moa.core.Example
import moa.core.InstanceExample
import māia.ml.dataset.DataRow
import māia.ml.dataset.util.weight

/**
 * TODO
 */
fun dataRowToInstanceExample(
    row: DataRow,
    headers: InstancesHeader,
    predict: Boolean = false
): Example<Instance> {
    if (row is MOADataRow)
        return InstanceExample(row.source)

    val classIndex = headers.classIndex()

    // Create a custom value-getter which inserts a zero-value at the class-index
    // when in predict-mode. This is due to a difference between MOA and MĀIA, where
    // MOA will pass a class-value to a learner during prediction and expect it to
    // ignore it, MĀIA will simply not pass one (hence the row is one column shorter).
    val getMOAValue = if (predict)
    { index ->
        when {
            index == classIndex -> 0.0
            index > classIndex -> row.getColumnForMOA(index - 1)
            else -> row.getColumnForMOA(index)
        }
    } else row::getColumnForMOA

    val instance = InstanceImpl(row.weight, DoubleArray(headers.numAttributes(), getMOAValue))
    instance.setDataset(headers)

    return InstanceExample(instance)
}
