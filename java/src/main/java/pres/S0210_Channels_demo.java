package pres;

import com.softwaremill.jox.Channel;

import static com.softwaremill.jox.Select.selectOrClosed;

public class S0210_Channels_demo {
    private static void demo1() throws InterruptedException {
        var ch = new Channel<Integer>(16);
        ch.send(1);
        ch.send(2);
        ch.send(3);
        ch.done();

        System.out.println(ch.receiveOrClosed());
        System.out.println(ch.receiveOrClosed());
        System.out.println(ch.receiveOrClosed());
        System.out.println(ch.receiveOrClosed());
    }

    private static void demo2() throws InterruptedException {
        var ch = new Channel<Integer>(16);
        ch.send(1);
        ch.send(2);
        ch.send(3);
        ch.error(new IllegalArgumentException("boom"));

        System.out.println(ch.receiveOrClosed());
        System.out.println(ch.receiveOrClosed());
    }

    private static void demo3_startThread(Channel<Integer> ch, int from, int to) {
        Thread.ofVirtual().start(() -> {
            try {
                for (int i = from; i < to; i++) {
                    ch.send(i);
                    Thread.sleep(100);
                }
                ch.done();
            } catch (Exception e) {
                ch.error(e);
            }
        });
    }

    private static void demo3() throws InterruptedException {
        var ch1 = new Channel<Integer>(16);
        var ch2 = new Channel<Integer>(16);

        demo3_startThread(ch1, 0, 5);
        demo3_startThread(ch2, 50, 55);

        for (int i=0; i<11; i++) {
            System.out.println(selectOrClosed(ch1.receiveClause(), ch2.receiveClause()));
        }
    }

    public static void main(String[] args) throws InterruptedException {
        demo1();
    }
}
