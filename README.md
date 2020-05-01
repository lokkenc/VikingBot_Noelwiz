# StarCraft BroodWar AI
A bot that uses AI Planning for general strategy management and Reinforcement ML.Learning for combat management.

## Requirements
 - Java 1.8
 - StarCraft: BroodWar patch 1.16.1
 - BWAPI / JavaBWAPI
 - Chaos Launcher
 
Refer to [SSCAI Tutorial](https://sscaitournament.com/index.php?action=tutorial) for a full setup guide

## Components
TODO

### Intelligence Agent
TODO

### Strategy Agent
TODO

### Combat Agent
TODO

### Economy Agent
TODO


## Replays
A list of replays recorded at the end of each milestone


## Making the Bot Play Itself
1. in intellij, compile the bot into a jar. (https://www.jetbrains.com/help/idea/compiling-applications.html#package_into_jar)
2. Convert the jar to a .exe https://www.genuinecoder.com/convert-java-jar-to-exe/
3. move the file to a place bwapi can find it (so basically into the bapi data folder)
4. set up the config to work for multiple ai http://www.starcraftai.com/wiki/Multiple_instances_of_StarCraft
5. use the Chaoslauncher - MultiInstance.exe rather than normal chaos launcher, launch it twice
6. multiplayer -> local pc, and join the game (make sure the config is expecting protoss vs protoss)
7. start the match and have fun tabbing between windows