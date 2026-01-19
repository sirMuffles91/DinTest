package com.sethvanstaden.dintest

import kotlin.random.Random

class FakeRandom(private val values: IntArray) : Random() {
    private var i = 0

    override fun nextBits(bitCount: Int): Int {
        return 0
    }

    override fun nextInt(from: Int, until: Int): Int {
        require(from == 1 && until == 10) { "Unexpected bounds: $from..$until" }
        if (i >= values.size) error("FakeRandom exhausted at index $i")
        val v = values[i++]
        require(v in from until until) { "FakeRandom value out of range: $v" }
        return v
    }
}