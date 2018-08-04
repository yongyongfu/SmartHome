package com.demo.smarthome.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * �ַ��� ������
 * 
 * @author Administrator
 * 
 */
public class StrTools {
	public static int byteToUint(byte b) {
		return (int) (b & 0xFF);

	}

	public static byte[] byteToSwapByte(byte[] b) {
		int length = b.length;
		byte[] buff = new byte[length];
		for (int i = 0; i < length; i++) {
			buff[i] = b[length - 1 - i];
		}
		return buff;
	}

	public static long stringToInt(String strNum) {
		long index = 0;
		byte[] b = strNum.getBytes();
		byte[] buff = new byte[b.length];
		long num = 0;
		for (int i = 0; i < b.length; i++) {
			if ((b[i] >= 0x30) && (b[i] <= 0x39)) {
				num *= 10;
				num += b[i] - 0x30;
			} else {
				// if(index != 0){
				// break;
				// }
			}
		}

		// try {
		// tmp = new String(buff);
		// //num = Integer.parseInt(strNum.trim());
		// num = Integer.parseInt(new String(buff));
		// } catch (Exception e) {
		// num = 0;
		// }
		return num;
	}

	// public static int strToInt(String str) {
	// // TODO Auto-generated method stub
	// return 0;
	// }
	public static long bcdIntToInt(long num) {
		int[] valPar = new int[] { 1, 10, 100, 1000, 10000, 100000, 1000000,
				10000000 };
		int val = 0;
		int i = 0;
		for (i = 0; i < 8; i++) {

			val += (num % 16) * valPar[i];
			num /= 16;
		}
		return val;
		// int val = 0;
		// try {
		// val = Integer.parseInt(num + "", 10);
		// } catch (Exception e) {
		// val = 0;
		// }
		// return val;
	}

	// public static int strToInt(String str) {
	// // TODO Auto-generated method stub
	// return 0;
	// }
	public static long intToBcdInt(long num) {
		// int val = 0;
		// try {
		// val = Integer.parseInt(num + "", 16);
		// } catch (Exception e) {
		// val = 0;
		// }
		// return val;
		long val = 0;
		int i = 0;
		for (i = 0; i < 8; i++) {
			val += (num % 10) << (i * 4);
			num /= 10;
		}
		return val;
	}

	public static long idTo8ByteId(long num) {
		// int val = 0;
		// try {
		// val = Integer.parseInt(num + "", 16);
		// } catch (Exception e) {
		// val = 0;
		// }
		// return val;
		long val = 0;
		int i = 0;
		for (i = 0; i < 8; i++) {
			val += (num % 10) << (i * 4);
			num /= 10;
		}
		return val;
	}

