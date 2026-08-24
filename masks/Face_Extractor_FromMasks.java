import ij.IJ;
import ij.ImagePlus;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;
import java.awt.Color;
import java.io.File;

public class Face_Extractor_FromMasks implements PlugIn {

    @Override
    public void run(String arg) {
        String originalDir = "/Users/syuzannaharutyunyan/Desktop/FEI_Dataset_01/";
        String resultsDir  = "/Users/syuzannaharutyunyan/Desktop/FEI_Dataset_01/Output/Results/";
        String outputDir   = "/Users/syuzannaharutyunyan/Desktop/FEI_Dataset_01/Faces/";

        new File(outputDir).mkdirs();

        int minHue = 3;
        int maxHue = 24;
        int N = 256;

        File[] files = new File(resultsDir).listFiles((dir, name) -> name.endsWith("-Result.png"));
        if (files == null || files.length == 0) {
            IJ.error("No multiplication results found!");
            return;
        }

        for (File file : files) {
            String baseName = file.getName().replace("-Result.png", "");

            // Load S×V product image
            ImagePlus impSV = IJ.openImage(file.getAbsolutePath());
            ImageProcessor ipSV = impSV.getProcessor();
            int[] hist = ipSV.getHistogram();

            // Conditional logarithm
            double[] logged = new double[N];
            for (int i = 0; i < N; i++)
                logged[i] = (hist[i] > 3) ? Math.log(hist[i]) : hist[i];

            // 5-point smoothing
            double[] smooth = new double[N];
            for (int i = 0; i < N; i++) {
                if (i < 2 || i > N - 3) smooth[i] = logged[i];
                else smooth[i] = (logged[i - 2] + logged[i - 1] + logged[i] + logged[i + 1] + logged[i + 2]) / 5.0;
            }

            // Linear regression
            double sumX = 0, sumY = 0, sumXY = 0, sumXX = 0;
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
            double a = (count * sumXY - sumX * sumY) / (count * sumXX - sumX * sumX);
            double b = (sumY - a * sumX) / count;

            // Detrend
            double[] detrended = new double[N];
            for (int i = 0; i < N; i++) detrended[i] = smooth[i] - (a * i + b);

            // Max in range 40–80
            int maxIndex = 40;
            double maxValue = detrended[40];
            for (int i = 41; i <= 80; i++) {
                if (detrended[i] > maxValue) { maxValue = detrended[i]; maxIndex = i; }
            }

            // Left & right intercepts
            int leftIntercept = maxIndex;
            for (int i = maxIndex; i > 0; i--) { if (detrended[i-1] <= 0 && detrended[i] > 0) { leftIntercept = i-1; break; } }
            int rightIntercept = maxIndex;
            for (int i = maxIndex; i < N-1; i++) { if (detrended[i] >= 0 && detrended[i+1] < 0) { rightIntercept = i; break; } }

            // Load original image
            File origFile = new File(originalDir + baseName + ".jpg");
            if (!origFile.exists()) { IJ.log("Original not found: " + baseName); continue; }

            ImagePlus impOrig = IJ.openImage(origFile.getAbsolutePath());
            ImageProcessor ipOrig = impOrig.getProcessor();
            int width = ipOrig.getWidth();
            int height = ipOrig.getHeight();
            int whiteRGB = new Color(255, 255, 255).getRGB();

            // Apply hue + S×V product filtering
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Color c = new Color(ipOrig.getPixel(x, y));
                    float[] hsb = new float[3];
                    Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsb);

                    int hue = (int) (hsb[0] * 256);
                    int sv  = (int) (hsb[1] * hsb[2] * 256);

                    if (hue < minHue || hue > maxHue || sv < leftIntercept || sv > rightIntercept) {
                        ipOrig.putPixel(x, y, whiteRGB);
                    }
                }
            }

            // Save final face image
            String outPath = outputDir + baseName + "_face.png";
            IJ.save(impOrig, outPath);
            IJ.log("Saved face image: " + outPath);
        }

        IJ.showMessage("All images processed and saved to Faces folder.");
    }
}
