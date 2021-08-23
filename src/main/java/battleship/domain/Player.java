package battleship.domain;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player {

    final public static int PLAYER = 0;
    final public static int COMPUTER = 1;

    // evt Lav Player som superclass med computer og menneske som underklasse evt?
    String name;
    boolean human;
    Ship[] ship = new Ship[BattleshipGame.noOfShips];
    Board board = new Board(BattleshipGame.gridSize);
    int aiLevel = 4;

    // computer ai!
    int aiStatus = 0;
    Point lastHit;

    final int UP = 0;
    final int DOWN = 1;
    final int LEFT = 2;
    final int RIGHT = 3;

    // DISSE KAN LAVES FINAL STATIC?
    /*
     * aiStatus = 0: intet truffet - random skud
     * aiStatus = 1: skib truffet sidste skud
     * aiStatus = 2: skib vertikal - skyd op
     * aiStatus = 3: skib vertikal - skyd ned
     * aiStatus = 4; skib horisontal - skyd venstre
     * aiStatus = 5: skib horisontal - skyd højre
     */

    // Ai level 0 = stupid, stay in random shot
    // Ai level 1 = clever when hit a boat, bot no 4'marks
    // Ai level 2 = clever when hit a boat, + 4 marks
    // ai level 3 = sometimes use intelligent random shot!
    // ai level 4 = allways use intelligent random shot

    public Player(boolean human) {
        this.human = human;
        System.out.println("NOS"+BattleshipGame.noOfShips);
        generateShips();

        if (human) {
            this.name = "no name";
        }

        if (!human) {
            this.name = "computer";
            randomShipPlacement();
        }

    }

    private void generateShips() {
        System.out.println("generate ships");
        System.out.println(BattleshipGame.ships);
        // generating an array of ships based on the defenitions i BattleshipGame
        int i = 0;
        for (String shipName : BattleshipGame.ships.keySet()) {
            System.out.println("shipname " + shipName);
            ship[i] = new Ship(BattleshipGame.ships.get(shipName), shipName);
            i++;
        }
    }

    public void placeShip(int shipNr, Point point) {
        ship[shipNr].place(board, point);
    }

    public void randomShipPlacement() {
        board.clearBoard();
        boolean randomHor;

        // løkke der gennemgår alle antalSkibe
        for (int i = 0; i < BattleshipGame.noOfShips; i++) {

            boolean shipPlaced = false;

            while (!shipPlaced) {

                randomHor = (boolean) (Math.random() < 0.5);
                Point randomPoint = randomPoint();

                // Sætter en random orientering
                ship[i].setHorizontal(randomHor);

                // Tjekker om skibet kan placers
                if (ship[i].canPlace(randomPoint, board)) {
                    ship[i].place(board, randomPoint, randomHor); // placerer skib
                    shipPlaced = true; // afslutter løkke
                }
            }
        }
    }

    public int longestShipLength(){
        int longestShip = 0;

        for (int i = 0; i < BattleshipGame.noOfShips; i++) {
            if (!ship[i].isSunk() && ship[i].getLength() > longestShip) {
                longestShip = ship[i].getLength();
            }
        }

        return longestShip;
    }

    public Point randomPoint() {

        // vælger random parametre mlm 0 og str. på bræt og returnerer som Point
        int randomX = (int) (Math.random() * board.getGridSize());
        int randomY = (int) (Math.random() * board.getGridSize());
        return new Point(randomX, randomY);

    }

    public Point aiShot(Board playerBoard, int longestShipRemaining) {

        System.out.println("aiStatus " + aiStatus);
        System.out.println("aiLevel " + aiLevel);
        //System.out.println("longest ship " + longestShipRemaining);


        Point returnPoint = null;
        int shot;


        while (true) {
            // System.out.println("ai " + aiStatus);

            switch (aiStatus) {

                case 0:

                    // intet ramt, tilfældigt skud
                    // System.out.println("case 0");

                    if (aiLevel < 3) {
                        returnPoint = randomShot(playerBoard);
                    }

                    if (aiLevel == 3) {
                        boolean randomBol = new Random().nextBoolean();
                        if (randomBol) {
                            returnPoint = randomShot(playerBoard);
                        }
                        else {
                            returnPoint = intelligentRandomShot(playerBoard, longestShipRemaining);
                        }
                    }
                    if (aiLevel > 3) {
                        returnPoint = intelligentRandomShot(playerBoard, longestShipRemaining);
                    }


                    shot = playerBoard.getValue(returnPoint);

                    if (shot == Board.SHIP) {
                        lastHit = returnPoint;
                        if (aiLevel != 0) aiStatus = 1;
                    }
                    return returnPoint;


                case 1:// Et skib lige ramt, og retning skal estableres
                    // System.out.println("case 1");

                    // skyder i tilfældig retning ud fra hit!
                    int randomDirection = (int) (Math.random() * 4);

                    // System.out.println("random direction " + randomDirection);

                    if (randomDirection == UP) {

                        // breaks if the point hit is in the top of the grid x=0
                        if (lastHit.x == 0) break;

                        // finds a point 1 UP from lastHit. (point.x-1)
                        returnPoint = new Point(lastHit.x - 1, lastHit.y);

                        // find value from opponents grid in point
                        shot = playerBoard.getValue(returnPoint);

                        // if point is empty - keep shooting randomly around point (aiStatus = 1)
                        if (shot == Board.EMPTY) {
                            return returnPoint;
                        }

                        // if you hit a ship, you know that the ship i vertical, and you keep shootin UP (change aiStatus to 2)
                        if (shot == Board.SHIP) {
                            aiStatus = 2;
                            return returnPoint;
                        }

                        // if the point is a place you previously missed or AI calculated as not possible to place a ship.
                        if (shot == Board.MISS || shot == Board.AI) {
                            break;
                        }
                    }

                    if (randomDirection == DOWN) {

                        if (lastHit.x == board.getGridSize() - 1) break;

                        returnPoint = new Point(lastHit.x + 1, lastHit.y); // finder punkt 1 ned (x+1)
                        shot = playerBoard.getValue(returnPoint);

                        if (shot == Board.EMPTY) {
                            return returnPoint;
                        }

                        if (shot == Board.SHIP) {
                            aiStatus = 3;
                            return returnPoint;
                        }

                        if (shot == Board.MISS || shot == Board.AI) {
                            break;
                        }
                    }


                    if (randomDirection == LEFT) {

                        if (lastHit.y == 0) break;

                        returnPoint = new Point(lastHit.x, lastHit.y - 1);
                        shot = playerBoard.getValue(returnPoint);

                        if (shot == Board.EMPTY) {
                            return returnPoint;
                        }

                        if (shot == Board.SHIP) {
                            aiStatus = 4;
                            return returnPoint;
                        }

                        if (shot == Board.MISS || shot == Board.AI) {
                            break;
                        }
                    }


                    if (randomDirection == RIGHT) {

                        if (lastHit.y == board.getGridSize() - 1) break;

                        returnPoint = new Point(lastHit.x, lastHit.y + 1); // finder punkt 1 th (y+1)
                        shot = playerBoard.getValue(returnPoint);

                        if (shot == Board.EMPTY) {
                            return returnPoint;
                        }

                        if (shot == Board.SHIP) {
                            aiStatus = 5;
                            return returnPoint;
                        }

                        if (shot == Board.MISS || shot == Board.AI) {
                            break;
                        }
                    }
                {
                    // NB delete this code!
                    //System.out.println(returnPoint);
                    System.out.println("reached end of case 1! - some bug?");
                    break;
                }


                case 2: // skib vertikal - skyd op
                    // System.out.println("case 2");

                    if (lastHit.x == 0) {
                        aiStatus = 3; // vi er øverst på pladen, prøv ned i stedet.
                        break;
                    }

                    returnPoint = new Point(lastHit.x - 1, lastHit.y); // finder punkt 1 op

                    shot = playerBoard.getValue(returnPoint); // finder værdi i 1 op (x-1)

                    if (shot == Board.EMPTY) {
                        aiStatus = 3; // missed skud, prøv nedad næste gang
                        return returnPoint;
                    }

                    if (shot == Board.SHIP) { // fortsætte med at ramme næste skud i denne retning
                        return returnPoint;
                    }

                    if (shot == Board.HIT) { // hvis vi rammer et flet der allerede er ramt, er det fordi vi har vendt om og skal fortsætte i samme retning
                        lastHit.x = lastHit.x - 1;
                        break;
                    }

                    if (shot == Board.MISS || shot == Board.AI) { // tidligere skudt, eller ikke en mulighed, prøv nedad!
                        aiStatus = 3;
                        break;
                    }

                case 3:  // skib v -  sidste træf var ned
                    // System.out.println("case 3");

                    if (lastHit.x == board.getGridSize() - 1) {
                        aiStatus = 2;
                        break;
                    }

                    returnPoint = new Point(lastHit.x + 1, lastHit.y);
                    shot = playerBoard.getValue(returnPoint);

                    if (shot == Board.EMPTY) {
                        aiStatus = 2;
                        return returnPoint;
                    }

                    if (shot == Board.SHIP) {
                        return returnPoint;
                    }

                    if (shot == Board.HIT) {
                        lastHit.x = lastHit.x + 1;
                        break;
                    }

                    if (shot == Board.MISS || shot == Board.AI) {
                        aiStatus = 2;
                        break;
                    }

                case 4: // skib h -  sidste træf var venstre
                    // System.out.println("case 4");

                    if (lastHit.y == 0) {
                        aiStatus = 5;
                        break;
                    }

                    returnPoint = new Point(lastHit.x, lastHit.y - 1);
                    shot = playerBoard.getValue(returnPoint);

                    if (shot == Board.EMPTY) {
                        aiStatus = 5;
                        return returnPoint;
                    }

                    if (shot == Board.SHIP) {
                        return returnPoint;
                    }

                    if (shot == Board.HIT) {
                        lastHit.y = lastHit.y - 1;
                        break;
                    }

                    if (shot == Board.MISS || shot == Board.AI) {
                        aiStatus = 5;
                        break;
                    }


                case 5: // skib horisontalt -  sidste træf var højre
                    // System.out.println("case 5");

                    if (lastHit.y == board.getGridSize() - 1) {
                        aiStatus = 4;
                        break;
                    }

                    returnPoint = new Point(lastHit.x, lastHit.y + 1);

                    shot = playerBoard.getValue(returnPoint);

                    if (shot == Board.EMPTY) {
                        aiStatus = 4;
                        return returnPoint;
                    }

                    if (shot == Board.SHIP) {
                        return returnPoint;
                    }

                    if (shot == Board.HIT) {
                        lastHit.y = lastHit.y + 1;
                        break;
                    }

                    if (shot == Board.MISS || shot == Board.AI) {
                        aiStatus = 5;
                        break;
                    }
            }
        }
    }

    private Point randomShot(Board playerBoard) {
        System.out.println("randomshot");
        Point returnPoint;
        do {
            // skyder på tilfældigt punkt
            returnPoint = randomPoint();
            // henter værdien af tilfældigt punkt på spillers plade
        } while (playerBoard.getValue(returnPoint) >= 2);
        return returnPoint;
    }

    private Point intelligentRandomShot(Board board, int longestShipRemaining) {
        System.out.println("intelligent random shot");
        // tjek i alle fire retninger ud fra punkt om der er 4 tomme felter foran og en "barrierer på 5"
        // de punkter der har flest hit, er bedste skud!
        // for AI=0.
        List<Point> returnPoints = new ArrayList<>();

        int maxPossibleHit = 0;

        // genererer liste med bedste muligheder
        for (int row = 0; row < board.getGridSize(); row++) {

            for (int column = 0; column < board.getGridSize(); column++) {

                Point p = new Point(row, column);

                if (board.getValue(p) == Board.EMPTY ||
                        board.getValue(p) == Board.SHIP
                )

                {
                    int possibleHitsAtPoint = possibleHits(board, p, longestShipRemaining);


                    if (possibleHitsAtPoint == maxPossibleHit) {
                        returnPoints.add(p);
                    }

                    if (possibleHitsAtPoint > maxPossibleHit) {
                        returnPoints.clear();
                        returnPoints.add(p);
                        maxPossibleHit = possibleHitsAtPoint;
                        System.out.println("new max pos hits:" + maxPossibleHit);
                    }
                }
            }
        }

        // vælger random element i listen
        int randomIndex = (int) (Math.random() * returnPoints.size());
        System.out.println("return points" + returnPoints);

        Point returnPoint = returnPoints.get(randomIndex);
        System.out.println("return Point " + returnPoint);
        System.out.println("max pos hits" + maxPossibleHit);
        System.out.println("longest ship remaining" + longestShipRemaining);
        return returnPoints.get(randomIndex);
    }

    private int possibleHits(Board board, Point point, int longestShip) {
        int result = 0;
        //Point checkPoint = new Point(point);
        // evaluer om fundne pkt er hit etc.
        //UP
        if (checkNeighbour(board, point, longestShip,-1,0)) {
            //System.out.println("point " + point + " up TRUE");
            result++;
        }

        //DOWN
        if (checkNeighbour(board, point, longestShip,1,0)) {
            //System.out.println("point " + point + " down TRUE");

            result++;
        }

        // LEFT
        if (checkNeighbour(board, point, longestShip,0,-1)) {
            //System.out.println("point " + point + " left TRUE");
            result++;
        }

        // Right
        if (checkNeighbour(board, point, longestShip,0,1)) {
            //System.out.println("point " + point + " right TRUE");

            result++;
        }
        return result;
    }


    public boolean checkNeighbour(Board board, Point checkPoint, int longestShip, int dx, int dy) {
        // tjek i en retninge dx,dy ud fra punkt p om der er ls-1 tomme felter foran og en "barrierer på ls"
        // anvendes til intelligent Ai shot med status ai= 0 (random)
        // bruger rekursion

        Point point = new Point(checkPoint);

        longestShip--;
        point.translate(dx, dy);

        // tjek om vi har nået kanten og der er mere skib tilbage
        if ((point.y == -1 && longestShip != 0)
                || (point.x == -1 && longestShip != 0)
                || (point.x == BattleshipGame.gridSize && longestShip != 0)
                || (point.y == BattleshipGame.gridSize && longestShip != 0)) {
            return false;
        }

        // Hvis vi når til sidste element af skibet
        if (longestShip == 0) {
            // tjekker om det er en kant!
            if (point.y == -1 || point.y == BattleshipGame.gridSize
                    || point.x == -1 || point.x == BattleshipGame.gridSize) {
                return true;
            }

            // returnerer true om det er hit, miss elle AI, ellers false
            return board.getValue(point) >= 2;
        }

        // hvis det er tomt eller skib felt, tjek næste
        if (board.getValue(point) == Board.EMPTY
                || board.getValue(point) == Board.SHIP) {
            return checkNeighbour(board, point, longestShip, dx, dy);
        } else {
            return false;
        }
    }

    public int saveHit(Point point) {
        // gennemløber alle skibe, og ser om punktet er indeholdt i dem,
        // registrer hit på det der er ramt.
        // returnerer skibsnummer hvis det er lige sunket
        for (int i = 0; i < BattleshipGame.noOfShips; i++) {
            if (ship[i].updateHitCount(point)) {
                return i;
            }
        }
        return -1;
    }

    public boolean allSunk() {
        for (int i = 0; i < BattleshipGame.noOfShips; i++) {
            if (!ship[i].isSunk()) return false;
        }
        return true;
    }

    public void aiMarkingsWhenSunk(int shipNr) {
        if (aiLevel < 2) return;

        // indsætter 4'taller rundt om skib, der hvor der ikke er 3-taller
        Point shipPos = ship[shipNr].getPosition();

        // flytter punkt vertikalt eller horisontalt!
        for (int i = 0; i < ship[shipNr].getLength(); i++) {
            if (ship[shipNr].isHorizontal()) {
                shipPos.y = ship[shipNr].getPosition().y + i;
            }

            if (!ship[shipNr].isHorizontal()) {
                shipPos.x = ship[shipNr].getPosition().x + i;
            }

            // kører rundt om punkt
            for (int j = -1; j < 2; j++) {
                // y konstant x flytter
                setAiIfEmpty(new Point(shipPos.x + j, shipPos.y));
                // y-1
                setAiIfEmpty(new Point(shipPos.x + j, shipPos.y - 1));
                // y +1
                setAiIfEmpty(new Point(shipPos.x + j, shipPos.y + 1));
            }
        }
    }

    private void setAiIfEmpty(Point point) {
        if (point.x >= 0 && board.getGridSize() - 1 >= point.x &&
                point.y >= 0 && point.y <= board.getGridSize() - 1) {

            if (board.getValue(point) == 0) {
                board.setValue(point, 4);
            }
        }
    }

    // Getter & Setters
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Ship[] getShip() {
        return ship;
    }

    public void setShip(Ship[] ship) {
        this.ship = ship;
    }

    public int getAiStatus() {
        return aiStatus;
    }

    public void setAiStatus(int aiStatus) {
        this.aiStatus = aiStatus;
    }

    public Point getLastHit() {
        return lastHit;
    }

    public void setLastHit(Point lastHit) {
        this.lastHit = lastHit;
    }

    public int getAiLevel() {
        return aiLevel;
    }

    public void setAiLevel(int aiLevel) {
        this.aiLevel = aiLevel;
    }

    @Override
    public String toString() {
        return "name " + name + "\nhuman " + human;
    }
}
