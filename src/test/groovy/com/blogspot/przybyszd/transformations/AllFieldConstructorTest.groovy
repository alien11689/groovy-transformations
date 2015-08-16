package com.blogspot.przybyszd.transformations

import org.junit.Test

class AllFieldConstructorTest extends GroovyTestCase{
    @Test
    void testSimpleTupleConstructorShouldGenerateConstructor() {
        assertScript '''
            import com.blogspot.przybyszd.transformations.AllFieldConstructor
            @AllFieldConstructor
            class Person {
                private final String firstName
                private final String lastName
                private final boolean male
                String toString(){ "$firstName $lastName $male" }
            }

            assert Person.constructors.size() == 1

            assert new Person('John','Smith', true).toString() == 'John Smith true'
        '''
    }
}
