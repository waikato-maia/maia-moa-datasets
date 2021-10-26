/*
 * materializeMOAClass.kt
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

import moa.MOAObject
import moa.options.ClassOption
import moa.options.AbstractOptionHandler
import moa.tasks.NullMonitor

/**
 * Class that is instantiated to have a single option, which defaults
 * to the CLI configuration string given. This is then used to materialise
 * an instance of the MOA class that the configuration describes.
 *
 * This is just a helper class for [materializeMOAClass]. Use that instead.
 *
 * @param cls
 *          The MOA class to be materialised.
 * @param config
 *          The configuration string specifying the instance.
 *
 * @author Corey Sterling (csterlin at waikato dot ac dot nz)
 */
class MOAClassMaterializer<C: MOAObject>(
    cls: Class<C>,
    config: String
) {
    private val option = ClassOption("class", 'c', "Class.", cls, config)

    private val monitor = NullMonitor()

    fun materialize() : C {
        val instance = option.materializeObject(monitor, null) as C
        if (instance is AbstractOptionHandler) instance.prepareForUse(monitor, null)
        return instance
    }

}

/**
 * Attempts to materialise an instance of the given class
 * from the given CLI configuration string.
 *
 * @param C
 *          The class to instantiate.
 * @param config
 *          The CLI configuration string for the instance.
 * @return
 *          The materialised instance.
 */
inline fun <reified C: MOAObject> materalizeMOAClass(
    config : String
): C = MOAClassMaterializer(C::class.java, config).materialize()
