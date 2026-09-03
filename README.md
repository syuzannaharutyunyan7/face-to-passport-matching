Face ID Comparison
A Java desktop application for comparing ID photos and selfies using image processing and grayscale histogram correlation.
The application was created to make it easier to process a dataset of ID and selfie images, extract relevant face regions, compare the resulting images, and review the comparison results through a simple graphical interface.

Author
Syuzanna Harutyunyan
About the Project
Face ID Comparison is a Java Swing desktop application designed around a simple image-comparison pipeline.
The application processes a dataset containing numbered folders and performs the following operations:

Finds the dataset folders.
Extracts face regions from the original images.
Identifies ID and selfie images.
Compares ID images with selfie images.
Calculates a similarity value using grayscale histograms.
Classifies the comparison result as either:
SAME PERSON
DIFFERENT
Displays the results through a graphical user interface.
Allows the comparison results to be exported to a CSV file.
The main goal of the project is to provide a practical and easy-to-use tool for experimenting with image processing, face extraction, and image similarity comparison.
Note: This project is intended as an image-processing and comparison experiment. Histogram correlation is a visual similarity technique and should not be considered a production-grade biometric face-recognition system.
Features
Java desktop application
Java Swing graphical user interface
Dataset folder selection
Automatic detection of numbered dataset folders
Face-region extraction
ID and selfie image processing
Grayscale image conversion
Grayscale histogram generation
Histogram correlation for image comparison
Similarity score calculation
Automatic classification:
SAME PERSON
DIFFERENT
Processing progress indicators
Real-time processing log
ID image preview
Selfie image preview
Sortable comparison results table
CSV result generation
Batch processing of multiple dataset folders
How the Application Works
The application uses a multi-step image-processing pipeline.
1. Select Dataset
The user selects the root directory containing the dataset.
For example:

Dataset/
├── 1/
├── 2/
├── 3/
└── ...

2. Find Dataset Folders
The application searches for numbered folders inside the selected dataset directory.
Each numbered folder represents a dataset sample or comparison group.

3. Extract Face Regions
The application processes the images and extracts the relevant face regions.
This helps reduce the influence of unnecessary image content such as backgrounds and other objects.

4. Identify ID and Selfie Images
The application identifies the processed images as either ID images or selfie images based on the dataset naming/processing structure.
5. Compare Images
The application compares ID images with selfie images using grayscale histograms.
The images are converted to grayscale and their pixel-intensity distributions are analyzed.

6. Calculate Similarity
A histogram correlation value is calculated to estimate how similar the two images are.
The resulting similarity value is then used to classify the comparison.

7. Display Results
The graphical interface displays:
ID image
Selfie image
Similarity score
Comparison decision
Processing information
8. Export Results
The comparison results can be saved as a CSV file for further analysis.
Image Comparison Method
The project uses grayscale histogram correlation as its primary comparison method.
A grayscale image contains pixel values representing brightness, typically ranging from:

0   → Black
255 → White

A histogram represents the distribution of these grayscale values within an image.
For example:

Pixel intensity
0 ─────────────────────── 255
│
│       ███
│    ███████
│  ███████████
│ █████████████
└──────────────────────────
       Histogram

The histograms of two images are compared using correlation.
A higher correlation indicates that the grayscale distributions are more similar, while a lower correlation indicates greater visual difference.

Important Consideration
Histogram correlation compares image characteristics rather than understanding facial identity.
Factors such as:

lighting
image quality
pose
facial expression
background
cropping
camera quality
can influence the similarity score.
Therefore, the SAME PERSON / DIFFERENT result should be interpreted as the output of the implemented image-comparison algorithm rather than definitive identity verification.

Classification
The similarity score is compared against a configured threshold.
Conceptually:

Similarity Score
       │
       ▼
   ┌───────────┐
   │ Compare   │
   │ threshold │
   └─────┬─────┘
         │
    ┌────┴────┐
    │         │
  >= T       < T
    │         │
    ▼         ▼
SAME       DIFFERENT
PERSON

Where T represents the configured similarity threshold.
The threshold can be adjusted depending on the dataset and the desired comparison sensitivity.

User Interface
The application provides a graphical interface built with Java Swing.
The interface includes the following components:

