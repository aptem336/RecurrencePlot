package com.vycka;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;

public class RecurrencePlotFrame extends JFrame {

    public static void main(String[] args) {
        //инициализируем главный фрейм
        RecurrencePlotFrame me = new RecurrencePlotFrame();
        me.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        me.getContentPane().setLayout(new GridLayout(1, 3));
        me.setSize(1800,
                600);
        me.setLocationRelativeTo(null);
        me.setResizable(false);

        //инициализруем панель с графиком
        JPanel signalPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Double[] timeSeries = Data.getTimeSeries();
                double max = -Double.MAX_VALUE;
                double min = Double.MAX_VALUE;
                for (Double timeSeriesSignal : timeSeries) {
                    max = Math.max(max, timeSeriesSignal);
                    min = Math.min(min, timeSeriesSignal);
                }
                double dx = (double) (getWidth() - 10) / timeSeries.length;
                double dy = (getHeight() - 10) / Math.abs(max - min);
                for (int i = 0; i < timeSeries.length - 1; i++) {
                    g.drawLine((int) (i * dx), (int) (timeSeries[i] * dy) + getHeight() / 2,
                            (int) ((i + 1) * dx), (int) (timeSeries[i + 1] * dy) + getHeight() / 2);
                }
            }
        };
        me.add(signalPanel);

        //инициализруем панель с изображением
        JPanel recurrencePlotImagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(Data.getRecurrencePlotImage(), 0, 0,
                        getWidth() - 10, getHeight() - 10,
                        null);
            }
        };
        me.add(recurrencePlotImagePanel);

        //инициализруем панель с контроллами
        JPanel controlsPanel = new JPanel();
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

        JRadioButton euclid = new JRadioButton("Euclid");
        JRadioButton taxicab = new JRadioButton("Taxicab");
        JRadioButton infinity = new JRadioButton("Infinity");

        ButtonGroup lenCalculationMethodButtonsGroup = new ButtonGroup();
        lenCalculationMethodButtonsGroup.add(euclid);
        lenCalculationMethodButtonsGroup.add(taxicab);
        lenCalculationMethodButtonsGroup.add(infinity);
        lenCalculationMethodButtonsGroup.setSelected(euclid.getModel(), true);

        JPanel lenCalculationMethodButtonsPanel = new JPanel();
        lenCalculationMethodButtonsPanel.add(euclid);
        lenCalculationMethodButtonsPanel.add(taxicab);
        lenCalculationMethodButtonsPanel.add(infinity);

        euclid.addActionListener(e -> Data.setLenCalculationMethod(new LenCalculationMethod.EuclidCalculationMethod()));
        taxicab.addActionListener(e -> Data.setLenCalculationMethod(new LenCalculationMethod.TaxicabCalculationMethod()));
        infinity.addActionListener(e -> Data.setLenCalculationMethod(new LenCalculationMethod.InfinityCalculationMethod()));

        NumberFormatter blackPointsPercentNumberFormatter = new NumberFormatter(NumberFormat.getPercentInstance());
        blackPointsPercentNumberFormatter.setMinimum(0.0d);
        blackPointsPercentNumberFormatter.setMaximum(1.0d);
        JTextField blackPointsPercentTextField = new BoundFormattedJTextField<>(blackPointsPercentNumberFormatter, Data::getBlackPointsPercent, Data::setBlackPointsPercent, Double::parseDouble);

        NumberFormatter blackPointsPercentEpsNumberFormatter = new NumberFormatter(NumberFormat.getPercentInstance());
        blackPointsPercentEpsNumberFormatter.setMinimum(0.0d);
        blackPointsPercentEpsNumberFormatter.setMaximum(1.0d);
        JTextField blackPointsPercentEpsTextField = new BoundFormattedJTextField<>(blackPointsPercentEpsNumberFormatter, Data::getBlackPointsPercentEps, Data::setBlackPointsPercentEps, Double::parseDouble);

        controlsPanel.add(new JLabel("timeSeries index (1-8)"));
        controlsPanel.add(timeSeriesFileIndexTextField);
        controlsPanel.add(new JLabel("d >= 1"));
        controlsPanel.add(dTextField);
        controlsPanel.add(new JLabel("D >= 2"));
        controlsPanel.add(DTextField);
        controlsPanel.add(new JLabel("black points percent"));
        controlsPanel.add(blackPointsPercentTextField);
        controlsPanel.add(new JLabel("black points percent eps"));
        controlsPanel.add(blackPointsPercentEpsTextField);
        controlsPanel.add(lenCalculationMethodButtonsPanel);

        JButton updateButton = new JButton("UPDATE");
        updateButton.addActionListener(e -> {
            Data.updateRecurrencePlotImage();
            me.repaint();
        });
        controlsPanel.add(updateButton);

        me.add(controlsPanel);
        me.setVisible(true);
    }
}
