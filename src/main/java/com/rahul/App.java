package com.rahul;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
    String name = null;

    for (int i = 0; i < args.length; i++) {
            if ("--name".equals(args[i]) && i + 1 < args.length) {
                name = args[i + 1];
                break;
            }
        }

         if (name == null || name.isBlank()) {
            System.out.println("Hello World!");
        } else {
            System.out.println("Hello " + name);
        }
    }
}
