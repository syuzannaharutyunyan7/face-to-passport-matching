package com.faceid.ui;

import javax.swing.*;
import java.awt.*;

public class HeaderPanel extends JPanel {

    public HeaderPanel() {

        super(new BorderLayout());

        setBackground(
                new Color(
                        35,
                        70,
                        105
                )
        );

        setBorder(
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

        add(
                headerText,
                BorderLayout.WEST
        );
    }
}

