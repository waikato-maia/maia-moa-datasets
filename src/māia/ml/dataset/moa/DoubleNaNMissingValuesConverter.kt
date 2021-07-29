package māia.ml.dataset.moa

import māia.ml.dataset.type.MissingValuesConverter

/**
 * Converter which uses a double NaN to represent the missing/not missing status
 * of values.
 */
object DoubleNaNMissingValuesConverter : MissingValuesConverter<Double, Double>() {
    override fun isMissing(value : Double) : Boolean = value.isNaN()
    override fun convertNotMissingToBase(value : Double) : Double = value
    override fun convertBaseToNotMissing(value : Double) : Double = value
    override fun missing() : Double = Double.NaN
}
