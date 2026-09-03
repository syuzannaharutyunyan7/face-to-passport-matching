package com.faceid.service;

import java.awt.image.BufferedImage;

public class HistogramService {

    public double[] getFaceHistogram(
            BufferedImage face
    ) {

        double[] histogram =
                new double[256];

        int count = 0;

        int width =
                face.getWidth();

        int height =
                face.getHeight();

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
                        face.getRGB(
                                x,
                                y
                        );

                int r =
                        (rgb >> 16) & 0xFF;

                int g =
                        (rgb >> 8) & 0xFF;

                int b =
                        rgb & 0xFF;

                if (
                        r >= 250 &&
                                g >= 250 &&
                                b >= 250
                ) {

                    continue;
                }

                int gray =
                        (int)
                                (
                                        0.299 * r +
                                                0.587 * g +
                                                0.114 * b
                                );

                gray =
                        Math.max(
                                0,
                                Math.min(
                                        255,
                                        gray
                                )
                        );

                histogram[gray]++;

                count++;
            }
        }

        if (count == 0)
            return null;

        for (
                int i = 0;
                i < histogram.length;
                i++
        ) {

            histogram[i] /= count;
        }

        return histogram;
    }

    public double compareHistograms(
            double[] h1,
            double[] h2
    ) {

        double mean1 = 0;
        double mean2 = 0;

        for (
                int i = 0;
                i < 256;
                i++
        ) {

            mean1 += h1[i];
            mean2 += h2[i];
        }

        mean1 /= 256.0;
        mean2 /= 256.0;

        double numerator = 0;
        double denominator1 = 0;
        double denominator2 = 0;

        for (
                int i = 0;
                i < 256;
                i++
        ) {

            double value1 =
                    h1[i] - mean1;

            double value2 =
                    h2[i] - mean2;

            numerator +=
                    value1 * value2;

            denominator1 +=
                    value1 * value1;

            denominator2 +=
                    value2 * value2;
        }

        double denominator =
                Math.sqrt(
                        denominator1 *
                                denominator2
                );

        if (denominator == 0)
            return 0;

        return numerator / denominator;
    }
}

