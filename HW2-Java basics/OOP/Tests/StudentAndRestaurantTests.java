package OOP.Tests;

import OOP.Provided.HungryStudent;
import OOP.Provided.HungryStudent.*;
import OOP.Provided.Restaurant;
import OOP.Provided.Restaurant.RateRangeException;
import OOP.Provided.Restaurant.RestaurantAlreadyInSystemException;
import OOP.Provided.Restaurant.RestaurantNotInSystemException;
import OOP.Solution.HamburgerNetworkImpl;
import OOP.Solution.HungryStudentImpl;
import OOP.Solution.RestaurantImpl;
import org.junit.Test;

import java.util.*;
import java.util.function.Predicate;

import static org.junit.Assert.*;
public class StudentAndRestaurantTests {
    @Test
    public void StudentAndRestaurantTestsTest() {
        Set<String> menu1 = new HashSet<>(), menu2 = new HashSet<>();
        menu1.add("Hamburger");
        menu1.add("Fries");
        menu2.add("Steak");
        menu2.add("Fries");
        menu2.add("Orange Juice");

        Restaurant r1=new RestaurantImpl(10, "BBB", 12, menu1);
        Restaurant r2=new RestaurantImpl(12, "Bob's place", 5, menu2);
        Restaurant r3=new RestaurantImpl(14, "Ben's hut", 1, menu1);

        HungryStudent s1 = new HungryStudentImpl(100, "Anne");
        HungryStudent s2 = new HungryStudentImpl(300, "Ben");


        try {
            r1.rate(s1, 4)
                    .rate(s2, 5);

            r2.rate(s1, 2)
                    .rate(s1, 3)
                    .rate(s2, 4);
            r3.rate(s2, 4);
        } catch (RateRangeException e) {
            fail();
        }

        assertEquals(2, r1.numberOfRates(), 0);
        assertEquals(2, r2.numberOfRates(), 0);
        assertEquals(3.5, r2.averageRating(), 0);

        try {
            s1.favorite(r1)
                    .favorite(r2);
            s2.favorite(r2)
                    .favorite(r3)
                    .favorite(r1);
        } catch (UnratedFavoriteRestaurantException e) {
            fail();
        }


        Iterator<Restaurant> s1RateIterator = s1.favoritesByRating(1).iterator();
        Iterator<Restaurant> s2DistIterator = s2.favoritesByDist(6).iterator();

        assertEquals(s1RateIterator.next(), r1);
        assertEquals(s1RateIterator.next(), r2);
        assertEquals(s1RateIterator.hasNext(), false);
       // assertEquals(s2DistIterator.hasNext(),false);
        assertEquals(s2DistIterator.next(), r3);
        assertEquals(s2DistIterator.next(), r2);
      assertEquals(s2DistIterator.hasNext(),false);
    }

}
