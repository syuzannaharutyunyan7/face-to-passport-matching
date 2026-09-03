package com.faceid.ui;

import javax.swing.*;
import java.awt.*;

public final class UiStyles {

    private UiStyles() {
    }

    public static JLabel blackLabel(
            String text,
            int size,
            int style
    ) {

        JLabel label =
                new JLabel(text);

        label.setForeground(Color.BLACK);

        label.setFont(
                new Font(
                        "Arial",
                        style,
                        size
                )
        );

        return label;
    }

    public static void styleButton(
            JButton button,
            Color background,
            Color foreground
    ) {

        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(true);

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );
    }
}

