package OOP.Solution;
import OOP.Provided.*;

import java.util.*;
import java.util.stream.Collectors;

public class HamburgerNetworkImpl implements HamburgerNetwork {
    Map<Integer, HungryStudent> students;
    Map<Integer, Restaurant> restaurants;

    public HamburgerNetworkImpl() {
        students = new TreeMap<>();
        restaurants = new TreeMap<>();
    }

    @Override
    public HungryStudent joinNetwork(int id, String name) throws HungryStudent.StudentAlreadyInSystemException {
        return null;
    }

    @Override
    public Restaurant addRestaurant(int id, String name, int dist, Set<String> menu) throws Restaurant.RestaurantAlreadyInSystemException {
        RestaurantImpl r = new RestaurantImpl(id,name,dist,menu);
        if (this.restaurants.containsKey(r.getId()))
        {
            throw new Restaurant.RestaurantAlreadyInSystemException();
        }
        this.restaurants.put(r.getId(),r);
        return null;
    }

    @Override
    public Collection<HungryStudent> registeredStudents() {
        return null;
    }

    @Override
    public Collection<Restaurant> registeredRestaurants() {
        Set<Restaurant> copyOfRestaurants = new HashSet<>(this.restaurants.values());
        return copyOfRestaurants;
    }

    @Override
    public HungryStudent getStudent(int id) throws HungryStudent.StudentNotInSystemException {
        return null;
    }

    @Override
    public Restaurant getRestaurant(int id) throws Restaurant.RestaurantNotInSystemException {
        if(!this.restaurants.containsKey(id))
        {
            throw new Restaurant.RestaurantNotInSystemException();
        }
        return this.restaurants.get(id);
    }

    @Override
    public HamburgerNetwork addConnection(HungryStudent s1, HungryStudent s2) throws HungryStudent.StudentNotInSystemException, HungryStudent.ConnectionAlreadyExistsException, HungryStudent.SameStudentException {
        s1.addFriend(s2);
        s2.addFriend(s1);
        //maybe need to handle exceptions
        return this;
    }

    @Override
    public Collection<Restaurant> favoritesByRating(HungryStudent s) throws HungryStudent.StudentNotInSystemException {
        return null;
    }

    @Override
    public Collection<Restaurant> favoritesByDist(HungryStudent s) throws HungryStudent.StudentNotInSystemException {
       List<Restaurant> favOfFriends = new ArrayList<>();
       for (HungryStudent friend: s.getFriends())
       {
           List<Restaurant> favOfFriend = new ArrayList<>(friend.favoritesByDist(Integer.MAX_VALUE));
           List<Integer> indicesToRemove = new ArrayList<>();
           for (int i=0; i<favOfFriend.size();i++)
           {
               if(favOfFriends.contains(favOfFriend.get(i)))
               {
                   indicesToRemove.add(i);
               }
           }
           for (int i=indicesToRemove.size()-1; i>=0;i--)
           {
               favOfFriend.remove(indicesToRemove.get(i));
           }
           favOfFriends.addAll(favOfFriend);
       }

        return favOfFriends;
    }

    @Override
    public boolean getRecommendation(HungryStudent s, Restaurant r, int t) throws HungryStudent.StudentNotInSystemException, Restaurant.RestaurantNotInSystemException, ImpossibleConnectionException {
        return false;
    }

    @Override
    public String toString() {
        String stringStudents = students.keySet().stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        String stringRestaurants = restaurants.keySet().stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));

        String studentsAndFriends = "";
        for (HungryStudent s : this.students.values())
        {
            HungryStudentImpl student= (HungryStudentImpl) s;
            studentsAndFriends.concat(String.valueOf(student.getId())+" -> "); //adding the student Id to the format
            Set<HungryStudent> friend = new HashSet<>(student.getFriends());

            //for Koren to continue:)
        }


        return "";
    }
    /**
     * @return the network's description as a string in the following format:
     * <format>
     * Registered students: <studentId1, studentId2, studentId3...>.
     * Registered restaurants: <resId1, resId2, resId3...>.
     * Students:
     * <student1Id> -> [<friend1Id, friend2Id, friend3Id...>].
     * <student2Id> -> [<friend1Id, friend2Id, friend3Id...>].
     * ...
     * End students.
     * </format>
     * Note: students, restaurants and friends' ids are ordered by natural integer order, asc.*
     * Example:
     *
     * Registered students: 1, 236703, 555555.
     * Registered restaurants: 12, 13.
     * Students:
     * 1 -> [236703, 555555555].
     * 236703 -> [1].
     * 555555 -> [1].
     * End students.
     * */

}
