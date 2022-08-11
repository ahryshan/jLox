package com.makinginterpreters.jlox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.List;

public class Lox {
	public static boolean hadError = false;
	private static boolean hadRuntimeError = false;

	private static final Interpreter interpreter = new Interpreter();

	public static void main(String[] args) throws IOException {
		if (args.length > 1) {
			System.out.println("Usage: lox [path/to/script]");
			System.exit(64);
		} else if (args.length == 1) {
			String pathToScript = args[0];
			if (!pathToScript.endsWith(".lox")) pathToScript += ".lox";
			runFile(pathToScript);
		} else {
			System.out.println("This will start prompts some day!");
			runPrompt();
		}
	}

	private static void runFile(String path) throws IOException {
		byte[] bytes;
		try {
			bytes = Files.readAllBytes(Paths.get(path));
		} catch (NoSuchFileException e) {
			System.out.println("Cannot find file: \"" + path + '"');
			System.exit(65);
			return;
		}
		run(new String(bytes, Charset.defaultCharset()));
		if (hadError) System.exit(65);
		if (hadRuntimeError) System.exit(70);
	}

	private static void runPrompt() throws IOException {
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);

		for (; ; ) {
			System.out.print("> ");
			String line = reader.readLine();
			if (line == null) break;
			run(line);
			hadError = false;
		}
	}

	private static void run(String source) {
		Scanner scanner = new Scanner(source);
		List<Token> tokens = scanner.scanTokens();
		Parser parser = new Parser(tokens);
		List<Stmt> statements = parser.parse();
		interpreter.interpret(statements);
	}

	public static void error(int line, String message) {
		report(line, "", message);
		hadError = true;
	}

	public static void error(Token token, String message) {
		if (token.type == TokenType.EOF) {
			report(token.line, "at the end", message);
		} else {
			report(token.line, "at \"" + token.lexeme + '"', message);
		}
		hadError = true;
	}

	private static void report(int line, String where, String message) {
		System.out.println("[line " + line + "] Error " + where + ": " + message);
	}

	static void runtimeError(Interpreter.RuntimeError error) {
		System.err.println(error.getMessage() +
						"\n[line " + error.token.line + "]");
		hadRuntimeError = true;
	}
}