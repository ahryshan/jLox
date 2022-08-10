package com.makinginterpreters.jlox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.makinginterpreters.jlox.TokenType.*;

public class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();

    private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("class", CLASS);
        keywords.put("else", ELSE);
        keywords.put("false", FALSE);
        keywords.put("for", FOR);
        keywords.put("fun", FUN);
        keywords.put("if", IF);
        keywords.put("nil", NIL);
        keywords.put("or", OR);
        keywords.put("print", PRINT);
        keywords.put("return", RETURN);
        keywords.put("super", SUPER);
        keywords.put("this", THIS);
        keywords.put("true", TRUE);
        keywords.put("var", VAR);
        keywords.put("while", WHILE);
    }

    private int start = 0;
    private int current = 0;
    private int line = 1;

    public Scanner(String source) {
        this.source = source;
    }

    public List<Token> scanTokens() {
        while (!isEnd()) {
            start = current;
            scanToken();
        }

        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    private boolean isEnd() {
        return isEnd(0);
    }

    private boolean isEnd(int offset) {
        return this.current + offset >= source.length();
    }

    private void scanToken() {
        char c = advance();
        switch (c) {

            // one character tokens
            case '(' -> addToken(LEFT_PAREN);
            case ')' -> addToken(RIGHT_PAREN);
            case '{' -> addToken(LEFT_BRACE);
            case '}' -> addToken(RIGHT_BRACE);
            case ',' -> addToken(COMMA);
            case '.' -> addToken(DOT);
            case '-' -> addToken(MINUS);
            case '+' -> addToken(PLUS);
            case ';' -> addToken(SEMICOLON);
            case '*' -> addToken(STAR);

            // logic operators and assign
            case '!' -> addToken(match('=') ? BANG_EQUAL : BANG);
            case '=' -> addToken(match('=') ? EQUAL_EQUAL : EQUAL);
            case '<' -> addToken(match('=') ? LESS_EQUAL : LESS);
            case '>' -> addToken(match('=') ? GREATER_EQUAL : GREATER);

            // process slashes or comments
            case '/' -> {
                if (match('/')) {
                    while (peek() != '\n' && current <= source.length() - 1) advance();
                } else if (match('*')) {
                    commentBlock();
                } else {
                    addToken(SLASH);
                }
            }

            // process strings
            case '"' -> string();


            case ' ', '\t', '\r' -> {
            }
            case '\n' -> line++;

            default -> {
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    Lox.error(line, "Unexpected symbol: \"" + c + "\".");
                }
            }
        }
    }

    char advance() {
        return source.charAt(current++);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    private boolean match(char expected) {
        if (isEnd()) return false;
        if (source.charAt(current) != expected) return false;

        current++;
        return true;
    }

    private char peek(int offset) {
        if (isEnd(offset)) return '\0';
        return source.charAt(current + offset);
    }

    private char peek() {
        return peek(0);
    }

    private void string() {
        while (peek() != '"' && !isEnd()) {
            if (peek() == '\n') line++;
            advance();
        }
        if (isEnd()) {
            Lox.error(line, "Unterminated string");
            return;
        }
        advance();
        String value = source.substring(start + 1, current - 1);
        addToken(STRING, value);
    }

    private boolean isDigit(char c) {
        return Character.toString(c).matches("[0-9]");
    }

    private void number() {
        while (isDigit(peek())) advance();
        if (peek() == '.' && isDigit(peek(1))) {
            advance();
            while (isDigit(peek())) advance();
            addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
            return;
        }
        addToken(NUMBER, Integer.parseInt(source.substring(start, current)));
    }

    private boolean isAlpha(char c) {
        return Character.toString(c).matches("[A-Za-z_]");
    }

    private boolean isAlphanumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private void identifier() {
        while (isAlphanumeric(peek())) advance();

        String text = source.substring(start, current);
        TokenType type = keywords.get(text);
        if (type == null) type = IDENTIFIER;

        addToken(type);
    }

    private void commentBlock() {
        while(!(Character.toString(peek()) + Character.toString(peek(1))).equals("*/")) {
            if(advance() == '\n') line++;
            if((Character.toString(peek()) + Character.toString(peek(1))).equals("/*")) {
                commentBlock();
            }
        }
        current+=2;
    }
}
