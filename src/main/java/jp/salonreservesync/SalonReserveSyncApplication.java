package jp.salonreservesync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SalonReserveSyncApplication
{
  public static void main(String[] args)
  {
    SpringApplication.run(SalonReserveSyncApplication.class, args);
  }
}
