package by.radioegor146;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Main {
    
    private static final String VERSION = "1.7b+";
    
    public static void main(String[] args) throws IOException {
        System.out.println("native-obfuscator v" + VERSION);
        if (args.length < 3) {
            System.err.println("java -jar native-obfuscator.jar <jar file> <output directory> <forAndroid true/false> [libraries dir]");
            System.err.println("java -jar native-obfuscator.jar classes.jar classes true androidLibDir");
            return;
        }
        System.out.println("Android "+ args[2]);
        String libsDir = args.length < 4 ? null : args[3];
        List<Path> libs = new ArrayList<>();
        if (libsDir != null)
            Files.walkFileTree(Paths.get(libsDir), Collections.singleton(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException
                {
                    Objects.requireNonNull(file);
                    Objects.requireNonNull(attrs);
                    if (file.toString().endsWith(".jar") || file.toString().endsWith(".zip")) {
                        libs.add(file);
                        System.out.println("library " + file.getFileName());
                     }
                    return super.visitFile(file, attrs);
                }
            });
        NativeObfuscator instance = new NativeObfuscator();
        instance.process(Paths.get(args[0]), Paths.get(args[1]), libs, Boolean.valueOf(args[2]));
    }
}
