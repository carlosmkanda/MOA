import com.sun.org.apache.xpath.internal.SourceTree;

import javax.sound.midi.Soundbank;
import java.util.*;

class Main {

    static int matrizDesejada[][] = {{1,2,3,4},{12,13,14,5},{11,0,15,6},{10,9,8,7}};

    public static Boolean compareMatrix(byte matriz[][]){
        for(int i = 0; i <= 3; i++){
            for(int j = 0; j <= 3; j++){
                if(matriz[i][j] != matrizDesejada[i][j]){
                    return false;
                }
            }
        }
        return true;
    }

    public static Node aboveChildren(Node node, int x, int y){ // x e y representam as posições onde há o espaço em branco no pai

        byte matriz[][] = new byte[4][4];
        int newG = node.g + 1;

        for(int i = 0; i <= 3; i++){
            for(int j = 0; j <= 3; j++){
                matriz[i][j] = node.matriz[i][j];
            }
        }

        matriz[x][y] = matriz[x-1][y];
        matriz[x-1][y] = 0;

        Node childrenNode = new Node(newG, matriz);
        return childrenNode;
    }

    public static Node belowChildren(Node node, int x, int y){ // x e y representam as posições onde há o espaço em branco no nó pai

        byte matriz[][] = new byte[4][4];
        int newG = node.g + 1;

        for(int i = 0; i <= 3; i++){
            for(int j = 0; j <= 3; j++){
                matriz[i][j] = node.matriz[i][j];
            }
        }

        matriz[x][y] = matriz[x+1][y];
        matriz[x+1][y] = 0;

        Node childrenNode = new Node(newG, matriz);
        return childrenNode;
    }

    public static Node leftChildren(Node node, int x, int y){ // x e y representam as posições onde há o espaço em branco no nó pai

        byte matriz[][] = new byte[4][4];
        int newG = node.g + 1;

        for(int i = 0; i <= 3; i++){
            for(int j = 0; j <= 3; j++){
                matriz[i][j] = node.matriz[i][j];
            }
        }

        matriz[x][y] = matriz[x][y-1];
        matriz[x][y-1] = 0;

        Node childrenNode = new Node(newG, matriz);
        return childrenNode;
    }

    public static Node rightChildren(Node node, int x, int y){ // x e y representam as posições onde há o espaço em branco no nó pai

        byte matriz[][] = new byte[4][4];
        int newG = node.g + 1;

        for(int i = 0; i <= 3; i++){
            for(int j = 0; j <= 3; j++){
                matriz[i][j] = node.matriz[i][j];
            }
        }

        matriz[x][y] = matriz[x][y+1];
        matriz[x][y+1] = 0;

        Node childrenNode = new Node(newG, matriz);
        return childrenNode;
    }

    public static ArrayList<Node> childrensOfNode(Node node){

        ArrayList<Node> childrens = new ArrayList<>();

        for(int i = 0; i <= 3; i++){
            for(int j = 0; j <= 3; j++){
                if(node.matriz[i][j] == 0){
                    if(i != 0){
                        childrens.add(aboveChildren(node, i, j));
                    }
                    if(i != 3){
                        childrens.add(belowChildren(node, i, j));
                    }
                    if(j != 0){
                        childrens.add(leftChildren(node, i, j));
                    }
                    if(j != 3) {
                        childrens.add(rightChildren(node, i, j));
                    }
                }
            }
        }
        return childrens;
    }

    public static int aStar(Node start){

        HashSet<Node> closedList = new HashSet<>();
        PriorityQueue<Node> openList = new PriorityQueue<>();
        openList.add(start);

        while(!openList.isEmpty()){

            ArrayList<Node> children;
            Node smallNode = openList.poll();

            if(closedList.contains(smallNode)){
                continue;
            }

            if(compareMatrix(smallNode.matriz)){
                return smallNode.f;
            }

            closedList.add(smallNode);
            children = childrensOfNode(smallNode);

            for(Node a : children){

                if(closedList.contains(a)) {
                    continue;
                }
                else {
                    openList.add(a);
                }
            }
        }
        return -1;
    }

