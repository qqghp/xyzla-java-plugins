package com.xyzla.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.*;
import javax.naming.directory.*;
import java.io.*;
import java.net.Socket;
import java.util.Hashtable;

public class EmailValidator {
    private static final Logger logger = LoggerFactory.getLogger(EmailValidator.class);

    public static boolean validate(String email) {
        try {
            String domain = email.substring(email.indexOf("@") + 1);

            // 获取域名的MX记录
            Hashtable<String, Object> env = new Hashtable<String, Object>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory");
            DirContext ctx = new InitialDirContext(env);
            Attributes attrs = ctx.getAttributes(domain, new String[]{"MX"});
            Attribute attr = attrs.get("MX");

            if (attr == null) {
                return false;
            }

            // 连接到邮箱服务器并发送命令
            String[] mxRecords = attr.toString().split(",");
            for (String mxRecord : mxRecords) {
                String server = mxRecord.split(" ")[1];
                try (Socket socket = new Socket(server, 25)) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                    // 发送HELO命令
                    writer.write("HELO " + domain + "\r\n");
                    writer.flush();
                    reader.readLine();

                    // 发送RCPT TO命令
                    writer.write("RCPT TO: " + email + "\r\n");
                    writer.flush();
                    String response = reader.readLine();

                    if (response.startsWith("250")) {
                        return true;
                    }
                } catch (IOException ex) {
                    logger.error("", ex);
                    // 连接失败，继续尝试下一个服务器
                    continue;
                }
            }
        } catch (Exception ex) {
            logger.error("", ex);
            return false;
        }

        return false;
    }

    public static void main(String[] args) {
        String email = "example@gmail.com";
        email = "ryan@showmac.com";
        boolean isValid = validate(email);
        System.out.println("Is " + email + " valid? " + isValid);
    }
}
