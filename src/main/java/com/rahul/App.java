package com.rahul;

public class App {

    static String parseName(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if ("--name".equals(args[i])) {
                if (i + 1 < args.length) return args[i + 1];
                return null;
            }
        }
        return null;
    }

    static String buildGreeting(String[] args) {
        String name = parseName(args);
        if (name == null || name.isBlank()) return "Hello World!";
        return "Hello " + name;
    }

    public static void main(String[] args) {
        System.out.println(buildGreeting(args));
    }
}
