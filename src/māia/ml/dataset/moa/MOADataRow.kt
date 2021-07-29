package māia.ml.dataset.moa

import com.yahoo.labs.samoa.instances.Instance
import moa.core.Example
import māia.ml.dataset.DataRow
import māia.ml.dataset.WithColumnHeaders

/**
 * TODO: What class does.
 *
 * @author Corey Sterling (csterlin at waikato dot ac dot nz)
 */
class MOADataRow(
    internal val source: Example<Instance>,
    internal val stream: MOADataStream
) : DataRow, WithColumnHeaders by stream {

    override fun getColumn(columnIndex : Int) : Double = source.data.value(columnIndex)

}
