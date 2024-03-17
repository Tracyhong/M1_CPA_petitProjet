/**
 * CPA petit projet 2023
 * Problème du cercle minimum / Smallest enclosing disk : Welzl algorithm
 * ETUDIANTS STL :
 *  - Elhadj Alseiny DIALLO - 21314820
 *  - Tracy HONG - 21314944
 */
package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import supportGUI.Circle;

public class DefaultTeam {

    

    // ----------------------------------------------------------------
    // ------------------- CODE DU TME CORRIGE ------------------------
    // ----------------------------------------------------------------
    /**
     * Algorithme naif pour le calcul du cercle minimum
     * @param inputPoints
     * @return cercle minimum
     */
    static public Circle algoNaif(ArrayList<Point> inputPoints){
		ArrayList<Point> points = (ArrayList<Point>) inputPoints.clone();
		if (points.size()<1) return null;
		double cX,cY,cRadius,cRadiusSquared;
		for (Point p: points){
			for (Point q: points){
				cX = .5*(p.x+q.x);
				cY = .5*(p.y+q.y);
				cRadiusSquared = 0.25*((p.x-q.x)*(p.x-q.x)+(p.y-q.y)*(p.y-q.y));
				boolean allHit = true;
				for (Point s: points)
					if ((s.x-cX)*(s.x-cX)+(s.y-cY)*(s.y-cY)>cRadiusSquared){
                      allHit = false;
                      break;
                  }
              if (allHit) return new Circle(new Point((int)cX,(int)cY),(int)Math.sqrt(cRadiusSquared));
          }
      }
      double resX=0;
      double resY=0;
      double resRadiusSquared=Double.MAX_VALUE;
      for (int i=0;i<points.size();i++){
          for (int j=i+1;j<points.size();j++){
              for (int k=j+1;k<points.size();k++){
                  Point p=points.get(i);
                  Point q=points.get(j);
                  Point r=points.get(k);
                  //si les trois sont colineaires on passe
                  if ((q.x-p.x)*(r.y-p.y)-(q.y-p.y)*(r.x-p.x)==0) continue;
                  //si p et q sont sur la meme ligne, ou p et r sont sur la meme ligne, on les echange
                  if ((p.y==q.y)||(p.y==r.y)) {
                      if (p.y==q.y){
                          p=points.get(k); //ici on est certain que p n'est sur la meme ligne de ni q ni r
                          r=points.get(i); //parce que les trois points sont non-colineaires
                      } else {
                          p=points.get(j); //ici on est certain que p n'est sur la meme ligne de ni q ni r
                          q=points.get(i); //parce que les trois points sont non-colineaires
                      }
                  }
                  //on cherche les coordonnees du cercle circonscrit du triangle pqr
                  //soit m=(p+q)/2 et n=(p+r)/2
                  double mX=.5*(p.x+q.x);
                  double mY=.5*(p.y+q.y);
                  double nX=.5*(p.x+r.x);
                  double nY=.5*(p.y+r.y);
                  //soit y=alpha1*x+beta1 l'equation de la droite passant par m et perpendiculaire a la droite (pq)
                  //soit y=alpha2*x+beta2 l'equation de la droite passant par n et perpendiculaire a la droite (pr)
                  double alpha1=(q.x-p.x)/(double)(p.y-q.y);
                  double beta1=mY-alpha1*mX;
                  double alpha2=(r.x-p.x)/(double)(p.y-r.y);
                  double beta2=nY-alpha2*nX;
                  //le centre c du cercle est alors le point d'intersection des deux droites ci-dessus
                  cX=(beta2-beta1)/(double)(alpha1-alpha2);
                  cY=alpha1*cX+beta1;
                  cRadiusSquared=(p.x-cX)*(p.x-cX)+(p.y-cY)*(p.y-cY);
                  if (cRadiusSquared>=resRadiusSquared) continue;
                  boolean allHit = true;
                  for (Point s: points)
                      if ((s.x-cX)*(s.x-cX)+(s.y-cY)*(s.y-cY)>cRadiusSquared){
                          allHit = false;
                          break;
                      }
                  if (allHit) {
                    //System.out.println("Found r="+Math.sqrt(cRadiusSquared));
                    resX=cX;
                    resY=cY;
                    resRadiusSquared=cRadiusSquared;
                }
              }
          }
      }
      return new Circle(new Point((int)resX,(int)resY),(int)Math.sqrt(resRadiusSquared));
  }
  // -----------------------------------------------------------------------------

