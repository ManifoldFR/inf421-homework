import java.util.Arrays;

public class StableMatching implements StableMatchingInterface {

    public int[][] constructStableMatching (int[] menGroupCount, int[] womenGroupCount, int[][] menPrefs, int[][] womenPrefs) {
        int m = menGroupCount.length;
        int w = womenGroupCount.length;

        // result array to be returned
        int[][] res = new int[m][w];

        if (m==0 || w==0) return res;

        // rejectedBy[i][j] is true whenever the j-th group of women has rejected the men in group i
        boolean[][] rejectedBy = new boolean[m][w];

        // number of single men; we initialise at the biggest number
        int i = maxOfArray(menGroupCount);
        int freeMen = menGroupCount[i];

        /*
        System.out.print("(");
        System.out.print(i);
        System.out.print(", ");
        System.out.print(Arrays.toString(menGroupCount));
        System.out.print(", ");
        System.out.print(Arrays.toString(womenGroupCount));
        System.out.print(", ");
        */

        // number of single women
        int freeWomen;

        while (freeMen > 0) {
            /*
            System.out.print("seen=");
            System.out.print(Arrays.deepToString(rejectedBy));
            System.out.print(", ");
            */

            // get the group of women preferred by men in group i that hasn't rejected them yet
            // j is the number of this group
            int j = firstSuchThat(menPrefs[i],rejectedBy[i]);
            /*
            System.out.print("j=");
            System.out.print(j);
            System.out.print(", ");
            */

            // get the number of free women in group j
            freeWomen = womenGroupCount[j];
            // get the number of free men left
            freeMen = menGroupCount[i];

            int numToSwitch = minimum(freeMen,freeWomen);

            /*
            System.out.print("fM=");
            System.out.print(freeMen);
            System.out.print(", nSw=");
            System.out.print(numToSwitch);
            System.out.print(", ");
            */

            // now, if there are free women, set them up with as many of the free men in group i as possible
            if (freeWomen > 0) {
                res[i][j] += numToSwitch;
                menGroupCount[i] -= numToSwitch;
                womenGroupCount[j] -= numToSwitch;

                // System.out.print("), ");
            } else {
                // already engaged women have the priority over the singles
                // find the least preferred group of men any woman in group j is currently engaged to
                // rivalIndex is the position of this group in the group of women's preference list
                int rivalIndex = 0;
                for(int k = 0; k < m; k++) {
                    if(res[womenPrefs[j][k]][j] > 0) {
                        rivalIndex = k;
                    }
                }
                int rival = womenPrefs[j][rivalIndex];

                /*
                System.out.print("riv=");
                System.out.print(rival);;
                System.out.print(", rInd=");
                System.out.print(rivalIndex);
                System.out.print(", ");
                */

                // now test the rival group against our current contender:
                // if its index is farther than i's index, then it is not optimal for the women
                int iIndex = searchFor(i, womenPrefs[j]);
                if (rivalIndex > iIndex) {
                /* if the rival group is not as good as our current one,
                make as many ladies engaged to them as possible switch */
                    int occWomen = res[rival][j];

                    /*
                    System.out.print(occWomen);
                    System.out.print(", ");
                    */

                    menGroupCount[rival] += minimum(freeMen,occWomen);
                    menGroupCount[i] -= minimum(freeMen,occWomen);
                    rejectedBy[rival][j] = true;

                    res[rival][j] -= minimum(freeMen,occWomen);
                    res[i][j] += minimum(freeMen,occWomen);
                } else { rejectedBy[i][j] = true; }
                // System.out.print("),");

            }
            // Preparing for next loop: Get the group of men with the most singles
            i = maxOfArray(menGroupCount);

            /*
            System.out.print("(");
            System.out.print(i);
            System.out.print(", ");
            System.out.print(Arrays.toString(menGroupCount));
            System.out.print(", ");
            System.out.print(Arrays.toString(womenGroupCount));
            System.out.print(", ");
            */
            freeMen = menGroupCount[i];
        }
        return res;
    }

    private int maxOfArray(int[] data) {
        int ind = 0;
        for(int j=0; j < data.length; j++)
            if (data[j] > data[ind]) { ind = j; }
        return ind;
    }

    /*
     * Returns the first datapoint of array data that verifies the
     * contraint constr
     */
    private int firstSuchThat(int[] data, boolean[] constr) {
        for(int j=0; j < constr.length; j++) {
            if (!constr[data[j]]) return data[j];
        }
        return constr.length-1;
    }

    private int minimum(int x,int y) {
        if (x <= y) {
            return x;
        } else return y;
    }

    private int searchFor(int i,int[] data) {
        for(int j=0; j<data.length; j++) {
            if (data[j]==i) {
                return j;
            }
        }
        return -1;
    }
}
