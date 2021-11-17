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

/**
 * A door behind which lies either a prize or a bogey.
 *
 * @param num  the number of this door
 * @param open whether this door is open
 */
final case class Door(num : Int, open : Boolean):

  override val toString : String =
    if open then s"-$num-" else s"[$num]"

/**
 * A set of doors.
 *
 * @param doors the doors in this set
 */
final case class Doors(doors : List[Door]):

  /** A string representation of this set of doors. */
  override val toString : String = doors.mkString("[", ", ", "]")

  /**
   * Returns the number of the first unopened door.
   *
   * @return the number of the first unopened door
   */
  def chooseFirst() : Int = choose(false, Nil)

  /**
   * Returns the number of a random unopened door.
   *
   * @return the number of a random unopened door
   */
  def chooseRandom() : Int = choose(true, Nil)

  /**
   * Returns the number of a random door that is unopened and does not have the given number.
   *
   * @param initialChoice the number of the door to exclude from the possible results
   *
   * @return the number of a random door that is unopened and does not have the given number
   */
  def switchRandom(initialChoice : Int) : Int = choose(true, List(initialChoice))

  /**
   * Returns a set of doors based on this set of doors, with one (non-prize, non-player-chosen) door opened.
   *
   * @param prizeDoor    the number of the door containing the prize
   * @param playerChoice the number of the door initially chosen by the player
   *
   * @return a set of doors based on this set of doors, with one (non-prize, non-player-chosen) door opened
   */
  def openBogey(prizeDoor : Int, playerChoice : Int) : Doors =
    val bogey = choose(true, List(prizeDoor, playerChoice))
    Doors((Door(bogey, true) :: doors.filterNot(_.num == bogey)).sortBy(_.num))

  /**
   * Returns the number of a door within the given constraints.
   *
   * @param random  whether a random unopened door (as opposed to the first unopened door) not in the exclude list should
   *                be returned
   * @param exclude a list of door numbers not to be returned
   *
   * @return the number of a door within the given constraints
   */
  private def choose(random : Boolean, exclude : List[Int]) : Int =
    val available = doors.filterNot(d => d.open || exclude.contains(d.num))
    val index = if random then scala.util.Random.between(0, available.length) else 0
    available(index).num

/** Factories and constants for [[Doors]]. */
object Doors:

  /**
   * Returns a set of unopened doors.
   *
   * @param numDoors the number of doors to return
   *
   * @return a set of unopened doors
   */
  def apply(numDoors : Int) : Doors = Doors((1 to numDoors).toList.map(i => Door(i, false)))
