package com.acorn.cakeviewwidget;

/**
 * 转换工具
 * @author {acorn}
 *
 */
public class TransUtil {
	public static int obj2Int(Object o) {
		return ((Number) o).intValue();
	}

	public static float obj2Float(Object o) {
		return ((Number) o).floatValue();
	}

	/**
	 * 角度转弧度
	 * 
	 * @param angle
	 * @return
	 */
	public static double angle2radians(float angle) {
		return angle / 180f * Math.PI;
	}

	/**
	 * 弧度转角度
	 * 
	 * @param radians
	 * @return
	 */
	public static double radians2angle(double radians) {
		return 180f * radians / Math.PI;
	}
}
