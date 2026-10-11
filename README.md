# Face ID Dataset Search and Comparison System

## About the Project

The **Face ID Dataset Search and Comparison System** is a Java desktop application that allows users to compare selfie images with ID photos stored in a local dataset.

The application has a graphical interface built with Java Swing, making it easy to upload images, select a dataset folder, run comparisons, and view the results without using the command line for everyday tasks.

It uses a custom image-processing technique to filter image colors, generate grayscale histograms, and calculate similarity scores between images. After processing the dataset, it ranks the ID photos according to their similarity to the uploaded selfies and saves the results to a CSV file.

This project was developed to explore image processing and image similarity using Java.

**Note:** The current version does not use a trained facial recognition model. It compares image histograms rather than directly identifying facial features, so its results should be treated as similarity estimates, not proof that two images belong to the same person.

## Features

The application includes the following features:

- **Multiple selfie uploads:** Select several selfie images at once.
- **Dataset selection:** Choose a folder containing ID photos.
- **Recursive image scanning:** Search for images in the selected folder and its subfolders.
- **Image counting:** Check how many supported image files are available in the dataset.
- **Image processing:** Apply a custom color-filtering algorithm to each image.
- **Similarity comparison:** Compare normalized grayscale histograms to calculate similarity scores.
- **Automatic ranking:** Sort ID photos from the highest similarity score to the lowest.
- **Visual results:** View original and processed images for selected results.
- **Progress tracking:** Follow the search through the progress bar and log panel.
- **CSV export:** Save the comparison results for later review.

## Technologies Used

The project uses standard Java libraries and does not require additional image-processing dependencies.

- **Java** – Main programming language.
- **Java Swing** – Used to build the graphical user interface.
- **Java AWT** – Handles layouts, colors, fonts, and image scaling.
- **ImageIO** – Reads image files.
- **BufferedImage** – Provides access to image pixels.
- **JTable** – Displays the comparison results.
- **Java Collections** – Stores and sorts the results.
- **Java I/O** – Handles file operations and CSV export.
- **Java Threads** – Runs the dataset search in the background.

## Requirements

Before running the application, make sure you have:

- A Java Development Kit (JDK) installed.
- A computer that supports Java Swing applications.
- Selfie images in a supported format.
- A folder containing the ID-photo dataset.
- Read permission for the image files and write permission for the dataset folder if you want to export the results.

JDK 17 or later is a suitable choice.

To check whether Java is installed, open a terminal or command prompt and run:

```
java -version
```

You can also check the Java compiler with:

```
javac -version
```

## Installation and Setup

### 1\. Save the Java file

Save the source code as:

```
Face_ID_Comparison_UI_new.java
```

The filename must match the public class name.

### 2\. Open the project directory

Open a terminal and navigate to the folder containing the Java file.

For example:

```
cd Face-ID-Comparison
```

### 3\. Compile the application

Run the following command:

```
javac Face_ID_Comparison_UI_new.java
```

If compilation is successful, Java will generate the required `.class` files.

### 4\. Run the application

Start the program with:

```
java Face_ID_Comparison_UI_new
```

The application window should open, and you can begin selecting images.

## How to Use the Application

### Step 1: Upload selfie images

Click **ADD SELFIE IMAGE(S)** and select the selfie images you want to compare.

You can select multiple images in one operation. The application keeps track of the selected files and displays the first selfie in the image panel.

If you want to start over, click **CLEAR SELFIES** to remove the selected images.

### Step 2: Select the ID-photo dataset

Click **BROWSE DATASET** and choose the folder containing your ID photos.

The application searches the selected folder and its subfolders, so you do not need to place all images in a single directory.

For example:

```
id_dataset/
├── person_group_1/
│   ├── id_001.jpg
│   └── id_002.png
├── person_group_2/
│   ├── id_003.jpeg
│   └── id_004.jpg
└── id_005.png
```

### Step 3: Count the images

Click **COUNT ID PHOTOS** to check how many supported image files were found.

This is optional, but it helps confirm that you selected the correct folder.

Keep in mind that the application counts files based on their extensions. Some files may still fail when the program attempts to open them.

