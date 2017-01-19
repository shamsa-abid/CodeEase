package yoshikihigo.jmc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BlockComment;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.CreationReference;
import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.ExpressionMethodReference;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.IntersectionType;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.LambdaExpression;
import org.eclipse.jdt.core.dom.LineComment;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MemberRef;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.MethodRef;
import org.eclipse.jdt.core.dom.MethodRefParameter;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.NameQualifiedType;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.SuperMethodReference;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TextElement;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.TypeMethodReference;
import org.eclipse.jdt.core.dom.TypeParameter;
import org.eclipse.jdt.core.dom.UnionType;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.core.dom.WildcardType;

import yoshikihigo.jmc.data.JMethod;
import yoshikihigo.jmc.data.JStatement;
import yoshikihigo.jmc.data.JavaKeyword;

public class JMCVisitor extends ASTVisitor implements JavaKeyword {

	final private String file;
	final private CompilationUnit root;
	final private Stack<JMethod> methodsStack;
	final private Stack<JStatement> statementsStack;
	final private Stack<Map<String, String>> variablesStack;
	final private List<JMethod> methods;

	JMCVisitor(final String file, final CompilationUnit root) {
		this.file = file;
		this.root = root;
		this.methodsStack = new Stack<>();
		this.statementsStack = new Stack<>();
		this.variablesStack = new Stack<>();
		this.methods = new ArrayList<>();
		this.methodsStack.push(new JMethod(file, -1, -1));
		if (JMCConfig.getInstance().isDEBUG()) {
			System.out.println(file);
		}
	}

	List<JMethod> getMethods() {
		return new ArrayList<JMethod>(this.methods);
	}

	@Override
	public boolean visit(final AnnotationTypeDeclaration node) {
		return super.visit(node);
	}

	@Override
	public boolean visit(final AnnotationTypeMemberDeclaration node) {
		return super.visit(node);
	}

	@Override
	public boolean visit(final AnonymousClassDeclaration node) {
		return super.visit(node);
	}

	@Override
	public boolean visit(final ArrayAccess node) {

		if (!this.statementsStack.isEmpty()) {
			node.getArray().accept(this);
			this.statementsStack.peek().addToken(LEFTSQUAREBRACKET);
			node.getIndex().accept(this);
			this.statementsStack.peek().addToken(RIGHTSQUAREBRACKET);
		}

		return false;
	}

	@Override
	public boolean visit(final ArrayCreation node) {

		if (!this.statementsStack.isEmpty()) {
			this.statementsStack.peek().addToken(NEW);

			if (null == node.getInitializer()) {
				node.getType().getElementType().accept(this);
				for (final Object o : node.dimensions()) {
					this.statementsStack.peek().addToken(LEFTSQUAREBRACKET);
					((ASTNode) o).accept(this);
					this.statementsStack.peek().addToken(RIGHTSQUAREBRACKET);
				}
			}

			else {
				node.getType().accept(this);
				node.getInitializer().accept(this);
			}
		}

		return false;
	}

	@Override
	public boolean visit(final ArrayInitializer node) {

		if (!this.statementsStack.isEmpty()) {
			this.statementsStack.peek().addToken(LEFTBRACKET);
			this.addTokens(this.statementsStack.peek(), node.expressions());
			this.statementsStack.peek().addToken(RIGHTBRACKET);
		}

		return false;
	}

	@Override
	public boolean visit(final ArrayType node) {

		if (!this.statementsStack.isEmpty()) {
			node.getElementType().accept(this);
			for (int i = 0; i < node.getDimensions(); i++) {
				this.statementsStack.peek().addToken(LEFTSQUAREBRACKET);
				this.statementsStack.peek().addToken(RIGHTSQUAREBRACKET);
			}
		}

		return false;
	}

