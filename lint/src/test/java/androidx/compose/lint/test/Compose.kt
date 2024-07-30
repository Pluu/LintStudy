/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("UnstableApiUsage")

package androidx.compose.lint.test

import com.android.tools.lint.checks.infrastructure.LintDetectorTest.kotlin
import com.android.tools.lint.checks.infrastructure.TestFile
import com.android.tools.lint.checks.infrastructure.TestFiles.bytecode
import org.intellij.lang.annotations.Language
import java.util.Locale

object ComposeStubs {
    val Composable: TestFile =
        bytecodeStub(
            filename = "Composable.kt",
            filepath = "androidx/compose/runtime",
            checksum = 0xbcdac3c7,
            source =
            """
        package androidx.compose.runtime

        @MustBeDocumented
        @Retention(AnnotationRetention.BINARY)
        @Target(
            AnnotationTarget.FUNCTION,
            AnnotationTarget.TYPE,
            AnnotationTarget.TYPE_PARAMETER,
            AnnotationTarget.PROPERTY_GETTER
        )
        annotation class Composable
        """,
            """
        META-INF/main.kotlin_module:
        H4sIAAAAAAAA/2NgYGBmYGBgBGJOBijgUuaSTMxLKcrPTKnQS0wuySzLLKnU
        S87PLcgvThViC0ktLvEu4RLl4gYK6aVWJOYW5MCFlRi0GADOEtiIVAAAAA==
        """,
            """
        androidx/compose/runtime/Composable.class:
        H4sIAAAAAAAA/41SW28SQRT+ZoFCUVuwXqC19k6tJm5tfPMJ6FY34ZZl24Tw
        0EzZSbNl2W3YAds3Hkz8Tz4Y4qM/yngGImCyiWaTb7455ztzLnt+/vr2HcB7
        vGHY477TD1znTu8EvdsgFHp/4Eu3J/Ty5M6vPJEEY8jc8CHXPe5f6/WrG9GR
        ScQYtuZW7vuB5NINfL04o0kkGPYr3UB6rr8oqQ5CWRKnQWfQE74UzgeGzQiZ
        JSS5iZE/MeTeQDAcRujmGRcjlkpmrWi1GNYjQmzevxaSVCvc84LPwpkawuh6
        5wlmcamz81rZNus1hrjdahj0kjouG0WrWDVsw2JYbVj1hmHZrcuPhj2x7FQi
        J/bXILajNYudFf4haQSe27lXQytXis2mGm5kwKyZ3Wi/4QlVln1/K9Q8qa1P
        9VNqfdLoeZN6zv4ZVlVI7nDJSaf1hjFaMKZgWQEYWJfsd666HRNz3jHkx6NU
        WstpaS2zkfrxVcuNRyfaMSuNR0pwwnBQ+Y/tpHz0/KO54W1XMqSbwaDfEWeu
        RxuTt6ZRF27okmD+L8MCZUKc4pdUlcSPJvgKr+n8giR9QIr8ywJpPMBDlaqN
        uMAKVhVkFGQVPMYaaZ9MtU/xDM8VbSMmkENeQVbBOjaQwAuym9g08dLEFraJ
        YsfELvbaYCH2cdCGFqIQ4vA3hGOdXq0DAAA=
        """
        )

