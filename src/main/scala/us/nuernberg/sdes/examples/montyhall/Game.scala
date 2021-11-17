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

import java.time.LocalDateTime

object Game {

  val numTrialsPerScenario = 1000

  @main def play : Unit =
    val startDt = LocalDateTime.now()
    GameScenario.all.foreach {
      scenario =>
        Iterator.continually {
          SdesSimulationDriver.run(
            SdesSimulation(
              scenario,
              startDt,
              List(Monty.init(scenario), Player.init())
            )
          )
        }.take(numTrialsPerScenario).foreach(_ => ())
    }
}
