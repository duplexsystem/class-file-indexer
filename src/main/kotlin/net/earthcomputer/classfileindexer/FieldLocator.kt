package net.earthcomputer.classfileindexer

import com.intellij.psi.*
import com.intellij.psi.util.PsiUtil

class FieldLocator(
    private val fieldPtr: SmartPsiElementPointer<PsiField>,
    private val isWrite: Boolean,
    location: String,
    index: Int
) : DecompiledSourceElementLocator<PsiElement>(location, index) {
    private var field: PsiField? = null

    override fun findElement(clazz: PsiClass): PsiElement? {
        field = fieldPtr.element ?: return null
        try {
            return super.findElement(clazz)
        } finally {
            field = null
        }
    }

    override fun visitReferenceExpression(expression: PsiReferenceExpression) {
        super.visitReferenceExpression(expression)
        if (PsiUtil.isAccessedForWriting(expression) == isWrite) {
            if (expression.isReferenceTo(field!!)) {
                matchElement(expression)
            }
        }
    }
}