	@Override
	public boolean visit(final AssertStatement node) {

		final JMethod method = this.methodsStack.peek();
		final int line = this.getFromLineNumber(node);
		final JStatement statement = new JStatement(method.id, line);
		this.statementsStack.push(statement);
		this.variablesStack.push(new HashMap<String, String>());

		statement.addToken(ASSERT);
		node.getExpression().accept(this);
		statement.addToken(COLON);
		node.getMessage().accept(this);
		statement.addToken(SEMICOLON);

		this.statementsStack.pop();
		this.variablesStack.pop();
		method.addStatement(statement);
		if (JMCConfig.getInstance().isDEBUG()) {
			this.print("assert statement", statement);
		}
		return false;
	}

	@Override
	public boolean visit(final Assignment node) {

		if (!this.statementsStack.isEmpty()) {
			node.getLeftHandSide().accept(this);
			final Assignment.Operator operator = node.getOperator();
			this.statementsStack.peek().addToken(operator.toString());
			node.getRightHandSide().accept(this);
		}

		return false;
	}

	@Override
	public boolean visit(final Block node) {
		return super.visit(node);
	}

	@Override
	public boolean visit(final BlockComment node) {
		return super.visit(node);
	}

	@Override
	public boolean visit(final BooleanLiteral node) {

		if (!this.statementsStack.isEmpty()) {
			this.statementsStack.peek().addToken(node.toString());
		}

		return false;
	}

	@Override
	public boolean visit(final BreakStatement node) {

		final JMethod method = this.methodsStack.peek();
		final int line = getFromLineNumber(node);
		final JStatement statement = new JStatement(method.id, line);
		this.statementsStack.push(statement);
		this.variablesStack.push(new HashMap<String, String>());

		statement.addToken(BREAK);
		if (null != node.getLabel()) {
			node.getLabel().accept(this);
		}
		statement.addToken(SEMICOLON);

		this.statementsStack.pop();
		this.variablesStack.pop();
		method.addStatement(statement);
		if (JMCConfig.getInstance().isDEBUG()) {
			this.print("break statement", statement);
		}
		return false;
	}

	@Override
	public boolean visit(final CastExpression node) {

		if (!this.statementsStack.isEmpty()) {
			this.statementsStack.peek().addToken(LEFTPAREN);
			node.getType().accept(this);
			this.statementsStack.peek().addToken(RIGHTPAREN);
			node.getExpression().accept(this);
		}

		return false;
	}

	@Override
	public boolean visit(final CatchClause node) {

		final JMethod method = this.methodsStack.peek();
		final int line = getFromLineNumber(node);
		final JStatement statement = new JStatement(method.id, line);
		this.statementsStack.push(statement);
		this.variablesStack.push(new HashMap<String, String>());

		statement.addToken(CATCH);
		statement.addToken(LEFTPAREN);
		node.getException().accept(this);
		statement.addToken(RIGHTPAREN);

		this.statementsStack.pop();
		this.variablesStack.pop();
		method.addStatement(statement);
		if (JMCConfig.getInstance().isDEBUG()) {
			this.print("catch clause", statement);
		}

		node.getBody().accept(this);

		return false;
	}

	@Override
	public boolean visit(final CharacterLiteral node) {

		if (!this.statementsStack.isEmpty()) {
			this.statementsStack.peek().addToken("$CHAR");
		}

		return false;
	}

	@Override
	public boolean visit(final ClassInstanceCreation node) {

		if (!this.statementsStack.isEmpty()) {
			this.statementsStack.peek().addToken(NEW);
			node.getType().accept(this);
			this.statementsStack.peek().addToken(LEFTPAREN);
			this.addTokens(this.statementsStack.peek(), node.arguments());
			this.statementsStack.peek().addToken(RIGHTPAREN);
		}

		return false;
	}

	@Override
	public boolean visit(final CompilationUnit node) {
		return super.visit(node);
	}

	@Override
	public boolean visit(final ConditionalExpression node) {

		if (!this.statementsStack.isEmpty()) {
			node.getExpression().accept(this);
			this.statementsStack.peek().addToken(HATENA);
			node.getThenExpression().accept(this);
			this.statementsStack.peek().addToken(COLON);
			node.getElseExpression().accept(this);
		}

		return false;
	}

