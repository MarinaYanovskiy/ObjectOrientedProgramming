package OOP.Provided;

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
        }
        favoriteRestaurants.add(r);
        return this;
    }

    @Override
    public Collection<Restaurant> favorites() {
        return favoriteRestaurants;
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
        return this;
    }

    @Override
    public Set<HungryStudent> getFriends() {
        return friends;
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

        return filteredFavorites;
    }

    @Override
    public Collection<Restaurant> favoritesByDist(int dLimit) {
        return null;
    }

    @Override
    public int compareTo(HungryStudent o) {
        return 0;
    }
}
