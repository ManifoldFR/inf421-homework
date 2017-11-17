import java.util.*;

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
            if (n==w) return backupAlgorithm(womenGroupCount,menPrefs,womenPrefs);
        }

        // nextIndex[i][j] is true whenever the j-th group of women has rejected the men in group i
        int[] nextIndex = new int[m];

        int[][] womenPrefsRankings = new int[w][m];
        for(int j=0; j<w;j++) {
            for(int i=0; i<m; i++) {
                womenPrefsRankings[j][womenPrefs[j][i]] = i;
            }
        }

        // current matchings: match[j] contains a priority queue of the indices of the men married to women group j
        PriorityQueue<Integer>[] match = new PriorityQueue[w];
        for(int j=0; j<w; j++) {
            match[j] = new PriorityQueue<>(m, Collections.reverseOrder());
        }

        LinkedList<Integer> celibs = new LinkedList<>();
        for (int i = 0; i < m; i++) {
            celibs.add(i);
        }

        /*
        System.out.print("(");
        System.out.print(i);
        System.out.print(", ");
        System.out.print(Arrays.toString(menGroupCount));
        System.out.print(", ");
        System.out.print(Arrays.toString(womenGroupCount));
        System.out.print(", ");
        */
        int i;
        int freeMen;

        // number of single women
        int freeWomen;

        while (!celibs.isEmpty()) {
            i = celibs.getFirst();
            freeMen = menGroupCount[i];
            // best women preferred by men i that haven't rejected them yet
            int j = menPrefs[i][nextIndex[i]];

            int iIndex = womenPrefsRankings[j][i];

            // get the number of free women in group j
            freeWomen = womenGroupCount[j];

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
                // add a matching if no men and women from i and j were married before
                if (res[i][j] == 0) match[j].add(iIndex);

                res[i][j] += numToSwitch;

                menGroupCount[i] -= numToSwitch;
                womenGroupCount[j] -= numToSwitch;

                if (menGroupCount[i] == 0) {
                    celibs.poll();
                }
            } else {
                // find the worst group of men women in group j are engaged to
                int rivalIndex = match[j].peek();
                int rival = womenPrefs[j][rivalIndex];

                // compare the rival group against i
                if (rivalIndex > iIndex) {
                    // if the rival group is not as good as our current one, we make some women switch1
                    int occWomen = res[rival][j];
                    menGroupCount[i] -= minimum(freeMen,occWomen);
                    if (menGroupCount[i] == 0) {
                        celibs.poll();
                    }
                    if (res[i][j] == 0) {
                        match[j].add(iIndex);
                    }

                    res[rival][j] -= minimum(freeMen,occWomen);
                    if (res[rival][j] == 0) {
                        match[j].poll();
                    }
                    // if the rival wasn't single before... now it is
                    if (menGroupCount[rival] == 0) {
                        celibs.add(rival);
                    }
                    menGroupCount[rival] += minimum(freeMen,occWomen);

                    res[i][j] += minimum(freeMen,occWomen);
                } else { nextIndex[i]++; }

            }

        }
        return res;
    }

    private int[][] backupAlgorithm(int[] womenGroupCount, int[][] menPrefs,int[][] womenPrefs) {
        int n = menPrefs.length;
        int[][] res = new int[n][n];

        int[] match = new int[n];

        int[] nextIndex = new int[n];

        LinkedList<Integer> celibs = new LinkedList<>();
        for(int k = 0; k < n; k++) {
            celibs.add(k);
        }

        int[][] womenPrefsRankings = new int[n][n];
        for(int j=0; j<n;j++) {
            for(int i=0; i<n; i++) {
                womenPrefsRankings[j][womenPrefs[j][i]] = i;
            }
        }

        int i;
        while (!celibs.isEmpty()) {
            i = celibs.getFirst();
            int j = menPrefs[i][nextIndex[i]];

            if (womenGroupCount[j] > 0) {
                womenGroupCount[j] = 0;
                match[j] = i;
                celibs.poll();
                res[i][j] = 1;
            } else {
                int rival = match[j];

                int rivalIndex = womenPrefsRankings[j][rival];
                int iIndex = womenPrefsRankings[j][i];

                if (rivalIndex > iIndex) {
                    res[i][j] = 1;
                    res[rival][j] = 0;
                    match[j] = i;
                    celibs.poll();
                    celibs.add(rival);
                } else {
                    nextIndex[i]++;
                }
            }

        }


        return res;
    }

    private static int minimum(int x,int y) {
        if (x <= y) {
            return x;
        } else return y;
    }
}
