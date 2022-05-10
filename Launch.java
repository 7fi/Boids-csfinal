import java.util.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.*;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

public class Launch extends Application{
    //Main Scene
    private static Scene scene;
    private GraphicsContext context;
    private Canvas canvas;
    public int w;
    public int h;
    
    // Slider values to change
    public int numBoids = 50;
    public double visionRange = 100;
    public double alignmentMult = 0.5;
    public double seperationMult = 1; // 0.7
    public double cohesionMult = 0.3; // 0.1
    public double speed = 5;

    //Checkbox values to change
    public boolean avoidEdges = false;
    public boolean drawDebugCircles = false;
    public boolean debugLines = false;
    public boolean shadows = true;
    public boolean triangles = true;

    //Flock
    public Boid[] boids = new Boid[numBoids];
    
    public static void main(String[] args) {
        launch(args);
    }
    public void start(Stage mainStage) throws Exception{
        StackPane mainPane;

        //Try to load fxml file
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Boids.fxml"));
            mainPane = loader.<StackPane>load();
            scene = new Scene(mainPane);
        }catch(Exception e){
            e.printStackTrace();
        }
        
        // Set window parameters
        mainStage.setTitle("Boid Flocking Simulation");
        mainStage.setScene(scene); 

        // Setup canvas
        AnchorPane Anchor = (AnchorPane)scene.getRoot().lookup("#anchorPane");
        canvas = new ResizableCanvas();
        Anchor.getChildren().add(canvas);
        canvas.widthProperty().bind(Anchor.widthProperty());
        canvas.heightProperty().bind(Anchor.heightProperty());
        context = canvas.getGraphicsContext2D();

        Controller.launch = this; // make a reference to this so we can read and write values
        
        AnimationTimer gameloop = new AnimationTimer() {
            public void handle(long currentTimeMillis){
                int deltaTime = 1/60;
                update(deltaTime);
            }
        };

        //Start loop and show window
        gameloop.start();
        mainStage.show();

