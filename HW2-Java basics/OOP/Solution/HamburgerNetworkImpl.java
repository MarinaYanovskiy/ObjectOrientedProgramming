package OOP.Solution;
import OOP.Provided.*;

import java.util.Collection;
import java.util.Set;

public class HamburgerNetworkImpl implements HamburgerNetwork{
    @Override
    public HungryStudent joinNetwork(int id, String name) throws HungryStudent.StudentAlreadyInSystemException {
        return null;
    }

    @Override
    public Restaurant addRestaurant(int id, String name, int dist, Set<String> menu) throws Restaurant.RestaurantAlreadyInSystemException {
        return null;
    }

    @Override
    public Collection<HungryStudent> registeredStudents() {
        return null;
    }

    @Override
    public Collection<Restaurant> registeredRestaurants() {
        return null;
    }

    @Override
    public HungryStudent getStudent(int id) throws HungryStudent.StudentNotInSystemException {
        return null;
    }

    @Override
    public Restaurant getRestaurant(int id) throws Restaurant.RestaurantNotInSystemException {
        return null;
    }

    @Override
    public HamburgerNetwork addConnection(HungryStudent s1, HungryStudent s2) throws HungryStudent.StudentNotInSystemException, HungryStudent.ConnectionAlreadyExistsException, HungryStudent.SameStudentException {
        return null;
    }

    @Override
    public Collection<Restaurant> favoritesByRating(HungryStudent s) throws HungryStudent.StudentNotInSystemException {
        return null;
    }

    @Override
    public Collection<Restaurant> favoritesByDist(HungryStudent s) throws HungryStudent.StudentNotInSystemException {
        return null;
    }

    @Override
    public boolean getRecommendation(HungryStudent s, Restaurant r, int t) throws HungryStudent.StudentNotInSystemException, Restaurant.RestaurantNotInSystemException, ImpossibleConnectionException {
        return false;
    }
}
