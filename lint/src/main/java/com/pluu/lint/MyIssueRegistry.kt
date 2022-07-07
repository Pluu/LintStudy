package com.pluu.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.CURRENT_API

@Suppress("UnstableApiUsage", "unused")
class MyIssueRegistry : IssueRegistry() {
    override val api = CURRENT_API
    override val issues = listOf(
        PropertyWithExcludeFirstCommentDetector.ISSUE,
        InnerCheckerOnJavaDetector.ISSUE
    )
}