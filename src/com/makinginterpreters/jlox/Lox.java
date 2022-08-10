package com.makinginterpreters.jlox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Lox {
    public static boolean hadError = false;

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: lox [path/to/script]");
            System.exit(64);
        } else if (args.length == 1) {
            System.out.println("This will run scripts some day!");
            runFile(args[0]);
        } else {
            System.out.println("This will start prompts some day!");
            runPrompt();
        }
    }

    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
        if(hadError) System.exit(65);
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
        //TODO actual implementation of this;
    }

    private static void error(int line, String message) {
        report(line, "", message);
        hadError = true;
    }

    private static void report(int line, String where, String message) {
        System.out.println("[line " + line + "] Error" + where + ": ");
    }
}