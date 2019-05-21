package com.revolut.danieledmonds;

import static spark.Spark.get;

public class Alpha {

    void alpha() {
        get("/alpha", (req, res) -> "Alpha");
    }

}
