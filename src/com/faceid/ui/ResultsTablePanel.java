package com.faceid.ui;

import com.faceid.model.Result;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.function.Consumer;

public class ResultsTablePanel extends JPanel {

    private final DefaultTableModel tableModel;
    private final JTable resultTable;

    public ResultsTablePanel(
            Consumer<Integer> rowSelectionListener
    ) {

        super(new BorderLayout());

        setBackground(Color.WHITE);

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
                                5,
                                5,
                                5,
                                5
                        )
                )
        );

        JLabel tableTitle =
                UiStyles.blackLabel(
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

        add(
                tableTitle,
                BorderLayout.NORTH
        );

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

                            rowSelectionListener.accept(
                                    modelRow
                            );
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

        add(
                tableScroll,
                BorderLayout.CENTER
        );
    }

    public void clear() {
        tableModel.setRowCount(0);
    }

    public void addResult(
            Result result
    ) {

        tableModel.addRow(
                new Object[]{
                        result.getFolder(),
                        result.getId(),
                        result.getSelfie(),
                        String.format(
                                "%.4f",
                                result.getSimilarity()
                        ),
                        result.getDecision()
                }
        );

        int row =
                tableModel.getRowCount() - 1;

        resultTable
                .setRowSelectionInterval(
                        row,
                        row
                );
    }

    public void selectFirstRow() {

        if (
                tableModel.getRowCount() > 0
        ) {

            resultTable
                    .setRowSelectionInterval(
                            0,
                            0
                    );
        }
    }
}

