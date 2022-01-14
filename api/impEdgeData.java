package api;

public class impEdgeData implements EdgeData {

    private int src;
    private int dest;
    private double weight;
    private String info;
    private int tag;

    public impEdgeData(int src, int dest, double weight, String info, int tag) {
        this.tag = tag;
        this.dest = dest;
        this.weight = weight;
        this.info = info;
        this.src = src;
    }

    public impEdgeData(int src, int dest, double w) {
        this.dest = dest;
        this.src = src;
        this.weight = w;
        this.tag=0;
        this.info="";
    }

    public impEdgeData(int src, int dest) {
        this.dest = dest;
        this.src = src;
        this.weight=0;
        this.tag=0;
        this.info="";

    }

    @Override
    public int getSrc() {
        return this.src;
    }

    @Override
    public int getDest() {
        return this.dest;
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
    public String getInfo() {
        return this.info;
    }

    @Override
    public void setInfo(String s) {
        this.info = s;
    }

    @Override
    public int getTag() {
        return this.tag;
    }

    @Override
    public void setTag(int t) {
        this.tag = t;
    }

    public String toString() {
        return src + "->" + dest + "(" + weight + ")";
    }
}