import java.util.Random;
import java.util.Scanner;
import java.util.*;

public class Main {
    static final String[][] gui = new String[3][3];
    static final String varX = "x";
    static final String varO = "\u00F8";
    static Scanner sc = new Scanner(System.in);
    static String respuesta_terminate = "";
    static int[] tempPosicion = new int[2];
    static int contadorTurnos;
    static Random r = new Random();
    static boolean amenaza;
    static boolean fin;


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int respuesta;
        fill();
        do {
            fin = false;
            representarTablero();
            System.out.print("Indique una posición > ");
            respuesta = sc.nextInt();
            if (respuesta > 0 && respuesta < 10 && check(devolverPosicion(respuesta))) {
                modificarTableroU();
                representarTablero();
                if (contadorTurnos > 4) {
                    mostrarResultado();
                    System.out.println("Ha sido empate");
                    terminarPartida();
                } else if (!fin) {
                    modificarTableroM();
                }
            }
        } while (!respuesta_terminate.equals("N"));
    }

    static int[] devolverLugar() {
        Vector vector = new Vector();
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
        if (amenaza) {
            gui[tempPosicion[0]][tempPosicion[1]] = varO;
            amenaza = false;
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
        respuesta_terminate = sc.next();
        if (!respuesta_terminate.equals("N")) {
            fin=true;
            contadorTurnos=0;
            fill();

        }
    }


    static void defensa(String[] tempArray, int fila, int x, int incrementer) {
        int contador = 0;
        int columna = 0;
        for (int i = 0; i < tempArray.length; i++) {
            if (tempArray[i].equals(varX)) {
                contador++;
            } else if (tempArray[i].equals(varO)) {
                contador--;
            } else {
                if (incrementer == -1) {
                    fila = i;
                    columna = Math.abs(i - 2);
                } else if (incrementer == 1) {
                    fila = i;
                    columna = i;
                } else {
                    columna = i;
                }
            }
        }
        if (contador == 2) {
            if (x == 0) {
                System.out.println("1 Solucionar en x: " + fila + " y " + columna);
                tempPosicion[0] = fila;
                tempPosicion[1] = columna;
            } else if (x == 1) {
                System.out.println("2 Solucionar en x: " + columna + " y " + fila);
                tempPosicion[0] = columna;
                tempPosicion[1] = fila;
            } else {
                System.out.println("3 Solucionar en x: " + columna + " y " + fila);
                tempPosicion[0] = fila;
                tempPosicion[1] = columna;
            }
            System.out.println("amenaza");
            amenaza = true;
        } else {

        }
    }

    static boolean comprobarFyC(String simbolo) {
        String[] temp = new String[3];
        int valorPrimero = 0, valorSegundo = 0, incrementer = 1, k_ = 0;
        boolean cambio = false;
        for (int x = 0; x < 2; x++) {
            for (int i = 0; i < gui.length; i++) {
                for (int j = 0, contTemp = 0; j < gui[i].length; j++) {
                    if (x>0) {
                        valorPrimero = j;
                        valorSegundo = i;
                    } else {
                        valorPrimero = i;
                        valorSegundo = j;
                    }
                    temp[j] = gui[valorPrimero][valorSegundo];
                    if (gui[valorPrimero][valorSegundo].equals(simbolo)) {
                        contTemp++;
                        if (contTemp > 2) {
                            return true;
                        }
                    }
                }
                defensa(temp, i, x, 0);
            }
            for (int z = 0, k = k_, cont = 0; z < gui.length; z++, k += incrementer) {
                temp[z] = gui[z][k];
                if (gui[z][k].equals(simbolo)) {
                    cont++;
                    if (cont > 2) return true;
                }
            }
            defensa(temp, 0, 3, incrementer);

            k_ = 2;
            incrementer = -1;
            cambio = true;
        }
        return false;
    }

    static void mostrarResultado() {
        System.out.println("<---- Resultado de partida ---->");
        representarTablero();
        System.out.println("<---- Resultado de partida ---->");
    }

    static void comprobarGanador(String simbolo) {
        if (comprobarFyC(simbolo)) {
            mostrarResultado();
            System.out.println("Ha ganado el jugador --> " + simbolo);
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
        tempPosicion[0] = x;
        tempPosicion[1] = y;
        return new int[]{x, y};
    }

    static void modificarTableroU() {
        gui[tempPosicion[0]][tempPosicion[1]] = varX;
        comprobarGanador(varX);
        contadorTurnos++;
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