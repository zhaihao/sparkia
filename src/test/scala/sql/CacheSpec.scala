/*
 * Copyright (c) 2020-2025.
 * OOON.ME ALL RIGHTS RESERVED.
 * Licensed under the Mozilla Public License, version 2.0
 * Please visit <http://ooon.me> or mail to zhaihao@ooon.me
 */

package me.ooon.sparkia
package sql

import com.typesafe.scalalogging.StrictLogging
import org.apache.spark.storage.StorageLevel
//noinspection SqlNoDataSourceInspection
//language=SQL
class CacheSpec extends SparkSpec with StrictLogging {

  /**
   * 使用SQL cache 数据，并选择级别
   */
  "cache" in {
    spark.sql("""
                |create or replace temporary view t_result as
                |select 1 as id;
                |""".stripMargin)

    spark.sql("""
                |cache table t_result;
                |""".stripMargin)

    spark.catalog.isCached("t_result")   ==> true
    spark.table("t_result").storageLevel ==> StorageLevel(useDisk = true, useMemory = true, deserialized = true, 1)

    spark.sql("""
                |uncache table t_result;
                |""".stripMargin)

    spark.catalog.isCached("t_result") ==> false

    spark.sql("""
                |cache table t_result options ('storageLevel' = 'DISK_ONLY_2');
                |""".stripMargin)

    spark.catalog.isCached("t_result")   ==> true
    spark.table("t_result").storageLevel ==> StorageLevel(useDisk = true, useMemory = false, deserialized = false, 2)
  }

}
