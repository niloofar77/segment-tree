import java.util.Scanner;

public class Segment {

    static int dark[],light[],li[],hi[],lazy[];

    public  void doLazy(int pos) {
        if (lazy[pos] != -1) {
            light[pos] = lazy[pos];
            dark[pos] = lazy[pos];
            if (li[pos] != hi[pos]) {
                lazy[2*pos] = lazy[pos];
                lazy[2*pos+1] = lazy[pos];
            }
            lazy[pos] = -1;
        }
    }
    public void buildST(int LI, int HI, int pos) {
        if (LI == HI) {
            li[pos]  = LI;
            hi[pos] = HI;
            return;
        }
        buildST(LI,(LI + HI)/2,2*pos);
        buildST((LI + HI)/2+1, HI,2*pos+1);
        li[pos] = li[2*pos];
        hi[pos] = hi[2*pos+1];
    }

    public int get_darkest(int LI, int HI, int pos) {
        doLazy(pos);
        if (hi[pos] < LI || li[pos] > HI)
            return Integer.MIN_VALUE;
        if (hi[pos] <= HI && li[pos] >= LI)
            return dark[pos];
        return Math.max(get_darkest(LI,HI,2*pos),get_darkest(LI,HI,2*pos+1));
    }

    public int get_color(int pos, int index) {
        doLazy(index);
        if (li[index] == hi[index]) {
            return light[index];
        }
        if (pos <= (li[index]+hi[index])/2) {
            return get_color(pos,2*index);
        } else {
            return get_color(pos,2*index+1);
        }
    }

    public int get_lightest(int LI, int HI, int pos) {
        doLazy(pos);
        if (hi[pos] < LI || li[pos] > HI)
            return Integer.MAX_VALUE;
        if (hi[pos] <= HI && li[pos] >= LI)
            return light[pos];
        return Math.min(get_lightest(LI,HI,2*pos),get_lightest(LI,HI,2*pos+1));
    }
 public void set_color(int LI, int HI, int color, int pos) {
        doLazy(pos);
        if (hi[pos] <= HI && li[pos] >= LI) {
            light[pos] = color;
            dark[pos] = color;
            if (li[pos] != hi[pos]) {
                lazy[2*pos] = color;
                lazy[2*pos+1] = color;
            }
            return;
        }
     if (hi[pos] < LI || li[pos] > HI)
         return;
        set_color(LI,HI,color,2*pos);
        set_color(LI,HI,color,2*pos+1);
        light[pos] = Math.min(light[2*pos],light[2*pos+1]);
        dark[pos]  = Math.max(dark[2*pos],dark[2*pos+1]);
    }

    public static void main(String[] args) {
        int arraySize, Commands,color,left,right,pos;
        Scanner scanner = new Scanner(System.in);
        arraySize = scanner.nextInt();
        Commands = scanner.nextInt();
        int Segments =  (int) Math.pow(2,Math.ceil(Math.log(arraySize)/Math.log(2)));
        dark=new int[2*Segments];
        light=new int[2*Segments];
        li=new int[2*Segments];
        hi=new int[2*Segments];
        lazy=new int[2*Segments];
        Segment segment=new Segment();
        for (int i = 0; i < 2*Segments; ++i) {
            lazy[i] = -1;
        }
        segment.buildST(1,Segments,1);
        for (int i = 0; i < Commands; ++i) {
            String CMD = scanner.next();
            switch(CMD) {
                case "set_color":
                    color = scanner.nextInt();
                    left = scanner.nextInt();
                    right = scanner.nextInt();
                    segment.set_color(left, right, color, 1);
                    break;
                case "get_darkest_color":
                    left = scanner.nextInt();
                    right = scanner.nextInt();
                    System.out.println(segment.get_darkest(left, right, 1));
                    break;
                case "get_lightest_color":
                    left = scanner.nextInt();
                    right = scanner.nextInt();
                    System.out.println(segment.get_lightest(left, right, 1));
                    break;
                case "get_color":
                    pos = scanner.nextInt();
                    System.out.println(segment.get_color(pos, 1));
                    break;
            }
        }
    }
}
