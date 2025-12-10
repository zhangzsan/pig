package com.pig4cloud.pig.common.core.util;

import cn.hutool.core.util.ObjectUtil;
import com.pig4cloud.pig.common.core.constant.CommonConstants;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
public class RetOps<T> {

	/** 状态码为成功 */
	public static final Predicate<Result<?>> CODE_SUCCESS = r -> CommonConstants.SUCCESS == r.getCode();

	/** 数据有值 */
	public static final Predicate<Result<?>> HAS_DATA = r -> ObjectUtil.isNotEmpty(r.getData());

	/** 数据有值,并且包含元素 */
	public static final Predicate<Result<?>> HAS_ELEMENT = r -> ObjectUtil.isNotEmpty(r.getData());

	/** 状态码为成功并且有值 */
	public static final Predicate<Result<?>> DATA_AVAILABLE = CODE_SUCCESS.and(HAS_DATA);

	private final Result<T> original;

	RetOps(Result<T> original) {
		this.original = original;
	}

	public static <T> RetOps<T> of(Result<T> original) {
		return new RetOps<>(Objects.requireNonNull(original));
	}


	/**
	 * 观察原始值
	 */
	public Result<T> peek() {
		return original;
	}

	/**
	 */
	public int getCode() {
		return original.getCode();
	}

	/**
     * 获取data值
	 */
	public Optional<T> getData() {
		return Optional.ofNullable(original.getData());
	}

	/**
	 * @return 返回 Optional 包装的data,如果断言失败返回empty
	 */
	public Optional<T> getDataIf(Predicate<? super Result<?>> predicate) {
		return predicate.test(original) ? getData() : Optional.empty();
	}

	public Optional<String> getMsg() {
		return Optional.of(original.getMsg());
	}

	public boolean codeEquals(int value) {
		return original.getCode() == value;
	}


	public boolean codeNotEquals(int value) {
		return !codeEquals(value);
	}

	/**
	 * 是否成功
	 */
	public boolean isSuccess() {
		return codeEquals(CommonConstants.SUCCESS);
	}

	/**
	 * 是否失败
	 */
	public boolean notSuccess() {
		return !isSuccess();
	}

	// ~ 链式操作
	// ===================================================================================================

	/**
	 * 断言{@code code}的值
	 * @param expect 预期的值
	 * @param func 用户函数,负责创建异常对象
	 * @param <Ex> 异常类型
	 * @return 返回实例，以便于继续进行链式操作
	 */
	public <Ex extends Exception> RetOps<T> assertCode(int expect, Function<? super Result<T>, ? extends Ex> func)
			throws Ex {
		if (codeNotEquals(expect)) {
			throw func.apply(original);
		}
		return this;
	}

	/**
	 * 断言成功
	 * @param func 用户函数,负责创建异常对象
	 * @param <Ex> 异常类型
	 * @return 返回实例，以便于继续进行链式操作
	 * @throws Ex 断言失败时抛出
	 */
	public <Ex extends Exception> RetOps<T> assertSuccess(Function<? super Result<T>, ? extends Ex> func) throws Ex {
		return assertCode(CommonConstants.SUCCESS, func);
	}

	/**
	 * 断言业务数据有值
	 * @param func 用户函数,负责创建异常对象
	 * @param <Ex> 异常类型
	 * @return 返回实例，以便于继续进行链式操作
	 * @throws Ex 断言失败时抛出
	 */
	public <Ex extends Exception> RetOps<T> assertDataNotNull(Function<? super Result<T>, ? extends Ex> func) throws Ex {
		if (Objects.isNull(original.getData())) {
			throw func.apply(original);
		}
		return this;
	}

	/**
	 * 断言业务数据有值,并且包含元素
	 * @param func 用户函数,负责创建异常对象
	 * @param <Ex> 异常类型
	 * @return 返回实例，以便于继续进行链式操作
	 * @throws Ex 断言失败时抛出
	 */
	public <Ex extends Exception> RetOps<T> assertDataNotEmpty(Function<? super Result<T>, ? extends Ex> func) throws Ex {
		if (ObjectUtil.isNotEmpty(original.getData())) {
			throw func.apply(original);
		}
		return this;
	}

	/**
	 * 对业务数据(data)转换
	 * @param mapper 业务数据转换函数
	 * @param <U> 数据类型
	 * @return 返回新实例，以便于继续进行链式操作
	 */
	public <U> RetOps<U> map(Function<? super T, ? extends U> mapper) {
		Result<U> result = Result.restResult(mapper.apply(original.getData()), original.getCode(), original.getMsg());
		return of(result);
	}

	/**
	 * 对业务数据(data)转换
	 * @param predicate 断言函数
	 * @param mapper 业务数据转换函数
	 * @param <U> 数据类型
	 * @return 返回新实例，以便于继续进行链式操作
	 * @see RetOps#CODE_SUCCESS
	 * @see RetOps#HAS_DATA
	 * @see RetOps#HAS_ELEMENT
	 * @see RetOps#DATA_AVAILABLE
	 */
	public <U> RetOps<U> mapIf(Predicate<? super Result<T>> predicate, Function<? super T, ? extends U> mapper) {
		Result<U> result = Result.restResult(mapper.apply(original.getData()), original.getCode(), original.getMsg());
		return of(result);
	}

	// ~ 数据消费
	// ===================================================================================================

	/**
	 * 消费数据,注意此方法保证数据可用
	 * @param consumer 消费函数
	 */
	public void useData(Consumer<? super T> consumer) {
		consumer.accept(original.getData());
	}

	/**
	 * 条件消费(错误代码匹配某个值)
	 * @param consumer 消费函数
	 * @param codes 错误代码集合,匹配任意一个则调用消费函数
	 */
	public void useDataOnCode(Consumer<? super T> consumer, int... codes) {
		useDataIf(o -> Arrays.stream(codes).filter(c -> original.getCode() == c).findFirst().isPresent(), consumer);
	}

	/**
	 * 条件消费(错误代码表示成功)
	 * @param consumer 消费函数
	 */
	public void useDataIfSuccess(Consumer<? super T> consumer) {
		useDataIf(CODE_SUCCESS, consumer);
	}

	/**
	 * 条件消费
	 * @param predicate 断言函数
	 * @param consumer 消费函数,断言函数返回{@code true}时被调用
	 */
	public void useDataIf(Predicate<? super Result<T>> predicate, Consumer<? super T> consumer) {
		if (predicate.test(original)) {
			consumer.accept(original.getData());
		}
	}

}
