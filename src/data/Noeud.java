package data;


public class Noeud {

    private double coutF;
    private double coutG;
    private double coutH;
    private Point parent;

    public Noeud() {
        this.coutF = Float.MAX_VALUE;
        this.coutG = 0.0;
        this.coutH = 0.0;
        this.parent = null;
    }

    public Noeud(double coutF, double coutG, double coutH, Point parent) {
        this.coutF = coutF;
        this.coutG = coutG;
        this.coutH = coutH;
        this.parent = parent;
    }

    public double getCoutF() {
        return coutF;
    }

    public void setCoutF(double coutF) {
        this.coutF = coutF;
    }

    public double getCoutG() {
        return coutG;
    }

    public void setCoutG(double coutG) {
        this.coutG = coutG;
    }

    public Point getParent() {
        return parent;
    }

    public void setParent(Point parent) {
        this.parent = parent;
    }

    public double getCoutH() {
        return coutH;
    }

    public void setCoutH(double coutH) {
        this.coutH = coutH;
    }
}
