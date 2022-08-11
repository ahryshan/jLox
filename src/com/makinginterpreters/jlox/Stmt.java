package com.makinginterpreters.jlox;

public abstract class Stmt {
    interface Visitor<R> {
        R visitExpressionStmt(Expression stmt);
        R visitPrintStmt(Print stmt);
    }
    public static class Expression extends Stmt {
        Expression(Expr expression) {
            this.expression = expression;
        }


    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitExpressionStmt(this);
    }
        final Expr expression;
    }
    public static class Print extends Stmt {
        Print(Expr expression) {
            this.expression = expression;
        }


    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitPrintStmt(this);
    }
        final Expr expression;
    }

  abstract <R> R accept(Visitor<R> visitor);

}
