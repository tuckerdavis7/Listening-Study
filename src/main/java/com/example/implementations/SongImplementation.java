package com.example.implementations;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class SongImplementation {
    

    public int getMostReplayedTimestamp(String url) {

        System.setProperty("webdriver.chrome.driver", "./driver/chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);

        try {
            driver.get(url);
            driver.manage().window().maximize();

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("path.ytp-heat-map-path")));

            int videoDuration = getVideoDuration(driver);

            String pageSource = driver.getPageSource();
            Document doc = Jsoup.parse(pageSource);

            Element heatmapPath = doc.selectFirst("path.ytp-heat-map-path");
            if (heatmapPath != null) {
                String dAttribute = heatmapPath.attr("d");
                //System.out.println("Heatmap path data: " + dAttribute);
                return extractMostReplayedTimestamp(dAttribute, videoDuration);
            }
            else {
                System.out.println("Heatmap not found.");
            }

        }
        catch (Exception e) {
            System.out.println(e.toString());
        }

        return -1;
    }

    private int getVideoDuration(WebDriver driver) {
        try {
            WebElement durationElement = driver.findElement(By.cssSelector(".ytp-time-duration"));
            String durationText = durationElement.getText();
            return convertTimeToSeconds(durationText);
        }
        catch (Exception e) {
            System.out.println("Failed to get video duration: " + e.getMessage());
            return -1;
        }
    }

    public int convertTimeToSeconds(String time) {
        String[] parts = time.split(":");
        int seconds = 0;
        if (parts.length == 2) { // MM:SS format
            seconds = Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
        }
        else if (parts.length == 3) { // HH:MM:SS format
            seconds = Integer.parseInt(parts[0]) * 3600 + Integer.parseInt(parts[1]) * 60 + Integer.parseInt(parts[2]);
        }
        return seconds;
    }

    private int extractMostReplayedTimestamp(String heatmap, int videoDuration) {
        Pattern pattern = Pattern.compile("([0-9\\.]+),([0-9\\.]+)");
        Matcher matcher = pattern.matcher(heatmap);

        double minY = Double.MAX_VALUE;
        double timestamp = -1.0;

        while (matcher.find()) {
            double x = Double.parseDouble(matcher.group(1));
            double y = Double.parseDouble(matcher.group(2));

            if (y < minY) {
                minY = y;
                timestamp = x;
            }
        }

        return (int) Math.round(timestamp / 1000.0 * videoDuration);
    }

    public String extractVideoId(String url) {
        String regex = "(?:youtube\\.com/watch\\?v=|youtu\\.be/)([a-zA-Z0-9_-]{11})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return "INVALID";
    }
}
