/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: Server
 * Author:   sunhongyang
 * Date:     2018/7/18 16:35
 * Description: MyTomcat核心服务类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package MyTomcat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 〈一句话功能简述〉<br>
 * 〈MyTomcat核心服务类〉
 *
 * @author sunhongyang
 * @create 2018/7/18
 * @since 1.0.0
 */
public class Server {
    //统计服务
//    private static int count = 0;

    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocket ss = null;
        try {
            int port = 9999;
            ss = new ServerSocket(port);

            while (true) {
                Socket socket = ss.accept();
                InputStream is = socket.getInputStream();
                byte[] buff = new byte[1024];
                int len = is.read(buff);
                if (len > 0) {
                    String msg = new String(buff, 0, len);
                    System.out.println("args = [" + msg + "]");
                } else {
                    System.out.println("bad request! ");
                }

                //创建服务端应答
                OutputStream os = socket.getOutputStream();
                String s = "<html><head><title>滚</title></head><body>滚</body></a'a'a'a'a'a'a'a'a'a'a'a'a'a'a'a'a'a'a>";
                os.write(s.getBytes());

                os.flush();
                os.close();
                //关闭客户端
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ss.close();
        }

    }


}
