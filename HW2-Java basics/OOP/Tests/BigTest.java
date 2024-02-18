package OOP.Tests;

import OOP.Provided.HamburgerNetwork;
import OOP.Provided.HungryStudent;
import OOP.Provided.Restaurant;
import OOP.Solution.HamburgerNetworkImpl;
import OOP.Solution.HungryStudentImpl;
import OOP.Solution.RestaurantImpl;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class BigTest {

    @Test
    public void FinalTest() {

        HungryStudent s1 = null, s2 = null,s3 = null, s4 = null,s5 = null,s6 = null, s7 = null;
        Set<String> menu1 = new HashSet<>(), menu2 = new HashSet<>();
        menu1.add("Apple");
        menu1.add("Avocado");
        menu1.add("BigBurger");
        menu1.add("Hamburger");
        menu1.add("Fries");
        menu2.add("Steak");
        menu2.add("Fries");
        menu2.add("Orange Juice");
        Restaurant r1 = null, r2 = null, r3 = null, r4 = null, r5 = null , r6 = null , r7 = null;

        // HERE ACTUALLY STARTING THE TESTS !!
        RestaurantImpl r = new RestaurantImpl(1,"a",1231,menu1);
        assert(r.distance() == 1231);

        s1 = new HungryStudentImpl(1,"s1");
        s2 = new HungryStudentImpl(2,"s2");
        r1 = new RestaurantImpl(1,"r1",1,menu1);
        boolean catched1 = false;
        boolean catched2 = false;
        try {
            r1.rate(s1,6);
        } catch (Restaurant.RateRangeException e) {
            catched1 = true;
        }
        try{
            r1.rate(s1,-1);
        } catch (Restaurant.RateRangeException e) {
            catched2 = true;
        }
        assert(catched1);
        assert(catched2);

        r2 = new RestaurantImpl(2,"r2",1,menu1);
        try{
            r1.rate(s1,2);
            r2.rate(s1,3);
            assert(r1.numberOfRates() == 1);
            assert(r2.numberOfRates() == 1);
            assert(r1.averageRating() == 2);
            assert(r2.averageRating() == 3);
            r2.rate(s1,4);
            assert(r1.numberOfRates() == 1);
            assert(r1.averageRating() == 2);
            assert(r2.numberOfRates() == 1);
            assert(r2.averageRating() == 4);
            r2.rate(s2,5);
            assert(r1.numberOfRates() == 1);
            assert(r1.averageRating() == 2);
            assert(r2.numberOfRates() == 2);
            assert(r2.averageRating() == 4.5);
        } catch (Exception e) {
            fail();
        }
        r3 = new RestaurantImpl(2,"r3",1,menu1);
        r4 = new RestaurantImpl(2,"r4",2,menu2);
        assert(r3.equals(r4));
        assert(r4.equals(r3));
        assert(r3.equals(r2));
        assert(r2.equals(r3));
        String sr1 = "Restaurant: r1.\n" +
                "Id: 1.\n" +
                "Distance: 1.\n" +
                "Menu: Apple, Avocado, BigBurger, Fries, Hamburger.";
        String sr2 = "Restaurant: r2.\n" +
                "Id: 2.\n" +
                "Distance: 1.\n" +
                "Menu: Apple, Avocado, BigBurger, Fries, Hamburger.";
        String sr3 = "Restaurant: r3.\n" +
                "Id: 2.\n" +
                "Distance: 1.\n" +
                "Menu: Apple, Avocado, BigBurger, Fries, Hamburger.";
        String sr4 = "Restaurant: r4.\n" +
                "Id: 2.\n" +
                "Distance: 2.\n" +
                "Menu: Fries, Orange Juice, Steak.";
        assert(sr1.equals(r1.toString()));
        assert(sr2.equals(r2.toString()));
        assert(sr3.equals(r3.toString()));
        assert(sr4.equals(r4.toString()));

        assert(r1.compareTo(r2) < 0);
        assert(r2.compareTo(r3) == 0);
        assert(r3.compareTo(r2) == 0);
        assert(r2.compareTo(r1) > 0);

        boolean catched3 = false;
        try {
            s1.favorite(r3);
        } catch (HungryStudent.UnratedFavoriteRestaurantException e) {
            catched3 = true;
        }
        assert(catched3);
        assert(s1.favorites().isEmpty());
        try{
            s1.favorite(r1);
            s1.favorite(r2);
            assert(s1.favorites().contains(r1));
            assert(s1.favorites().contains(r2));
            assert(s1.favorites().contains(r3));
        } catch (HungryStudent.UnratedFavoriteRestaurantException e) {
            fail();
        }

        boolean catched4 = false;
        try {
            s1.addFriend(s1);
        } catch (HungryStudent.SameStudentException e) {
            catched4 = true;
        } catch (HungryStudent.ConnectionAlreadyExistsException e) {
            fail();
        }
        assert(catched4);
        assert(s1.getFriends().isEmpty());
        try {
            s1.addFriend(s2);
        } catch (HungryStudent.SameStudentException e) {
            fail();
        } catch (HungryStudent.ConnectionAlreadyExistsException e) {
            fail();
        }
        assert(s1.getFriends().contains(s2));
        assert(s2.getFriends().isEmpty());
        catched4 = false;
        try{
            s1.addFriend(s2);
        } catch (HungryStudent.ConnectionAlreadyExistsException e) {
            catched4 = true;
        } catch (HungryStudent.SameStudentException e) {
            fail();
        }
        assert(catched4);
        r5 = new RestaurantImpl(3,"r5",5,menu2);
        r6 = new RestaurantImpl(4,"r6",5,menu2);
        r7 = new RestaurantImpl(7,"r7",10,menu1);
        assert(s1.favoritesByRating(1).toString().equals("[Restaurant: r2.\n" +
                "Id: 2.\n" +
                "Distance: 1.\n" +
                "Menu: Apple, Avocado, BigBurger, Fries, Hamburger., Restaurant: r1.\n" +
                "Id: 1.\n" +
                "Distance: 1.\n" +
                "Menu: Apple, Avocado, BigBurger, Fries, Hamburger.]"));
        try {
            r1.rate(s1,5);
        } catch (Restaurant.RateRangeException e) {
            fail();
        }
        assert (s1.favoritesByRating(1).toString().equals("[Restaurant: r1.\n" +
                "Id: 1.\n" +
                "Distance: 1.\n" +
                "Menu: Apple, Avocado, BigBurger, Fries, Hamburger., Restaurant: r2.\n" +
                "Id: 2.\n" +
                "Distance: 1.\n" +
                "Menu: Apple, Avocado, BigBurger, Fries, Hamburger.]"));
        try {
            r1.rate(s2,4);
        } catch (Restaurant.RateRangeException e) {
            fail();
        }
        assert (s1.favoritesByRating(1).toString().equals("[Restaurant: r1.\n" +
                "Id: 1.\n" +
                "Distance: 1.\n" +
                "Menu: Apple, Avocado, BigBurger, Fries, Hamburger., Restaurant: r2.\n" +
                "Id: 2.\n" +
                "Distance: 1.\n" +
                "Menu: Apple, Avocado, BigBurger, Fries, Hamburger.]"));
        try {
            r5.rate(s1,5);
        } catch (Restaurant.RateRangeException e) {
            fail();
        }
        try {
            s1.favorite(r5);
        } catch (HungryStudent.UnratedFavoriteRestaurantException e) {
            fail();
        }
        assert (s1.favoritesByRating(1).toString().equals("[Restaurant: r5.\n" +
                "Id: 3.\n" +
                "Distance: 5.\n" +
                "Menu: Fries, Orange Juice, Steak., Restaurant: r1.\n" +
                "Id: 1.\n" +
                "Distance: 1.\n" +
                "Menu: Apple, Avocado, BigBurger, Fries, Hamburger., Restaurant: r2.\n" +
                "Id: 2.\n" +
                "Distance: 1.\n" +
                "Menu: Apple, Avocado, BigBurger, Fries, Hamburger.]"));
        try {
            r7.rate(s1,5);
        } catch (Restaurant.RateRangeException e) {
            fail();
        }
        try {
            s1.favorite(r7);
        } catch (HungryStudent.UnratedFavoriteRestaurantException e) {
            fail();
        }
        assert(s1.favoritesByRating(1).toString().equals("[Restaurant: r5.\n" +
                "Id: 3.\n" +
                "Distance: 5.\n" +
                "Menu: Fries, Orange Juice, Steak., Restaurant: r7.\n" +
                "Id: 7.\n" +
                "Distance: 10.\n" +
                "Menu: Apple, Avocado, BigBurger, Fries, Hamburger., Restaurant: r1.\n" +
                "Id: 1.\n" +
                "Distance: 1.\n" +
                "Menu: Apple, Avocado, BigBurger, Fries, Hamburger., Restaurant: r2.\n" +
                "Id: 2.\n" +
                "Distance: 1.\n" +
                "Menu: Apple, Avocado, BigBurger, Fries, Hamburger.]"));
        assert (s1.favoritesByRating(4).toString().equals("[Restaurant: r5.\n" +
                "Id: 3.\n" +
                "Distance: 5.\n" +
                "Menu: Fries, Orange Juice, Steak., Restaurant: r7.\n" +
                "Id: 7.\n" +
                "Distance: 10.\n" +
                "Menu: Apple, Avocado, BigBurger, Fries, Hamburger., Restaurant: r1.\n" +
                "Id: 1.\n" +
                "Distance: 1.\n" +
                "Menu: Apple, Avocado, BigBurger, Fries, Hamburger., Restaurant: r2.\n" +
                "Id: 2.\n" +
                "Distance: 1.\n" +
                "Menu: Apple, Avocado, BigBurger, Fries, Hamburger.]"));
        assert(s1.favoritesByRating(5).toString().equals("[Restaurant: r5.\n" +
                "Id: 3.\n" +
                "Distance: 5.\n" +
                "Menu: Fries, Orange Juice, Steak., Restaurant: r7.\n" +
                "Id: 7.\n" +
                "Distance: 10.\n" +
                "Menu: Apple, Avocado, BigBurger, Fries, Hamburger.]"));

        RestaurantImpl r8 = new RestaurantImpl(8,"r8",1,menu1);
        try {
            r7.rate(s1,4);
            r8.rate(s1,4);

        } catch (Restaurant.RateRangeException e) {
            fail();
        }

        try {
            s1.favorite(r7);
            s1.favorite(r8);
        } catch (HungryStudent.UnratedFavoriteRestaurantException e) {
            fail();
        }
        assert (s1.favoritesByDist(10).toString().equals("[Restaurant: r1.\n" +
                "Id: 1.\n" +
                "Distance: 1.\n" +
                "Menu: Apple, Avocado, BigBurger, Fries, Hamburger., Restaurant: r2.\n" +
                "Id: 2.\n" +
                "Distance: 1.\n" +
                "Menu: Apple, Avocado, BigBurger, Fries, Hamburger., Restaurant: r8.\n" +
                "Id: 8.\n" +
                "Distance: 1.\n" +
                "Menu: Apple, Avocado, BigBurger, Fries, Hamburger., Restaurant: r5.\n" +
                "Id: 3.\n" +
                "Distance: 5.\n" +
                "Menu: Fries, Orange Juice, Steak., Restaurant: r7.\n" +
                "Id: 7.\n" +
                "Distance: 10.\n" +
                "Menu: Apple, Avocado, BigBurger, Fries, Hamburger.]"));
        s3 = new HungryStudentImpl(2,"s3");
        s4 = new HungryStudentImpl(4,"s4");
        assert (!s1.equals(s2));
        assert (!s2.equals(s1));
        assert (s2.equals(s3));
        assert (s3.equals(s2));
        assert (s1.toString().equals("Hungry student: s1.\n" +
                "Id: 1.\n" +
                "Favorites: r1, r2, r5, r7, r8."));
        try {
            s2.favorite(r2);
        } catch (HungryStudent.UnratedFavoriteRestaurantException e) {
            fail();
        }
        assert (s2.toString().equals("Hungry student: s2.\n" +
                "Id: 2.\n" +
                "Favorites: r2."));
        assert (s3.toString().equals("Hungry student: s3.\n" +
                "Id: 2.\n" +
                "Favorites: ."));
        HamburgerNetwork network = new HamburgerNetworkImpl();
        assert (network.toString().equals("Registered students: .\n" +
                "Registered restaurants: .\n" +
                "Students:\n" +
                "End students."));
        HungryStudent ns1 = null;
        HungryStudent ns2 = null;
        HungryStudent ns3 = null;
        try {
            ns1 = network.joinNetwork(1,"ns1");
            ns2 = network.joinNetwork(2,"ns2");
            ns3 = network.joinNetwork(3,"ns3");
        } catch (HungryStudent.StudentAlreadyInSystemException e) {
            fail();
        }
        boolean nscastch = false;
        try {
            ns1 = network.joinNetwork(1,"nns1");
        } catch (HungryStudent.StudentAlreadyInSystemException e) {
            nscastch = true;
        }
        assert (nscastch);
        Restaurant nr1 = null, nr2 = null, nr3 = null, nr4 = null, nr5 = null , nr6 = null , nr7 = null, nr8;

        try {
            nr1 = network.addRestaurant(1,"nr1",1,menu1);
            nr2 = network.addRestaurant(2,"nr2",1,menu1);
            nr3 = network.addRestaurant(3,"nr3",1,menu1);
            nr4 = network.addRestaurant(4,"nr4",2,menu2);
            nr5 = network.addRestaurant(5,"nr5",5,menu2);
            nr6 = network.addRestaurant(6,"nr6",5,menu2);
            nr7 = network.addRestaurant(7,"nr7",10,menu2);
            nr8 = network.addRestaurant(8,"nr8",1,menu2);
        } catch (Restaurant.RestaurantAlreadyInSystemException e) {
            fail();
        }
        nscastch = false;
        try {
            nr1 = network.addRestaurant(1,"nnr1",2,menu2);
        } catch (Restaurant.RestaurantAlreadyInSystemException e) {
            nscastch = true;
        }
        assert (nscastch);
        assert (network.toString().equals("Registered students: 1, 2, 3.\n" +
                "Registered restaurants: 1, 2, 3, 4, 5, 6, 7, 8.\n" +
                "Students:\n" +
                "1 -> [].\n" +
                "2 -> [].\n" +
                "3 -> [].\n" +
                "End students."));
        assert (network.registeredStudents().size() == 3);
        assert (network.registeredRestaurants().size() == 8);
        try {
            assert (network.getStudent(1).equals(ns1));
        } catch (HungryStudent.StudentNotInSystemException e) {
            fail();
        }
        nscastch = false;
        try {
            network.getStudent(4);
        } catch (HungryStudent.StudentNotInSystemException e) {
            nscastch = true;
        }
        assert (nscastch);

        try {
            network.getRestaurant(1).equals(nr1);
        } catch (Restaurant.RestaurantNotInSystemException e) {
            fail();
        }
        nscastch = false;
        try {
            network.getRestaurant(10);
        } catch (Restaurant.RestaurantNotInSystemException e) {
            nscastch = true;
        }
        assert (nscastch);
        nscastch = false;
        try {
            network.addConnection(ns1,ns1);
        } catch (HungryStudent.StudentNotInSystemException e) {
            throw new RuntimeException(e);
        } catch (HungryStudent.ConnectionAlreadyExistsException e) {
            throw new RuntimeException(e);
        } catch (HungryStudent.SameStudentException e) {
            nscastch = true;
        }
        assert (nscastch);
        nscastch = false;
        try {
            network.addConnection(ns1,ns2);
            network.addConnection(ns2,ns3);
            assert (ns1.getFriends().contains(ns2));
            assert (ns2.getFriends().contains(ns1));
            assert (ns2.getFriends().contains(ns3));
            assert (ns3.getFriends().contains(ns2));

        } catch (HungryStudent.StudentNotInSystemException e) {
            fail();
        } catch (HungryStudent.ConnectionAlreadyExistsException e) {
            fail();
        } catch (HungryStudent.SameStudentException e) {
            fail();
        }

        nscastch = false;
        try {
            network.addConnection(ns1,s2);
        } catch (HungryStudent.StudentNotInSystemException e) {
            fail();
        } catch (HungryStudent.ConnectionAlreadyExistsException e) {
            nscastch = true;
        } catch (HungryStudent.SameStudentException e) {
            fail();
        }
        assert (nscastch);

        nscastch = false;
        try {
            network.addConnection(ns1,ns2);
        } catch (HungryStudent.StudentNotInSystemException e) {
            throw new RuntimeException(e);
        } catch (HungryStudent.ConnectionAlreadyExistsException e) {
            nscastch = true;
        } catch (HungryStudent.SameStudentException e) {
            throw new RuntimeException(e);
        }
        assert (nscastch);
        try {
            nr1.rate(ns1,5);
            nr1.rate(ns2,4);
            nr2.rate(ns1,5);
            nr2.rate(ns2,4);
            nr5.rate(ns3,5);
            nr6.rate(ns3,3);
            nr7.rate(ns3,2);
            nr7.rate(ns1,4);
            ns1.favorite(nr1);
            ns1.favorite(nr7);
            ns2.favorite(nr2);
            ns3.favorite(nr5);
            ns3.favorite(nr6);
            ns3.favorite(nr7);

        } catch (Restaurant.RateRangeException e) {
            fail();
        } catch (HungryStudent.UnratedFavoriteRestaurantException e) {
            fail();
        }
        try {
            assert (network.favoritesByRating(ns1).toString().equals("[Restaurant: nr2.\n" +
                    "Id: 2.\n" +
                    "Distance: 1.\n" +
                    "Menu: Apple, Avocado, BigBurger, Fries, Hamburger.]"));

            assert (network.favoritesByRating(ns2).toString().equals("[Restaurant: nr1.\n" +
                    "Id: 1.\n" +
                    "Distance: 1.\n" +
                    "Menu: Apple, Avocado, BigBurger, Fries, Hamburger., Restaurant: nr7.\n" +
                    "Id: 7.\n" +
                    "Distance: 10.\n" +
                    "Menu: Fries, Orange Juice, Steak., Restaurant: nr5.\n" +
                    "Id: 5.\n" +
                    "Distance: 5.\n" +
                    "Menu: Fries, Orange Juice, Steak., Restaurant: nr6.\n" +
                    "Id: 6.\n" +
                    "Distance: 5.\n" +
                    "Menu: Fries, Orange Juice, Steak.]"));
            assert (network.favoritesByRating(ns3).toString().equals("[Restaurant: nr2.\n" +
                    "Id: 2.\n" +
                    "Distance: 1.\n" +
                    "Menu: Apple, Avocado, BigBurger, Fries, Hamburger.]"));
        } catch (HungryStudent.StudentNotInSystemException e) {
            fail();
        }

        try {
            assert (network.favoritesByDist(ns1).toString().equals("[Restaurant: nr2.\n" +
                    "Id: 2.\n" +
                    "Distance: 1.\n" +
                    "Menu: Apple, Avocado, BigBurger, Fries, Hamburger.]"));
            assert (network.favoritesByDist(ns2).toString().equals("[Restaurant: nr1.\n" +
                    "Id: 1.\n" +
                    "Distance: 1.\n" +
                    "Menu: Apple, Avocado, BigBurger, Fries, Hamburger., Restaurant: nr7.\n" +
                    "Id: 7.\n" +
                    "Distance: 10.\n" +
                    "Menu: Fries, Orange Juice, Steak., Restaurant: nr5.\n" +
                    "Id: 5.\n" +
                    "Distance: 5.\n" +
                    "Menu: Fries, Orange Juice, Steak., Restaurant: nr6.\n" +
                    "Id: 6.\n" +
                    "Distance: 5.\n" +
                    "Menu: Fries, Orange Juice, Steak.]"));
            assert (network.favoritesByDist(ns3).toString().equals("[Restaurant: nr2.\n" +
                    "Id: 2.\n" +
                    "Distance: 1.\n" +
                    "Menu: Apple, Avocado, BigBurger, Fries, Hamburger.]"));


        } catch (HungryStudent.StudentNotInSystemException e) {
            fail();
        }
        assert(network.toString().equals("Registered students: 1, 2, 3.\n" +
                "Registered restaurants: 1, 2, 3, 4, 5, 6, 7, 8.\n" +
                "Students:\n" +
                "1 -> [2].\n" +
                "2 -> [1, 3].\n" +
                "3 -> [2].\n" +
                "End students."));
        nscastch = false;
        try {
            network.getRecommendation(s7,nr1,1);
        } catch (HungryStudent.StudentNotInSystemException e) {
            nscastch = true;
        } catch (Restaurant.RestaurantNotInSystemException e) {
            fail();
        } catch (HamburgerNetwork.ImpossibleConnectionException e) {
            fail();
        }
        assert (nscastch);
        RestaurantImpl r9 = new RestaurantImpl(100,"r9",2,menu2);
        nscastch = false;
        try {
            network.getRecommendation(ns1,r9,1);
        } catch (HungryStudent.StudentNotInSystemException e) {
            fail();
        } catch (Restaurant.RestaurantNotInSystemException e) {
            nscastch = true;
        } catch (HamburgerNetwork.ImpossibleConnectionException e) {
            fail();
        }
        assert (nscastch);
        nscastch = false;
        try {
            network.getRecommendation(ns1,nr1,-1);
        } catch (HungryStudent.StudentNotInSystemException e) {
            fail();
        } catch (Restaurant.RestaurantNotInSystemException e) {
            fail();
        } catch (HamburgerNetwork.ImpossibleConnectionException e) {
            nscastch = true;
        }
        assert (nscastch);
        try {
            assert (network.getRecommendation(ns1,nr2,1));
            assert (!network.getRecommendation(ns1, nr2, 0));
            assert (!network.getRecommendation(ns1,nr5,1));
            assert (network.getRecommendation(ns1,nr5,2));

        } catch (HungryStudent.StudentNotInSystemException e) {
            fail();
        } catch (Restaurant.RestaurantNotInSystemException e) {
            fail();
        } catch (HamburgerNetwork.ImpossibleConnectionException e) {
            fail();
        }

        Restaurant rt1 = new RestaurantImpl(1,"rt1",1,menu1);
        Restaurant rt2 = new RestaurantImpl(1,"rt2",2,menu2);
        HungryStudent s = new HungryStudentImpl(15, "Aviad");

        try {
            rt1.rate(s, 1);
            rt2.rate(s, 2);
        } catch (Restaurant.RateRangeException e) {
            fail();
        }

        try {
            s.favorite(rt1);
            s.favorite(rt2);
        } catch (HungryStudent.UnratedFavoriteRestaurantException e) {
            fail();
        }
        assert (s.favorites().toString().equals("[Restaurant: rt1.\n" +
                "Id: 1.\n" +
                "Distance: 1.\n" +
                "Menu: Apple, Avocado, BigBurger, Fries, Hamburger.]"));
        assert (s.favorites().contains(rt1));
        assert (s.favorites().contains(rt2));

        Restaurant rtt1 = new RestaurantImpl(1,"BBB",3,menu1);
        Restaurant rtt2 = new RestaurantImpl(1,"BBB2",4,menu2);
        HungryStudent stt = new HungryStudentImpl(15, "Daniel");

        try {
            rtt1.rate(stt, 4);
        } catch (Restaurant.RateRangeException e) {
            fail();
        }
        nscastch = false;
        try {
            stt.favorite(rtt2);
        } catch (HungryStudent.UnratedFavoriteRestaurantException e) {
            nscastch = true;
        }
        assert (nscastch);
    }

}
