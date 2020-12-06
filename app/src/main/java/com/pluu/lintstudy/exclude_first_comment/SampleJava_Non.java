package com.pluu.lintstudy.exclude_first_comment;

@SuppressWarnings("unused")
public class SampleJava_Non {
    // aaa
    private final String a1 = "A"; // aakak

    /**
     * comment
     */
    final String a2 = "adasa";

    final String a3 /**ajdadajsd*/ = /**ajdadajsd*/ "A"; // akaka

    final String a4 = "A"; // akaka

    String a5;

    String a6 = null;

    ///////////////////////////////////////////////////////////////////////////
    // aksdkdk
    ///////////////////////////////////////////////////////////////////////////
    String a7 = "";

    public void test() {
        String a = "";
        System.out.println(a);
    }
}
