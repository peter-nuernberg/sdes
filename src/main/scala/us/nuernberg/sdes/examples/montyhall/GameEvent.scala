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

/** An event generated in a Monty Hall game simulation. */
sealed trait GameEvent extends SdesEvent

/**
 * An event signalling Monty prompting the player for an initial guess.
 *
 * @param id    the identifier of this event
 * @param doors the current state of the doors
 */
final case class PromptPlayerEvent(override val id : SdesId, doors : Doors) extends GameEvent :
  override val toString : String = s"""PromptPlayer($doors)"""

/**
 * An event signalling Monty insisting that the player provide an initial guess.
 *
 * @param id    the identifier of this event
 * @param doors the current state of the doors
 */
final case class InsistOnAnswerEvent(override val id : SdesId, doors : Doors) extends GameEvent :
  override val toString : String = s"""InsistOnAnswer($doors)"""

/**
 * An event signalling a Player's initial guess.
 *
 * @param id      the identifier of this event
 * @param doorNum the door number guessed by the player
 */
final case class InitialChoiceEvent(override val id : SdesId, doorNum : Int) extends GameEvent :
  override val toString : String = s"""InitialChoice($doorNum)"""

/**
 * An event signalling Monty offering the player an opportunity to switch its guess.
 *
 * @param id    the identifier of this event
 * @param doors the current state of the doors
 */
final case class SwitchOfferEvent(override val id : SdesId, doors : Doors) extends GameEvent :
  override val toString : String = s"""Switch?($doors)"""

/**
 * An event signalling a Player's final guess.
 *
 * @param id      the identifier of this event
 * @param doorNum the door number guessed by the player
 */
final case class FinalChoiceEvent(override val id : SdesId, doorNum : Int) extends GameEvent :
  override val toString : String = s"""FinalChoice?($doorNum)"""

/** Factories and constants for [[GameEvent]]. */
object GameEvent {

  /**
   * Returns a new event signalling Monty prompting the player for an initial guess.
   *
   * @param doors the current state of the doors
   *
   * @return a new event signalling Monty prompting the player for an initial guess
   */
  def promptPlayer(doors : Doors) : GameEvent = PromptPlayerEvent(SdesId.next(), doors)

  /**
   * Returns a new event signalling Monty insisting that the player provide an initial guess.
   *
   * @param doors the current state of the doors
   *
   * @return a new event signalling Monty insisting that the player provide an initial guess
   */
  def insistOnAnswer(doors : Doors) : GameEvent = InsistOnAnswerEvent(SdesId.next(), doors)

  /**
   * Returns a new event signalling a Player's initial guess.
   *
   * @param doorNum the door number guessed by the player
   *
   * @return a new event signalling a Player's initial guess
   */
  def initialChoice(doorNum : Int) : GameEvent = InitialChoiceEvent(SdesId.next(), doorNum)

  /**
   * Returns a new event signalling Monty offering the player an opportunity to switch its guess.
   *
   * @param doors the current state of the doors
   *
   * @return a new event signalling Monty offering the player an opportunity to switch its guess
   */
  def switchOffer(doors : Doors) : GameEvent = SwitchOfferEvent(SdesId.next(), doors)

  /**
   * Returns a new event signalling a Player's final guess.
   *
   * @param doorNum the door number guessed by the player
   *
   * @return a new event signalling a Player's final guess
   */
  def finalChoice(doorNum : Int) : GameEvent = FinalChoiceEvent(SdesId.next(), doorNum)
}
