package com.revolut.danieledmonds;

import com.google.inject.Singleton;

import java.time.LocalDateTime;

import static spark.Spark.get;

@Singleton
public class HelloMessageService {

    public HelloMessage sayHello() {

       return new HelloMessage("Hello spark!", LocalDateTime.now());
    }

}
