/*
 * DoubleNaNMissingValuesConverter.kt
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
