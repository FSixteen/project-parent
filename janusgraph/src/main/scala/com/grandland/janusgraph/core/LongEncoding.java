package com.grandland.janusgraph.core;

import com.google.common.base.Preconditions;

public class LongEncoding {
	
	public static void main(String[] args) {
		System.out.println(
				decode("jhsw")
				);
	}

	private static final String BASE_SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyz";

	public static long decode(String s) {
		return decode(s, BASE_SYMBOLS);
	}

	public static String encode(long num) {
		return encode(num, BASE_SYMBOLS);
	}

	public static long decode(String s, String symbols) {
		final int B = symbols.length();
		long num = 0;
		for (char ch : s.toCharArray()) {
			num *= B;
			int pos = symbols.indexOf(ch);
			if (pos < 0)
				throw new NumberFormatException("Symbol set does not match string");
			num += pos;
		}
		return num;
	}

	public static String encode(long num, String symbols) {
		Preconditions.checkArgument(num >= 0, "Expected non-negative number: " + num);
		final int B = symbols.length();
		StringBuilder sb = new StringBuilder();
		while (num != 0) {
			sb.append(symbols.charAt((int) (num % B)));
			num /= B;
		}
		return sb.reverse().toString();
	}

}
