package pres;

public class S0200_Channels {
    /*

    To communicate between stages, we need a concurrent data structure:

    * queue-like (send/receive)
    * can be closed:
      * completed
      * error
    * interruption-friendly
    * fast

    Inspiration from Kotlin:
    [Fast and Scalable Channels in Kotlin Coroutines](https://arxiv.org/abs/2211.04986)

    Implementation in Java:
    [Jox](https://github.com/softwaremill/jox)

     */
}
