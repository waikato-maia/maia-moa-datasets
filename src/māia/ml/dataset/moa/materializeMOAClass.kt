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
