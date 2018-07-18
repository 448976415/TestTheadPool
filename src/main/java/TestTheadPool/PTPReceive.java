/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: PTPReceive
 * Author:   sunhongyang
 * Date:     2018/7/18 11:15
 * Description: 测试点对点接收端
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package TestTheadPool;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 〈一句话功能简述〉<br> 
 * 〈测试点对点接收端〉
 *
 * @author sunhongyang
 * @create 2018/7/18
 * @since 1.0.0
 */
public class PTPReceive {

    //连接账号
    private String userName = "admin";
    //连接密码
    private String password = "admin";
    //连接地址
    private String broKerURL = "tcp://192.168.199.223:61616";
    //Connection的工厂
    private ConnectionFactory factory;
    //连接对象
    private Connection connection;
    //一个操作会话
    private Session session;
    //目的地，其实就是连接到哪个队列，如果是点对点，那么它的实现是Queue，如果是订阅模式，那它的实现是Topic
    private Destination destination;
    //消费者，就是接受数据的对象
    private MessageConsumer consumer;

    public static void main(String[] args) {
        PTPReceive receive = new PTPReceive();
        receive.start();
    }

    private void start() {

        try {
            //根据用户名，密码，url创建一个连接工厂
            factory = new ActiveMQConnectionFactory(userName,password,broKerURL);
            //从工厂中获取一个连接
            connection = factory.createConnection();

            connection.start();
            //创建一个session
            //第一个参数:是否支持事务，如果为true，则会忽略第二个参数，被jms服务器设置为SESSION_TRANSACTED
            //第二个参数为false时，paramB的值可为Session.AUTO_ACKNOWLEDGE，Session.CLIENT_ACKNOWLEDGE，DUPS_OK_ACKNOWLEDGE其中一个。
            //Session.AUTO_ACKNOWLEDGE为自动确认，客户端发送和接收消息不需要做额外的工作。哪怕是接收端发生异常，也会被当作正常发送成功。
            //Session.CLIENT_ACKNOWLEDGE为客户端确认。客户端接收到消息后，必须调用javax.jms.Message的acknowledge方法。jms服务器才会当作发送成功，并删除消息。
            //DUPS_OK_ACKNOWLEDGE允许副本的确认模式。一旦接收方应用程序的方法调用从处理消息处返回，会话对象就会确认消息的接收；而且允许重复确认。
            session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
            //创建一个到达的目的地，其实想一下就知道了，activemq不可能同时只能跑一个队列吧，这里就是连接了一个名为"text-msg"的队列，这个会话将会到这个队列，当然，如果这个队列不存在，将会被创建
            destination = session.createQueue("text-msg");

            //根据Session 创建一个接收者对象
            consumer = session.createConsumer(destination);

            //实现一个消息监听器
            //实现这个监听器后，以后只要有消息，就会通过这个监听器接受
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        //获取接收到的数据
                        String text = ((TextMessage)message).getText();
                        System.out.println(text);
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });

            //关闭接收端，也不会终止程序
            //consumer.close();

        } catch (JMSException e) {
            e.printStackTrace();
        }


    }

}
