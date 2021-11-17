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

/** An object that can run a simulation. */
object SdesSimulationDriver:

  /**
   * Runs the given simulation.
   *
   * @param sim the simulation to be run
   * @tparam S the type of scenario for the simulation to be run
   */
  def run[S <: SdesScenario](sim : SdesSimulation[S]) : Unit =
    Iterator.iterate(sim) {
      s =>
        val (dt, event) = s.schedule.headOption.getOrElse(sim.clock, SdesEvent.abortSimulation())
        val fwdSim = s.copy(clock = dt)
        val (resXform, schedXform) = s.resources.foldLeft(doNothing[S]) {
          case ((accRf, accSf), r) =>
            val (rf, sf) = r.dispatchWrapper(fwdSim).getOrElse((removeResource[S](r.id), noScheduleChanges))
            (accRf andThen rf, accSf andThen sf)
        }
        val newResources = resXform(s.resources)
        val newSchedule = removeEvent(event.id).andThen(schedXform)(s.schedule).sortBy(_._1)
        fwdSim.copy(resources = newResources, schedule = newSchedule)
    }.takeWhile(!_.done).foreach(_ => ())

