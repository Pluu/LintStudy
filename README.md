# Android Studio 에서 동작하는 Lint 공부하는 레포

## Sample 1

1. Java/Kotlin 파일에 존재하는 Local Property/Field를 Detect
2. 필드 정의보다 앞에 존재하는 Document, Comment는 제외

> https://github.com/Pluu/LintStudy/blob/master/lint/src/main/java/com/pluu/lint/PropertyWithExcludeFirstCommentDetector.kt

### Preview

|               Kotlin                |               Java                |
| :---------------------------------: | :-------------------------------: |
| <img src="arts/efc_kotlin_1.png" /> | <img src="arts/efc_java_1.png" /> |
| <img src="arts/efc_kotlin_2.png" /> | <img src="arts/efc_java_2.png" /> |

