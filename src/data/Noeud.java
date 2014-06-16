package data;


public class Noeud {

    private float coutF;
    private float coutG;
    private float coutH;
    private Point parent;

    public Noeud() {
        this.coutF = Float.MAX_VALUE;
        this.coutG = 0.0f;
        this.coutH = 0.0f;
        this.parent = null;
    }

    public Noeud(float coutF, float coutG, float coutH, Point parent) {
        this.coutF = coutF;
        this.coutG = coutG;
        this.coutH = coutH;
        this.parent = parent;
    }

    public float getCoutF() {
        return coutF;
    }

    public void setCoutF(float coutF) {
        this.coutF = coutF;
    }

    public float getCoutG() {
        return coutG;
    }

    public void setCoutG(float coutG) {
        this.coutG = coutG;
    }

    public Point getParent() {
        return parent;
    }

    public void setParent(Point parent) {
        this.parent = parent;
    }

    public float getCoutH() {
        return coutH;
    }

    public void setCoutH(float coutH) {
        this.coutH = coutH;
    }
}
