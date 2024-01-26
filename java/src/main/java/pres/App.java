package pres;

import com.softwaremill.jox.Channel;

public class App {
    public static void main(String[] args) {
        Thread.startVirtualThread(() -> new Channel(0));
        System.out.println("Hello World!");
    }
}
