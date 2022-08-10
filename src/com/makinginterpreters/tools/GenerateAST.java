package com.makinginterpreters.tools;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class GenerateAST {
    public static void main(String @NotNull [] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: generate_ast <output directory>");
            System.exit(64);
        } else {
            String outputDirPath = args[0];
            defineAst(outputDirPath, "Expr", Arrays.asList(
                    "Binary   : Expr left, Token operator, Expr right",
                    "Grouping : Expr expression",
                    "Literal  : Object value",
                    "Unary    : Token operator, Expr right"
            ));
        }
    }

    private static void defineAst(String outputDir, String baseName, List<String> types) throws IOException {
        String path = outputDir + "/" + baseName + ".java";
        File newFile = new File(path);
        try {
            if (newFile.createNewFile()) {
                System.out.println("File successfully created");
            } else {
                System.out.println("File already exists");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to create a file on " + newFile.getAbsolutePath());
            System.exit(-1);
        }
        PrintWriter writer = new PrintWriter(path, StandardCharsets.UTF_8);

        writer.print(
                "package com.makinginterpreters.jlox;\n" +
                        "\n" +
                        "public abstract class " + baseName + " {\n"
        );

        defineVisitor(writer, baseName, types);

        for (String type : types) {
            String className = type.split(":")[0].trim();
            String fields = type.split(":")[1].trim();
            defineType(writer, baseName, className, fields);
        }
        writer.println();
        writer.println("  abstract <R> R accept(Visitor<R> visitor);");
        writer.println();
        writer.println("}");
        writer.close();
    }

    private static void defineType(@NotNull PrintWriter writer, String baseName, String className, @NotNull String fieldList) {
        writer.print(
                "    public static class " + className + " extends " + baseName + " {\n" +
                        "        " + className + "(" + fieldList + ") {\n"
        );
        String[] fields = fieldList.split(", ");
        for (String field : fields) {
            String name = field.split(" ")[1];
            writer.println(
                    "            this." + name + " = " + name + ";"
            );
        }
        writer.println("        }");
        writer.println("");
        writer.println();
        writer.println("    @Override");
        writer.println("    <R> R accept(Visitor<R> visitor) {");
        writer.println("      return visitor.visit" +
                className + baseName + "(this);");
        writer.println("    }");
        for (String field : fields) {
            writer.println("        final " + field + ";");
        }
        writer.println("    }");
    }

    private static void defineVisitor(PrintWriter writer, String baseName, List<String> types) {
        writer.println("    interface Visitor<R> {");

        for (String type : types) {
            String typeName = type.split(":")[0].trim();
            writer.println("        R visit" + typeName + baseName + "(" +
                    typeName + " " + baseName.toLowerCase() + ");");
        }

        writer.println("    }");
    }
}
