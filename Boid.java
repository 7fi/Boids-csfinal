import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

//Boid class by 7fi
public class Boid {
    private Vector pos;
    private Vector vel;
    private Vector acl;
    public double maxForce = 0.2;

    public Boid(double x, double y, double r){
        this.pos.x = x;
        this.pos.y = y;
        this.vel.setDir(r);
    }

    public Boid(double x, double y, double r, double vmag){
        pos = new Vector(x,y,true);
        vel = new Vector(r,vmag);
        acl = new Vector();
    }

    //Getters
    public Vector getPos(){
        return pos;
    }
    public Vector getVel(){
        return vel;
    }
    public Vector getAcl(){
        return acl;
    }

    //Setter
    public void addAcl(Vector acl){
        this.acl.add(acl); 
    }
    
    public void update(Launch main, Boid[] boids, double deltaTime){ // updates every frame (called from MainLogic)
        // update vel with acl
        vel.add(acl);

        vel.setMag(main.speed /4); // to correct for framerate * deltaTime
        
        //update position with velocity
        if(isInBounds(main)){ // if inside of borders
            pos.add(vel);
        } else { // otherwise tp to other side
            wrap(main);
        }
        
        acl = new Vector(); // reset acceleration for next frame
    }

    public void render(GraphicsContext context, Launch main){

        if(main.shadows){ // if shadows are on
            context.setFill(new javafx.scene.paint.Color(0, 0, 0, 0.4)); // set to shadow color
            if(main.triangles){ // if in trinagle mode
                Shapes.drawTri(context, (int)pos.x + 4, (int)pos.y + 4, vel.dir(), 2);
            }else{ // otherwise its circle mode
                context.fillOval(pos.x - (15/2) + 4, pos.y - (15/2) + 4, 15, 15);
            }
        }

        context.setFill(new javafx.scene.paint.Color(0.9, 0.9, 0.9, 1)); // set to boid color
        if(main.triangles){ // if in triangle mode
            Shapes.drawTri(context, (int)pos.x, (int)pos.y, vel.dir(), 2);
        }else{ // otherwise its circle mode
            context.fillOval(pos.x - (15/2), pos.y - (15/2), 15, 15);
        }

        //setup debug circles
        context.setStroke(new Color(0, 0, 0, 0.2));
        context.setLineWidth(2);
        if(main.drawDebugCircles){
            context.strokeOval(pos.x - (main.visionRange), pos.y - (main.visionRange), main.visionRange * 2, main.visionRange * 2);
        }
    }
    
    public void wrap(Launch main){ // teleports a boid across the screen
        if(pos.x <= 0){
            pos.x += main.w;
        }else if(pos.y <= 0){
            pos.y += main.h;
        }else if(pos.x >= main.w){
            pos.x = 1;
        }else if(pos.y >= main.h){
            pos.y = 1;
        }
    }

    public double dist(Boid first, Boid second){ // returns distance between two boids
        return dist(first.pos.x, first.pos.y, second.pos.x, second.pos.y);
    }
    public double distSquared(Boid first, Boid second){ // returns the distance squared between two boids
        return distSquared(first.pos.x, first.pos.y, second.pos.x, second.pos.y);
    }
    public double dist(double firstx, double firsty, double secondx, double secondy){ // returns the distance between two points
        return Math.sqrt(Math.pow((secondx - firstx), 2) + Math.pow((secondy - firsty), 2));
    }
    public double distSquared(double firstx, double firsty, double secondx, double secondy){ // returns the distance squared between two points
        return (secondx - firstx) * (secondx - firstx)  + (secondy - firsty) * (secondy - firsty);
    }
    public boolean isInBounds(Launch main){ // Returns boolean value based on if a boid is in the bounds of the screen
        return (pos.x <= main.w && pos.y <= main.h && pos.x >= 0 && pos.y >= 0); 
    }
}
