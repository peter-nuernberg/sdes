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

/** A state of a resource. */
trait SdesState :

  /** The identifier for this state. */
  def id: SdesId

/** Base states applicable to most resources. */
enum SdesBaseState extends SdesState :

  /** The identifier of this state. */
  override val id: SdesId = SdesId.next()

  /** A state signalling that a resource is newly created. */
  case Created extends SdesBaseState

  /** A state signalling that a resource is ready to dispatch simulation events. */
  case Ready extends SdesBaseState

  /** A state signalling that a resource is in an error state. */
  case Error extends SdesBaseState
