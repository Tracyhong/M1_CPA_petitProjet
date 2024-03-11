package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
//Problème du cercle minimum / Smallest enclosing disk : Welzl algorithm
/***************************************************************
 * TME 1: calcul de diamètre et de cercle couvrant minimum.    *
 *   - Trouver deux points les plus éloignés d'un ensemble de  *
 *     points donné en entrée.                                 *
 *   - Couvrir l'ensemble de poitns donné en entrée par un     *
 *     cercle de rayon minimum.                                *
 *                                                             *
 * class Circle:                                               *
 *   - Circle(Point c, int r) constructs a new circle          *
 *     centered at c with radius r.                            *
 *   - Point getCenter() returns the center point.             *
 *   - int getRadius() returns the circle radius.              *
 *                                                             *
 * class Line:                                                 *
 *   - Line(Point p, Point q) constructs a new line            *
 *     starting at p ending at q.                              *
 *   - Point getP() returns one of the two end points.         *
 *   - Point getQ() returns the other end point.               *
 ***************************************************************/
import supportGUI.Circle;
import supportGUI.Line;

public class DefaultTeam {

  // calculCercleMin: ArrayList<Point> --> Circle
  //   renvoie un cercle couvrant tout point de la liste, de rayon minimum.
	
  //créer un autre viewer pour lancer calcul cercleMinNaif et calculCercleMinWelzl
	public Circle calculCercleMin(ArrayList<Point> points) {
        Circle cercle;
		//cercle = algoNaif(points);
		cercle = algoWelzl(points);
		
		return cercle;
	}
    // public Circle calculCercleMinWelzl(ArrayList<Point> points) {
    //     Circle cercle;
	// 	cercle = algoWelzl(points);
		
	// 	return cercle;
	// }
	
 	private Circle trivialCircle(ArrayList<Point> inputsPoints, ArrayList<Point> R){
            if (inputsPoints.isEmpty() && R.size() == 0)
                return new Circle(new Point(0, 0), 10);
            Random r = new Random();
            Circle D = null;
            if (R.size() == 1) {
                D = new Circle(R.get(0), 0);
            }
            if (R.size() == 2) {
    
                double cx = (R.get(0).x + R.get(1).x) / 2;
                double cy = (R.get(0).y + R.get(1).y) / 2;
                double d = R.get(0).distance(R.get(1)) / 2;
                Point p = new Point((int) cx, (int) cy);
                D = new Circle(p, (int) Math.ceil(d));
            } else {
                if (R.size() == 3)
                    D = circle3point(R.get(0), R.get(1), R.get(2));
            }
            return D;
        }
	private Circle circle3point(Point a, Point b, Point c) {
        double d = (a.x * (b.y - c.y) + b.x * (c.y - a.y) + c.x * (a.y - b.y)) * 2;
        if (d == 0)
            return null;
        double x = ((norm(a) * (b.y - c.y)) + (norm(b) * (c.y - a.y)) + (norm(c) * (a.y - b.y)))
                / d;
        double y = ((norm(a) * (c.x - b.x)) + (norm(b) * (a.x - c.x)) + (norm(c) * (b.x - a.x)))
                / d;
        Point p = new Point((int) x, (int) y);
        return new Circle(p, (int) Math.ceil(p.distance(a)));

    }
    private boolean contains(Circle c, Point p) {
		if (p.distance(c.getCenter()) - c.getRadius() < 0.00001) {
			return true;
		}
		return false;
	}
    private int norm(Point a) {
		return (a.x * a.x) + (a.y * a.y);
	}
	private Circle bMinDisk(ArrayList<Point> Ps, ArrayList<Point> R) {
		ArrayList<Point> P = new ArrayList<Point>(Ps);
		Random r = new Random();
		Circle D = null;

		if (P.isEmpty() || R.size() == 3) {
			D = trivialCircle(new ArrayList<Point>(), R);

		} else {
			Point p = P.get((r.nextInt(P.size())));
			P.remove(p);

			D = bMinDisk(P, R);
			if (D != null && !contains(D, p)) {
				R.add(p);
				D = bMinDisk(P, R);
				R.remove(p);
			}
		}

		return D;
	}
	private Circle algoWelzl(ArrayList<Point> inputsPoints) {
		
		// TODO
		// 1. Si la liste de points contient moins de trois deux, renvoyer un cercle de rayon 0
		// 3. Si la liste de points contient deux points, renvoyer un cercle de rayon la distance entre les deux points
		// 4. Si la liste de points contient trois points, renvoyer un cercle circonscrit au triangle formé par les trois points
		// 5. Si la liste de points contient plus de trois points, renvoyer un cercle circonscrit au cercle circonscrit au sous-ensemble de points
		//    obtenu en retirant un point aléatoire de la liste de points
		/* ArrayList<Point> P = new ArrayList<Point>(inputsPoints);
        ArrayList<Point> R = new ArrayList<Point>();
        
        if(P.isEmpty()|| R.size() == 3){
            return trivialCircle(P,R);
        }
        else {
            int index = (int) (Math.random() * P.size());
            Point p = P.get(index);
            P.remove(index);
            Circle c = algoWelzl(P);
            if(c.getRadius() >= p.distance(c.getCenter())){
                R.add(p);
                return c;
            }
            else {
                R.add(p);
                return algoWelzl(R);
            }
        } */
        return bMinDisk(inputsPoints, new ArrayList<Point>());
	}
	
	
	private Circle algoNaif(ArrayList<Point> inputPoints){
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
                  if (allHit) {System.out.println("Found r="+Math.sqrt(cRadiusSquared));resX=cX;resY=cY;resRadiusSquared=cRadiusSquared;}
              }
          }
      }
      return new Circle(new Point((int)resX,(int)resY),(int)Math.sqrt(resRadiusSquared));
  }
}
