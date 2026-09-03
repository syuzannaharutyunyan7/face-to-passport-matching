package com.faceid.ui;

import com.faceid.model.Result;
import com.faceid.service.ComparisonService;
import com.faceid.service.DatasetService;
import com.faceid.service.FaceExtractionService;
import com.faceid.service.FacePairComparator;
import com.faceid.service.HistogramService;
import com.faceid.service.PipelineService;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.faceid.config.AppConfig.THRESHOLD;

public class FaceComparisonFrame {

    private JFrame frame;

    private ControlPanel controlPanel;
    private ResultInfoPanel resultInfoPanel;
    private ImagePanel idImagePanel;
    private ImagePanel selfieImagePanel;
    private ResultsTablePanel resultsTablePanel;
    private BottomPanel bottomPanel;

    private final List<Result> results =
            new ArrayList<>();

    private final PipelineService pipelineService;

    public FaceComparisonFrame() {

        DatasetService datasetService =
                new DatasetService();

        FaceExtractionService faceExtractionService =
                new FaceExtractionService();

        HistogramService histogramService =
                new HistogramService();

        FacePairComparator facePairComparator =
                new FacePairComparator(
                        histogramService
                );

        ComparisonService comparisonService =
                new ComparisonService(
                        datasetService,
                        facePairComparator
                );

        pipelineService =
                new PipelineService(
                        datasetService,
                        faceExtractionService,
                        comparisonService
                );
    }

    public void createInterface() {

        frame =
                new JFrame(
                        "Face ID Comparison"
                );

        frame.setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        frame.setSize(
                1750,
                1100
        );

        frame.setMinimumSize(
                new Dimension(
                        1400,
                        900
                )
        );

        frame.setLocationRelativeTo(null);

        JPanel root =
                new JPanel(
                        new BorderLayout(
                                12,
                                12
                        )
                );

        root.setBackground(
                new Color(
                        238,
                        241,
                        245
                )
        );

        root.setBorder(
                BorderFactory.createEmptyBorder(
                        12,
                        12,
                        12,
                        12
                )
        );

        HeaderPanel header =
                new HeaderPanel();

        root.add(
                header,
                BorderLayout.NORTH
        );

        controlPanel =
                new ControlPanel(
                        this::browseFolder,
                        this::startPipeline
                );

        root.add(
                controlPanel,
                BorderLayout.WEST
        );

        JPanel center =
                createCenterPanel();

        root.add(
                center,
                BorderLayout.CENTER
        );

        bottomPanel =
                new BottomPanel();

        root.add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        frame.setContentPane(root);

        frame.setVisible(true);

        frame.toFront();

        frame.requestFocus();
    }

