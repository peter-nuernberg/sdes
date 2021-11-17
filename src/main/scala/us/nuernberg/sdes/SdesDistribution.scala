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

import org.apache.commons.math3.distribution.{IntegerDistribution, RealDistribution}

/** A probability distribution. */
trait SdesDistribution[+A]:

  d =>

  /** A sample from this distribution. */
  def sample() : A

  /**
   * Returns a new distribution based on the given transform applied to this distribution.
   *
   * @param f the transform to apply
   * @tparam B the type returned by the given transform
   *
   * @return a new distribution based on the given transform applied to this distribution
   */
  def map[B](f : A => B) : SdesDistribution[B] = new SdesDistribution[B] :
    override def sample() : B = f(d.sample())

/** Factories and constants for [[SdesDistribution]]. */
object SdesDistribution:

  /**
   * Returns a new uniform distribution, optionally initialized with the given seed.
   *
   * Samples from the distribution are uniformly distributed in [0.0, 1.0).
   *
   * @param seed the seed (if any) to be used to initialize the underlying random number generator
   *
   * @return a new uniform distribution, optionally initialized with the given seed
   */
  def prUniform(seed : Option[Long]) : SdesDistribution[Double] = new SdesDistribution[Double] :

    private val rnd = scala.util.Random
    seed.foreach(rnd.setSeed)

    override def sample() : Double = rnd.nextDouble()

  /**
   * Returns a new normal distribution, optionally initialized with the given seed.
   *
   * Samples from the distribution are normally distributed around 0.0 with a standard deviation of 1.0.
   *
   * @param seed the seed (if any) to be used to initialize the underlying random number generator
   *
   * @return a new normal distribution, optionally initialized with the given seed
   */
  def prNormal(seed : Option[Long]) : SdesDistribution[Double] = new SdesDistribution[Double] :

    private val rnd = scala.util.Random
    seed.foreach(rnd.setSeed)

    override def sample() : Double = rnd.nextGaussian()

  /**
   * Returns a new distribution based on the given [[org.apache.commons.math3.distribution.RealDistribution]].
   *
   * @param rd the underlying Apache Commons Math distribution
   *
   * @return a new distribution based on the given [[org.apache.commons.math3.distribution.RealDistribution]]
   */
  def fromACM(rd : RealDistribution) : SdesDistribution[Double] = new SdesDistribution[Double] :

    override def sample() : Double = rd.sample()

  /**
   * Returns a new distribution based on the given [[org.apache.commons.math3.distribution.IntegerDistribution]].
   *
   * @param rd the underlying Apache Commons Math distribution
   *
   * @return a new distribution based on the given [[org.apache.commons.math3.distribution.IntegerDistribution]]
   */
  def fromACM(id : IntegerDistribution) : SdesDistribution[Int] = new SdesDistribution[Int] :

    override def sample() : Int = id.sample()
