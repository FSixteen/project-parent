package s.j.l.activemqutils;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class ActivemqUtils {
  // 消息服务器IP
  public static String hostName = "10.231.49.60";
  // 队列管理器
  public static String queueManager = "QM_HIATMP";
  // 通道
  public static String channel = "HIATMP.CHANNEL";
  // Topic
  public static String topic = "HIATMP.HISENSE.ILLEGAL";
  // 端口号
  public static int port = 1414;

  public ActivemqUtils() {
  }

  public ActivemqUtils(String hostName, String queueManager, String channel, String topic,
      int port) {
    super();
    this.hostName = hostName;
    this.queueManager = queueManager;
    this.channel = channel;
    this.topic = topic;
    this.port = port;
  }

  public void run() {
    ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
        ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD,
        "tcp://" + hostName + ":" + port);
    Session session = null;
    Destination receiveQueue;
    try {
      Connection connection = connectionFactory.createConnection();

      session = connection.createSession(true, Session.SESSION_TRANSACTED);
      session.createTopic(topic);
      receiveQueue = session.createQueue(queueManager);
      MessageConsumer consumer = session.createConsumer(receiveQueue);

      connection.start();
      System.out.println(Thread.currentThread().getName() + " start");

      while (true) {
        Message message = consumer.receive();

        if (message instanceof TextMessage) {
          TextMessage receiveMessage = (TextMessage) message;
          System.out.println("我是Receiver,收到消息如下: \r\n" + receiveMessage.getText());
        } else {
          session.commit();
          break;
        }

      }
      connection.close();
      System.out.println(Thread.currentThread().getName() + " close");
    } catch (JMSException e) {
      e.printStackTrace();
    }
  }

}
