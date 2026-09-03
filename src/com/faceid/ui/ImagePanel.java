package com.faceid.ui;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {

    private final JLabel imageLabel;

    public ImagePanel(
            String title
    ) {

        super(new BorderLayout());

        setBackground(Color.WHITE);

        setBorder(
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

        setPreferredSize(
                new Dimension(
                        650,
                        620
                )
        );

        JLabel titleLabel =
                UiStyles.blackLabel(
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

        add(
                titleLabel,
                BorderLayout.NORTH
        );

        imageLabel =
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

        imageLabel.setOpaque(true);

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

        add(
                imageLabel,
                BorderLayout.CENTER
        );
    }

    public JLabel getImageLabel() {
        return imageLabel;
    }
}

