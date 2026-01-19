package com.sethvanstaden.dintest.domain.triplet

class TripletGenerator(
    private val rng: kotlin.random.Random = kotlin.random.Random.Default
) {
    private val used = LinkedHashSet<String>()

    fun reset() { used.clear() }

    fun next(previous: Triplet?): Triplet {
        repeat(10_000) {
            val t = Triplet(
                a = rng.nextInt(1, 10),
                b = rng.nextInt(1, 10),
                c = rng.nextInt(1, 10),
            )

            if (previous != null) {
                if (t.a == previous.a) return@repeat
                if (t.b == previous.b) return@repeat
                if (t.c == previous.c) return@repeat
            }

            if (!used.add(t.asString)) return@repeat

            return t
        }
        error("Unable to generate a valid triplet...")
    }
}