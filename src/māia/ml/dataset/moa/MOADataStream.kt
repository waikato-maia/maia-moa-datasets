package māia.ml.dataset.moa

import com.yahoo.labs.samoa.instances.Attribute
import com.yahoo.labs.samoa.instances.InstancesHeader
import moa.streams.InstanceStream
import māia.ml.dataset.DataColumnHeader
import māia.ml.dataset.DataMetadata
import māia.ml.dataset.DataStream

/**
 * TODO: What class does.
 *
 * @author Corey Sterling (csterlin at waikato dot ac dot nz)
 */
class MOADataStream(
    internal val source : InstanceStream,
    assertPresent: (Int, Attribute, Boolean) -> Boolean = { _, _, _ -> false }
) : DataStream<MOADataRow> {

    private val header : InstancesHeader = source.header

    override val metadata: DataMetadata = object : DataMetadata {
        override val name: String = header.toString()
    }

    private val rowIterator = object : Iterator<MOADataRow> {
        override fun hasNext(): Boolean {
            return source.hasMoreInstances()
        }

        override fun next(): MOADataRow {
            return MOADataRow(source.nextInstance(), this@MOADataStream)
        }
    }

    override fun rowIterator(): Iterator<MOADataRow> = rowIterator

    override val numColumns : Int = header.numAttributes()

    private val columnHeaders = Array(numColumns) { columnIndex ->
        val attribute = header.attribute(columnIndex)
        val isTarget = columnIndex == header.classIndex()
        MOADataColumnHeader(
            attribute,
            isTarget,
            assertPresent(columnIndex, attribute, isTarget)
        )
    }

    override fun getColumnHeader(columnIndex : Int) : MOADataColumnHeader = columnHeaders[columnIndex]

    override fun iterateColumnHeaders() : Iterator<DataColumnHeader> {
        return columnHeaders.iterator()
    }

}
