/*
 * dataStreamToInstanceStream.kt
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
import com.yahoo.labs.samoa.instances.InstancesHeader
import moa.MOAObject
import moa.core.Example
import moa.streams.InstanceStream
import māia.ml.dataset.DataStream
import java.lang.StringBuilder

fun dataStreamToInstanceStream(
    source: DataStream<*>
): InstanceStream {
    return object : InstanceStream {
        private val headers = withColumnHeadersToInstancesHeader(source)
        private val iter = source.rowIterator()

        override fun measureByteSize() : Int {
            TODO("Not yet implemented")
        }

        override fun copy() : MOAObject {
            TODO("Not yet implemented")
        }

        override fun getDescription(sb : StringBuilder?, indent : Int) {
            TODO("Not yet implemented")
        }

        override fun getHeader() : InstancesHeader = headers

        override fun estimatedRemainingInstances() : Long {
            TODO("Not yet implemented")
        }

        override fun hasMoreInstances() : Boolean = iter.hasNext()

        override fun nextInstance() : Example<Instance> =
            dataRowToInstanceExample(iter.next(), headers)

        override fun isRestartable() : Boolean {
            TODO("Not yet implemented")
        }

        override fun restart() {
            TODO("Not yet implemented")
        }

    }
}