### Step 4: Start the search

Click **SEARCH DATASET** to begin processing and comparing the images.

The application checks that at least one selfie and a valid dataset folder have been selected. If fewer than 100 images are found, it displays a warning asking whether you want to continue.

You can continue with a smaller dataset if you wish.

### Step 5: Follow the progress

While the search is running, the log panel displays information about the current operation, and the progress bar shows the approximate completion percentage.

The application processes the uploaded selfies first, then scans and compares the ID photos.

Images that cannot be read or processed are skipped where possible, and the reason is recorded in the log.

### Step 6: Review the results

When the search finishes, the results table displays up to 50 ID photos with the highest similarity scores.

Each row includes:

- The result's rank.
- The ID image filename.
- The selfie that produced the highest score for that ID image.
- The similarity score.
- The result classification.

Click a row to display the corresponding original and filtered images in the center of the interface.

### Step 7: Save the results

After processing, the application attempts to save a CSV file named:

```
Comparison_Results.csv
```

The file is saved in the selected dataset folder.

You can open it with Excel, LibreOffice Calc, or another program that supports CSV files.

## How the Image Processing Works

The application follows a series of steps to process and compare the images.

### 1\. Loading the images

The program uses Java's `ImageIO` class to read each image into a `BufferedImage`.

If an image cannot be opened, the application records an error and moves on to the next file.

### 2\. Filtering the image

The `extractFaceImage()` method applies a custom color-filtering algorithm.

It converts image pixels from RGB to HSB color space and calculates a value based on saturation and brightness. It then builds a histogram of these values, smooths the histogram, removes a linear trend, and looks for a peak within a predefined range.

The resulting boundaries are used to decide which pixels to keep. Pixels outside the selected hue and saturation-brightness ranges are replaced with white.

This produces a filtered version of the original image.

Although the method is named `extractFaceImage()`, it is actually detect or locate a face. It filters pixels based on their color properties, because the identified threshold for the hue, saturation and brightness cut the faces regardless of the races.

### 3\. Generating grayscale histograms

After filtering, the application converts the remaining non-white pixels to grayscale.

The grayscale intensity is calculated using:

$$
G = 0.299R + 0.587G + 0.114B
$$

The program then builds a histogram containing 256 bins, representing intensity values from 0 to 255.

Finally, it normalizes the histogram so that each bin represents the proportion of retained pixels with that intensity.

This provides a compact summary of the image's grayscale distribution.

### 4\. Comparing histograms

The application compares the histogram of each ID image with the histograms of the successfully processed selfies.

It uses a Pearson-style correlation coefficient to measure how closely the histogram distributions are related.

The score generally ranges from -1 to +1:

- A score close to `1` indicates a strong positive correlation.
- A score close to `0` indicates little or no linear correlation.
- A score close to `-1` indicates a strong negative correlation.

If the calculation encounters a zero denominator, the method returns `0`.

**Important:** These scores measure similarities between histogram distributions. They are not probabilities and do not establish whether two images show the same person.

### 5\. Finding the best match

For each ID image, the application compares its histogram against every usable selfie histogram.

It keeps the selfie that produces the highest similarity score for that ID image.

For example, if an ID image receives scores of `0.72`, `0.41`, and `0.18` against three selfies, the first selfie is selected as its best match.

These numbers are only an example of how the comparison works.

### 6\. Sorting the results

Once all ID images have been processed, the application sorts the results from the highest similarity score to the lowest.

The 50 highest-ranked entries are displayed in the table. The CSV export includes all results generated by the search, not just the 50 shown in the interface.

## Configuration

Several constants near the beginning of the Java class control how the algorithm behaves.

| Parameter | Default value | Purpose |
| --- | --- | --- |
| `MIN_HUE` | 3 | Minimum accepted hue index |
| `MAX_HUE` | 24 | Maximum accepted hue index |
| `N` | 256 | Number of SV histogram bins |
| `LOG_THRESHOLD` | 3.0 | Threshold for the logarithmic transformation |
| `RANGE_START` | 40 | Start of the histogram peak-search range |
| `RANGE_END` | 80 | End of the histogram peak-search range |
| `THRESHOLD` | 0.50 | Similarity threshold for result labels |
| `MAX_RESULTS` | 50 | Maximum number of displayed results |

