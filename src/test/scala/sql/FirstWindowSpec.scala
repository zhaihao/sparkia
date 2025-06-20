/*
 * Copyright (c) 2020-2022.
 * OOON.ME ALL RIGHTS RESERVED.
 * Licensed under the Mozilla Public License, version 2.0
 * Please visit <http://ooon.me> or mail to zhaihao@ooon.me
 */

package me.ooon.sparkia
package sql

/**
  * =FirstWindowSpec=
  * '''first 默认的含义是直接取第一个，可以通过排序某个字段，取另一个字段的第一个值，需要使用
  * first(FIRST(score) OVER ( PARTITION BY ID,ts ORDER BY `TIME` ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING ))'''
  *
  * @author zhaihao
  * @version 1.0
  * @since 2022/9/6 17:36
  */
//noinspection SqlNoDataSourceInspection
//language=SQL
class FirstWindowSpec extends SparkSpec {

  import org.apache.spark.sql.AnalysisException
  import spark.implicits._
  Seq(
    ("2020-05-06 23:10:01", "2020-05-06 23:10:00", "1", 23),
    ("2020-05-06 23:10:02", "2020-05-06 23:10:00", "1", 33),
    ("2020-05-06 23:10:03", "2020-05-06 23:10:00", "1", 43),
    ("2020-05-06 23:10:01", "2020-05-06 23:10:00", "2", 12),
    ("2020-05-06 23:10:02", "2020-05-06 23:10:00", "2", 22),
    ("2020-05-06 23:10:03", "2020-05-06 23:10:00", "2", 32),
    ("2020-05-06 23:11:01", "2020-05-06 23:11:00", "1", 231),
    ("2020-05-06 23:11:02", "2020-05-06 23:11:00", "1", 331),
    ("2020-05-06 23:11:03", "2020-05-06 23:11:00", "1", 431),
    ("2020-05-06 23:11:01", "2020-05-06 23:11:00", "2", 121),
    ("2020-05-06 23:11:02", "2020-05-06 23:11:00", "2", 221),
    ("2020-05-06 23:11:03", "2020-05-06 23:11:00", "2", 321)
  ).toDF("time", "ts", "id", "score").createTempView("test")

  /**
   * 只能用子查询
   */
  "无法实现 first 与 group 一起用" in {
    assertThrows[AnalysisException] {
      spark
        .sql("""
               |SELECT
               | first(first(`TIME`) OVER ( PARTITION BY ID,ts ORDER BY `TIME` ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING ))   AS first_time,
               | ts,
               | id,
               | first(FIRST(`score`) OVER ( PARTITION BY ID,ts ORDER BY `TIME` ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING ))   AS first_score
               |FROM test
               | GROUP BY ts,id
               |""".stripMargin)
        .show()
    }

    assertThrows[AnalysisException] {
      spark
        .sql("""
               |select
               | FIRST(TIME) OVER ( PARTITION BY ID,ts ORDER BY `TIME` ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING )  AS first_time,
               | ts,
               | id,
               | FIRST(score) OVER ( PARTITION BY ID,ts ORDER BY `TIME` ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING )   AS first_score
               |from test
               | group by ts,id,first_time,first_score
               |""".stripMargin)
        .show()
    }
  }

  "子查询实现" in {
    val r = spark.sql(
      """
        |SELECT
        | t.ts,
        | t.id,
        | first(t.first_time),
        | first(t.first_score)
        |FROM
        |   (SELECT
        |     first(`TIME`) OVER ( PARTITION BY ID,ts ORDER BY `TIME` ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING ) AS first_time,
        |     ts,
        |     id,
        |     first(score) OVER ( PARTITION BY ID,ts ORDER BY `TIME` ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING ) AS first_score
        |   FROM test) t
        |GROUP BY t.ts, t.id
        |""".stripMargin
    )
    r.show()
    r.string ==> """|+-------------------+---+-------------------+------------------+
                    ||                 ts| id|  first(first_time)|first(first_score)|
                    |+-------------------+---+-------------------+------------------+
                    ||2020-05-06 23:10:00|  1|2020-05-06 23:10:01|                23|
                    ||2020-05-06 23:10:00|  2|2020-05-06 23:10:01|                12|
                    ||2020-05-06 23:11:00|  1|2020-05-06 23:11:01|               231|
                    ||2020-05-06 23:11:00|  2|2020-05-06 23:11:01|               121|
                    |+-------------------+---+-------------------+------------------+
                    |""".stripMargin
  }
}