	/*
	 * Convert byte[] to hex
	 * string.�������ǿ��Խ�byteת����int��Ȼ������Integer.toHexString(int)��ת����16�����ַ�����
	 * 
	 * @param src byte[] data
	 * 
	 * @return hex string
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
			stringBuilder.append(" ");
		}
		return stringBuilder.toString();
	}

	/**
	 * Convert hex string to byte[]
	 * 
	 * @param hexString
	 *            the hex string
	 * @return byte[]
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		if (hexString.length() % 2 != 0) {
			hexString = "0" + hexString;
		}
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	/**
	 * Convert char to byte
	 * 
	 * @param c
	 *            char
	 * @return byte
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	// ��ָ��byte������16���Ƶ���ʽ��ӡ������̨
	public static void printHexString(byte[] b) {
		System.out.print("len:" + b.length + "  ");
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			System.out.print(hex.toUpperCase() + " ");
		}
		System.out.println();
	}

	public static String bytes2HexString(byte[] b) {
		String ret = "";
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			ret += hex.toUpperCase();
		}
		return ret;
	}

	/*
	 * �����ǽ�byte[]ת��ʮ�����Ƶ��ַ���,ע������b[ i ] & 0xFF��һ��byte��
	 * 0xFF������������,Ȼ��ʹ��Integer.toHexStringȡ����ʮ�������ַ���,���Կ��� b[ i ] &
	 * 0xFF�����ó�����Ȼ�Ǹ�int,��ôΪ��Ҫ�� 0xFF������������?ֱ�� Integer.toHexString(b[ i
	 * ]);,��byteǿתΪint������?���ǲ��е�.
	 * 
	 * ��ԭ������: 1.byte�Ĵ�СΪ8bits��int�Ĵ�СΪ32bits 2.java�Ķ����Ʋ��õ��ǲ�����ʽ
	 * 
	 * ����������ϰ�¼������������
	 * 
	 * byte��һ���ֽڱ���ģ���8��λ����8��0��1�� 8λ�ĵ�һ��λ�Ƿ���λ�� Ҳ����˵0000 0001�����������1 1000
	 * 0000����ľ���-1 �����������λ0111 1111��Ҳ��������127 �������Ϊ1111 1111��Ҳ��������-128
	 * 
	 * ����˵���Ƕ�����ԭ�룬������java�в��õ��ǲ������ʽ�����������ʲô�ǲ���
	 * 
	 * 1�����룺 һ������������������ķ�����ԭ����ͬ�� һ��������Ǹ��������λΪ1�������λ�Ƕ�ԭ��ȡ����
	 * 
	 * 2�����룺������������ǿ��Խ�������ɼӷ� ����ʮ����������9�õ�5���ü����� 9��4��5 ��Ϊ4+6��10�����ǿ��Խ�6��Ϊ4�Ĳ���
	 * ��дΪ�ӷ��� 9+6��15��ȥ����λ1��Ҳ���Ǽ�10���õ�5.
	 * 
	 * ����ʮ������������c��5���ü����� c��7��5 ��Ϊ7+9��16 ��9��Ϊ7�Ĳ��� ��дΪ�ӷ��� c+9��15��ȥ����λ1��Ҳ���Ǽ�16���õ�5.
	 * 
	 * �ڼ�����У����������1���ֽڱ�ʾһ������һ���ֽ���8λ������8λ�ͽ�1�����ڴ������Ϊ��100000000������λ1��������
	 * 
	 * ��һ����Ϊ����������ԭ�롢���롢������ͬ ��һ����Ϊ�����շ���λΪ1�������λ�Ƕ�ԭ��ȡ����Ȼ����������1
	 * 
	 * - 1��ԭ��Ϊ 10000001 - 1�ķ���Ϊ 11111110 + 1 - 1�Ĳ���Ϊ 11111111
	 * 
	 * 0��ԭ��Ϊ 00000000 0�ķ���Ϊ 11111111������͸���ķ�����ͬ�� +1 0�Ĳ���Ϊ
	 * 100000000�������ͷ��1������͸���Ĳ�����ͬ��
	 * 
	 * Integer.toHexString�Ĳ�����int�����������&0xff����ô��һ��byte��ת����intʱ������int��32λ��
	 * ��byteֻ��8λ��ʱ����в�λ��
	 * ���粹��11111111��ʮ������Ϊ-1ת��Ϊintʱ��Ϊ11111111111111111111111111111111�ö�1��
	 * ���Ǻǣ���0xffffffff����������ǲ��Եģ����ֲ�λ�ͻ������ ��0xff����󣬸�24���ؾͻᱻ��0�ˣ�����Ͷ��ˡ�
	 * 
	 * ---- Java�е�һ��byte���䷶Χ��-128~127�ģ���Integer.toHexString�Ĳ���������int�����������&0xff��
	 * ��ô��һ��byte��ת����intʱ
	 * �����ڸ���������λ��չ��������˵��һ��byte��-1����0xff�����ᱻת����int��-1����0xffffffff
	 * ������ôת�����Ľ���Ͳ���������Ҫ���ˡ�
	 * 
	 * ��0xffĬ�������Σ����ԣ�һ��byte��0xff������Ƚ��Ǹ�byteת�����������㣬����������еĸߵ�24�����ؾ��ܻᱻ��0�����ǽ������������Ҫ��
	 * ��
	 */
	/*
	 * ��16�����ַ���ת�����ֽ�����
	 * 
	 * @param hex
	 * 
	 * @return
	 */
	public static byte[] hexStringToByte(String hex) {
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}

