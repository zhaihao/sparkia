/*
 * Copyright (c) 2020-2022.
 * OOON.ME ALL RIGHTS RESERVED.
 * Licensed under the Mozilla Public License, version 2.0
 * Please visit <http://ooon.me> or mail to zhaihao@ooon.me
 */

package me.ooon

import org.apache.spark.sql.{Dataset, DatasetOps}

import scala.language.implicitConversions

/**
  * package
  *
  * @author zhaihao
  * @version 1.0
  * @since 2022/9/7 11:32
  */
package object sparkia {
  implicit def toDatasetOps[T](ds: Dataset[T]) = new DatasetOps(ds)
}
