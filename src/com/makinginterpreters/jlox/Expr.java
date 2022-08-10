package com.makinginterpreters.jlox;

public abstract class Expr {
    public static class Binary extends Expr {
        Binary(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        final Expr left;
        final Token operator;
        final Expr right;
    }
    public static class Grouping extends Expr {
        Grouping(Expr expression) {
            this.expression = expression;
        }

        final Expr expression;
    }
    public static class Literal extends Expr {
        Literal(Object value) {
            this.value = value;
        }

        final Object value;
    }
    public static class Unary extends Expr {
        Unary(Token operator, Expr right) {
            this.operator = operator;
            this.right = right;
        }

        final Token operator;
        final Expr right;
    }
}
