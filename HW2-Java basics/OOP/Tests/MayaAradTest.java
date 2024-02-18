package OOP.Tests;

import OOP.Provided.*;
import OOP.Provided.Restaurant.*;
import OOP.Provided.HungryStudent.*;
import OOP.Provided.HamburgerNetwork.*;
import OOP.Solution.*;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.Before;

public class MayaAradTest {
    private HamburgerNetwork network;
    private HungryStudent s1, s2, s3, s4, s5, s6;
    private Restaurant r1, r2, r3, r4, r5;
    private Set<String> menu1, menu2, menu3;

    @Before
    public void init() {
        initNetowrk();
        initRestaurants();
        initStudents();
    }

    private void initNetowrk() {
        network = new HamburgerNetworkImpl();
    }

    private void initStudents() {
        try {
            s1 = network.joinNetwork(1, "Maya");
            s2 = network.joinNetwork(69420, "Arad");
            s3 = network.joinNetwork(666, "Mr. Satan");
            s4 = network.joinNetwork(9001, "Goku");
            s5 = network.joinNetwork(9000, "Vegeta");
            s6 = network.joinNetwork(6, "Bulma");
        } catch (StudentAlreadyInSystemException e) {
            fail();
        }
    }

    private void initRestaurants() {
        menu1 = new HashSet<>();
        menu2 = new HashSet<>();
        menu3 = new HashSet<>();

        menu1.add("Hamburger");
        menu1.add("Fries");
        menu2.add("Steak");
        menu2.add("Fries");
        menu2.add("Orange Juice");
        menu3.add("Hamburger");
        menu3.add("Fries");
        menu3.add("Orange Juice");
        menu3.add("Salad");
        menu3.add("Coke");

        try {
            r1 = network.addRestaurant(1, "McDonald's", 10, menu1);
            r2 = network.addRestaurant(2, "Shony's", 100, menu2);
            r3 = network.addRestaurant(300, "Burger King", 1000, menu1);
            r4 = network.addRestaurant(40, "Pizza Hut", 1000, menu3);
            r5 = network.addRestaurant(5, "Subway", 100, menu3);
        } catch (RestaurantAlreadyInSystemException e) {
            fail();
        }
    }

    @Test
    public void SameStudentExceptionTest() {
        assertThrows(SameStudentException.class, () -> {
            network.addConnection(s1, s1);
        });
    }

    @Test
    public void StudentAlreadyInSystemExceptionTest() {
        assertThrows(StudentAlreadyInSystemException.class, () -> {
            network.joinNetwork(69420, "Moshe");
        });
        try {
            network.joinNetwork(42069, "Arad");
        } catch (StudentAlreadyInSystemException e) {
            fail();
        }
    }

    @Test
    public void StudentNotInSystemException() {
        assertThrows(StudentNotInSystemException.class, () -> {
            network.addConnection(new HungryStudentImpl(2, "Eren"), s1);
        });
    }

    @Test
    public void UnratedFavoriteRestaurantExceptionTest() {
        assertThrows(UnratedFavoriteRestaurantException.class, () -> {
            s1.favorite(r1);
        });
    }

    @Test
    public void ConnectionAlreadyExistsExceptionTest() {
        try {
            network.addConnection(s1, s2);
        } catch (Exception e) {
            fail();
        }
        assertThrows(ConnectionAlreadyExistsException.class, () -> {
            network.addConnection(s1, s2);
        });
    }

    @Test
    public void RestaurantAlreadyInSystemExceptionTest() {
        assertThrows(RestaurantAlreadyInSystemException.class, () -> {
            network.addRestaurant(1, "McDavid", 5, menu1);
        });
    }

    @Test
    public void RestaurantNotInSystemExceptionTest() {
        assertThrows(RestaurantNotInSystemException.class, () -> {
            network.getRestaurant(3);
        });
    }

    @Test
    public void RateRangeExceptionTest() {
        assertThrows(RateRangeException.class, () -> {
            r1.rate(s1, 6);
        });
        assertThrows(RateRangeException.class, () -> {
            r1.rate(s1, -1);
        });
    }

    @Test
    public void ImpossibleConnectionExceptionTest() {
        assertThrows(ImpossibleConnectionException.class, () -> {
            network.getRecommendation(s1, r1, -1);
        });
    }

