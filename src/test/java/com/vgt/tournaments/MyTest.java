package com.vgt.tournaments;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
public class MyTest {

  @Test
  void dateConversion() {

    LocalDate date = LocalDate.now();

    long epochDay = date.toEpochDay();

    LocalDate localDate = LocalDate.ofEpochDay(20234);


    System.out.println(date);
    System.out.println(epochDay
    );
    System.out.println(localDate);
  }


}
