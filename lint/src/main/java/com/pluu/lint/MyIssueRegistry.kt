package com.pluu.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.CURRENT_API
import com.pluu.lint.compose.RequiredModifierParameterDetector

@Suppress("unused")
class MyIssueRegistry : IssueRegistry() {
    override val api = CURRENT_API
    override val issues = listOf(
        PropertyWithExcludeFirstCommentDetector.ISSUE,
        InnerCheckerOnJavaDetector.ISSUE,
        FindMethodWithArgumentDetector.ISSUE,
        SafeUseForTypedArrayDetector.ISSUE,
        PreferencesKeyDetector.ISSUE,
        ViewModelScopeLaunchDetector.ISSUE,
        RequiredCustomViewAttributeDetector.ISSUE,
        TypoMethodInComponentDetector.ISSUE,
        LiveDataObserveNotNullDetector.ISSUE,
        LazyBundleDetector.ISSUE,
        DataBindingDuplicationDetector.ISSUE,
        TwoMoreBlankLineDetector.ISSUE,
        RequiredModifierParameterDetector.ISSUE,
        OnCreateSuperCallDetector.ISSUE,
    )
}