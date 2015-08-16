package com.blogspot.przybyszd.transformations

import org.junit.Test

class TupleConstructorTest extends GroovyTestCase{
    @Test
    void testSimpleTupleConstructorShouldGenerateConstructor() {
        assertScript '''
            import groovy.transform.TupleConstructor
            @TupleConstructor(includeFields = true)
            class Person {
                private final String firstName
                private final String lastName
                private final boolean male
                String toString(){ "$firstName $lastName $male" }
            }

            assert Person.constructors.size() == 4

            assert new Person().toString() == 'null null false'
            assert new Person('John').toString() == 'John null false'
            assert new Person('John','Smith').toString() == 'John Smith false'
            assert new Person('John','Smith', true).toString() == 'John Smith true'
        '''
    }
}
