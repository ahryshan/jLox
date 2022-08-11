package com.makinginterpreters.jlox;

import java.util.HashMap;
import java.util.Map;

public class Environment {
	private final Map<String, Object> values = new HashMap<>();

	void define(String name, Object value) {
		values.put(name, value);
	}

	Object get(Token name) {
		if(values.containsKey(name.lexeme)) {
			return values.get(name.lexeme);
		}

		throw new Interpreter.RuntimeError(name, "Undefined variable at '" + name.lexeme + "'.");

	}

	public void assign(Token name, Object value) {
		if(values.containsKey(name.lexeme)) {
			values.put(name.lexeme, value);
			return;
		}

		throw new Interpreter.RuntimeError(name, "Undefined variable at '" + name.lexeme + "'.");
	}
}
