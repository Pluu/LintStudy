package com.pluu.lint

import com.android.resources.ResourceFolderType
import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Context
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Location
import com.android.tools.lint.detector.api.ResourceXmlDetector
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.android.tools.lint.detector.api.XmlContext
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UFile

class TwoMoreBlankLineDetector : ResourceXmlDetector(), SourceCodeScanner {

    private val regex = "^(\\s*\\n){2,}".toRegex(RegexOption.MULTILINE)

    override fun getApplicableUastTypes(): List<Class<out UElement>> {
        return listOf(UFile::class.java)
    }

    override fun appliesTo(folderType: ResourceFolderType): Boolean {
        return folderType == ResourceFolderType.LAYOUT
    }

    override fun afterCheckFile(context: Context) {
        val xmlContext = context as? XmlContext ?: return
        val xml = xmlContext.getContents() ?: return
        findBlankLines(xml).forEach {
            val location = Location.create(
                context.file,
                xml,
                it.range.first,
                it.range.last
            )
            report(context, location)
        }
    }

    override fun createUastHandler(context: JavaContext): UElementHandler {
        return object : UElementHandler() {
            private val internalContext = context

            override fun visitFile(node: UFile) {
                findBlankLines(node.sourcePsi.fileDocument.text).forEach {
                    val location = internalContext.getRangeLocation(
                        node,
                        it.range.first,
                        it.range.last - it.range.first
                    )
                    report(internalContext, location)
                }
            }
        }
    }

    private fun findBlankLines(element: CharSequence): Sequence<MatchResult> {
        return regex.findAll(element)
    }

    private fun report(context: Context, location: Location) {
        context.report(
            ISSUE,
            location,
            message,
            fix().name("1줄로 바꾸기")
                .replace()
                .with("\n")
                .reformat(true)
                .build()
        )
    }

    companion object {
        private const val message = "2줄 이상의 공백이 탐지됨"

        val ISSUE = Issue.create(
            id = "ExtraBlankLines",
            briefDescription = message,
            explanation = message,
            category = Category.CORRECTNESS,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                TwoMoreBlankLineDetector::class.java,
                Scope.JAVA_AND_RESOURCE_FILES,
                Scope.JAVA_FILE_SCOPE,
                Scope.RESOURCE_FILE_SCOPE
            )
        )
    }
}