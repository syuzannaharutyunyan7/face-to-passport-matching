Face ID Comparison
A Java desktop application for comparing ID photos and selfies using image processing and grayscale histogram correlation.
The application was created to make it easier to process a dataset of ID and selfie images, extract the relevant face regions, compare the resulting images, and review the results through a simple graphical interface.

Author
Syuzanna Harutyunyan
About the Project
Face ID Comparison is a Java Swing application designed around a simple image-comparison pipeline.
The application takes a dataset containing numbered folders and processes the images inside them. It extracts face regions, identifies ID and selfie images, compares their visual characteristics, and produces a comparison result.

The application also provides a graphical interface where the results can be reviewed without having to inspect every image manually.

The main goal of the project is to provide a practical and easy-to-use tool for experimenting with image processing and face-image comparison.

Note: This project is intended as an image-processing and comparison experiment. Histogram correlation is a visual similarity technique and should not be considered a production-grade biometric face-recognition system.
Features
Java desktop application
Java Swing graphical user interface
Dataset folder selection
Folder browser
Complete pipeline execution
Automatic detection of numbered dataset folders
Face-region extraction
ID and selfie image processing
Grayscale image conversion
Grayscale histogram generation
Histogram normalization
Histogram correlation for image comparison
Similarity score calculation
Automatic classification
SAME PERSON / DIFFERENT decision
Face extraction progress
Comparison progress
Real-time processing log
ID image preview
Selfie image preview
Similarity score display
Comparison decision display
Sortable comparison table
CSV result generation
Batch processing of multiple dataset folders
What the Application Does
The application follows these main steps:
Select the dataset folder.
Find the numbered dataset folders.
Extract face regions from the original images.
Identify ID and selfie images.
Convert processed images to grayscale.
Generate grayscale histograms.
Normalize the histograms.
Compare ID and selfie histograms using correlation.
Calculate a similarity value.
Classify the comparison result.
Display the results in the graphical interface.
Export the comparison results to a CSV file.
The possible classification results are:
SAME PERSON
DIFFERENT
How the Application Works
Step 1: Select the Dataset
The user selects the root directory containing the dataset.
Step 2: Find Dataset Folders
The application searches for numbered folders inside the selected directory.
For example:

Dataset/
├── 1/
├── 2/
├── 3/
├── 4/
└── 10/

Step 3: Load Images
The application reads the images contained inside each numbered dataset folder.
Step 4: Extract Face Regions
Relevant face regions are extracted from the original images.
This allows the comparison process to focus primarily on the face rather than the complete image.

Step 5: Identify ID and Selfie Images
The application identifies ID and selfie images according to the dataset structure and naming conventions used by the project.
Step 6: Convert Images to Grayscale
The extracted images are converted to grayscale before comparison.
Step 7: Generate Histograms
A grayscale histogram is generated for each processed image.
Step 8: Normalize Histograms
The generated histograms are normalized before comparison.
Step 9: Compare Histograms
The normalized grayscale histograms are compared using correlation.
Step 10: Calculate Similarity
The histogram correlation produces a similarity value.
Step 11: Classify the Result
The similarity value is compared against the configured classification threshold.
The current threshold used by the application is:

0.50

If the similarity is greater than or equal to 0.50, the result is classified as:
SAME PERSON

If the similarity is below 0.50, the result is classified as:
DIFFERENT

Step 12: Display Results
The comparison results are displayed in the graphical user interface.
Step 13: Export Results
The comparison results can be exported to a CSV file.
Processing Pipeline
The complete processing pipeline can be summarized as:
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
Apply Classification Threshold
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
A typical dataset can look like:

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
The application can also detect the dataset when the numbered folders are located inside a Files directory.

For example:

Files/
│
├── 1/
├── 2/
├── 3/
├── ...
└── 10/

Image Naming
The application identifies ID and selfie images according to the naming conventions used by the project.
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
Histogram Normalization
      |
      v
Histogram Correlation
      |
      v
Similarity Score

Face extraction helps reduce the influence of irrelevant parts of the image.
Grayscale Histogram Correlation
The application uses grayscale histogram correlation to compare ID photos and selfies.
A grayscale image represents each pixel using an intensity value.

Typically:

0 represents black.
255 represents white.
A histogram represents how frequently different intensity values occur in an image.
A simplified histogram can be represented as:

Intensity
0 -------------------------------- 255
          ███
        ███████
      ███████████
    ███████████████
  ███████████████████

The application calculates a grayscale histogram for each processed face image.
The histograms are then normalized and compared using correlation.

Similarity
In general:
A higher correlation indicates greater similarity between the images.
A lower correlation indicates greater difference between the images.
The resulting similarity value is then used for classification.
Classification
The application uses a similarity threshold to determine the final result.
The current threshold is:

0.50

The classification rules are:
Similarity	Decision
>= 0.50	SAME PERSON
< 0.50	DIFFERENT

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

Folder	ID	Selfie	Similarity	Same Person
1	ID_1.jpg	selfie_1.jpg	0.XX	SAME PERSON
2	ID_1.jpg	selfie_1.jpg	0.XX	DIFFERENT
3	ID_2.jpg	selfie_1.jpg	0.XX	SAME PERSON

The exact similarity values depend on the input images and image-processing results.
Example Result
A single comparison may be displayed as:
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

CSV Export
The application allows the comparison results to be saved as a CSV file.
The output file is named:

Comparison_Results.csv

The file is created inside the Face_Faces directory.
The CSV contains:

Folder
ID
Selfie
Similarity
Same_Person
Example:
Folder,ID,Selfie,Similarity,Same_Person
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
Output Directory
After processing, the application creates a Face_Faces directory containing the extracted images and comparison results.
A typical output structure is:

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

