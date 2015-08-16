package com.blogspot.przybyszd.transformations

import org.codehaus.groovy.transform.GroovyASTTransformationClass

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@Retention(RetentionPolicy.SOURCE)
@Target([ElementType.TYPE])
@GroovyASTTransformationClass("com.blogspot.przybyszd.transformations.AllFieldConstructorASTTransformation")
@interface AllFieldConstructor {}
