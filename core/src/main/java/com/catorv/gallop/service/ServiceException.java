package com.catorv.gallop.service;

/**
 * Service Exception
 * Created by cator on 8/15/16.
 */
@SuppressWarnings({
		"StaticInitializerReferencesSubClass",
		"ThrowableInstanceNeverThrown"
})
public abstract class ServiceException extends Exception {

	public static final ServiceException OK                 = new E(0, "OK");
	public static final ServiceException UNKNOWN            = new E(1, "未知错误");
	public static final ServiceException PERMISSION_DENIED  = new E(2, "您无权进行此操作");
	public static final ServiceException OPERATION_FAILED   = new E(3, "操作失败");
	public static final ServiceException ILLEGAL_PARAMETER  = new E(4, "非法参数");
	public static final ServiceException MISSING_PARAMETER  = new E(5, "缺少参数");
	public static final ServiceException BAD_DATA           = new E(6, "数据错误");
	public static final ServiceException ENTITY_NOT_FOUND   = new E(7, "未找到数据");
	public static final ServiceException DUPLICATE_ENTITY   = new E(8, "数据重复");
	public static final ServiceException AUTH_FAILED        = new E(9, "认证失败");
	public static final ServiceException INVALID_SESSION    = new E(10, "无效的Session");

	private int code = -1;

	public ServiceException() {
	}

	public ServiceException(int code, String message) {
		super(message);
		this.code = code;
	}

	public ServiceException(int code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public ServiceException(int code, Throwable cause) {
		super(cause);
		this.code = code;
	}

	public ServiceException(int code, String message, Throwable cause,
	                        boolean enableSuppression,
	                        boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	private static class E extends ServiceException {
		public E(int code, String message) {
			super(code, message);
		}
	}
}
