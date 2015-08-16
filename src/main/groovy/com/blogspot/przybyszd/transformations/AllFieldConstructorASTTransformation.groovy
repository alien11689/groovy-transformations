package com.blogspot.przybyszd.transformations

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.AnnotatedNode
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.ConstructorNode
import org.codehaus.groovy.ast.FieldNode
import org.codehaus.groovy.ast.Parameter
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.AbstractASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

import static org.codehaus.groovy.ast.ClassHelper.make
import static org.codehaus.groovy.ast.tools.GeneralUtils.assignS
import static org.codehaus.groovy.ast.tools.GeneralUtils.getInstanceNonPropertyFields
import static org.codehaus.groovy.ast.tools.GeneralUtils.propX
import static org.codehaus.groovy.ast.tools.GeneralUtils.varX

/**
 * Based on groovy.transform.TupleConstructor but generated only constructor with all fields
 */
@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
class AllFieldConstructorASTTransformation extends AbstractASTTransformation {
    static final Class MY_CLASS = AllFieldConstructor
    static final ClassNode MY_TYPE = make(MY_CLASS)
    static final String MY_TYPE_NAME = "@" + MY_TYPE.getNameWithoutPackage()

    public void visit(ASTNode[] nodes, SourceUnit source) {
        init(nodes, source)
        AnnotatedNode parent = nodes[1] as AnnotatedNode
        AnnotationNode anno = nodes[0] as AnnotationNode
        if (MY_TYPE != anno.classNode) {
            return
        }

        if (parent instanceof ClassNode) {
            ClassNode cNode = parent as ClassNode
            if (!checkNotInterface(cNode, MY_TYPE_NAME)) {
                return
            }
            createConstructor(cNode)
        }
    }

    public static void createConstructor(ClassNode cNode) {
        List<ConstructorNode> constructors = cNode.declaredConstructors
        if (constructors) {
            throw new VerifyError('Class already has a constructor')
        }

        List<FieldNode> list = getInstanceNonPropertyFields(cNode)

        final List<Parameter> params = []
        final BlockStatement body = new BlockStatement()

        for (FieldNode fNode in list) {
            if (fNode.type.name == 'groovy.lang.MetaClass') {
                continue
            }
            String name = fNode.name
            Parameter nextParam = new Parameter(fNode.type, name)
            params.add(nextParam)
            body.addStatement(assignS(propX(varX("this"), name), varX(nextParam)))
        }
        cNode.addConstructor(new ConstructorNode(ACC_PUBLIC, params.toArray(new Parameter[params.size()]), ClassNode.EMPTY_ARRAY, body))
    }

}
