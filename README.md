# Face ID Comparison

A Java desktop application for comparing ID photos and selfies using image processing and grayscale histogram correlation.

The application makes it easier to process a dataset of ID and selfie images, extract relevant face regions, compare the resulting images, and review the comparison results through a simple graphical interface.

## Author

**Syuzanna Harutyunyan**

## About the Project

Face ID Comparison is a Java Swing desktop application designed around a simple image-comparison pipeline.

The application processes a dataset containing numbered folders and performs the following operations:

1. Finds the dataset folders.
2. Extracts face regions from the original images.
3. Identifies ID and selfie images.
4. Converts the extracted images to grayscale.
5. Generates grayscale histograms.
6. Normalizes the histograms.
7. Compares the histograms using correlation.
8. Calculates a similarity score.
9. Classifies the comparison result as:
   - `SAME PERSON`
   - `DIFFERENT`
10. Displays the results through a graphical user interface.
11. Allows the comparison results to be exported to a CSV file.

> **Note:** This project is an image-processing and comparison experiment. Histogram correlation is a visual similarity technique and should not be considered a production-grade biometric face-recognition system.

## Features

- Java desktop application
- Java Swing graphical user interface
- Dataset folder selection
- Automatic detection of numbered dataset folders
- Face-region extraction
- ID and selfie image processing
- Grayscale image conversion
- Grayscale histogram generation
- Histogram normalization
- Histogram correlation
- Similarity score calculation
- `SAME PERSON` / `DIFFERENT` classification
- Face extraction progress
- Comparison progress
- Real-time processing log
- ID image preview
- Selfie image preview
- Similarity score display
- Comparison decision display
- Sortable comparison table
- CSV result generation
- Batch processing of multiple dataset folders

## Comparison Method

The current version uses image histograms rather than a machine-learning face-recognition model.

For each processed image, the application:

1. Extracts the relevant face region.
2. Converts the image to grayscale.
3. Generates a grayscale histogram.
4. Normalizes the histogram.
5. Compares the histograms using correlation.
6. Produces a similarity score.

### Similarity Threshold

The current similarity threshold used by the application is:

**0.50**

The classification works as follows:

| Similarity | Decision |
| --- | --- |
| `>= 0.50` | `SAME PERSON` |
| `< 0.50` | `DIFFERENT` |

The threshold is part of the current implementation and may be adjusted in future versions.

## Dataset

The application requires a compatible image dataset containing numbered folders.

A typical dataset structure looks like:

    Dataset/
    │
    ├── 1/
    │   ├── ID_1.jpg
    │   ├── ID_2.jpg
    │   ├── selfie_1.jpg
    │   └── ...
    │
    ├── 2/
    │   ├── ID_1.jpg
    │   ├── ID_2.jpg
    │   ├── selfie_1.jpg
    │   └── ...
    │
    ├── 3/
    │   └── ...
    │
    └── 10/
        ├── ID_1.jpg
        ├── ID_2.jpg
        ├── selfie_1.jpg
        └── ...

Each numbered folder represents a dataset group containing ID and selfie images.

### Dataset Download

The dataset used with this application is available here:

**Dataset:** [ADD DATASET LINK HERE]

> Replace the placeholder above with the actual dataset link.

### Image Naming

The application expects the dataset to follow the naming conventions used by the project.

Typical filenames include:

    ID_1.jpg
    ID_2.jpg
    selfie_1.jpg
    selfie_2.jpg

The naming convention allows the application to distinguish between ID images and selfie images.

## How the Application Works

The application follows this processing pipeline:

    Dataset
        |
        v
    Select Dataset Folder
        |
        v
    Find Numbered Directories
        |
        v
    Load Images
        |
        v
    Extract Face Regions
        |
        v
    Identify ID / Selfie Images
        |
        v
    Convert to Grayscale
        |
        v
    Generate Histograms
        |
        v
    Normalize Histograms
        |
        v
    Calculate Histogram Correlation
        |
        v
    Calculate Similarity Score
        |
        v
    Apply Classification
        |
        +-------------------+
        |                   |
        v                   v
    SAME PERSON        DIFFERENT
        |                   |
        +---------+---------+
                  |
                  v
           Display Results
                  |
                  v
              Export CSV