	@Override
	public boolean visit(final ConstructorInvocation node) {

		final JMethod method = this.methodsStack.peek();
		final int line = getFromLineNumber(node);
		final JStatement statement = new JStatement(method.id, line);
		this.statementsStack.push(statement);
		this.variablesStack.push(new HashMap<String, String>());

		this.statementsStack.peek().addToken(THIS);
		this.statementsStack.peek().addToken(LEFTPAREN);
		this.addTokens(this.statementsStack.peek(), node.arguments());
		this.statementsStack.peek().addToken(RIGHTPAREN);
		this.statementsStack.peek().addToken(SEMICOLON);

		this.statementsStack.pop();
		this.variablesStack.pop();
		method.addStatement(statement);
		if (JMCConfig.getInstance().isDEBUG()) {
			this.print("this invocation", statement);
		}
		return false;
	}

	@Override
	public boolean visit(final ContinueStatement node) {

		final JMethod method = this.methodsStack.peek();
		final int line = getFromLineNumber(node);
		final JStatement statement = new JStatement(method.id, line);
		this.statementsStack.push(statement);
		this.variablesStack.push(new HashMap<String, String>());

		statement.addToken(CONTINUE);
		if (null != node.getLabel()) {
			node.getLabel().accept(this);
		}
		statement.addToken(SEMICOLON);

		this.statementsStack.pop();
		this.variablesStack.pop();
		method.addStatement(statement);
		if (JMCConfig.getInstance().isDEBUG()) {
			this.print("continue statement", statement);
		}
		return false;
	}

	@Override
	public boolean visit(final CreationReference node) {
		return super.visit(node);
	}

	@Override
	public boolean visit(final Dimension node) {
		return super.visit(node);
	}

	@Override
	public boolean visit(final DoStatement node) {

		if (null != node.getBody()) {
			node.getBody().accept(this);
		}

		final JMethod method = this.methodsStack.peek();
		final int line = getFromLineNumber(node.getExpression());
		final JStatement statement = new JStatement(method.id, line);
		this.statementsStack.push(statement);
		this.variablesStack.push(new HashMap<String, String>());

		node.getExpression().accept(this);

		this.statementsStack.pop();
		this.variablesStack.pop();
		method.addStatement(statement);
		if (JMCConfig.getInstance().isDEBUG()) {
			this.print("do conditional expression", statement);
		}
		return false;
	}

	@Override
	public boolean visit(final EmptyStatement node) {

		final JMethod method = this.methodsStack.peek();
		final int line = getFromLineNumber(node);
		final JStatement statement = new JStatement(method.id, line);
		statement.addToken(SEMICOLON);
		method.addStatement(statement);
		return false;
	}

	@Override
	public boolean visit(final EnhancedForStatement node) {

		final JMethod method = this.methodsStack.peek();
		final int line = getFromLineNumber(node);
		final JStatement statement = new JStatement(method.id, line);
		this.statementsStack.push(statement);
		this.variablesStack.push(new HashMap<String, String>());

		node.getParameter().accept(this);
		statement.addToken(COLON);
		node.getExpression().accept(this);

		this.statementsStack.pop();
		this.variablesStack.pop();
		method.addStatement(statement);
		if (JMCConfig.getInstance().isDEBUG()) {
			this.print("foreach expression", statement);
		}

		node.getBody().accept(this);

		return false;
	}

	@Override
	public boolean visit(final EnumConstantDeclaration node) {
		return super.visit(node);
	}

	@Override
	public boolean visit(final EnumDeclaration node) {
		return super.visit(node);
	}

	@Override
	public boolean visit(final ExpressionMethodReference node) {
		return super.visit(node);
	}

