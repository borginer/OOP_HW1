package homework1;

import java.util.Iterator;
import java.util.ArrayList;

/**
 * A Route is a path that traverses arbitrary GeoSegments, regardless
 * of their names.
 * <p>
 * Routes are immutable. New Routes can be constructed by adding a segment 
 * to the end of a Route. An added segment must be properly oriented; that 
 * is, its p1 field must correspond to the end of the original Route, and
 * its p2 field corresponds to the end of the new Route.
 * <p>
 * Because a Route is not necessarily straight, its length - the distance
 * traveled by following the path from start to end - is not necessarily
 * the same as the distance along a straight line between its endpoints.
 * <p>
 * Lastly, a Route may be viewed as a sequence of geographical features,
 * using the <tt>getGeoFeatures()</tt> method which returns an Iterator of
 * GeoFeature objects.
 * <p>
 * <b>The following fields are used in the specification:</b>
 * <pre>
 *   start : GeoPoint            // location of the start of the route
 *   end : GeoPoint              // location of the end of the route
 *   startHeading : angle        // direction of travel at the start of the route, in degrees
 *   endHeading : angle          // direction of travel at the end of the route, in degrees
 *   geoFeatures : sequence      // a sequence of geographic features that make up this Route
 *   geoSegments : sequence      // a sequence of segments that make up this Route
 *   length : real               // total length of the route, in kilometers
 *   endingGeoSegment : GeoSegment  // last GeoSegment of the route
 * </pre>
 **/
public class Route {
   private ArrayList<GeoFeature> features;
	
 	/*
    * Rep. Invariant:
    * A continuous non-linear path devided into sub-paths that share the same name
    * Abstraction Function:
    * features represents a list of GeoFeatures, that together combine into one non-linear path
	 * The end point of each GeoFeature is equal to the beggining of the next one in the list,
    * and the name is different.
    */


  	/**
  	 * Constructs a new Route.
     * @requires gs != null
     * @effects Constructs a new Route, r, such that
     *	        r.startHeading = gs.heading &&
     *          r.endHeading = gs.heading &&
     *          r.start = gs.p1 &&
     *          r.end = gs.p2
     **/
  	public Route(GeoSegment gs) {
  		if (gs == null) {
         throw new IllegalArgumentException();
      }
      features = new ArrayList<GeoFeature>();
      features.add(new GeoFeature(gs));
  	}


    /**
     * Returns location of the start of the route.
     * @return location of the start of the route.
     **/
  	public GeoPoint getStart() {
  		return features.getLast().getStart();
  	}


  	/**
  	 * Returns location of the end of the route.
     * @return location of the end of the route.
     **/
  	public GeoPoint getEnd() {
  		return features.getLast().getEnd();
  	}


  	/**
  	 * Returns direction of travel at the start of the route, in degrees.
   	 * @return direction (in compass heading) of travel at the start of the
   	 *         route, in degrees.
   	 **/
  	public double getStartHeading() {
  		return features.getFirst().getStartHeading();
  	}


  	/**
  	 * Returns direction of travel at the end of the route, in degrees.
     * @return direction (in compass heading) of travel at the end of the
     *         route, in degrees.
     **/
  	public double getEndHeading() {
      return features.getLast().getEndHeading();
   }


  	/**
  	 * Returns total length of the route.
     * @return total length of the route, in kilometers.  NOTE: this is NOT
     *         as-the-crow-flies, but rather the total distance required to
     *         traverse the route. These values are not necessarily equal.
   	 **/
  	public double getLength() {
  		Iterator<GeoFeature> iter = features.iterator();

      int length = 0;

      while (iter.hasNext()) {
         length += iter.next().getLength();
      }

      return length;
  	}


  	/**
     * Creates a new route that is equal to this route with gs appended to
     * its end.
   	 * @requires gs != null && gs.p1 == this.end
     * @return a new Route r such that
     *         r.end = gs.p2 &&
     *         r.endHeading = gs.heading &&
     *         r.length = this.length + gs.length
     **/
  	public Route addSegment(GeoSegment gs) {
      checkRep();
      if (gs == null || !gs.getP1().equals(this.getEnd())) {
         throw new IllegalArgumentException();
      }
      
      if (features.getLast().getName() == gs.getName()) {
         features.getLast().addSegment(gs);
      } else {
         features.add(new GeoFeature(gs));
      }

      checkRep();
      return this;
   }


