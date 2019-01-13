import org.subethamail.smtp.MessageContext;
import org.subethamail.smtp.MessageHandler;
import org.subethamail.smtp.MessageHandlerFactory;
import org.subethamail.smtp.RejectException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueMessageHandlerFactory implements MessageHandlerFactory {
    private final ConcurrentLinkedQueue<Message> queue;
    private final Session session;

    public QueueMessageHandlerFactory(ConcurrentLinkedQueue<Message> queue, Session session) {
        this.session = session;
        this.queue = queue;
        //session.setDebug(true);
    }

    public MessageHandler create(MessageContext ctx) {
        return new Handler(ctx);
    }

    class Handler implements MessageHandler {
        MessageContext ctx;

        public Handler(MessageContext ctx) {
            this.ctx = ctx;
        }

        public void from(String from) throws RejectException {
            System.out.println("FROM:" + from);
        }

        public void recipient(String recipient) throws RejectException {
            System.out.println("RECIPIENT:" + recipient);
        }

        public void data(InputStream data) throws IOException {
            try {
                MimeMessage msg = new MimeMessage(session, data);
                queue.offer(msg);
            } catch (MessagingException e) {
                e.printStackTrace();
            }

        }

        public void done() {
            System.out.println("Finished");
        }

    }
}