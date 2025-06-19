/*
 * Copyright (c) 2020-2022.
 * OOON.ME ALL RIGHTS RESERVED.
 * Licensed under the Mozilla Public License, version 2.0
 * Please visit <http://ooon.me> or mail to zhaihao@ooon.me
 */

package me.ooon.sparkia
package sql

/**
  * MaxMinBySpec
  *
  * @author zhaihao
  * @version 1.0
  * @since 2022/9/7 11:08
  */
//noinspection SqlNoDataSourceInspection
//language=SQL
class MaxMinBySpec extends SparkSpec {
  import spark.implicits._

  val df = Seq(
    ("A", "s1", 10),
    ("A", "s2", 20),
    ("A", "s3", 30),
    ("B", "s4", 40),
    ("B", "s5", 50),
    ("B", "s6", 60)
  ).toDF("class", "student", "score")
  df.show()
  df.createTempView("t_score")

  "max_by & min_by" in {
    val r = spark
      .sql("""select class,
             |       max_by(student,score) as maxStudent,
             |       max_by(score,score)   as maxByStudentScore,
             |       max(score)            as maxStudentScore,
             |       min_by(student,score) as minStudent,
             |       min_by(score,score)   as minByStudentScore,
             |       min(score)            as minStudentScore
             |from t_score
             |group by class""".stripMargin)

    r.show()
  }
}
