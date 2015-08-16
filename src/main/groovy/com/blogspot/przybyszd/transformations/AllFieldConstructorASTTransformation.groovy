package com.blogspot.przybyszd.transformations

import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.AbstractASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

import static org.codehaus.groovy.ast.ClassHelper.make
import static org.codehaus.groovy.ast.tools.GeneralUtils.*

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
        AnnotatedNode parent = (AnnotatedNode) nodes[1]
        AnnotationNode anno = (AnnotationNode) nodes[0]
        if (!MY_TYPE.equals(anno.getClassNode())) return

        if (parent instanceof ClassNode) {
            ClassNode cNode = (ClassNode) parent
            if (!checkNotInterface(cNode, MY_TYPE_NAME)) return
            createConstructor(cNode)
        }
    }

    public static void createConstructor(ClassNode cNode) {
        List<ConstructorNode> constructors = cNode.getDeclaredConstructors()
        if (constructors.size() > 0) {throw new VerifyError('Class already has a constructor')}

        List<FieldNode> list = getInstanceNonPropertyFields(cNode)

        final List<Parameter> params = new ArrayList<Parameter>()
        final BlockStatement body = new BlockStatement()

        for (FieldNode fNode : list) {
            String name = fNode.getName()
            Parameter nextParam = new Parameter(fNode.getType(), name)
            params.add(nextParam)
            body.addStatement(assignS(propX(varX("this"), name), varX(nextParam)))
        }
        cNode.addConstructor(new ConstructorNode(ACC_PUBLIC, params.toArray(new Parameter[params.size()]), ClassNode.EMPTY_ARRAY, body))
    }

}
