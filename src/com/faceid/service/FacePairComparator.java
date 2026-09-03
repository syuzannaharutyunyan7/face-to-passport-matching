package com.faceid.service;

import com.faceid.model.Result;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public class FacePairComparator {

    private final HistogramService histogramService;

    public FacePairComparator(
            HistogramService histogramService
    ) {

        this.histogramService =
                histogramService;
    }

    public Result comparePair(
            String folder,
            String idName,
            File idFile,
            File selfieFile,
            Consumer<String> logger
    ) {

        BufferedImage id;

        BufferedImage selfie;

        try {

            id =
                    ImageIO.read(idFile);

            selfie =
                    ImageIO.read(selfieFile);

        } catch (IOException e) {

            logger.accept(
                    "Could not read image: " +
                            e.getMessage()
            );

            return null;
        }

        if (
                id == null ||
                        selfie == null
        ) {

            return null;
        }

        double[] idHistogram =
                histogramService.getFaceHistogram(id);

        double[] selfieHistogram =
                histogramService.getFaceHistogram(selfie);

        if (
                idHistogram == null ||
                        selfieHistogram == null
        ) {

            return null;
        }

        double similarity =
                histogramService.compareHistograms(
                        idHistogram,
                        selfieHistogram
                );

        String selfieName =
                selfieFile
                        .getName()
                        .replace(
                                "_face.png",
                                ""
                        );

        Result result =
                new Result(
                        folder,
                        idName,
                        selfieName,
                        similarity,
                        idFile,
                        selfieFile
                );

        logger.accept(
                folder +
                        " | " +
                        idName +
                        " vs " +
                        selfieName +
                        " | Similarity = " +
                        String.format(
                                "%.4f",
                                similarity
                        ) +
                        " | " +
                        result.getDecision()
        );

        return result;
    }
}