You can change these values in the source code and recompile the application.

For example, to change the similarity threshold to `0.60`, update:

```
private static final double THRESHOLD = 0.60;
```

Scores at or above the new threshold will be labelled `SIMILAR`; scores below it will be labelled `LOW SIMILARITY`.

Changing the threshold only changes the classification. It does not make the underlying comparison more accurate.

## CSV Output

The exported `Comparison_Results.csv` file contains the following columns:

| Column | Description |
| --- | --- |
| `Rank` | Position of the result in the sorted list |
| `ID_Image` | Filename of the ID image |
| `Selfie_Image` | Filename of the best-matching selfie |
| `Similarity` | Calculated histogram correlation score |
| `Result` | Classification based on the configured threshold |

An example of the output format is shown below:

```
Rank,ID_Image,Selfie_Image,Similarity,Result
1,id_001.jpg,selfie_01.jpg,0.7832,SIMILAR
2,id_002.png,selfie_02.jpg,0.4621,LOW SIMILARITY
3,id_003.jpeg,selfie_01.jpg,-0.1045,LOW SIMILARITY
```

These are sample values, not results from an actual run.

The application includes basic CSV escaping for filenames containing commas, quotation marks, or newline characters.

If the dataset folder is not writable, the CSV file may not be created. Check the application log if an export error occurs.

## Supported Image Formats

The application checks for these file extensions:

- JPG (`.jpg`)
- JPEG (`.jpeg`)
- PNG (`.png`)
- BMP (`.bmp`)
- GIF (`.gif`)

The extension alone does not guarantee that an image can be processed. Corrupted files or files with unsupported encodings may still be skipped.

## Performance and Limitations

Processing time depends on the number of images, their resolution, and the number of uploaded selfies.

For every ID image, the program generates a filtered image and histogram, then compares that histogram with each successfully processed selfie histogram.

Large datasets and high-resolution images may increase processing time and memory usage because the application retains original and filtered images for its results.

The current implementation also has some limitations:

- It does not identify facial landmarks or align faces.
- It compares grayscale histograms rather than facial embeddings.
- It can be affected by lighting, shadows, backgrounds, and image quality.
- Similarity scores are not calibrated to represent identity confidence.
- The CSV stores filenames rather than complete source paths.

These limitations should be considered when testing the application or interpreting its output.

## Troubleshooting

**The application does not start.**

Check that a compatible JDK is installed and that the source file has been compiled successfully.

**No images are found in the dataset.**

Make sure you selected the correct folder and that it contains files with supported image extensions.

**Some images are skipped.**

The files may be corrupted, unreadable, or unsuitable for the filtering and histogram-generation steps. Try using standard RGB JPG or PNG images.

**The similarity scores are unexpectedly low.**

The filtering algorithm may remove too many pixels, or the images may have substantially different grayscale distributions. Inspect the filtered image layers to understand what is happening.

**The CSV file is missing.**

Check the application log and make sure the program has permission to write to the selected dataset directory.

**The search is slow.**

Try a smaller dataset or lower-resolution images first. For larger projects, caching histograms and avoiding the storage of every processed image would help reduce resource usage.

## Future Improvements

There are several ways this project could be improved:

- Identify more reliable comparison method.
- Align detected faces before comparison.
- Replace histogram comparison with a new comparison method.
- Improve error handling and dataset validation.

These changes would make the application easier to maintain and provide a stronger foundation for further development.

## Privacy and Responsible Use

Selfies and ID photographs can contain sensitive personal information. Only use images you have permission to process, and store the dataset and exported results securely.

The current application processes images locally and does not contain explicit network-upload functionality. However, you are still responsible for protecting the source images and generated CSV files.

The results should not be used as the sole basis for identity verification, authentication, or other high-stakes decisions.

## License

Author

Developed by: Syuzanna Harutyunyan

This is my own project, developed using Java Swing and image-processing techniques to compare selfie images with an ID-photo dataset.