	private static byte toByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}

	/** */
	/**
	 * ���ֽ�����ת����16�����ַ���
	 * 
	 * @param bArray
	 * @return
	 */
	// public static final String bytesToHexString(byte[] bArray) {
	// StringBuffer sb = new StringBuffer(bArray.length);
	// String sTemp;
	// for (int i = 0; i < bArray.length; i++) {
	// sTemp = Integer.toHexString(0xFF & bArray[i]);
	// if (sTemp.length() < 2)
	// sb.append(0);
	// sb.append(sTemp.toUpperCase());
	// sb.append(" ");
	// }
	// return sb.toString();
	// }

	/** */
	/**
	 * ���ֽ�����ת��Ϊ����
	 * 
	 * @param bytes
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static final Object bytesToObject(byte[] bytes) throws IOException,
			ClassNotFoundException {
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		ObjectInputStream oi = new ObjectInputStream(in);
		Object o = oi.readObject();
		oi.close();
		return o;
	}

	/** */
	/**
	 * �ѿ����л�����ת�����ֽ�����
	 * 
	 * @param s
	 * @return
	 * @throws IOException
	 */
	public static final byte[] objectToBytes(Serializable s) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream ot = new ObjectOutputStream(out);
		ot.writeObject(s);
		ot.flush();
		ot.close();
		return out.toByteArray();
	}

	public static final String objectToHexString(Serializable s)
			throws IOException {
		return bytesToHexString(objectToBytes(s));
	}

	public static final Object hexStringToObject(String hex)
			throws IOException, ClassNotFoundException {
		return bytesToObject(hexStringToByte(hex));
	}

	/** */
	/**
	 * @��������: BCD��תΪ10���ƴ�(����������)
	 * @�������: BCD��
	 * @������: 10���ƴ�
	 */
	public static String bcd2Str(byte[] bytes) {
		StringBuffer temp = new StringBuffer(bytes.length * 2);

		for (int i = 0; i < bytes.length; i++) {
			temp.append((byte) ((bytes[i] & 0xf0) >>> 4));
			temp.append((byte) (bytes[i] & 0x0f));
		}
		return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp
				.toString().substring(1) : temp.toString();
	}

	/**
	 * *
	 * 
	 * @��������: 10���ƴ�תΪBCD��
	 * @�������: 10���ƴ�
	 * @������: BCD��
	 */
	public static byte[] str2Bcd(String asc) {
		int len = asc.length();
		int mod = len % 2;

		if (mod != 0) {
			asc = "0" + asc;
			len = asc.length();
		}

		byte abt[] = new byte[len];
		if (len >= 2) {
			len = len / 2;
		}

		byte bbt[] = new byte[len];
		abt = asc.getBytes();
		int j, k;

		for (int p = 0; p < asc.length() / 2; p++) {
			if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
				j = abt[2 * p] - '0';
			} else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
				j = abt[2 * p] - 'a' + 0x0a;
			} else {
				j = abt[2 * p] - 'A' + 0x0a;
			}

			if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
				k = abt[2 * p + 1] - '0';
			} else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
				k = abt[2 * p + 1] - 'a' + 0x0a;
			} else {
				k = abt[2 * p + 1] - 'A' + 0x0a;
			}

			int a = (j << 4) + k;
			byte b = (byte) a;
			bbt[p] = b;
		}
		return bbt;
	}

	/** */
	/**
	 * @��������: BCD��תASC��
	 * @�������: BCD��
	 * @������: ASC��
	 */
	// public static String BCD2ASC(byte[] bytes) {
	// StringBuffer temp = new StringBuffer(bytes.length * 2);
	//
	// for (int i = 0; i < bytes.length; i++) {
	// int h = ((bytes[i] & 0xf0) >>> 4);
	// int l = (bytes[i] & 0x0f);
	// temp.append(BToA[h]).append( BToA[l]);
	// }
	// return temp.toString() ;
	// }

	/** */
	/**
	 * MD5�����ַ��������ؼ��ܺ��16�����ַ���
	 * 
	 * @param origin
	 * @return
	 */
	public static String MD5EncodeToHex(String origin) {
		return bytesToHexString(MD5Encode(origin));
	}

	/** */
	/**
	 * MD5�����ַ��������ؼ��ܺ���ֽ�����
	 * 
	 * @param origin
	 * @return
	 */
	public static byte[] MD5Encode(String origin) {
		return MD5Encode(origin.getBytes());
	}

	/** */
	/**
	 * MD5�����ֽ����飬���ؼ��ܺ���ֽ�����
	 * 
	 * @param bytes
	 * @return
	 */
	public static byte[] MD5Encode(byte[] bytes) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
			return md.digest(bytes);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return new byte[0];
		}

	}

	/**
	 * id��578437695752307204 16������0x807060504030204 ת���ɣ�402030405060708
	 * 
	 * @param strNum
	 * @return
	 */
	public static String strNumToBig(String strNum) {
		String str = "";
		// byte[] b = StrTools.hexStringToBytes(strNum);
		long num = Long.parseLong(strNum);
		int len = 0;

		String temp = Long.toHexString(num);
		len = temp.length();
		while (len > 0) {
			if (len < 2) {
				str += "0" + temp.substring(0, len);
				len = 0;
			} else {
				str += temp.substring(len - 2, len);
				len -= 2;
			}
		}
		if (str.length() > 2) {
			if (str.substring(0, 1).equals("0")) {
				str = str.substring(1, str.length());
			}
		}
		return str;
	}

	public static String strNumToHex(String strNum) {
		String str = "";
		// byte[] b = StrTools.hexStringToBytes(strNum);
		long num = Long.parseLong(strNum);
		System.out.println("strNum:" + strNum);
		// System.out.println("");
		System.out.println("num:" + num);
		str = Long.toHexString(num);

		System.out.println("str:" + str);
		return str;
	}

	/**
	 * 
	 * @param strNum
	 * @return
	 */
	public static String strHexNumToStr(String strNum) {
		String str = "";
		// byte[] b = StrTools.hexStringToBytes(strNum);
		long num = Long.parseLong(strNum);
		System.out.println("strNum:" + strNum);
		// System.out.println("");
		System.out.println("num:" + num);
		str = Long.toHexString(num);

		System.out.println("str:" + str);
		return str;
	}

	/**
	 * 0x01 0x02 0x03 ==> "66051"
	 * 
	 * @param strNum
	 * @return
	 */
	public static String byteHexNumToStr(byte[] b) {
		String str = "";
		long val = 0;
		int i;
		for (i = b.length - 1; i >= 0; i--) {
			val += byteToUint(b[i]);
			if (i <= 0) {
				break;
			}
			val *= 256;
		}
		str = val + "";
		System.out.println("b:" + StrTools.bytesToHexString(b));
		System.out.println("str:" + str);
		return str;
	}

	/**
	 * id 16���Ƶ�λ��ǰ 0xa1b234671 ==> 10���� 1731506849
	 * 0x123456789 -->0x0123456789 --> 4886718345
	 * @return
	 */
	public static long StrHexLowToLong(String strVal) {
		long val = 0;
		String str = strVal.replace("0x", " ").trim().toUpperCase();

		while (true) {
			if (str.charAt(0) == '0') {
				str = str.substring(1);
			} else {
				break;
			}
		}
		if (str.length() % 2 == 1) {
			str = "0" + str;
		}
		int len = (str.length() / 2);
		// System.out.println("len: " + len+" str:"+str);
		byte[] result = new byte[len];
		char[] achar = str.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		for (int i = 0; i < result.length; i++) {
			val *= 256;
			val += result[i] & 0xFF;
			// System.out.println("StrHexLowToLong() i:" + i + " ="
			// + (result[i] & 0xFF));

		}
		System.out.println("StrHexHighToLong() strVal:" + strVal + "  to val:"
				+ val);
		return val;
	}

	/**
	 * passwd 16���Ƹ�λ��ǰ 0xaf3212341 ==> 10���� 2939294260
	 * 0x987654321 -->0x0132547698 --> 5139363480
	 * @return
	 */
	public static long StrHexHighToLong(String strVal) {
		long val = 0;
		int len;
		String tmp;
		String str = strVal.replace("0x", " ").trim().toUpperCase();
//		while (true) {
//			if (str.charAt(0) == '0') {
//				str = str.substring(1);
//			} else {
//				break;
//			}
//		}
		if(str.equals("")){
			return 0;
		}
		len =str.length();
		if (len % 2 == 1) {
			if(len ==1){
				str = "0"+str;
			}else{
				tmp = str.substring(len-1,len);
				str=str.substring(0,len-1);
				str = str+"0"+tmp;
			}
		}
		len = (str.length() / 2);
		System.out.println("len: " + len + " str:" + str);
		byte[] result = new byte[len];
		char[] achar = str.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		for (int i = result.length - 1; i >= 0; i--) {
			val *= 256;
			val += result[i] & 0xFF;
			// System.out.println("StrHexLowToLong() i:" + i + " =" + (result[i]
			// & 0xFF));
		}
		System.out.println("StrHexLowToLong() strVal:" + strVal + "  to val:"
				+ val);
		return val;

	}
	/**
	 * 
	 * @param strNum
	 * @param isBig
	 *            ���ģʽ
	 * @return
	 */
	// public static long strNumToHex(String strNum){
	// String str ="";
	// long num = Long.parseLong(strNum);
	// Long.parseLong(string, radix)
	// int len = 0;
	//
	// String temp=Long.toHexString(num);
	// if(isBig){
	// len = temp.length();
	// while(len>0){
	// if(len<2){
	// str +="0"+temp.substring(0, len);
	// len = 0;
	// }else{
	// str +=temp.substring(len-2, len);
	// len-=2;
	// }
	// }
	// if(str.length()>2){
	// if(str.substring(0, 1).equals("0"));
	// str= str.substring(1, str.length());
	// }
	// }else{
	// str = temp;
	// }
	// num = Long.parseLong(str);
	//
	//
	//
	// return str;
	// }
}