    val Composables: TestFile =
        bytecodeStub(
            filename = "Composables.kt",
            filepath = "androidx/compose/runtime",
            checksum = 0x8882d27,
            source =
            """
        package androidx.compose.runtime

        @Composable
        inline fun <T> key(
            @Suppress("UNUSED_PARAMETER")
            vararg keys: Any?,
            block: @Composable () -> T
        ) = block()

        @Composable
        inline fun ReusableContent(
            key: Any?,
            content: @Composable () -> Unit
        ) {
            content()
        }

        @Composable
        inline fun ReusableContentHost(
            active: Boolean,
            crossinline content: @Composable () -> Unit
        ) {
            if (active) { content() }
        }
        """,
            """
        META-INF/main.kotlin_module:
        H4sIAAAAAAAA/2NgYGBmYGBgBGJOBijgMuWSSMxLKcrPTKnQS87PLcgvTtUr
        Ks0rycxNFeJ1BgskJuWkFnuXCHEFpeam5ialFnmXcPFxsZSkFpcIsYUASe8S
        JQYtBgAsMUsXXwAAAA==
        """,
            """
        androidx/compose/runtime/ComposablesKt.class:
        H4sIAAAAAAAA/51VzXLbVBT+rvwnK7ajyE5xXEhT103tGCrHpfzUaaBNKfHg
        BiY2gWlW17IaFNtSR5I9ZZdhwzN0yxPArrBgMmHHg/AUDMORLCdO7GkK49H5
        P+d+55wr+c9/fvsdwPv4jGGVmx3bMjovVM3qP7ccXbUHpmv0dXXL13m7pztf
        uDEwBvmQD7na4+aB+mX7UNfIGmIIdfXvGT4v7jcuumuNruX2DFM9HPbVZwNT
        cw3LdNTHgVSplaZTGFobrXvT9s3/UX+j3GrVNmslogw3G2/QKcXdaFj2gXqo
        u22bG1SNm6bl8lHlHcvdGfR6FBWmph0REsPyBAbDdHXb5D21bro2JRuaE0OC
        YVH7Tte6QfZX3OZ9nQIZbhVn9HRmaXpFDmqlvQRSmJeQhMwQafcsrStCOX/0
        jPZjyDBEDXNodXWGTHHGtBO4grfmsIgsg1gwCs8K/jJZnSE9Y94MK5etlGF+
        Vx/4w9yyaBymy3B3VpuXXY09hkf/PW9j7P/aNFxv91Sm8LqN0kKCxce0EV4R
        eZqWP4ypTpRZM0lfCNu2HAotFJ++SY93Lg2b1VJ2FrzRuVFOaUNaOHvKsDDO
        faK7vMNdTnCF/jBEbz/zSNwjoNiuJwjkfGF4UoWkzjrDX8dHa9LxkSTIwogR
        8XhWGD0iPfJcwBc8nitRTE6osKooC7lwllVC1QU5nEsqYYWslcjJT1FBjG6f
        /Cj+8YpRaEmO+eFRWSQer6bF8OvCH1JVYVvKh8XjI1mqXpHncglFFJniH1VJ
        5MdHJkc1tqWpGp4qp05+EGJSRDx5Wa0wr9sq8wahjAc2eaNZiyE18Tm83aU5
        h7esDk15vmGY+s6g39btluf0Slga7+1x2/D0wBhvGgcmdwc2yVd3R9+dujk0
        HIPcD84uJN2bi97T78W5sGTT5Vr3CX8eHCA1rYGt6Y8NT1kKauxN1cc6BIS9
        pSOEJUQQJW2DtCbZvc0vrilzr7BQVtJEQ/fLv2KJ4WfvcuA+0SjNKIU4Nkle
        oYQUYsjhKnkpFRLe9ksvQsE7FPmJnxfDp0GmSPwBPUmBlLh/8zy6hGVcC3Ds
        BjjksnJjAsE3v5xCkIiLkDGPhVMYIv0KAQyZurrpw5CRn4CxMhvG9QkYq7gV
        wGgHMDJjGLmXkCaghPBwVOxvpNkErCzSVOcMVgKlAFYGayj7sDLnYBWnYMWF
        U0gCtnxawyPi35L1XeruvX2E6rhdh1pHBet1VHGnTn/nd/fBHHyAD/cx72DZ
        wUcOIj7NO/jYgehg1cGab7nnQPIFxUH0X0eNH/YRCAAA
        """
        )

