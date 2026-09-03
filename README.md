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
6. Classifies the comparison result as either:
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

The application follows a simple image-processing pipeline:

1. The user selects the dataset folder.
2. The application searches for numbered dataset folders.
3. Images inside the folders are processed.
4. Relevant face regions are extracted from the images.
5. ID and selfie images are identified.
6. The extracted images are converted to grayscale.
7. Grayscale histograms are calculated.
8. Histograms are compared using correlation.
9. A similarity score is calculated.
10. The similarity score is used to classify the result.
11. Results are displayed in the graphical interface.
12. Results can be exported to a CSV file.

## Dataset Structure

The application expects a dataset containing numbered folders, from `1` to `10`.

A typical dataset can look like this:

```text
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
├── ...
│
└── 10/
    ├── ID_1.jpg
    ├── ID_2.jpg
    ├── selfie_1.jpg
    └── ...

Each numbered folder represents a dataset group containing ID and selfie images.
Image Processing
The application extracts the relevant face regions from the original images before performing the comparison.
This allows the comparison to focus primarily on the face region instead of the entire image.

The processing pipeline can be summarized as:

Original Images
       │
       ▼
Face Extraction
       │
       ▼
ID / Selfie Images
       │
       ▼
Grayscale Conversion
       │
       ▼
Histogram Generation
       │
       ▼
Histogram Correlation
       │
       ▼
Similarity Score
       │
       ▼
Classification
       │
   ┌───┴────┐
   ▼        ▼
SAME      DIFFERENT
PERSON

Grayscale Histogram Correlation
The application uses grayscale histograms to compare ID photos and selfies.
A grayscale histogram represents the distribution of pixel intensity values in an image.

The images are first converted to grayscale and then their histograms are calculated.

The histograms are compared using correlation to produce a similarity value.

In general:

A higher correlation indicates greater similarity between the images.
A lower correlation indicates greater difference between the images.
The similarity score is then compared against the configured classification threshold.
Classification
The application classifies each comparison into one of two categories:
SAME PERSON
The calculated similarity is above the configured threshold.
DIFFERENT
The calculated similarity is below the configured threshold.
The threshold can affect the final classification and should be selected according to the characteristics of the dataset.

User Interface
The application provides a Java Swing graphical interface with the following functionality:
Dataset folder selection
Folder browser
Complete pipeline execution
Face extraction progress
Comparison progress
Processing log
ID image preview
Selfie image preview
Similarity score display
Comparison decision display
Sortable comparison table
CSV result generation
The interface allows the user to process and review the dataset without manually opening and comparing every image.
Results
After the processing is complete, the application displays the comparison results in a sortable table.
A comparison result contains information such as:

Dataset	ID Image	Selfie Image	Similarity	Decision
1	ID_1.jpg	selfie_1.jpg	0.XX	SAME PERSON
2	ID_1.jpg	selfie_1.jpg	0.XX	DIFFERENT
3	ID_2.jpg	selfie_1.jpg	0.XX	SAME PERSON

The exact similarity values depend on the input images and processing results.
CSV Export
The application allows comparison results to be saved to a CSV file.
A typical CSV file can contain:

Dataset,ID Image,Selfie Image,Similarity,Decision
1,ID_1.jpg,selfie_1.jpg,0.XX,SAME PERSON
2,ID_1.jpg,selfie_1.jpg,0.XX,DIFFERENT
3,ID_2.jpg,selfie_1.jpg,0.XX,SAME PERSON

The CSV results can be used for:
Further analysis
Sorting and filtering
Spreadsheet processing
Statistical evaluation
Dataset comparison
Technologies
The project is built using:
Java
Java Swing
Image Processing
Grayscale Histograms
Histogram Correlation
CSV
Requirements
To run the application, you need:
Java Development Kit (JDK)
A Java-compatible IDE or build environment
The required project dependencies
A compatible image dataset
Installation
Clone the repository:
git clone <repository-url>

Navigate to the project directory:
cd <project-directory>

Open the project in a Java IDE such as:
IntelliJ IDEA
Eclipse
NetBeans
Visual Studio Code
Make sure all required dependencies are configured before running the application.
Usage
Start the application.
Select the dataset folder.
Make sure the dataset contains the expected numbered folders.
Start the complete processing pipeline.
Wait for face extraction to complete.
Wait for image comparison to complete.
Review the processing log.
Review the comparison results in the table.
Select a result to view the corresponding ID and selfie images.
Review the similarity score and classification.
Export the results to CSV if required.
Example
For a dataset containing:
Dataset/
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

The application processes each folder and compares the corresponding ID and selfie images.
A result may look like:

ID Image:       ID_1.jpg
Selfie Image:   selfie_1.jpg
Similarity:     0.XX
Decision:       SAME PERSON

Limitations
The current comparison method has several limitations.
Histogram-Based Comparison
A grayscale histogram describes the distribution of pixel intensities. It does not directly understand facial features or determine whether two faces belong to the same person.
Lighting
Different lighting conditions can significantly affect the grayscale histogram.
Pose
Differences in face angle, position, or expression can affect the similarity score.
Image Quality
Blurred, low-resolution, compressed, or poorly exposed images may produce less reliable results.
Background and Cropping
Differences in image cropping and remaining background information can also influence histogram correlation.
Biometric Accuracy
The application should not be considered a production biometric identity verification system.
The SAME PERSON and DIFFERENT classifications are based on the implemented image-comparison method and should not be treated as definitive proof of identity.

Future Improvements
Possible improvements include:
More accurate face detection
Face alignment
Improved image preprocessing
Automatic image resizing
More advanced image comparison methods
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
Privacy
ID documents and selfies may contain sensitive personal information.
When working with real datasets:

Only use images that you are authorized to process.
Do not upload private ID documents or selfies to a public repository.
Do not commit sensitive datasets to GitHub.
Use anonymized or synthetic images when possible.
Store processed images securely.
Follow applicable privacy and data-protection requirements.
It is recommended to add dataset and image files to .gitignore if they should not be committed to the repository.
Example:

dataset/
*.jpg
*.jpeg
*.png
*.webp

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

The actual structure may vary depending on the project implementation.
Disclaimer
This project is intended for educational and experimental purposes.
The image comparison method is based on grayscale histogram correlation and does not provide definitive biometric identity verification.

The results should not be used as the sole basis for authentication, identity verification, access control, or other high-risk decisions.

License
No license has been specified for this project yet.
If the project is published as open source, an appropriate license such as the MIT License, Apache License 2.0, or GPL-3.0 can be added.

Author
Syuzanna Harutyunyan
