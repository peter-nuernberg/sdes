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
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.¬
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package us.nuernberg.sdes

import java.time.LocalDateTime

// ---- generic aliases

/** The result of an operation. */
type SdesResult[A] = Either[SdesError, A]

/** The response of a resource to a dispatch request from the simulation driver. */
type SdesDispatchResponse[S <: SdesScenario] = (SdesResourceTransform[S], SdesScheduleTransform)

// ---- resource aliases

/** A list of resources. */
type SdesResources[S <: SdesScenario] = List[SdesResource[S]]

/** A transform applicable to a list of resources. */
type SdesResourceTransform[S <: SdesScenario] = SdesResources[S] => SdesResources[S]

// ---- schedule aliases

/** A scheduled event. */
type SdesScheduledEvent = (LocalDateTime, SdesEvent)

/** A list of scheduled events. */
type SdesSchedule = List[SdesScheduledEvent]

/** A transform applicable to a schedule. */
type SdesScheduleTransform = SdesSchedule => SdesSchedule
