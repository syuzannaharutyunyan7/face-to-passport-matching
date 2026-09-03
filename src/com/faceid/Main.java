package com.faceid;

import com.faceid.ui.FaceComparisonFrame;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            FaceComparisonFrame app =
                    new FaceComparisonFrame();

            app.createInterface();
        });
    }
}