    val Modifier: TestFile =
        bytecodeStub(
            filename = "Modifier.kt",
            filepath = "androidx/compose/ui",
            checksum = 0x33b78359,
            source =
            """
        package androidx.compose.ui

        @Suppress("ModifierFactoryExtensionFunction")
        interface Modifier {
            infix fun then(other: Modifier): Modifier =
                if (other === Modifier) this else CombinedModifier(this, other)

            interface Element : Modifier

            companion object : Modifier {
                override infix fun then(other: Modifier): Modifier = other
            }
        }

        class CombinedModifier(
            private val outer: Modifier,
            private val inner: Modifier
        ) : Modifier
        """,
            """
        META-INF/main.kotlin_module:
        H4sIAAAAAAAA/2NgYGBmYGBgBGJOBijgUueSTMxLKcrPTKnQS87PLcgvTtXL
        TSxJLcpMzBHiCk5OTEvLz0nxLuHi5WJOy88XYgtJLS7xLlFi0GIAADDzNLNQ
        AAAA
        """,
            """
        androidx/compose/ui/CombinedModifier.class:
        H4sIAAAAAAAA/6VTWU8TURT+7nRlKFBG2RdBqnRBBhDXGoxiTJoUNECICb7c
        the47XSGzEwbHok/xV+gD0TigyE8+qOM57ZlsZDyYJqe7Z7znXV+//n5C8Ay
        sgwJbpdcR5YOzaJTPXA8YdakuepUC9IWpTWnJHelcCNgDPEyr3PT4vae+aFQ
        FkU/ggDD+E3xl3EhhvAraUt/heF1Mt/JOdv5NbXNMJN33D2zLPyCy6Xtmdy2
        HZ/70iF53fHXa5ZFLYWcmi/cKHSGyYrjW9I2y/WqKW2y2twyc7bvUrQsehHE
        GAaK+6JYaYV/5C6vCnJkmE3m2zvOXrFsKpA9KiuGXvTp6EGcUkvbVqkNhkBS
        PUVwV0cQA1RK5/Zi0DHUBQ3DMRhNaZQh6O9Lj+HhjbHtW8o2/IXNsHLLpFOd
        nxlSnd4T78Qur1l+rnpg0QinGD7/32Zvm8wk7uuYxoxaLTVIu+nPtxa7Jnxe
        4j6nmrVqPUBXzRTpUgQMrEL2Q6m0BZJKiwxbp0cxXRvWmv9oYPj0aElbYG8H
        jHBcGyWJeEDxs69hLR7c6G1qn86+BJVFPz0ik9ZmUthLDLGrk2HoPu9hvuIz
        jG3UbF9WRc6uS08WLPHm8nppc6tOSTD05Wmj67VqQbhbnHwYjLxT5NY2d6XS
        W8ZEO9bF3f4DGsupc1y1uOcJUvVNp+YWxXupIEZaENvXisEiHV+Qphcmbqi7
        JPkxjTBMvIu4oS60zaYRjyBKfJk0QVwNfSxjdJ+gf864QzT9A4PpzDFG0nPH
        GPveCHrSgAsihjj9+jFA2ihJT8k+1YTAOCbUNklqlqIkVYCGZyT3aK0Kzukk
        7lGgquIluWiqxnTmBIlvFwnDDZBmksGmx0WSMB5c602libCWEsBzojpp0+Sw
        iBG8aAAvUTqVkr5WhDC7g0AOyRxSOaSRIRFzOTzC/A6YBxMLOwh5mPAw5EH3
        YHiI/gWbMWV0lgUAAA==
        """,
            """
        androidx/compose/ui/Modifier＄Companion.class:
        H4sIAAAAAAAA/41SXU8TQRQ9s1va7bLIggLlQ/ygQgFlofpgLCHBRuOaUo0Q
        EsPTdDvCtNtZs7tteOTJn+AP8BdIfMBoYgiP/ijj3VJRMEEfeufeM+fce3tm
        v//48g3AA9xnmOWqHgayvu94QettEAmnLZ2NoC7fSBHmy4RxJQOVAWOwG7zD
        HZ+rXedFrSG8OAOdYeqyDhn0MaRXpZLxGoNemN+2kIFhIoUsQyrekxFDofJ/
        S5S6CqEY1i6XlOYvv2aYqQThrtMQcS3kUkUOVyqIeUxDIqcaxNW27xOrL6Bx
        oQGbYboZxL5UTqPTcqSKRai477gqDkktvSiDYYYRb094zZ78JQ95SxCRYa5Q
        uehc6Q9kM2myW0qsuYYRE1cxSvP+9Q+MVc/v2mpCS7w083m3urm1Xi0/sTAJ
        K0vwFMNQpbf4hoh5ncecpFqro9P7syRkkwAG1iR8XybVMmX1FYaHxweWqeW0
        05+hGyfv9NzxQVFbZo8zhnbyIa3Z2vMhW58gpJi2U8n57OR9KtEXGbJnD8fQ
        /2vzpWZMr1gO6oJhsCKVqLZbNRFu8ZpPyHAl8Li/zUOZ1D1w8lVbxbIlXNWR
        kSRo/fdjMeQv3p4Zf45muUqJsOzzKBJUmptBO/TEU5kMGO+12P6rPVbIxRTZ
        kwazc4mt5I9OGH3DhC5S5STm0dm3cATzkBINdymmu2A/7lG0TglUWV2vB3CF
        miTiVWJrdBqLw0OfMbb48Zw+TfxEP3rK6emTzEaO7pd6vMHuGkCG9QoD42f7
        jXXFtMpXaK+PMPEJ1w+7gI5liibRNOoyT5KV7vQFFOl8RPg07XxjB7qLmy5u
        ubiNGUqRd3EHsztgEeZQ2EFfBCtCLoIRYSCC/RM2YumyXwQAAA==
        """,
            """
        androidx/compose/ui/Modifier＄DefaultImpls.class:
        H4sIAAAAAAAA/6VS308TQRD+9gq9thShVUAE8QcVWqqcP3ir0Zgak4stGjFN
        jCZme13abe92yd224S/SV+OLRhPjs3+UcQ5OQDTVxIebm/lmdnbnm+/b909f
        AGxhi6HCVSfUsrPveDrY05FwhtJp6o7clSIsPRS7fOgbN9jzIxuMYbbPR9zx
        ueo6T9p94RkbKYYJ0xOK4VW5Ma5bbXy2Mj7NsNrQYdfpC9MOuVSRw5XShhup
        yd/WZnvo+1Q1qekxYQYZhpWBNr5UTn8UOFIZESruO64yIZ2WHg2UY5jzesIb
        JMef8pAHggoZ1suN06PWTiA7cZNurdLKI4/pHKZwhmF53AA2ZhmydUpwRU9m
        GE9W6aiylkcRZ7Mo4BxD6U9nqLQtlegcXzXPkL4rlTT3GO7/31ZowvNYzGEB
        F4jckunJiJj9264KjYT7pjC8ww0nzApGKZIdi002NmBgg9ghCVn7MvZuMqz9
        Gy021hnyJwXKMPWzbHNgSJV13REM0zuGe4Mm33vO2z7FMw3iansYtEWYIMWG
        9rjf4qGM4wRcejZURgbCVSMZSYIeHMuN9nA6eySdX8ryrlIirPs8igSFuR09
        DD3xSMYXLCYtWr+1xy1YmMAhQ1lMIo0UqhS9JtSi/0q1mP2Imep7zL2BvfEW
        M5+x8GKj+gFLX1F4FzOK62RtWIXHNinnBgVpapYmaJP8+cM2WMbFg2tWkCGP
        wUnq7HgV9E1bSXBoU/QykBotlKlpEYvkFVCh/23CLWzgzkEzEgjVXnqJlIvL
        Lq64uIpVFyVcc7H2Ayw1Nq9+BAAA
        """,
            """
        androidx/compose/ui/Modifier＄Element＄DefaultImpls.class:
        H4sIAAAAAAAA/6VSXW8SQRQ9A5QFSqVQW62t9aO0haJdm/jGk0FNNqFoWsOL
        vgzLFAZ2Z5rdWdKf5WPjg/HZH2W8C6u2NUESH/Z+nHvu3Zkz9/uPL18BvMQL
        hmOu+oGW/Uvb1f6FDoUdSftE9+W5FEH1jSd8oUz1tTjnkWcc/8ILLTCG1RGf
        cNvjamC/642EayykGTJmKBRDv9ZeZGpzLqtZn19m2G3rYGCPhOkFXKrQ5kpp
        w43UFHe06USeR6wlTYcKcsgx7Iy18aSyRxPflsqIQHHPdpQJqFu6dLECw7o7
        FO44aX/PA+4LIjIc1Nq3r9y8hpzFQwbNereIIlYKWMYdhu15F7CwylCfq9NN
        1SsMn+YL+3+CFrGEuwWsYZ1Eq5qhDBn2F3tIkvZfj1VuJ+KfCMP73HDCUv4k
        TXvIYpOPDRjYOA5ol1KXMo5oRYvXdWCoLnIkC1UGK0kYln/Vj8aUZVq6LxhK
        balEJ/J7IvjAex4hlbZ2udflgYzzBNw6jZSRvnDURIaSoFd/9oxOc7v6e2du
        0IqOUiJoeTwMBaWFMx0Frngr4x9sJiO6f43HMVLIYKZMnt4nizRqlLUIT5Ev
        NSr5K5QOv6HcuMLGZ4JSqJPNUkOWWg4p3phRcQ/3p6NKyGGT6o2EZ5F/Rt9K
        KklmNo3nZB+QX0MZu9SyR34/m6dxe9P8aPq7A9jkd6gr5m59RNrBtoOHDmGP
        HDzGEwdPfwJuilgecwQAAA==
        """,
            """
        androidx/compose/ui/Modifier＄Element.class:
        H4sIAAAAAAAA/42QTU/CQBCG32mVUkAFRAUlnohHC8SbJ+NH0gRiookXTgtd
        zEK7Jd2WcOR3eTCc/VHGaaKJJ8JhZ2efeedj5+v74xPADdqEjtBBEqtg5U3i
        aBEb6WXKG8aBmiqZdB5DGUmdOiBCdSaWwguFfveexzM5YWoT2tvyHewTaoN5
        nIZKe0OZikCk4pZgRUubJ6DcuLkBgebMVyp/ddkLeoTGZl0sWU0rP8Vpc7Pu
        W13KY33C1WCXybnX5VYhC5xfLaG3S8nOg5yKLEz9aBEaByeEyn9CKP9lXM+5
        ZsXXWib3oTBGcrD0GmfJRD6pUBJaL5lOVSTflFHjUN5pHaciVbE2Bf4l9ngr
        hXw5sNFiW2dmMWkUXJyyd8b0nLmFJi747rGeV47iCLYP10fJR5kHgIsDH4c4
        GoEMqqiNYBnUDY5/AJ5HRJ4KAgAA
        """,
            """
        androidx/compose/ui/Modifier.class:
        H4sIAAAAAAAA/4VRTW/TQBB94zhxkgZwKB/9orQ0lIaPulRwoVCpCkUYpQFR
        1EtOm2RbtnXWlXcT9Zhfwf8AbhxQxJEfhRhHhVKQgi3vm3m7+9545vuPL18B
        PMIqYU7oThKrzknQjrvHsZFBTwU7cUftK5l4IIJ/KPoiiIQ+CF63DmXbesgQ
        XPteasLmSn2cwEZ1/DZhqR4nB8GhtK1EKG0CoXVshVUxx43YNnpRxKfyT9uR
        0spuEjIr1T3C8jjZSo05oVnDQ5FQrFTCxu67rUZtmzC+3rObGyWUcKGACVwk
        FH7TJfgp66BMKNePYstlBTvSio6wggt1uv0Mt5bSpZAuINAR8ycqzdY46jwk
        PB4OSkVnyik6/nBQdPJufn9qOFhw1501euK42Vdl35lx1oaD9ZyfGQUvv31w
        08vrhPn/9ZQ9szHPJyF425HsSm0J1bH//Vzui15kw+5xZDzcIpT+ZAiVsbdP
        TTzcJkz8YleP2HX2bU9b1ZWh7iujWpHcOpsvq/69+0YkoiutTM4dc2txR3JJ
        odYyqUXCGMlscTfuJW35QkW8N32qtPePS467Bnc0iOl0eIwrnOUYPUaHu5rl
        zDnHVvnz6DTJ81vg+C7Hk4zp433Cpc+4/HGUZHCP1xnGCdbx2WeJscK4zLiY
        K+B+6oQ7eMD4jCUm2fNKE5kQV0NcC3EdUxxiOmSV2SbIYA43msgZzBvcNMga
        LBj4Bos/AU5gqBi8AwAA
        """
        )
}

