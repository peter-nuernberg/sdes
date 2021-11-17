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

import java.time.LocalDateTime
import scala.reflect.ClassTag

// ---- resource transforms

/** The identity transform for a list of resources. */
def noResourceChanges[S <: SdesScenario] : SdesResourceTransform[S] = identity[SdesResources[S]]

/**
 * Returns a resource transform that adds the given resource to a list of resources.
 *
 * @param r the resource to be added
 * @tparam S the type of scenario expected by the given resource
 *
 * @return a resource transform that adds the given resource to a list of resources
 */
def addResource[S <: SdesScenario](r : SdesResource[S]) : SdesResourceTransform[S] = rs => r :: rs

/**
 * Returns a resource transform that removes all resources matching the given predicate from a list of resources.
 *
 * @param p the predicate identifying the resources to be removed
 * @tparam S the type of scenario expected by the identified resources
 *
 * @return a resource transform that removes all resources matching the given predicate from a list of resources
 */
def removeResources[S <: SdesScenario](p : SdesResource[S] => Boolean) : SdesResourceTransform[S] = _.filterNot(p)

/**
 * Returns a resource transform that removes the resource with the given identifier from a list of resources.
 *
 * @param id the identifier of the resource to be removed
 * @tparam S the type of scenario expected by the identified resource
 *
 * @return a resource transform that removes the resource with the given identifier from a list of resources
 */
def removeResource[S <: SdesScenario](id : SdesId) : SdesResourceTransform[S] = removeResources(_.id == id)

/**
 * Returns a resource transform that removes the given resource from a list of resources.
 *
 * @param r the resource to be removed
 * @tparam S the type of scenario expected by the given resource
 *
 * @return a resource transform that removes the given resource from a list of resources
 */
def removeResource[S <: SdesScenario](r : SdesResource[S]) : SdesResourceTransform[S] = removeResources(_ == r)

/**
 * Returns a resource transform that removes the given "old" resource and replaces it with the given "new" resource.
 *
 * @param oldR the resource to be removed
 * @param newR the resource to be added
 * @tparam S the type of scenario expected by the given resources
 *
 * @return a resource transform that removes the given "old" resource and replaces it with the given "new" resource
 */
def updateResource[S <: SdesScenario](oldR : SdesResource[S], newR : SdesResource[S]) : SdesResourceTransform[S] =
  removeResource(oldR) andThen addResource(newR)

// ---- schedule transforms

/** The identity transform for a schedule. */
val noScheduleChanges : SdesScheduleTransform = identity[SdesSchedule]

/**
 * Returns a schedule transform that adds the given event at the given date/time to a schedule.
 *
 * @param dt the date/time at which the given event is to be added
 * @param e  the event to be added
 *
 * @return a schedule transform that adds the given event at the given date/time to a schedule
 */
def addEvent(dt : LocalDateTime, e : SdesEvent) : SdesScheduleTransform = s => (dt, e) :: s

/**
 * Returns a schedule transform that removes all events matching the given predicate from a schedule.
 *
 * @param p the predicate identifying the events to be removed
 *
 * @return a schedule transform that removes all events matching the given predicate from a schedule
 */
def removeEvents(p : SdesScheduledEvent => Boolean) : SdesScheduleTransform = _.filterNot(p)

/**
 * Returns a schedule transform that removes the event with the given identifier from a schedule.
 *
 * @param id the identifier of the event to be removed
 *
 * @return a schedule transform that removes the event with the given identifier from a schedule
 */
def removeEvent(id : SdesId) : SdesScheduleTransform = removeEvents { (_, e) => e.id == id }

/**
 * Returns a schedule transform that removes the given event from a schedule.
 *
 * @param e the event to be removed
 *
 * @return a schedule transform that removes the given event from a schedule
 */
def removeEvent(e : SdesEvent) : SdesScheduleTransform = removeEvents { (_, e2) => e == e2 }

/**
 * Returns a schedule transform that removes all of the events of the given type from a schedule.
 *
 * @param tag a tag for the type of events to be removed
 * @tparam E the type of events to be removed
 *
 * @return a schedule transform that removes all of the events of the given type from a schedule
 */
def removeEvents[E <: SdesEvent](using tag : ClassTag[E]) : SdesScheduleTransform =
  removeEvents { (_, e) => tag.runtimeClass.isAssignableFrom(e.getClass) }

// ---- other

/** A dispatch response that consists of identity transforms for both resources and a schedule. */
def doNothing[S <: SdesScenario] : SdesDispatchResponse[S] = (noResourceChanges, noScheduleChanges)
