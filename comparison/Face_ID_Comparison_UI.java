import ij.IJ;
import ij.ImagePlus;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Face_ID_Comparison_UI implements PlugIn {

    private JFrame frame;

    private JTextField pathField;
    private JTextArea logArea;
    private JProgressBar progressBar;

    private JTable resultTable;
    private DefaultTableModel tableModel;

    private JLabel statusLabel;
    private JLabel similarityLabel;
    private JLabel decisionLabel;

    private JLabel idImageLabel;
    private JLabel selfieImageLabel;

    private final List<Result> results = new ArrayList<>();

    private static final int MIN_HUE = 3;
    private static final int MAX_HUE = 24;
    private static final int N = 256;
    private static final double LOG_THRESHOLD = 3.0;
    private static final int RANGE_START = 40;
    private static final int RANGE_END = 80;
    private static final double THRESHOLD = 0.50;

    // =========================================================
    // RESULT CLASS
    // =========================================================

    private static class Result {

        String folder;
        String id;
        String selfie;
        double similarity;
        String decision;

        File idFile;
        File selfieFile;

        Result(
                String folder,
                String id,
                String selfie,
                double similarity,
                File idFile,
                File selfieFile
        ) {

            this.folder = folder;
            this.id = id;
            this.selfie = selfie;
            this.similarity = similarity;

            this.decision =
                    similarity >= THRESHOLD
                            ? "SAME PERSON"
                            : "DIFFERENT";

            this.idFile = idFile;
            this.selfieFile = selfieFile;
        }
    }

    // =========================================================
    // RUN
    // =========================================================

    @Override
    public void run(String arg) {

        SwingUtilities.invokeLater(
                this::createInterface
        );
    }

    // =========================================================
    // INTERFACE
    // =========================================================

    private void createInterface() {

        frame = new JFrame(
                "Face ID Comparison"
        );

        frame.setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        // Larger window
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

        // =====================================================
        // HEADER
        // =====================================================

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setBackground(
                new Color(
                        35,
                        70,
                        105
                )
        );

        header.setBorder(
                BorderFactory.createEmptyBorder(
                        18,
                        22,
                        18,
                        22
                )
        );

        JLabel title =
                new JLabel(
                        "FACE ID COMPARISON"
                );

        title.setForeground(
                Color.WHITE
        );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        JLabel subtitle =
                new JLabel(
                        "Face extraction and ID/selfie comparison"
                );

        subtitle.setForeground(
                new Color(
                        225,
                        235,
                        245
                )
        );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        JPanel headerText =
                new JPanel();

        headerText.setOpaque(false);

        headerText.setLayout(
                new BoxLayout(
                        headerText,
                        BoxLayout.Y_AXIS
                )
        );

        headerText.add(title);

        headerText.add(
                Box.createVerticalStrut(6)
        );

        headerText.add(subtitle);

        header.add(
                headerText,
                BorderLayout.WEST
        );

        root.add(
                header,
                BorderLayout.NORTH
        );

        // =====================================================
        // LEFT CONTROL PANEL
        // =====================================================

        JPanel controls =
                new JPanel();

        controls.setBackground(
                Color.WHITE
        );

        controls.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        205,
                                        210,
                                        215
                                )
                        ),
                        BorderFactory.createEmptyBorder(
                                15,
                                15,
                                15,
                                15
                        )
                )
        );

        controls.setPreferredSize(
                new Dimension(
                        310,
                        0
                )
        );

        controls.setLayout(
                new BoxLayout(
                        controls,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel datasetLabel =
                blackLabel(
                        "DATASET FOLDER",
                        15,
                        Font.BOLD
                );

        controls.add(
                datasetLabel
        );

        controls.add(
                Box.createVerticalStrut(8)
        );

        pathField =
                new JTextField();

        pathField.setText(
                "/Users/syuzannaharutyunyan/Desktop/Selfie-and-ID-Dataset-main/"
        );

        pathField.setForeground(
                Color.BLACK
        );

        pathField.setBackground(
                Color.WHITE
        );

        pathField.setCaretColor(
                Color.BLACK
        );

        controls.add(
                pathField
        );

        controls.add(
                Box.createVerticalStrut(8)
        );

        JButton browseButton =
                new JButton(
                        "BROWSE"
                );

        styleButton(
                browseButton,
                Color.WHITE,
                Color.BLACK
        );

        browseButton.addActionListener(
                e -> browseFolder()
        );

        controls.add(
                browseButton
        );

        controls.add(
                Box.createVerticalStrut(15)
        );

        // =====================================================
        // RUN COMPLETE PIPELINE
        // WHITE BACKGROUND + BLACK LETTERS
        // =====================================================

        JButton runButton =
                new JButton(
                        "RUN COMPLETE PIPELINE"
                );

        runButton.setBackground(
                Color.WHITE
        );

        runButton.setForeground(
                Color.BLACK
        );

        runButton.setOpaque(true);

        runButton.setContentAreaFilled(true);

        runButton.setFocusPainted(false);

        runButton.setBorderPainted(true);

        runButton.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                Color.BLACK,
                                2
                        ),
                        BorderFactory.createEmptyBorder(
                                10,
                                10,
                                10,
                                10
                        )
                )
        );

        runButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        runButton.setPreferredSize(
                new Dimension(
                        280,
                        52
                )
        );

        runButton.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        52
                )
        );

        runButton.addActionListener(
                e -> startPipeline(runButton)
        );

        controls.add(
                runButton
        );

        controls.add(
                Box.createVerticalStrut(25)
        );

        JLabel pipelineTitle =
                blackLabel(
                        "PIPELINE",
                        15,
                        Font.BOLD
                );

        controls.add(
                pipelineTitle
        );

        controls.add(
                Box.createVerticalStrut(10)
        );

        JLabel pipeline =
                blackLabel(
                        "<html>" +
                                "1. Extract faces<br><br>" +
                                "2. Find ID 1 / ID 2<br><br>" +
                                "3. Find selfies<br><br>" +
                                "4. Compare histograms<br><br>" +
                                "5. Display results<br><br>" +
                                "6. Save CSV" +
                                "</html>",
                        13,
                        Font.PLAIN
                );

        controls.add(
                pipeline
        );

        controls.add(
                Box.createVerticalGlue()
        );

        JLabel thresholdText =
                blackLabel(
                        "<html><b>Similarity threshold:</b><br>" +
                                "0.50 = SAME PERSON<br>" +
                                "Below 0.50 = DIFFERENT</html>",
                        13,
                        Font.PLAIN
                );

        controls.add(
                thresholdText
        );

        root.add(
                controls,
                BorderLayout.WEST
        );

        // =====================================================
        // CENTER
        // =====================================================

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

        // =====================================================
        // IMAGE AREA
        // =====================================================

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

        // IMPORTANT:
        // Larger image area so complete photos can be visible.
        imageArea.setPreferredSize(
                new Dimension(
                        0,
                        620
                )
        );

        JPanel idPanel =
                createImagePanel(
                        "ID PHOTO",
                        true
                );

        JPanel selfiePanel =
                createImagePanel(
                        "SELFIE",
                        false
                );

        imageArea.add(
                idPanel
        );

        imageArea.add(
                selfiePanel
        );

        center.add(
                imageArea,
                BorderLayout.CENTER
        );

        // =====================================================
        // RESULT INFORMATION
        // =====================================================

        JPanel resultInfo =
                new JPanel(
                        new GridLayout(
                                1,
                                3,
                                10,
                                0
                        )
                );

        resultInfo.setBackground(
                new Color(
                        238,
                        241,
                        245
                )
        );

        resultInfo.setPreferredSize(
                new Dimension(
                        0,
                        105
                )
        );

        JPanel decisionBox =
                createInfoBox(
                        "DECISION"
                );

        decisionLabel =
                blackLabel(
                        "NO RESULT",
                        22,
                        Font.BOLD
                );

        decisionLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        decisionBox.add(
                decisionLabel,
                BorderLayout.CENTER
        );

        JPanel similarityBox =
                createInfoBox(
                        "SIMILARITY"
                );

        similarityLabel =
                blackLabel(
                        "-",
                        26,
                        Font.BOLD
                );

        similarityLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        similarityBox.add(
                similarityLabel,
                BorderLayout.CENTER
        );

        JPanel statusBox =
                createInfoBox(
                        "STATUS"
                );

        statusLabel =
                blackLabel(
                        "READY",
                        18,
                        Font.BOLD
                );

        statusLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        statusBox.add(
                statusLabel,
                BorderLayout.CENTER
        );

        resultInfo.add(
                decisionBox
        );

        resultInfo.add(
                similarityBox
        );

        resultInfo.add(
                statusBox
        );

        center.add(
                resultInfo,
                BorderLayout.NORTH
        );

        // =====================================================
        // TABLE
        // =====================================================

        String[] columns = {
                "Folder",
                "ID",
                "Selfie",
                "Similarity",
                "Decision"
        };

        tableModel =
                new DefaultTableModel(
                        columns,
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };

        resultTable =
                new JTable(
                        tableModel
                );

        resultTable.setForeground(
                Color.BLACK
        );

        resultTable.setBackground(
                Color.WHITE
        );

        resultTable.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        resultTable.setRowHeight(
                32
        );

        resultTable.setGridColor(
                new Color(
                        220,
                        220,
                        220
                )
        );

        resultTable.setSelectionBackground(
                new Color(
                        185,
                        215,
                        245
                )
        );

        resultTable.setSelectionForeground(
                Color.BLACK
        );

        resultTable.getTableHeader()
                .setBackground(
                        new Color(
                                40,
                                75,
                                110
                        )
                );

        resultTable.getTableHeader()
                .setForeground(
                        Color.WHITE
                );

        resultTable.getTableHeader()
                .setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                13
                        )
                );

        resultTable.setAutoCreateRowSorter(
                true
        );

        DefaultTableCellRenderer renderer =
                new DefaultTableCellRenderer() {

                    @Override
                    public Component
                    getTableCellRendererComponent(
                            JTable table,
                            Object value,
                            boolean selected,
                            boolean focused,
                            int row,
                            int column
                    ) {

                        Component c =
                                super.getTableCellRendererComponent(
                                        table,
                                        value,
                                        selected,
                                        focused,
                                        row,
                                        column
                                );

                        if (!selected) {

                            c.setBackground(
                                    Color.WHITE
                            );

                            c.setForeground(
                                    Color.BLACK
                            );

                            if (
                                    column == 4 &&
                                            value != null
                            ) {

                                String text =
                                        value.toString();

                                if (
                                        text.equals(
                                                "SAME PERSON"
                                        )
                                ) {

                                    c.setForeground(
                                            new Color(
                                                    0,
                                                    125,
                                                    55
                                            )
                                    );

                                } else {

                                    c.setForeground(
                                            new Color(
                                                    190,
                                                    30,
                                                    30
                                            )
                                    );
                                }
                            }
                        }

                        return c;
                    }
                };

        for (
                int i = 0;
                i < resultTable
                        .getColumnCount();
                i++
        ) {

            resultTable
                    .getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(
                            renderer
                    );
        }

        resultTable
                .getSelectionModel()
                .addListSelectionListener(
                        e -> {

                            if (
                                    e.getValueIsAdjusting()
                            )
                                return;

                            int row =
                                    resultTable
                                            .getSelectedRow();

                            if (row < 0)
                                return;

                            int modelRow =
                                    resultTable
                                            .convertRowIndexToModel(
                                                    row
                                            );

                            if (
                                    modelRow >= 0 &&
                                            modelRow <
                                                    results.size()
                            ) {

                                showResult(
                                        results.get(
                                                modelRow
                                        )
                                );
                            }
                        }
                );

        JScrollPane tableScroll =
                new JScrollPane(
                        resultTable
                );

        tableScroll.setPreferredSize(
                new Dimension(
                        0,
                        250
                )
        );

        JPanel tablePanel =
                new JPanel(
                        new BorderLayout()
                );

        tablePanel.setBackground(
                Color.WHITE
        );

        tablePanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        205,
                                        210,
                                        215
                                )
                        ),
                        BorderFactory.createEmptyBorder(
                                5,
                                5,
                                5,
                                5
                        )
                )
        );

        JLabel tableTitle =
                blackLabel(
                        "COMPARISON RESULTS",
                        16,
                        Font.BOLD
                );

        tableTitle.setBorder(
                BorderFactory.createEmptyBorder(
                        5,
                        8,
                        8,
                        8
                )
        );

        tablePanel.add(
                tableTitle,
                BorderLayout.NORTH
        );

        tablePanel.add(
                tableScroll,
                BorderLayout.CENTER
        );

        center.add(
                tablePanel,
                BorderLayout.SOUTH
        );

        root.add(
                center,
                BorderLayout.CENTER
        );

        // =====================================================
        // BOTTOM LOG / PROGRESS
        // =====================================================

        JPanel bottom =
                new JPanel(
                        new BorderLayout(
                                8,
                                8
                        )
                );

        bottom.setBackground(
                new Color(
                        238,
                        241,
                        245
                )
        );

        logArea =
                new JTextArea(
                        5,
                        30
                );

        logArea.setEditable(
                false
        );

        logArea.setForeground(
                Color.BLACK
        );

        logArea.setBackground(
                Color.WHITE
        );

        logArea.setCaretColor(
                Color.BLACK
        );

        logArea.setFont(
                new Font(
                        "Monospaced",
                        Font.PLAIN,
                        12
                )
        );

        JScrollPane logScroll =
                new JScrollPane(
                        logArea
                );

        progressBar =
                new JProgressBar(
                        0,
                        100
                );

        progressBar.setStringPainted(
                true
        );

        progressBar.setForeground(
                new Color(
                        30,
                        135,
                        75
                )
        );

        progressBar.setBackground(
                new Color(
                        215,
                        220,
                        225
                )
        );

        progressBar.setString(
                "READY"
        );

        bottom.add(
                logScroll,
                BorderLayout.CENTER
        );

        bottom.add(
                progressBar,
                BorderLayout.SOUTH
        );

        root.add(
                bottom,
                BorderLayout.SOUTH
        );

        frame.setContentPane(
                root
        );

        frame.setVisible(
                true
        );

        frame.toFront();

        frame.requestFocus();
    }

    // =========================================================
    // LARGE IMAGE PANELS
    // =========================================================

    private JPanel createImagePanel(
            String title,
            boolean id
    ) {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setBackground(
                Color.WHITE
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        180,
                                        185,
                                        190
                                ),
                                2
                        ),
                        BorderFactory.createEmptyBorder(
                                8,
                                8,
                                8,
                                8
                        )
                )
        );

        // Larger preferred size for each photo square
        panel.setPreferredSize(
                new Dimension(
                        650,
                        620
                )
        );

        JLabel titleLabel =
                blackLabel(
                        title,
                        18,
                        Font.BOLD
                );

        titleLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        titleLabel.setBorder(
                BorderFactory.createEmptyBorder(
                        5,
                        5,
                        10,
                        5
                )
        );

        panel.add(
                titleLabel,
                BorderLayout.NORTH
        );

        JLabel imageLabel =
                new JLabel(
                        "NO RESULT SELECTED",
                        SwingConstants.CENTER
                );

        imageLabel.setForeground(
                new Color(
                        80,
                        80,
                        80
                )
        );

        imageLabel.setBackground(
                new Color(
                        245,
                        246,
                        248
                )
        );

        imageLabel.setOpaque(
                true
        );

        imageLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );

        imageLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        imageLabel.setVerticalAlignment(
                SwingConstants.CENTER
        );

        if (id) {

            idImageLabel =
                    imageLabel;

        } else {

            selfieImageLabel =
                    imageLabel;
        }

        panel.add(
                imageLabel,
                BorderLayout.CENTER
        );

        return panel;
    }

    // =========================================================
    // INFO BOX
    // =========================================================

    private JPanel createInfoBox(
            String title
    ) {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setBackground(
                Color.WHITE
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        205,
                                        210,
                                        215
                                )
                        ),
                        BorderFactory.createEmptyBorder(
                                8,
                                8,
                                8,
                                8
                        )
                )
        );

        JLabel titleLabel =
                blackLabel(
                        title,
                        12,
                        Font.BOLD
                );

        titleLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        panel.add(
                titleLabel,
                BorderLayout.NORTH
        );

        return panel;
    }

    // =========================================================
    // BUTTON STYLE
    // =========================================================

    private void styleButton(
            JButton button,
            Color background,
            Color foreground
    ) {

        button.setBackground(
                background
        );

        button.setForeground(
                foreground
        );

        button.setFocusPainted(
                false
        );

        button.setOpaque(
                true
        );

        button.setContentAreaFilled(
                true
        );

        button.setBorderPainted(
                true
        );

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );
    }

    // =========================================================
    // BLACK LABEL
    // =========================================================

    private JLabel blackLabel(
            String text,
            int size,
            int style
    ) {

        JLabel label =
                new JLabel(
                        text
                );

        label.setForeground(
                Color.BLACK
        );

        label.setFont(
                new Font(
                        "Arial",
                        style,
                        size
                )
        );

        return label;
    }

    // =========================================================
    // BROWSE
    // =========================================================

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

            pathField.setText(
                    chooser
                            .getSelectedFile()
                            .getAbsolutePath()
            );
        }
    }

    // =========================================================
    // START PIPELINE
    // =========================================================

    private void startPipeline(
            JButton runButton
    ) {

        File mainFolder =
                new File(
                        pathField
                                .getText()
                                .trim()
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

        File datasetFolder =
                findDatasetFolder(
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

        runButton.setEnabled(
                false
        );

        tableModel.setRowCount(
                0
        );

        results.clear();

        logArea.setText(
                ""
        );

        clearImages();

        clearResult();

        Thread worker =
                new Thread(
                        () -> {

                            try {

                                runPipeline(
                                        datasetFolder
                                );

                            } catch (
                                    Exception e
                            ) {

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

                                            statusLabel.setText(
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

    // =========================================================
    // PIPELINE
    // =========================================================

    private void runPipeline(
            File datasetFolder
    ) throws Exception {

        log(
                "Dataset: " +
                        datasetFolder
                                .getAbsolutePath()
        );

        File facesDir =
                new File(
                        datasetFolder,
                        "Face_Faces"
                );

        if (!facesDir.exists()) {

            facesDir.mkdirs();
        }

        File[] folders =
                datasetFolder.listFiles(
                        File::isDirectory
                );

        List<File> validFolders =
                new ArrayList<>();

        if (folders != null) {

            for (File folder : folders) {

                if (
                        isNumberFolder(
                                folder.getName()
                        )
                ) {

                    validFolders.add(
                            folder
                    );
                }
            }
        }

        if (validFolders.isEmpty()) {

            throw new Exception(
                    "No numbered dataset folders 1-10 found."
            );
        }

        int totalImages = 0;

        for (File folder : validFolders) {

            File[] files =
                    getOriginalImages(
                            folder
                    );

            if (files != null) {

                totalImages +=
                        files.length;
            }
        }

        log(
                "Dataset folders found: " +
                        validFolders.size()
        );

        log(
                "Images found: " +
                        totalImages
        );

        int processed = 0;

        for (File folder : validFolders) {

            File outputFolder =
                    new File(
                            facesDir,
                            folder.getName()
                    );

            outputFolder.mkdirs();

            File[] files =
                    getOriginalImages(
                            folder
                    );

            if (files == null)
                continue;

            for (File file : files) {

                log(
                        "Extracting: " +
                                folder.getName() +
                                "/" +
                                file.getName()
                );

                extractFace(
                        file,
                        outputFolder
                );

                processed++;

                int percent;

                if (totalImages == 0) {

                    percent = 50;

                } else {

                    percent =
                            (int)
                                    (
                                            processed *
                                                    60.0 /
                                                    totalImages
                                    );
                }

                setProgress(
                        percent,
                        "EXTRACTING FACES " +
                                percent +
                                "%"
                );
            }
        }

        log(
                "FACE EXTRACTION COMPLETE"
        );

        setProgress(
                60,
                "FACE EXTRACTION COMPLETE"
        );

        compareFaces(
                facesDir
        );

        setProgress(
                100,
                "COMPLETE"
        );

        log("");

        log(
                "========================================"
        );

        log(
                "PIPELINE COMPLETE"
        );

        log(
                "COMPARISON RESULTS: " +
                        results.size()
        );

        log(
                "========================================"
        );

        File csv =
                new File(
                        facesDir,
                        "Comparison_Results.csv"
                );

        log(
                "CSV: " +
                        csv.getAbsolutePath()
        );

        SwingUtilities.invokeLater(
                () -> {

                    if (!results.isEmpty()) {

                        resultTable
                                .setRowSelectionInterval(
                                        0,
                                        0
                                );

                        showResult(
                                results.get(0)
                        );

                        statusLabel.setText(
                                "RESULTS READY"
                        );

                    } else {

                        statusLabel.setText(
                                "NO RESULTS"
                        );

                        decisionLabel.setText(
                                "NO COMPARISONS"
                        );

                        similarityLabel.setText(
                                "-"
                        );

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
        );
    }

    // =========================================================
    // COMPARISON
    // =========================================================

    private void compareFaces(
            File facesDir
    ) throws Exception {

        File[] folders =
                facesDir.listFiles(
                        File::isDirectory
                );

        if (
                folders == null ||
                        folders.length == 0
        ) {

            throw new Exception(
                    "Face_Faces folder is empty."
            );
        }

        File csv =
                new File(
                        facesDir,
                        "Comparison_Results.csv"
                );

        PrintWriter writer =
                new PrintWriter(
                        new FileWriter(csv)
                );

        writer.println(
                "Folder,ID,Selfie,Similarity,Same_Person"
        );

        int totalComparisons = 0;

        for (File folder : folders) {

            if (
                    !isNumberFolder(
                            folder.getName()
                    )
            ) {

                continue;
            }

            File[] selfies =
                    findSelfies(
                            folder
                    );

            if (
                    selfies == null ||
                            selfies.length == 0
            ) {

                continue;
            }

            File id1 =
                    findFaceFile(
                            folder,
                            "ID_1_face"
                    );

            File id2 =
                    findFaceFile(
                            folder,
                            "ID_2_face"
                    );

            if (id1 != null) {

                totalComparisons +=
                        selfies.length;
            }

            if (id2 != null) {

                totalComparisons +=
                        selfies.length;
            }
        }

        log(
                "Comparisons to perform: " +
                        totalComparisons
        );

        int completed = 0;

        for (File folder : folders) {

            if (
                    !isNumberFolder(
                            folder.getName()
                    )
            ) {

                continue;
            }

            File[] selfies =
                    findSelfies(
                            folder
                    );

            if (
                    selfies == null ||
                            selfies.length == 0
            ) {

                continue;
            }

            File id1 =
                    findFaceFile(
                            folder,
                            "ID_1_face"
                    );

            File id2 =
                    findFaceFile(
                            folder,
                            "ID_2_face"
                    );

            if (id1 != null) {

                for (File selfie : selfies) {

                    Result result =
                            comparePair(
                                    folder.getName(),
                                    "ID_1",
                                    id1,
                                    selfie
                            );

                    if (result != null) {

                        results.add(
                                result
                        );

                        writer.println(
                                csvLine(result)
                        );

                        writer.flush();

                        addResultToTable(
                                result
                        );
                    }

                    completed++;

                    updateComparisonProgress(
                            completed,
                            totalComparisons
                    );
                }
            }

            if (id2 != null) {

                for (File selfie : selfies) {

                    Result result =
                            comparePair(
                                    folder.getName(),
                                    "ID_2",
                                    id2,
                                    selfie
                            );

                    if (result != null) {

                        results.add(
                                result
                        );

                        writer.println(
                                csvLine(result)
                        );

                        writer.flush();

                        addResultToTable(
                                result
                        );
                    }

                    completed++;

                    updateComparisonProgress(
                            completed,
                            totalComparisons
                    );
                }
            }
        }

        writer.close();

        log(
                "Comparison CSV saved."
        );
    }

    // =========================================================
    // COMPARISON PROGRESS
    // =========================================================

    private void updateComparisonProgress(
            int completed,
            int total
    ) {

        int percent;

        if (total <= 0) {

            percent = 100;

        } else {

            percent =
                    60 +
                            (int)
                                    (
                                            completed *
                                                    40.0 /
                                                    total
                                    );
        }

        setProgress(
                percent,
                "COMPARING " +
                        completed +
                        "/" +
                        total +
                        "  (" +
                        percent +
                        "%)"
        );
    }

    // =========================================================
    // COMPARE PAIR
    // =========================================================

    private Result comparePair(
            String folder,
            String idName,
            File idFile,
            File selfieFile
    ) {

        ImagePlus id =
                IJ.openImage(
                        idFile.getAbsolutePath()
                );

        ImagePlus selfie =
                IJ.openImage(
                        selfieFile.getAbsolutePath()
                );

        if (
                id == null ||
                        selfie == null
        ) {

            if (id != null)
                id.close();

            if (selfie != null)
                selfie.close();

            return null;
        }

        try {

            double[] idHistogram =
                    getFaceHistogram(
                            id
                    );

            double[] selfieHistogram =
                    getFaceHistogram(
                            selfie
                    );

            if (
                    idHistogram == null ||
                            selfieHistogram == null
            ) {

                return null;
            }

            double similarity =
                    compareHistograms(
                            idHistogram,
                            selfieHistogram
                    );

            String selfieName =
                    selfieFile
                            .getName()
                            .replace(
                                    "_face.png",
                                    ""
                            );

            Result result =
                    new Result(
                            folder,
                            idName,
                            selfieName,
                            similarity,
                            idFile,
                            selfieFile
                    );

            log(
                    folder +
                            " | " +
                            idName +
                            " vs " +
                            selfieName +
                            " | Similarity = " +
                            String.format(
                                    "%.4f",
                                    similarity
                            ) +
                            " | " +
                            result.decision
            );

            return result;

        } finally {

            id.close();

            selfie.close();
        }
    }

    // =========================================================
    // ADD RESULT TO TABLE
    // =========================================================

    private void addResultToTable(
            Result result
    ) {

        SwingUtilities.invokeLater(
                () -> {

                    tableModel.addRow(
                            new Object[]{
                                    result.folder,
                                    result.id,
                                    result.selfie,
                                    String.format(
                                            "%.4f",
                                            result.similarity
                                    ),
                                    result.decision
                            }
                    );

                    int row =
                            tableModel
                                    .getRowCount() - 1;

                    resultTable
                            .setRowSelectionInterval(
                                    row,
                                    row
                            );

                    showResult(
                            result
                    );
                }
        );
    }

    // =========================================================
    // SHOW RESULT
    // =========================================================

    private void showResult(
            Result result
    ) {

        if (result == null)
            return;

        decisionLabel.setText(
                result.decision
        );

        similarityLabel.setText(
                String.format(
                        "%.4f",
                        result.similarity
                )
        );

        statusLabel.setText(
                "FOLDER " +
                        result.folder +
                        "  •  " +
                        result.id +
                        " vs " +
                        result.selfie
        );

        if (
                result.similarity >=
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
                idImageLabel,
                result.idFile
        );

        showImage(
                selfieImageLabel,
                result.selfieFile
        );
    }

    // =========================================================
    // SHOW IMAGE
    //
    // IMPORTANT:
    // Images are scaled proportionally to fit the ENTIRE
    // available image area. No cropping.
    // =========================================================

    private void showImage(
            JLabel label,
            File file
    ) {

        if (
                file == null ||
                        !file.exists()
        ) {

            label.setIcon(
                    null
            );

            label.setText(
                    "IMAGE NOT FOUND"
            );

            label.setForeground(
                    Color.RED
            );

            return;
        }

        ImagePlus image =
                IJ.openImage(
                        file.getAbsolutePath()
                );

        if (image == null) {

            label.setIcon(
                    null
            );

            label.setText(
                    "COULD NOT OPEN IMAGE"
            );

            return;
        }

        try {

            ImageProcessor processor =
                    image.getProcessor();

            Image awtImage =
                    processor.createImage();

            int imageWidth =
                    awtImage.getWidth(
                            null
                    );

            int imageHeight =
                    awtImage.getHeight(
                            null
                    );

            /*
             * Use the actual large label size.
             *
             * The small minimum values from the old version
             * have been removed. This lets the image use the
             * large photo square.
             */

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

            /*
             * Use the smaller scale so BOTH dimensions
             * always fit completely.
             */
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
                    awtImage.getScaledInstance(
                            newWidth,
                            newHeight,
                            Image.SCALE_SMOOTH
                    );

            label.setText(
                    ""
            );

            label.setIcon(
                    new ImageIcon(
                            scaled
                    )
            );

            label.setHorizontalAlignment(
                    SwingConstants.CENTER
            );

            label.setVerticalAlignment(
                    SwingConstants.CENTER
            );

            label.setBackground(
                    Color.WHITE
            );

        } finally {

            image.close();
        }
    }

    // =========================================================
    // CLEAR RESULT
    // =========================================================

    private void clearResult() {

        decisionLabel.setText(
                "NO RESULT"
        );

        decisionLabel.setForeground(
                Color.BLACK
        );

        similarityLabel.setText(
                "-"
        );

        similarityLabel.setForeground(
                Color.BLACK
        );

        statusLabel.setText(
                "READY"
        );
    }

    // =========================================================
    // CLEAR IMAGES
    // =========================================================

    private void clearImages() {

        idImageLabel.setIcon(
                null
        );

        idImageLabel.setText(
                "NO RESULT SELECTED"
        );

        idImageLabel.setForeground(
                new Color(
                        80,
                        80,
                        80
                )
        );

        idImageLabel.setBackground(
                new Color(
                        245,
                        246,
                        248
                )
        );

        selfieImageLabel.setIcon(
                null
        );

        selfieImageLabel.setText(
                "NO RESULT SELECTED"
        );

        selfieImageLabel.setForeground(
                new Color(
                        80,
                        80,
                        80
                )
        );

        selfieImageLabel.setBackground(
                new Color(
                        245,
                        246,
                        248
                )
        );
    }

    // =========================================================
    // HISTOGRAM
    // =========================================================

    private double[] getFaceHistogram(
            ImagePlus face
    ) {

        ImageProcessor ip =
                face.getProcessor();

        double[] histogram =
                new double[256];

        int count = 0;

        int width =
                ip.getWidth();

        int height =
                ip.getHeight();

        for (
                int y = 0;
                y < height;
                y++
        ) {

            for (
                    int x = 0;
                    x < width;
                    x++
            ) {

                int[] rgb =
                        ip.getPixel(
                                x,
                                y,
                                null
                        );

                int r =
                        rgb[0];

                int g =
                        rgb[1];

                int b =
                        rgb[2];

                if (
                        r >= 250 &&
                                g >= 250 &&
                                b >= 250
                ) {

                    continue;
                }

                int gray =
                        (int)
                                (
                                        0.299 * r +
                                                0.587 * g +
                                                0.114 * b
                                );

                gray =
                        Math.max(
                                0,
                                Math.min(
                                        255,
                                        gray
                                )
                        );

                histogram[gray]++;

                count++;
            }
        }

        if (count == 0)
            return null;

        for (
                int i = 0;
                i < histogram.length;
                i++
        ) {

            histogram[i] /=
                    count;
        }

        return histogram;
    }

    // =========================================================
    // CORRELATION
    // =========================================================

    private double compareHistograms(
            double[] h1,
            double[] h2
    ) {

        double mean1 = 0;

        double mean2 = 0;

        for (
                int i = 0;
                i < 256;
                i++
        ) {

            mean1 +=
                    h1[i];

            mean2 +=
                    h2[i];
        }

        mean1 /=
                256.0;

        mean2 /=
                256.0;

        double numerator = 0;

        double denominator1 = 0;

        double denominator2 = 0;

        for (
                int i = 0;
                i < 256;
                i++
        ) {

            double value1 =
                    h1[i] -
                            mean1;

            double value2 =
                    h2[i] -
                            mean2;

            numerator +=
                    value1 *
                            value2;

            denominator1 +=
                    value1 *
                            value1;

            denominator2 +=
                    value2 *
                            value2;
        }

        double denominator =
                Math.sqrt(
                        denominator1 *
                                denominator2
                );

        if (denominator == 0)
            return 0;

        return numerator /
                denominator;
    }

    // =========================================================
    // FACE EXTRACTION
    // =========================================================

    private void extractFace(
            File originalFile,
            File outputFolder
    ) throws Exception {

        ImagePlus imp =
                IJ.openImage(
                        originalFile
                                .getAbsolutePath()
                );

        if (imp == null)
            return;

        try {

            ImageProcessor ip =
                    imp.getProcessor();

            int width =
                    ip.getWidth();

            int height =
                    ip.getHeight();

            int[] svHistogram =
                    new int[N];

            for (
                    int y = 0;
                    y < height;
                    y++
            ) {

                for (
                        int x = 0;
                        x < width;
                        x++
                ) {

                    int[] rgb =
                            ip.getPixel(
                                    x,
                                    y,
                                    null
                            );

                    float[] hsb =
                            Color.RGBtoHSB(
                                    rgb[0],
                                    rgb[1],
                                    rgb[2],
                                    null
                            );

                    int sv =
                            (int)
                                    (
                                            hsb[1] *
                                                    hsb[2] *
                                                    256
                                    );

                    sv =
                            Math.max(
                                    0,
                                    Math.min(
                                            255,
                                            sv
                                    )
                            );

                    svHistogram[sv]++;
                }
            }

            double[] logged =
                    new double[N];

            for (
                    int i = 0;
                    i < N;
                    i++
            ) {

                logged[i] =
                        svHistogram[i] >
                                LOG_THRESHOLD
                                ? Math.log(
                                svHistogram[i]
                        )
                                : svHistogram[i];
            }

            double[] smooth =
                    new double[N];

            for (
                    int i = 0;
                    i < N;
                    i++
            ) {

                if (
                        i < 2 ||
                                i > N - 3
                ) {

                    smooth[i] =
                            logged[i];

                } else {

                    smooth[i] =
                            (
                                    logged[i - 2] +
                                            logged[i - 1] +
                                            logged[i] +
                                            logged[i + 1] +
                                            logged[i + 2]
                            ) /
                                    5.0;
                }
            }

            double sumX = 0;

            double sumY = 0;

            double sumXY = 0;

            double sumXX = 0;

            int count = 0;

            for (
                    int i = 0;
                    i < N;
                    i++
            ) {

                if (
                        smooth[i] != 0
                ) {

                    sumX +=
                            i;

                    sumY +=
                            smooth[i];

                    sumXY +=
                            i *
                                    smooth[i];

                    sumXX +=
                            i *
                                    i;

                    count++;
                }
            }

            if (count < 2)
                return;

            double denominator =
                    count *
                            sumXX -
                            sumX *
                                    sumX;

            if (denominator == 0)
                return;

            double a =
                    (
                            count *
                                    sumXY -
                                    sumX *
                                            sumY
                    ) /
                            denominator;

            double b =
                    (
                            sumY -
                                    a *
                                            sumX
                    ) /
                            count;

            double[] detrended =
                    new double[N];

            for (
                    int i = 0;
                    i < N;
                    i++
            ) {

                detrended[i] =
                        smooth[i] -
                                (
                                        a *
                                                i +
                                                b
                                );
            }

            int maxIndex =
                    RANGE_START;

            double maxValue =
                    detrended[
                            RANGE_START
                            ];

            for (
                    int i =
                            RANGE_START + 1;
                    i <= RANGE_END;
                    i++
            ) {

                if (
                        detrended[i] >
                                maxValue
                ) {

                    maxValue =
                            detrended[i];

                    maxIndex =
                            i;
                }
            }

            int leftIntercept =
                    maxIndex;

            for (
                    int i = maxIndex;
                    i > 0;
                    i--
            ) {

                if (
                        detrended[i - 1] <= 0 &&
                                detrended[i] > 0
                ) {

                    leftIntercept =
                            i - 1;

                    break;
                }
            }

            int rightIntercept =
                    maxIndex;

            for (
                    int i = maxIndex;
                    i < N - 1;
                    i++
            ) {

                if (
                        detrended[i] >= 0 &&
                                detrended[i + 1] < 0
                ) {

                    rightIntercept =
                            i;

                    break;
                }
            }

            ImageProcessor faceProcessor =
                    ip.duplicate();

            for (
                    int y = 0;
                    y < height;
                    y++
            ) {

                for (
                        int x = 0;
                        x < width;
                        x++
                ) {

                    int[] rgb =
                            ip.getPixel(
                                    x,
                                    y,
                                    null
                            );

                    float[] hsb =
                            Color.RGBtoHSB(
                                    rgb[0],
                                    rgb[1],
                                    rgb[2],
                                    null
                            );

                    int hue =
                            (int)
                                    (
                                            hsb[0] *
                                                    256
                                    );

                    int sv =
                            (int)
                                    (
                                            hsb[1] *
                                                    hsb[2] *
                                                    256
                                    );

                    if (
                            hue < MIN_HUE ||
                                    hue > MAX_HUE ||
                                    sv < leftIntercept ||
                                    sv > rightIntercept
                    ) {

                        faceProcessor.putPixel(
                                x,
                                y,
                                new int[]{
                                        255,
                                        255,
                                        255
                                }
                        );
                    }
                }
            }

            String baseName =
                    originalFile.getName();

            int dot =
                    baseName.lastIndexOf(
                            "."
                    );

            if (dot > 0) {

                baseName =
                        baseName.substring(
                                0,
                                dot
                        );
            }

            File output =
                    new File(
                            outputFolder,
                            baseName +
                                    "_face.png"
                    );

            ImagePlus result =
                    new ImagePlus(
                            baseName +
                                    "_face",
                            faceProcessor
                    );

            IJ.save(
                    result,
                    output.getAbsolutePath()
            );

            result.close();

        } finally {

            imp.close();
        }
    }

    // =========================================================
    // FILE HELPERS
    // =========================================================

    private File[] getOriginalImages(
            File folder
    ) {

        return folder.listFiles(
                (dir, name) -> {

                    String n =
                            name.toLowerCase();

                    return
                            (
                                    n.endsWith(".jpg") ||
                                            n.endsWith(".jpeg") ||
                                            n.endsWith(".png")
                            )
                                    &&
                                    !n.contains(
                                            "_face"
                                    )
                                    &&
                                    !n.contains(
                                            "_mask"
                                    );
                }
        );
    }

    private File[] findSelfies(
            File folder
    ) {

        return folder.listFiles(
                (dir, name) -> {

                    String n =
                            name.toLowerCase();

                    return
                            n.startsWith(
                                    "selfie_"
                            ) &&
                                    n.endsWith(
                                            "_face.png"
                                    );
                }
        );
    }

    private File findFaceFile(
            File folder,
            String name
    ) {

        File file =
                new File(
                        folder,
                        name +
                                ".png"
                );

        return file.exists()
                ? file
                : null;
    }

    private File findDatasetFolder(
            File mainFolder
    ) {

        if (
                hasFolder1(
                        mainFolder
                )
        )
            return mainFolder;

        File filesFolder =
                new File(
                        mainFolder,
                        "Files"
                );

        if (
                hasFolder1(
                        filesFolder
                )
        )
            return filesFolder;

        return null;
    }

    private boolean hasFolder1(
            File folder
    ) {

        if (
                folder == null ||
                        !folder.exists() ||
                        !folder.isDirectory()
        ) {

            return false;
        }

        File[] dirs =
                folder.listFiles(
                        File::isDirectory
                );

        if (dirs == null)
            return false;

        for (File f : dirs) {

            if (
                    f.getName()
                            .equals(
                                    "1"
                            )
            ) {

                return true;
            }
        }

        return false;
    }

    private boolean isNumberFolder(
            String name
    ) {

        try {

            int number =
                    Integer.parseInt(
                            name
                    );

            return number >= 1 &&
                    number <= 10;

        } catch (
                NumberFormatException e
        ) {

            return false;
        }
    }

    // =========================================================
    // CSV
    // =========================================================

    private String csvLine(
            Result r
    ) {

        return r.folder +
                "," +
                r.id +
                "," +
                r.selfie +
                "," +
                r.similarity +
                "," +
                r.decision;
    }

    // =========================================================
    // LOG
    // =========================================================

    private void log(
            String text
    ) {

        SwingUtilities.invokeLater(
                () -> {

                    logArea.append(
                            text +
                                    "\n"
                    );

                    logArea.setCaretPosition(
                            logArea
                                    .getDocument()
                                    .getLength()
                    );
                }
        );
    }

    // =========================================================
    // PROGRESS
    // =========================================================

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

                    progressBar.setValue(
                            v
                    );

                    progressBar.setString(
                            text
                    );

                    statusLabel.setText(
                            text
                    );
                }
        );
    }
}