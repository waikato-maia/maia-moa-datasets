package māia.ml.dataset.moa

import com.yahoo.labs.samoa.instances.Attribute
import māia.ml.dataset.DataColumnHeader
import māia.ml.dataset.type.DataType
import māia.ml.dataset.type.WithFiniteMissingValues
import māia.ml.dataset.type.WithMissingValues
import māia.ml.dataset.type.standard.NominalDoubleIndexImpl
import māia.ml.dataset.type.standard.NullMissingValuesConverter
import māia.ml.dataset.type.standard.NumericDoubleImpl
import java.util.*

/**
 * TODO: What class does.
 *
 * @author Corey Sterling (csterlin at waikato dot ac dot nz)
 */
class MOADataColumnHeader(
    internal val source : Attribute,
    override val isTarget : Boolean,
    assertPresent: Boolean = false
) : DataColumnHeader {

    override val name : String = source.name()

    override val type : DataType<*, *> = when {
        source.isNumeric -> if (assertPresent)
            NumericDoubleImpl
        else
            WithMissingValues(
                NumericDoubleImpl,
                DoubleNaNMissingValuesConverter,
                DoubleNaNMissingValuesConverter
            )
        source.isNominal -> {
            val classes = source.enumerateValues() as Enumeration<String>
            val nominal = NominalDoubleIndexImpl(
                *Array<String>(source.numValues()) {
                    classes.nextElement()
                }
            )
            if (assertPresent)
                nominal
            else
                WithFiniteMissingValues(
                    nominal,
                    DoubleNaNMissingValuesConverter,
                    NullMissingValuesConverter()
                )
        }
        else -> throw Exception("MOA data-stream wrappers can only handle nominal/numeric attributes")
    }
}
