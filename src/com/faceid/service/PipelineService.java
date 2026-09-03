package com.faceid.service;

import com.faceid.model.Result;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class PipelineService {

    private final DatasetService datasetService;
    private final FaceExtractionService faceExtractionService;
    private final ComparisonService comparisonService;

    public PipelineService(
            DatasetService datasetService,
            FaceExtractionService faceExtractionService,
            ComparisonService comparisonService
    ) {

        this.datasetService =
                datasetService;

        this.faceExtractionService =
                faceExtractionService;

        this.comparisonService =
                comparisonService;
    }

    public void runPipeline(
            File datasetFolder,
            List<Result> results,
            Consumer<Result> resultConsumer,
            Consumer<String> logger,
            BiConsumer<Integer, String> progress
    ) throws Exception {

        logger.accept(
                "Dataset: " +
                        datasetFolder.getAbsolutePath()
        );

        File facesDir =
                new File(
                        datasetFolder,
                        "Face_Faces"
                );

        if (!facesDir.exists()) {
            facesDir.mkdirs();
        }

        File[] folders =
                datasetFolder.listFiles(
                        File::isDirectory
                );

        List<File> validFolders =
                new ArrayList<>();

        if (folders != null) {

            for (File folder : folders) {

                if (
                        datasetService.isNumberFolder(
                                folder.getName()
                        )
                ) {

                    validFolders.add(folder);
                }
            }
        }

        if (validFolders.isEmpty()) {

            throw new Exception(
                    "No numbered dataset folders 1-10 found."
            );
        }

        int totalImages = 0;

        for (File folder : validFolders) {

            File[] files =
                    datasetService.getOriginalImages(
                            folder
                    );

            if (files != null) {
                totalImages += files.length;
            }
        }

        logger.accept(
                "Dataset folders found: " +
                        validFolders.size()
        );

        logger.accept(
                "Images found: " +
                        totalImages
        );

        int processed = 0;

        for (File folder : validFolders) {

            File outputFolder =
                    new File(
                            facesDir,
                            folder.getName()
                    );

            outputFolder.mkdirs();

            File[] files =
                    datasetService.getOriginalImages(
                            folder
                    );

            if (files == null)
                continue;

            for (File file : files) {

                logger.accept(
                        "Extracting: " +
                                folder.getName() +
                                "/" +
                                file.getName()
                );

                faceExtractionService.extractFace(
                        file,
                        outputFolder
                );

                processed++;

                int percent;

                if (totalImages == 0) {

                    percent = 50;

                } else {

                    percent =
                            (int)
                                    (
                                            processed *
                                                    60.0 /
                                                    totalImages
                                    );
                }

                progress.accept(
                        percent,
                        "EXTRACTING FACES " +
                                percent +
                                "%"
                );
            }
        }

        logger.accept(
                "FACE EXTRACTION COMPLETE"
        );

        progress.accept(
                60,
                "FACE EXTRACTION COMPLETE"
        );

        File csv =
                comparisonService.compareFaces(
                        facesDir,
                        results,
                        resultConsumer,
                        logger,
                        progress
                );

        progress.accept(
                100,
                "COMPLETE"
        );

        logger.accept("");
        logger.accept(
                "========================================"
        );
        logger.accept(
                "PIPELINE COMPLETE"
        );
        logger.accept(
                "COMPARISON RESULTS: " +
                        results.size()
        );
        logger.accept(
                "========================================"
        );

        logger.accept(
                "CSV: " +
                        csv.getAbsolutePath()
        );
    }
}

