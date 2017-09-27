import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.mdht.uml.cda.ClinicalDocument;
import org.eclipse.mdht.uml.cda.util.BasicValidationHandler;
import org.eclipse.mdht.uml.cda.util.CDAUtil;

public class Main {

    public static void main(String[] args) throws IOException {
        CDAUtil.loadPackages();
        
        Path path;
        if (args.length > 0) path = Paths.get(args[0]);
        else path = Paths.get(".");

        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.getFileName().toString().endsWith(".xml")) {
                    System.out.println(file + ": ");
                    FileInputStream inputStream = new FileInputStream(file.toFile());
                    ClinicalDocument clinicalDocument;
                    try {
                        clinicalDocument = CDAUtil.load(inputStream);
                        CDAUtil.validate(clinicalDocument, new BasicValidationHandler() {
                            public void handleError(Diagnostic diagnostic) {
                                System.out.println("ERROR: " + diagnostic.getMessage());
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