	@Override
	public boolean visit(final ExpressionStatement node) {

		final JMethod method = this.methodsStack.peek();
		final int line = getFromLineNumber(node);
		final JStatement statement = new JStatement(method.id, line);
		this.statementsStack.push(statement);
		this.variablesStack.push(new HashMap<String, String>());

		node.getExpression().accept(this);
		statement.addToken(SEMICOLON);

		this.statementsStack.pop();
		this.variablesStack.pop();
		method.addStatement(statement);
		if (JMCConfig.getInstance().isDEBUG()) {
			this.print("expression statement", statement);
		}
		return false;
	}

	@Override
	public boolean visit(final FieldAccess node) {

		if (!this.statementsStack.isEmpty()) {
			node.getExpression().accept(this);
			this.statementsStack.peek().addToken(DOT);
			node.getName().accept(this);
		}

		return false;
	}

	@Override
	public boolean visit(final FieldDeclaration node) {
		return super.visit(node);
	}

	@Override
	public boolean visit(final ForStatement node) {

		final JMethod method = this.methodsStack.peek();

		for (final Object o : node.initializers()) {
			final ASTNode n = (ASTNode) o;
			final int line = getFromLineNumber(node);
			final JStatement statement = new JStatement(method.id, line);
			this.statementsStack.push(statement);
			this.variablesStack.push(new HashMap<String, String>());

			n.accept(this);

			this.statementsStack.pop();
			this.variablesStack.pop();
			method.addStatement(statement);
		}

		if (null != node.getExpression()) {
			final int line = getFromLineNumber(node.getExpression());
			final JStatement statement = new JStatement(method.id, line);
			this.statementsStack.push(statement);
			this.variablesStack.push(new HashMap<String, String>());

			node.getExpression().accept(this);

			this.statementsStack.pop();
			this.variablesStack.pop();
			method.addStatement(statement);
			if (JMCConfig.getInstance().isDEBUG()) {
				this.print("for conditional expression", statement);
			}
		}

		for (final Object o : node.updaters()) {
			final ASTNode n = (ASTNode) o;
			final int line = getFromLineNumber(node);
			final JStatement statement = new JStatement(method.id, line);
			this.statementsStack.push(statement);
			this.variablesStack.push(new HashMap<String, String>());

			n.accept(this);

			this.statementsStack.pop();
			this.variablesStack.pop();
			method.addStatement(statement);
		}

		if (null != node.getBody()) {
			node.getBody().accept(this);
		}

		return false;
	}

	@Override
	public boolean visit(final IfStatement node) {

		final JMethod method = this.methodsStack.peek();
		final int line = getFromLineNumber(node.getExpression());
		final JStatement statement = new JStatement(method.id, line);
		this.statementsStack.push(statement);
		this.variablesStack.push(new HashMap<String, String>());

		node.getExpression().accept(this);

		this.statementsStack.pop();
		this.variablesStack.pop();
		method.addStatement(statement);
		if (JMCConfig.getInstance().isDEBUG()) {
			this.print("if conditional expression", statement);
		}

		node.getThenStatement().accept(this);
		if (null != node.getElseStatement()) {
			node.getElseStatement().accept(this);
		}

		return false;
	}

	@Override
	public boolean visit(final ImportDeclaration node) {
		return super.visit(node);
	}

	@Override
	public boolean visit(final InfixExpression node) {

		if (!this.statementsStack.isEmpty()) {
			node.getLeftOperand().accept(this);

			final Operator operator = node.getOperator();
			this.statementsStack.peek().addToken(operator.toString());

			node.getRightOperand().accept(this);
		}

		return false;
	}

	@Override
	public boolean visit(final Initializer node) {
		return super.visit(node);
	}

	@Override
	public boolean visit(final InstanceofExpression node) {

		if (!this.statementsStack.isEmpty()) {
			node.getLeftOperand().accept(this);
			this.statementsStack.peek().addToken(INSTANCEOF);
			node.getRightOperand().accept(this);
		}

		return false;
	}

	@Override
	public boolean visit(final IntersectionType node) {
		return super.visit(node);
	}

	@Override
	public boolean visit(final Javadoc node) {
		return super.visit(node);
	}

	@Override
	public boolean visit(final LabeledStatement node) {
		return super.visit(node);
	}

