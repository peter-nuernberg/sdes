# Monty Hall Example

This package contains an example simulation based on the [Monty Hall problem](https://en.wikipedia.org/wiki/Monty_Hall_problem).

## Resources

There are two resources: Monty and Player.

### Monty

Monty is a simple, (essentially) stateless resource that follows a set script (see below).

### Player

In this simulation, a player has two characteristics: decisiveness, and stubbornness.
The player's decisiveness determines how long it takes that player to respond with its initial guess.
There are two possible options: *decisive* players respond quickly, while *indecisive* players respond slowly.
The player's stubbornness determines how likely it is that a player will change its guess when offered to do so.
Again, there are two possible options: *stubborn* players rarely change, while *fickle* players often change.

## Simulation script

1. When the simulation starts, Monty schedules 2 events: *PromptPlayer* at +10s and *InsistOnAnswer* at +40s.

2. On *PromptPlayer*, Player schedules an *InitialChoice* event with a random door at a random offset determined by its decisiveness.

3. On *InitialChoice*, Monty unschedules any pending *InsistOnAnswer* events, opens a random bogey door, and schedules *SwitchOffer* at +30s.

4. On *InsistOnAnswer*, Player unschedules any pending *InitialChoice* events, schedules a new *InitialChoice* event with door #1, and switches to the *Hurried* state.

5. On *SwitchOffer*, Player schedules a *FinalChoice* event at +10s.
   Whether this event indicates that the player switches its guess depends on factors.
   If Player is hurried, the probability that it switches is 50%.
   Otherwise, the probability is dictated by the player's stubbornness.

6. On *FinalChoice*, Monty logs whether Player wins.

## Game and results

The method [`Game.play`](./Game.scala) will execute each scenario (decisive/stubborn, decisive/fickle, indecisive/stubborn, indecisive/fickle) 1000 times.
Results are logged to standard out, and to a results file named `results.csv`.
The results file has 3 columns.
The first column contains either the string `Decisive` or `Indecisive`.
The second column contains either the string `Stubborn` or `Fickle`.
The third column contains either `0` (indicating a loss) or `1` (indicating a win).

The expected result is that stubbornness is highly negatively correlated with winning, whereas decisivess is somewhat negatively correlated with winning.

## Modifications

Some avenues for exploration include the following, most of which are set in [`GameScenario`](./GameScenario.scala):

- Adding a range of options for decisiveness and/or stubbornness.

- Changing the number of doors in the game.
  More doors should increase the benefit of being fickle.

- Changing the number of doors Monty keeps closed before offering a switch to Player.
  More closed doors should decrease the benefit of being fickle.
