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

import java.time.Duration

/**
 * A resource in a simulation.
 * 
 * @tparam S the type of scenario expected by this resource
 */
trait SdesResource[S <: SdesScenario]:

  /** The identifier of this resource. */
  def id : SdesId

  /** The state of this resource. */
  def state : SdesState

  /**
   * Dispatches the current event of the given simulation.
   *
   * This should be defined by every implementing type.
   *
   * @param sim the simulation context for the dispatch
   *
   * @return either a pair of transforms to be applied to the given simulation resources and schedule, or an error
   */
  def dispatch(sim : SdesSimulation[S]) : SdesResult[SdesDispatchResponse[S]]

  /**
   * Dispatches the current event of the given simulation.
   *
   * This is a wrapper which intercepts certain internal events (e.g., [[SdesAbortSimulationEvent]]) before passing
   * control to [[SdesResource.dispatch]]
   *
   * @param sim the simulation context for the dispatch
   *
   * @return either a pair of transforms to be applied to the given simulation resources and schedule, or an error
   */
  def dispatchWrapper(sim : SdesSimulation[S]) : SdesResult[SdesDispatchResponse[S]] =
    sim.currentEvent match
      case SdesAbortSimulationEvent(_) => Right(doNothing)
      case _                           => dispatch(sim)
