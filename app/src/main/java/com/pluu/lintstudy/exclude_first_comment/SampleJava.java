package com.pluu.lintstudy.exclude_first_comment;

import com.pluu.lintstudy.SampleAnnotation;

@SuppressWarnings("unused")
@SampleAnnotation
public class SampleJava {
    private final String a1 = "A"; // aakak

    // comment
    private final String a2 = "A"; // aakak

    /**
     * comment
     */
    private final String a3 = "A"; // aakak

    final String b1 = "A"; // aakak

    // comment
    final String b2 = "A"; // aakak

    /**
     * comment
     */
    final String b3 = "A"; // aakak

    ///////////////////////////////////////////////////////////////////////////
    // Complex
    ///////////////////////////////////////////////////////////////////////////

    // Multiple Comment
    final String d1 /**ajdadajsd*/ = /**ajdadajsd*/ "A"; // akaka

    // Nullable Variable
    String d2;

    String d3 = null;

    String d4 = "";

    ///////////////////////////////////////////////////////////////////////////
    // aksdkdk
    ///////////////////////////////////////////////////////////////////////////
    String d5 = "";

    public void test() {
        String a = "";
        System.out.println(a);
    }
}
