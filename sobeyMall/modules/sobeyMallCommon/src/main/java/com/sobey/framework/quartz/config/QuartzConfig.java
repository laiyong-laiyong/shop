package com.sobey.framework.quartz.config;

import org.apache.commons.lang3.StringUtils;
import org.quartz.spi.JobFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * 启用注解就开启定时任务功能
 */
@ConditionalOnProperty(prefix = "sobey-task", name = "enable", havingValue = "yes")
@Configuration
public class QuartzConfig {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    // 配置文件路径
    private static final String DEFAULT_QUARTZ_CONFIG = "/quartz.properties";
    private static final String QUARTZ_CONFIG = "/config/quartz.properties";

    private static final String ENV_PREFIX = "${";
    private static final String ENV_SUFFIX = "}";


    /**
     * 从quartz.properties文件中读取Quartz配置属性
     *
     * @return
     * @throws IOException
     */
    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propFactory = new PropertiesFactoryBean();

        ClassPathResource configResource = new ClassPathResource(QUARTZ_CONFIG);//config配置
        ClassPathResource defaultResource = new ClassPathResource(DEFAULT_QUARTZ_CONFIG);//默认配置

        //首先获取默认配置
        propFactory.setLocation(defaultResource);
        propFactory.afterPropertiesSet();
        Properties defaultProp = propFactory.getObject();
        //然后获取config目录配置
        Properties configProp = null;
        if (configResource.exists()) {
            propFactory.setLocation(configResource);
            propFactory.afterPropertiesSet();
            configProp = propFactory.getObject();
        }


        //处理环境变量
        if (null != defaultProp) {
            //如果config目录有配置则与默认配置合并(同名配置会被config配置覆盖)
            if (null != configProp && configProp.size() > 0) {
                defaultProp.putAll(configProp);
            }
            //获取系统环境变量并处理
            Map<String, String> env = System.getenv();
            for (Map.Entry<Object, Object> entry : defaultProp.entrySet()) {
                Object obj = entry.getValue();
                String val = "";
                if (null != obj && StringUtils.isNotBlank(val = obj.toString())) {
                    //判断是否有环境变量
                    while (true) {
                        if (val.contains(ENV_PREFIX) && val.contains(ENV_SUFFIX)) {
                            //逐个匹配
                            int start = val.indexOf(ENV_PREFIX);
                            int end = val.indexOf(ENV_SUFFIX);
                            String key = val.substring(start + 2, end);
                            String envVal = env.get(key.trim());
                            if (StringUtils.isNotBlank(envVal)) {
                                val = val.replace(ENV_PREFIX + key + ENV_SUFFIX, envVal);
                            }
                            continue;
                        }
                        defaultProp.put(entry.getKey(), val);
                        break;
                    }
                }
            }
        }
        return defaultProp;
    }


    /**
     * @param jobFactory 为SchedulerFactory配置JobFactory
     * @return
     * @throws IOException
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(JobFactory jobFactory) throws IOException {
        log.info("正在初始化quartz......");
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setJobFactory(jobFactory);
        factory.setOverwriteExistingJobs(true);
        factory.setAutoStartup(true); // 设置自行启动
        factory.setQuartzProperties(quartzProperties());
        /**
         *
         * 如果这里不设置为true，手动关闭服务时会报错：Unexpected runtime exception  JobStore is shutdown
         */
        factory.setWaitForJobsToCompleteOnShutdown(true);
        /**
         *
         * 使用应用的dataSource替换quartz的dataSource
         * 这里使用quartz自带的数据源配置
         */
//        factory.setDataSource(dataSource);
        return factory;
    }

}

