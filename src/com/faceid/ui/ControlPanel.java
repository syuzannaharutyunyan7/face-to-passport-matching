package com.faceid.ui;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

import static com.faceid.config.AppConfig.DEFAULT_DATASET_PATH;

public class ControlPanel extends JPanel {

    private final JTextField pathField;
    private final JButton runButton;

    public ControlPanel(
            Runnable browseAction,
            Consumer<JButton> runAction
    ) {

        setBackground(
                Color.WHITE
        );

        setBorder(
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

        setPreferredSize(
                new Dimension(
                        310,
                        0
                )
        );

        setLayout(
                new BoxLayout(
                        this,
                        BoxLayout.Y_AXIS
                )
        );

        add(
                UiStyles.blackLabel(
                        "DATASET FOLDER",
                        15,
                        Font.BOLD
                )
        );

        add(
                Box.createVerticalStrut(8)
        );

        pathField =
                new JTextField();

        pathField.setText(
                DEFAULT_DATASET_PATH
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

        add(pathField);

        add(
                Box.createVerticalStrut(8)
        );

        JButton browseButton =
                new JButton(
                        "BROWSE"
                );

        UiStyles.styleButton(
                browseButton,
                Color.WHITE,
                Color.BLACK
        );

        browseButton.addActionListener(
                e -> browseAction.run()
        );

        add(browseButton);

        add(
                Box.createVerticalStrut(15)
        );

        runButton =
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
                e -> runAction.accept(runButton)
        );

        add(runButton);

        add(
                Box.createVerticalStrut(25)
        );

        add(
                UiStyles.blackLabel(
                        "PIPELINE",
                        15,
                        Font.BOLD
                )
        );

        add(
                Box.createVerticalStrut(10)
        );

        add(
                UiStyles.blackLabel(
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
                )
        );

        add(
                Box.createVerticalGlue()
        );

        add(
                UiStyles.blackLabel(
                        "<html><b>Similarity threshold:</b><br>" +
                                "0.50 = SAME PERSON<br>" +
                                "Below 0.50 = DIFFERENT</html>",
                        13,
                        Font.PLAIN
                )
        );
    }

    public String getDatasetPath() {
        return pathField.getText().trim();
    }

    public void setDatasetPath(
            String path
    ) {
        pathField.setText(path);
    }

    public JButton getRunButton() {
        return runButton;
    }
}