Dataset Selection
Allows the user to select the root dataset folder.
Folder Browser
Displays or allows navigation through the selected dataset directory.
Pipeline Execution
A button is provided to start the complete processing pipeline.
Face Extraction Progress
Shows the progress of face-region extraction.
Comparison Progress
Displays the progress of ID/selfie comparisons.
Processing Log
Provides information about the current processing stage, including successful operations and errors.
Image Preview
The interface displays the images being compared:
ID image
Selfie image
Similarity Score
Displays the calculated similarity value for the selected comparison.
Comparison Decision
Displays the final classification:
SAME PERSON

or
DIFFERENT

Results Table
A sortable table allows the user to review multiple comparisons.
CSV Export
The comparison results can be exported to a CSV file for further processing.
Dataset Structure
The application expects a dataset containing numbered folders.
For example:

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
└── 10/
    ├── ID_1.jpg
    ├── ID_2.jpg
    ├── selfie_1.jpg
    └── ...

Each numbered folder represents one dataset group.
For example:

1/

may contain all ID and selfie images belonging to one person or comparison group.
The exact image naming convention should match the expectations of the application's processing logic.

Example Dataset
A complete dataset might look like:
Dataset/
│
├── 1/
│   ├── ID_1.jpg
│   ├── ID_2.jpg
│   ├── selfie_1.jpg
│   └── selfie_2.jpg
│
├── 2/
│   ├── ID_1.jpg
│   ├── ID_2.jpg
│   ├── selfie_1.jpg
│   └── selfie_2.jpg
│
├── 3/
│   ├── ID_1.jpg
│   ├── selfie_1.jpg
│   └── selfie_2.jpg
│
└── 10/
    ├── ID_1.jpg
    └── selfie_1.jpg

Processing Pipeline
The complete workflow can be summarized as:
                 Dataset
                    │
                    ▼
          Select Dataset Folder
                    │
                    ▼
        Find Numbered Directories
                    │
                    ▼
             Load Images
                    │
                    ▼
          Extract Face Regions
                    │
                    ▼
          Identify ID / Selfie
                    │
                    ▼
          Convert Images to
              Grayscale
                    │
                    ▼
          Generate Histograms
                    │
                    ▼
       Calculate Histogram
            Correlation
                    │
                    ▼
          Apply Threshold
                    │
             ┌──────┴──────┐
             ▼             ▼
        SAME PERSON     DIFFERENT
             │             │
             └──────┬──────┘
                    ▼
            Display Results
                    │
                    ▼
               Export CSV

Results
The application produces comparison results containing information such as:
Dataset	ID Image	Selfie Image	Similarity	Decision
1	ID_1.jpg	selfie_1.jpg	0.XX	SAME PERSON
2	ID_1.jpg	selfie_1.jpg	0.XX	DIFFERENT
3	ID_2.jpg	selfie_1.jpg	0.XX	SAME PERSON

The exact columns and values depend on the implementation and dataset.
CSV Output
After processing, the application can save comparison results to a CSV file.
A typical CSV structure may look like:

Dataset,ID Image,Selfie Image,Similarity,Decision
1,ID_1.jpg,selfie_1.jpg,0.XX,SAME PERSON
2,ID_1.jpg,selfie_1.jpg,0.XX,DIFFERENT
3,ID_2.jpg,selfie_1.jpg,0.XX,SAME PERSON

The CSV file makes it easier to:
analyze results
filter comparisons
calculate statistics
import results into Excel
perform additional data analysis
compare different threshold values
Requirements
Before running the application, make sure you have:
Java Development Kit (JDK)
A Java-compatible development environment or build system
The required project dependencies
A compatible image dataset
It is recommended to use a modern JDK version compatible with the project's source code.
Installation
1. Clone the Repository
Clone the repository:
git clone https://github.com/YOUR_USERNAME/face-id-comparison.git

Move into the project directory:
cd face-id-comparison

2. Open the Project
Open the project in your preferred Java IDE, such as:
IntelliJ IDEA
Eclipse
NetBeans
Visual Studio Code with Java extensions
3. Configure Dependencies
Make sure all required project dependencies are available and correctly configured.
If the project uses Maven, run:

mvn clean install

If the project uses Gradle, run:
./gradlew build

