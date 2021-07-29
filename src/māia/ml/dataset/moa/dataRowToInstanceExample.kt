package māia.ml.dataset.moa

import com.yahoo.labs.samoa.instances.Instance
import com.yahoo.labs.samoa.instances.InstanceImpl
import com.yahoo.labs.samoa.instances.InstancesHeader
import moa.core.Example
import moa.core.InstanceExample
import māia.ml.dataset.DataRow
import māia.ml.dataset.util.weight

fun dataRowToInstanceExample(
    row: DataRow,
    headers: InstancesHeader,
    predict: Boolean = false
): Example<Instance> {
    if (row is MOADataRow) return row.source

    val classIndex = headers.classIndex()

    val getMOAValue = if (predict) { index ->
        when {
            index == classIndex -> 0.0
            index > classIndex -> row.getColumnForMOA(index - 1)
            else -> row.getColumnForMOA(index)
        }
    } else row::getColumnForMOA

    return InstanceExample(
        InstanceImpl(
            row.weight,
            DoubleArray(headers.numAttributes(), getMOAValue)
        ).apply {
            setDataset(headers)
        }
    )
}
