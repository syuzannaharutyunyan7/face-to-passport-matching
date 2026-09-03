# Face ID Comparison

A Java desktop application for comparing ID photos and selfies using image processing and grayscale histogram correlation.

The application was created to make it easier to process a dataset of ID and selfie images, extract relevant face regions, compare the resulting images, and review the comparison results through a simple graphical interface.

## Author

**Syuzanna Harutyunyan**

## About the Project

Face ID Comparison is a Java Swing desktop application designed around a simple image-comparison pipeline.

The application processes a dataset containing numbered folders and performs the following operations:

1. Finds the dataset folders.
2. Extracts face regions from the original images.
3. Identifies ID and selfie images.
4. Compares ID images with selfie images.
5. Calculates a similarity value using grayscale histograms.
6. Classifies the comparison result as:
   - `SAME PERSON`
   - `DIFFERENT`
7. Displays the results through a graphical user interface.
8. Allows the comparison results to be exported to a CSV file.

The main goal of the project is to provide a practical and easy-to-use tool for experimenting with image processing, face extraction, and image similarity comparison.

> **Note:** This project is intended as an image-processing and comparison experiment. Histogram correlation is a visual similarity technique and should not be considered a production-grade biometric face-recognition system.

## Features

- Java desktop application
- Java Swing graphical user interface
- Dataset folder selection
- Automatic detection of numbered dataset folders
- Face-region extraction
- ID and selfie image processing
- Grayscale image conversion
- Grayscale histogram generation
- Histogram correlation for image comparison
- Similarity score calculation
- Automatic classification:
  - `SAME PERSON`
  - `DIFFERENT`
- Processing progress indicators
- Real-time processing log
- ID image preview
- Selfie image preview
- Sortable comparison results table
- CSV result generation
- Batch processing of multiple dataset folders

## How the Application Works

The application follows a simple image-processing pipeline.

### Step 1: Select the Dataset

The user selects the root directory containing the dataset.

### Step 2: Find Dataset Folders

The application searches for numbered folders inside the selected directory.

For example:

