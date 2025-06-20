/*
 * Copyright (c) 2020-2025.
 * OOON.ME ALL RIGHTS RESERVED.
 * Licensed under the Mozilla Public License, version 2.0
 * Please visit <http://ooon.me> or mail to zhaihao@ooon.me
 */

package me.ooon.sparkia
package sql

import com.typesafe.scalalogging.StrictLogging
//noinspection SqlNoDataSourceInspection
//language=SQL
class CheckThrowExceptionSpec extends SparkSpec with StrictLogging {
  import spark.implicits._
  "test" in {
    val df = Seq(
      (1, 1),
      (2, 2)
    ).toDF("c1", "c2")
    df.show()
    df.createOrReplaceTempView("test")

    // 必须 show 才会报错

    // 高版本, 有内置函数
    spark.sql("select RAISE_ERROR('error!')").show()

    //  低版本
    spark.sql(
      """
        |
        |select assert_true((select count(1) as cnt from test) > 1) as check;
        |
        |""".stripMargin).show()

    spark.sql(
      """
        |
        |select assert_true((select count(1) as cnt from test) > 10) as check;
        |
        |""".stripMargin).show()
  }
}
