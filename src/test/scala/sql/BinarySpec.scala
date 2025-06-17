/*
 * Copyright (c) 2020-2022.
 * OOON.ME ALL RIGHTS RESERVED.
 * Licensed under the Mozilla Public License, version 2.0
 * Please visit <http://ooon.me> or mail to zhaihao@ooon.me
 */

package me.ooon.sparkia
package sql

/**
  * BinarySpec
  *
  * @author zhaihao
  * @version 1.0
  * @since 2022/9/7 11:04
  */
class BinarySpec extends SparkSpec {

  /**
   * impala 不支持 binary 类型，建议做base64处理
   */
  "binary" in {
    // language=SQL
    spark
      .sql("""
             |select base64('hello'), base64('Hello')
             |""".stripMargin)
      .show()

    import spark.implicits._
    val df = Seq(
      "hello".getBytes,
      "Hello".getBytes,
      null
    ).toDF("a")
    df.show()
    df.createOrReplaceTempView("tt")

    // language=SQL
    val r = spark
      .sql("""
             |select base64(a) from tt
             |""".stripMargin)
    r.show()
  }
}