    @Test
    public void AverageRatingTest() {
        try {
            r1.rate(s1, 5);
            r1.rate(s2, 3);
            r1.rate(s3, 4);
        } catch (Exception e) {
            fail();
        }
        assertEquals(4, r1.averageRating(), 0.01);
    }

    @Test
    public void RestaurantStringTest() {
        assertEquals("Restaurant: McDonald's.\n" + //
                        "Id: 1.\n" + //
                        "Distance: 10.\n" + //
                        "Menu: Fries, Hamburger.", r1.toString());
    }

    @Test
    public void FavoritesTest() {
        try {
            r1.rate(s1, 0);
            r1.rate(s2, 5);
            r2.rate(s1, 0);
            s1.favorite(r1);
            s1.favorite(r2);
            s2.favorite(r1);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertEquals(2, s1.favorites().size());
        assertEquals(1, s2.favorites().size());
    }

    @Test
    public void FriendsTest() {
        try {
            network.addConnection(s1, s2);
            network.addConnection(s1, s3);
            network.addConnection(s2, s3);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertEquals(2, s1.getFriends().size());
        assertEquals(2, s2.getFriends().size());
        assertEquals(2, s3.getFriends().size());
    }

    @Test
    public void StudentFavoritesByRatingTest() {
        try {
            r1.rate(s1, 5);
            r2.rate(s1, 3);
            r4.rate(s1, 2);
            r5.rate(s1, 3);
            s1.favorite(r1);
            s1.favorite(r2);
            s1.favorite(r4);
            s1.favorite(r5);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertEquals(s1.favoritesByRating(3), Arrays.asList(r1, r2, r5));
    }

    @Test
    public void StudentFavoritesByDistTest() {
        try {
            r1.rate(s1, 5);
            r2.rate(s1, 3);
            r4.rate(s1, 2);
            r5.rate(s1, 4);
            s1.favorite(r1);
            s1.favorite(r2);
            s1.favorite(r4);
            s1.favorite(r5);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertEquals(s1.favoritesByDist(100), Arrays.asList(r1, r5, r2));
    }

    @Test
    public void getRecommendationTest() {
        try {
            r1.rate(s6, 5);
            s6.favorite(r1);

            network.addConnection(s1, s2)
                    .addConnection(s2, s3)
                    .addConnection(s3, s4)
                    .addConnection(s4, s5)
                    .addConnection(s5, s6);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        try {
            assertTrue(network.getRecommendation(s1, r1, 5));
            assertFalse(network.getRecommendation(s1, r1, 4));
        } catch (Exception e) {
            fail(e.getMessage());
        }

        try {
            network.addConnection(s1, s4);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        try {
            assertTrue(network.getRecommendation(s1, r1, 5));
            assertTrue(network.getRecommendation(s1, r1, 3));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void NetworkFavoritesBy() {
        try {
            r1.rate(s1, 5);
            s1.favorite(r1);

            r2.rate(s2, 0);
            s2.favorite(r2);

            r2.rate(s3, 0);
            s3.favorite(r2);

            r3.rate(s4, 4);
            r4.rate(s4, 5);
            s4.favorite(r3);
            s4.favorite(r4);

            r4.rate(s5, 5);
            r5.rate(s5, 5);
            s5.favorite(r4);
            s5.favorite(r5);

            network.addConnection(s1, s2)
                    .addConnection(s1, s3)
                    .addConnection(s1, s4)
                    .addConnection(s4, s5)
                    .addConnection(s2, s6);
            
            assertEquals(network.favoritesByRating(s1), Arrays.asList(r2, r4, r3));

            r3.rate(s1, 5);
            r3.rate(s2, 5);
            r3.rate(s3, 5);
            r4.rate(s4, 4);
            assertEquals(network.favoritesByRating(s1), Arrays.asList(r2, r3, r4));

            r5.rate(s4, 0);
            s4.favorite(r5);

            assertEquals(network.favoritesByDist(s1), Arrays.asList(r2, r5, r3, r4));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void FavoriteNotInSystemTest() {
        Restaurant or1 = new RestaurantImpl(0, null, 0, menu1);
        Restaurant or2 = new RestaurantImpl(0, null, 0, menu1);
        try {
            or1.rate(s1, 0);
        } catch (RateRangeException e) {
            fail(e.getMessage());
        }
        assertThrows(UnratedFavoriteRestaurantException.class, () -> {
            s1.favorite(or2);
        });
    }

}