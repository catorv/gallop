package com.catorv.gallop.util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by cator on 12/11/2016.
 */
public class CosineSimilarAlgorithm {
	public static double getSimilarity(String doc1, String doc2) {
		if (doc1 != null && doc1.trim().length() > 0 && doc2 != null
				&& doc2.trim().length() > 0) {

			Map<Integer, int[]> AlgorithmMap = new HashMap<>();

			//将两个字符串中的中文字符以及出现的总数封装到，AlgorithmMap中
			for (int i = 0; i < doc1.length(); i++) {
				char d1 = doc1.charAt(i);
				if (isValidChar(d1)) {
					int charIndex = getGB2312Id(d1);
					if (charIndex != -1) {
						int[] fq = AlgorithmMap.get(charIndex);
						if (fq != null && fq.length == 2) {
							fq[0]++;
						} else {
							fq = new int[2];
							fq[0] = 1;
							fq[1] = 0;
							AlgorithmMap.put(charIndex, fq);
						}
					}
				}
			}

			for (int i = 0; i < doc2.length(); i++) {
				char d2 = doc2.charAt(i);
				if (isValidChar(d2)) {
					int charIndex = getGB2312Id(d2);
					if (charIndex != -1) {
						int[] fq = AlgorithmMap.get(charIndex);
						if (fq != null && fq.length == 2) {
							fq[1]++;
						} else {
							fq = new int[2];
							fq[0] = 0;
							fq[1] = 1;
							AlgorithmMap.put(charIndex, fq);
						}
					}
				}
			}

			Iterator<Integer> iterator = AlgorithmMap.keySet().iterator();
			double sqdoc1 = 0;
			double sqdoc2 = 0;
			double denominator = 0;
			while (iterator.hasNext()) {
				int[] c = AlgorithmMap.get(iterator.next());
				denominator += c[0] * c[1];
				sqdoc1 += c[0] * c[0];
				sqdoc2 += c[1] * c[1];
			}

			return denominator / Math.sqrt(sqdoc1 * sqdoc2);
		} else {
			throw new NullPointerException(
					" the Document is null or have not cahrs!!");
		}
	}

	private static boolean isValidChar(char ch) {
		return ch >= 0x4E00 && ch <= 0x9FA5 ||
				ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z' ||
				ch >= '0' && ch <= '9';
	}

	/**
	 * 根据输入的Unicode字符，获取它的GB2312编码或者ascii编码，
	 *
	 * @param ch 输入的GB2312中文字符或者ASCII字符(128个)
	 * @return ch在GB2312中的位置，-1表示该字符不认识
	 */
	private static int getGB2312Id(char ch) {
		try {
			if (ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z' || ch >= '0' && ch <= '9') {
				return ch;
			}
			byte[] buffer = Character.toString(ch).getBytes("GB2312");
			if (buffer.length != 2) {
				// 正常情况下buffer应该是两个字节，否则说明ch不属于GB2312编码，故返回'?'，此时说明不认识该字符
				return -1;
			}
			return ((buffer[0] & 0x0FF) << 8) | buffer[1];
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return -1;
	}
}
