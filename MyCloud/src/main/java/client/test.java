package client;


import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class test {
    public static void main(String[] args) {
/*        Stream.of("bb2", "aa1", "cc3", "aa2")
                .filter(s->{
                    System.out.println("filter "+ s);
                    return s.startsWith("a");
                })
                .map(s-> {
                    System.out.println("map " + s);
                    return s.toUpperCase();
                })
                .forEach(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        System.out.println("forEach " + s);
                    }
                });*/
        System.out.println();
        System.out.println();
        Stream.of("bb2", "aa1", "cc3", "aa2")
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) {
                        System.out.println("map " + s);
                        return s.toUpperCase();
                    }
                })
                .anyMatch(new Predicate<String>() {
                    @Override
                    public boolean test(String s) {
                        System.out.println("anyMatch " + s);
                        return s.startsWith("A");
                    }
                });
        Stream.of("bb2", "aa1", "cc3", "aa2")
                .map(s -> {
                    System.out.println("map " + s);
                    return s.toUpperCase();
                })
                .anyMatch(s -> {
                    System.out.println("anyMatch " + s);
                    return s.startsWith("A");
                });
    }
}
