package ru.spsuace.homework4.files;

import javafx.scene.shape.Path;

import java.io.IOException;
import java.io.File;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;



public class Directories {


    /**
     * Реализовать рекурсивное удаление всех файлов и дерикторий из директороии по заданному пути.
     * Метод должен возвращать количество удаленных файла и директорий.
     * Если директории по существующему пути нет, то возвращаем 0.
     * Написать двумя способами. С использованием File
     */
    public static int removeWithFile(String path) {
        final File mainDir = new File(path);
        int countDeletedFilesDirs = 0;
        if (mainDir.exists()) {
            if (mainDir.isDirectory()) {
                File[] children = mainDir.listFiles();
                if (children != null) {
                    for (File currentChild : children) {
                        if (currentChild.isFile()) {
                            currentChild.delete();
                            countDeletedFilesDirs += 1;
                        } else if (currentChild.isDirectory()) {
                            countDeletedFilesDirs += removeWithFile(currentChild.getPath());
                        }
                    }
                }
                mainDir.delete();
                countDeletedFilesDirs += 1;
            }
            if (mainDir.isFile()) {
                mainDir.delete();
                countDeletedFilesDirs += 1;
            }
        }
        return countDeletedFilesDirs;
    }

    /**
     * С использованием Path\
     */
    public static int removeWithPath(String path) throws IOException {
        RemoverVisitor delete = new RemoverVisitor();
        Files.walkFileTree(Paths.get(path), delete);
        return delete.countDeletedElements;
    }

    public static  class RemoverVisitor extends SimpleFileVisitor<java.nio.file.Path> implements FileVisitor<java.nio.file.Path> {

        int countDeletedElements = 0;

        @Override
        public FileVisitResult visitFile(java.nio.file.Path path, BasicFileAttributes basicFileAttributes) throws IOException {
            File current = path.toFile();
            current.delete();
            countDeletedElements++;
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(java.nio.file.Path path, IOException e) throws IOException {
            return FileVisitResult.TERMINATE;
        }

        @Override
        public FileVisitResult postVisitDirectory(java.nio.file.Path path, IOException e) throws IOException {
            File current = path.toFile();
            current.delete();
            countDeletedElements++;
            return FileVisitResult.CONTINUE;
        }
    }

}
