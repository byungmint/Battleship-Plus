# Battleship Mayhem

## By Byung Min Jung

The program would be the game *Battleship* - a two player game where each player sets up ships of various sizes without 
letting the other player know of its location, and then taking turns to find and shoot down every ship before their
 opponent does so. To add a little twist to it, the player can choose how many opponents to face at once - every turn, 
 they will have to attack all other opponents, and in turn, the automated opponents would attack everyone other than 
 themselves. The board would be made of 8 x 8 tiles, a total of 64, with 4 ships to each player: 1 
 five-tile ship, 1 four-tile ship, and 2 three-tile ship. The winning condition would be to sink all of the computer's 
 ships before all of your ships get sunk.
 
 Anyone interested in amusing themselves for some time would be the users of this program. There's also the challenge of
 having to mentally juggle many different boards, and thus, ship placements, of all opponents. 
 I chose this project, as I am interested in game development. I also wanted to attempt creating a computer run 
 opponent as an additional challenge.
 
## User Stories
- As a user, I want to be able to select the number of opponents I would face at once, adding that many boards as well 
as my board to the game.
- As a user, I want to be able to add my ships in the positions of the board I want them to be in.
- As a user, I want the generated board for my opponent to be random.
- As a user, I want to be able to select tiles to attack in my opponent's board.
- As a user, I want empty tiles to be marked when shot, and told if I hit an enemy ship.
- As a user, I want to be able to save the computers' boards and my board (the placement of ships and
 marked locations) and maintain the turn order.
- As a user, I want to have the option to continue from a previously saved game.
- As a user, I want to be told and able to fix any invalid inputs.

# Instructions for Grader
- You can generate the first required event by starting a new game and inputting the number of opponents in the 
JOptionPane asking for the number of opponents. The first event is adding boards to the game.
- You can generate the second required event by clicking on the tile you want to attack on the opponent's board, and by
being (automatically) attacked by opponents. The second event is modifying the state of the board.
- You can locate my visual component at the middle of the screen - the board that allows the board of your choice to be
displayed.
- You can save the state of my application by going to 'Options' and 'Save' during a ceasefire (a pause in-game).
- You can reload the state of my application by clicking 'Yes' when the game starts up and detects a previous save.

# Phase 4 Task 2:
Option 1: Test and design a class that is robust. You must have at least one method that throws a checked exception. 
You must have one test for the case where the exception is expected and another where the exception is not expected.

The robust class is ShipPlacement (methods: checkValidPlacement(), isConsecutive()) - the thrown exceptions are thrown 
again in Board (method: tryAddingShip()), and caught in Game (method: goThroughAddingExceptions()).

# Phase 4 Task 3:
Problems:
- AllBoards in Game was public to allow access to it in GameRenderer, increasing coupling between GameRenderer and Game.
This was fixed by making the appropriate getters and setters in the class Game, and making AllBoards private.
- GameRenderer was responsible for both initializing all objects for it to render and keep updating it, which reduced
cohesion due to the single responsibility principle. This was fixed by creating separate classes for initializing the
items that would be inside the main JFrame (BoardList, BoardViewer, ChatBox, MyMenuBar).
- Game had a dependency on Board to create the player's board in the case of starting a new game, which introduced
coupling between Board and Game. This was fixed by adding a new, empty Board to AllBoards first, and accessing it to
add the player's ships; rather than setting aside a Board, adding the player's ships to it, and then adding the player's
board to allBoards.