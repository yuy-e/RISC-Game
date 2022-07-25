ECE 651: CI/CD 

![pipeline](https://gitlab.oit.duke.edu/afsana.chowdhury/ece-651-sp-22-risk/badges/Eval-3-Development/pipeline.svg)
![coverage](https://gitlab.oit.duke.edu/afsana.chowdhury/ece-651-sp-22-risk/badges/edit-CI/CD/coverage.svg?job=test)

## Coverage
[Detailed coverage](https://afsana.chowdhury.pages.oit.duke.edu/ece-651-sp-22-risk/dashboard.html)


> **Authur** Yue Yu, Afsana Chowdhury, Airu Song

## Run
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
## About the Game

## Project Development
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

### Eval-3 Game Preview (check main branch)
![5981658782331_ pic_hd](https://user-images.githubusercontent.com/60654350/180886078-06c5639c-f428-47b4-9e34-60b9534e821e.jpg)

![6041658782463_ pic_hd](https://user-images.githubusercontent.com/60654350/180886099-1f890152-6690-48c2-ae75-2751e31c9c83.jpg)

## UX Principles
We use the MVC design pattern and follow the following UX principles:

- **Figure-Gound and Focal Point:** We set territories in different colors indicating owned by different players, and the font color of the action would turn white to indicate their choice, making our app more user-friendly.

- **Similarity, Proximity, and Common Region:**
For displaying the pane of actions and choices, we use similar boxes and place them together(in a Turn Pane) to indicate they belong to the same action.



