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
package examples.montyhall

import org.apache.commons.math3.distribution.*

import java.time.Duration
import scala.math

/**
 * A game scenario.
 *
 * @param id                 the identifier of this scenario
 * @param playerDecisiveness the decisiveness of the player in this scenario
 * @param playerStubbornness the stubbornness of the player in this scenario
 */
final case class GameScenario(
    override val id    : SdesId,
    playerDecisiveness : PlayerDecisiveness,
    playerStubbornness : PlayerStubbornness
) extends SdesScenario :

  /** The number of doors in a game in this scenario. */
  val numDoors : Int = 3

  /** The number of doors to remain closed when Monty offers the player an opportunity to switch choices. */
  val numClosedAtOffer : Int = numDoors - 1

  /**
   * Returns a record of the result of a game played in this scenario.
   *
   * @param win a flag indicating whther the player won the game
   *
   * @return a record of the result of a game played in this scenario
   */
  def record(win : Boolean) : String =
    s"""$playerDecisiveness,$playerStubbornness,${ if win then "1" else "0" }"""


/** Factories and constants for [[GameScenario]]. */
object GameScenario :

  /** All possible game scenarios. */
  val all : List[GameScenario] =
    for
      d <- PlayerDecisiveness.values.toList
      s <- PlayerStubbornness.values.toList
    yield new GameScenario(SdesId.next(), d, s)

/**
 * An enumeration of the possible degress of player decisiveness.
 *
 * @param dist a distribution of initial choice delay durations
 */
enum PlayerDecisiveness(dist : SdesDistribution[Duration]):

  /**
   * Returns a delay for a player's initial choice.
   *
   * The value returned by this method is sampled from its underlying distribution.
   *
   * @return a delay for a player's initial choice
   */
  def delay() : Duration = dist.sample()

  /** A decisive player (i.e., one that does not delay a long time before providing an initial choice.) */
  case Decisive extends PlayerDecisiveness(
    SdesDistribution.fromACM(new WeibullDistribution(0.5, 2))
        .map(d => Duration.ofSeconds(math.round(d)))
  )

  /** An indecisive player (i.e., one that delays a long time before providing an initial choice.) */
  case Indecisive extends PlayerDecisiveness(
    SdesDistribution.fromACM(new WeibullDistribution(5.0, 100))
        .map(d => Duration.ofSeconds(math.round(d)))
  )

/**
 * An enumeration of the possible degress of player stubbornness.
 *
 * @param dist a distribution of switch choices
 */
enum PlayerStubbornness(dist : SdesDistribution[Boolean]):

  /**
   * Returns an indication of a player's decision to switch choices.
   *
   * The value returned by this method is sampled from its underlying distribution.
   *
   * @return an indication of a player's decision to switch choices
   */
  def willSwitch() : Boolean = dist.sample()

  /** A fickle player (i.e., one that tends to switch choices.) */
  case Fickle extends PlayerStubbornness(
    SdesDistribution.fromACM(new EnumeratedIntegerDistribution((1 :: List.fill(10)(0)).toArray))
        .map(_ == 1)
  )

  /** A stubborn player (i.e., one that tends not to switch choices.) */
  case Stubborn extends PlayerStubbornness(
    SdesDistribution.fromACM(new EnumeratedIntegerDistribution((0 :: List.fill(10)(1)).toArray))
        .map(_ == 1)
  )
