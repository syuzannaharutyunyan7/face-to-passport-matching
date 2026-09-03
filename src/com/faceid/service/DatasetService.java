package com.faceid.service;

import java.io.File;

public class DatasetService {

    public File[] getOriginalImages(
            File folder
    ) {

        return folder.listFiles(
                (dir, name) -> {

                    String n =
                            name.toLowerCase();

                    return
                            (
                                    n.endsWith(".jpg") ||
                                            n.endsWith(".jpeg") ||
                                            n.endsWith(".png")
                            )
                                    &&
                                    !n.contains("_face")
                                    &&
                                    !n.contains("_mask");
                }
        );
    }

    public File[] findSelfies(
            File folder
    ) {

        return folder.listFiles(
                (dir, name) -> {

                    String n =
                            name.toLowerCase();

                    return
                            n.startsWith("selfie_") &&
                                    n.endsWith("_face.png");
                }
        );
    }

    public File findFaceFile(
            File folder,
            String name
    ) {

        File file =
                new File(
                        folder,
                        name + ".png"
                );

        return file.exists()
                ? file
                : null;
    }

    public File findDatasetFolder(
            File mainFolder
    ) {

        if (
                hasFolder1(mainFolder)
        ) {
            return mainFolder;
        }

        File filesFolder =
                new File(
                        mainFolder,
                        "Files"
                );

        if (
                hasFolder1(filesFolder)
        ) {
            return filesFolder;
        }

        return null;
    }

    public boolean hasFolder1(
            File folder
    ) {

        if (
                folder == null ||
                        !folder.exists() ||
                        !folder.isDirectory()
        ) {

            return false;
        }

        File[] dirs =
                folder.listFiles(
                        File::isDirectory
                );

        if (dirs == null)
            return false;

        for (File f : dirs) {

            if (
                    f.getName()
                            .equals("1")
            ) {

                return true;
            }
        }

        return false;
    }

    public boolean isNumberFolder(
            String name
    ) {

        try {

            int number =
                    Integer.parseInt(name);

            return number >= 1 &&
                    number <= 10;

        } catch (NumberFormatException e) {

            return false;
        }
    }
}

