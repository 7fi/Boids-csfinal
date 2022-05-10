import javafx.scene.canvas.GraphicsContext;

// Shape Class by 7fi (Initially I thought I might have to draw more shapes)
public class Shapes {
    public static void drawTri(GraphicsContext g, int x, int y, double r, int factor){
        int topx   = x + (int) Math.round(((7 * factor) * Math.cos(r)));
        int topy   = y + (int) Math.round(((7 * factor) * Math.sin(r)));

        int rightx = x + (int) Math.round(((3 * factor) * Math.cos(r + Math.toRadians(110))));
        int righty = y + (int) Math.round(((3 * factor) * Math.sin(r + Math.toRadians(110))));

        int leftx  = x + (int) Math.round(((3 * factor) * Math.cos(r - Math.toRadians(110))));
        int lefty  = y + (int) Math.round(((3 * factor) * Math.sin(r - Math.toRadians(110))));
        double[] xPoints = {topx, rightx, leftx};
        double[] yPoints = {topy, righty, lefty};
        g.fillPolygon(xPoints, yPoints, 3);

        // Origonal approach before I knew about drawPolygon
        //g.drawLine(rightx, righty, topx, topy); // right to up
        //g.drawLine(topx, topy, leftx, lefty); // up to left 
        //g.drawLine(leftx, lefty, rightx, righty); // left to right
    }
}
