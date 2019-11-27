package com.example;

/**
 * Enum class that represents the long and short form names of a match time period
 */
public enum Period {
  /**
   * Prematch period of match
   */
  PM("PRE_MATCH"),

  /**
   * First half period of a match
   */
  H1("FIRST_HALF"),

  /**
   * Halftime period of a match
   */
  HT("HALF_TIME"),

  /**
   * Second half period of a match
   */
  H2("SECOND_HALF"),

  /**
   * Fulltime period of a match
   */
  FT("FULL_TIME");

  /**
   * Longform name of a match period
   */
  private String longName;

  /**
   * Sets the long form name of a match period
   *
   * @param periodLongName longform name of match period
   */
  Period(String periodLongName) {
    this.longName = periodLongName;
  }

  /**
   * Gets the long form name of a match time period
   *
   * @return long form name of match period
   */
  public String getLongName() {
    return longName;
  }
}
