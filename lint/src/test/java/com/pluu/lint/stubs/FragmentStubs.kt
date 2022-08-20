package com.pluu.lint.stubs

import com.android.tools.lint.checks.infrastructure.TestFiles.java

val FRAGMENT_STUB = java(
    """
    package androidx.fragment.app;

    public class Fragment {
    }
    """
)