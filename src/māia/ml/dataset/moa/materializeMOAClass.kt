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

import moa.options.ClassOption
import moa.options.AbstractOptionHandler
import moa.tasks.NullMonitor

/**
 * TODO: What class does.
 *
 * @author Corey Sterling (csterlin at waikato dot ac dot nz)
 */
private class MOAClassMaterializer<C>(
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

fun <C> materalizeMOAClass(cls: Class<C>, config : String) = MOAClassMaterializer(cls, config).materialize()
