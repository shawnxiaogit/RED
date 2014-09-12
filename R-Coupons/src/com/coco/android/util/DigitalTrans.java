package com.coco.android.util;

import java.io.UnsupportedEncodingException;

public class DigitalTrans {
	
	public static void main(String[] args) {
		String hex = "3031353002007038040000C0001931363930303933323031303030303034323534304130303130303030303030303539353030303030313031393436353330363136303131303030303130303241414141414141414141414141414130343041443130332020202020203436303032333030373336333034383836303130393032313932393532323734322CB5A5CCE5B3ACCAD02C30342CD7A8C2F42C30362CCAD6B1EDD7A8C2F42C317C322CB5A5CCE5B3ACCAD02C30312CBBF9B4A1B3C9B9A6CDBCCFF131612C31302CD6F7BBF5BCDC2C317C322CB5A5CCE5B3ACCAD02C30312CBBF9B4A1B3C9B9A6CDBCCFF12C31312CB1D8B1B8B0FCD7B02C317C322CB5A5CCE5B3ACCAD02C30312CBBF9B4A1B3C9B9A6CDBCCFF12C31322CB6E0B5E3B3C2C1D02C317C322CB5A5CCE5B3ACCAD02C30322CC6B7C5C6B3C2C1D02C31332CC4CCD3C5A1A2B9FBD6ADB6D1CFE42C317C322CB5A5CCE5B3ACCAD02C30322CC6B7C5C6B3C2C1D02C31342CC4CCD3C5A1A2B9FBD6ADB6CBBCDC2C317C322CB5A5CCE5B3ACCAD02C30322CC6B7C5C6B3C2C1D02C31362C666473612C31826D6E88664779FC";
		try {
			System.out.println(new String(hexStringToByte(hex),"GBK"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 数字字符串转ASCII码字符串
	 * 
	 * @param String
	 *            字符串
	 * @return ASCII字符串
	 */
	public static String StringToAsciiString(String content) {
		String result = "";
		int max = content.length();
		for (int i = 0; i < max; i++) {
			char c = content.charAt(i);
			String b = Integer.toHexString(c);
			result = result + b;
		}
		return result;
	}
	
	/**
	 * 
	 * @Description 字符串转化为16进制字符串
	 * @author Shawn
	 * @Time 2013-10-4  下午2:25:03
	 * @param param
	 * @return String
	 * @exception exception
	 */
	public static String stringToHexString2(String strPart) {

		String hexString = "";

		for (int i = 0; i < strPart.length(); i++) {

			int ch = (int) strPart.charAt(i);

			String strHex = Integer.toHexString(ch);
			if(strHex.length()>2){
				strHex = strHex.substring(6);
			}
			hexString = hexString + strHex;

		}

		return hexString;

	}
	
	
	/**
	 * 
	 * @Description 字符串转化为16进制字符串
	 * @author Shawn
	 * @Time 2013-10-4  下午2:25:03
	 * @param param
	 * @return String
	 * @exception exception
	 */
	public static String integerToHexString3(int strPart) {
		

		String strHex = Integer.toHexString(strPart);

		if(strHex.length()>2){
			strHex = strHex.substring(6);
		}

		return strHex;

	}

	/**
	 * 十六进制转字符串
	 * 
	 * @param hexString
	 *            十六进制字符串
	 * @param encodeType
	 *            编码类型4：Unicode，2：普通编码
	 * @return 字符串
	 */
	public static String hexStringToString(String hexString, int encodeType) {
		String result = "";
		int max = hexString.length() / encodeType;
		for (int i = 0; i < max; i++) {
			char c = (char) DigitalTrans.hexStringToAlgorism(hexString
					.substring(i * encodeType, (i + 1) * encodeType));
			result += c;
		}
		return result;
	}

	/**
	 * 十六进制字符串装十进制
	 * 
	 * @param hex
	 *            十六进制字符串
	 * @return 十进制数值
	 */
	public static int hexStringToAlgorism(String hex) {
		hex = hex.toUpperCase();
		int max = hex.length();
		int result = 0;
		for (int i = max; i > 0; i--) {
			char c = hex.charAt(i - 1);
			int algorism = 0;
			if (c >= '0' && c <= '9') {
				algorism = c - '0';
			} else {
				algorism = c - 55;
			}
			result += Math.pow(16, max - i) * algorism;
		}
		return result;
	}

	/**
	 * 十六转二进制
	 * 
	 * @param hex
	 *            十六进制字符串
	 * @return 二进制字符串
	 */
	public static String hexStringToBinary(String hex) {
		hex = hex.toUpperCase();
		String result = "";
		int max = hex.length();
		for (int i = 0; i < max; i++) {
			char c = hex.charAt(i);
			switch (c) {
			case '0':
				result += "0000";
				break;
			case '1':
				result += "0001";
				break;
			case '2':
				result += "0010";
				break;
			case '3':
				result += "0011";
				break;
			case '4':
				result += "0100";
				break;
			case '5':
				result += "0101";
				break;
			case '6':
				result += "0110";
				break;
			case '7':
				result += "0111";
				break;
			case '8':
				result += "1000";
				break;
			case '9':
				result += "1001";
				break;
			case 'A':
				result += "1010";
				break;
			case 'B':
				result += "1011";
				break;
			case 'C':
				result += "1100";
				break;
			case 'D':
				result += "1101";
				break;
			case 'E':
				result += "1110";
				break;
			case 'F':
				result += "1111";
				break;
			}
		}
		return result;
	}

	/**
	 * ASCII码字符串转数字字符串
	 * 
	 * @param String
	 *            ASCII字符串
	 * @return 字符串
	 */
	public static String AsciiStringToString(String content) {
		String result = "";
		int length = content.length() / 2;
		for (int i = 0; i < length; i++) {
			String c = content.substring(i * 2, i * 2 + 2);
			int a = hexStringToAlgorism(c);
			char b = (char) a;
			String d = String.valueOf(b);
			result += d;
		}
		return result;
	}

	/**
	 * 
	 * @Description 字符串转化为16进制字符串
	 * @author Shawn
	 * @Time 2013-10-4  下午2:25:03
	 * @param param
	 * @return String
	 * @exception exception
	 */
	public static String stringToHexString(String strPart) {

		String hexString = "";

		for (int i = 0; i < strPart.length(); i++) {

			int ch = (int) strPart.charAt(i);

			String strHex = Integer.toHexString(ch);

			hexString = hexString + strHex;

		}

		return hexString;

	}

	/**
	 * 将十进制转换为指定长度的十六进制字符串
	 * 
	 * @param algorism
	 *            int 十进制数字
	 * @param maxLength
	 *            int 转换后的十六进制字符串长度
	 * @return String 转换后的十六进制字符串
	 */
	public static String algorismToHEXString(int algorism, int maxLength) {
		String result = "";
		result = Integer.toHexString(algorism);

		if (result.length() % 2 == 1) {
			result = "0" + result;
		}
		return patchHexString(result.toUpperCase(), maxLength);
	}

	/**
	 * 字节数组转为普通字符串（ASCII对应的字符）
	 * 
	 * @param bytearray
	 *            byte[]
	 * @return String
	 */
	public static String bytetoString(byte[] bytearray) {
		String result = "";
		char temp;

		int length = bytearray.length;
		for (int i = 0; i < length; i++) {
			temp = (char) bytearray[i];
			result += temp;
		}
		return result;
	}

	/**
	 * 二进制字符串转十进制
	 * 
	 * @param binary
	 *            二进制字符串
	 * @return 十进制数值
	 */
	public static int binaryToAlgorism(String binary) {
		int max = binary.length();
		int result = 0;
		for (int i = max; i > 0; i--) {
			char c = binary.charAt(i - 1);
			int algorism = c - '0';
			result += Math.pow(2, max - i) * algorism;
		}
		return result;
	}

	/**
	 * 十进制转换为十六进制字符串
	 * 
	 * @param algorism
	 *            int 十进制的数字
	 * @return String 对应的十六进制字符串
	 */
	public static String algorismToHEXString(int algorism) {
		String result = "";
		result = Integer.toHexString(algorism);

		if (result.length() % 2 == 1) {
			result = "0" + result;

		}
		result = result.toUpperCase();

		return result;
	}

	/**
	 * HEX字符串前补0，主要用于长度位数不足。
	 * 
	 * @param str
	 *            String 需要补充长度的十六进制字符串
	 * @param maxLength
	 *            int 补充后十六进制字符串的长度
	 * @return 补充结果
	 */
	static public String patchHexString(String str, int maxLength) {
		String temp = "";
		for (int i = 0; i < maxLength - str.length(); i++) {
			temp = "0" + temp;
		}
		str = (temp + str).substring(0, maxLength);
		return str;
	}

	/**
	 * 将一个字符串转换为int
	 * 
	 * @param s
	 *            String 要转换的字符串
	 * @param defaultInt
	 *            int 如果出现异常,默认返回的数字
	 * @param radix
	 *            int 要转换的字符串是什么进制的,如16 8 10.
	 * @return int 转换后的数字
	 */
	public static int parseToInt(String s, int defaultInt, int radix) {
		int i = 0;
		try {
			i = Integer.parseInt(s, radix);
		} catch (NumberFormatException ex) {
			i = defaultInt;
		}
		return i;
	}

	/**
	 * 将一个十进制形式的数字字符串转换为int
	 * 
	 * @param s
	 *            String 要转换的字符串
	 * @param defaultInt
	 *            int 如果出现异常,默认返回的数字
	 * @return int 转换后的数字
	 */
	public static int parseToInt(String s, int defaultInt) {
		int i = 0;
		try {
			i = Integer.parseInt(s);
		} catch (NumberFormatException ex) {
			i = defaultInt;
		}
		return i;
	}

	/**
	 * 十六进制字符串转为Byte数组,每两个十六进制字符转为一个Byte
	 * 
	 * @param hex
	 *            十六进制字符串
	 * @return byte 转换结果
	 */
	public static byte[] hexStringToByte(String hex) {
		int max = hex.length() / 2;
		byte[] bytes = new byte[max];
		String binarys = DigitalTrans.hexStringToBinary(hex);
		for (int i = 0; i < max; i++) {
			bytes[i] = (byte) DigitalTrans.binaryToAlgorism(binarys.substring(
					i * 8 + 1, (i + 1) * 8));
			if (binarys.charAt(8 * i) == '1') {
				bytes[i] = (byte) (0 - bytes[i]);
			}
		}
		return bytes;
	}

	/**
	 * 十六进制串转化为byte数组
	 * 
	 * @return the array of byte
	 */
	public static final byte[] hex2byte(String hex)
			throws IllegalArgumentException {
		if (hex.length() % 2 != 0) {
			throw new IllegalArgumentException();
		}
		char[] arr = hex.toCharArray();
		byte[] b = new byte[hex.length() / 2];
		for (int i = 0, j = 0, l = hex.length(); i < l; i++, j++) {
			String swap = "" + arr[i++] + arr[i];
			int byteint = Integer.parseInt(swap, 16) & 0xFF;
			b[j] = new Integer(byteint).byteValue();
		}
		return b;
	}

	/**
	 * 字节数组转换为十六进制字符串
	 * 
	 * @param b
	 *            byte[] 需要转换的字节数组
	 * @return String 十六进制字符串
	 */
	public static final String byte2hex(byte b[]) {
		if (b == null) {
			throw new IllegalArgumentException(
					"Argument b ( byte array ) is null! ");
		}
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0xff);
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
		}
		return hs.toUpperCase();
	}
	
	
}