    public static void main(String args[]){

        int answer;
        byte matriz[][] = new byte[4][4];

        Scanner scan = new Scanner(System.in);
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                matriz[i][j] = scan.nextByte();
            }
        }

        Node start = new Node(0, matriz);
        System.out.printf("%d\n", aStar(start));
    }
}

class Heuristics {

    static byte matrizDesejada[][] = {{1,2,3,4},{12,13,14,5},{11,0,15,6},{10,9,8,7}};

    public static int h1(byte matriz[][]){

        int counter = 0;

        for(int i = 0; i <= 3; i++){
            for(int j = 0; j <= 3; j++){
                if(matrizDesejada[i][j] != matriz[i][j] && matriz[i][j] != 0){
                    counter++;
                }
            }
        }
        return counter;
    }

    public static int h2(byte matriz[][]){

        int counter = 0;

        int vetor[] = new int[16];
        vetor[0] = matriz[0][0];
        vetor[1] = matriz[0][1];
        vetor[2] = matriz[0][2];
        vetor[3] = matriz[0][3];
        vetor[4] = matriz[1][3];
        vetor[5] = matriz[2][3];
        vetor[6] = matriz[3][3];
        vetor[7] = matriz[3][2];
        vetor[8] = matriz[3][1];
        vetor[9] = matriz[3][0];
        vetor[10] = matriz[2][0];
        vetor[11] = matriz[1][0];
        vetor[12] = matriz[1][1];
        vetor[13] = matriz[1][2];
        vetor[14] = matriz[2][2];
        vetor[15] = matriz[2][1];

        /* HF:
         *  Mudei o jeito de correr o vetor
         *  Ao inves de olhar pro elemento da frente vou olhando para o de tras
         *  Acredito que o problema era o algoritmo que estava errado
         */
        for(int i = 1; i < 16; i++){

            if((vetor[i] != vetor[i-1] + 1) && (vetor[i] != 0) && (vetor[i-1] != 0)){
                counter++;
            }
            /*
             * So uma duvida pq vc esta fazendo isso aki?
             */
            if(vetor[i] != 0){
                if(vetor[i] != vetor[i-1] + 1){
                    counter++;
                }
            }
            /*
             * Como meu codigo perdeu para isso aqui????????

             */
        }
        return counter;
    }

    public static int[] posCorreta(int posAtual){

        int ret[] = new int[2];

        for(int i = 0; i <= 3; i++){
            for(int j = 0; j <= 3; j++){
                if(matrizDesejada[i][j] == posAtual){
                    ret[0] = i;
                    ret[1] = j;
                    return ret;
                }
            }
        }
        return null;
    }

    public static int h3(byte matriz[][]){

        int total = 0;
        for(int i = 0; i <= 3; i++){
            for(int j = 0; j <= 3; j++){
                if(/*matrizDesejada[i][j] != matriz[i][j] &&*/ matriz[i][j] != 0){
                    int[] valCorretos = posCorreta(matriz[i][j]);
                    // System.out.println(matriz[i][j] + ":" + valCorretos[0] + "," + valCorretos[1]);
                    total += Math.abs(i - valCorretos[0]) + Math.abs(j - valCorretos[1]);
                }
            }
        }
        return total;
    }

    public static int h4(byte matriz[][]){

        double p1, p2, p3;
        p1 = 0.2;
        p2 = 0.3;
        p3 = 0.5;
        return (int)((p1*h1(matriz)) + (p2*h2(matriz)) + (p3*h3(matriz)));
    }

    public static int h5(byte matriz[][]){

        int maior, resultH1, resultH2, resultH3;
        resultH1 = h1(matriz);
        resultH2 = h2(matriz);
        resultH3 = h3(matriz);
        maior = (resultH1 > resultH2) ? resultH1 : resultH2;
        maior = (resultH3 > maior) ? resultH3 : maior;
        return maior;
    }
}

class Node implements Comparable<Node> {

    int g, h, f;
    byte matriz[][] = new byte[4][4];

    public Node(int g, byte matriz[][]){
        this.g = g;
        this.matriz = matriz;
        this.h = Heuristics.h2(matriz);
        this.f = g + h;
    }

    @Override
    public int compareTo(Node node){
        return this.f - node.f;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        return Arrays.deepEquals(matriz, node.matriz);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(matriz);
    }
}
