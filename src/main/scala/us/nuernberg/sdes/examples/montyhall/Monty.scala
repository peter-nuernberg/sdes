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
 * Monty (the game host).
 *
 * @param id        the identifier of this resource
 * @param state     the state of this resource
 * @param doors     the doors in the game hosted by this resource
 * @param prizeDoor the number of the door containing the prize in the game hosted by this resource
 */
final case class Monty(
    override val id    : SdesId,
    override val state : SdesState,
    doors              : Doors,
    prizeDoor          : Int,
) extends GameResource :

  override def dispatch(sim : SdesSimulation[GameScenario]) : SdesResult[GameDispatchResponse] =

    sim.currentEvent match

      case SdesStartSimulationEvent(_) =>
        Right(
          updateResource(this, copy(state = SdesBaseState.Ready)),
          addEvent(sim.clock ++ Monty.initialPromptDelay, GameEvent.promptPlayer(doors)).andThen(
            addEvent(sim.clock ++ Monty.insistDelay, GameEvent.insistOnAnswer(doors))
          )
        )

      case InitialChoiceEvent(_, doorNum) =>
        val numToOpen = sim.scenario.numDoors - sim.scenario.numClosedAtOffer
        val newDoors = (1 to numToOpen).toList.foldLeft(doors) { case (acc, _) => acc.openBogey(prizeDoor, doorNum) }
        Right(
          updateResource(this, copy(doors = newDoors)),
          removeEvents[InsistOnAnswerEvent].andThen(
            addEvent(sim.clock ++ Monty.offerDelay, GameEvent.switchOffer(newDoors))
          )
        )

      case FinalChoiceEvent(_, doorNum) =>
        sim.logger.info(sim.scenario.record(doorNum == prizeDoor))
        Right(doNothing)

      case _ => Right(doNothing)

/** Factories and constants for [[Monty]]. */
object Monty {

  /** The delay for scheduling the prompt for the player's initial choice. */
  val initialPromptDelay : Duration = Duration.ofSeconds(10)

  /** The delay for scheduling the insistence on the player's initial choice. */
  val insistDelay : Duration = initialPromptDelay + Duration.ofSeconds(30)

  /** The delay for scheduling the offer to the player to switch choices. */
  val offerDelay : Duration = Duration.ofSeconds(30)

  /**
   * Returns a Monty in its initial state.
   *
   * @param scenario the scenario for the game being hosted by Monty
   *
   * @return a Monty in its initial state
   */
  def init(scenario : GameScenario) : Monty =
    val doors = Doors(scenario.numDoors)
    Monty(SdesId.next(), SdesBaseState.Created, doors, doors.chooseRandom())
}
