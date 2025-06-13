/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vp;

import io.github.cdimascio.dotenv.Dotenv;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

public class DataSync {

    public static void main(String[] args) {
        // 加载环境变量
        Dotenv dotenv = Dotenv.configure()
                .filename(".env")
                .load();

        // 设置执行环境
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(1); // 设置并行度，可根据集群资源调整
        env.enableCheckpointing(3000); // 启用检查点，间隔 3 秒

        // 创建表执行环境
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        // 定义 MySQL CDC 源表 - 对应 Flink SQL 中的 mysql_file_source
        tableEnv.executeSql("CREATE TABLE mysql_file_source (" +
                "id BIGINT," +
                "belong_to CHAR (36)," +
                "file_type TINYINT," +
                "file_name VARCHAR (255)," +
                "file_uuid CHAR (36)," +
                "file_extension VARCHAR (10)," +
                "file_path VARCHAR (100)," +
                "file_size_bytes BIGINT," +
                "video_codec VARCHAR (20)," +
                "video_format VARCHAR (20)," +
                "video_bitrate BIGINT," +
                "video_duration_millis BIGINT," +
                "create_time TIMESTAMP (3)," +
                "last_used_time TIMESTAMP (3)," +
                "PRIMARY KEY (id) NOT ENFORCED" +
                ") WITH (" +
                "'connector' = 'mysql-cdc'," +
                "'hostname' = '" + dotenv.get("MYSQL_HOST") + "'," +
                "'port' = '" + dotenv.get("MYSQL_PORT") + "'," +
                "'username' = '" + dotenv.get("MYSQL_USERNAME") + "'," +
                "'password' = '" + dotenv.get("MYSQL_PASSWORD") + "'," +
                "'database-name' = '" + dotenv.get("MYSQL_DATABASE") + "'," +
                "'table-name' = '" + dotenv.get("MYSQL_TABLE") + "'," +
                "'debezium.snapshot.mode' = 'initial'," + // 初始全量同步
                "'debezium.connector.class' = 'io.debezium.connector.mysql.MySqlConnector'," +
                "'scan.incremental.snapshot.enabled' = 'true'" +
                ")");

        // 定义 Elasticsearch 汇表 - 对应 Flink SQL 中的 es_file_sink
        tableEnv.executeSql("CREATE TABLE es_file_sink (" +
                "id BIGINT," +
                "belong_to CHAR (36)," +
                "file_type TINYINT," +
                "file_name VARCHAR (255)," +
                "file_uuid CHAR (36)," +
                "file_extension VARCHAR (10)," +
                "file_path VARCHAR (100)," +
                "file_size_bytes BIGINT," +
                "video_codec VARCHAR (20)," +
                "video_format VARCHAR (20)," +
                "video_bitrate BIGINT," +
                "video_duration_millis BIGINT," +
                "create_time TIMESTAMP (3)," +
                "last_used_time TIMESTAMP (3)," +
                "PRIMARY KEY (id) NOT ENFORCED" +
                ") WITH (" +
                "'connector' = 'elasticsearch-7'," +
                "'hosts' = 'http://" + dotenv.get("ES_HOST") + ":" + dotenv.get("ES_PORT") + "'," +
                "'index' = '" + dotenv.get("ES_INDEX") + "'" +
                ")");

        // 执行数据同步 - 对应 Flink SQL 中的 INSERT SELECT
        tableEnv.executeSql("INSERT INTO es_file_sink SELECT * FROM mysql_file_source");
    }
}