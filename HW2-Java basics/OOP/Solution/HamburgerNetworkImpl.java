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
        if (students.containsKey(id)) {
            throw new HungryStudent.StudentAlreadyInSystemException();
        }

        HungryStudentImpl student = new HungryStudentImpl(id, name);
        students.put(id, student);
        return student;
    }

    @Override
    public Restaurant addRestaurant(int id, String name, int dist, Set<String> menu) throws Restaurant.RestaurantAlreadyInSystemException {
        RestaurantImpl r = new RestaurantImpl(id,name,dist,menu);
        if (this.restaurants.containsKey(r.getId())) // Check the input
        {
            throw new Restaurant.RestaurantAlreadyInSystemException();
        }
        this.restaurants.put(r.getId(),r);
        return r;
    }

    @Override
    public Collection<HungryStudent> registeredStudents() {
        return new ArrayList<>(students.values());
    }

    @Override
    public Collection<Restaurant> registeredRestaurants() {
        return new ArrayList<>(this.restaurants.values());
    }

    @Override
    public HungryStudent getStudent(int id) throws HungryStudent.StudentNotInSystemException {
        HungryStudent student = students.get(id);

        if (student == null) {
            throw new HungryStudent.StudentNotInSystemException();
        }

        return student;
    }

    @Override
    public Restaurant getRestaurant(int id) throws Restaurant.RestaurantNotInSystemException {
        if(!this.restaurants.containsKey(id)) //Check the input.
        {
            throw new Restaurant.RestaurantNotInSystemException();
        }
        return this.restaurants.get(id);
    }

    @Override
    public HamburgerNetwork addConnection(HungryStudent s1, HungryStudent s2) throws HungryStudent.StudentNotInSystemException, HungryStudent.ConnectionAlreadyExistsException, HungryStudent.SameStudentException {
        if(!this.students.containsValue(s1)|| !this.students.containsValue(s2)) {
        throw new HungryStudent.StudentNotInSystemException();
        }
        s1.addFriend(s2);
        s2.addFriend(s1);
        return this;
    }

    @Override
    public Collection<Restaurant> favoritesByRating(HungryStudent s) throws HungryStudent.StudentNotInSystemException {
        if (!students.containsValue(s)) {
            throw new HungryStudent.StudentNotInSystemException();
        }

        List<Restaurant> favOfFriends = new ArrayList<>(); //the result we are expecting
        Set<HungryStudent> sortedFriends = s.getFriends();

        for (HungryStudent friend : sortedFriends) {// We will iterate over the student's friends.
            List<Restaurant> favOfFriend = new ArrayList<>(friend.favoritesByRating(0));

            //now we will not add duplications

            // Remove duplicates
            favOfFriend.removeAll(favOfFriends);

            // Add the favorite restaurants of the friend to the result list
            favOfFriends.addAll(favOfFriend);

        }

        return favOfFriends;
    }

    @Override
    public Collection<Restaurant> favoritesByDist(HungryStudent s) throws HungryStudent.StudentNotInSystemException {
        if(!students.containsValue(s))
        {
            throw new HungryStudent.StudentNotInSystemException();
        }
        List<Restaurant> favOfFriends = new ArrayList<>(); //the result we are expecting
       for (HungryStudent friend: s.getFriends()) // We will iterate over the student's friends.
       {
           List<Restaurant> favOfFriend = new ArrayList<>(friend.favoritesByDist(Integer.MAX_VALUE)); // Get the desired order of the friend's favorites

           //now we will not add duplications
           // Remove duplicates
           favOfFriend.removeAll(favOfFriends);

           // Add the favorite restaurants of the friend to the result list
           favOfFriends.addAll(favOfFriend);
       }

        return favOfFriends;
    }

    @Override
    public boolean getRecommendation(HungryStudent s, Restaurant r, int t) throws HungryStudent.StudentNotInSystemException, Restaurant.RestaurantNotInSystemException, ImpossibleConnectionException {
//First, lest validate the arguments.
        if(!students.containsValue(s))
        {
            throw new HungryStudent.StudentNotInSystemException();
        }
        if(!restaurants.containsValue(r))
        {
            throw new Restaurant.RestaurantNotInSystemException();
        }
        if(t<0)
        {
            throw new ImpossibleConnectionException();
        }

        Queue<HungryStudent> queueStudents = new LinkedList<>(); // Queue to hold students
        Queue<Integer> queueDist = new LinkedList<>(); // Queue to hold students corresponding distance from 's'

        Set<Integer> visited = new HashSet<>(); // To keep track of visited students

        queueStudents.add(s); // Start with the given student, distance 0
        queueDist.add(0);
        visited.add(((HungryStudentImpl)s).getId()); // Mark as visited

        while (!queueStudents.isEmpty()) {
            // Current student and their distance from 's'
            HungryStudent currentStudent = queueStudents.poll();
            int distance = queueDist.poll();

            // Check if the current student likes the restaurant and is within 't' hops
            if (distance <= t && currentStudent.favorites().contains(r)) {
                return true;
            }

            // If not, put in the queue all the friends of the current student, if they haven't been visited
            if (distance < t) { // Only consider friends if we haven't reached the limit 't'
                for (HungryStudent friend : currentStudent.getFriends()) {
                    if (!visited.contains(((HungryStudentImpl)friend).getId())) {
                        visited.add(((HungryStudentImpl)friend).getId());
                        // Increment distance for the next level
                        queueStudents.add(friend);
                        queueDist.add(distance+1);
                    }
                }
            }
        }
        return false; // Restaurant 'r' was not found within 't' hops
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        // Registered students
        result.append("Registered students: ")
                .append(students.keySet().stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(", ")))
                .append(".\n");

        // Registered restaurants
        result.append("Registered restaurants: ")
                .append(restaurants.keySet().stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(", ")))
                .append(".\n");

        // Students and their friends
        result.append("Students:\n");
        for (Map.Entry<Integer,HungryStudent> entry : this.students.entrySet()) {
            result.append(entry.getKey()).append(" -> [");
            Set<HungryStudent> friends = new HashSet<>(entry.getValue().getFriends());
            List<Integer> friendsIds = new ArrayList<>();
            for (HungryStudent friend:friends)
            {
                friendsIds.add(((HungryStudentImpl)friend).getId());
            }
            result.append(friendsIds.stream()
                            .map(Object::toString)
                            .collect(Collectors.joining(", ")))
                    .append("].\n");
        }
        result.append("End students.");

        return result.toString();
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
