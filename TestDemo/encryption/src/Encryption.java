import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Encryption {

    //加密的初始值
    private static String pwd1 = "0402";
    private static String pwd2 = "0209";
    private static String pwd3="2018-07-06";
    private static String pwd4 = "0706";

    public static void main(String[] args) throws Exception {

        Scanner in=new Scanner(System.in);
        System.out.println("请输入第一个密钥：");
        String p1=in.next();
        System.out.println("请输入第二个密钥：");
        String p2=in.next();
        System.out.println("请输入第三个密钥：");
        String p3=in.next();
        System.out.println("请输入第四个密钥(按enter结束)：");
        String p4=in.next();
        String origin=encoding(pwd1,pwd2,pwd3,pwd4);
        String shuru=encoding(p1,p2,p3,p4);
        if(origin.equals(shuru))
            System.out.println("输入的密码正确");
        else System.out.println("输入的密码错误");
    }

    private static String encoding(String s1,String s2,String s3,String s4)throws Exception{
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        long timestamp=sdf.parse(s3).getTime();
        String sha1_password = SHA1Util.encryptSHA(pwd1+ "-" + pwd2 + "-" + timestamp + "-" + pwd4);
        return sha1_password;
    }
}