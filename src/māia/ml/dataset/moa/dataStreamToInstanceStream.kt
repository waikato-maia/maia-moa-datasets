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
