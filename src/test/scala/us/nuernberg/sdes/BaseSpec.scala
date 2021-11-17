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

import org.scalatest.exceptions.TestFailedException
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.*
import org.scalacheck.{Arbitrary, Gen}

import org.scalactic.source
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

/** A base for test specifications. */
trait BaseSpec extends AnyFreeSpec
    with Matchers
    with ScalaCheckPropertyChecks

/**
 * Extension methods for an instance of [[Either]].
 *
 * @param e the either instance being extended
 * @tparam A the type on the left side of the given either
 * @tparam B the type on the right side of the given either
 */
extension[A, B] (e : Either[A, B])(using pos : source.Position)

/**
 * Returns the left side of the given either, or throws a [[TestFailedException]] if the given either is
 * right-sided.
 */
  def leftValue : A =
    e.fold(
      identity,
      _ => throw new TestFailedException(_ => Some("right-valued either does not have a left value"), None, pos)
    )

  /**
   * Returns the right side of the given either, or throws a [[TestFailedException]] if the given either is
   * left-sided.
   */
  def rightValue : B =
    e.fold(
      _ => throw new TestFailedException(_ => Some("right-valued either does not have a left value"), None, pos),
      identity
    )

/**
 * Extension methods for an instance of [[Option]].
 *
 * @param o the option instance being extended
 * @tparam A the type inside of the given option
 */
extension[A] (o : Option[A])(using pos : source.Position)

/**
 * Returns the inside of the given option, or throws a [[TestFailedException]] if the given option is empty.
 */
  def value : A =
    o.fold(throw new TestFailedException(_ => Some("empty option does not have a value"), None, pos))(identity)
