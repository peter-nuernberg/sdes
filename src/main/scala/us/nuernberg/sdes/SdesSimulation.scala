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

import com.typesafe.scalalogging.Logger

import java.time.{Duration, LocalDateTime}

/**
 * A simulation.
 *
 * @param id        the identifier of this simulation
 * @param scenario  the scenario for this simulation
 * @param clock     the clock used by this simulation
 * @param resources the resources in this simulation
 * @param schedule  the schedule of events to be executed (potentially) by this simulation
 * @tparam S the type of scenario in this simulation
 */
final case class SdesSimulation[S <: SdesScenario](
    id        : SdesId,
    scenario  : S,
    clock     : LocalDateTime,
    resources : SdesResources[S],
    schedule  : SdesSchedule
):

  /** A logger for this simulation. */
  val logger = Logger[SdesSimulation[S]]

  /** Indicates whether there are any remaining scheduled events. */
  val done : Boolean = schedule.isEmpty

  /** The event to be dispatched by the resources in this simulation. */
  val currentEvent : SdesEvent = schedule.headOption.map(_._2).getOrElse(SdesEvent.abortSimulation())

/** Factories and constants for [[SdesSimulation]]. */
object SdesSimulation:

  /**
   * Creates a new simulation.
   *
   * @param scenario  the scenario for the simulation to be created
   * @param start     the start date/time for the simulation to be created
   * @param resources the resources in the simulation to be created
   * @tparam S the type of scenario in the simulation to be created
   *
   * @return a new simulation
   */
  def apply[S <: SdesScenario](scenario : S, start : LocalDateTime, resources : SdesResources[S]) : SdesSimulation[S] =
    SdesSimulation(SdesId.next(), scenario, start, resources, List(start -> SdesEvent.startSimulation()))

