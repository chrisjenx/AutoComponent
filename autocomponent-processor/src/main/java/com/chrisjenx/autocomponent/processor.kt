package com.chrisjenx.autocomponent

import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.ClassName
import javax.annotation.processing.Processor
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements

internal fun Processor.createGeneratedAnnotation(elements: Elements): AnnotationSpec? {
    val generatedType = elements.getTypeElement("javax.annotation.processing.Generated")
        ?: elements.getTypeElement("javax.annotation.Generated")
        ?: return null
    return AnnotationSpec.builder(generatedType.toClassName())
        .addMember("value", "\$S", javaClass.name)
        .addMember("comments", "\$S", "https://github.com/chrisjenx/AutoComponent")
        .build()
}

internal fun TypeElement.toClassName(): ClassName = ClassName.get(this)
