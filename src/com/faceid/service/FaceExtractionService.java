package com.faceid.service;

import com.faceid.config.AppConfig;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class FaceExtractionService {

    public void extractFace(
            File originalFile,
            File outputFolder
    ) throws Exception {

        BufferedImage image =
                ImageIO.read(originalFile);

        if (image == null)
            return;

        int width =
                image.getWidth();

        int height =
                image.getHeight();

        int[] svHistogram =
                new int[AppConfig.N];

        for (
                int y = 0;
                y < height;
                y++
        ) {

            for (
                    int x = 0;
                    x < width;
                    x++
            ) {

                int rgb =
                        image.getRGB(
                                x,
                                y
                        );

                int r =
                        (rgb >> 16) & 0xFF;

                int g =
                        (rgb >> 8) & 0xFF;

                int b =
                        rgb & 0xFF;

                float[] hsb =
                        Color.RGBtoHSB(
                                r,
                                g,
                                b,
                                null
                        );

                int sv =
                        (int)
                                (
                                        hsb[1] *
                                                hsb[2] *
                                                256
                                );

                sv =
                        Math.max(
                                0,
                                Math.min(
                                        255,
                                        sv
                                )
                        );

                svHistogram[sv]++;
            }
        }

        double[] logged =
                new double[AppConfig.N];

        for (
                int i = 0;
                i < AppConfig.N;
                i++
        ) {

            logged[i] =
                    svHistogram[i] >
                            AppConfig.LOG_THRESHOLD
                            ? Math.log(
                            svHistogram[i]
                    )
                            : svHistogram[i];
        }

        double[] smooth =
                new double[AppConfig.N];

        for (
                int i = 0;
                i < AppConfig.N;
                i++
        ) {

            if (
                    i < 2 ||
                            i > AppConfig.N - 3
            ) {

                smooth[i] =
                        logged[i];

            } else {

                smooth[i] =
                        (
                                logged[i - 2] +
                                        logged[i - 1] +
                                        logged[i] +
                                        logged[i + 1] +
                                        logged[i + 2]
                        ) / 5.0;
            }
        }

        double sumX = 0;
        double sumY = 0;
        double sumXY = 0;
        double sumXX = 0;

        int count = 0;

        for (
                int i = 0;
                i < AppConfig.N;
                i++
        ) {

            if (smooth[i] != 0) {

                sumX += i;
                sumY += smooth[i];
                sumXY += i * smooth[i];
                sumXX += i * i;

                count++;
            }
        }

        if (count < 2)
            return;

        double denominator =
                count * sumXX -
                        sumX * sumX;

        if (denominator == 0)
            return;

        double a =
                (
                        count * sumXY -
                                sumX * sumY
                ) /
                        denominator;

        double b =
                (
                        sumY -
                                a * sumX
                ) /
                        count;

        double[] detrended =
                new double[AppConfig.N];

        for (
                int i = 0;
                i < AppConfig.N;
                i++
        ) {

            detrended[i] =
                    smooth[i] -
                            (
                                    a * i +
                                            b
                            );
        }

        int maxIndex =
                AppConfig.RANGE_START;

        double maxValue =
                detrended[
                        AppConfig.RANGE_START
                        ];

        for (
                int i =
                        AppConfig.RANGE_START + 1;
                i <= AppConfig.RANGE_END;
                i++
        ) {

            if (
                    detrended[i] >
                            maxValue
            ) {

                maxValue =
                        detrended[i];

                maxIndex =
                        i;
            }
        }

        int leftIntercept =
                maxIndex;

        for (
                int i = maxIndex;
                i > 0;
                i--
        ) {

            if (
                    detrended[i - 1] <= 0 &&
                            detrended[i] > 0
            ) {

                leftIntercept =
                        i - 1;

                break;
            }
        }

        int rightIntercept =
                maxIndex;

        for (
                int i = maxIndex;
                i < AppConfig.N - 1;
                i++
        ) {

            if (
                    detrended[i] >= 0 &&
                            detrended[i + 1] < 0
            ) {

                rightIntercept =
                        i;

                break;
            }
        }

        BufferedImage face =
                new BufferedImage(
                        width,
                        height,
                        BufferedImage.TYPE_INT_RGB
                );

        for (
                int y = 0;
                y < height;
                y++
        ) {

            for (
                    int x = 0;
                    x < width;
                    x++
            ) {

                int rgb =
                        image.getRGB(
                                x,
                                y
                        );

                int r =
                        (rgb >> 16) & 0xFF;

                int g =
                        (rgb >> 8) & 0xFF;

                int bColor =
                        rgb & 0xFF;

                float[] hsb =
                        Color.RGBtoHSB(
                                r,
                                g,
                                bColor,
                                null
                        );

                int hue =
                        (int)
                                (
                                        hsb[0] *
                                                256
                                );

                int sv =
                        (int)
                                (
                                        hsb[1] *
                                                hsb[2] *
                                                256
                                );

                if (
                        hue < AppConfig.MIN_HUE ||
                                hue > AppConfig.MAX_HUE ||
                                sv < leftIntercept ||
                                sv > rightIntercept
                ) {

                    face.setRGB(
                            x,
                            y,
                            Color.WHITE.getRGB()
                    );

                } else {

                    face.setRGB(
                            x,
                            y,
                            rgb
                    );
                }
            }
        }

        String baseName =
                originalFile.getName();

        int dot =
                baseName.lastIndexOf(".");

        if (dot > 0) {

            baseName =
                    baseName.substring(
                            0,
                            dot
                    );
        }

        File output =
                new File(
                        outputFolder,
                        baseName +
                                "_face.png"
                );

        ImageIO.write(
                face,
                "png",
                output
        );
    }
}

