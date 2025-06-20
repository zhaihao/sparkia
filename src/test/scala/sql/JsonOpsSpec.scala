/*
 * Copyright (c) 2020-2022.
 * OOON.ME ALL RIGHTS RESERVED.
 * Licensed under the Mozilla Public License, version 2.0
 * Please visit <http://ooon.me> or mail to zhaihao@ooon.me
 */

package me.ooon.sparkia
package sql

import org.apache.spark.sql.types.StringType

/**
  * JsonOpsSpec
  *
  * @author zhaihao
  * @version 1.0
  * @since 2022/9/8 11:13
  */
//noinspection SqlNoDataSourceInspection
//language=SQL
class JsonOpsSpec extends SparkSpec {
  import spark.implicits._
  "从json中获取一个字段" in {
    val df = Seq(
      (1, """{"a":"1","b":2}"""),
      (2, """{"a":"3","b":4}""")
    ).toDF("c1", "c2")
    df.show()
    df.createOrReplaceTempView("test")
    val r = spark.sql("select c1,get_json_object(c2,'$.a') as f1, get_json_object(c2,'$.b') as f2 from test")
    r.show()
    // 全部当作 string
    r.schema("f1").dataType ==> StringType
    r.schema("f2").dataType ==> StringType
    r.string ==> """+---+---+---+
                   || c1| f1| f2|
                   |+---+---+---+
                   ||  1|  1|  2|
                   ||  2|  3|  4|
                   |+---+---+---+
                   |""".stripMargin
  }



}
