/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: PTPSend
 * Author:   sunhongyang
 * Date:     2018/7/18 10:43
 * Description: 测试点对点发送端
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package TestTheadPool;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 〈一句话功能简述〉<br> 
 * 〈测试点对点发送端〉
 *
 * @author sunhongyang
 * @create 2018/7/18
 * @since 1.0.0
 */
public class PTPSend {

    //连接账号
    private String userName = "admin" ;
    //连接密码
    private String password = "admin";
    //链接地址
    private String brokerURL = "tcp://192.168.199.223:61616";
    //connection 工厂
    private ConnectionFactory factory;
    //连接对象
    private Connection connection;
    //一个操作会话
    private Session session;
    //目的地，其实就是连接到哪个队列，如果是点对点，那么他的实现是Queue ，如果是订阅模式，那么他的实现是Topic
    private Destination destination;
    //生产者，就是产生数据的对象
    private MessageProducer producer;

    public static void main(String[] args) {
        PTPSend send = new PTPSend();
        send.start();
    }

    private void start() {
        try {
            //根据用户名，密码，url 创建一个连接工厂
            factory = new ActiveMQConnectionFactory(userName,password,brokerURL);
            //从工厂中获取一个连接
            connection = factory.createConnection();
            connection.start();
            //创建一个session
            //第一个参数：是否支持事务，如果为true 则会忽略第二个参数，被JMS服务器设置为SESSION_TRANSACTED
            //第二个参数为false时，paramB的值可为Session.AUTO_ACKNOWLEDGE，Session.CLIENT_ACKNOWLEDGE，DUPS_OK_ACKNOWLEDGE其中一个
            //Session.AUTO_ACKNOWLEDGE为自动确认，客户端发送和接收消息不需要做额外的工作。哪怕是接收端发生异常，也会被当作正常发送成功。
            //Session.CLIENT_ACKNOWLEDGE为客户端确认。客户端接收到消息后，必须调用javax.jms.Message的acknowledge方法。jms服务器才会当作发送成功，并删除消息。
            //DUPS_OK_ACKNOWLEDGE允许副本的确认模式。一旦接收方应用程序的方法调用从处理消息处返回，会话对象就会确认消息的接收；而且允许重复确认。
            session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
            //创建一个到达的目的地，其实想一下就知道了，activemq不可能同时只能跑一个队列吧，这里就是连接了一个名为"text-msg"的队列，这个会话将会到这个队列，当然，如果这个队列不存在，将会被创建
            destination = session.createQueue("text-msg");
            //从Session中获取一个消息生产者
            producer = session.createProducer(destination);
            //设置生产者的模式，有两种可选
            //DeliveryMode.PERSISTENT 当activemq关闭的时候，队列数据将会被保存
            //DeliveryMode.NON_PERSISTENT 当activemq关闭的时候，队列里面的数据将会被清空
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);

            //创建一条消息，当然，消息的类型有很多，如文字/字节/对象等，可以通过session.create..方法来创建
            TextMessage textMessage = session.createTextMessage("呵呵");

            for (int i = 0; i < 100; i++) {
                //发送一条消息
                producer.send(textMessage);
            }

            System.out.println("发送消息成功");
            //即便生产者的对象关闭了，程序还在运行
            producer.close();



        } catch (JMSException e) {
            e.printStackTrace();
        }

    }


}