        //Set starting conditions
        resetBoids();
    }

    public void resetBoids(){
        w = (int) canvas.getWidth();
        h = (int) canvas.getHeight();

        Random rand = new Random();
        for(int i = 0; i < numBoids; i++){
            //       X getPos()               Y getPos()                 Rotation                    Speed
            boids[i] = new Boid(rand.nextInt(50, w - 50), rand.nextInt(50, h - 50), rand.nextDouble(0,(Math.PI * 2)), 5);
        }
    }

    public void update(int deltaTime){
        if(scene != null){
            //Get scene size
            w = (int) canvas.getWidth();
            h = (int) canvas.getHeight();

            //Paint background
            context.setFill(new javafx.scene.paint.Color(0.25, 0.25, 0.25, 1));
            context.fillRect(0, 0, w, h);

            //if slider value changed update boid array length and amount
            if(numBoids > boids.length){
                Random rand = new Random();
                int oldLength = boids.length;
                boids = Arrays.copyOf(boids, (numBoids));
                for(int i = oldLength - 1; i < (numBoids); i++){
                    //       X getPos()               Y getPos()                 Rotation                    Speed
                    boids[i] = new Boid(rand.nextInt(50, w - 50), rand.nextInt(50, h -50), rand.nextDouble(0,(Math.PI * 2)), 5);
                }
            }else if(numBoids < boids.length){
                boids = Arrays.copyOf(boids, (numBoids));
            }

            // Update and draw boids
            
            applyRules(); // applies rules
            for (Boid boid : boids) {
                boid.update(this, boids, deltaTime);
                boid.render(context, this);
            }
        }
    }

    public void applyRules(){ // Applies rules to every boid
        for (Boid boid : boids) { // loops over all boids
            
            //Create default vectors
            Vector seperationForce = new Vector();
            Vector cohesionForce = new Vector();
            Vector alignmentForce = new Vector();
            Vector edgeForce = new Vector();

            int numBoidsInRange = 0;

            for (Boid other : boids) { // add up steering forces
                if(other.distSquared(boid, other) < Math.pow(visionRange, 2) && !other.equals(boid)){ // if boid is in range and is not self
                    // Inversely scaled vector in opposite direction of other boid || c = c - (b.position - bJ.position) 
                    //seperationForce = seperationForce - (other.pos - boid.pos);

                    Vector tempDiff = new Vector();
                    tempDiff.add(boid.getPos());
                    tempDiff.subtract(other.getPos());
                    tempDiff.divide(other.distSquared(boid,other));
                    
                    //add up (for avg)
                    seperationForce.add(tempDiff);
                    cohesionForce.add(other.getPos());
                    alignmentForce.add(other.getVel());

                    numBoidsInRange++; // add to total boids in range
                    
                    if(debugLines){ // draws lines between two boids (very inefficient because it draws every line twice)
                        context.strokeLine(boid.getPos().x, boid.getPos().y, other.getPos().x, other.getPos().y);
                    }
                }
            }
            
            if(avoidEdges){ // if edge avoidance setting is on, avoid edges
                int edgeAvoidanceForce = 3; // not currently changeable apart from here

                /*
                    Essentially this method creates a vector that points in the opposite direction of each edge, 
                    and that is scaled proportionally to how close each boid is to the edge.
                */

                //Top
                Vector tempDiff = new Vector(0, boid.getPos().y, true); 
                if(boid.getPos().y != 0){
                    tempDiff.divide(Math.pow((boid.getPos().y),2));
                }
                tempDiff.multiply(edgeAvoidanceForce);
                edgeForce.add(tempDiff);
                
                // Left
                tempDiff = new Vector(boid.getPos().x, 0, true);
                if(boid.getPos().x != 0){
                    tempDiff.divide(Math.pow((boid.getPos().x),2));
                }
                tempDiff.multiply(edgeAvoidanceForce);
                edgeForce.add(tempDiff);
                
                //Bottom
                tempDiff = new Vector(0, (h - boid.getPos().y), true);
                if(boid.getPos().y != 0){
                    tempDiff.divide(Math.pow(( h - boid.getPos().y),2));
                    tempDiff.multiply(-edgeAvoidanceForce);
                    edgeForce.add(tempDiff);
                }
                
                //Right
                tempDiff = new Vector((w - boid.getPos().x), 0, true);
                if(boid.getPos().x != 0){
                    tempDiff.divide(Math.pow(( w - boid.getPos().x),2));
                    tempDiff.multiply(-edgeAvoidanceForce);
                    edgeForce.add(tempDiff);
                }
            }
            
            if(numBoidsInRange > 0){ // if there are no boids in range its divide by zero
                //hard coded offsets (not sure why they are so weird tbh)
                double alignmentStr = 150;
                double cohesionStr = 4000;
                double seperationStr = 1.03;
                
                //desired_velocity = normalize (position - target) * max_speed
                //steering = desired_velocity - velocity

                //Alignment Force (steer towards avg dir = (Desired - Current) / damper then limit by maxForce
                alignmentForce.divide(numBoidsInRange); // finish taking avg to get desired
                alignmentForce.subtract(boid.getVel()); // subtract current
                alignmentForce.divide(alignmentStr); // divide by damper
                alignmentForce.limit(boid.maxForce); // limit by maxForce
                
                //Cohesion Force Steer towards avg pos 
                cohesionForce.divide(numBoidsInRange); // finish taking avg to get desired
                cohesionForce.subtract(boid.getPos()); // subtract current pos
                cohesionForce.divide(cohesionStr);  // divide by damper
                cohesionForce.limit(boid.maxForce); // limit by maxForce
                
                //Seperation Force // steer away from nearest neighbors (working) c = c - (b.position - bJ.position)
                seperationForce.divide(numBoidsInRange); // finish taking avg to get desired
                seperationForce.subtract(boid.getVel()); // subtract current
                seperationForce.divide(seperationStr); // divide by damper
                // seperationForce.limit(boid.maxForce); // limit by maxForce
                
                //Scale by slider values
                alignmentForce.multiply(alignmentMult); 
                seperationForce.multiply(seperationMult);
                cohesionForce.multiply(cohesionMult);
                
            }
            //Add up forces
            boid.addAcl(edgeForce);
            boid.addAcl(alignmentForce);
            boid.addAcl(seperationForce);
            boid.addAcl(cohesionForce);
        }
    }

    public class ResizableCanvas extends Canvas // from https://stackoverflow.com/questions/24533556/how-to-make-canvas-resizable-in-javafx
    {
        public boolean isResizable()
        {
            return true;
        }
    }
}
