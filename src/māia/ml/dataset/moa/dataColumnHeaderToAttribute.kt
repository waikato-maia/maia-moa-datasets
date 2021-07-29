package māia.ml.dataset.moa

import com.yahoo.labs.samoa.instances.Attribute
import māia.ml.dataset.DataColumnHeader
import māia.ml.dataset.type.Nominal
import māia.ml.dataset.type.Numeric
import māia.ml.dataset.util.ifIsPossiblyMissing
import māia.util.collect

fun dataColumnHeaderToAttribute(
    source: DataColumnHeader
): Attribute {
    if (source is MOADataColumnHeader) return source.source

    val type = source.type
    return ifIsPossiblyMissing<Numeric<*>>(type) then {
        Attribute(source.name)
    } otherwise {
        ifIsPossiblyMissing<Nominal<*>>(type) then {
            Attribute(source.name, it.iterateCategories().collect(ArrayList()))
        } otherwise {
            throw Exception("Column must be numeric or nominal")
        }
    }
}
