package com.faceid.ui;

import javax.swing.*;
import java.awt.*;

public class BottomPanel extends JPanel {

    private final JTextArea logArea;
    private final JProgressBar progressBar;

    public BottomPanel() {

        super(
                new BorderLayout(
                        8,
                        8
                )
        );

        setBackground(
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

        add(
                logScroll,
                BorderLayout.CENTER
        );

        add(
                progressBar,
                BorderLayout.SOUTH
        );
    }

    public JTextArea getLogArea() {
        return logArea;
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }
}