/**
 * Utility for creating a [kotlin] and corresponding [bytecode] stub, to try and make it easier to
 * configure everything correctly.
 *
 * @param filename name of the Kotlin source file, with extension - e.g. "Test.kt". These should be
 *   unique across a test.
 * @param filepath directory structure matching the package name of the Kotlin source file. E.g. if
 *   the source has `package foo.bar`, this should be `foo/bar`. If this does _not_ match, lint will
 *   not be able to match the generated classes with the source file, and so won't print them to
 *   console.
 * @param source Kotlin source for the bytecode
 * @param bytecode generated bytecode that will be used in tests. Leave empty to generate the
 *   bytecode for [source].
 * @return a pair of kotlin test file, to bytecode test file
 */
fun kotlinAndBytecodeStub(
    filename: String,
    filepath: String,
    checksum: Long,
    @Language("kotlin") source: String,
    vararg bytecode: String
): KotlinAndBytecodeStub {
    val filenameWithoutExtension = filename.substringBefore(".").lowercase(Locale.ROOT)
    val kotlin = kotlin(source).to("$filepath/$filename")
    val bytecodeStub = bytecode("libs/$filenameWithoutExtension.jar", kotlin, checksum, *bytecode)
    return KotlinAndBytecodeStub(kotlin, bytecodeStub)
}

class KotlinAndBytecodeStub(val kotlin: TestFile, val bytecode: TestFile)

/**
 * Utility for creating a [bytecode] stub, to try and make it easier to configure everything
 * correctly.
 *
 * @param filename name of the Kotlin source file, with extension - e.g. "Test.kt". These should be
 *   unique across a test.
 * @param filepath directory structure matching the package name of the Kotlin source file. E.g. if
 *   the source has `package foo.bar`, this should be `foo/bar`. If this does _not_ match, lint will
 *   not be able to match the generated classes with the source file, and so won't print them to
 *   console.
 * @param source Kotlin source for the bytecode
 * @param bytecode generated bytecode that will be used in tests. Leave empty to generate the
 *   bytecode for [source].
 */
fun bytecodeStub(
    filename: String,
    filepath: String,
    checksum: Long,
    @Language("kotlin") source: String,
    vararg bytecode: String
): TestFile = kotlinAndBytecodeStub(filename, filepath, checksum, source, *bytecode).bytecode