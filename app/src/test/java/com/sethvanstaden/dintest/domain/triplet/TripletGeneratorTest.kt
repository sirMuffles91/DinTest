package com.sethvanstaden.dintest.domain.triplet

import com.sethvanstaden.dintest.FakeRandom
import kotlin.test.assertFailsWith
import org.junit.Test
import kotlin.test.assertEquals

class TripletGeneratorTest {
    @Test
    fun `next previous null returns first candidate and tracks used`() {
        val rng = FakeRandom(intArrayOf(
            1,2,3
        ))
        val gen = TripletGenerator(rng)

        val t = gen.next(previous = null)

        assertEquals(Triplet(1,2,3), t)
        val rng2 = FakeRandom(intArrayOf(
            1,2,3,
            4,5,6
        ))
        val gen2 = TripletGenerator(rng2)
        gen2.next(null)
        val t2 = gen2.next(null)
        assertEquals(Triplet(4,5,6), t2)
    }

    @Test
    fun `rejects duplicate triplet and retries until unique`() {
        val rng = FakeRandom(
            intArrayOf(
                1, 2, 3,
                1, 2, 3,
                1, 2, 3,
                9, 8, 7
            )
        )
        val gen = TripletGenerator(rng)

        val first = gen.next(null)
        val second = gen.next(null)

        assertEquals(Triplet(1,2,3), first)
        assertEquals(Triplet(9,8,7), second)
    }

    @Test
    fun `rejects same-position repeats versus previous`() {
        val previous = Triplet(1,2,3)

        val rng = FakeRandom(intArrayOf(
            1,9,9,
            9,2,9,
            9,9,3,
            4,5,6
        ))
        val gen = TripletGenerator(rng)

        val t = gen.next(previous)

        assertEquals(Triplet(4,5,6), t)
    }

    @Test
    fun `eventually returns valid after invalid generated sequences`() {
        val previous = Triplet(1,2,3)

        val rng = FakeRandom(intArrayOf(
            4,5,6,
            4,5,6,
            1,9,9,
            9,2,9,
            9,9,3,
            7,8,9
        ))
        val gen = TripletGenerator(rng)

        val first = gen.next(null)
        val second = gen.next(previous)

        assertEquals(Triplet(4,5,6), first)
        assertEquals(Triplet(7,8,9), second)
    }

    @Test
    fun `reset clears used history`() {
        val rng = FakeRandom(intArrayOf(
            1,2,3,
            1,2,3
        ))
        val gen = TripletGenerator(rng)

        val first = gen.next(null)
        gen.reset()
        val second = gen.next(null)

        assertEquals(first, second)
    }

    @Test
    fun `throws after 10_000 failed attempts`() {
        val previous = Triplet(1,2,3)
        val alwaysInvalid = IntArray(30_000) { idx ->
            when (idx % 3) {
                0 -> 1
                1 -> 2
                else -> 3
            }
        }

        val rng = FakeRandom(alwaysInvalid)
        val gen = TripletGenerator(rng)

        assertFailsWith<IllegalStateException> {
            gen.next(previous)
        }
    }
}