If the project does not use a build system, configure the required libraries through the IDE.
Running the Application
After configuring the project, run the application's main Java class from your IDE.
Alternatively, if the project is packaged as a JAR:

java -jar face-id-comparison.jar

The graphical interface should open after the application starts.
Basic Usage
Start the application.
Select the dataset directory.
Verify that the dataset contains the expected numbered folders.
Start the complete processing pipeline.
Wait for face extraction to finish.
Wait for image comparison to finish.
Review the processing log.
Select individual comparisons from the results table.
Inspect the ID and selfie previews.
Review the similarity score and classification.
Export the results to CSV if required.
Error Handling
The application should handle common processing problems such as:
Invalid dataset folders
Missing images
Unsupported image files
Images where a face cannot be detected
Empty folders
Invalid image paths
Image-processing errors
Processing information and errors are displayed in the application log where applicable.
Project Structure
A possible project structure is:
face-id-comparison/
│
├── src/
│   └── ...
│
├── dataset/
│   └── ...
│
├── README.md
├── pom.xml
└── ...

The exact structure depends on the project configuration.
Technologies
The project is built primarily with:
Java
Java Swing
Image Processing
Grayscale Histograms
Histogram Correlation
CSV Export
Use Cases
This project can be useful for:
Experimenting with image processing
Learning Java Swing
Processing image datasets
Exploring histogram-based image comparison
Comparing ID photos and selfies
Building simple computer-vision workflows
Studying the effects of image preprocessing on similarity
Generating structured comparison results
Limitations
The current approach has several limitations.
Histogram-Based Comparison
A grayscale histogram describes the distribution of pixel intensities. It does not capture the complete spatial structure of a face.
Two different faces with similar lighting and image characteristics may produce similar histogram distributions.

Lighting Sensitivity
Different lighting conditions can significantly change the grayscale histogram.
Pose and Expression
Differences in:
head position
facial angle
facial expression
distance from the camera
may affect the resulting similarity score.
Image Quality
Blurred, compressed, low-resolution, or poorly exposed images can reduce comparison reliability.
Face Detection
If the face extraction stage fails, the resulting comparison may not be meaningful.
Not a Biometric Verification System
This application should not be treated as a secure identity-verification or authentication system.
For production identity verification, specialized face-recognition/biometric algorithms, appropriate evaluation datasets, security controls, and privacy safeguards would be required.

Future Improvements
Possible future improvements include:
More robust face detection
Face alignment before comparison
Improved image preprocessing
Automatic image resizing
Multiple comparison algorithms
Feature-based face comparison
Deep-learning-based face embeddings
Adjustable similarity thresholds through the UI
Configuration files for processing parameters
More detailed CSV reports
Statistics and result visualization
Support for larger datasets
Parallel image processing
Better error reporting
Unit and integration tests
Packaging the application as a standalone executable
Privacy Considerations
ID documents and selfies can contain sensitive personal information.
When using this application:

Use only images you are authorized to process.
Avoid uploading personal datasets to public repositories.
Do not commit real ID documents or selfies to GitHub.
Remove sensitive images before publishing the project.
Consider adding dataset directories to .gitignore.
Follow applicable privacy and data-protection requirements.
For example, a .gitignore file can contain:
dataset/
*.jpg
*.jpeg
*.png
*.webp

This helps prevent accidentally committing image data to the repository.
Contributing
Contributions and improvements are welcome.
To contribute:

Fork the repository.
Create a new branch:
git checkout -b feature/my-improvement

Make your changes.
Test the application.
Commit your changes:
git commit -m "Add my improvement"

Push the branch:
git push origin feature/my-improvement

Open a Pull Request.
License
If this project does not currently have a license, you can add one before publishing it publicly.
For example, you may choose the MIT License and add a LICENSE file to the repository.

Author
Syuzanna Harutyunyan
Created as a practical Java image-processing project for experimenting with face extraction, image comparison, grayscale histograms, and graphical result analysis.

Acknowledgements
This project demonstrates how traditional image-processing techniques can be combined with a Java desktop interface to create a simple image-comparison workflow.
Disclaimer
This software is provided for educational and experimental purposes.
The comparison result is based on image-processing techniques and should not be interpreted as definitive proof that two images belong to the same person.

The author is not responsible for decisions made solely on the basis of the application's comparison results.

