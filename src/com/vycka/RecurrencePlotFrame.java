package com.vycka;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;

public class RecurrencePlotFrame extends JFrame {

    private static final int CONTROL_PANEL_WIDTH = 200;
    private static final int RECURRENCE_PLOT_SIZE = 800;

    public static void main(String[] args) {
        //инициализируем главный фрейм
        RecurrencePlotFrame me = new RecurrencePlotFrame();
        me.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        me.setLayout(new BorderLayout());
        me.setSize(RECURRENCE_PLOT_SIZE + CONTROL_PANEL_WIDTH,
                RECURRENCE_PLOT_SIZE);
        me.setLocationRelativeTo(null);
        me.setResizable(false);

        //инициализруем панель с изображением
        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(Data.getRecurrencePlotImage(), 0, 0,
                        RECURRENCE_PLOT_SIZE, RECURRENCE_PLOT_SIZE,
                        null);
            }
        };
        imagePanel.setSize(RECURRENCE_PLOT_SIZE, RECURRENCE_PLOT_SIZE);
        me.add(imagePanel, BorderLayout.CENTER);

        //инициализруем панель с контроллами
        JPanel controlsPanel = new JPanel();
        controlsPanel.setSize(CONTROL_PANEL_WIDTH, RECURRENCE_PLOT_SIZE);
        controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.PAGE_AXIS));

        NumberFormatter timeSeriesFileIndexFormatter = new NumberFormatter(NumberFormat.getIntegerInstance());
        timeSeriesFileIndexFormatter.setMinimum(1);
        timeSeriesFileIndexFormatter.setMaximum(8);
        JTextField timeSeriesFileIndexTextField = new BoundFormattedJTextField<>(timeSeriesFileIndexFormatter, Data::getTimeSeriesFileIndex, Data::setTimeSeriesFileIndex, Integer::parseInt);

        NumberFormatter dNumberFormatter = new NumberFormatter(NumberFormat.getIntegerInstance());
        dNumberFormatter.setMinimum(1);
        JTextField dTextField = new BoundFormattedJTextField<>(dNumberFormatter, Data::getd, Data::setd, Integer::parseInt);

        NumberFormatter DNumberFormatter = new NumberFormatter(NumberFormat.getIntegerInstance());
        DNumberFormatter.setMinimum(2);
        JTextField DTextField = new BoundFormattedJTextField<>(DNumberFormatter, Data::getD, Data::setD, Integer::parseInt);

//        ButtonGroup lenCalculationMethodButtonGroup = new ButtonGroup();
//        lenCalculationMethodButtonGroup.add(new JRadioButton("Euclid"));
//        lenCalculationMethodButtonGroup.add(new JRadioButton("Taxicab"));
//        lenCalculationMethodButtonGroup.add(new JRadioButton("Infinity"));

        NumberFormatter blackPointsPercentNumberFormatter = new NumberFormatter(NumberFormat.getPercentInstance());
//        blackPointsPercentNumberFormatter.setMinimum(0);
//        blackPointsPercentNumberFormatter.setMaximum(1);
        JTextField blackPointsPercentTextField = new BoundFormattedJTextField<>(blackPointsPercentNumberFormatter, Data::getBlackPointsPercent, Data::setBlackPointsPercent, Double::parseDouble);

        controlsPanel.add(new JLabel("timeSeries index (1-8)"));
        controlsPanel.add(timeSeriesFileIndexTextField);
        controlsPanel.add(new JLabel("d >= 1"));
        controlsPanel.add(dTextField);
        controlsPanel.add(new JLabel("D >= 2"));
        controlsPanel.add(DTextField);
        //controlsPanel.add(lenCalculationMethodButtonGroup);
        controlsPanel.add(new JLabel("black points percent"));
        controlsPanel.add(blackPointsPercentTextField);

        JButton updateButton = new JButton("UPDATE");
        updateButton.addActionListener(e -> {Data.updateRecurrencePlotImage(); me.repaint();});
        controlsPanel.add(updateButton);

        me.add(controlsPanel, BorderLayout.LINE_END);
        me.setVisible(true);
    }
}
