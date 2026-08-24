import ij.IJ;
import ij.ImagePlus;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Face_ID_Comparison implements PlugIn {

    private final String mainDir =
            "/Users/syuzannaharutyunyan/Desktop/Selfie-and-ID-Dataset-main/";

    private final int minHue = 3;
    private final int maxHue = 24;
    private final int N = 256;
    private final double logThreshold = 3.0;
    private final int rangeStart = 40;
    private final int rangeEnd = 80;

    @Override
    public void run(String arg) {

        File mainFolder = new File(mainDir);
        File datasetFolder = findDatasetFolder(mainFolder);

        if (datasetFolder == null) {
            IJ.error(
                    "Could not find folders 1-10 inside:\n" +
                            mainDir
            );
            return;
        }

        String facesDir =
                datasetFolder.getAbsolutePath() +
                        File.separator +
                        "Face_Faces" +
                        File.separator;

        new File(facesDir).mkdirs();

        File[] folders = datasetFolder.listFiles(File::isDirectory);

        if (folders == null || folders.length == 0) {
            IJ.error("No dataset folders found.");
            return;
        }

        for (File folder : folders) {

            String folderName = folder.getName();

            if (!isNumberFolder(folderName)) {
                continue;
            }

            File outputFolder = new File(
                    facesDir + folderName + File.separator
            );

            outputFolder.mkdirs();

            File[] files = folder.listFiles(
                    (dir, name) -> {
                        String lower = name.toLowerCase();
                        return lower.endsWith(".jpg") ||
                                lower.endsWith(".jpeg") ||
                                lower.endsWith(".png");
                    }
            );

            if (files == null || files.length == 0) {
                continue;
            }

            for (File file : files) {

                String lowerName = file.getName().toLowerCase();

                if (lowerName.contains("_mask") ||
                        lowerName.contains("_face")) {
                    continue;
                }

                extractFace(file, outputFolder);
            }
        }

        compareFaces(facesDir);

        IJ.showMessage(
                "Complete",
                "Face extraction and comparison completed.\n\n" +
                        "Results saved to:\n" +
                        facesDir +
                        "Comparison_Results.csv"
        );
    }

    private void extractFace(File originalFile, File outputFolder) {

        ImagePlus imp = IJ.openImage(originalFile.getAbsolutePath());

        if (imp == null) {
            return;
        }

        ImageProcessor ip = imp.getProcessor();

        int width = ip.getWidth();
        int height = ip.getHeight();

        int[] svHistogram = new int[N];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                int[] rgb = ip.getPixel(x, y, null);

                float[] hsb = new float[3];

                Color.RGBtoHSB(
                        rgb[0],
                        rgb[1],
                        rgb[2],
                        hsb
                );

                int sv = (int) (hsb[1] * hsb[2] * 256);

                if (sv < 0) {
                    sv = 0;
                }

                if (sv > 255) {
                    sv = 255;
                }

                svHistogram[sv]++;
            }
        }

        double[] logged = new double[N];

        for (int i = 0; i < N; i++) {
            logged[i] =
                    svHistogram[i] > logThreshold
                            ? Math.log(svHistogram[i])
                            : svHistogram[i];
        }

        double[] smooth = new double[N];

        for (int i = 0; i < N; i++) {

            if (i < 2 || i > N - 3) {
                smooth[i] = logged[i];
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

        for (int i = 0; i < N; i++) {

            if (smooth[i] != 0) {

                sumX += i;
                sumY += smooth[i];
                sumXY += i * smooth[i];
                sumXX += i * i;

                count++;
            }
        }

        if (count < 2) {
            imp.close();
            return;
        }

        double denominator =
                count * sumXX - sumX * sumX;

        if (denominator == 0) {
            imp.close();
            return;
        }

        double a =
                (count * sumXY - sumX * sumY) /
                        denominator;

        double b =
                (sumY - a * sumX) /
                        count;

        double[] detrended = new double[N];

        for (int i = 0; i < N; i++) {
            detrended[i] =
                    smooth[i] - (a * i + b);
        }

        int maxIndex = rangeStart;
        double maxValue = detrended[rangeStart];

        for (int i = rangeStart + 1; i <= rangeEnd; i++) {

            if (detrended[i] > maxValue) {
                maxValue = detrended[i];
                maxIndex = i;
            }
        }

        int leftIntercept = maxIndex;

        for (int i = maxIndex; i > 0; i--) {

            if (detrended[i - 1] <= 0 &&
                    detrended[i] > 0) {

                leftIntercept = i - 1;
                break;
            }
        }

        int rightIntercept = maxIndex;

        for (int i = maxIndex; i < N - 1; i++) {

            if (detrended[i] >= 0 &&
                    detrended[i + 1] < 0) {

                rightIntercept = i;
                break;
            }
        }

        ImageProcessor faceProcessor = ip.duplicate();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                int[] rgb = ip.getPixel(x, y, null);

                float[] hsb = new float[3];

                Color.RGBtoHSB(
                        rgb[0],
                        rgb[1],
                        rgb[2],
                        hsb
                );

                int hue = (int) (hsb[0] * 256);

                int sv = (int) (
                        hsb[1] *
                                hsb[2] *
                                256
                );

                if (hue < minHue ||
                        hue > maxHue ||
                        sv < leftIntercept ||
                        sv > rightIntercept) {

                    faceProcessor.putPixel(
                            x,
                            y,
                            new int[]{255, 255, 255}
                    );
                }
            }
        }

        String baseName = originalFile.getName();

        int dot = baseName.lastIndexOf(".");

        if (dot > 0) {
            baseName = baseName.substring(0, dot);
        }

        String outputPath =
                outputFolder.getAbsolutePath() +
                        File.separator +
                        baseName +
                        "_face.png";

        ImagePlus faceImage =
                new ImagePlus(
                        baseName + "_face",
                        faceProcessor
                );

        IJ.save(faceImage, outputPath);

        faceImage.close();
        imp.close();
    }

    private void compareFaces(String facesDir) {

        File facesRoot = new File(facesDir);

        File[] folders =
                facesRoot.listFiles(File::isDirectory);

        if (folders == null || folders.length == 0) {
            IJ.error("No face folders found.");
            return;
        }

        String csvPath =
                facesDir +
                        "Comparison_Results.csv";

        try {

            PrintWriter writer =
                    new PrintWriter(
                            new FileWriter(csvPath)
                    );

            writer.println(
                    "Folder,ID,Selfie,Similarity,Same_Person"
            );

            for (File folder : folders) {

                String folderName = folder.getName();

                if (!isNumberFolder(folderName)) {
                    continue;
                }

                File id1File =
                        findFaceFile(
                                folder,
                                "ID_1_face"
                        );

                File id2File =
                        findFaceFile(
                                folder,
                                "ID_2_face"
                        );

                File[] selfieFiles =
                        folder.listFiles(
                                (dir, name) -> {

                                    String lower =
                                            name.toLowerCase();

                                    return
                                            lower.startsWith("selfie_") &&
                                                    lower.endsWith("_face.png");
                                }
                        );

                if (selfieFiles == null ||
                        selfieFiles.length == 0) {
                    continue;
                }

                if (id1File != null) {

                    ImagePlus id1 =
                            IJ.openImage(
                                    id1File.getAbsolutePath()
                            );

                    if (id1 != null) {

                        for (File selfieFile : selfieFiles) {

                            compareOnePair(
                                    writer,
                                    folderName,
                                    "ID_1",
                                    id1,
                                    selfieFile
                            );
                        }

                        id1.close();
                    }
                }

                if (id2File != null) {

                    ImagePlus id2 =
                            IJ.openImage(
                                    id2File.getAbsolutePath()
                            );

                    if (id2 != null) {

                        for (File selfieFile : selfieFiles) {

                            compareOnePair(
                                    writer,
                                    folderName,
                                    "ID_2",
                                    id2,
                                    selfieFile
                            );
                        }

                        id2.close();
                    }
                }
            }

            writer.close();

            IJ.log(
                    "Comparison results saved to: " +
                            csvPath
            );

        } catch (IOException e) {

            IJ.error(
                    "Could not save comparison results:\n" +
                            e.getMessage()
            );
        }
    }

    private void compareOnePair(
            PrintWriter writer,
            String folderName,
            String idName,
            ImagePlus id,
            File selfieFile) {

        ImagePlus selfie =
                IJ.openImage(
                        selfieFile.getAbsolutePath()
                );

        if (selfie == null) {
            return;
        }

        double[] idHistogram =
                getFaceHistogram(id);

        double[] selfieHistogram =
                getFaceHistogram(selfie);

        selfie.close();

        if (idHistogram == null ||
                selfieHistogram == null) {
            return;
        }

        double similarity =
                compareHistograms(
                        idHistogram,
                        selfieHistogram
                );

        String samePerson =
                similarity >= 0.50
                        ? "YES"
                        : "NO";

        String selfieName =
                selfieFile.getName()
                        .replace("_face.png", "");

        writer.println(
                folderName +
                        "," +
                        idName +
                        "," +
                        selfieName +
                        "," +
                        similarity +
                        "," +
                        samePerson
        );

        IJ.log(
                folderName +
                        " | " +
                        idName +
                        " vs " +
                        selfieName +
                        " | " +
                        similarity +
                        " | " +
                        samePerson
        );
    }

    private double[] getFaceHistogram(ImagePlus face) {

        ImageProcessor ip = face.getProcessor();

        double[] histogram = new double[256];

        int facePixelCount = 0;

        int width = ip.getWidth();
        int height = ip.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                int[] rgb =
                        ip.getPixel(
                                x,
                                y,
                                null
                        );

                int r = rgb[0];
                int g = rgb[1];
                int b = rgb[2];

                if (r >= 250 &&
                        g >= 250 &&
                        b >= 250) {
                    continue;
                }

                int gray =
                        (int) (
                                0.299 * r +
                                        0.587 * g +
                                        0.114 * b
                        );

                if (gray < 0) {
                    gray = 0;
                }

                if (gray > 255) {
                    gray = 255;
                }

                histogram[gray]++;
                facePixelCount++;
            }
        }

        if (facePixelCount == 0) {
            return null;
        }

        for (int i = 0; i < 256; i++) {
            histogram[i] /= facePixelCount;
        }

        return histogram;
    }

    private double compareHistograms(
            double[] h1,
            double[] h2) {

        double mean1 = 0;
        double mean2 = 0;

        for (int i = 0; i < 256; i++) {
            mean1 += h1[i];
            mean2 += h2[i];
        }

        mean1 /= 256.0;
        mean2 /= 256.0;

        double numerator = 0;
        double denominator1 = 0;
        double denominator2 = 0;

        for (int i = 0; i < 256; i++) {

            double value1 =
                    h1[i] - mean1;

            double value2 =
                    h2[i] - mean2;

            numerator += value1 * value2;
            denominator1 += value1 * value1;
            denominator2 += value2 * value2;
        }

        double denominator =
                Math.sqrt(
                        denominator1 *
                                denominator2
                );

        if (denominator == 0) {
            return 0;
        }

        return numerator / denominator;
    }

    private File findFaceFile(
            File folder,
            String nameWithoutExtension) {

        File[] files =
                folder.listFiles(
                        (dir, name) ->
                                name.equalsIgnoreCase(
                                        nameWithoutExtension +
                                                ".png"
                                )
                );

        if (files != null && files.length > 0) {
            return files[0];
        }

        return null;
    }

    private File findDatasetFolder(File mainFolder) {

        File[] directFolders =
                mainFolder.listFiles(File::isDirectory);

        if (directFolders != null) {

            for (File folder : directFolders) {

                if (folder.getName().equals("1")) {
                    return mainFolder;
                }
            }
        }

        File filesFolder =
                new File(
                        mainFolder,
                        "Files"
                );

        if (filesFolder.exists() &&
                filesFolder.isDirectory()) {

            File[] filesFolders =
                    filesFolder.listFiles(File::isDirectory);

            if (filesFolders != null) {

                for (File folder : filesFolders) {

                    if (folder.getName().equals("1")) {
                        return filesFolder;
                    }
                }
            }
        }

        return null;
    }

    private boolean isNumberFolder(String name) {

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