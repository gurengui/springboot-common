/*
 * Copyright (c) 2015-2020, www.dibo.ltd (service@dibo.ltd).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.lc.springboot.common.utils;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.toolkit.JdbcUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Spring上下文帮助类
 * @author mazc@dibo.ltd
 * @version 2.0
 * @date 2019/01/01
 */
@Component
@Lazy(false)
public class ContextHelper implements ApplicationContextAware {
    private static final Logger log = LoggerFactory.getLogger(ContextHelper.class);

    /***
     * ApplicationContext上下文
     */
    private static ApplicationContext APPLICATION_CONTEXT = null;

    /**
     * 数据库类型
     */
    private static String DATABASE_TYPE = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        APPLICATION_CONTEXT = applicationContext;
    }

    /***
     * 获取ApplicationContext上下文
     */
    public static ApplicationContext getApplicationContext() {
        if (APPLICATION_CONTEXT == null){
            APPLICATION_CONTEXT = ContextLoader.getCurrentWebApplicationContext();
        }
        if(APPLICATION_CONTEXT == null){
            log.warn("无法获取ApplicationContext，请在Spring初始化之后调用!");
        }
        return APPLICATION_CONTEXT;
    }

    /***
     * 根据beanId获取Bean实例
     * @param beanId
     * @return
     */
    public static Object getBean(String beanId){
        return getApplicationContext().getBean(beanId);
    }

    /***
     * 获取指定类型的单个Bean实例
     * @param clazz
     * @return
     */
    public static <T> T getBean(Class<T> clazz){
        try{
            return getApplicationContext().getBean(clazz);
        }
        catch (Exception e){
            log.debug("无法找到 bean: {}", clazz.getSimpleName());
            return null;
        }
    }

    /***
     * 获取指定类型的全部实现类
     * @param type
     * @param <T>
     * @return
     */
    public static <T> List<T> getBeans(Class<T> type){
        Map<String, T> map = getApplicationContext().getBeansOfType(type);
        if(ObjectUtil.isEmpty(map)){
            return null;
        }
        List<T> beanList = new ArrayList<>();
        beanList.addAll(map.values());
        return beanList;
    }

    /***
     * 根据注解获取beans
     * @param annotationType
     * @return
     */
    public static List<Object> getBeansByAnnotation(Class<? extends Annotation> annotationType){
        Map<String, Object> map = getApplicationContext().getBeansWithAnnotation(annotationType);
        if(ObjectUtil.isEmpty(map)){
            return null;
        }
        List<Object> beanList = new ArrayList<>();
        beanList.addAll(map.values());
        return beanList;
    }

    /***
     * 获取JdbcUrl
     * @return
     */
    public static String getJdbcUrl() {
        Environment environment = getApplicationContext().getEnvironment();
        String jdbcUrl = environment.getProperty("spring.datasource.url");
        if(jdbcUrl == null){
            jdbcUrl = environment.getProperty("spring.datasource.druid.url");
        }
        if(jdbcUrl == null){
            String master = environment.getProperty("spring.datasource.dynamic.primary");
            jdbcUrl = environment.getProperty("spring.datasource.dynamic.datasource."+master+".url");
        }
        return jdbcUrl;
    }

    /**
     * 获取数据库类型
     * @return
     */
    public static String getDatabaseType(){
        if(DATABASE_TYPE != null){
            return DATABASE_TYPE;
        }
        String jdbcUrl = getJdbcUrl();
        if(jdbcUrl != null){
            DbType dbType = JdbcUtils.getDbType(jdbcUrl);
            DATABASE_TYPE = dbType.getDb();
            if(DATABASE_TYPE.startsWith(DbType.SQL_SERVER.getDb())){
                DATABASE_TYPE = DbType.SQL_SERVER.getDb();
            }
        }
        else{
            SqlSessionFactory sqlSessionFactory = getBean(SqlSessionFactory.class);
            if(sqlSessionFactory != null){
                DATABASE_TYPE = sqlSessionFactory.getConfiguration().getDatabaseId();
            }
        }
        if(DATABASE_TYPE == null){
            log.warn("无法识别数据库类型，请检查数据源配置:spring.datasource.url等");
        }
        return DATABASE_TYPE;
    }

}