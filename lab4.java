public class lab4 {

    static boolean[] find(Graph g){
        int tempSize = g.size() - 1;
        int i = 0;
        int j = 0;
        Graph g1 = g;
        boolean [] isInIndependentSet = new boolean[g.size()];

        while(tempSize >= 0){
            if(g1.remove(i).has(g.size())){
                g1 = g1.remove(i);
                isInIndependentSet[j] = true;
                j++;
            }else
                i++;
            tempSize --;
        }
        return isInIndependentSet;
    }
}
