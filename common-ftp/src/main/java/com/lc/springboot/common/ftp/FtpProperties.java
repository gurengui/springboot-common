package com.lc.springboot.common.ftp;

import cn.hutool.extra.ftp.FtpConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**

 * @description: ftp配置
 * @author: liangc
 * @date: 2020-08-06 15:07
 * @version 1.0
 **/
@Setter
@Getter
@ConfigurationProperties(prefix = "ftp")
public class FtpProperties extends FtpConfig {

    /**
     * 文件存放目录
     */
    private String dirPath;


}
