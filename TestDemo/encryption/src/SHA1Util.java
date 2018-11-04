import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;


/**
 *
 * SHA1算法
 * SHA，全称为“Secure Hash Algorithm”，中文名“安全哈希算法”，
 * 主要适用于数字签名标准（Digital Signature Standard DSS）里面定义的数字签名算法（Digital Signature Algorithm DSA）。
 * 对于长度小于 2^64 位的消息，SHA1 会产生一个 160 位的消息摘要。
 * 该算法的思想是接收一段明文，然后以一种不可逆的方式将它转换成一段（通常更小）密文，也可以简单的理解为取一串输入码（称为预映射或信息），
 * 并把它们转化为长度较短、位数固定的输出序列即散列值的过程。
 *
 */
public class SHA1Util {

	/** 
     * 定义加密方式 
     */   
    private final static String KEY_SHA1 = "SHA-1";  
	
    /** 
     * 构造函数 
     */  
    public SHA1Util() {  
  
    }  
    
    /** 
     * SHA1 加密 
     * @param data 需要加密的字符串 
     * @return 加密之后的字符串 
     * @throws Exception 
     */  
    public static String encryptSHA(String data) throws Exception {  
    
        // 验证传入的字符串  
        if (null == data || "".equals(data)) {  
            return "";  
        }  
        // 创建具有指定算法名称的信息摘要  
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA1);  
        // 使用指定的字节数组对摘要进行最后更新  
        sha.update(data.getBytes());  
        // 完成摘要计算  
        byte[] bytes = sha.digest();  
        // 将得到的字节数组变成字符串返回  
        return Hex.encodeHexString(bytes);
    }  
    
}
