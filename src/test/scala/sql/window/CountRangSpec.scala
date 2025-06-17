/*
 * Copyright (c) 2020-2025.
 * OOON.ME ALL RIGHTS RESERVED.
 * Licensed under the Mozilla Public License, version 2.0
 * Please visit <http://ooon.me> or mail to zhaihao@ooon.me
 */

package me.ooon.sparkia
package sql.window

//noinspection SqlNoDataSourceInspection
//language=SQL
class CountRangSpec extends SparkSpec {

  /**
   * 计算同一IP过去1小时注册的量
   * order by 的字段必须是时间字段
   */

  "test" in {
    spark.sql("""
                |create or replace temporary view t_test as
                |select 1 as uuid,'192.168.1.1' as ip,cast('2025-06-17 10:00:00' as timestamp) as create_time union all
                |select 2 as uuid,'192.168.1.2' as ip,cast('2025-06-17 10:15:00' as timestamp) as create_time union all
                |select 3 as uuid,'192.168.1.1' as ip,cast('2025-06-17 10:30:00' as timestamp) as create_time union all
                |select 4 as uuid,'192.168.1.1' as ip,cast('2025-06-17 11:15:00' as timestamp) as create_time
                |
                |""".stripMargin)

    spark.sql("select * from t_test").show()

    spark
      .sql("""
             |SELECT
             |    uuid,
             |    ip,
             |    create_time,
             |    COUNT(*) OVER (
             |        PARTITION BY ip
             |        ORDER BY create_time
             |        RANGE BETWEEN INTERVAL 1 HOUR PRECEDING AND CURRENT ROW
             |    ) AS register_count_last_hour
             |FROM t_test order by uuid;
             |
             |""".stripMargin)
      .show()
  }

}
