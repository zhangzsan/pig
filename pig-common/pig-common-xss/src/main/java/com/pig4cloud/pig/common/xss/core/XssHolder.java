package com.pig4cloud.pig.common.xss.core;

/**
 * 利用 ThreadLocal 缓存线程间的数据
 */
public class XssHolder {

	private static final ThreadLocal<Boolean> TL = new ThreadLocal<>();

	private static final ThreadLocal<XssCleanIgnore> TL_IGNORE = new ThreadLocal<>();

	/**
	 * 是否开启
	 */
	public static boolean isEnabled() {
		return Boolean.TRUE.equals(TL.get());
	}

	/**
	 * 标记为开启
	 */
	static void setEnable() {
		TL.set(Boolean.TRUE);
	}

	/**
	 * 保存接口上的 XssCleanIgnore
	 */
	public static void setXssCleanIgnore(XssCleanIgnore xssCleanIgnore) {
		TL_IGNORE.set(xssCleanIgnore);
	}

	/**
	 * 获取接口上的 XssCleanIgnore
	 */
	public static XssCleanIgnore getXssCleanIgnore() {
		return TL_IGNORE.get();
	}

	/**
	 * 关闭 xss 清理
	 */
	public static void remove() {
		TL.remove();
		TL_IGNORE.remove();
	}

}
