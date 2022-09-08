/*
 * Copyright (c) 2020-2022.
 * OOON.ME ALL RIGHTS RESERVED.
 * Licensed under the Mozilla Public License, version 2.0
 * Please visit <http://ooon.me> or mail to zhaihao@ooon.me
 */

package org.apache.spark.sql

import scala.language.implicitConversions

/**
  * DatasetOps
  *
  * @author zhaihao
  * @version 1.0
  * @since 2022/9/7 11:28
  */
final class DatasetOps[T](private val ds: Dataset[T]) extends AnyVal {
  def string = ds.showString(20)
}
