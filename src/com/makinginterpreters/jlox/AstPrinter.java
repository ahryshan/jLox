package com.makinginterpreters.jlox;

public class AstPrinter implements Expr.Visitor<String> {
	String print(Expr expr) {
		if (expr == null) return null;
		return expr.accept(this);
	}

	@Override
	public String visitVariableExpr(Expr.Variable expr) {
		return "(" + "var " + expr.name.lexeme + ")";
	}

	@Override
	public String visitAssignExpr(Expr.Assign expr) {
		return parenthesize(expr.name.lexeme + "=", expr.value);
	}

	@Override
	public String visitBinaryExpr(Expr.Binary expr) {
		return parenthesize(expr.operator.lexeme, expr.left, expr.right);
	}

	@Override
	public String visitGroupingExpr(Expr.Grouping expr) {
		return parenthesize("group", expr.expression);
	}

	@Override
	public String visitLiteralExpr(Expr.Literal expr) {
		if (expr.value == null) return "nil";
		return expr.value.toString();
	}

	@Override
	public String visitUnaryExpr(Expr.Unary expr) {
		return parenthesize(expr.operator.lexeme, expr.right);
	}

	private String parenthesize(String name, Expr... exprs) {
		StringBuilder builder = new StringBuilder();

		builder.append("(").append(name);
		for (Expr expr : exprs) {
			builder.append(" ");
			builder.append(expr.accept(this));
		}
		builder.append(")");

		return builder.toString();
	}

	/**
	 * Some little entry point to check if printer works correctly.
	 *
	 * @param args should be ignored. Doesn't get used in any form.
	 * @apiNote btw, must print "(smh= (* (- 123) (group (+ 45.67 (var x)))))"
	 */
	public static void main(String[] args) {
		Expr expression = new Expr.Assign(
						new Token(TokenType.IDENTIFIER, "smh", null, 1),
						new Expr.Binary(
										new Expr.Unary(
														new Token(TokenType.MINUS, "-", null, 1),
														new Expr.Literal(123)),
										new Token(TokenType.STAR, "*", null, 1),
										new Expr.Grouping(
														new Expr.Binary(
																		new Expr.Literal(45.67),
																		new Token(TokenType.PLUS, "+", null, 1),
																		new Expr.Variable(
																						new Token(TokenType.IDENTIFIER, "x", null, 1)
																		)
														)
										)
						));
		System.out.println(new AstPrinter().print(expression));
	}
}
