package com.recommend.config;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * HBase配置类
 */
@org.springframework.context.annotation.Configuration
@Slf4j
@Profile("!test")
public class HBaseConfig {

    @Value("${hbase.zookeeper.quorum:localhost}")
    private String zookeeperQuorum;

    @Value("${hbase.zookeeper.property.clientPort:2181}")
    private String zookeeperPort;

    @Value("${hbase.master:localhost:16000}")
    private String hbaseMaster;

    @Bean
    public Configuration hbaseConfiguration() {
        Configuration config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum", zookeeperQuorum);
        config.set("hbase.zookeeper.property.clientPort", zookeeperPort);
        config.set("hbase.master", hbaseMaster);
        config.set("hbase.client.retries.number", "3");
        config.set("hbase.client.operation.timeout", "30000");
        config.set("hbase.client.scanner.timeout.period", "60000");
        log.info("HBase配置初始化完成: {}", zookeeperQuorum);
        return config;
    }

    @Bean
    public Connection hbaseConnection() throws IOException {
        try {
            Connection connection = ConnectionFactory.createConnection(hbaseConfiguration());
            log.info("HBase连接创建成功");
            return connection;
        } catch (IOException e) {
            log.error("HBase连接创建失败", e);
            throw e;
        }
    }
} 