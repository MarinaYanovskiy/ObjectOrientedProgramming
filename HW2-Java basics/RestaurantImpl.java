package OOP.Provided;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RestaurantImpl implements Restaurant {
    private int id;
    private String name;
    private int distFromTech;
    private Set<String> menu;
    private Map<HungryStudent,Integer> ratingsFromStudents;

    public RestaurantImpl(int id,String name,int distFromTech, Set<String> menu){
        this.id=id;
        this.distFromTech=distFromTech;
        this.name=name;
        this.menu=menu;
        this.ratingsFromStudents=new HashMap<>();
    }

    public int getId() {
       return this.id;
    }

    @Override
    public int distance() {
        return this.distFromTech;
    }

    @Override
    public Restaurant rate(HungryStudent s, int r) throws RateRangeException {
        if(r<0 || r>5) {
           throw new RateRangeException();
        }
        ratingsFromStudents.put(s,r);
        return this;
    }

    @Override
    public int numberOfRates() {
        return ratingsFromStudents.size();
    }

    @Override
    public double averageRating() {
        int avR=0;
        for(Integer aRating : ratingsFromStudents.values())
        {
            avR+=aRating;
        }
        return (avR/(ratingsFromStudents.size()));
    }

    @Override
    public int compareTo(Restaurant o) {
        if (o instanceof RestaurantImpl) {
            RestaurantImpl otherRestaurant = (RestaurantImpl) o;
            return this.id-otherRestaurant.id;

        } else {
            // Handle the case where o is not an instance of RestaurantImpl
            // This might throw an exception or use some other logic to handle the comparison
            throw new IllegalArgumentException("Cannot compare RestaurantImpl with non-RestaurantImpl instance.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RestaurantImpl)){
            return false;
        }
        RestaurantImpl otherRestaurant = (RestaurantImpl) o;
        return (otherRestaurant.id==this.id);
    }
}
