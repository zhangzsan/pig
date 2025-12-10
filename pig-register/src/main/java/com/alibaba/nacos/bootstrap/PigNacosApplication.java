package com.alibaba.nacos.bootstrap;

import static org.springframework.boot.context.logging.LoggingApplicationListener.CONFIG_PROPERTY;
import static org.springframework.core.io.ResourceLoader.CLASSPATH_URL_PREFIX;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import com.alibaba.nacos.NacosServerBasicApplication;
import com.alibaba.nacos.NacosServerWebApplication;
import com.alibaba.nacos.console.NacosConsole;
import com.alibaba.nacos.core.listener.startup.NacosStartUp;
import com.alibaba.nacos.core.listener.startup.NacosStartUpManager;
import com.alibaba.nacos.sys.env.Constants;
import com.alibaba.nacos.sys.env.DeploymentType;
import com.alibaba.nacos.sys.env.EnvUtil;

/**
   微服务应用启动
 */
public class PigNacosApplication {

    public static void main(String[] args) {
        String standaloneMode = "nacos.standalone";
        System.setProperty(standaloneMode, "true");
		System.setProperty(CONFIG_PROPERTY, CLASSPATH_URL_PREFIX + "logback-spring.xml");

		String type = System.getProperty(Constants.NACOS_DEPLOYMENT_TYPE, Constants.NACOS_DEPLOYMENT_TYPE_MERGED);
		DeploymentType deploymentType = DeploymentType.getType(type);
		EnvUtil.setDeploymentType(deploymentType);

		// Start Core Context
		NacosStartUpManager.start(NacosStartUp.CORE_START_UP_PHASE);
		ConfigurableApplicationContext coreContext = new SpringApplicationBuilder(NacosServerBasicApplication.class)
			.web(WebApplicationType.NONE)
			.run(args);

		// Start Server Web Context
		NacosStartUpManager.start(NacosStartUp.WEB_START_UP_PHASE);
		 new SpringApplicationBuilder(NacosServerWebApplication.class)
			.parent(coreContext)
			.run(args);

		// Start Console Context
		NacosStartUpManager.start(NacosStartUp.CONSOLE_START_UP_PHASE);
		new SpringApplicationBuilder(NacosConsole.class).parent(coreContext).run(args);
	}

}