```text
Dataset/
├── 1/
├── 2/
├── 3/
├── 4/
└── 10/

Step 3: Load Images
The application reads the images contained inside each numbered folder.
Step 4: Extract Face Regions
Relevant face regions are extracted from the original images.
This allows the comparison process to focus primarily on the face rather than the complete image.

Step 5: Identify ID and Selfie Images
The application identifies the ID and selfie images according to the dataset structure and naming conventions.
Step 6: Convert Images to Grayscale
The extracted images are converted to grayscale before comparison.
Step 7: Generate Histograms
A grayscale histogram is calculated for each image.
Step 8: Compare Histograms
The histograms of the ID and selfie images are compared using correlation.
Step 9: Calculate Similarity
The histogram correlation produces a similarity value.
Step 10: Classify the Result
The similarity value is compared against the configured threshold.
The comparison is classified as either:

SAME PERSON
DIFFERENT
Step 11: Display Results
The results are displayed in the graphical user interface.
Step 12: Export Results
The comparison results can be exported to a CSV file.
Processing Pipeline
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
Identify ID / Selfie
   |
   v
Convert to Grayscale
   |
   v
Generate Histograms
   |
   v
Calculate Histogram Correlation
   |
   v
Similarity Score
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

Dataset Structure
The application expects a dataset containing numbered folders, typically from 1 to 10.
A typical dataset can look like this:

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
│   ├── ID_1.jpg
│   ├── ID_2.jpg
│   ├── selfie_1.jpg
│   └── ...
│
├── 4/
│   ├── ID_1.jpg
│   ├── selfie_1.jpg
│   └── ...
│
├── ...
│
└── 10/
    ├── ID_1.jpg
    ├── ID_2.jpg
    ├── selfie_1.jpg
    └── ...

Each numbered folder represents a dataset group containing ID and selfie images.
Example Dataset
A smaller dataset could contain:
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

The exact number of images in each folder may vary depending on the dataset.
Image Naming
The application expects the dataset to follow the image naming conventions used by the project.
Typical filenames may include:

ID_1.jpg
ID_2.jpg
selfie_1.jpg
selfie_2.jpg

The naming convention allows the application to distinguish between ID images and selfie images.
Image Processing
The application extracts the relevant face regions from the original images before performing the comparison.
The basic processing flow is:

Original Image
      |
      v
Face Detection / Extraction
      |
      v
Face Region
      |
      v
Grayscale Conversion
      |
      v
Histogram Generation
      |
      v
Histogram Comparison

Face extraction helps reduce the influence of irrelevant parts of the image.
Grayscale Histogram Correlation
The application uses grayscale histogram correlation to compare ID photos and selfies.
A grayscale image represents each pixel using an intensity value.

Typically:

0   = Black
255 = White

A histogram represents how frequently different intensity values occur in an image.
For example:

Intensity
0 -------------------------------- 255
          ███
        ███████
      ███████████
    ███████████████
  ███████████████████

The application calculates a grayscale histogram for each processed face image.
The histograms are then compared using correlation.

Similarity
In general:
A higher correlation indicates greater similarity.
A lower correlation indicates greater difference.
The resulting similarity value is then used for classification.
Classification
The application uses a similarity threshold to determine the final result.
SAME PERSON
If the calculated similarity is above the configured threshold, the result is classified as:
SAME PERSON

DIFFERENT
If the calculated similarity is below the configured threshold, the result is classified as:
DIFFERENT

The threshold can significantly affect the final results and should be selected based on the characteristics of the dataset.

User Interface
The application provides a Java Swing graphical interface.
The interface includes:

Dataset folder selection
Folder browser
Complete pipeline execution
Face extraction progress
Comparison progress
Processing log
ID image preview
Selfie image preview
Similarity score
Comparison decision
Sortable comparison table
CSV result generation
The graphical interface allows the user to process and review the dataset without manually opening every image.
Results
After processing is complete, the application displays the comparison results in a sortable table.
A typical result table can contain:

Dataset	ID Image	Selfie Image	Similarity	Decision
1	ID_1.jpg	selfie_1.jpg	0.XX	SAME PERSON
2	ID_1.jpg	selfie_1.jpg	0.XX	DIFFERENT
3	ID_2.jpg	selfie_1.jpg	0.XX	SAME PERSON

The exact similarity values depend on the input images and the image-processing results.
Example Result
A single comparison may be displayed as:
ID Image:       ID_1.jpg
Selfie Image:   selfie_1.jpg
Similarity:     0.XX
Decision:       SAME PERSON

Another comparison may produce:
ID Image:       ID_1.jpg
Selfie Image:   selfie_1.jpg
Similarity:     0.XX
Decision:       DIFFERENT

CSV Export
The application allows the comparison results to be saved as a CSV file.
A typical CSV file can contain:

Dataset,ID Image,Selfie Image,Similarity,Decision
1,ID_1.jpg,selfie_1.jpg,0.XX,SAME PERSON
2,ID_1.jpg,selfie_1.jpg,0.XX,DIFFERENT
3,ID_2.jpg,selfie_1.jpg,0.XX,SAME PERSON

CSV results can be used for:
Further analysis
Sorting and filtering
Spreadsheet processing
Statistical evaluation
Dataset comparison
Creating reports
Technologies
The project is built using:
Java
Java Swing
Image Processing
Grayscale Histograms
Histogram Correlation
CSV Export
Requirements
To run the application, you need:
Java Development Kit (JDK)
A Java-compatible IDE or build environment
All required project dependencies
A compatible image dataset
The Java version should match the version configured by the project.
Installation
Clone the Repository
Clone the repository using Git:
git clone <repository-url>

Navigate to the Project Directory
cd <project-directory>

Open the Project
Open the project using a Java IDE such as:
IntelliJ IDEA
Eclipse
NetBeans
Visual Studio Code
Make sure all required dependencies are correctly configured before running the application.
Running the Application
After configuring the project:
Open the project in your Java IDE.
Locate the application's main class.
Run the main class.
The Java Swing interface should open.
Select the dataset folder.
Start the processing pipeline.
If the project is packaged as a JAR file, it can be run using:
java -jar face-id-comparison.jar

Replace the JAR filename with the actual generated filename.
Usage
Follow these steps to process a dataset:
Start the application.
Select the dataset folder.
Make sure the dataset contains the expected numbered folders.
Start the complete processing pipeline.
Wait for face extraction to complete.
Wait for image comparison to complete.
Monitor the processing log.
Review the comparison results in the table.
Select a result to view the corresponding ID and selfie images.
Review the similarity score.
Review the classification.
Export the results to CSV if required.
Progress and Logging
The application provides progress information for the processing stages.
The interface can display progress for:

Face extraction
Image comparison
A processing log provides additional information about the operations being performed.
For example:

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

The exact log messages depend on the implementation.
Error Handling
The application may encounter different types of dataset or image-processing problems.
Possible issues include:

Missing image files
Invalid image files
Unsupported image formats
Empty dataset folders
Missing ID images
Missing selfie images
Face detection failures
File access errors
Corrupted images
The processing log can be used to identify problems that occur during execution.
Supported Image Formats
Supported image formats depend on the image-processing libraries used by the application.
Common formats include:

.jpg
.jpeg
.png
.bmp
For the best results, use clear images containing a visible face.
Project Structure
A typical project structure may look like:
Face-ID-Comparison/
│
├── src/
│   └── ...
│
├── dataset/
│   └── ...
│
├── output/
│   └── ...
│
├── README.md
└── ...

The actual project structure may differ depending on the implementation.
Limitations
The current comparison method has several limitations.
Histogram-Based Comparison
A grayscale histogram describes the distribution of pixel intensities.
It does not directly understand facial features such as:

Eyes
Nose
Mouth
Face shape
Facial landmarks
Therefore, histogram correlation should be considered an image-similarity technique rather than a complete face-recognition algorithm.
Lighting
Different lighting conditions can significantly affect the grayscale histogram.
For example, the same person photographed in bright lighting and low lighting may produce different similarity values.

Pose
Differences in:
Head angle
Face position
Facial expression
Camera angle
can affect the comparison result.
Image Quality
Blurred, compressed, low-resolution, or poorly exposed images may produce less reliable results.
Background and Cropping
Differences in cropping and image composition can influence histogram correlation if non-face pixels remain in the processed image.
Threshold Selection
The classification depends on the selected similarity threshold.
A threshold that works well for one dataset may not work equally well for another dataset.

Biometric Accuracy
This application should not be considered a production biometric identity verification system.
The SAME PERSON and DIFFERENT classifications are based on the implemented image-comparison method and should not be treated as definitive proof of identity.

Future Improvements
Possible improvements include:
More accurate face detection
Face alignment
Improved image preprocessing
Automatic image resizing
Better image normalization
Multiple histogram comparison methods
Facial landmark detection
Feature-based face comparison
Face embeddings
Deep-learning-based face recognition
Configurable similarity thresholds
More detailed CSV reports
Result statistics and visualization
Multi-threaded image processing
Improved error handling
Automated testing
Better dataset validation
Support for larger datasets
Privacy and Security
ID documents and selfies may contain sensitive personal information.
When working with real datasets:

Only use images that you are authorized to process.
Do not upload private ID documents or selfies to a public repository.
Do not commit sensitive datasets to GitHub.
Use anonymized or synthetic images when possible.
Store processed images securely.
Follow applicable privacy and data-protection requirements.
Git Ignore
It is recommended to exclude datasets and image files from Git if they contain private information.
Example .gitignore:

dataset/
output/
*.jpg
*.jpeg
*.png
*.bmp
*.webp

This helps prevent accidentally committing sensitive image files to the repository.
Example Workflow
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
Select Dataset/
Find folders 1, 2, and 3
Read images
Extract face regions
Identify ID and selfie images
Convert images to grayscale
Generate histograms
Calculate histogram correlation
Calculate similarity scores
Apply the classification threshold
Display results
Export results to CSV
Example Output
The application may produce results similar to:
Dataset: 1
ID Image: ID_1.jpg
Selfie Image: selfie_1.jpg
Similarity: 0.XX
Decision: SAME PERSON

Another example:
Dataset: 2
ID Image: ID_1.jpg
Selfie Image: selfie_1.jpg
Similarity: 0.XX
Decision: DIFFERENT

The actual similarity values depend on the images and configured threshold.
Use Cases
This project can be used for:
Learning Java Swing
Learning basic image processing
Experimenting with face extraction
Processing image datasets
Exploring grayscale histograms
Studying histogram correlation
Comparing ID and selfie images
Building a simple computer-vision pipeline
Generating structured comparison results
Experimenting with similarity thresholds
Development Notes
The project demonstrates how several concepts can be combined into a single Java desktop application:
Java
 |
 +-- Java Swing
 |
 +-- File and Directory Processing
 |
 +-- Image Processing
 |
 +-- Face Extraction
 |
 +-- Grayscale Conversion
 |
 +-- Histogram Generation
 |
 +-- Histogram Correlation
 |
 +-- Similarity Calculation
 |
 +-- Result Classification
 |
 +-- CSV Export

The project provides a simple foundation that can later be extended with more advanced computer-vision and face-recognition techniques.
Troubleshooting
Dataset Folders Are Not Detected
Make sure the selected directory contains numbered folders.
For example:

Dataset/
├── 1/
├── 2/
├── 3/
└── 10/

Face Extraction Fails
Check that:
The image contains a visible face.
The face is large enough to detect.
The image is not severely blurred.
The image is not corrupted.
The image format is supported.
Similarity Results Are Unexpected
Remember that histogram correlation measures image characteristics rather than facial identity.
Differences in lighting, pose, cropping, background, and image quality can affect the result.

Processing Takes a Long Time
Processing time depends on:
Number of images
Image resolution
Number of dataset folders
Face extraction method
Computer hardware
Number of comparisons
For large datasets, processing may take longer.
Disclaimer
This project is provided for educational and experimental purposes.
The comparison method is based on image processing and grayscale histogram correlation.

A SAME PERSON result does not prove that two images belong to the same individual, and a DIFFERENT result does not definitively prove that they belong to different individuals.

This software should not be used as the sole mechanism for:

Authentication
Identity verification
Access control
Financial decisions
Security-critical decisions
Other high-risk applications
License
No license has been specified for this project yet.
If you plan to publish the project as open source, consider adding an appropriate license such as:

MIT License
Apache License 2.0
GNU General Public License v3.0
Author
Syuzanna Harutyunyan
Face ID Comparison is a Java desktop application for image processing, face extraction, ID/selfie comparison, and grayscale histogram analysis.

