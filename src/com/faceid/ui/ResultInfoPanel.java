package com.faceid.ui;

import javax.swing.*;
import java.awt.*;

public class ResultInfoPanel extends JPanel {

    private final JLabel statusLabel;
    private final JLabel similarityLabel;
    private final JLabel decisionLabel;

    public ResultInfoPanel() {

        super(
                new GridLayout(
                        1,
                        3,
                        10,
                        0
                )
        );

        setBackground(
                new Color(
                        238,
                        241,
                        245
                )
        );

        setPreferredSize(
                new Dimension(
                        0,
                        105
                )
        );

        JPanel decisionBox =
                createInfoBox("DECISION");

        decisionLabel =
                UiStyles.blackLabel(
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
                createInfoBox("SIMILARITY");

        similarityLabel =
                UiStyles.blackLabel(
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
                createInfoBox("STATUS");

        statusLabel =
                UiStyles.blackLabel(
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

        add(decisionBox);
        add(similarityBox);
        add(statusBox);
    }

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
                UiStyles.blackLabel(
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

    public JLabel getStatusLabel() {
        return statusLabel;
    }

    public JLabel getSimilarityLabel() {
        return similarityLabel;
    }

    public JLabel getDecisionLabel() {
        return decisionLabel;
    }
}