	@Override
	public boolean visit(final LambdaExpression node) {

		if (!this.statementsStack.isEmpty()) {
			if (node.hasParentheses()) {
				this.statementsStack.peek().addToken(LEFTPAREN);
			}
			this.addTokens(this.statementsStack.peek(), node.parameters());
			if (node.hasParentheses()) {
				this.statementsStack.peek().addToken(RIGHTPAREN);
			}
			this.statementsStack.peek().addToken("->");
			node.getBody().accept(this);
		}

		return false;
	}

	@Override
	public boolean visit(final LineComment node) {
		return super.visit(node);
	}

	@Override
	public boolean visit(final MarkerAnnotation node) {
		return super.visit(node);
	}

	@Override
	public boolean visit(final MemberRef node) {
		return super.visit(node);
	}

	@Override
	public boolean visit(final MemberValuePair node) {
		return super.visit(node);
	}

	@Override
	public boolean visit(final MethodDeclaration node) {

		final int fromLine = this.getFromLineNumber(node);
		final int toLine = this.getToLineNumber(node);
		final JMethod method = new JMethod(this.file, fromLine, toLine);
		this.methodsStack.push(method);

		if (null != node.getBody()) {
			node.getBody().accept(this);
		}

		this.methodsStack.pop();
		this.methods.add(method);

		return false;
	}

	@Override
	public boolean visit(final MethodInvocation node) {

		if (!this.statementsStack.isEmpty()) {
			if (null != node.getExpression()) {
				node.getExpression().accept(this);
				this.statementsStack.peek().addToken(DOT);
			}
			final String name = node.getName().getIdentifier();
			this.statementsStack.peek().addToken(name);
			this.statementsStack.peek().addToken(LEFTPAREN);
			this.addTokens(this.statementsStack.peek(), node.arguments());
			this.statementsStack.peek().addToken(RIGHTPAREN);
		}

		return false;
	}

	@Override
	public boolean visit(final MethodRef node) {
		return super.visit(node);
	}

	@Override
	public boolean visit(final MethodRefParameter node) {
		return super.visit(node);
	}

	@Override
	public boolean visit(final Modifier node) {
		return super.visit(node);
	}

	@Override
	public boolean visit(final NameQualifiedType node) {
		return super.visit(node);
	}

	@Override
	public boolean visit(final NormalAnnotation node) {
		return super.visit(node);
	}

	@Override
	public boolean visit(final NullLiteral node) {

		if (!this.statementsStack.isEmpty()) {
			this.statementsStack.peek().addToken(NULL);
		}

		return false;
	}

	@Override
	public boolean visit(final NumberLiteral node) {

		if (!this.statementsStack.isEmpty()) {
			this.statementsStack.peek().addToken("$NUMBER");
		}

		return false;
	}

	@Override
	public boolean visit(final PackageDeclaration node) {
		return super.visit(node);
	}

	@Override
	public boolean visit(final ParameterizedType node) {

		if (!this.statementsStack.isEmpty()) {
			node.getType().accept(this);
			this.statementsStack.peek().addToken(LEFTANGLEBRACKET);
			this.addTokens(this.statementsStack.peek(), node.typeArguments());
			this.statementsStack.peek().addToken(RIGHTANGLEBRACKET);
		}

		return false;
	}

	@Override
	public boolean visit(final ParenthesizedExpression node) {

		if (!this.statementsStack.isEmpty()) {
			this.statementsStack.peek().addToken(LEFTPAREN);
			node.getExpression().accept(this);
			this.statementsStack.peek().addToken(RIGHTPAREN);
		}

		return false;
	}

	@Override
	public boolean visit(final PostfixExpression node) {

		if (!this.statementsStack.isEmpty()) {
			node.getOperand().accept(this);
			final PostfixExpression.Operator operator = node.getOperator();
			this.statementsStack.peek().addToken(operator.toString());
		}

		return false;
	}

