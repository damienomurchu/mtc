package com.example;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.example.MatchTimeConverter.convertTime;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class MatchTimeConverterTest {

  /**
   * Tests all invalid input period values
   */
  @Test
  public void testInvalidPeriodValues() {
    Map<String, String> testCases = new HashMap<String, String>() {{
      put(null, "INVALID");
      put("", "INVALID");
      put("[H33] 90:00.000", "INVALID");
      put("H11 90:00.000", "INVALID");
      put("H2 90:00.000", "INVALID");
      put("*H2* 90:00.000", "INVALID");
      put("[%^] 90:00.000", "INVALID");
      put("90:00", "INVALID");
      put("[H3] 90:00.000", "INVALID");
      put("[PM] -10:00.000", "INVALID");
      put("FOO", "INVALID");
    }};
    for (Map.Entry<String, String> entry : testCases.entrySet()) {
      String expected = entry.getValue();
      String actual = convertTime(entry.getKey());
      assertThat(actual, is(equalTo(expected)));
    }
  }

  /**
   * Tests all invalid time inputs
   */
  @Test
  public void testInvalidTimeValues() {
    Map<String, String> testCases = new HashMap<String, String>() {{
      put("[H2] :00.000", "INVALID");
      put("[H2] 00:.000", "INVALID");
      put("[H2] 00:00.", "INVALID");
      put("[H2] 1&:00.000", "INVALID");
      put("[H2] &:00.000", "INVALID");
      put("[H2] 00:1$.000", "INVALID");
      put("[H2] 00:$.000", "INVALID");
      put("[H2] 00:00.4*", "INVALID");
      put("[H2] 00:00.*", "INVALID");
      put("[H2] :.", "INVALID");
    }};
    for (Map.Entry<String, String> entry : testCases.entrySet()) {
      String expected = entry.getValue();
      String actual = convertTime(entry.getKey());
      assertThat(actual, is(equalTo(expected)));
    }
  }

  /**
   * Tests correct output of prematch inputs
   */
  @Test
  public void testPreMatchOutput() {
    Map<String, String> testCases = new HashMap<String, String>() {{
      put("[PM] 0:00.000", "00:00 – PRE_MATCH");
    }};
    for (Map.Entry<String, String> entry : testCases.entrySet()) {
      String expected = entry.getValue();
      String actual = convertTime(entry.getKey());
      assertThat(actual, is(equalTo(expected)));
    }
  }

  /**
   * Tests output is correctly rounded in output values
   */
  @Test
  public void testRounding() {
    Map<String, String> testCases = new HashMap<String, String>() {{
      put("[H1] 0:15.025", "00:15 – FIRST_HALF");
      put("[H1] 3:07.513", "03:08 – FIRST_HALF");
      put("[H1] 45:00.001", "45:00 +00:00 – FIRST_HALF");
      put("[H1] 46:15.752", "45:00 +01:16 – FIRST_HALF");
      put("[H2] 90:00.908", "90:00 +00:01 – SECOND_HALF");
    }};
    for (Map.Entry<String, String> entry : testCases.entrySet()) {
      String expected = entry.getValue();
      String actual = convertTime(entry.getKey());
      assertThat(actual, is(equalTo(expected)));
    }
  }

  /**
   * Tests correct output of first half inputs
   */
  @Test
  public void testFirstHalfOutput() {
    Map<String, String> testCases = new HashMap<String, String>() {{
      put("[H1] 0:15.025", "00:15 – FIRST_HALF");
      put("[H1] 3:07.513", "03:08 – FIRST_HALF");
      put("[H1] 45:00.001", "45:00 +00:00 – FIRST_HALF");
      put("[H1] 46:15.752", "45:00 +01:16 – FIRST_HALF");
    }};
    for (Map.Entry<String, String> entry : testCases.entrySet()) {
      String expected = entry.getValue();
      String actual = convertTime(entry.getKey());
      assertThat(actual, is(equalTo(expected)));
    }
  }

  /**
   * Tests correct output of halftime inputs
   */
  @Test
  public void testHalfTimeInput() {
    Map<String, String> testCases = new HashMap<String, String>() {{
      put("[HT] 45:00.000", "45:00 – HALF_TIME");
    }};
    for (Map.Entry<String, String> entry : testCases.entrySet()) {
      String expected = entry.getValue();
      String actual = convertTime(entry.getKey());
      assertThat(actual, is(equalTo(expected)));
    }
  }

  /**
   * Tests correct output of secondhalf inputs
   */
  @Test
  public void testSecondHalfOutput() {
    Map<String, String> testCases = new HashMap<String, String>() {{
      put("[H2] 45:00.005", "45:00 – SECOND_HALF");
      put("[H2] 45:00.500", "45:01 – SECOND_HALF");
      put("[H2] 90:00.908", "90:00 +00:01 – SECOND_HALF");
    }};
    for (Map.Entry<String, String> entry : testCases.entrySet()) {
      String expected = entry.getValue();
      String actual = convertTime(entry.getKey());
      assertThat(actual, is(equalTo(expected)));
    }
  }

  /**
   * Tests correct output of fulltime inputs
   */
  @Test
  public void testFullTimeInput() {
    Map<String, String> testCases = new HashMap<String, String>() {{
      put("[FT] 90:00.000", "90:00 +00:00 – FULL_TIME");
      put("[FT] 90:00.008", "90:00 +00:00 – FULL_TIME");
      put("[FT] 90:00.908", "90:00 +00:01 – FULL_TIME");
    }};
    for (Map.Entry<String, String> entry : testCases.entrySet()) {
      String expected = entry.getValue();
      String actual = convertTime(entry.getKey());
      assertThat(actual, is(equalTo(expected)));
    }
  }

}