import ij.IJ;
import ij.ImagePlus;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;

public class SimpleHistogramAlgorithm implements PlugIn {

    @Override
    public void run(String arg) {
        ImagePlus imp = IJ.getImage();
        if (imp == null) {
            IJ.error("No image open");
            return;
        }

        ImageProcessor ip = imp.getProcessor();
        int[] hist = ip.getHistogram();
        if (hist == null || hist.length != 256) {
            IJ.error("Histogram must have 256 bins");
            return;
        }

        double logThreshold = 3.0;
        int rangeStart = 40;
        int rangeEnd = 80;
        int N = 256;

        // Step 1: Conditional logarithm
        double[] logged = new double[N];
        for (int i = 0; i < N; i++) {
            logged[i] = (hist[i] > logThreshold) ? Math.log(hist[i]) : hist[i];
        }

        // Step 2: 5-point smoothing
        double[] smooth = new double[N];
        for (int i = 0; i < N; i++) {
            if (i < 2 || i > N - 3) {
                smooth[i] = logged[i]; // edges unchanged
            } else {
                smooth[i] = (logged[i - 2] + logged[i - 1] + logged[i] + logged[i + 1] + logged[i + 2]) / 5.0;
            }
        }

        // Step 3: Linear regression on non-zero bins
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

        // Step 4: Detrend
        double[] detrended = new double[N];
        for (int i = 0; i < N; i++) {
            detrended[i] = smooth[i] - (a * i + b);
        }

        // Step 5: Find maximum in range 40-80
        int maxIndex = rangeStart;
        double maxValue = detrended[rangeStart];
        for (int i = rangeStart + 1; i <= rangeEnd; i++) {
            if (detrended[i] > maxValue) {
                maxValue = detrended[i];
                maxIndex = i;
            }
        }

        // Step 6: Find left intercept
        double leftIntercept = maxIndex;
        for (int i = maxIndex; i > 0; i--) {
            if (detrended[i] <= 0 && detrended[i - 1] > 0) {
                leftIntercept = i - 1 + (-detrended[i - 1]) / (detrended[i] - detrended[i - 1]);
                break;
            }
        }

        // Step 7: Find right intercept
        double rightIntercept = maxIndex;
        for (int i = maxIndex; i < N - 1; i++) {
            if (detrended[i] >= 0 && detrended[i + 1] < 0) {
                rightIntercept = i + (-detrended[i]) / (detrended[i + 1] - detrended[i]);
                break;
            }
        }

        // Step 8: Log results
        IJ.log("Trend line: y = " + a + " * x + " + b);
        IJ.log("Max value at index: " + maxIndex + ", value: " + maxValue);
        IJ.log("Left intercept: " + leftIntercept);
        IJ.log("Right intercept: " + rightIntercept);
    }
}
