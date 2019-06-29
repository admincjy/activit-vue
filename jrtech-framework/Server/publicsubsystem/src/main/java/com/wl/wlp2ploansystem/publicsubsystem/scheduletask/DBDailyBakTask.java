package com.wl.wlp2ploansystem.publicsubsystem.scheduletask;

import com.alibaba.druid.pool.DruidDataSource;
import com.wl.wlp2ploansystem.infrastructures.common.ApplicationProperties;
import com.wl.wlp2ploansystem.infrastructures.common.support.DBHelper;
import com.wl.wlp2ploansystem.infrastructures.common.support.IdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;

@Component("DBDailyBakTask")
public class DBDailyBakTask {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    DruidDataSource dataSource;

    public void execute() throws IOException,InterruptedException,SQLException{
        logger.info("开始数据库备份");

        String user =  dataSource.getUsername();
        String password =dataSource.getPassword();
        String dbName = dataSource.getConnection().getCatalog();
        String jdbcUrl =dataSource.getRawJdbcUrl();
        String[] hostPortArray = jdbcUrl.split("/")[2].split(":");
        String host = hostPortArray[0];
        String port = hostPortArray[1];
        String dbBakName = applicationProperties.getDbbak().getFilePath();
        String fileName = IdGenerator.get()+".sql";

        DBHelper.bak(applicationProperties.getDbbak().getScript(),host,port,user,password,dbBakName,fileName,dbName);
        logger.info("完成数据库备份");
    }
}