	@Override
	public boolean visit(final PrefixExpression node) {

		if (!this.statementsStack.isEmpty()) {
			final PrefixExpression.Operator operator = node.getOperator();
			if (!operator.toString().equals("-")
					&& !operator.toString().equals("+")) {
				this.statementsStack.peek().addToken(operator.toString());
			}
			node.getOperand().accept(this);
		}

		return false;
	}

	@Override
	public boolean visit(final PrimitiveType node) {

		if (!this.statementsStack.isEmpty()) {
			final String type = node.getPrimitiveTypeCode().toString();
			this.statementsStack.peek().addToken(type);
		}

		return false;
	}

	@Override
	public boolean visit(final QualifiedName node) {

		if (!this.statementsStack.isEmpty()) {
			final String identifier = node.getFullyQualifiedName();
			String normalizedName = this.variablesStack.peek().get(identifier);
			if (null == normalizedName) {
				normalizedName = "$V" + this.variablesStack.peek().size();
				this.variablesStack.peek().put(identifier, normalizedName);
			}
			this.statementsStack.peek().addToken(normalizedName);
		}

		return false;
	}

	@Override
	public boolean visit(final QualifiedType node) {

		if (!this.statementsStack.isEmpty()) {
			node.getQualifier().accept(this);
			this.statementsStack.peek().addToken(DOT);
			final String type = node.getName().toString();
			this.statementsStack.peek().addToken(type);
		}

		return false;
	}

	@Override
	public boolean visit(final ReturnStatement node) {
		final JMethod method = this.methodsStack.peek();
		final int line = getFromLineNumber(node);
		final JStatement statement = new JStatement(method.id, line);
		this.statementsStack.push(statement);
		this.variablesStack.push(new HashMap<String, String>());

		statement.addToken(RETURN);
		if (null != node.getExpression()) {
			node.getExpression().accept(this);
		}
		statement.addToken(SEMICOLON);

		this.statementsStack.pop();
		this.variablesStack.pop();
		method.addStatement(statement);
		if (JMCConfig.getInstance().isDEBUG()) {
			this.print("return statement", statement);
		}
		return false;
	}

	@Override
	public boolean visit(final SimpleName node) {
		if (!this.statementsStack.isEmpty()) {
			final String identifier = node.getIdentifier();
			String normalizedName = this.variablesStack.peek().get(identifier);
			if (null == normalizedName) {
				normalizedName = "$V" + this.variablesStack.peek().size();
				this.variablesStack.peek().put(identifier, normalizedName);
			}
			this.statementsStack.peek().addToken(normalizedName);
		}
		return false;
	}

	@Override
	public boolean visit(final SimpleType node) {

		if (!this.statementsStack.isEmpty()) {
			final String type = node.getName().toString();
			this.statementsStack.peek().addToken(type);
		}

		return false;
	}

	@Override
	public boolean visit(final SingleMemberAnnotation node) {
		return super.visit(node);
	}

	@Override
	public boolean visit(final SingleVariableDeclaration node) {

		if (!this.statementsStack.isEmpty()) {
			node.getType().accept(this);
			node.getName().accept(this);
			if (null != node.getInitializer()) {
				this.statementsStack.peek().addToken("=");
				node.getInitializer().accept(this);
			}
		}

		return false;
	}

	@Override
	public boolean visit(final StringLiteral node) {

		if (!this.statementsStack.isEmpty()) {
			this.statementsStack.peek().addToken("$STRING");
		}

		return false;
	}

	@Override
	public boolean visit(final SuperConstructorInvocation node) {

		final JMethod method = this.methodsStack.peek();
		final int line = getFromLineNumber(node);
		final JStatement statement = new JStatement(method.id, line);
		this.statementsStack.push(statement);
		this.variablesStack.push(new HashMap<String, String>());

		statement.addToken(SUPER);
		statement.addToken(LEFTPAREN);
		this.addTokens(this.statementsStack.peek(), node.arguments());
		statement.addToken(RIGHTPAREN);
		statement.addToken(SEMICOLON);

		this.statementsStack.pop();
		this.variablesStack.pop();
		method.addStatement(statement);
		if (JMCConfig.getInstance().isDEBUG()) {
			this.print("super constructorinvocation", statement);
		}
		return false;
	}

