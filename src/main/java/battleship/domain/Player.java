package battleship.domain;

import java.awt.*;

public class Player {

    // evt Lav Player som superclass med computer og menneske som underklasse evt?
    String name;
    boolean human;
    Ship[] ship = new Ship[BattleshipGame.noOfShips];
    Board grid = new Board();

    // computer ai!
    int aiStatus = 0;
    Point lastHit;

    // DISSE KAN LAVES FINAL STATIC?
    /*
     * aiStatus = 0: intet truffet - random skud
     * aiStatus = 1: skib truffet sidste skud
     * aiStatus = 2: skib vertikal - skyd op
     * aiStatus = 3: skib vertikal - skyd ned
     * aiStatus = 4; skib horisontal - skyd venstre
     * aiStatus = 5: skib horisontal - skyd højre
     */

    public Player(boolean human) {
        this.human = human;

        if (human) {
            this.name = "no name";
        }

        if (!human) {
            this.name = "computer";
        }

        generateShips();
    }

    private void generateShips() {
        // generating an array of ships based on the defenitions i BattleshipGame
        int i = 0;
        for (String shipName : BattleshipGame.ships.keySet()) {
            //System.out.println("shipname " + shipName);
            ship[i] = new Ship(BattleshipGame.ships.get(shipName), shipName);
            i++;
        }
    }

    public void randomShipPlacement() {
        grid.clearBoard();
        int randomX, randomY;
        boolean randomHor;

        // løkke der gennemgår alle antalSkibe
        for (int i = 0; i < BattleshipGame.noOfShips; i++) {
            boolean shipPlaced = false;
            while (!shipPlaced) {

                // vælger random parametre mlm 0 og 9
                randomX = (int) (Math.random() * 10);
                randomY = (int) (Math.random() * 10);
                randomHor = (boolean) (Math.random() < 0.5);
                Point randomPoint = new Point(randomX, randomY);

                // Sætter en random orientering
                ship[i].setHorizontal(randomHor);

                // Tjekker om skibet kan placers
                if (ship[i].canPlace(randomPoint, grid)) {
                    ship[i].place(grid, randomPoint, randomHor); // placerer skib
                    shipPlaced = true; // afslutter løkke
                }
            }
        }
    }

    public Point randomShot() {

        // vælger random parametre mlm 0 og 9 og returnerer som Point
        int randomX = (int) (Math.random() * 10);
        int randomY = (int) (Math.random() * 10);
        return new Point(randomX, randomY);

    }

    public Point aiShot(Board playerBoard) {

        final int UP = 0;
        final int DOWN = 1;
        final int LEFT = 2;
        final int RIGHT = 3;

        Point returnPoint = null;
        int shot;

        while (true) {
            // System.out.println("ai " + aiStatus);

            switch (aiStatus) {

                case 0: // intet ramt, tilfældigt skud
                    // System.out.println("case 0");
                    do {
                        // skyder på tilfældigt punkt
                        returnPoint = randomShot();
                        // henter værdien af tilfældigt punkt på spillers plade
                        shot = playerBoard.getValue(returnPoint);
                    } while (shot >= 2);

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

                        if (lastHit.x == 9) break;

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

                        if (lastHit.y == 9) break;

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

                    if (lastHit.x == 9) {
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

                    if (lastHit.y == 9) {
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


            // kører rund om punkt
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
        if (point.x >= 0 && 9 >= point.x &&
                point.y >= 0 && point.y <= 9) {

            if (grid.getValue(point) == 0) {
                grid.setValue(point, 4);
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

    public Board getGrid() {
        return grid;
    }

    public void setGrid(Board grid) {
        this.grid = grid;
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

    @Override
    public String toString() {
        return "name " + name + "\nhuman " + human;
    }
}
