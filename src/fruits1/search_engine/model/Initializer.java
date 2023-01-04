package fruits1.search_engine.model;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static fruits1.search_engine.model.Constants.BASE_DIR;

public class Initializer {
    protected static void initialize(){
        Path basePath = FileSystems.getDefault().getPath(BASE_DIR.getName());
        try{
//        delete directories and files
            if (Files.exists(basePath)) {
                Files.walkFileTree(basePath, new SimpleFileVisitor<>(){
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes a) throws IOException {
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    }
                });
            }
//        create directory of files
            Files.createDirectory(basePath);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
