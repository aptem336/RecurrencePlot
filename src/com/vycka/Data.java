package com.vycka;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Data {
    private static Integer timeSeriesFileIndex = 7;
    private static Integer d = 1;
    private static Integer D = 2;
    private static LenCalculationMethod lenCalculationMethod = new LenCalculationMethod.EuclidCalculationMethod();
    private static Double blackPointsPercent = 0.5;

    private static BufferedImage recurrencePlotImage;

    public static BufferedImage getRecurrencePlotImage() {
        return recurrencePlotImage;
    }

    static {
        updateRecurrencePlotImage();
    }

    public static void updateRecurrencePlotImage() {
        Double[] timeSeries = new Double[0];
        try {
            timeSeries = RecurrencePlot.readTimeSeries(timeSeriesFileIndex);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Double[][] timeSeriesCorteges = RecurrencePlot.collectTimeSeriesCorteges(timeSeries, D, d);
        Double[][] timeSeriesCortegesDifferences = RecurrencePlot.calcTimeSeriesCortegesDifferences(timeSeriesCorteges, lenCalculationMethod);
        Double r = RecurrencePlot.calcR(timeSeriesCortegesDifferences, blackPointsPercent);
        recurrencePlotImage = RecurrencePlot.buildRecurrencePlotImage(timeSeriesCortegesDifferences, r);
    }


    public static Integer getTimeSeriesFileIndex(Void aVoid) {
        return timeSeriesFileIndex;
    }

    public static void setTimeSeriesFileIndex(Integer timeSeriesFileIndex) {
        Data.timeSeriesFileIndex = timeSeriesFileIndex;
    }

    public static Integer getd(Void aVoid) {
        return d;
    }

    public static void setd(Integer d) {
        Data.d = d;
    }

    public static Integer getD(Void aVoid) {
        return D;
    }

    public static void setD(Integer d) {
        D = d;
    }

    public static LenCalculationMethod getLenCalculationMethod(Void aVoid) {
        return lenCalculationMethod;
    }

    public static void setLenCalculationMethod(LenCalculationMethod lenCalculationMethod) {
        Data.lenCalculationMethod = lenCalculationMethod;
    }

    public static Double getBlackPointsPercent(Void aVoid) {
        return blackPointsPercent;
    }

    public static void setBlackPointsPercent(Double blackPointsPercent) {
        Data.blackPointsPercent = blackPointsPercent;
    }
}
