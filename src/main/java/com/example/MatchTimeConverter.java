package com.example;

import java.util.*;

/**
 * Converter class that converts a match time string from one format to another
 */
public class MatchTimeConverter {

  /**
   * Number of periods in match
   */
  static final int MATCH_PERIODS = 2;

  /**
   * Number of millseconds in a period
   */
  static final int PERIOD_MILLISECONDS = 45 * 60 * 1000;

  /**
   * Number of milliseconds in a fulltime match
   */
  static final int FULLTIME_MILLISECONDS = MATCH_PERIODS * PERIOD_MILLISECONDS;

  /**
   * Accepts a match time in the format "[H1] 15:00.100" and returns a match time in the format
   * "15:00 – FIRST_HALF"
   *
   * @param matchTime match time string to convert
   * @return converted string
   */
  public static String convertTime(String matchTime) {
    // must not be null, empty string, or incorrect format
    if (!validInputString(matchTime)) {
      return "INVALID";
    }
    // must be valid period
    String periodString = matchTime.substring(0, 4);
    if (!validPeriod(periodString)) {
      return "INVALID";
    }

    // must be valid time
    String timeString = matchTime.substring(5);
    if (!validTimeString(timeString)) {
      return "INVALID";
    }

    String period = matchTime.substring(1, 3);
    String convertedString = transformString(period, timeString);

    return convertedString;
  }

  /**
   * Transforms a matchtime string value
   *
   * @param period The match time period
   * @param time The current match time in the format MM:SS:MMM
   * @return a formatted string with the rounded time, long form period, and any extra time values
   */
  private static String transformString(String period, String time) {
    int totalMs = msFromTimeString(time);
    int roundedMs = roundToNearestSecond(totalMs);

    String output = "";
    String extraTime;

    switch(period) {
      case "PM":
        output = "00:00 – " + Period.PM.getLongName();
        break;
      case "H1":
        if (roundedMs < PERIOD_MILLISECONDS) {
          output = millisecondsToTimeString(roundedMs) + " – " + Period.H1.getLongName();
        } else {
          extraTime = millisecondsToTimeString(roundedMs - PERIOD_MILLISECONDS);
          output = "45:00 +" + extraTime + " – " + Period.H1.getLongName();
        }
        break;
      case "HT":
        output = "45:00 – " + Period.HT.getLongName();
        break;
      case "H2":
        if (roundedMs < PERIOD_MILLISECONDS) {
          return "INVALID";
        } else if (roundedMs <= (FULLTIME_MILLISECONDS)) {
          output = millisecondsToTimeString(roundedMs) + " – " + Period.H2.getLongName();
        } else {
          extraTime = millisecondsToTimeString(roundedMs - (FULLTIME_MILLISECONDS));
          output = "90:00 +" + extraTime + " – " + Period.H2.getLongName();
        }
        break;
      case "FT":
        extraTime = millisecondsToTimeString(roundedMs - (PERIOD_MILLISECONDS * 2));
        output = "90:00 +" + extraTime + " – " + Period.FT.getLongName();
        break;
      default:
        return "INVALID";
    }
    return output;
  }

  /**
   * Helper method that validates input string is not empty and in correct format, ie
   * [HT] 45:00.000
   *
   * @param inputString input match time string
   * @return true if the inputString is in correct format
   */
  private static boolean validInputString(String inputString) {
    String expectedFormat = "\\[..\\]\\s\\d{1,2}:\\d{2}.\\d{3}";
    if ((inputString == null) || !(inputString.matches(expectedFormat))) {
      return false;
    }
    return true;
  }

  /**
   * Helper method to validate a match time period input value
   *
   * @param period shortform match time period, ie [H1]
   * @return true if the input value is valid
   */
  private static boolean validPeriod(String period) {
    // format must be correct, ie [FT]
    if (!period.matches("\\[..\\]")) {
      return false;
    }

    // Needs to be a valid period value
    period = period.substring(1, 3);
    for (Period p : Period.values()) {
      if (p.name().equals(period)) {
        return true;
      }
    }
    return false;
  }


  /**
   * Helper method to validate a time string input value
   * @param timeString time string input value, ie
   * @return true if the string contains valid minute, second, and millisecond values
   */
  private static boolean validTimeString(String timeString) {
    String timeValues[] = timeString.split("[:.]");

    // must be minute, second, and ms values
    if (timeValues.length != 3) {
      return false;
    }

    // all value digits must be positive integer values
    for (int i = 0; i < 3; i++) {
      if (!timeValues[i].matches("\\d+")) {
        return false;
      }
    }

    return true;
  }

  /**
   * Helper method to round a value in milliseconds to the nearest second
   *
   * @param milliseconds millisecond value to round
   * @return a value rounded to the nearest second
   */
  private static int roundToNearestSecond(int milliseconds) {
    int roundedMs = 0;
    int spareMs = milliseconds % 1000;
    if (spareMs == 0) {
      return milliseconds;
    } else if (spareMs >= 500) {
      roundedMs += milliseconds + (1000 - spareMs);
    } else {
      roundedMs = milliseconds - spareMs;
    }
    return roundedMs;
  }

  /**
   * Helper method to calculate the number of milliseconds in a time string
   *
   * @param timeString time string in the form MM:SS:MMM
   * @return total number of milliseconds in time string
   */
  private static int msFromTimeString(String timeString) {
    String[] timeValues = timeString.split("[:.]");
    int minutes = Integer.parseInt(timeValues[0]);
    int seconds = Integer.parseInt(timeValues[1]);
    int milliseconds = Integer.parseInt(timeValues[2]);
    return (minutes * 60000) + (seconds * 1000) + milliseconds;
  }

  /**
   * Helper method to return a string that represents the total minutes and seconds
   * in a millisecond value
   *
   * @param totalMs millisecond value to convert
   * @return MM:SS string that represents millisecond value
   */
  private static String millisecondsToTimeString(int totalMs) {
    // calculate string time value
    int minutes = (int) Math.floor(totalMs / 60000);
    int seconds = (totalMs -(minutes * 60000)) / 1000;

    String mins = minutes <= 9? "0" + minutes : String.valueOf(minutes);
    String secs = seconds <= 9? "0" + seconds : String.valueOf(seconds);

    return mins + ":" + secs;
  }

  public static void main(String[] args) {
    HashMap<String, String> testCases = new LinkedHashMap<String, String>() {{
      put("[PM] 0:00.000", "00:00 – PRE_MATCH");
      put("[H1] 0:15.025", "00:15 – FIRST_HALF");
      put("[H1] 3:07.513", "03:08 – FIRST_HALF");
      put("[H1] 45:00.001", "45:00 +00:00 – FIRST_HALF");
      put("[H1] 46:15.752", "45:00 +01:16 – FIRST_HALF");
      put("[HT] 45:00.000", "45:00 – HALF_TIME");
      put("[H2] 45:00.500", "45:01 – SECOND_HALF");
      put("[H2] 90:00.908", "90:00 +00:01 – SECOND_HALF");
      put("[FT] 90:00.000", "90:00 +00:00 – FULL_TIME");
      put("90:00", "INVALID");
      put("[H3] 90:00.000", "INVALID");
      put("[PM] -10:00.000", "INVALID");
      put("FOO", "INVALID");
    }};
    System.out.println("Input:\t\t\tExpected:\t\t\tActual:");
    for (Map.Entry<String, String> entry : testCases.entrySet()) {
      String input = entry.getKey();
      String expected = entry.getValue();
      String actual = convertTime(input);
      System.out.println(input +"\t" + expected + "\t" + actual);
    }
  }

}
