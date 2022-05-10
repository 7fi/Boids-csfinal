//Vector class written entirely by me (7fi)
public class Vector {
    public double x;
    public double y;

    //Constructors
    public Vector(){
        x = 0;
        y = 0;
    }

    public Vector(double dir, double mag){
        x = mag * Math.cos(dir);
        y = mag * Math.sin(dir);
        //System.out.println(x + " " + y);
    }
    
    public Vector(double x, double y, boolean placeholder){ // to differentiate between the other contructor
        this.x = x;
        this.y = y;
    }

    //Getters
    public double dir(){ // updates the direction
        return Math.atan2(y , x);
    }

    public double mag(){ // updates the magnitude 
        return Math.sqrt((x * x) + (y * y));
    }

    //Setters
    public void setMag(double mag){ // sets the magnitude and updates x & y
        double tempDir = dir();
        x = mag * Math.cos(tempDir);
        y = mag * Math.sin(tempDir);
    }

    public void setDir(double dir){ // sets the direction and updates x & y
        double tempMag = mag();
        x = tempMag * Math.cos(dir);
        y = tempMag * Math.sin(dir);
        System.out.println(x + " " + y);
    }

    //Other useful functions
    public void normalize(){
        divide(mag());
    }

    public void limit(double max){
        if(mag() > max){
            setMag(max);
        }
    }

    public double dot(Vector other){
        return x * other.x + y * other.y;
    }

    public double angle(Vector other){ // unused currently
        return Math.atan2(dot(other), (x * other.x - y * other.y));
    }

    //Math
    public void add(Vector input){
        x += input.x;
        y += input.y;
    }
    public void add(double x, double y){
        this.x += x;
        this.y += y;
    }

    public void subtract(Vector input){
        x -= input.x;
        y -= input.y;
    }
    public void multiply(double num){
        x *= num;
        y *= num;
    }

    public void divide(double divisor){
        if(divisor != 0){
            x /= divisor;
            y /= divisor;
        } else{
            System.out.println("Tried to divide by zero.");
            x = 0;
            y = 0;
        }
    }
}
