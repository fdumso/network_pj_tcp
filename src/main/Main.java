package main;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            if (args.length == 1) {
                switch (args[0]) {
                    case "--alice": {
                        new Alice();
                        break;
                    }
                    case "--bob": {
                        new Bob();
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
