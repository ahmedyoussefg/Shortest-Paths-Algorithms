package main;

import java.util.ArrayList;
import java.util.List;

public class FloydWarshall implements Algorithm {
    private int[][] floydMinimumCosts;
    private List<Integer>[][] floydAllPaths;
    private int numberOfNodes;
    private int [][] costMatrix;
    private boolean containsNegativeCycle;

    @Override
    public boolean calculateShortestPathsFromSource(int source) {
        return calculateAllPairsShortestPaths();
    }

    @Override
    public boolean calculateAllPairsShortestPaths() {
        if (this.floydMinimumCosts == null) {
            int[][] nextNode = new int[numberOfNodes][numberOfNodes];
            return floydWarshall(this.costMatrix, nextNode);
        }
        return !containsNegativeCycle;
    }

    public void constructFloydPaths(int[][] nextNode) {
        if (this.floydAllPaths != null) return;
        this.floydAllPaths=new ArrayList[numberOfNodes][numberOfNodes];
        for (int u =0;u<numberOfNodes;u++){
            for (int v = 0; v < numberOfNodes;v++){
                if (nextNode[u][v]==-1){
                    this.floydAllPaths[u][v]= new ArrayList<>();
                }
                else {
                    int temp = u;
                    List<Integer> path= new ArrayList<>();
                    path.add(u);
                    while (temp!=v){
                        temp=nextNode[temp][v];
                        path.add(temp);
                    }
                    this.floydAllPaths[u][v]=path;
                }
            }
        }
    }
    public void setupFloydMatrices(int [][] floydCostMatrix, int[][] nextNode){
        this.floydMinimumCosts=new int[numberOfNodes][numberOfNodes];
        for (int i =0;i<numberOfNodes;i++){
            for (int j =0;j<numberOfNodes;j++){
                if (i==j)
                    this.floydMinimumCosts[i][j]=0;
                else
                    this.floydMinimumCosts[i][j]=floydCostMatrix[i][j];
                if(floydCostMatrix[i][j]!= Integer.MAX_VALUE){
                    nextNode[i][j]=j;
                }
                else {
                    nextNode[i][j]=-1;
                }
            }
        }
    }
    public boolean floydWarshall(int[][] floydCostMatrix, int [][] nextNode) {
        setupFloydMatrices(floydCostMatrix,nextNode);
        for (int i = 0;i < numberOfNodes;i ++){
            for (int j = 0;j < numberOfNodes;j ++){
                if(j==i)continue;
                for (int k =0; k< numberOfNodes;k++) {
                    if (k == i) continue;
                    if (this.floydMinimumCosts[j][i] != Integer.MAX_VALUE && this.floydMinimumCosts[i][k] != Integer.MAX_VALUE
                            && (this.floydMinimumCosts[j][i] + this.floydMinimumCosts[i][k] < this.floydMinimumCosts[j][k]) ){
                        this.floydMinimumCosts[j][k] = this.floydMinimumCosts[j][i] + this.floydMinimumCosts[i][k];
                        nextNode[j][k] = nextNode[j][i];
                    }
                }
            }
        }

        constructFloydPaths(nextNode);
        /* Now detect negative cycles */

        for (int i = 0; i < numberOfNodes; i++)
            if (this.floydMinimumCosts[i][i] < 0) {
                this.containsNegativeCycle = true;
                return false;
            }
        return true;
    }
    @Override
    public void printCost(int u, int v){
        System.out.println("Minimum Path Cost Between "+ u +" and " + v
                + " is " + this.floydMinimumCosts[u][v]);
    }

    @Override
    public void setNumberOfNodes(int numberOfNodes) {
        this.numberOfNodes=numberOfNodes;
    }

    @Override
    public void setCostMatrix(int[][] costMatrix) {
        this.costMatrix=costMatrix;
    }

    @Override
    public void printPath(int u, int v){
        List<Integer> path = this.floydAllPaths[u][v];
        if (path.isEmpty()){
            System.out.println("There is no path between "+u+" and "+v);
            return;
        }
        for (int i = 0; i < path.size();i++){
            System.out.print(path.get(i));
            if (i!=path.size()-1) {
                System.out.print(" -> ");
            }
        }
        System.out.println();
    }
}
