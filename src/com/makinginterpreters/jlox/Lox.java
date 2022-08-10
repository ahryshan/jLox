package com.makinginterpreters.jlox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Lox {
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

    public static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
    }

    public static void runPrompt() throws IOException {

    }

}