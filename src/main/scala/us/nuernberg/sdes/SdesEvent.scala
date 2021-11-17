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

/** An event. */
trait SdesEvent:

  /** The identifier of this event. */
  def id : SdesId

/**
 * An event signalling the start of a simulation.
 *
 * @param id the identifier of this event
 */
final case class SdesStartSimulationEvent(override val id : SdesId) extends SdesEvent

/**
 * An event signalling the abnormal termination of a simulation.
 *
 * @param id the identifier of this event
 */
final case class SdesAbortSimulationEvent(override val id : SdesId) extends SdesEvent

/** Factories and constants for [[SdesEvent]]. */
object SdesEvent:

  /**
   * Creates a new event signalling the start of a simulation.
   *
   * @return a new event signalling the start of a simulation
   */
  def startSimulation() : SdesEvent = SdesStartSimulationEvent(SdesId.next())

  /**
   * Creates a new event signalling the abnormal termination of a simulation.
   *
   * @return a new event signalling the abnormal termination of a simulation
   */
  def abortSimulation() : SdesEvent = SdesAbortSimulationEvent(SdesId.next())
