package com.catorv.gallop.service;

import com.google.common.base.Preconditions;

import javax.annotation.Nullable;

/**
 * Service Exception
 * Created by cator on 8/15/16.
 */
@SuppressWarnings({
		"StaticInitializerReferencesSubClass",
		"ThrowableInstanceNeverThrown"
})
public abstract class ServiceException extends Exception {

	public static final ServiceException OK = new E(0, "OK");

	public static final ServiceException UNKNOWN = new E(1000, "未知错误");
	public static final ServiceException PERMISSION_DENIED = new E(1001, "权限禁止");
	public static final ServiceException OPERATION_FAILED = new E(1002, "操作失败");
	public static final ServiceException OPERATION_TIMEOUT = new E(1003, "操作超时");

	public static final ServiceException ILLEGAL_PARAMETER = new E(1010, "非法参数");
	public static final ServiceException MISSING_PARAMETER = new E(1011, "缺少参数");

	public static final ServiceException BAD_DATA = new E(1020, "数据错误");
	public static final ServiceException MISSING_DATA = new E(1021, "数据错误");
	public static final ServiceException ENTITY_NOT_FOUND = new E(1022, "未找到数据");
	public static final ServiceException DUPLICATE_ENTITY = new E(1023, "数据重复");

	public static final ServiceException AUTH_FAILED = new E(1090, "认证失败");
	public static final ServiceException INVALID_SESSION = new E(1091, "无效的Session");
	public static final ServiceException INVALID_VERIFY_CODE = new E(1092, "无效的验证码");

	private int code = 1000;

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

	public ServiceException withMessage(String message) {
		return new E(code, message);
	}

	public ServiceException withCause(Throwable cause) {
		return new E(code, getMessage(), cause);
	}

	public static void throwException(Exception e, @Nullable CodeMessagePair... pairs)
			throws ServiceException {
		if (e instanceof ServiceException) {
			throwWithMessage((ServiceException) e, pairs);
		}

		if (e instanceof javax.persistence.EntityNotFoundException ||
				e instanceof javax.persistence.NoResultException) {
			throwWithMessage(ENTITY_NOT_FOUND.withCause(e), pairs);
		}

		if (e instanceof javax.persistence.EntityExistsException) {
			throwWithMessage(DUPLICATE_ENTITY.withCause(e), pairs);
		}

		if (e instanceof javax.persistence.LockTimeoutException ||
				e instanceof javax.persistence.QueryTimeoutException) {
			throwWithMessage(OPERATION_TIMEOUT.withCause(e), pairs);
		}

		throwWithMessage(OPERATION_FAILED.withCause(e), pairs);
	}

	private static void throwWithMessage(ServiceException exception,
	                                     @Nullable CodeMessagePair[] pairs)
			throws ServiceException {

		if (pairs != null) {
			for (CodeMessagePair pair : pairs) {
				if (pair != null && exception.getCode() == pair.code) {
					throw exception.withMessage(String.valueOf(pair.message));
				}
			}
		}

		throw exception;
	}

	private static class E extends ServiceException {

		public E(int code, String message) {
			super(code, message);
		}

		public E(int code, String message, Throwable cause) {
			super(code, message, cause);
		}

	}

	public static class CodeMessagePair {

		public final int code;
		public final Object message;

		public CodeMessagePair(int code, Object message) {
			Preconditions.checkNotNull(message, "message is null");
			this.code = code;
			this.message = message;
		}

		public CodeMessagePair(ServiceException exception, Object message) {
			this(exception.getCode(), message);
		}

	}

}
