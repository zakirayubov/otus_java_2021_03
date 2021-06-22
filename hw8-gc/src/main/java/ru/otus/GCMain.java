package ru.otus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GCMain {
    private static long cycles = 0;

    public static void main(String[] args) {
        Thread printingHook = new Thread(() -> System.out.printf("There have been %s cycles%n", cycles));
        Runtime.getRuntime().addShutdownHook(printingHook);

        List<Person> people = new ArrayList<>();
        while (true) {
            for (int i = 0; i < 1000000; i++) {
                people.add(new Person());
            }
            for (int i = 0; i < 10000; i++) {
                people.remove(i);
            }
            cycles++;
        }
    }

    static class Person {
        private final String name;
        private final Integer age;
        private String[] strings = {
                new String("aaaaaaaaaaaaaaaaaaaaaaaaaaaa"),
                new String("bbbbbbbbbbbbbbbbbbbbbbbbbbbb"),
                new String("ccccccccccccccccccccccccccc"),
                new String("ddddddddddddddddddddddddddddddddddddddddd"),
                new String("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee")
        };

        Person() {
            this.age = new Random().nextInt();
            name = randomName();
        }

        public Integer getAge() {
            return age;
        }

        private static String randomName() {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < 10; i++) {
                var letter = (char) (new Random()).nextInt();
                result.append(letter);
            }
            return new String(result.toString());
        }

        public String getName() {
            return new String(name);
        }
    }
}
