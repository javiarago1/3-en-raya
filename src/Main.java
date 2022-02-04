import java.util.Random;
import java.util.Scanner;

public class Main {
    static final String[][] gui = new String[3][3];
    static final String varX = "x";
    static final String varO = "\u00F8";
    static int contadorX;
    static Scanner sc = new Scanner(System.in);
    static String respuesta_terminate="";
    static Random r = new Random();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int respuesta;
        fill();
        do {
            representarTablero();
            System.out.print("Indique una posición > ");
            respuesta = sc.nextInt();
            if (respuesta > 0 && respuesta < 10) {
                modificarTableroU(respuesta);
                representarTablero();
                modificarTableroM();
            }
        } while (!respuesta_terminate.equals("N"));
    }

    static void terminarPartida() {
        System.out.println("¿Quieres seguir jugando? (S/N)");
        respuesta_terminate = sc.next();
        if (!respuesta_terminate.equals("N"))fill();
    }

    static boolean comprobarFyC(String simbolo) {
        int valorPrimero, valorSegundo, incrementer = 1, k_ = 0;
        boolean cambio = false;
        for (int x = 0; x < 2; x++) {
            for (int i = 0; i < gui.length; i++) {
                for (int j = 0, contTemp = 0; j < gui[i].length; j++) {
                    for (int z = 0, k = k_, cont = 0; z < gui.length; z++, k += incrementer) {
                        if (gui[z][k].equals(simbolo)) {
                            cont++;
                            if (cont > 2) return true;
                        }
                    }
                    if (cambio) {
                        valorPrimero = j;
                        valorSegundo = i;
                    } else {
                        valorPrimero = i;
                        valorSegundo = j;
                    }
                    if (gui[valorPrimero][valorSegundo].equals(simbolo)) {
                        contTemp++;
                        if (contTemp > 2) return true;
                    }
                }
            }
            k_ = 2;
            incrementer = -1;
            cambio = true;
        }
        return false;
    }

    static void mostrarResultado(){
        System.out.println("<---- Resultado de partida ---->");
        representarTablero();
        System.out.println("<---- Resultado de partida ---->");
    }

    static void comprobarGanador(String simbolo) {
        if (contadorX > 2) {
            if (comprobarFyC(simbolo)) {
                mostrarResultado();
                System.out.println("Ha ganado el jugador --> "+simbolo);
                terminarPartida();
                contadorX = 0;
            }
        }

    }

    static void modificarTableroM() {
        boolean posible;
        do {
            int valor_a = r.nextInt(3-1)+1;
            int valor_b = r.nextInt(3-1)+1;
            if (gui[valor_a][valor_b].equals(varX) || gui[valor_a][valor_b].equals(varO)){
                posible=false;
            }
            else {
                posible=true;
                gui[valor_a][valor_b]=varO;
            }
        } while (!posible);
        comprobarGanador(varO);

    }

    static void modificarTableroU(int respuesta) {
        if (respuesta <= 3) {
            gui[0][respuesta - 1] = Main.varX;
        } else if (respuesta <= 6) {
            gui[1][respuesta - 4] = Main.varX;
        } else {
            gui[2][respuesta - 7] = Main.varX;
        }
        contadorX++;
        comprobarGanador(Main.varX);
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