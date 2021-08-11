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
    Point firstHit;


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

        // TODO laves om til CASE ...

        // intet ramt, tilfældigt skud
        if (aiStatus == 0) {
            // random shot

            int shotValue = 4;
            // skyder på tilfældigt punkt
            while (shotValue >= 2) { // løkke så længe punktet  er skudt på før.
                // henter værdien af tilfældigt punkt på spillers plade
                returnPoint = randomShot();
                shotValue = playerBoard.getValue(returnPoint);
            }

            return returnPoint;
        }


        /*
         * aiTraef = 0: intet truffet - random skud
         * aiTraef = 1: skib truffet sidste skud // DISSE KAN LAVES FINAL STATIC?
         * aiTraef = 2: skib vertikal - skyd op
         * aiTraef = 3: skib vertikal - skyd ned
         * aiTraef = 4; skib horisontal - skyd venstre
         * aiTraef = 5: skib horisontal - skyd højre
         */

        while (true) {

            if (aiStatus == 1) { // skib lige ramt!

                // skyder i tilfældig retning ud fra hit!
                int randomSkud = (int) (Math.random() * 4);


                // hvis op valgt og det er tilladt
                // hvis korrekt valg, skal vi ændre ai, og hvis den får et ramt, kører den videre i samme retning

                if (randomSkud == UP && lastHit.x > 0) {
                    returnPoint = new Point(lastHit.x - 1, lastHit.y); // finder punkt 1 op

                    int shot = playerBoard.getValue(returnPoint); // finder værdi i 1 op (x-1)

                    if (shot < 3) { // skud ok - empty, ship eller hit!.

                        // hvis tom, bare bliv i samme status!

                        // hvis man rammer når man skyder op, er skibet vertikalt, og vi fortsætter med at skyde opad
                        if (shot == Board.SHIP) {
                            aiStatus = 2;
                        }

                        if (shot == Board.HIT) {
                            // vi har vendt fra nedad, og skal tilbage til start og nedad i stedet.
                            returnPoint = firstHit;
                            returnPoint.x = returnPoint.x + 1;
                            aiStatus = 3;
                        }

                        return returnPoint;
                    }
                }

                if (randomSkud == DOWN && lastHit.x < 9) { // hvis ned er tilladt
                    returnPoint = new Point(lastHit.x + 1, lastHit.y); // finder punkt 1 ned (x+1)
                    int shot = playerBoard.getValue(returnPoint);

                    if (shot < 3) { // skud ok

                        if (shot == Board.SHIP) {
                            // hvis man rammer når man skyder ned, er skibet vertikalt, og vi fortsætter med at skyde ned
                            aiStatus = 3;
                        }

                        if (shot == Board.HIT) {
                            // tilbage til start og opad i stedet.
                            returnPoint = firstHit;
                            returnPoint.x = returnPoint.x + 1;
                            aiStatus = 2;
                        }

                        return returnPoint;
                    }
                }

                // hvis venstre er tilladt
                if (randomSkud == LEFT && lastHit.y > 0) {
                    returnPoint = new Point(lastHit.x, lastHit.y - 1);
                    int shot = playerBoard.getValue(returnPoint);
                    if (shot < 3) {
                        if (shot == Board.SHIP) {
                            // hvis man rammer når man skyder venstre, er skibet horisontalt, og vi fortsætter med at skyde tv
                            aiStatus = 4;
                        }

                        if (shot == Board.HIT) {
                            // tilbage til start og højre i stedet.
                            returnPoint = firstHit;
                            returnPoint.x = returnPoint.y + 1;
                            aiStatus = 5;
                        }

                        return returnPoint;
                    }
                }

                if (randomSkud == RIGHT && lastHit.y < 9) { // hvis højre er tilladt
                    returnPoint = new Point(lastHit.x, lastHit.y + 1); // finder punkt 1 th (y+1)
                    int shot = playerBoard.getValue(returnPoint);
                    if (shot < 3) { // skud ok

                        if (shot == Board.SHIP) {
                            // hvis man rammer når man skyder højre, er skibet horisontalt, og vi fortsætter med at skyde th
                            aiStatus = 5;
                        }

                        if (shot == Board.HIT) {
                            // tilbage til start og højre i stedet.
                            returnPoint = firstHit;
                            returnPoint.x = returnPoint.y - 1;
                            aiStatus = 4;
                        }
                        return returnPoint;
                    }
                }
            }


            // skib v -  sidste træf var op - FÆRDIG
            if (aiStatus == 2) {
                if (lastHit.x == 0) aiStatus = 3; // vi er øverst på pladen, prøv ned i stedet.

                returnPoint = new Point(lastHit.x - 1, lastHit.y); // finder punkt 1 op

                int shot = playerBoard.getValue(returnPoint); // finder værdi i 1 op (x-1)

                if (shot == Board.EMPTY) {
                    aiStatus = 3; // missed skud, prøv nedad næste gang
                    return returnPoint;
                }

                if (shot == Board.SHIP) { // fortsætte med at ramme næste skud i denne retning
                    return returnPoint;
                }

                if (shot == Board.HIT) { // hvis vi rammer et flet der allerede er ramt, er det fordi vi har vendt om og skal fortsætte i samme retning
                    lastHit.x = lastHit.x - 1;
                }

                if (shot == Board.MISS || shot == Board.AI) { // tidligere skudt, eller ikke en mulighed, prøv nedad!
                    aiStatus = 3;
                }
            }


            if (aiStatus == 3) { // skib v -  sidste træf var ned

                if (lastHit.x == 9) aiStatus = 2;
                // vi er nederst på pladen, prøv op i stedet.

                returnPoint = new Point(lastHit.x + 1, lastHit.y); // finder punkt 1 ned
                int shot = playerBoard.getValue(returnPoint); // finder værdi i 1 ned (x+1)

                if (shot == Board.EMPTY) {
                    aiStatus = 2; // missed skud, prøv opad næste gang
                    return returnPoint;
                }

                if (shot == Board.SHIP) { // fortsætte med at ramme næste skud i denne retning
                    return returnPoint;
                }

                if (shot == Board.HIT) { // hvis vi rammer et flet der allerede er ramt, er det fordi vi har vendt om og skal fortsætte i samme retning
                    lastHit.x = lastHit.x + 1;
                }

                if (shot == Board.MISS || shot == Board.AI) { // tidligere skudt, eller ikke en mulighed, prøv opdad!
                    aiStatus = 2;
                }
            }

            if (aiStatus == 4) { // skib h -  sidste træf var venstre
                if (lastHit.y == 0) aiStatus = 5; // vi er tv på pladen, prøv højre i stedet.

                returnPoint = new Point(lastHit.x, lastHit.y - 1); // finder punkt 1 tv
                int shot = playerBoard.getValue(returnPoint); // finder værdi i 1 tv (y-1)

                if (shot == Board.EMPTY) {
                    aiStatus = 5; // missed skud, prøv th næste gang
                    return returnPoint;
                }

                if (shot == Board.SHIP) { // fortsætte med at ramme næste skud i denne retning
                    return returnPoint;
                }

                if (shot == Board.HIT) { // hvis vi rammer et flet der allerede er ramt, er det fordi vi har vendt om og skal fortsætte i samme retning
                    lastHit.y = lastHit.y - 1;
                }

                if (shot == Board.MISS || shot == Board.AI) { // tidligere skudt, eller ikke en mulighed, prøv th!
                    aiStatus = 5;
                }
            }

            // skib h -  sidste træf var højre
            if (aiStatus == 5) {
                if (lastHit.y == 9) aiStatus = 4; // vi er th på pladen, prøv venstre i stedet.

                returnPoint = new Point(lastHit.x, lastHit.y + 1); // finder punkt 1 th

                int shot = playerBoard.getValue(returnPoint); // finder værdi i 1 th (y+1)

                if (shot == Board.EMPTY) {
                    aiStatus = 4; // missed skud, prøv th næste gang
                    return returnPoint;
                }

                if (shot == Board.SHIP) { // fortsætte med at ramme næste skud i denne retning
                    return returnPoint;
                }

                if (shot == Board.HIT) { // hvis vi rammer et flet der allerede er ramt, er det fordi vi har vendt om og skal fortsætte i samme retning
                    lastHit.y = lastHit.y + 1;
                }

                if (shot == Board.MISS || shot == Board.AI) { // tidligere skudt, eller ikke en mulighed, prøv th!
                    aiStatus = 5;
                }
            }
        }   // slutpå while løkke
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

    public Point getFirstHit() {
        return firstHit;
    }

    public void setFirstHit(Point firstHit) {
        this.firstHit = firstHit;
    }

    @Override
    public String toString() {
        return "name " + name + "\nhuman " + human;
    }
}
