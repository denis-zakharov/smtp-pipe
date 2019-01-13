import org.subethamail.smtp.server.SMTPServer;

import javax.mail.Message;
import javax.mail.Session;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// Accepts incoming mails and send them from one queue reusing connection to SMTP hub
public class SMTPPipe {
    public static void main(String[] args) {
        // Mail Session to SMTP hub
        Properties props = new Properties();
        props.put("mail.smtp.host", "localhost");
        props.put("mail.smtp.port", 25000);
        Session session = Session.getInstance(props, null);

        // Message queue
        ConcurrentLinkedQueue<Message> queue = new ConcurrentLinkedQueue<>();

        QueueMessageHandlerFactory queueFactory = new QueueMessageHandlerFactory(queue, session) ;

        // Accept messages
        SMTPServer smtpServer = new SMTPServer(queueFactory);
        smtpServer.setPort(26000);
        smtpServer.start();

        // Send messages from queue
        MailQueueConsumer consumer = new MailQueueConsumer(queue, session);
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(consumer, 10, 30, TimeUnit.SECONDS);
    }
}