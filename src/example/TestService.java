package example;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.jws.WebService;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@WebService
public class TestService {

/*    public static void main(String[] args) {
        *//**
         * 参数:1,本地的服务地址;
                2,提供服务的类;
                发布成功后 在浏览器输入 http://192.168.51.23:8080/Service/TestService?wsdl
         *//*
        Endpoint.publish("http://192.168.51.23:8080/Service/TestService",new TestService());
        System.out.println("发布成功");
        //getInsert(TestAxis.getRequestData("pushProToUtrl", true, "1466587993913"));


    }*/

    public String getInsert(String xml){
        System.out.println("接收到来自客户端的报文："+xml);
        Map<String,Object> map = new HashMap<>();
        String name = "";
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(xml); // 将字符串转为XML
            Element rootElt = doc.getRootElement(); // 获取根节点

/*            Iterator iter = rootElt.elementIterator("Body"); // 获取根节点下的子节点Body
            // 遍历Body节点
            while (iter.hasNext()) {
                Element recordEle = (Element) iter.next();
                String title = recordEle.elementTextTrim("title"); // 拿到head节点下的子节点title值
                System.out.println("title:" + title);
                Iterator iters = recordEle.elementIterator("script"); // 获取子节点head下的子节点script
                // 遍历Header节点下的Response节点
                while (iters.hasNext()) {
                    Element itemEle = (Element) iters.next();
                    String username = itemEle.elementTextTrim("username"); // 拿到head下的子节点script下的字节点username的值
                    String password = itemEle.elementTextTrim("password");
                    System.out.println("username:" + username);
                    System.out.println("password:" + password);
                }
            }*/

            Iterator iterss = rootElt.elementIterator("Body"); ///获取根节点下的子节点body
            // 遍历body节点
            while (iterss.hasNext()) {
                Element recordEless = (Element) iterss.next();
                String sysPass = recordEless.elementTextTrim("sysPass"); // 拿到body节点下的子节点sysPass值
                System.out.println("sysPass:" + sysPass);
                Iterator itersElIterator = recordEless.elementIterator("prop"); // 获取子节点body下的子节点prop
                // 遍历prop节点下的Response节点
                while (itersElIterator.hasNext()) {
                    Element itemEle = (Element) itersElIterator.next();

                    map.put("projectName",itemEle.elementTextTrim("projectName")); // 拿到body下的子节点prop下的字节点projectName的值

                    System.out.println(map);

                    //加载驱动程序
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    //连接数据库
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/oa?serverTimezone=CTT&useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true", "root", "root");

                    //编写SQL
                    PreparedStatement preparedStatement = connection.prepareStatement("insert into XML (projectName,opening_area,construction) VALUE (?,?,?)");

                    //占位符，从1开始
                    preparedStatement.setString(1, (String) map.get("projectName"));

                    //返回的结果是一个整数表示该操作影响了数据表中的几条数据
                    int i = preparedStatement.executeUpdate();

                    if(i>0){
                        name = "{\"msg\":\"true\",\"message\":\"新增成功\"}";
                    }else{
                        name = "{\"msg\":\"false\",\"message\":\"新增失败\"}";
                    }
                    //关闭
                    preparedStatement.close();
                    connection.close();

                    System.out.println(i);

                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return name;
    }
}
