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

import org.scalacheck.Gen

import java.time.LocalDateTime

/** A specification of the DSL. */
class DslSpec extends BaseSpec :

  // ---- generators

  val resourcesGen : Gen[SdesResources[TestScenario]] =
    for
      n <- Gen.chooseNum(0, 10)
      l <- Gen.listOfN(n, new TestResource())
    yield l

  val scheduleGen : Gen[SdesSchedule] =
    for
      n <- Gen.chooseNum(0, 10)
      l <- Gen.listOfN(n, (LocalDateTime.now(), new TestEvent()))
    yield l

  val scheduleAltGen : Gen[SdesSchedule] =
    for
      n <- Gen.chooseNum(0, 10)
      l <- Gen.listOfN(n, (LocalDateTime.now(), new TestEventAlt()))
    yield l

  // ---- assertions

  "The noResourceChanges transform should leave a resource list unaffected" in {
    forAll((resourcesGen, "rs")) {
      rs =>
        noResourceChanges(rs) shouldBe rs
    }
  }

  "The addResource transform should add the given resource to any resource list" in {
    forAll((resourcesGen, "rs")) {
      rs =>
        val newR = new TestResource()
        val newRs = addResource(newR)(rs)

        newRs should have size (rs.size + 1)
        rs.foreach { r => newRs should contain(r) }
        newRs should contain(newR)
    }
  }

  "The removeResources transform should have the expected effect on any resource list" in {
    forAll((resourcesGen, "rs")) {
      rs =>
        val p : SdesResource[TestScenario] => Boolean = { _.id.value % 2 == 0 }
        val newRs = removeResources[TestScenario](p)(rs)
        val numEvenIds = rs.count(p)

        newRs should have size (rs.size - numEvenIds)
        newRs.foreach { r => p(r) shouldBe false }
    }
  }

  "The removeResource(id) transform should remove the resource with the given id from any resource list" in {
    forAll((resourcesGen, "rs")) {
      rs =>
        val newR = new TestResource()
        val newRs = removeResource[TestScenario](newR.id)(scala.util.Random.shuffle(newR :: rs))

        newRs should have size rs.size
        rs.foreach { r => newRs should contain(r) }
        newRs should not contain (newR)
    }
  }

  "The removeResource(resource) transform should remove the given resource from any resource list" in {
    forAll((resourcesGen, "rs")) {
      rs =>
        val newR = new TestResource()
        val newRs = removeResource[TestScenario](newR)(scala.util.Random.shuffle(newR :: rs))

        newRs should have size rs.size
        rs.foreach { r => newRs should contain(r) }
        newRs should not contain (newR)
    }
  }

  "The noScheduleChanges transform should leave a schedule unaffected" in {
    forAll((scheduleGen, "s")) {
      s =>
        noScheduleChanges(s) shouldBe s
    }
  }

  "The addEvent transform should add the given event to any schedule" in {
    forAll((scheduleGen, "s")) {
      s =>
        val (when, newE) = (LocalDateTime.now(), new TestEvent())
        val newS = addEvent(when, newE)(s)

        newS should have size (s.size + 1)
        s.foreach { dte => newS should contain(dte) }
        newS should contain((when, newE))
    }
  }

  "The removeEvents(predicate) transform should have the expected effect on any schedule" in {
    forAll((scheduleGen, "s")) {
      s =>
        val p : SdesScheduledEvent => Boolean = { _._2.id.value % 2 == 0 }
        val newS = removeEvents(p)(s)
        val numEvenIds = s.count(p)

        newS should have size (s.size - numEvenIds)
        newS.foreach { dte => p(dte) shouldBe false }
    }
  }

  "The removeEvent(id) transform should remove the event wit the given id from any schedule" in {
    forAll((scheduleGen, "s")) {
      s =>
        val (when, newE) = (LocalDateTime.now(), new TestEvent())
        val newS = removeEvent(newE.id)(scala.util.Random.shuffle((when, newE) :: s))

        newS should have size s.size
        s.foreach { dte => newS should contain(dte) }
        newS should not contain ((when, newE))
    }
  }

  "The removeEvent(event) transform should remove the given event from any schedule" in {
    forAll((scheduleGen, "s")) {
      s =>
        val (when, newE) = (LocalDateTime.now(), new TestEvent())
        val newS = removeEvent(newE)(scala.util.Random.shuffle((when, newE) :: s))

        newS should have size s.size
        s.foreach { dte => newS should contain(dte) }
        newS should not contain ((when, newE))
    }
  }

  "The removeEvents(type) transform should remove the given event from any schedule" in {
    forAll((scheduleGen, "s1"), (scheduleAltGen, "s2")) {
      (s1, s2) =>
        val s = s1 ++ s2
        val newS = removeEvents[TestEventAlt](s)

        newS should have size s1.size
        s1.foreach { dte => newS should contain(dte) }
        s2.foreach { dte => newS should not contain (dte) }
    }
  }
