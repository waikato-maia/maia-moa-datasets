package māia.ml.dataset.moa

import com.yahoo.labs.samoa.instances.Instances
import com.yahoo.labs.samoa.instances.InstancesHeader
import māia.ml.dataset.WithColumnHeaders
import māia.ml.dataset.WithMetadata
import māia.util.asIterable
import māia.util.enumerate
import māia.util.filter

fun withColumnHeadersToInstancesHeader(
    source: WithColumnHeaders,
    classIndex : Int? = null
): InstancesHeader {
    return when (source) {
        is MOADataStream -> source.source.header
        is MOADataRow -> source.stream.source.header
        else -> InstancesHeader(
            Instances(
                if (source is WithMetadata) source.name else "",
                Array(source.numColumns) {
                    dataColumnHeaderToAttribute(
                        source.getColumnHeader(it)
                    )
                },
                0
            )
        ).apply {
            val classIndexActual = classIndex ?: source
                .iterateColumnHeaders()
                .enumerate()
                .filter { it.second.isTarget }
                .asIterable()
                .firstOrNull()
                ?.first

            if (classIndexActual != null) setClassIndex(classIndexActual)
        }
    }
}
