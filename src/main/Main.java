package main;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            if (args.length == 1) {
                switch (args[0]) {
                    case "--alice": {
                        new Proxy("127.0.0.1", 7000, "172.19.0.1", 9000).run();
                        break;
                    }
                    case "--bob": {
                        new Proxy("172.20.0.1", 9000, "127.0.0.1", 8000).run();
                        break;
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
