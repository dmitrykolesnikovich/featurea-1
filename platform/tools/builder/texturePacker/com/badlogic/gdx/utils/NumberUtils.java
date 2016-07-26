package com.badlogic.gdx.utils;
public class NumberUtils {
	public static int floatToIntBits (float value) {
		return Float.floatToIntBits(value);
	}
	public static int floatToRawIntBits (float value) {
		return Float.floatToRawIntBits(value);
	}
	public static int floatToIntColor (float value) {
		return Float.floatToRawIntBits(value);
	}
	public static float intToFloatColor (int value) {
		return Float.intBitsToFloat(value & 0xfeffffff);
	}
	public static float intBitsToFloat (int value) {
		return Float.intBitsToFloat(value);
	}
	public static long doubleToLongBits (double value) {
		return Double.doubleToLongBits(value);
	}
	public static double longBitsToDouble (long value) {
		return Double.longBitsToDouble(value);
	}
}