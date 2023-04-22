package com.pluu.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.CURRENT_API

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
    )
}