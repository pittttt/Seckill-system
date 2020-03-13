package com.pitt.kill.server.utils;

import java.text.SimpleDateFormat;
import java.util.concurrent.ThreadLocalRandom;

import org.joda.time.DateTime;

/**
 * 随机生成订单编号
 * 
 * @author pitt
 *
 */
public class RandomUtil {

	private static final SimpleDateFormat dateForamtOne = new SimpleDateFormat("yyyyMMddHHmmssSSS");

	private static final ThreadLocalRandom random = ThreadLocalRandom.current();

	// 方式一
	public static String generateOrderCode() {
		// 时间戳+5位随机数
		return dateForamtOne.format(DateTime.now().toDate()) + generateNumber(5);
	}

	// 生成随机数
	public static String generateNumber(final int num) {
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i <= num; i++) {
			sb.append(random.nextInt(9));
		}
		return sb.toString();
	}

	// public static void main(String[] args) {
	// for (int i = 1; i < 10000; i++) {
	// System.out.println(generateOrderCode());
	// }
	// }

	// 进行md5加密
	// public static void main(String[] args) {
	// String salt="11299c42bf954c0abb373efbae3f6b26";
	// String password="123456";
	// System.out.println(new Md5Hash(password,salt));
	// }

}
