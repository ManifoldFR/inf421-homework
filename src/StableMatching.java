import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class StableMatching implements StableMatchingInterface {

    public int[][] constructStableMatching (int[] menGroupCount, int[] womenGroupCount, int[][] menPrefs, int[][] womenPrefs) {
        int m = menGroupCount.length;
        int w = womenGroupCount.length;

        // result array to be returned
        int[][] res = new int[m][w];

        if (m==0 || w==0) {
            return res;
        } else if (m==w) {
            int n = 0;
            for(int l: menGroupCount) {
                n += l;
            }
            if (n==w) return backupAlgorithm(menGroupCount,womenGroupCount,menPrefs,womenPrefs);
        }

        // nextIndex[i][j] is true whenever the j-th group of women has rejected the men in group i
        int[] nextIndex = new int[m];

        int[][] womenPrefsRankings = new int[w][m];
        for(int j=0; j<w;j++) {
            for(int i=0; i<m; i++) {
                womenPrefsRankings[j][womenPrefs[j][i]] = i;
            }
        }

        // current worst matchings
        int[] worstMatch = new int[w];
        for(int j=0; j<w; j++) {
            worstMatch[j] = -1;
        }

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
            System.out.print(Arrays.toString(nextIndex));
            System.out.print(", ");
            */

            // get the group of women preferred by men in group i that hasn't rejected them yet
            // j is the number of this group
            int j = menPrefs[i][nextIndex[i]];
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

                // now we check if our new men from group i are the worst
                int currentWorstMatch = worstMatch[j];
                if (currentWorstMatch == -1) {
                    worstMatch[j] = i;
                } else if (womenPrefsRankings[j][i] > womenPrefsRankings[j][currentWorstMatch]) {
                    worstMatch[j] = i;
                }
                menGroupCount[i] -= numToSwitch;
                womenGroupCount[j] -= numToSwitch;

                // System.out.print("), ");
            } else {
                // already engaged women have the priority over the singles
                // find the least preferred group of men any woman in group j is currently engaged to
                // rivalIndex is the position of this group in the group of women's preference list
                /*
                int rivalIndex = 0;
                for(int k = m-1; k >= 0; k--) {
                    if(res[womenPrefs[j][k]][j] > 0) {
                        rivalIndex = k;
                        break;
                    }
                }
                */
                int rival = worstMatch[j];

                // int rival = worstMatch[j];
                int rivalIndex = womenPrefsRankings[j][rival];
                /*
                System.out.print("riv=");
                System.out.print(rival);;
                System.out.print(", rInd=");
                System.out.print(rivalIndex);
                System.out.print(", ");
                */

                // now test the rival group against our current contender:
                // if its index is farther than i's index, then it is not optimal for the women
                int iIndex = womenPrefsRankings[j][i];
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

                    res[rival][j] -= minimum(freeMen,occWomen);
                    res[i][j] += minimum(freeMen,occWomen);

                    // if all the men from the rival group have been kicked to the curb
                    // then we have to find the new worst match
                    if (res[rival][j] == 0) {
                        int indNewWorst = 0;
                        for(int k = m-1; k >= 0; k--) {
                            if(res[womenPrefs[j][k]][j] > 0) {
                                indNewWorst = k;
                                break;
                            }
                        }
                        worstMatch[j] = womenPrefs[j][indNewWorst];
                    }
                } else { nextIndex[i]++; }
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

    private int[][] backupAlgorithm(int[] menGroupCount, int[] womenGroupCount, int[][] menPrefs,int[][] womenPrefs) {
        int n = menPrefs.length;
        int[][] res = new int[n][n];

        int[] match = new int[n];

        int[] nextIndex = new int[n];

        LinkedList<Integer> freeMen = new LinkedList<>();
        for(int k = 0; k < n; k++) {
            freeMen.add(k);
        }

        int[][] womenPrefsRankings = new int[n][n];
        for(int j=0; j<n;j++) {
            for(int i=0; i<n; i++) {
                womenPrefsRankings[j][womenPrefs[j][i]] = i;
            }
        }

        int i;
        while (!freeMen.isEmpty()) {
            i = freeMen.getFirst();
            int j = menPrefs[i][nextIndex[i]];

            if (womenGroupCount[j] > 0) {
                womenGroupCount[j] = 0;
                match[j] = i;
                freeMen.poll();
                res[i][j] = 1;
            } else {
                int rival = match[j];

                int rivalIndex = womenPrefsRankings[j][rival];
                int iIndex = womenPrefsRankings[j][i];

                if (rivalIndex > iIndex) {
                    res[i][j] = 1;
                    res[rival][j] = 0;
                    match[j] = i;
                    freeMen.poll();
                    freeMen.add(rival);
                } else {
                    nextIndex[i]++;
                }
            }

        }


        return res;
    }

    private int maxOfArray(int[] data) {
        int ind = 0;
        for(int j=0; j < data.length; j++)
            if (data[j] > data[ind]) { ind = j; }
        return ind;
    }

    private int minimum(int x,int y) {
        if (x <= y) {
            return x;
        } else return y;
    }
}