    private JPanel createCenterPanel() {

        JPanel center =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        center.setBackground(
                new Color(
                        238,
                        241,
                        245
                )
        );

        JPanel imageArea =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                20,
                                0
                        )
                );

        imageArea.setBackground(
                new Color(
                        238,
                        241,
                        245
                )
        );

        imageArea.setPreferredSize(
                new Dimension(
                        0,
                        620
                )
        );

        idImagePanel =
                new ImagePanel(
                        "ID PHOTO"
                );

        selfieImagePanel =
                new ImagePanel(
                        "SELFIE"
                );

        imageArea.add(
                idImagePanel
        );

        imageArea.add(
                selfieImagePanel
        );

        center.add(
                imageArea,
                BorderLayout.CENTER
        );

        resultInfoPanel =
                new ResultInfoPanel();

        center.add(
                resultInfoPanel,
                BorderLayout.NORTH
        );

        resultsTablePanel =
                new ResultsTablePanel(
                        this::handleTableSelection
                );

        center.add(
                resultsTablePanel,
                BorderLayout.SOUTH
        );

        return center;
    }

    private void handleTableSelection(
            int modelRow
    ) {

        if (
                modelRow >= 0 &&
                        modelRow < results.size()
        ) {

            showResult(
                    results.get(modelRow)
            );
        }
    }

    private void browseFolder() {

        JFileChooser chooser =
                new JFileChooser();

        chooser.setFileSelectionMode(
                JFileChooser.DIRECTORIES_ONLY
        );

        if (
                chooser.showOpenDialog(frame)
                        ==
                        JFileChooser.APPROVE_OPTION
        ) {

            controlPanel.setDatasetPath(
                    chooser
                            .getSelectedFile()
                            .getAbsolutePath()
            );
        }
    }

    private void startPipeline(
            JButton runButton
    ) {

        File mainFolder =
                new File(
                        controlPanel
                                .getDatasetPath()
                );

        if (
                !mainFolder.exists() ||
                        !mainFolder.isDirectory()
        ) {

            JOptionPane.showMessageDialog(
                    frame,
                    "Dataset folder does not exist.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        DatasetService datasetService =
                new DatasetService();

        File datasetFolder =
                datasetService.findDatasetFolder(
                        mainFolder
                );

        if (datasetFolder == null) {

            JOptionPane.showMessageDialog(
                    frame,
                    "Could not find folders 1-10.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        runButton.setEnabled(false);

        resultsTablePanel.clear();

        results.clear();

        bottomPanel
                .getLogArea()
                .setText("");

        clearImages();

        clearResult();

        Thread worker =
                new Thread(
                        () -> {

                            try {

                                pipelineService.runPipeline(
                                        datasetFolder,
                                        results,
                                        this::addResultToTable,
                                        this::log,
                                        this::setProgress
                                );

                                SwingUtilities.invokeLater(
                                        this::pipelineCompleted
                                );

                            } catch (Exception e) {

                                e.printStackTrace();

                                log(
                                        "ERROR: " +
                                                e.getMessage()
                                );

                                SwingUtilities.invokeLater(
                                        () -> {

                                            JOptionPane.showMessageDialog(
                                                    frame,
                                                    e.toString(),
                                                    "Pipeline Error",
                                                    JOptionPane.ERROR_MESSAGE
                                            );

                                            resultInfoPanel
                                                    .getStatusLabel()
                                                    .setText(
                                                            "ERROR"
                                                    );
                                        }
                                );

                            } finally {

                                SwingUtilities.invokeLater(
                                        () ->
                                                runButton.setEnabled(
                                                        true
                                                )
                                );
                            }
                        }
                );

        worker.start();
    }

    private void pipelineCompleted() {

        if (!results.isEmpty()) {

            resultsTablePanel
                    .selectFirstRow();

            showResult(
                    results.get(0)
            );

            resultInfoPanel
                    .getStatusLabel()
                    .setText(
                            "RESULTS READY"
                    );

        } else {

            resultInfoPanel
                    .getStatusLabel()
                    .setText(
                            "NO RESULTS"
                    );

            resultInfoPanel
                    .getDecisionLabel()
                    .setText(
                            "NO COMPARISONS"
                    );

            resultInfoPanel
                    .getSimilarityLabel()
                    .setText("-");

            JOptionPane.showMessageDialog(
                    frame,
                    "Pipeline completed, but no comparisons were produced.\n\n" +
                            "Check that your folders contain:\n" +
                            "ID_1 / ID_2 images and selfie_ images.",
                    "No Results",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void addResultToTable(
            Result result
    ) {

        SwingUtilities.invokeLater(
                () -> {

                    resultsTablePanel.addResult(
                            result
                    );

                    showResult(result);
                }
        );
    }

    private void showResult(
            Result result
    ) {

        if (result == null)
            return;

        JLabel decisionLabel =
                resultInfoPanel
                        .getDecisionLabel();

        JLabel similarityLabel =
                resultInfoPanel
                        .getSimilarityLabel();

        JLabel statusLabel =
                resultInfoPanel
                        .getStatusLabel();

        decisionLabel.setText(
                result.getDecision()
        );

        similarityLabel.setText(
                String.format(
                        "%.4f",
                        result.getSimilarity()
                )
        );

        statusLabel.setText(
                "FOLDER " +
                        result.getFolder() +
                        "  •  " +
                        result.getId() +
                        " vs " +
                        result.getSelfie()
        );

        if (
                result.getSimilarity() >=
                        THRESHOLD
        ) {

            decisionLabel.setForeground(
                    new Color(
                            0,
                            130,
                            60
                    )
            );

            similarityLabel.setForeground(
                    new Color(
                            0,
                            130,
                            60
                    )
            );

        } else {

            decisionLabel.setForeground(
                    new Color(
                            190,
                            35,
                            35
                    )
            );

            similarityLabel.setForeground(
                    new Color(
                            190,
                            35,
                            35
                    )
            );
        }

        showImage(
                idImagePanel.getImageLabel(),
                result.getIdFile()
        );

        showImage(
                selfieImagePanel.getImageLabel(),
                result.getSelfieFile()
        );
    }

    private void showImage(
            JLabel label,
            File file
    ) {

        if (
                file == null ||
                        !file.exists()
        ) {

            label.setIcon(null);

            label.setText(
                    "IMAGE NOT FOUND"
            );

            label.setForeground(Color.RED);

            return;
        }

        BufferedImage image;

        try {

            image =
                    ImageIO.read(file);

        } catch (IOException e) {

            label.setIcon(null);

            label.setText(
                    "COULD NOT OPEN IMAGE"
            );

            return;
        }

        if (image == null) {

            label.setIcon(null);

            label.setText(
                    "COULD NOT OPEN IMAGE"
            );

            return;
        }

        int imageWidth =
                image.getWidth();

        int imageHeight =
                image.getHeight();

        int labelWidth =
                Math.max(
                        100,
                        label.getWidth() - 30
                );

        int labelHeight =
                Math.max(
                        100,
                        label.getHeight() - 30
                );

        double scaleX =
                (double) labelWidth /
                        imageWidth;

        double scaleY =
                (double) labelHeight /
                        imageHeight;

        double scale =
                Math.min(
                        scaleX,
                        scaleY
                );

        int newWidth =
                Math.max(
                        1,
                        (int)
                                Math.round(
                                        imageWidth *
                                                scale
                                )
                );

        int newHeight =
                Math.max(
                        1,
                        (int)
                                Math.round(
                                        imageHeight *
                                                scale
                                )
                );

        Image scaled =
                image.getScaledInstance(
                        newWidth,
                        newHeight,
                        Image.SCALE_SMOOTH
                );

        label.setText("");

        label.setIcon(
                new ImageIcon(scaled)
        );

        label.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        label.setVerticalAlignment(
                SwingConstants.CENTER
        );

        label.setBackground(Color.WHITE);
    }

    private void log(
            String text
    ) {

        SwingUtilities.invokeLater(
                () -> {

                    JTextArea logArea =
                            bottomPanel.getLogArea();

                    logArea.append(
                            text + "\n"
                    );

                    logArea.setCaretPosition(
                            logArea
                                    .getDocument()
                                    .getLength()
                    );
                }
        );
    }

    private void setProgress(
            int value,
            String text
    ) {

        final int v =
                Math.max(
                        0,
                        Math.min(
                                100,
                                value
                        )
                );

        SwingUtilities.invokeLater(
                () -> {

                    bottomPanel
                            .getProgressBar()
                            .setValue(v);

                    bottomPanel
                            .getProgressBar()
                            .setString(text);

                    resultInfoPanel
                            .getStatusLabel()
                            .setText(text);
                }
        );
    }

    private void clearResult() {

        resultInfoPanel
                .getDecisionLabel()
                .setText(
                        "NO RESULT"
                );

        resultInfoPanel
                .getDecisionLabel()
                .setForeground(
                        Color.BLACK
                );

        resultInfoPanel
                .getSimilarityLabel()
                .setText("-");

        resultInfoPanel
                .getSimilarityLabel()
                .setForeground(
                        Color.BLACK
                );

        resultInfoPanel
                .getStatusLabel()
                .setText(
                        "READY"
                );
    }

    private void clearImages() {

        clearImageLabel(
                idImagePanel.getImageLabel()
        );

        clearImageLabel(
                selfieImagePanel.getImageLabel()
        );
    }

    private void clearImageLabel(
            JLabel label
    ) {

        label.setIcon(null);

        label.setText(
                "NO RESULT SELECTED"
        );

        label.setForeground(
                new Color(
                        80,
                        80,
                        80
                )
        );

        label.setBackground(
                new Color(
                        245,
                        246,
                        248
                )
        );
    }
}

