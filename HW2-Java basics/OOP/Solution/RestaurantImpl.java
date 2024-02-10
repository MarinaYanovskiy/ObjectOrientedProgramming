package OOP.Solution;
import OOP.Provided.HungryStudent;
import OOP.Provided.Restaurant;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RestaurantImpl implements Restaurant {
    private int id; //the restaurant id
    private String name; //the restaurant name
    private int distFromTech; //the restaurant distance from Technion
    private Set<String> menu; //the restaurant menu
    private Map<HungryStudent,Integer> ratingsFromStudents; //all the ratings

    public RestaurantImpl(int id,String name,int distFromTech, Set<String> menu){ //C'tor
        this.id=id;
        this.distFromTech=distFromTech;
        this.name=name;
        this.menu=menu;
        this.ratingsFromStudents=new HashMap<>();
    }

    @Override
    public int distance() {
        return this.distFromTech;
    }

    @Override
    public Restaurant rate(HungryStudent s, int r) throws RateRangeException {
        if(r<0 || r>5) { //check if the rating r is in the range
           throw new RateRangeException();
        }
        ratingsFromStudents.put(s,r); //update rating
        return this;
    }

    @Override
    public int numberOfRates() {
        return ratingsFromStudents.size();
    }

    @Override
    public double averageRating() {
        int sumR=0; //sum all the ratings
        for(Integer aRating : ratingsFromStudents.values()) //iterate over the ratings
        {
            sumR+=aRating;
        }
        return (sumR/(ratingsFromStudents.size()));//return the average
    }

    @Override
    public int compareTo(Restaurant o) {
        if (o instanceof RestaurantImpl) {
            RestaurantImpl otherRestaurant = (RestaurantImpl) o; //cast o to class type
            return this.id-otherRestaurant.id;
        } else {
            // Handle the case where o is not an instance of RestaurantImpl
            // This might throw an exception or use some other logic to handle the comparison
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RestaurantImpl)){
            return false;
        }
        RestaurantImpl otherRestaurant = (RestaurantImpl) o; //cast o to class type
        return (otherRestaurant.id==this.id); //check if the ids are equal
    }
}
