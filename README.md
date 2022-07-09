# Android Studio 에서 동작하는 Lint 공부하는 레포

## Debug

UAST Tree 검사

1. Help | Edit Custom Properties
   1. idea.is.internal=true
   2. Restart IDE

2. Tools | Internal Actions | UAST | Dump UAST Tree (By Each PsiElement)

> Enabling Internal Mode : https://plugins.jetbrains.com/docs/intellij/enabling-internal.html
>
> Inspecting UAST Tree﻿ : https://plugins.jetbrains.com/docs/intellij/uast.html#inspecting-uast-tree

## Sample

- [Sample, exclude first comment](exclude_first_comment.md)
