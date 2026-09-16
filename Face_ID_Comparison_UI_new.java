import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Face_ID_Comparison_UI_new {

    private JFrame frame;

    private JTextField datasetPathField;
    private JTextArea logArea;
    private JProgressBar progressBar;

    private JTable resultTable;
    private DefaultTableModel tableModel;

    private JLabel statusLabel;
    private JLabel similarityLabel;
    private JLabel decisionLabel;

    private JLabel selfieOriginalLabel;
    private JLabel selfieLayerLabel;

    private JLabel idOriginalLabel;
    private JLabel idLayerLabel;

    private final List<File> uploadedSelfies = new ArrayList<>();
    private final List<MatchResult> results = new ArrayList<>();

    // =========================================================
    // SETTINGS
    // =========================================================

    private static final int MIN_HUE = 3;
    private static final int MAX_HUE = 24;

    private static final int N = 256;

    private static final double LOG_THRESHOLD = 3.0;

    private static final int RANGE_START = 40;
    private static final int RANGE_END = 80;

    private static final double THRESHOLD = 0.50;

    // Number of matches shown in the table
    private static final int MAX_RESULTS = 50;

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            Face_ID_Comparison_UI_new app =
                    new Face_ID_Comparison_UI_new();

            app.createInterface();
        });
    }

    // =========================================================
    // MATCH RESULT
    // =========================================================

    private static class MatchResult {

        File idFile;

        File selfieFile;

        double similarity;

        BufferedImage originalID;
        BufferedImage extractedID;

        BufferedImage originalSelfie;
        BufferedImage extractedSelfie;

        String decision;

        MatchResult(
                File idFile,
                File selfieFile,
                double similarity,
                BufferedImage originalID,
                BufferedImage extractedID,
                BufferedImage originalSelfie,
                BufferedImage extractedSelfie
        ) {

            this.idFile = idFile;
            this.selfieFile = selfieFile;
            this.similarity = similarity;

            this.originalID = originalID;
            this.extractedID = extractedID;

            this.originalSelfie = originalSelfie;
            this.extractedSelfie = extractedSelfie;

            this.decision =
                    similarity >= THRESHOLD
                            ? "SIMILAR"
                            : "LOW SIMILARITY";
        }
    }

    // =========================================================
    // INTERFACE
    // =========================================================

    private void createInterface() {

        frame =
                new JFrame(
                        "Face ID Dataset Search"
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
                        "FACE ID DATASET SEARCH"
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
                        "Upload selfies and search an ID-photo dataset"
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
                        320,
                        0
                )
        );

        controls.setLayout(
                new BoxLayout(
                        controls,
                        BoxLayout.Y_AXIS
                )
        );

        // =====================================================
        // SELFIE SECTION
        // =====================================================

        controls.add(
                blackLabel(
                        "SELFIE IMAGES",
                        15,
                        Font.BOLD
                )
        );

        controls.add(
                Box.createVerticalStrut(8)
        );

        JButton addSelfieButton =
                new JButton(
                        "ADD SELFIE IMAGE(S)"
                );

        styleButton(
                addSelfieButton,
                Color.WHITE,
                Color.BLACK
        );

        addSelfieButton.addActionListener(
                e -> addSelfies()
        );

        controls.add(
                addSelfieButton
        );

        controls.add(
                Box.createVerticalStrut(8)
        );

        JButton clearSelfiesButton =
                new JButton(
                        "CLEAR SELFIES"
                );

        styleButton(
                clearSelfiesButton,
                Color.WHITE,
                Color.BLACK
        );

        clearSelfiesButton.addActionListener(
                e -> clearSelfies()
        );

        controls.add(
                clearSelfiesButton
        );

        controls.add(
                Box.createVerticalStrut(8)
        );

        JLabel selfieCountLabel =
                blackLabel(
                        "0 selfie images selected",
                        12,
                        Font.PLAIN
                );

        controls.add(
                selfieCountLabel
        );

        controls.add(
                Box.createVerticalStrut(20)
        );

        // =====================================================
        // DATASET SECTION
        // =====================================================

        controls.add(
                blackLabel(
                        "ID PHOTO DATASET",
                        15,
                        Font.BOLD
                )
        );

        controls.add(
                Box.createVerticalStrut(8)
        );

        datasetPathField =
                new JTextField();

        datasetPathField.setText(
                ""
        );

        datasetPathField.setForeground(
                Color.BLACK
        );

        datasetPathField.setBackground(
                Color.WHITE
        );

        datasetPathField.setCaretColor(
                Color.BLACK
        );

        controls.add(
                datasetPathField
        );

        controls.add(
                Box.createVerticalStrut(8)
        );

        JButton browseDatasetButton =
                new JButton(
                        "BROWSE DATASET"
                );

        styleButton(
                browseDatasetButton,
                Color.WHITE,
                Color.BLACK
        );

        browseDatasetButton.addActionListener(
                e -> browseDataset()
        );

        controls.add(
                browseDatasetButton
        );

        controls.add(
                Box.createVerticalStrut(8)
        );

        JButton countDatasetButton =
                new JButton(
                        "COUNT ID PHOTOS"
                );

        styleButton(
                countDatasetButton,
                Color.WHITE,
                Color.BLACK
        );

        countDatasetButton.addActionListener(
                e -> countDatasetImages()
        );

        controls.add(
                countDatasetButton
        );

        controls.add(
                Box.createVerticalStrut(20)
        );

        // =====================================================
        // RUN BUTTON
        // =====================================================

        JButton runButton =
                new JButton(
                        "SEARCH DATASET"
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
                e ->
                        startSearch(
                                runButton,
                                selfieCountLabel
                        )
        );

        controls.add(
                runButton
        );

        controls.add(
                Box.createVerticalStrut(25)
        );

        // =====================================================
        // PIPELINE DESCRIPTION
        // =====================================================

        controls.add(
                blackLabel(
                        "PIPELINE",
                        15,
                        Font.BOLD
                )
        );

        controls.add(
                Box.createVerticalStrut(10)
        );

        controls.add(
                blackLabel(
                        "<html>" +
                                "1. Upload selfie image(s)<br><br>" +
                                "2. Detect/extract face layer<br><br>" +
                                "3. Scan ID dataset recursively<br><br>" +
                                "4. Extract ID face layer<br><br>" +
                                "5. Compare histograms<br><br>" +
                                "6. Sort closest matches<br><br>" +
                                "7. Display image layers<br><br>" +
                                "8. Save CSV" +
                                "</html>",
                        13,
                        Font.PLAIN
                )
        );

        controls.add(
                Box.createVerticalGlue()
        );

        controls.add(
                blackLabel(
                        "<html><b>Similarity:</b><br>" +
                                "Higher = closer histogram match<br><br>" +
                                "Threshold: " +
                                THRESHOLD +
                                "</html>",
                        13,
                        Font.PLAIN
                )
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
        // RESULT INFO
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
                        100
                )
        );

        JPanel decisionBox =
                createInfoBox(
                        "RESULT"
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
                        17,
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
        // IMAGE LAYERS
        // =====================================================

        JPanel imageArea =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                15,
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

        JPanel selfiePanel =
                createLayerPanel(
                        "SELFIE PROCESSING LAYERS",
                        true
                );

        JPanel idPanel =
                createLayerPanel(
                        "ID PROCESSING LAYERS",
                        false
                );

        imageArea.add(
                selfiePanel
        );

        imageArea.add(
                idPanel
        );

        center.add(
                imageArea,
                BorderLayout.CENTER
        );

        // =====================================================
        // TABLE
        // =====================================================

        String[] columns = {
                "Rank",
                "ID Image",
                "Selfie",
                "Similarity",
                "Result"
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
                30
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
                                                "SIMILAR"
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
                i < resultTable.getColumnCount();
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
                            ) {
                                return;
                            }

                            int row =
                                    resultTable
                                            .getSelectedRow();

                            if (row < 0) {
                                return;
                            }

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

                                showMatch(
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
                        230
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
                        "CLOSEST ID MATCHES",
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
        // BOTTOM
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

        logArea.setEditable(false);

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
    // LAYER PANEL
    // =========================================================

    private JPanel createLayerPanel(
            String title,
            boolean selfie
    ) {

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                8,
                                0
                        )
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

        JPanel originalPanel =
                createSingleLayerPanel(
                        "ORIGINAL"
                );

        JPanel facePanel =
                createSingleLayerPanel(
                        "FACE LAYER"
                );

        if (selfie) {

            selfieOriginalLabel =
                    (JLabel)
                            originalPanel.getClientProperty(
                                    "imageLabel"
                            );

            selfieLayerLabel =
                    (JLabel)
                            facePanel.getClientProperty(
                                    "imageLabel"
                            );

        } else {

            idOriginalLabel =
                    (JLabel)
                            originalPanel.getClientProperty(
                                    "imageLabel"
                            );

            idLayerLabel =
                    (JLabel)
                            facePanel.getClientProperty(
                                    "imageLabel"
                            );
        }

        panel.add(
                originalPanel
        );

        panel.add(
                facePanel
        );

        return panel;
    }

    // =========================================================
    // SINGLE IMAGE LAYER
    // =========================================================

    private JPanel createSingleLayerPanel(
            String title
    ) {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setBackground(
                Color.WHITE
        );

        JLabel titleLabel =
                blackLabel(
                        title,
                        13,
                        Font.BOLD
                );

        titleLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        panel.add(
                titleLabel,
                BorderLayout.NORTH
        );

        JLabel imageLabel =
                new JLabel(
                        "NO IMAGE",
                        SwingConstants.CENTER
                );

        imageLabel.setOpaque(
                true
        );

        imageLabel.setBackground(
                new Color(
                        245,
                        246,
                        248
                )
        );

        imageLabel.setForeground(
                new Color(
                        80,
                        80,
                        80
                )
        );

        imageLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        imageLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        imageLabel.setVerticalAlignment(
                SwingConstants.CENTER
        );

        panel.add(
                imageLabel,
                BorderLayout.CENTER
        );

        panel.putClientProperty(
                "imageLabel",
                imageLabel
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
    // BUTTON
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
    // LABEL
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
    // ADD SELFIES
    // =========================================================

    private void addSelfies() {

        JFileChooser chooser =
                new JFileChooser();

        chooser.setMultiSelectionEnabled(
                true
        );

        chooser.setFileSelectionMode(
                JFileChooser.FILES_ONLY
        );

        if (
                chooser.showOpenDialog(frame)
                        ==
                        JFileChooser.APPROVE_OPTION
        ) {

            File[] files =
                    chooser.getSelectedFiles();

            for (File file : files) {

                if (isImage(file)) {

                    if (
                            !uploadedSelfies.contains(
                                    file
                            )
                    ) {

                        uploadedSelfies.add(
                                file
                        );
                    }
                }
            }

            log(
                    "Selfie images selected: " +
                            uploadedSelfies.size()
            );

            clearImages();

            if (!uploadedSelfies.isEmpty()) {

                showImage(
                        selfieOriginalLabel,
                        uploadedSelfies.get(0)
                );
            }
        }
    }

    // =========================================================
    // CLEAR SELFIES
    // =========================================================

    private void clearSelfies() {

        uploadedSelfies.clear();

        clearImages();

        log(
                "Selfie images cleared."
        );
    }

    // =========================================================
    // BROWSE DATASET
    // =========================================================

    private void browseDataset() {

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

            datasetPathField.setText(
                    chooser
                            .getSelectedFile()
                            .getAbsolutePath()
            );

            log(
                    "Dataset selected: " +
                            chooser
                                    .getSelectedFile()
                                    .getAbsolutePath()
            );
        }
    }

    // =========================================================
    // COUNT DATASET
    // =========================================================

    private void countDatasetImages() {

        File folder =
                new File(
                        datasetPathField
                                .getText()
                                .trim()
                );

        if (
                !folder.exists() ||
                        !folder.isDirectory()
        ) {

            JOptionPane.showMessageDialog(
                    frame,
                    "Please select a valid dataset folder.",
                    "Dataset Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        List<File> images =
                findAllImages(
                        folder
                );

        JOptionPane.showMessageDialog(
                frame,
                "ID images found: " +
                        images.size(),
                "Dataset",
                JOptionPane.INFORMATION_MESSAGE
        );

        log(
                "ID images found: " +
                        images.size()
        );
    }

    // =========================================================
    // START SEARCH
    // =========================================================

    private void startSearch(
            JButton runButton,
            JLabel selfieCountLabel
    ) {

        if (
                uploadedSelfies.isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    frame,
                    "Please upload at least one selfie image.",
                    "No Selfie",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        File dataset =
                new File(
                        datasetPathField
                                .getText()
                                .trim()
                );

        if (
                !dataset.exists() ||
                        !dataset.isDirectory()
        ) {

            JOptionPane.showMessageDialog(
                    frame,
                    "Please select a valid ID-photo dataset.",
                    "Dataset Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        List<File> idImages =
                findAllImages(
                        dataset
                );

        if (idImages.isEmpty()) {

            JOptionPane.showMessageDialog(
                    frame,
                    "No ID images were found in the selected dataset.",
                    "Dataset Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        if (idImages.size() < 100) {

            int answer =
                    JOptionPane.showConfirmDialog(
                            frame,
                            "Only " +
                                    idImages.size() +
                                    " images were found.\n\n" +
                                    "The requested dataset preferably contains at least 100 ID photos.\n\n" +
                                    "Continue anyway?",
                            "Small Dataset",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );

            if (
                    answer !=
                            JOptionPane.YES_OPTION
            ) {
                return;
            }
        }

        runButton.setEnabled(
                false
        );

        tableModel.setRowCount(
                0
        );

        results.clear();

        clearResult();

        clearImages();

        logArea.setText("");

        log(
                "========================================"
        );

        log(
                "FACE ID DATASET SEARCH"
        );

        log(
                "========================================"
        );

        log(
                "Selfies: " +
                        uploadedSelfies.size()
        );

        log(
                "ID images: " +
                        idImages.size()
        );

        Thread worker =
                new Thread(
                        () -> {

                            try {

                                runSearch(
                                        idImages
                                );

                            } catch (Exception e) {

                                e.printStackTrace();

                                log(
                                        "ERROR: " +
                                                e.getMessage()
                                );

                                SwingUtilities.invokeLater(
                                        () -> {

                                            statusLabel.setText(
                                                    "ERROR"
                                            );

                                            JOptionPane.showMessageDialog(
                                                    frame,
                                                    e.toString(),
                                                    "Search Error",
                                                    JOptionPane.ERROR_MESSAGE
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
    // RUN SEARCH
    // =========================================================

    private void runSearch(
            List<File> idImages
    ) throws Exception {

        // -----------------------------------------------------
        // STEP 1
        // Process uploaded selfies
        // -----------------------------------------------------

        log(
                ""
        );

        log(
                "STEP 1: PROCESSING SELFIES"
        );

        List<ProcessedImage> processedSelfies =
                new ArrayList<>();

        int selfieIndex = 0;

        for (File selfieFile : uploadedSelfies) {

            selfieIndex++;

            log(
                    "Processing selfie " +
                            selfieIndex +
                            "/" +
                            uploadedSelfies.size() +
                            ": " +
                            selfieFile.getName()
            );

            ProcessedImage processed =
                    processImage(
                            selfieFile
                    );

            if (processed != null) {

                processedSelfies.add(
                        processed
                );
            }

            int progress =
                    (int)
                            (
                                    selfieIndex *
                                            10.0 /
                                            uploadedSelfies.size()
                            );

            setProgress(
                    progress,
                    "PROCESSING SELFIES " +
                            progress +
                            "%"
            );
        }

        if (processedSelfies.isEmpty()) {

            throw new Exception(
                    "Could not process any uploaded selfie images."
            );
        }

        log(
                "Usable selfies: " +
                        processedSelfies.size()
        );

        // -----------------------------------------------------
        // STEP 2
        // Process every ID
        // -----------------------------------------------------

        log(
                ""
        );

        log(
                "STEP 2: SEARCHING ID DATASET"
        );

        int completed = 0;

        for (File idFile : idImages) {

            ProcessedImage processedID =
                    processImage(
                            idFile
                    );

            if (
                    processedID == null ||
                            processedID.histogram == null
            ) {

                completed++;

                continue;
            }

            MatchResult bestMatch =
                    null;

            // -------------------------------------------------
            // Compare this ID against every uploaded selfie
            // -------------------------------------------------

            for (
                    ProcessedImage selfie :
                    processedSelfies
            ) {

                if (
                        selfie.histogram == null
                ) {
                    continue;
                }

                double similarity =
                        compareHistograms(
                                processedID.histogram,
                                selfie.histogram
                        );

                if (
                        bestMatch == null ||
                                similarity >
                                        bestMatch.similarity
                ) {

                    bestMatch =
                            new MatchResult(
                                    idFile,
                                    selfie.sourceFile,
                                    similarity,
                                    processedID.original,
                                    processedID.face,
                                    selfie.original,
                                    selfie.face
                            );
                }
            }

            if (bestMatch != null) {

                results.add(
                        bestMatch
                );
            }

            completed++;

            int percent =
                    10 +
                            (int)
                                    (
                                            completed *
                                                    85.0 /
                                                    idImages.size()
                                    );

            setProgress(
                    percent,
                    "SEARCHING ID DATASET " +
                            completed +
                            "/" +
                            idImages.size()
            );
        }

        // -----------------------------------------------------
        // STEP 3
        // Sort
        // -----------------------------------------------------

        log(
                ""
        );

        log(
                "STEP 3: SORTING MATCHES"
        );

        Collections.sort(
                results,
                Comparator.comparingDouble(
                        (MatchResult r) ->
                                r.similarity
                ).reversed()
        );

        // -----------------------------------------------------
        // STEP 4
        // Display
        // -----------------------------------------------------

        SwingUtilities.invokeLater(
                () -> {

                    tableModel.setRowCount(
                            0
                    );

                    int numberToDisplay =
                            Math.min(
                                    MAX_RESULTS,
                                    results.size()
                            );

                    for (
                            int i = 0;
                            i < numberToDisplay;
                            i++
                    ) {

                        MatchResult result =
                                results.get(i);

                        tableModel.addRow(
                                new Object[]{
                                        i + 1,
                                        result.idFile.getName(),
                                        result.selfieFile.getName(),
                                        String.format(
                                                "%.4f",
                                                result.similarity
                                        ),
                                        result.decision
                                }
                        );
                    }

                    if (!results.isEmpty()) {

                        resultTable
                                .setRowSelectionInterval(
                                        0,
                                        0
                                );

                        showMatch(
                                results.get(0)
                        );

                        statusLabel.setText(
                                "SEARCH COMPLETE"
                        );

                    } else {

                        statusLabel.setText(
                                "NO MATCHES"
                        );
                    }
                }
        );

        // -----------------------------------------------------
        // STEP 5
        // Save CSV
        // -----------------------------------------------------

        saveCSV(
                idImages
        );

        setProgress(
                100,
                "COMPLETE"
        );

        log(
                ""
        );

        log(
                "========================================"
        );

        log(
                "SEARCH COMPLETE"
        );

        log(
                "ID images searched: " +
                        idImages.size()
        );

        log(
                "Matches produced: " +
                        results.size()
        );

        if (!results.isEmpty()) {

            log(
                    "Best similarity: " +
                            String.format(
                                    "%.4f",
                                    results.get(0)
                                            .similarity
                            )
            );

            log(
                    "Best ID: " +
                            results.get(0)
                                    .idFile
                                    .getAbsolutePath()
            );
        }

        log(
                "========================================"
        );
    }

    // =========================================================
    // PROCESSED IMAGE
    // =========================================================

    private static class ProcessedImage {

        File sourceFile;

        BufferedImage original;

        BufferedImage face;

        double[] histogram;

        ProcessedImage(
                File sourceFile
        ) {

            this.sourceFile =
                    sourceFile;
        }
    }

    // =========================================================
    // PROCESS IMAGE
    // =========================================================

    private ProcessedImage processImage(
            File file
    ) {

        try {

            BufferedImage original =
                    ImageIO.read(
                            file
                    );

            if (original == null) {

                log(
                        "Could not read: " +
                                file.getName()
                );

                return null;
            }

            BufferedImage face =
                    extractFaceImage(
                            original
                    );

            if (face == null) {

                log(
                        "Face extraction failed: " +
                                file.getName()
                );

                return null;
            }

            double[] histogram =
                    getFaceHistogram(
                            face
                    );

            if (histogram == null) {

                log(
                        "Histogram failed: " +
                                file.getName()
                );

                return null;
            }

            ProcessedImage result =
                    new ProcessedImage(
                            file
                    );

            result.original =
                    original;

            result.face =
                    face;

            result.histogram =
                    histogram;

            return result;

        } catch (Exception e) {

            log(
                    "Processing error for " +
                            file.getName() +
                            ": " +
                            e.getMessage()
            );

            return null;
        }
    }

    // =========================================================
    // FACE EXTRACTION
    // =========================================================

    private BufferedImage extractFaceImage(
            BufferedImage image
    ) {

        int width =
                image.getWidth();

        int height =
                image.getHeight();

        int[] svHistogram =
                new int[N];

        // -----------------------------------------------------
        // Build SV histogram
        // -----------------------------------------------------

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

                int rgb =
                        image.getRGB(
                                x,
                                y
                        );

                int r =
                        (rgb >> 16) & 0xFF;

                int g =
                        (rgb >> 8) & 0xFF;

                int b =
                        rgb & 0xFF;

                float[] hsb =
                        Color.RGBtoHSB(
                                r,
                                g,
                                b,
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

        // -----------------------------------------------------
        // Log transform
        // -----------------------------------------------------

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

        // -----------------------------------------------------
        // Smooth
        // -----------------------------------------------------

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
                        ) / 5.0;
            }
        }

        // -----------------------------------------------------
        // Linear detrending
        // -----------------------------------------------------

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

            if (smooth[i] != 0) {

                sumX += i;
                sumY += smooth[i];
                sumXY +=
                        i * smooth[i];

                sumXX +=
                        i * i;

                count++;
            }
        }

        if (count < 2) {
            return null;
        }

        double denominator =
                count * sumXX -
                        sumX * sumX;

        if (denominator == 0) {
            return null;
        }

        double a =
                (
                        count * sumXY -
                                sumX * sumY
                ) /
                        denominator;

        double b =
                (
                        sumY -
                                a * sumX
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
                                    a * i +
                                            b
                            );
        }

        // -----------------------------------------------------
        // Find peak
        // -----------------------------------------------------

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

        // -----------------------------------------------------
        // Find left intercept
        // -----------------------------------------------------

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

        // -----------------------------------------------------
        // Find right intercept
        // -----------------------------------------------------

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

        // -----------------------------------------------------
        // Create processed image
        // -----------------------------------------------------

        BufferedImage face =
                new BufferedImage(
                        width,
                        height,
                        BufferedImage.TYPE_INT_RGB
                );

        // -----------------------------------------------------
        // Remove non-face pixels
        // -----------------------------------------------------

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

                int rgb =
                        image.getRGB(
                                x,
                                y
                        );

                int r =
                        (rgb >> 16) & 0xFF;

                int g =
                        (rgb >> 8) & 0xFF;

                int bColor =
                        rgb & 0xFF;

                float[] hsb =
                        Color.RGBtoHSB(
                                r,
                                g,
                                bColor,
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

                    face.setRGB(
                            x,
                            y,
                            Color.WHITE.getRGB()
                    );

                } else {

                    face.setRGB(
                            x,
                            y,
                            rgb
                    );
                }
            }
        }

        return face;
    }

    // =========================================================
    // HISTOGRAM
    // =========================================================

    private double[] getFaceHistogram(
            BufferedImage face
    ) {

        double[] histogram =
                new double[256];

        int count = 0;

        int width =
                face.getWidth();

        int height =
                face.getHeight();

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

                int rgb =
                        face.getRGB(
                                x,
                                y
                        );

                int r =
                        (rgb >> 16) & 0xFF;

                int g =
                        (rgb >> 8) & 0xFF;

                int b =
                        rgb & 0xFF;

                // Ignore white background created
                // by face extraction.

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

        if (count == 0) {
            return null;
        }

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

            mean1 += h1[i];
            mean2 += h2[i];
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
                    h1[i] - mean1;

            double value2 =
                    h2[i] - mean2;

            numerator +=
                    value1 * value2;

            denominator1 +=
                    value1 * value1;

            denominator2 +=
                    value2 * value2;
        }

        double denominator =
                Math.sqrt(
                        denominator1 *
                                denominator2
                );

        if (
                denominator == 0
        ) {

            return 0;
        }

        return numerator /
                denominator;
    }

    // =========================================================
    // SHOW MATCH
    // =========================================================

    private void showMatch(
            MatchResult result
    ) {

        if (result == null) {
            return;
        }

        similarityLabel.setText(
                String.format(
                        "%.4f",
                        result.similarity
                )
        );

        decisionLabel.setText(
                result.decision
        );

        statusLabel.setText(
                "ID: " +
                        result.idFile.getName() +
                        "  •  SELFIE: " +
                        result.selfieFile.getName()
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

        showBufferedImage(
                selfieOriginalLabel,
                result.originalSelfie
        );

        showBufferedImage(
                selfieLayerLabel,
                result.extractedSelfie
        );

        showBufferedImage(
                idOriginalLabel,
                result.originalID
        );

        showBufferedImage(
                idLayerLabel,
                result.extractedID
        );
    }

    // =========================================================
    // SHOW BUFFERED IMAGE
    // =========================================================

    private void showBufferedImage(
            JLabel label,
            BufferedImage image
    ) {

        if (
                image == null
        ) {

            label.setIcon(
                    null
            );

            label.setText(
                    "NO IMAGE"
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
                        label.getWidth() - 20
                );

        int labelHeight =
                Math.max(
                        100,
                        label.getHeight() - 20
                );

        double scaleX =
                (double)
                        labelWidth /
                        imageWidth;

        double scaleY =
                (double)
                        labelHeight /
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
    }

    // =========================================================
    // SHOW FILE IMAGE
    // =========================================================

    private void showImage(
            JLabel label,
            File file
    ) {

        try {

            BufferedImage image =
                    ImageIO.read(
                            file
                    );

            if (image != null) {

                showBufferedImage(
                        label,
                        image
                );
            }

        } catch (IOException e) {

            label.setIcon(
                    null
            );

            label.setText(
                    "IMAGE ERROR"
            );
        }
    }

    // =========================================================
    // FIND ALL DATASET IMAGES
    // =========================================================

    private List<File> findAllImages(
            File root
    ) {

        List<File> images =
                new ArrayList<>();

        scanDirectory(
                root,
                images
        );

        return images;
    }

    // =========================================================
    // RECURSIVE SCAN
    // =========================================================

    private void scanDirectory(
            File directory,
            List<File> images
    ) {

        File[] files =
                directory.listFiles();

        if (files == null) {
            return;
        }

        for (File file : files) {

            if (file.isDirectory()) {

                scanDirectory(
                        file,
                        images
                );

            } else if (
                    isImage(file)
            ) {

                images.add(
                        file
                );
            }
        }
    }

    // =========================================================
    // IS IMAGE
    // =========================================================

    private boolean isImage(
            File file
    ) {

        if (
                file == null ||
                        !file.isFile()
        ) {

            return false;
        }

        String name =
                file.getName()
                        .toLowerCase();

        return
                name.endsWith(".jpg") ||
                        name.endsWith(".jpeg") ||
                        name.endsWith(".png") ||
                        name.endsWith(".bmp") ||
                        name.endsWith(".gif");
    }

    // =========================================================
    // CSV
    // =========================================================

    private void saveCSV(
            List<File> idImages
    ) {

        if (
                datasetPathField == null
        ) {
            return;
        }

        File dataset =
                new File(
                        datasetPathField
                                .getText()
                                .trim()
                );

        File csv =
                new File(
                        dataset,
                        "Comparison_Results.csv"
                );

        try (
                PrintWriter writer =
                        new PrintWriter(
                                new FileWriter(
                                        csv
                                )
                        )
        ) {

            writer.println(
                    "Rank,ID_Image,Selfie_Image,Similarity,Result"
            );

            int rank = 1;

            for (
                    MatchResult result :
                    results
            ) {

                writer.println(
                        rank +
                                "," +
                                csvEscape(
                                        result.idFile
                                                .getName()
                                ) +
                                "," +
                                csvEscape(
                                        result.selfieFile
                                                .getName()
                                ) +
                                "," +
                                result.similarity +
                                "," +
                                csvEscape(
                                        result.decision
                                )
                );

                rank++;
            }

            log(
                    "CSV saved: " +
                            csv.getAbsolutePath()
            );

        } catch (IOException e) {

            log(
                    "Could not save CSV: " +
                            e.getMessage()
            );
        }
    }

    // =========================================================
    // CSV ESCAPE
    // =========================================================

    private String csvEscape(
            String value
    ) {

        if (value == null) {
            return "";
        }

        if (
                value.contains(",") ||
                        value.contains("\"") ||
                        value.contains("\n")
        ) {

            return "\"" +
                    value.replace(
                            "\"",
                            "\"\""
                    ) +
                    "\"";
        }

        return value;
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

        progressBar.setValue(
                0
        );

        progressBar.setString(
                "READY"
        );
    }

    // =========================================================
    // CLEAR IMAGES
    // =========================================================

    private void clearImages() {

        clearImageLabel(
                selfieOriginalLabel
        );

        clearImageLabel(
                selfieLayerLabel
        );

        clearImageLabel(
                idOriginalLabel
        );

        clearImageLabel(
                idLayerLabel
        );

        if (
                !uploadedSelfies.isEmpty()
        ) {

            showImage(
                    selfieOriginalLabel,
                    uploadedSelfies.get(0)
            );
        }
    }

    // =========================================================
    // CLEAR IMAGE LABEL
    // =========================================================

    private void clearImageLabel(
            JLabel label
    ) {

        if (label == null) {
            return;
        }

        label.setIcon(
                null
        );

        label.setText(
                "NO IMAGE"
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
