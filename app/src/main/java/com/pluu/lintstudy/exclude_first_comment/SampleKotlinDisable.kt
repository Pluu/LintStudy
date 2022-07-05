package com.pluu.lintstudy.exclude_first_comment

import com.pluu.lintstudy.SampleAnnotation

@Suppress("unused")
@SampleAnnotation(isEnabled = false)
class SampleKotlinDisable {
    private val a1 = "A" // aakak

    // comment
    private val a2 = "A" // aakak

    /**
     * comment
     */
    private val a3 = "A" // aakak

    val b1 = "adasa"

    // comment
    val b2 = "adasa"

    /**
     * comment
     */
    val b3 = "adasa"
 
    internal val c1 = "A" // aakak

    // comment
    internal val c2 = "A" // aakak

    /**
     * comment
     */
    internal val c3 = "A" // aakak

    ///////////////////////////////////////////////////////////////////////////
    // Complex
    ///////////////////////////////////////////////////////////////////////////

    // Multiple Comment
    val d1 /**ajdadajsd*/ = /**ajdadajsd*/ "A" // akaka

    // Expression
    val d2 = if (a2.length == 3) {
        "1"
    } else {
        "2"
    }

    // Nullable Variable
    var d3: String? = null

    ///////////////////////////////////////////////////////////////////////////
    // Comment Block
    ///////////////////////////////////////////////////////////////////////////
    var d4 = ""

    // kotlin.NotImplementedError: An operation is not implemented: Not yet implemented
    // Late Init
//    lateinit var d5: String

    fun test() {
        val a = ""
        println(a)
    }

    companion object {
        private val a1 = "A" // aakak

        // comment
        private val a2 = "A" // aakak

        /**
         * comment
         */
        private val a3 = "A" // aakak

        val b1 = "adasa"

        // comment
        val b2 = "adasa"

        /**
         * comment
         */
        val b3 = "adasa"

        internal val c1 = "A" // aakak

        // comment
        internal val c2 = "A" // aakak

        /**
         * comment
         */
        internal val c3 = "A" // aakak

        // Complex

        // Multiple Comment
        val d1 /**ajdadajsd*/ = /**ajdadajsd*/ "A" // akaka

        // Expression
        val d2 = if (a2.length == 3) {
            "1"
        } else {
            "2"
        }

        // Nullable Variable
        var d3: String? = null

        ///////////////////////////////////////////////////////////////////////////
        // Comment Block
        ///////////////////////////////////////////////////////////////////////////
        var d4 = ""

        // Late Init
        lateinit var d5: String
    }
}