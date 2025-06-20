/*
 * Copyright (c) 2020-2025.
 * OOON.ME ALL RIGHTS RESERVED.
 * Licensed under the Mozilla Public License, version 2.0
 * Please visit <http://ooon.me> or mail to zhaihao@ooon.me
 */

package me.ooon.sparkia
package sql

import com.typesafe.scalalogging.StrictLogging
import org.apache.spark.{SparkException, SparkThrowable}
//noinspection SqlNoDataSourceInspection
//language=SQL
class CheckThrowExceptionSpec extends SparkSpec with StrictLogging {
  import spark.implicits._

  val df = Seq(
    (1, 1),
    (2, 2)
  ).toDF("c1", "c2")
  df.show()
  df.createOrReplaceTempView("t_test")

  // 必须 show 才会报错

  "高版本内置函数" in {
    assertThrows[SparkThrowable] {
      spark.sql("select RAISE_ERROR('error!')").show()
    }

  }

  // 可以自定义一个 udf RAISE_ERROR，或者使用 assert_true
  "低版本" in {
    spark
      .sql("""
             |select assert_true((select count(1) as cnt from t_test) > 1) as check;
             |
             |""".stripMargin)
      .show()

    assertThrows[SparkThrowable] {
      spark
        .sql("""
               |
               |select assert_true((select count(1) as cnt from t_test) > 10) as check;
               |
               |""".stripMargin)
        .show()
    }
  }
}