    /**
     * Returns an Iterator of GeoFeature objects. The concatenation
     * of the GeoFeatures, in order, is equivalent to this route. No two
     * consecutive GeoFeature objects have the same name.
     * @return an Iterator of GeoFeatures such that
     * <pre>
     *      this.start        = a[0].start &&
     *      this.startHeading = a[0].startHeading &&
     *      this.end          = a[a.length - 1].end &&
     *      this.endHeading   = a[a.length - 1].endHeading &&
     *      this.length       = sum(0 <= i < a.length) . a[i].length &&
     *      for all integers i
     *          (0 <= i < a.length - 1 => (a[i].name != a[i+1].name &&
     *                                     a[i].end  == a[i+1].start))
     * </pre>
     * where <code>a[n]</code> denotes the nth element of the Iterator.
     * @see homework1.GeoFeature
     **/
  	public Iterator<GeoFeature> getGeoFeatures() {
  		return features.iterator();
  	}


  	/**
     * Returns an Iterator of GeoSegment objects. The concatenation of the
     * GeoSegments, in order, is equivalent to this route.
     * @return an Iterator of GeoSegments such that
     * <pre>
     *      this.start        = a[0].p1 &&
     *      this.startHeading = a[0].heading &&
     *      this.end          = a[a.length - 1].p2 &&
     *      this.endHeading   = a[a.length - 1].heading &&
     *      this.length       = sum (0 <= i < a.length) . a[i].length
     * </pre>
     * where <code>a[n]</code> denotes the nth element of the Iterator.
     * @see homework1.GeoSegment
     **/
  	public Iterator<GeoSegment> getGeoSegments() {
  		ArrayList<GeoSegment> list = new ArrayList<GeoSegment>();

      Iterator<GeoFeature> this_iter = getGeoFeatures();
      while (this_iter.hasNext()) {
         Iterator<GeoSegment> iter = this_iter.next().getGeoSegments();
         while (iter.hasNext()) {
            list.add(iter.next());
         }
      }

      return list.iterator();
  	}


  	/**
     * Compares the specified Object with this Route for equality.
     * @return true iff (o instanceof Route) &&
     *         (o.geoFeatures and this.geoFeatures contain
     *          the same elements in the same order).
     **/
  	public boolean equals(Object o) {
  		if (o == null || !(o instanceof Route)) {
         return false;
      }

      Iterator<GeoFeature> this_iter = features.iterator();
      Iterator<GeoFeature> o_iter = ((Route)o).getGeoFeatures();

      while (this_iter.hasNext() && o_iter.hasNext()) {
         if (!this_iter.next().equals(o_iter.next())) {
            return false;
         }
      }

      return true;
  	}


    /**
     * Returns a hash code for this.
     * @return a hash code for this.
     **/
  	public int hashCode() {
    	// This implementation will work, but you may want to modify it
    	// for improved performance.

    	return 1;
  	}


    /**
     * Returns a string representation of this.
     * @return a string representation of this.
     **/
  	public String toString() {
      StringBuffer s = new StringBuffer();

      Iterator<GeoFeature> iter = this.getGeoFeatures();

      while (iter.hasNext()) {
         s.append(iter.next().toString() + "\n");
      }

      return new String(s);
  	}

   private void checkRep() {
		Iterator<GeoFeature> iter = this.getGeoFeatures(); 
		GeoFeature last_gf;
		GeoFeature gf;

		if (iter.hasNext()) {
			last_gf = iter.next();
		} else {
			return;
		}

		while (iter.hasNext()) {
			gf = iter.next();
			assert(last_gf.getEnd().equals(gf.getStart()));
			assert(last_gf.getName() != gf.getName());
			last_gf = gf;
		}
	}

}
