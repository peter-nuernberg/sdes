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

import java.time.{Duration, LocalDateTime}

/** Extensions to [[java.time.LocalDateTime]]. */
extension (dt : LocalDateTime)

  /**
   * Returns the date/time that results from adding the given duration to this date/time.
   *
   * @param d the duration to add
   *
   * @return the date/time that results from adding the given duration to this date/time
   */
  def ++(d : Duration) : LocalDateTime = dt.plus(d)

/** Extensions to [[java.time.Duration]]. */
extension (d : Duration)

  /**
   * Returns the sum of this duration and the given duration.
   *
   * @param other the duration to add
   *
   * @return the sum of this duration and the given duration
   */
  def +(other : Duration) : Duration = d.plus(other)
