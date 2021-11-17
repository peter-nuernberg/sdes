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

import org.scalacheck.Gen

class DoorsSpec extends BaseSpec {

  val doorsAndChoiceAndPrizeGen : Gen[(Doors, Int, Int)] =
    for
      nc <- Gen.chooseNum(3, 6)
      no <- Gen.chooseNum(0, 3)
      opens = scala.util.Random.shuffle(List.fill(nc)(false) ++ List.fill(no)(true))
      doors = List.tabulate(nc + no) { i => Door(i + 1, opens(i)) }
      pc <- Gen.chooseNum(0, nc - 1)
      pd <- Gen.chooseNum(0, nc - 1)
    yield (Doors(doors), findNthClosed(doors, pc), findNthClosed(doors, pd))

  val doorsGen : Gen[Doors] = doorsAndChoiceAndPrizeGen.map { case (ds, _, _) => ds }

  val doorsAndChoiceGen : Gen[(Doors, Int)] = doorsAndChoiceAndPrizeGen.map { case (ds, pc, _) => (ds, pc) }

  private def findNthClosed(rest : List[Door], n : Int) : Int =
    (rest, n) match
      case (Nil, _)                => throw new Exception("ran out of doors!")
      case (Door(_, true) :: t, _) => findNthClosed(t, n)
      case (Door(num, _) :: _, 0)  => num
      case (_ :: t, _)             => findNthClosed(t, n - 1)

  "A set of doors" - {

    "when the first door is chosen, should return the number of the first closed door" in {
      forAll((doorsGen, "doors")) {
        ds =>
          val i = ds.chooseFirst()
          ds.doors.find(!_.open).value.num shouldBe i
      }
    }

    "when a random door is chosen, should return the number of a closed door" in {
      forAll((doorsGen, "doors")) {
        ds =>
          val i = ds.chooseRandom()
          ds.doors.find(_.num == i).value.open shouldBe false
      }
    }

    "when a random door is switched to, should return the number of a closed door that is not the initial choice" in {
      forAll((doorsAndChoiceGen, "(doors, playerChoice)")) {
        case (ds, pc) =>
          val i = ds.switchRandom(pc)
          ds.doors.find(_.num == i).value.open shouldBe false
          i should not be pc
      }
    }

    "when a random bogey door is opened, should return the number of a bogey door that is not the player choice" in {
      forAll((doorsAndChoiceAndPrizeGen, "(doors, playerChoice, prizeDoor)")) {
        case (ds, pc, pd) =>
          val newDs = ds.openBogey(pd, pc)
          newDs.doors.count(_.open) shouldBe (ds.doors.count(_.open) + 1)
          newDs.doors.find(_.num == pc).value.open shouldBe false
          newDs.doors.find(_.num == pd).value.open shouldBe false
      }
    }
  }
}