## Output

After the processing pipeline finishes, the application creates a `Face_Faces` directory containing the extracted images and comparison results.

Example:

    Dataset/
    │
    ├── 1/
    ├── 2/
    ├── 3/
    ├── ...
    ├── 10/
    │
    └── Face_Faces/
        ├── 1/
        ├── 2/
        ├── 3/
        ├── ...
        ├── 10/
        │
        └── Comparison_Results.csv

The exact output structure may vary depending on the dataset and processing results.

## CSV Results

The application automatically generates a CSV file named:

    Comparison_Results.csv

The file contains comparison information such as:

- Folder
- ID
- Selfie
- Similarity
- Same_Person

Example:

    Folder,ID,Selfie,Similarity,Same_Person
    1,ID_1.jpg,selfie_1.jpg,0.XX,SAME PERSON
    2,ID_1.jpg,selfie_1.jpg,0.XX,DIFFERENT
    3,ID_2.jpg,selfie_1.jpg,0.XX,SAME PERSON

The CSV file can be used for:

- Further analysis
- Sorting and filtering
- Spreadsheet processing
- Statistical evaluation
- Dataset comparison
- Creating reports

## Requirements

To run the application, you need:

- Java installed on your computer
- A compatible Java Runtime Environment
- The provided `.jar` application
- A compatible image dataset

No Java source code or development environment is required to run the provided `.jar` application.

## Running the Application

The repository contains the built `.jar` application.

Download the `.jar` file from the repository and make sure Java is installed on your computer.

Open a terminal in the directory containing the `.jar` file and run:

    java -jar Face-ID-Comparison.jar

If the `.jar` file has a different filename, replace `Face-ID-Comparison.jar` with the actual filename.

The application will open a Java Swing graphical interface.

## Usage

Follow these steps to use the application:

1. Start the application.
2. Select the dataset folder.
3. Make sure the dataset contains the expected numbered folders.
4. Start the complete processing pipeline.
5. Wait for face extraction to complete.
6. Wait for image comparison to complete.
7. Monitor the processing log.
8. Review the comparison results in the table.
9. Select a result to view the corresponding ID and selfie images.
10. Review the similarity score.
11. Review the classification result.
12. Export the results to CSV if required.

## User Interface

The application provides a graphical interface with:

- Dataset folder selection
- Folder browser
- Complete pipeline execution
- Face extraction progress
- Comparison progress
- Processing log
- ID image preview
- Selfie image preview
- Similarity score
- Comparison decision
- Sortable comparison table
- CSV result generation

The interface allows the user to process and review the dataset without manually opening every image.

## Supported Image Formats

Supported image formats depend on the image-processing functionality used by the application.

Common formats include:

- `.jpg`
- `.jpeg`
- `.png`
- `.bmp`

For the best results, use clear images containing a visible face.

## Example Workflow

Consider the following dataset:

    Dataset/
    │
    ├── 1/
    │   ├── ID_1.jpg
    │   └── selfie_1.jpg
    │
    ├── 2/
    │   ├── ID_1.jpg
    │   └── selfie_1.jpg
    │
    └── 3/
        ├── ID_1.jpg
        └── selfie_1.jpg

The application performs the following operations:

1. Select the `Dataset/` folder.
2. Find folders `1`, `2`, and `3`.
3. Read the images.
4. Extract face regions.
5. Identify ID and selfie images.
6. Convert images to grayscale.
7. Generate histograms.
8. Normalize the histograms.
9. Calculate histogram correlation.
10. Calculate similarity scores.
11. Apply the `0.50` classification threshold.
12. Display the results.
13. Export the results to CSV.

## Example Result

A comparison may produce a result similar to:

    Folder:       1
    ID Image:     ID_1.jpg
    Selfie Image: selfie_1.jpg
    Similarity:   0.XX
    Decision:     SAME PERSON

Another comparison may produce:

    Folder:       2
    ID Image:     ID_1.jpg
    Selfie Image: selfie_1.jpg
    Similarity:   0.XX
    Decision:     DIFFERENT

The actual similarity values depend on the input images and processing results.

## Progress and Logging

The application provides progress information during the main processing stages.

The interface can display progress for:

