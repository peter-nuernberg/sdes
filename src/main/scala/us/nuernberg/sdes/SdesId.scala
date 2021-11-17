/*
 * Scala Discrete Event Simulator
 *
 * Copyright 2021 Peter J. Nuernberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package us.nuernberg.sdes

import java.util.concurrent.atomic.AtomicLong

/**
 * An identifier.
 *
 * @param value the underlying value of this identifier
 */
final case class SdesId private (value : Long):

  override val toString : String = value.toString

/** Factories and constants for [[SdesId]]. */
object SdesId:

  /** An long with atomic access controls that holds the value of the next identifier to be issued. */
  private val nextId : AtomicLong = new AtomicLong(0)

  /**
   * Returns a new identifier.
   *
   * @return a new identifier
   */
  def next() : SdesId = SdesId(nextId.getAndIncrement())
