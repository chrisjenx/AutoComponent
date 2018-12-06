package com.chrisjenx.autocomponent

import com.google.auto.common.BasicAnnotationProcessor
import com.google.auto.service.AutoService
import com.google.common.collect.SetMultimap
import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import dagger.Subcomponent
import java.io.File
import java.util.Observable
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import javax.tools.Diagnostic

@AutoService(Processor::class)
class AutoComponentProcessor : BasicAnnotationProcessor() {

    private lateinit var filer: Filer
    private lateinit var messager: Messager
    private lateinit var types: Types
    private lateinit var options: Map<String, String>

    override fun initSteps(): Iterable<ProcessingStep> {
        filer = processingEnv.filer
        messager = processingEnv.messager
        val elements = processingEnv.elementUtils
        types = processingEnv.typeUtils
        options = processingEnv.options
        return listOf(AutoInjectorGenerator(filer, elements, messager))
    }

    override fun postRound(roundEnv: RoundEnvironment) {
        super.postRound(roundEnv)
        if (roundEnv.processingOver()) {
            messager.printMessage(Diagnostic.Kind.NOTE, "AutoComponent Finished.")
        }
    }

    class AutoInjectorGenerator(
        private val filer: Filer,
        private val elements: Elements,
        private val messager: Messager
    ) : ProcessingStep {
        override fun process(elementsByAnnotation: SetMultimap<Class<out Annotation>, Element>): Set<Element> {
            val injections = elementsByAnnotation
                .values()
                .filterIsInstance(TypeElement::class.java)
                .map {
                    val classPackage = elements.getPackageOf(it)
                    val className = it.simpleName
                    messager.printMessage(Diagnostic.Kind.WARNING, "Generating [inject($classPackage.$className)]")
                    it
                }
                .toSet()
            create(injections)
            // No more processing
            return setOf()
        }

        private fun create(injections: Set<TypeElement>) {
            if (injections.isEmpty()) return
            JavaFile.builder(javaClass.`package`.name, createInterface(injections))
                .skipJavaLangImports(true)
                .build()
                .writeTo(filer)
        }

        private fun createInterface(injections: Set<TypeElement>): TypeSpec {
            return TypeSpec.interfaceBuilder("AutoComponent")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Subcomponent::class.java)
                .addMethods(injections.map(::createInjectMethod))
//                .addType(createBuilder())
                .build()
        }

        private fun createBuilder(): TypeSpec {
            return TypeSpec.interfaceBuilder("Builder")
                .addAnnotation(Subcomponent.Builder::class.java)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT, Modifier.STATIC)
                .addMethod(
                    MethodSpec.methodBuilder("build")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .returns(ClassName.get(javaClass.`package`.name, "AutoComponent"))
                        .build()
                )
                .build()
        }

        private fun createInjectMethod(element: TypeElement): MethodSpec {
            val injectionParam = TypeName.get(element.asType())
            return MethodSpec.methodBuilder("inject")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(Void.TYPE)
                .addParameter(injectionParam, element.simpleName.toString().toLowerCase())
                .build()
        }

        override fun annotations(): Set<Class<out Annotation>> {
            return setOf(Injection::class.java)
        }
    }

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latestSupported()
}