- Face extraction
- Image comparison

The processing log provides information about the operations being performed.

Example:

    Starting dataset processing...
    Finding dataset folders...
    Processing folder: 1
    Extracting faces...
    Face extraction completed.
    Starting image comparison...
    Comparing ID and selfie images...
    Comparison completed.
    Generating results...
    Processing completed.

The exact log messages depend on the application implementation.

## Troubleshooting

### Dataset Folders Are Not Detected

Make sure the selected directory contains numbered folders.

For example:

    Dataset/
    ├── 1/
    ├── 2/
    ├── 3/
    └── 10/

### Face Extraction Fails

Check that:

- The image contains a visible face.
- The face is large enough to detect.
- The image is not severely blurred.
- The image is not corrupted.
- The image format is supported.
- The application has permission to read the image.

### ID or Selfie Images Are Not Found

Check that the image filenames follow the expected naming convention.

Typical filenames include:

    ID_1.jpg
    ID_2.jpg
    selfie_1.jpg
    selfie_2.jpg

### Similarity Results Are Unexpected

Histogram correlation measures image characteristics rather than facial identity.

Differences in the following can affect the result:

- Lighting
- Pose
- Cropping
- Background
- Image quality
- Face position

### Processing Takes a Long Time

Processing time depends on:

- Number of images
- Image resolution
- Number of dataset folders
- Face extraction method
- Computer hardware
- Number of comparisons

Large datasets may require more processing time.

## Limitations

### Histogram-Based Comparison

A grayscale histogram describes the distribution of pixel intensities.

It does not directly understand facial features such as:

- Eyes
- Nose
- Mouth
- Face shape
- Facial landmarks
- Identity-specific facial characteristics

Therefore, histogram correlation should be considered an image-similarity technique rather than a complete face-recognition algorithm.

### Lighting

Different lighting conditions can significantly affect the grayscale histogram.

The same person photographed under different lighting conditions may produce different similarity values.

### Pose

Differences in the following can affect the comparison result:

- Head angle
- Face position
- Facial expression
- Camera angle

### Image Quality

Blurred, compressed, low-resolution, or poorly exposed images may produce less reliable results.

### Background and Cropping

Differences in cropping and image composition can influence histogram correlation if non-face pixels remain in the processed image.

### Threshold Selection

The current classification threshold is `0.50`.

A threshold that works well for one dataset may not work equally well for another dataset.

### Biometric Accuracy

This application should not be considered a production biometric identity verification system.

The `SAME PERSON` and `DIFFERENT` classifications are based on the implemented image-comparison method and should not be treated as definitive proof of identity.

## Privacy and Security

ID documents and selfies may contain sensitive personal information.

When working with real datasets:

- Only use images that you are authorized to process.
- Do not upload private ID documents or selfies to a public repository.
- Do not commit sensitive datasets to GitHub.
- Use anonymized or synthetic images when possible.
- Store processed images securely.
- Follow applicable privacy and data-protection requirements.

## Future Improvements

Possible future improvements include:

- Better face detection
- More reliable face alignment
- Improved image preprocessing
- Automatic image resizing
- Better image normalization
- More advanced face comparison
- Facial landmark detection
- Feature-based face comparison
- Face embeddings
- Deep-learning-based face recognition
- Configurable similarity thresholds
- More detailed CSV reports
- Result statistics and visualization
- Improved error handling
- Automated testing
- Better dataset validation
- Support for larger datasets
- More portable application packaging

## Disclaimer

This project is provided for educational and experimental purposes.

The comparison method is based on image processing and grayscale histogram correlation.

A `SAME PERSON` result does not prove that two images belong to the same individual.

A `DIFFERENT` result does not definitively prove that two images belong to different individuals.

This software should not be used as the sole mechanism for:

- Authentication
- Identity verification
- Access control
- Financial decisions
- Security-critical decisions
- Legal decisions
- Other high-risk applications

## License

No license has been specified for this project yet.

If you plan to publish the project as open source, consider adding an appropriate license such as:

- MIT License
- Apache License 2.0
- GNU General Public License v3.0

Until a license is added, the repository should not be assumed to grant permission to copy, modify, or redistribute the application.

## Author

**Syuzanna Harutyunyan**
