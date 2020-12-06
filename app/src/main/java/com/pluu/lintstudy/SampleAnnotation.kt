package com.pluu.lintstudy

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS)
annotation class SampleAnnotation(val isEnabled: Boolean = true)
