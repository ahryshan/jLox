package com.makinginterpreters.jlox;

import java.util.List;

public class Interpreter implements Expr.Visitor<Object>, Stmt.Visitor<Object> {

	public static class RuntimeError extends RuntimeException {
		final Token token;

		public RuntimeError(Token token, String msg) {
			super(msg);
			this.token = token;
		}
	}

	private final Environment environment = new Environment();

	public void interpret(List<Stmt> statements) {
		try {
			if (statements == null) return;
			for (Stmt statement : statements) {
				execute(statement);
			}
		} catch (RuntimeError error) {
			Lox.runtimeError(error);
		}
	}

	@Override
	public Object visitAssignExpr(Expr.Assign expr) {
		Object value = evaluate(expr.value);
		environment.assign(expr.name, value);
		return value;
	}

	@Override
	public Object visitVariableExpr(Expr.Variable expr) {
		return environment.get(expr.name);
	}

	@Override
	public Object visitVarStmt(Stmt.Var stmt) {
		Object value = null;
		if(stmt.initializer != null) {
			value = evaluate(stmt.initializer);
		}
		environment.define(stmt.name.lexeme, value);
		return null;
	}

	@Override
	public Object visitExpressionStmt(Stmt.Expression stmt) {
		return evaluate(stmt.expression);
	}

	@Override
	public Object visitPrintStmt(Stmt.Print stmt) {
		Object value = evaluate(stmt.expression);
		if(value == null) {
			System.out.println("nil");
			return null;
		}
		System.out.println(value.toString());
		return null;
	}

	@Override
	public Object visitBinaryExpr(Expr.Binary expr) {
		Object left = evaluate(expr.left);
		Object right = evaluate(expr.right);
		switch (expr.operator.type) {
			case MINUS:
				assertNumberOperands(expr.operator, left, right);
				return asDouble(left) - asDouble(right);
			case SLASH:
				assertNumberOperands(expr.operator, left, right);
				return asDouble(left) / asDouble(right);
			case STAR:
				assertNumberOperands(expr.operator, left, right);
				return asDouble(left) * asDouble(right);
			case PLUS:
				if (left instanceof String || right instanceof String) {
					return left.toString() + right.toString();
				} else return asDouble(left) + asDouble(right);
			case GREATER:
				assertNumberOperands(expr.operator, left, right);
				return asDouble(left) > asDouble(right);
			case GREATER_EQUAL:
				assertNumberOperands(expr.operator, left, right);
				return asDouble(left) >= asDouble(right);
			case LESS:
				assertNumberOperands(expr.operator, left, right);
				return asDouble(left) < asDouble(right);
			case LESS_EQUAL:
				assertNumberOperands(expr.operator, left, right);
				return asDouble(left) <= asDouble(right);
			case EQUAL_EQUAL:
				return isEqual(left, right);
			case BANG_EQUAL:
				return !isEqual(left, right);
			default:
				return null;
		}
	}

	@Override
	public Object visitGroupingExpr(Expr.Grouping expr) {
		return evaluate(expr.expression);
	}

	@Override
	public Object visitLiteralExpr(Expr.Literal expr) {
		return expr.value;
	}

	@Override
	public Object visitUnaryExpr(Expr.Unary expr) {
		Object right = evaluate(expr.right);
		switch (expr.operator.type) {
			case MINUS:
				assertNumberOperand(expr.operator, right);
				return assertWholeNumber(right) ? -asInt(right) : -asDouble(right);
			case BANG:
				return !isTruthy(right);
			default:
				return null;
		}
	}

	private void execute(Stmt stmt) {
		if (stmt == null) return;
		stmt.accept(this);
	}

	private Object evaluate(Expr expr) {
		Object result = expr.accept(this);
		if (result instanceof Number && assertWholeNumber(result)) {
			return asInt(result);
		}
		return result;
	}

	private boolean isTruthy(Object obj) {
		if (obj == null) return false;
		if (obj instanceof Boolean) return (boolean) obj;
		if (obj instanceof String && obj != "") return true;
		if (obj instanceof Number) {
			return asDouble(obj) != 0;
		}
		return false;
	}

	private boolean isEqual(Object a, Object b) {
		if (a == null && b == null) return true;
		if (a == null) return false;
		return a.equals(b);
	}

	private void assertNumberOperand(Token operator, Object right) throws RuntimeError {
		if (right instanceof Number) return;
		throw new RuntimeError(operator, "Operand must be a number.");
	}

	private void assertNumberOperands(Token operator, Object left, Object right) throws RuntimeError {
		if ((left instanceof Number) && (right instanceof Number))
			return;
		throw new RuntimeError(operator, "Operands must be numbers.");
	}

	private Double asDouble(Object o) {
		Double val = null;
		if (o instanceof Number) {
			val = ((Number) o).doubleValue();
		}
		return val;
	}

	private Integer asInt(Object o) {
		Integer val = null;
		if (o instanceof Number) {
			val = ((Number) o).intValue();
		}
		return val;
	}

	private boolean assertWholeNumber(Object o) {
		return (double) asDouble(o) == (int) asInt(o);
	}
}
