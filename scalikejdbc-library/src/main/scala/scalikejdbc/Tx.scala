/*
 * Copyright 2011 Kazuhiro Sera
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package scalikejdbc

import java.sql.{ SQLException, Connection }
import scala.util.control.Exception._

/**
 * DB Transaction abstraction.
 * @param conn connection
 */
class Tx(val conn: Connection) {

  /**
   * Begins this transaction.
   */
  def begin(): Unit = conn.setAutoCommit(false)

  /**
   * Commits this transaction.
   */
  def commit(): Unit = {
    conn.commit()
    conn.setAutoCommit(true)
  }

  /**
   * Returns is this transaction active.
   * @return active
   */
  def isActive(): Boolean = !conn.getAutoCommit

  /**
   * Rolls this transaction back.
   */
  def rollback(): Unit = {
    conn.rollback()
    ignoring(classOf[SQLException]) apply {
      conn.setAutoCommit(true)
    }
  }

  /**
   * Rolls this transaction back if this transaction is still active.
   */
  def rollbackIfActive(): Unit = {
    ignoring(classOf[SQLException]) apply {
      conn.rollback()
    }
    ignoring(classOf[SQLException]) apply {
      conn.setAutoCommit(true)
    }
  }

}

