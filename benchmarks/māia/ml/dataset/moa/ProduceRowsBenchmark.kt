/*
 * ProduceRowsBenchmark.kt
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

import kotlinx.benchmark.Blackhole
import kotlinx.benchmark.Mode
import kotlinx.benchmark.Scope
import moa.streams.generators.RandomRBFGenerator
import māia.ml.dataset.type.standard.Numeric
import māia.util.assertType
import māia.util.inlineRangeForLoop
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Measurement
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.annotations.Warmup
import java.util.concurrent.TimeUnit

/**
 * TODO
 */
@Warmup(iterations = 0)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
open class ProduceRowsBenchmark {

    @State(Scope.Benchmark)
    open class Params {

        @Param("100", "10000")
        var numRows: Int = 0

        @Param("10",  "100")
        var numAttrs: Int = 0

        @Param("10",  "100")
        var numClasses: Int = 0

        @Param("50",  "150")
        var numCentroids: Int = 0
    }

    @Benchmark
    fun moaBenchmark(params: Params, blackhole : Blackhole) {
        val gen = RandomRBFGenerator()
        gen.numAttsOption.value = params.numAttrs
        gen.numClassesOption.value = params.numClasses
        gen.numCentroidsOption.value = params.numCentroids
        gen.prepareForUse()

        inlineRangeForLoop(params.numRows) {
            val row = gen.nextInstance()
            var sum = 0.0
            inlineRangeForLoop(params.numAttrs) {
                sum += row.data.value(it)
            }
            blackhole.consume(sum)
        }
    }

    @Benchmark
    fun maiaBenchmark(params: Params, blackhole : Blackhole) {
        val gen = MOADataStream(
            materalizeMOAClass<RandomRBFGenerator>(
                "moa.streams.generators.RandomRBFGenerator " +
                        "-a ${params.numAttrs} " +
                        "-c ${params.numClasses} " +
                        "-n ${params.numCentroids}"
            )
        ) { _, _, _ -> true }
        val iter = gen.rowIterator()

        inlineRangeForLoop(params.numRows) {
            val row = iter.next()
            var sum = 0.0
            inlineRangeForLoop(params.numAttrs) {
                val repr = assertType<Numeric<*, *>>(gen.headers[it].type).canonicalRepresentation
                sum += row.getValue(repr)
            }
            blackhole.consume(sum)
        }
    }

}