    /**
     * Algorithme de Welzl pour le calcul du cercle minimum
     * @param inputPoints
     * @return cercle minimum
     */
    static public Circle algoWelzl(ArrayList<Point> inputsPoints) {
        Circle circle = b_MinDisk(inputsPoints, new ArrayList<Point>());
        return circle;
    }

    //methode recursive pour trouver le cercle minimum
    //source : http://www.stsci.edu/~RAB/Backup%20Oct%2022%202011/f_3_CalculationForWFIRSTML/Bob1.pdf
    private static Circle b_MinDisk(ArrayList<Point> inpuPoints, ArrayList<Point> R) {
        ArrayList<Point> P = new ArrayList<Point>(inpuPoints);
        Random r = new Random();
        Circle D = null;

        if (P.isEmpty() || R.size() == 3) {
            D = b_md(new ArrayList<Point>(), R);

        } else {
            Point p = P.get((r.nextInt(P.size())));
            P.remove(p);

            D = b_MinDisk(P, R);
            if (D != null && !isInside(D, p)) {
                R.add(p);
                D = b_MinDisk(P, R);
                R.remove(p);
            }
        }
        assert D != null;
        return D;
    }

    //cercle trivial entre 0 et 3 points
 	private static Circle b_md(ArrayList<Point> inputsPoints, ArrayList<Point> R){
            if (inputsPoints.isEmpty() && R.size() == 0)
                return new Circle(new Point(0, 0), 10);
            Circle D = null;
            if (R.size() == 1) {
                D = new Circle(R.get(0), 0);
            }
            if (R.size() == 2) {
                Point p = R.get(0);
                Point q = R.get(1);
				double cx = .5*(p.x+q.x); // calcul du centre du cercle
				double cy = .5*(p.y+q.y); // calcul du centre du cercle
				double cRadiusSquared = 0.25*((p.x-q.x)*(p.x-q.x)+(p.y-q.y)*(p.y-q.y)); // calcul du rayon du cercle
                Point center = new Point((int) cx, (int) cy);
                D = new Circle(center, (int)Math.ceil(Math.sqrt(cRadiusSquared)));
            } else {
                if (R.size() == 3){
                    Point p = R.get(0);
                    Point q = R.get(1);
                    Point r = R.get(2);
                    D = circleFrom3Point(p, q, r);
                }
            }
            return D;
        }

    //Cercle du cercle circonscrit au triangle formé par les trois points : methode des coordonnées cartesiennes du centre du cercle circonscrit
    //source : https://en.wikipedia.org/wiki/Circumcircle#Cartesian_coordinates_2
	private static Circle circleFrom3Point(Point a, Point b, Point c) {
        //calcul determinant de la matrice pour trouver le centre du cercle
        double d = (a.x * (b.y - c.y) + b.x * (c.y - a.y) + c.x * (a.y - b.y)) * 2;
        if (d == 0)// si d = 0, les points sont coliénaire/aligés
            return null;
        //calcul des coordonnées du centre du cercle
        double x = ((norm(a) * (b.y - c.y)) + (norm(b) * (c.y - a.y)) + (norm(c) * (a.y - b.y))) / d; 
        double y = ((norm(a) * (c.x - b.x)) + (norm(b) * (a.x - c.x)) + (norm(c) * (b.x - a.x))) / d; 
        Point p = new Point((int) x, (int) y);
        return new Circle(p, (int) Math.ceil(p.distance(a))); 
    }
    // calcul de la norme euclidienne quadratique d'un point pour le calcul du centre du cercle
    private static int norm(Point a) { 
		return (a.x * a.x) + (a.y * a.y);
	}

    // Verifie si le point p est dans le cercle c
    private static boolean isInside(Circle c, Point p) {
        double dist = Math.sqrt((p.x - c.getCenter().x) * (p.x - c.getCenter().x) + (p.y - c.getCenter().y) * (p.y - c.getCenter().y));
        return dist <= c.getRadius();
    }
    
    // Verifie si le cercle c est valide = si tous les points de l'ensemble sont dans le cercle
    private static boolean isValidCircle(Circle c,ArrayList<Point> inputsPoints) {
        for (Point p : inputsPoints) {
            if (!isInside(c, p)) {
                return false;
            }
        }
        return true;
    }

	
    //--------------------------------------------------------------------------------
    // -------------------- MAIN FUNCTION AVEC DIAM RACER ----------------------------
    // --------------------------------------------------------------------------------
	public Circle calculCercleMin(ArrayList<Point> points) {
        Circle cercle;
		// cercle = algoNaif(points);
		cercle = algoWelzl(points);
		
        if(cercle != null && isValidCircle(cercle, points)) {
            System.out.println("OK------------------");
        }
        else {
            System.out.println("KO------------------");
        }
		return cercle;
	}
}