The exact output structure may depend on the implementation.
Requirements
To run the built application, you need:
Java installed on your computer
A compatible Java Runtime Environment
A compatible image dataset
The provided application build or project source code
The project uses Java Swing for the desktop interface and Java-based image-processing functionality.
The Java version should match the version used to build the application.

Installation
Clone the Repository
Clone the repository using Git:
git clone <repository-url>

Navigate to the project directory:
cd <project-directory>

Open the Project
If you are working with the source code, open the project using a Java-compatible IDE such as:
IntelliJ IDEA
Eclipse
NetBeans
Visual Studio Code
Make sure all required dependencies are correctly configured before running the source version.
Running the Application
If you are using the provided build version, run the application according to the format of the build.
For a JAR build, use:

java -jar Face-ID-Comparison.jar

If the JAR file has a different filename, replace Face-ID-Comparison.jar with the actual filename.
After starting the application:

Select the dataset folder.
Verify that the numbered folders are detected.
Start the complete processing pipeline.
Wait for face extraction to complete.
Wait for image comparison to complete.
Review the processing log.
Review the comparison results.
Export the results to CSV if required.
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
The application provides progress information during the processing stages.
The interface can display progress for:

Face extraction
Image comparison
A processing log provides additional information about the operations being performed.
Example log:

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
Supported image formats depend on the image-processing functionality used by the application.
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
├── Face_Faces/
│   └── ...
│
├── Face-ID-Comparison.jar
├── README.md
└── ...

The actual project structure may vary depending on the project implementation and build version.
Technologies
The project is built using:
Java
Java Swing
Image Processing
Grayscale Conversion
Grayscale Histograms
Histogram Normalization
Histogram Correlation
CSV Export
File and Directory Processing
Project Status
Current status: Working prototype / desktop application
The main processing pipeline is implemented and includes:

Image extraction
Face-region extraction
Image comparison
Grayscale conversion
Histogram generation
Histogram correlation
Similarity calculation
Result classification
Result visualization
Progress reporting
CSV export
The repository currently contains the built version of the application so that it can be run without needing to open or modify the Java source code.
The source code and a more structured development version can be added to the repository in a future update.

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
Identity-specific facial characteristics
Therefore, histogram correlation should be considered an image-similarity technique rather than a complete face-recognition algorithm.
Lighting
Different lighting conditions can significantly affect the grayscale histogram.
For example, the same person photographed in bright lighting and low lighting may produce different similarity values.

Pose
Differences in the following can affect the comparison result:
Head angle
Face position
Facial expression
Camera angle
Image Quality
Blurred, compressed, low-resolution, or poorly exposed images may produce less reliable results.
Background and Cropping
Differences in cropping and image composition can influence histogram correlation if non-face pixels remain in the processed image.
Threshold Selection
The current classification threshold is 0.50.
A threshold that works well for one dataset may not work equally well for another dataset.

Biometric Accuracy
This application should not be considered a production biometric identity verification system.
The SAME PERSON and DIFFERENT classifications are based on the implemented image-comparison method and should not be treated as definitive proof of identity.

Future Improvements
Possible future improvements include:
Better face detection
More reliable face alignment
Improved image preprocessing
Automatic image resizing
Better image normalization
More advanced face comparison
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
More portable application packaging
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
It is recommended to exclude datasets and generated image files from Git if they contain private or sensitive information.
Example .gitignore:

dataset/
output/
Face_Faces/
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
Select Dataset/.
Find folders 1, 2, and 3.
Read the images.
Extract face regions.
Identify ID and selfie images.
Convert images to grayscale.
Generate histograms.
Normalize the histograms.
Calculate histogram correlation.
Calculate similarity scores.
Apply the 0.50 classification threshold.
Display the results.
Export the results to CSV.
Example Output
The application may produce results similar to:
Folder: 1
ID Image: ID_1.jpg
Selfie Image: selfie_1.jpg
Similarity: 0.XX
Decision: SAME PERSON

Another comparison may produce:
Folder: 2
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
Learning desktop application development in Java
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
 +-- Histogram Normalization
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

If the folders are inside a Files directory, select the appropriate parent directory according to the application's expected dataset structure.
Face Extraction Fails
Check that:
The image contains a visible face.
The face is large enough to detect.
The image is not severely blurred.
The image is not corrupted.
The image format is supported.
The application has permission to read the image.
ID or Selfie Images Are Not Found
Check that the image filenames follow the expected naming convention.
Typical filenames include:

ID_1.jpg
ID_2.jpg
selfie_1.jpg
selfie_2.jpg

Similarity Results Are Unexpected
Remember that histogram correlation measures image characteristics rather than facial identity.
Differences in the following can affect the result:

Lighting
Pose
Cropping
Background
Image quality
Face position
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

A SAME PERSON result does not prove that two images belong to the same individual.

A DIFFERENT result does not definitively prove that two images belong to different individuals.

This software should not be used as the sole mechanism for:

Authentication
Identity verification
Access control
Financial decisions
Security-critical decisions
Legal decisions
Other high-risk applications
License
No license has been specified for this project yet.
If you plan to publish the project as open source, consider adding an appropriate license such as:

MIT License
Apache License 2.0
GNU General Public License v3.0
Until a license is added, the repository should not be assumed to grant permission to copy, modify, or redistribute the code.
Author
Syuzanna Harutyunyan
Face ID Comparison is a Java desktop application for image processing, face extraction, ID/selfie comparison, grayscale histogram analysis, similarity calculation, and CSV result generation.
