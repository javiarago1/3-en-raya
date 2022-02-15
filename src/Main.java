import java.util.Scanner;

public class Main {

    static final String[][] gui = new String[3][3];
    static final String varX ="x";
    static final String varO = "\u00F8";
    static Scanner sc = new Scanner(System.in);
    static String respuesta_terminate = "";
    static int respuesta;
    static int[] tempPosicionU = new int[2];
    static int[] tempPosicionM = new int[2];
    static int[] tempPosicionG = new int[2];
    static int contadorTurnos;
    static boolean amenaza;
    static boolean fin, ganar;

    public static void main(String[] args) {
        fill();
        do {
            fin = false;
            representarTablero();
            do {
                System.out.print("Indique una posición > ");
                while (!sc.hasNextInt()) {
                    System.out.print("Indique una posición > ");
                    sc.next();
                }
                respuesta = sc.nextInt();
            } while (respuesta <= 0 || respuesta >= 10 || !check(devolverPosicion(respuesta)));
            modificarTableroU();
            if (!fin) {
                modificarTableroM();
            }

        } while (!respuesta_terminate.equalsIgnoreCase("N"));
    }

    static int[] devolverLugar() {
        for (int i = 0; i < gui.length; i++) {
            for (int j = 0; j < gui[i].length; j++) {
                if (!gui[i][j].equals(varO) && !gui[i][j].equals(varX)) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    static void modificarTableroM() {
        if (ganar) {
            gui[tempPosicionG[0]][tempPosicionG[1]] = varO;
            ganar = false;
        } else if (amenaza) {
            gui[tempPosicionM[0]][tempPosicionM[1]] = varO;
            amenaza = false;
        } else if (contadorTurnos == 1) {
            switch (respuesta) {
                case 1, 3, 7, 9, 2, 4, 6, 8 -> gui[1][1] = varO;
                default -> gui[0][2] = varO;
            }
        } else if (contadorTurnos == 2 && gui[0][2].equals(varX) && gui[2][0].equals(varX)) {
            gui[2][1]=varO;
        } else {
            int[] array = devolverLugar();
            assert array != null;
            gui[array[0]][array[1]] = varO;
        }
        comprobarGanador(varO);
    }

    static boolean check(int[] respuesta) {
        return !gui[respuesta[0]][respuesta[1]].equals(varO) && !gui[respuesta[0]][respuesta[1]].equals(varX);
    }

    static void terminarPartida() {
        System.out.println("¿Quieres seguir jugando? (S/N)");
        System.out.print("---> ");
        respuesta_terminate = sc.next();

        if (!respuesta_terminate.equals("N")) {
            fin = true;
            contadorTurnos = 0;
            amenaza = false;
            fill();
        }
    }

    static void defensa(String[] tempArray, int fila, int x, String simbolo) {
        if (simbolo.equals(varX)) {
            int ocurrenciasO = 0;
            int ocurrenciasX = 0;
            int columna = 0;
            for (int i = 0; i < tempArray.length; i++) {
                if (tempArray[i].equals(varX)) {
                    ocurrenciasO--;
                    ocurrenciasX++;
                } else if (tempArray[i].equals(varO)) {
                    ocurrenciasO++;
                    ocurrenciasX--;
                } else if (fila == -1 && x == 3) {
                    fila = i;
                    columna = Math.abs(i - 2);
                } else if (fila == 1 && x == 3) {
                    fila = i;
                    columna = i;
                } else {
                    columna = i;
                }
            }
            if (ocurrenciasO == 2) {
                solucionMaquina(x, fila, columna, tempPosicionG);
                ganar = true;
            } else if (ocurrenciasX == 2) {
                solucionMaquina(x, fila, columna, tempPosicionM);
                amenaza = !ganar;
            }
        }
    }

    static void solucionMaquina(int x, int fila, int columna, int[] array) {
        if (x == 1) {
            array[0] = columna;
            array[1] = fila;
        } else {
            array[0] = fila;
            array[1] = columna;
        }
    }


    static boolean comprobarFyC(String simbolo) {
        String[] temp = new String[3];
        int valP, valS, inc = 1, s = 0;
        for (int x = 0; x < 2; x++) {
            for (int i = 0; i < gui.length; i++) {
                for (int j = 0, contTemp = 0; j < gui[i].length; j++) {
                    if (x > 0) {
                        valP = j;
                        valS = i;
                    } else {
                        valP = i;
                        valS = j;
                    }
                    temp[j] = gui[valP][valS];
                    if (gui[valP][valS].equals(simbolo)) {
                        contTemp++;
                        if (contTemp > 2) {
                            return true;
                        }
                    }
                }
                defensa(temp, i, x, simbolo);
            }
            for (int z = 0, k = s, contTemp = 0; z < gui.length; z++, k += inc) {
                temp[z] = gui[z][k];
                if (gui[z][k].equals(simbolo)) {
                    contTemp++;
                    if (contTemp > 2) return true;
                }
            }
            defensa(temp, inc, 3, simbolo);
            inc = -1;
            s = 2;
        }
        return false;
    }

    static void mostrarResultado() {
        System.out.println("<---- Resultado de partida ---->");
        representarTablero();
        System.out.println("<---- Resultado de partida ---->");
    }

    static void comprobarGanador(String simbolo) {
        if (contadorTurnos == 5) {
            mostrarResultado();
            System.out.println("Ha empatado contra la máquina, no esta nada mal, si la ganas te regalo 1 BTC");
            terminarPartida();
        } else if (comprobarFyC(simbolo)) {
            mostrarResultado();
            System.out.println("Ha ganado el jugador --> " + simbolo);
            if (simbolo.equals(varO))
                System.out.println("Te ha ganado una máquina, yo me sentiría mal conmigo mismo...");
            else System.out.println("No se como lo has hecho pero acabas de ganar a la máquina");
            terminarPartida();
        }
    }

    static int[] devolverPosicion(int respuesta) {
        int x, y;
        if (respuesta <= 3) {
            x = 0;
            y = respuesta - 1;
        } else if (respuesta <= 6) {
            x = 1;
            y = respuesta - 4;
        } else {
            x = 2;
            y = respuesta - 7;
        }
        tempPosicionU[0] = x;
        tempPosicionU[1] = y;
        return new int[]{x, y};
    }

    static void modificarTableroU() {
        gui[tempPosicionU[0]][tempPosicionU[1]] = varX;
        contadorTurnos++;
        comprobarGanador(varX);
    }

    static void fill() {
        for (int i = 0, contador = 0; i < gui.length; i++) {
            for (int j = 0; j < gui[i].length; j++) {
                contador++;
                gui[i][j] = String.valueOf(contador);
            }
        }
    }

    static void representarTablero() {
        for (String[] strings : gui) {
            for (int j = 0; j < strings.length; j++) {
                System.out.print("|" + strings[j]);
                if (j == strings.length - 1) System.out.print("|");
            }
            System.out.println();
        }
    }
}