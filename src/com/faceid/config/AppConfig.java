package com.faceid.config;

public final class AppConfig {

    private AppConfig() {
    }

    public static final int MIN_HUE = 3;
    public static final int MAX_HUE = 24;
    public static final int N = 256;
    public static final double LOG_THRESHOLD = 3.0;
    public static final int RANGE_START = 40;
    public static final int RANGE_END = 80;
    public static final double THRESHOLD = 0.50;

    public static final String DEFAULT_DATASET_PATH =
            "/Users/syuzannaharutyunyan/Desktop/Selfie-and-ID-Dataset-main/";
}

