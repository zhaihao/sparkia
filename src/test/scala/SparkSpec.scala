/*
 * Copyright (c) 2020-2022.
 * OOON.ME ALL RIGHTS RESERVED.
 * Licensed under the Mozilla Public License, version 2.0
 * Please visit <http://ooon.me> or mail to zhaihao@ooon.me
 */

package me.ooon.sparkia

import com.typesafe.scalalogging.StrictLogging
import org.apache.spark.sql.SparkSession
import org.scalatest.BeforeAndAfterAll
import test.BaseSpec

/**
  * SparkSpec
  *
  * @author zhaihao
  * @version 1.0
  * @since 2022/9/6 17:34
  */
trait SparkSpec extends BaseSpec with StrictLogging with BeforeAndAfterAll {

  lazy val enableDelta = false

  lazy val spark: SparkSession = {
    val build = SparkSession.builder()
    if (enableDelta) {
      build
        .config("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension")
        .config("spark.sql.catalog.spark_catalog", "org.apache.spark.sql.delta.catalog.DeltaCatalog")
    }
    build
      .config("spark.sql.shuffle.partitions", 20)
      .config("spark.default.parallelism", 20)
      .appName("spark application")
      .master("local[*]") // 如果无法bind local，使用 `hostname` ，在`/etc/hosts`中将 hostname 配置到127.0.0.1
      .getOrCreate()
  }
}
