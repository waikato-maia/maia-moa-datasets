package māia.ml.dataset.moa

import māia.ml.dataset.DataRow
import māia.ml.dataset.type.Nominal
import māia.ml.dataset.type.Numeric
import māia.ml.dataset.util.convertToExternalUnchecked
import māia.ml.dataset.util.ifIsPossiblyMissing


fun DataRow.getColumnForMOA(
    columnIndex: Int
): Double {
    val header = getColumnHeader(columnIndex)
    val value = getColumn(columnIndex)
    val type = header.type

    return ifIsPossiblyMissing<Nominal<*>>(type) then {
        it.indexOf(type.convertToExternalUnchecked(value)).toDouble()
    } otherwise {
        ifIsPossiblyMissing<Numeric<*>>(type) then {
            it.convertToExternalUnchecked(value)
        } otherwise {
            throw Exception("Not nominal or numeric")
        }
    }
}