	@Override
	public boolean visit(final SuperFieldAccess node) {

		if (!this.statementsStack.isEmpty()) {
			this.statementsStack.peek().addToken(SUPER);
			this.statementsStack.peek().addToken(DOT);
			node.getName().accept(this);
		}

		return false;
	}

	@Override
	public boolean visit(final SuperMethodInvocation node) {

		if (!this.statementsStack.isEmpty()) {
			this.statementsStack.peek().addToken(SUPER);
			this.statementsStack.peek().addToken(DOT);
			final String name = node.getName().getIdentifier();
			this.statementsStack.peek().addToken(name);
			this.statementsStack.peek().addToken(LEFTPAREN);
			this.addTokens(this.statementsStack.peek(), node.arguments());
			this.statementsStack.peek().addToken(RIGHTPAREN);
		}

		return false;
	}

	@Override
	public boolean visit(final SuperMethodReference node) {
		return super.visit(node);
	}

	@Override
	public boolean visit(final SwitchCase node) {

		final JMethod method = this.methodsStack.peek();
		final int line = getFromLineNumber(node);
		final JStatement statement = new JStatement(method.id, line);
		this.statementsStack.push(statement);
		this.variablesStack.push(new HashMap<String, String>());

		if (null != node.getExpression()) {
			statement.addToken(CASE);
			node.getExpression().accept(this);
		} else {
			statement.addToken(DEFAULT);
		}
		statement.addToken(COLON);

		this.statementsStack.pop();
		this.variablesStack.pop();
		method.addStatement(statement);
		if (JMCConfig.getInstance().isDEBUG()) {
			this.print("switch case", statement);
		}
		return false;
	}

	@Override
	public boolean visit(final SwitchStatement node) {

		final JMethod method = this.methodsStack.peek();
		final int line = getFromLineNumber(node);
		final JStatement statement = new JStatement(method.id, line);
		this.statementsStack.push(statement);
		this.variablesStack.push(new HashMap<String, String>());

		node.getExpression().accept(this);

		this.statementsStack.pop();
		this.variablesStack.pop();
		method.addStatement(statement);
		if (JMCConfig.getInstance().isDEBUG()) {
			this.print("switch conditional expression", statement);
		}

		for (final Object o : node.statements()) {
			((ASTNode) o).accept(this);
		}

		return false;
	}

	@Override
	public boolean visit(final SynchronizedStatement node) {

		final JMethod method = this.methodsStack.peek();
		final int line = getFromLineNumber(node);
		final JStatement statement = new JStatement(method.id, line);
		this.statementsStack.push(statement);
		this.variablesStack.push(new HashMap<String, String>());

		node.getExpression().accept(this);

		this.statementsStack.pop();
		this.variablesStack.pop();
		method.addStatement(statement);
		if (JMCConfig.getInstance().isDEBUG()) {
			this.print("synchronized conditional expression", statement);
		}

		node.getBody().accept(this);

		return false;
	}

	@Override
	public boolean visit(final TagElement node) {
		return super.visit(node);
	}

	@Override
	public boolean visit(final TextElement node) {
		return super.visit(node);
	}

	@Override
	public boolean visit(final ThisExpression node) {

		if (!this.statementsStack.isEmpty()) {
			this.statementsStack.peek().addToken(THIS);
		}

		return false;
	}

	@Override
	public boolean visit(final ThrowStatement node) {
		final JMethod method = this.methodsStack.peek();
		final int line = getFromLineNumber(node);
		final JStatement statement = new JStatement(method.id, line);
		this.statementsStack.push(statement);
		this.variablesStack.push(new HashMap<String, String>());

		statement.addToken(THROW);
		if (null != node.getExpression()) {
			node.getExpression().accept(this);
		}
		statement.addToken(SEMICOLON);

		this.statementsStack.pop();
		this.variablesStack.pop();
		method.addStatement(statement);
		if (JMCConfig.getInstance().isDEBUG()) {
			this.print("throw statement", statement);
		}
		return false;
	}

