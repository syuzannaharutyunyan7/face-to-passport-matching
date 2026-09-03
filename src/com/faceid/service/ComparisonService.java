package com.faceid.service;

import com.faceid.model.Result;

import java.io.File;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ComparisonService {

    private final DatasetService datasetService;
    private final FacePairComparator facePairComparator;

    public ComparisonService(
            DatasetService datasetService,
            FacePairComparator facePairComparator
    ) {

        this.datasetService =
                datasetService;

        this.facePairComparator =
                facePairComparator;
    }

    public File compareFaces(
            File facesDir,
            List<Result> results,
            Consumer<Result> resultConsumer,
            Consumer<String> logger,
            BiConsumer<Integer, String> progress
    ) throws Exception {

        File[] folders =
                facesDir.listFiles(
                        File::isDirectory
                );

        if (
                folders == null ||
                        folders.length == 0
        ) {

            throw new Exception(
                    "Face_Faces folder is empty."
            );
        }

        File csv =
                new File(
                        facesDir,
                        "Comparison_Results.csv"
                );

        CsvResultWriter writer =
                new CsvResultWriter(csv);

        int totalComparisons = 0;

        for (File folder : folders) {

            if (
                    !datasetService.isNumberFolder(
                            folder.getName()
                    )
            ) {
                continue;
            }

            File[] selfies =
                    datasetService.findSelfies(folder);

            if (
                    selfies == null ||
                            selfies.length == 0
            ) {
                continue;
            }

            File id1 =
                    datasetService.findFaceFile(
                            folder,
                            "ID_1_face"
                    );

            File id2 =
                    datasetService.findFaceFile(
                            folder,
                            "ID_2_face"
                    );

            if (id1 != null) {
                totalComparisons += selfies.length;
            }

            if (id2 != null) {
                totalComparisons += selfies.length;
            }
        }

        logger.accept(
                "Comparisons to perform: " +
                        totalComparisons
        );

        int completed = 0;

        for (File folder : folders) {

            if (
                    !datasetService.isNumberFolder(
                            folder.getName()
                    )
            ) {
                continue;
            }

            File[] selfies =
                    datasetService.findSelfies(folder);

            if (
                    selfies == null ||
                            selfies.length == 0
            ) {
                continue;
            }

            File id1 =
                    datasetService.findFaceFile(
                            folder,
                            "ID_1_face"
                    );

            File id2 =
                    datasetService.findFaceFile(
                            folder,
                            "ID_2_face"
                    );

            if (id1 != null) {

                for (File selfie : selfies) {

                    Result result =
                            facePairComparator.comparePair(
                                    folder.getName(),
                                    "ID_1",
                                    id1,
                                    selfie,
                                    logger
                            );

                    if (result != null) {

                        results.add(result);

                        writer.write(result);

                        resultConsumer.accept(result);
                    }

                    completed++;

                    updateComparisonProgress(
                            completed,
                            totalComparisons,
                            progress
                    );
                }
            }

            if (id2 != null) {

                for (File selfie : selfies) {

                    Result result =
                            facePairComparator.comparePair(
                                    folder.getName(),
                                    "ID_2",
                                    id2,
                                    selfie,
                                    logger
                            );

                    if (result != null) {

                        results.add(result);

                        writer.write(result);

                        resultConsumer.accept(result);
                    }

                    completed++;

                    updateComparisonProgress(
                            completed,
                            totalComparisons,
                            progress
                    );
                }
            }
        }

        writer.close();

        logger.accept(
                "Comparison CSV saved."
        );

        return csv;
    }

    private void updateComparisonProgress(
            int completed,
            int total,
            BiConsumer<Integer, String> progress
    ) {

        int percent;

        if (total <= 0) {

            percent = 100;

        } else {

            percent =
                    60 +
                            (int)
                                    (
                                            completed *
                                                    40.0 /
                                                    total
                                    );
        }

        progress.accept(
                percent,
                "COMPARING " +
                        completed +
                        "/" +
                        total +
                        "  (" +
                        percent +
                        "%)"
        );
    }
}

