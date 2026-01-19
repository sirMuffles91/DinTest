package com.sethvanstaden.dintest.domain.triplet

data class Triplet(val a: Int, val b: Int, val c: Int) {
    val asString: String get() = "$a$b$c"
    val digits: List<Int> get() = listOf(a,b,c)
}