import java.util.Random;
import java.util.Scanner;

public class Main {
    static final String[][] gui = new String[3][3];
    static final String varX = "x";
    static final String varO = "\u00F8";
    static Scanner sc = new Scanner(System.in);
    static String respuesta_terminate = "";
    static int respuesta;
    static int[] tempPosicionU = new int[2];
    static int[] tempPosicionM = new int[2];
    static int contadorTurnos;
    static boolean amenaza;
    static boolean fin,ganar;
    static int ocurrenciasO;
    static Random rd = new Random();


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        fill();
        do {
            fin = false;
            System.out.println("fallo");
            representarTablero();
            System.out.print("Indique una posición > ");
            respuesta = sc.nextInt();
            if (respuesta > 0 && respuesta < 10 && check(devolverPosicion(respuesta))) {
                modificarTableroU();
                if (contadorTurnos > 4) {
                    mostrarResultado();
                    System.out.println("Ha sido empate");
                    terminarPartida();
                } else if (!fin) {
                    modificarTableroM();
                }
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
            gui[tempPosicionM[0]][tempPosicionM[1]]=varO;
            ganar=false;
        }

        else if (amenaza) {
            gui[tempPosicionM[0]][tempPosicionM[1]] = varO;
            amenaza = false;
        } else {
            if (contadorTurnos <= 1) {
                switch (respuesta) {
                    case 1, 3, 7, 9, 2, 4, 6, 8 -> gui[1][1] = varO;
                    default -> {
                        int[] array = {1, 3, 7, 9};
                        int[] arrayTemp = devolverPosicion(array[rd.nextInt(4)]);
                        gui[arrayTemp[0]][arrayTemp[1]] = varO;
                    }
                }
            } else {
                int[] array = devolverLugar();
                assert array != null;
                gui[array[0]][array[1]] = varO;

            }
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
            fin = true;
            contadorTurnos = 0;
            fill();
        } else {
            System.exit(0);
        }
    }


    static void defensa(String[] tempArray, int fila, int x) {
        ocurrenciasO=0;
        int contador = 0;
        int columna = 0;
        for (int i = 0; i < tempArray.length; i++) {
            if (tempArray[i].equals(varX)) {
                contador++;
            } else if (tempArray[i].equals(varO)) {
                ocurrenciasO++;
                contador--;
            } else {
                if (fila == -1 && x == 3) {
                    fila = i;
                    columna = Math.abs(i - 2);
                } else if (fila == 1 && x == 3) {
                    fila = i;
                    columna = i;
                } else {
                    columna = i;
                }
            }
        }
        if (ocurrenciasO == 2){
            System.out.println("Posicion ganar x: "+fila+ " y: "+columna);
            if (x == 0) {
                System.out.println("1 Solucionar en x: " + fila + " y " + columna);
                tempPosicionM[0] = fila;
                tempPosicionM[1] = columna;
            } else if (x == 1) {
                System.out.println("2 Solucionar en x: " + columna + " y " + fila);
                tempPosicionM[0] = columna;
                tempPosicionM[1] = fila;
            } else {
                System.out.println("3 Solucionar en x: " + columna + " y " + fila);
                tempPosicionM[0] = fila;
                tempPosicionM[1] = columna;
            }
            ganar = true;
        }
       else  if (contador == 2) {
            if (x == 0) {
                System.out.println("1 Solucionar en x: " + fila + " y " + columna);
                tempPosicionM[0] = fila;
                tempPosicionM[1] = columna;
            } else if (x == 1) {
                System.out.println("2 Solucionar en x: " + columna + " y " + fila);
                tempPosicionM[0] = columna;
                tempPosicionM[1] = fila;
            } else {
                System.out.println("3 Solucionar en x: " + columna + " y " + fila);
                tempPosicionM[0] = fila;
                tempPosicionM[1] = columna;
            }
            System.out.println("amenaza");
            amenaza = true;
        }


    }


    static boolean comprobarFyC(String simbolo) {
        String[] temp = new String[3];
        int valorPrimero, valorSegundo, incrementer = 1, k_ = 0;
        for (int x = 0; x < 2; x++) {
            for (int i = 0; i < gui.length; i++) {
                for (int j = 0, contTemp = 0; j < gui[i].length; j++) {
                    if (x > 0) {
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
                defensa(temp, i, x);
            }
            for (int z = 0, k = k_, cont = 0; z < gui.length; z++, k += incrementer) {
                temp[z] = gui[z][k];
                if (gui[z][k].equals(simbolo)) {
                    cont++;
                    if (cont > 2) return true;
                }
            }
            defensa(temp, incrementer, 3);
            k_ = 2;
            incrementer = -1;
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
        } else if (simbolo.equalsIgnoreCase(varO)) {
            System.out.println("ocurre");
            representarTablero();
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