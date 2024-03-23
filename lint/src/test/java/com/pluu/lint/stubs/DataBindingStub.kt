package com.pluu.lint.stubs

import com.android.tools.lint.checks.infrastructure.TestFiles.java

object DataBindingStub {

    val BindingAdapterStub = java(
        """
package androidx.databinding;
public @interface BindingAdapter {
    String[] value();
    boolean requireAll() default true;
}
        """.trimIndent()
    )

}