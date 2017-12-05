import java.util.*;

public class EditDistance implements EditDistanceInterface {
     
    int c_i, c_d, c_r;
    static int MAX = Integer.MAX_VALUE;
    static int UNDEF = -1;

    public EditDistance (int c_i, int c_d, int c_r) {
        this.c_i = c_i;
        this.c_d = c_d;
        this.c_r = c_r;
    }
        
    public int[][] getEditDistanceDP(String s1, String s2) {
        int n=s1.length(),m=s2.length();
        int[][] res = initializeResult(n,m);

        for (int j=0; j<m+1; j++) res[0][j] = j*this.c_i;
        for (int i=0; i<n+1; i++) res[i][0] = i*this.c_d;

        for (int i=1; i<n+1; i++) {
            for (int j=1; j<m+1; j++) {
                int first_arg = res[i-1][j-1];
                if (s1.charAt(i-1) != s2.charAt(j-1)) {
                    first_arg += this.c_r;
                }
                res[i][j] = Math.min(first_arg,
                        Math.min(res[i-1][j]+this.c_d,
                                res[i][j-1]+this.c_i));
            }
        }

        return res;
    }

    private int[][] initializeResult(int length1, int length2) {
        int[][] res = new int[length1+1][length2+1];
        for (int i = 0; i< length1+1; i++) {
            for (int j = 0; j< length2+1; j++) {
                res[i][j] = EditDistance.UNDEF;
            }
        }
        return res;
    }

    public List<String> getMinimalEditSequence(String s1, String s2) {
        LinkedList<String> ls = new LinkedList<> ();
        int[][] ed = getEditDistanceDP(s1,s2);
        int n=s1.length(),m=s2.length();

        int i=n, j=m;
        while ((i > 0) || (j > 0)) {
            if (i > 0 && ed[i][j] == ed[i-1][j]+c_d) {
                i--;
                String op = "delete("+i+")";
                ls.add(op);
            } else if (j > 0 && ed[i][j] == ed[i][j-1]+c_i) {
                String op = "insert(" + String.valueOf(i) + "," + s2.charAt(j-1) + ")";
                ls.add(op);
                j--;
            } else {
                if (s1.charAt(i-1) != s2.charAt(j-1)) {
                    String op = "replace(" + String.valueOf(i - 1) + "," + s2.charAt(j - 1) + ")";
                    ls.add(op);
                }
                i--;
                j--;
            }
        }
        return ls;
    }
};
