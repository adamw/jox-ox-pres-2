package pres;

import com.softwaremill.jox.Channel;
import com.softwaremill.jox.ChannelDone;
import com.softwaremill.jox.ChannelError;
import scala.Function1;

public class S0300_High_level_API {
    // Is that enough?

    static class RichChannel<T> {
        public final Channel<T> channel;

        public RichChannel(Channel<T> ch) {
            this.channel = ch;
        }

        public <U> RichChannel<U> map(Function1<T, U> f) {
            var out = new Channel<U>(16);
            Thread.ofVirtual().start(() -> {
                try {
                    while (true) {
                        var v = channel.receiveOrClosed();
                        if (v instanceof ChannelDone) {
                            out.done();
                            break;
                        } else if (v instanceof ChannelError ce) {
                            out.error(ce.cause());
                            break;
                        } else {
                            //noinspection unchecked
                            out.send(f.apply((T) v));
                        }
                    }
                } catch (Exception e) {
                    out.error(e);
                }
            });
            return new RichChannel<>(out);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        var ch = new RichChannel<>(new Channel<Integer>(16));

        ch.channel.send(1);
        ch.channel.send(1);

        var result = ch.map(x -> x + 1)
                .map(x -> x * 2)
                .map(x -> x + 5)
                .map(x -> x * 10);

        System.out.println(result.channel.receive());
        System.out.println(result.channel.receive());
    }
}
