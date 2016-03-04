import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;
import java.util.Scanner;


/**
 * Created by boss-cp on 2016/3/3.
 */
public class JsoupTest {

    public static  Statement stmt;
    public static ResultSet rs;
    public static Connection conn;

    public static void Insert_Test() throws Exception{
        stmt = null;
        rs = null;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        conn = DriverManager.getConnection("jdbc:mysql://localhost","root","1234");
        stmt = conn.createStatement();
        rs = stmt.executeQuery("SELECT * FROM world.city");
        while(rs.next()){
            System.out.println(rs.getString("Name")+", "+rs.getString("District"));
        }
        rs.close();
        stmt.close();
        conn.close();
        rs = null;
        stmt = null;
        conn = null;
    }

    public static void Init_Jdbc() throws Exception{
        stmt = null;
        rs = null;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        conn = DriverManager.getConnection("jdbc:mysql://localhost","root","1234");
        stmt = conn.createStatement();
    }

    public static void Close_Jdbc() throws Exception{
        rs.close();
        stmt.close();
        conn.close();
        rs = null;
        stmt = null;
        conn = null;
    }

    public static void Get_node(String url) throws Exception{
        String res = null;
        Thread.sleep(1000* (new Random()).nextInt(10)+1);
        while(true) {
            try{
                Document doc = Jsoup.connect(url).timeout(60000).get();
                Element contentArea = doc.getElementById("contentArea");
                Elements post = contentArea.getElementsByClass("post");
                for (Element e : post ) {
                    res = "INSERT INTO `jav`.`av` ( `name`, `src1`, `src2`) VALUES (";
                    Elements postHeader = e.getElementsByClass("postTitle");
                    Elements span = postHeader.get(0).select("span");
                    String s = span.text();
                    res = res + "'" + s + "',";
                    Elements postContent = e.getElementsByClass("postContent");
                    Elements imgs = postContent.get(0).select("img");
                    for (int i = 0; i < 2; i++) {
                        if (i >= imgs.size())
                            break;
                        if (i < imgs.size()) {
                            String u = imgs.get(i).absUrl("src");
                            res = res + "'" + u + "',";
                        }
                    }
                    res = res.replaceAll(",$", ")");
                    System.out.println(res);
                    stmt.execute(res);
                    res = null;
                }
                break;
            }catch (Exception e){
                System.out.println(e.toString());
                System.out.println("enter键重新运行...");
                new Scanner (System.in).nextLine();
                continue;
            }
        }
    }

    public static void main(String[] args) throws Exception{
        //Insert_Test();

        Init_Jdbc();
        //JsoupTest.Get_node("http://inlajav.com/page/1");

        int begin=62;
        int end=1000;
        for(int i=begin;i<end;i++) {
            System.out.println("page:" + i);
            JsoupTest.Get_node("http://inlajav.com/page/" + i);
        }
        Close_Jdbc();
    }
}