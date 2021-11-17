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

import java.time.{Duration, LocalDateTime}

/**
 * The game player.
 *
 * @param id            the identifier of this resource
 * @param state         the state of this resource
 * @param initialChoice the initial door choice made by this player, if any
 */
final case class Player(
    override val id    : SdesId,
    override val state : SdesState,
    initialChoice      : Option[Int] = None,
) extends GameResource :

  override def dispatch(sim : SdesSimulation[GameScenario]) : SdesResult[GameDispatchResponse] =

    sim.currentEvent match

      case SdesStartSimulationEvent(_) =>
        Right(
          updateResource(this, copy(state = SdesBaseState.Ready)),
          noScheduleChanges
        )

      case PromptPlayerEvent(_, doors) =>
        val doorNum = doors.chooseRandom()
        Right(
          updateResource(this, copy(initialChoice = Some(doorNum))),
          addEvent(sim.clock ++ sim.scenario.playerDecisiveness.delay(), GameEvent.initialChoice(doorNum))
        )

      case InsistOnAnswerEvent(_, doors) =>
        val doorNum = doors.chooseFirst()
        Right(
          updateResource(this, copy(state = PlayerState.Hurried, initialChoice = Some(doorNum))),
          removeEvents[InitialChoiceEvent].andThen(
            addEvent(sim.clock ++ Player.hurriedDelay, GameEvent.initialChoice(doorNum))
          )
        )

      case SwitchOfferEvent(_, doors) =>
        val willSwitch =
          if state == PlayerState.Hurried then
            scala.util.Random.nextBoolean()
          else
            sim.scenario.playerStubbornness.willSwitch()
        val doorNum = initialChoice.map(i => if willSwitch then doors.switchRandom(i) else i).getOrElse(-1)
        Right(
          noResourceChanges,
          addEvent(sim.clock ++ Player.switchDelay, GameEvent.finalChoice(doorNum))
        )

      case _ => Right(doNothing)

/** Factories and constants for [[Player]]. */
object Player {

  /** The delay for replying with the player's initial choice when hurried. */
  val hurriedDelay : Duration = Duration.ofSeconds(5)

  /** The delay for replying with the player's final choice. */
  val switchDelay : Duration = Duration.ofSeconds(10)

  /**
   * Returns a Player in its initial state.
   *
   * @return a Player in its initial state
   */
  def init() : Player = Player(SdesId.next(), SdesBaseState.Created)
}

/** An enumeration of states unique to a game player. */
enum PlayerState extends SdesState :

  /** The identifier of this state. */
  override val id : SdesId = SdesId.next()

  /** A state indicating that the player is hurried. */
  case Hurried extends PlayerState