	@Override
	public boolean visit(final TryStatement node) {
		return super.visit(node);
	}

	@Override
	public boolean visit(final TypeDeclaration node) {
		return super.visit(node);
	}

	@Override
	public boolean visit(final TypeDeclarationStatement node) {
		return super.visit(node);
	}

	@Override
	public boolean visit(final TypeLiteral node) {

		if (!this.statementsStack.isEmpty()) {
			node.getType().accept(this);
			this.statementsStack.peek().addToken(DOT);
			this.statementsStack.peek().addToken(CLASS);
		}

		return false;
	}

	@Override
	public boolean visit(final TypeMethodReference node) {
		return super.visit(node);
	}

	@Override
	public boolean visit(final TypeParameter node) {
		return super.visit(node);
	}

	@Override
	public boolean visit(final UnionType node) {

		if (!this.statementsStack.isEmpty()) {
			this.addTokens(this.statementsStack.peek(), node.types());
		}

		return false;
	}

	@Override
	public boolean visit(final VariableDeclarationExpression node) {

		if (!this.statementsStack.isEmpty()) {
			node.getType().accept(this);
			this.addTokens(this.statementsStack.peek(), node.fragments());
		}

		return false;
	}

	@Override
	public boolean visit(final VariableDeclarationFragment node) {

		if (!this.statementsStack.isEmpty()) {
			node.getName().accept(this);
			if (null != node.getInitializer()) {
				this.statementsStack.peek().addToken("=");
				node.getInitializer().accept(this);
			}
		}

		return false;
	}

	@Override
	public boolean visit(final VariableDeclarationStatement node) {

		final JMethod method = this.methodsStack.peek();
		final int line = getFromLineNumber(node);
		final JStatement statement = new JStatement(method.id, line);
		this.statementsStack.push(statement);
		this.variablesStack.push(new HashMap<String, String>());

		node.getType().accept(this);
		this.addTokens(statement, node.fragments());
		statement.addToken(SEMICOLON);

		this.statementsStack.pop();
		this.variablesStack.pop();
		method.addStatement(statement);
		if (JMCConfig.getInstance().isDEBUG()) {
			this.print("variable declaration statement", statement);
		}
		return false;
	}

	@Override
	public boolean visit(final WhileStatement node) {

		final JMethod method = this.methodsStack.peek();
		final int line = getFromLineNumber(node.getExpression());
		final JStatement statement = new JStatement(method.id, line);
		this.statementsStack.push(statement);
		this.variablesStack.push(new HashMap<String, String>());

		node.getExpression().accept(this);

		this.statementsStack.pop();
		this.variablesStack.pop();
		method.addStatement(statement);
		if (JMCConfig.getInstance().isDEBUG()) {
			this.print("while conditional expression", statement);
		}

		if (null != node.getBody()) {
			node.getBody().accept(this);
		}

		return false;
	}

	@Override
	public boolean visit(final WildcardType node) {

		if (!this.statementsStack.isEmpty()) {
			this.statementsStack.peek().addToken("?");
		}

		return false;
	}

	private int getFromLineNumber(final ASTNode node) {
		return this.root.getLineNumber(node.getStartPosition());
	}

	private int getToLineNumber(final ASTNode node) {
		return this.root.getLineNumber(node.getStartPosition()
				+ node.getLength());
	}

	private void print(final String prefix, final JStatement statement) {
		System.out.println(statement.line + " " + prefix + ": "
				+ statement.getText());
	}

	private void addTokens(final JStatement statement, final List list) {

		if (list.isEmpty()) {
			return;
		}

		final ASTNode node = (ASTNode) list.get(0);
		node.accept(this);

		for (int i = 1; i < list.size(); i++) {
			statement.addToken(COMMA);
			((ASTNode) list.get(i)).accept(this);
		}
	}
}
