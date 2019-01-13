import org.subethamail.smtp.server.SMTPServer;

public class SMTPHub {
    public static void main(String[] args) {
        LogbackMessageHandlerFactory logbackMessageHandlerFactory = new LogbackMessageHandlerFactory() ;
        SMTPServer smtpServer = new SMTPServer(logbackMessageHandlerFactory);
        smtpServer.setPort(25000);
        smtpServer.start();
    }
}