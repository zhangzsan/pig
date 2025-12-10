package com.pig4cloud.pig.common.feign.sentinel.ext;

import com.alibaba.cloud.sentinel.feign.SentinelContractHolder;
import feign.Contract;
import feign.Feign;
import feign.InvocationHandlerFactory;
import feign.Target;
import org.springframework.beans.BeansException;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 支持自动降级注入的Feign构建器，重写 {@link com.alibaba.cloud.sentinel.feign.SentinelFeign}
 *
 */
public final class PigSentinelFeign {

	private PigSentinelFeign() {

	}

	public static PigSentinelFeign.Builder builder() {
		return new PigSentinelFeign.Builder();
	}

	public static final class Builder extends Feign.Builder implements ApplicationContextAware {

		private Contract pigContract = new Contract.Default();

        private FeignClientFactory feignClientFactory;

		@Override
		public Feign.Builder invocationHandlerFactory(InvocationHandlerFactory invocationHandlerFactory) {
			throw new UnsupportedOperationException();
		}

		@Override
		public PigSentinelFeign.Builder contract(Contract contract) {
			this.pigContract = contract;
			return this;
		}

		@Override
		public Feign internalBuild() {
			super.invocationHandlerFactory(new InvocationHandlerFactory() {
				@Override
				public InvocationHandler create(Target target, Map<Method, MethodHandler> dispatch) {

					// 查找 FeignClient 上的 降级策略
					FeignClient feignClient = AnnotationUtils.findAnnotation(target.type(), FeignClient.class);
                    assert feignClient != null;
                    Class<?> fallback = feignClient.fallback();
					Class<?> fallbackFactory = feignClient.fallbackFactory();

					String beanName = feignClient.contextId();
					if (!StringUtils.hasText(beanName)) {
						beanName = feignClient.name();
					}

					Object fallbackInstance;
					FallbackFactory<?> fallbackFactoryInstance;
					if (void.class != fallback) {
						fallbackInstance = getFromContext(beanName, "fallback", fallback, target.type());
						return new PigSentinelInvocationHandler(target, dispatch,
								new FallbackFactory.Default<>(fallbackInstance));
					}

					if (void.class != fallbackFactory) {
						fallbackFactoryInstance = (FallbackFactory<?>) getFromContext(beanName, "fallbackFactory",
								fallbackFactory, FallbackFactory.class);
						return new PigSentinelInvocationHandler(target, dispatch, fallbackFactoryInstance);
					}
					return new PigSentinelInvocationHandler(target, dispatch);
				}

				private Object getFromContext(String name, String type, Class<?> fallbackType, Class<?> targetType) {
					Object fallbackInstance = feignClientFactory.getInstance(name, fallbackType);
					if (fallbackInstance == null) {
						throw new IllegalStateException(String
							.format("No %s instance of type %s found for feign client %s", type, fallbackType, name));
					}

					if (!targetType.isAssignableFrom(fallbackType)) {
						throw new IllegalStateException(String.format(
								"Incompatible %s instance. Fallback/fallbackFactory of type %s is not assignable to %s for feign client %s",
								type, fallbackType, targetType, name));
					}
					return fallbackInstance;
				}
			});

			super.contract(new SentinelContractHolder(pigContract));
			return super.internalBuild();
		}


		@Override
		public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.feignClientFactory = applicationContext.getBean(FeignClientFactory.class);
		}

	}

}
