package OOP.Solution;
import OOP.Provided.HungryStudent;
import OOP.Provided.Restaurant;

import java.util.*;
import java.util.stream.Collectors;


public class HungryStudentImpl implements HungryStudent{

    private int id;
    private String name;
    private List<Restaurant> favoriteRestaurants;
    private Map<Integer,HungryStudent> friends;

    public HungryStudentImpl(int id, String name) {
        this.id = id;
        this.name = name;
        this.favoriteRestaurants = new ArrayList<>();
        this.friends = new TreeMap<>();
    }

    @Override
    public HungryStudent favorite(Restaurant r) throws UnratedFavoriteRestaurantException {
        if (r instanceof RestaurantImpl) {
            RestaurantImpl convertedR = (RestaurantImpl) r;
            if(convertedR.didStudentRated(this))
            {
                if(!favoriteRestaurants.contains(r))
                {
                    favoriteRestaurants.add(r);
                }
                return this;
            }
        }
        throw new UnratedFavoriteRestaurantException();
    }

    @Override
    public Collection<Restaurant> favorites() {
        List<Restaurant> copy = new ArrayList<>(this.favoriteRestaurants);
        return copy;
        ///maybe needs deepcopy
    }

    @Override
    public HungryStudent addFriend(HungryStudent s) throws SameStudentException, ConnectionAlreadyExistsException {
        if (this.equals(s)) {
            throw new SameStudentException();
        }
        HungryStudentImpl student = (HungryStudentImpl) s;
        if (this.friends.containsKey(student.id)) {
            throw new ConnectionAlreadyExistsException();
        }
        this.friends.put(student.id,s);
        return this;
    }

    @Override
    public Set<HungryStudent> getFriends() {
        Set<HungryStudent> copyOfFriends= new TreeSet<>(new Comparator<HungryStudent>()
                //we want to keep the internal order when returning a copy.
        {
            @Override
            public int compare(HungryStudent o1, HungryStudent o2) {
                return Integer.compare(((HungryStudentImpl)o1).getId(), ((HungryStudentImpl)o2).getId());
            }
        }
        );
        copyOfFriends.addAll(this.friends.values());
        return copyOfFriends;
        ///maybe needs deepcopy
    }

    @Override
    public Collection<Restaurant> favoritesByRating(int rLimit) {
        List<Restaurant> sortedList = new ArrayList<>(favoriteRestaurants);
        Collections.sort(sortedList, new Comparator<Restaurant>() {
            @Override
            public int compare(Restaurant r1, Restaurant r2) {
                RestaurantImpl restaurantR1 = (RestaurantImpl) r1;
                RestaurantImpl restaurantR2 = (RestaurantImpl) r2;
                // Compare by rating
                int ratingCompare = Double.compare(r2.averageRating(), r1.averageRating()); // Assuming higher ratings first
                if (ratingCompare != 0) {
                    return ratingCompare;
                }

                // If ratings are equal, compare by distance
                int distanceCompare = Integer.compare(r1.distance(), r2.distance()); // Assuming shorter distance first
                if (distanceCompare != 0) {
                    return distanceCompare;
                }

                // If distances are also equal, compare by ID
                return Integer.compare(restaurantR1.getId(), restaurantR2.getId()); // Assuming smaller ID first
            }
        });

        return sortedList.stream().filter(restaurant -> restaurant.averageRating() >= rLimit).collect(Collectors.toList());
    }

    @Override
    public Collection<Restaurant> favoritesByDist(int dLimit) {
        List<Restaurant> sortedList = new ArrayList<>(favoriteRestaurants);
        Collections.sort(sortedList, new Comparator<Restaurant>() {
            @Override
            public int compare(Restaurant r1, Restaurant r2) {
                RestaurantImpl restaurantR1 = (RestaurantImpl) r1;
                RestaurantImpl restaurantR2 = (RestaurantImpl) r2;

                // Compare by distance
                int distanceCompare = Integer.compare(r1.distance(), r2.distance()); // Assuming shorter distance first
                if (distanceCompare != 0) {
                    return distanceCompare;
                }

                // If distances are equal, compare by rating
                int ratingCompare = Double.compare(r2.averageRating(), r1.averageRating()); // Assuming higher ratings first
                if (ratingCompare != 0) {
                    return ratingCompare;
                }

                // If ratings are also equal, compare by ID
                return Integer.compare(restaurantR1.getId(), restaurantR2.getId()); // Assuming smaller ID first
            }
        });

        return sortedList.stream().filter(restaurant -> restaurant.distance() <= dLimit).collect(Collectors.toList());

    }
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof HungryStudentImpl)){
            return false;
        }
        HungryStudentImpl otherStudent = (HungryStudentImpl) o; //cast o to class type
        return (otherStudent.id==this.id); //check if the ids are equal
    }

    @Override
    public int compareTo(HungryStudent o) {
        if (o instanceof HungryStudentImpl) {
            HungryStudentImpl otherStudent = (HungryStudentImpl) o; //cast o to class type
            return this.id-otherStudent.id;
        } else {
            // Handle the case where o is not an instance of HungryStudentImpl
            // This might throw an exception or use some other logic to handle the comparison
            throw new IllegalArgumentException();
        }
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public String toString(){
        Set<String> stringFavoriteRestaurants= new TreeSet<>();
        for (Restaurant r : this.favoriteRestaurants) {
            if (r instanceof RestaurantImpl){
                RestaurantImpl restaurant = (RestaurantImpl) r; //cast r to class type
                stringFavoriteRestaurants.add(restaurant.getName());
            }
        }
        String favoritesToString = String.join(", ", stringFavoriteRestaurants);
        // Formatting the entire string
        return String.format("Hungry student: %s.\nId: %d.\nFavorites: %s.",
                this.name, this.id, favoritesToString);
    }

    int getId()
    {
        return  this.id;
    }


}
