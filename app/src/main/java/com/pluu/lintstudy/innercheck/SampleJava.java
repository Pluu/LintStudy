package com.pluu.lintstudy.innercheck;

import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
public class SampleJava {

    private InnerClass innerClass;

    private InnerEnum innerEnum;

    private OtherType otherType;

    ///////////////////////////////////////////////////////////////////////////
    // List
    ///////////////////////////////////////////////////////////////////////////

    private List<InnerClass> a1;

    private List<InnerEnum> a2;

    private List<OtherType> a3;

    ///////////////////////////////////////////////////////////////////////////
    // Set
    ///////////////////////////////////////////////////////////////////////////

    private Set<InnerClass> b1;

    private Set<InnerEnum> b2;

    private Set<OtherType> b3;

    ///////////////////////////////////////////////////////////////////////////
    // Map
    ///////////////////////////////////////////////////////////////////////////

    private Map<String, InnerClass> c1;

    private Map<String, InnerEnum> c2;

    private Map<String, OtherType> c3;

    public class InnerClass {
    }

    public enum InnerEnum {
        A
    }
}
