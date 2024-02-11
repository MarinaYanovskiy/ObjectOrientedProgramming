package OOP.Solution;
import OOP.Provided.HungryStudent;
import OOP.Provided.Restaurant;

import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class HungryStudentImpl implements HungryStudent{

    private int id;
    private String name;
    private Collection<Restaurant> favoriteRestaurants;
    private Set<HungryStudent> friends;

    public HungryStudentImpl(int id, String name) {
        this.id = id;
        this.name = name;
        this.favoriteRestaurants = new HashSet<>();
        this.friends = new HashSet<>();
    }

    @Override
    public HungryStudent favorite(Restaurant r) throws UnratedFavoriteRestaurantException {
        if (!favoriteRestaurants.contains(r)) {
            throw new UnratedFavoriteRestaurantException();
            /*TODO: change the if statement because this will always throw an exception, it is incorrect.
             * I implemented a method called- bool didStudentRated(HungryStudent s) for this case.
             * It works on a restaurantImpl instance, receives a student and returns if the student rated that restaurant.
             * Note: you need to cast the Restaurant to RestaurantImpl.
             */
        }
        favoriteRestaurants.add(r);
        return this;
    }

    @Override
    public Collection<Restaurant> favorites() {
        return favoriteRestaurants;
        //TODO: should return a copy of the collection?
    }

    @Override
    public HungryStudent addFriend(HungryStudent s) throws SameStudentException, ConnectionAlreadyExistsException {
        if (this.equals(s)) {
            throw new SameStudentException();
        }
        if (friends.contains(s)) {
            throw new ConnectionAlreadyExistsException();
        }
        friends.add(s);
//        s->friends.add(this); להוסיף גם את דיס בתור חבר של אס
        //no need for that: in the faq in gr++ it is stated that it is asymmetric relationship in this part.
        return this;
    }

    @Override
    public Set<HungryStudent> getFriends() {
        return friends;
        //TODO: should return a copy of the collection?
    }

    @Override
    public Collection<Restaurant> favoritesByRating(int rLimit) {
        List<Restaurant> filteredFavorites = favoriteRestaurants.stream()
                .filter(restaurant -> restaurant.averageRating() >= rLimit)
                .sorted(Comparator
                        .comparing(Restaurant::averageRating).reversed()
                        .thenComparing(Restaurant::distance)
                        .thenComparing(Restaurant::compareTo())) // צריך להוסיף מסעדה פיקטיבית שתשמש לציר ייחוס
                .collect(Collectors.toList());

        //ToDo: implement comparator in an inner class and then use it!!!!! same for below.

        return filteredFavorites;
    }

    @Override
    public Collection<Restaurant> favoritesByDist(int dLimit) {
        List<Restaurant> filteredFavorites = favoriteRestaurants.stream()
                .filter(restaurant -> restaurant.distance() <= dLimit)
                .sorted(Comparator
                        .comparing(Restaurant::distance)
                        .thenComparing(Restaurant::averageRating, Comparator.reverseOrder())
                        .thenComparing(Restaurant::compareTo())) // צריך להוסיף מסעדה פיקטיבית שתשמש לציר ייחוס
                .collect(Collectors.toList());

        return filteredFavorites;
    }

    @Override
    public int compareTo(HungryStudent o) {
        return 0;
        //TODO: Implement it:)
    }

    //TODO: implement ToString method by the format given in the interface.
}
