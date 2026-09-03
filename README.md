Of course. Here is the complete version formatted properly as GitHub Markdown. You can copy everything inside the block directly into README.md.
Writing


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

What the Application Does
The application follows these main steps:
Select the dataset folder.
Find the numbered dataset folders.
Extract face regions from the original images.
Locate the processed ID and selfie images.
Compare ID images with selfie images.
Calculate a similarity value using grayscale histograms.
Classify the comparison as:
SAME PERSON
DIFFERENT
Display the results in the application.
Save the comparison results to a CSV file.
User Interface
The application provides:
Dataset folder selection
Folder browser
Complete pipeline execution button
Face extraction progress
Comparison progress
Processing log
ID image preview
Selfie image preview
Similarity score
Comparison decision
Sortable comparison table
CSV result generation
Dataset Structure
The application expects a dataset containing numbered folders from 1 to 10.
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
│   └── ...
│
└── 10/
    └── ...

The application can also detect the dataset when the numbered folders are located inside a Files directory.
After processing, the application creates a Face_Faces directory containing the extracted images and the comparison CSV file.

For example:

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

Comparison Method
The current version uses image histograms rather than a machine-learning face recognition model.
For each processed image, the application creates a grayscale histogram and normalizes it. The histograms are then compared using correlation.

The similarity threshold currently used by the application is:

0.50

A similarity of 0.50 or higher is classified as:
SAME PERSON

A similarity below 0.50 is classified as:
DIFFERENT

This threshold and the comparison method are part of the current implementation and can be improved in future versions.
Output
After the pipeline finishes, a file named:
Comparison_Results.csv

is created inside the Face_Faces directory.
The CSV contains:

Folder
ID
Selfie
Similarity
Same_Person

This makes it possible to use the results outside the application as well.
Requirements
To run the built application, you need:
Java installed on your computer
A compatible Java runtime for the provided build
The dataset used by the application
The project uses Java Swing for the desktop interface and standard Java image-processing functionality.
Running the Application
If you are using the provided build version, open a terminal in the directory containing the built application and run it using the appropriate Java command for the build format.
For a JAR build, for example:

java -jar Face-ID-Comparison.jar

If the JAR has a different filename, replace the filename in the command with the actual name of the file.
Important Note
This project is an image-processing and comparison application. The SAME PERSON / DIFFERENT result should not be considered a production-grade biometric identification decision.
The current implementation is intended primarily for experimentation, development, and demonstration of image-processing techniques.

Factors such as lighting, image quality, pose, background, cropping, and other image differences can affect the similarity score.

Current Version
The repository currently contains the built version of the application so that it can be run without needing to open or modify the Java source code.
The source code and a more structured development version can be added to the repository in a future update.

Project Status
Current status: Working prototype / desktop application
The main processing pipeline is implemented and includes:

Image extraction
Image comparison
Result visualization
Progress reporting
CSV export
Future Improvements
Possible future improvements include:
Better face detection
More reliable face alignment
More advanced face comparison
Improved error handling
Configuration of comparison thresholds
Cleaner dataset configuration
Automated testing
A more portable application package
Author
Created by Syuzanna Harutyunyan.
This is ready to save as README.md and upload to GitHub.
