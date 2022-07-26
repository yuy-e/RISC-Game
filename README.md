ECE 651: CI/CD 

![pipeline](https://gitlab.oit.duke.edu/afsana.chowdhury/ece-651-sp-22-risk/badges/Eval-3-Development/pipeline.svg)
![coverage](https://gitlab.oit.duke.edu/afsana.chowdhury/ece-651-sp-22-risk/badges/edit-CI/CD/coverage.svg?job=test)

## Coverage
[Detailed coverage](https://afsana.chowdhury.pages.oit.duke.edu/ece-651-sp-22-risk/dashboard.html)

> **Authur** Yue Yu, Afsana Chowdhury, Airu Song

## Table of Contents
1. [How to Run?](#run)
2. [How to play the game?](#game)
3. [UX principle](#ux)
4. [Project Development](#develop)
5. [Game Preview](#preview)

## How to Run <a name="run"></a>
Build the project: 
```
./gradlew installDist
```
Start the server of the game:
```
./server/build/install/server/bin/server
```
Activate the players:
```
./client/build/install/client/bin/client
```

## How to play the game? <a name="game"></a>

```Left Pane:``` Display resources productions and military deployment of a territory.
```Right Pane:``` Allows the player to choose, add, and commit their actions.
```Resource Pane:``` Display food and tech resources of players.

```Resources:``` Player's resources will increase by adding the resource production of each territory that they own at the end of each turn. ```Unit types:``` guards, infantry, archer, cavalry, dwarves, orcs, elves, spy. 

```Move:``` a player can relocate units within their territories. Move Actions will cost food resources according to the number of moving units and the 'size' of territories. 
Specifically, the cost of each move is (total size of territories moved through) * (number of units moved), and the game will pick the minimum total cost valid path when determining the cost.  

>'size': the cost to move through a territory  

```Attack:``` a player can send their units to an adjacent territory controlled by a different player to attack. Attack actions will cost 1 food resource per unit attacking.

```Upgrade:``` a player can upgrade the level of units, which costs tech resources. Upgrading a unit increases its combat bonuses. 

```Cloak:``` a player can cloak a territory, which will cost 20 tech resources and hide that territory from view for 3 turns.

```Fog of War:``` A player can only see their territories and adjacent territories. They can use SPY units to detect resources productions and military deployment of enemie's territories. If they can't see a territory anymore, they can still see old information about enemies' territories.

```Combat resolutions:```
- The server rolls two 20-sided dice (one for the attacker, one for the defender). Each roll is modified by adding a bonus for the type of unit involved. The side with the lower roll losts 1 unit.

- The order of evaluation alternates between the highest-bonus attacker unit paired with the lowest-bonus defender unit, and the lowest-bonus attacker unit paired with the highest-bonus defender unit. 

- If player A attacks territory X with units from multiple of her own territories, they count as a single combined force.

- If multiple players attack the same territories, each attack is resolved sequentially, with the winner of the first attack being the defender in subsequent attacks.

- If units from territory X attack territory Y, and at the same time, units from territory Y attack territory X, then they are assumed to take drastically different routes between their territories, missing each other, and ending up at their destination with no combat in the middle.

>The players enter 0 or more moves, then commit their moves. Once all players commit their moves, the game resolves the outcome of all moves, then reports that to the players, then the next turn happens in the same fashion. A player wins when he/she controls all territories, and loses when he/she no longer controls any territories.

## UX Principles <a name="ux"></a>
We use the MVC design pattern and follow the following UX principles:

- **Figure-Gound and Focal Point:** We set territories in different colors indicating owned by different players, and the font color of the action would turn white to indicate their choice, making our app more user-friendly.

- **Similarity, Proximity, and Common Region:**
For displaying the pane of actions and choices, we use similar boxes and place them together(in a Turn Pane) to indicate they belong to the same action.

## Project Development <a name="develop"></a>
### Eval-1 (check Eval1 branch)
- UML model 
https://github.com/yuy-e/RISC-Game/blob/main/UML-Eval1.jpg
![Final-Eval1](https://user-images.githubusercontent.com/60654350/180877298-8a1fe671-5cb9-480c-9c89-fd035215ba01.jpg)

### Eval-2 (check Eval2 branch) 
- UML Model
https://github.com/yuy-e/RISC-Game/blob/main/UML-Eval2.jpg
![Eval2](https://user-images.githubusercontent.com/60654350/180878054-4b11e571-4dca-4197-9fbe-973e6dbf8123.jpg)
- UX Prototype
[Eval-2 Prototype.pptx](https://github.com/yuy-e/RISC-Game/files/9184887/Eval-2.Prototype.pptx)
- Design
https://github.com/yuy-e/RISC-Game/blob/main/UXdocumentation.pdf

### Eval-3 Game Preview (check main branch) <a name="preview"></a>
![5981658782331_ pic_hd](https://user-images.githubusercontent.com/60654350/180886078-06c5639c-f428-47b4-9e34-60b9534e821e.jpg)

![6041658782463_ pic_hd](https://user-images.githubusercontent.com/60654350/180886099-1f890152-6690-48c2-ae75-2751e31c9c83.jpg)


