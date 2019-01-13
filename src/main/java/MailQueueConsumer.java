import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import java.util.concurrent.ConcurrentLinkedQueue;


public class MailQueueConsumer implements Runnable {

    private final Session session;
    private final ConcurrentLinkedQueue<Message> queue;

    public MailQueueConsumer(ConcurrentLinkedQueue<Message> queue, Session session) {
        this.queue = queue;
        this.session = session;
    }

    @Override
    public void run() {
        if (!queue.isEmpty()) {
            try (Transport transport = session.getTransport("smtp")) {
                transport.connect();
                Message msg;
                while ((msg = queue.poll()) != null) {
                    transport.sendMessage(msg, msg.getAllRecipients());
                }
            } catch (MessagingException mex) {
                mex.printStackTrace();
                Exception ex = null;
                if ((ex = mex.getNextException()) != null) {
                    ex.printStackTrace();
                }
            }
        } else {
            System.out.println("Queue is empty.");
        }
    }
}
