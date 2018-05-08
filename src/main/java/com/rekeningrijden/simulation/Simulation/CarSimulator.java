package com.rekeningrijden.simulation.Simulation;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.rekeningrijden.simulation.entities.Car;
import com.rekeningrijden.simulation.entities.Coordinate;
import com.rekeningrijden.simulation.entities.Route;
import com.rekeningrijden.simulation.entities.SubRoute;
import io.jenetics.jpx.GPX;
import io.jenetics.jpx.Track;
import io.jenetics.jpx.TrackSegment;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CarSimulator {
    private final static Logger logger = Logger.getLogger(CarSimulator.class);

    private List<Car> cars;
    private List<Route> routes;
    private List<Thread> journeys;
    private Random rndm;
    private MessageProducer messageProducer;

    public CarSimulator() {
        cars = new ArrayList<>();
        routes = new ArrayList<>();
        journeys = new ArrayList<>();
        rndm = new Random();
        messageProducer = new MessageProducer();
        loadCarsFromJson();
        loadRoutesFromGPX();
        createJourneys();
    }

    private void loadCarsFromJson() {
        JSONArray jsonarray = null;
        try {
            URL url = Resources.getResource("trackers.json");
            String text = Resources.toString(url, Charsets.UTF_8);

            jsonarray = new JSONArray(new JSONTokener(text));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject jsonobject = jsonarray.getJSONObject(i);
            String id = jsonobject.getString("id");
            String country = jsonobject.getString("country");
            Car newCar = new Car(id, country);
            cars.add(newCar);
        }

        logger.debug(cars.size() + " cars have been initialized.");
    }

    private void loadRoutesFromGPX() {
        String routesRoot = Resources.getResource("routes/").getPath();
        Path routesRootPath = Paths.get(routesRoot);
        Map<String, List<SubRoute>> intermediateRoutes = new HashMap<>();

        try {
            /**
             * Walk through all files found (recursively) in the routes resource
             * folder. Relativize the file name so that it is more readable.
             */
            Files
                    .walk(routesRootPath)
                    .filter(Files::isRegularFile)
                    .map(path -> routesRootPath.relativize(path).toString())
                    .forEach(subrouteResource -> {
                        /**
                         * Retrieve the country code by regex from the file
                         * path. An example of such file path is as follows:
                         * parma-udine/A-IT.gpx.
                         *
                         * Capture groups:
                         * 0: complete string
                         * 1: route name
                         * 2: subroute counter (alphabetical)
                         * 3: country code via the ISO specification, two
                         * capitalized alphabetical characters.
                         */
                        Pattern pattern = Pattern.compile("([a-zA-Z-]+)\\/([A-Z]+)\\-([A-Z]{2})");
                        Matcher matcher = pattern.matcher(subrouteResource);

                        if (!matcher.find()) {
                            logger.warn("The convention determined by the regex could not be found for the file with the location " + subrouteResource);
                            return;
                        }

                        String routeName = matcher.group(1);
                        String countryCode = matcher.group(3);
                        SubRoute subroute = new SubRoute(countryCode);

                        try {
                            /**
                             * Read the GPX file from the resources folder and
                             * map it to a coordinate.
                             */
                            GPX tracks = GPX.read(Resources.getResource("routes/" + subrouteResource).openStream());
                            List<Coordinate> coordinates = tracks
                                    .tracks()
                                    .flatMap(Track::segments)
                                    .flatMap(TrackSegment::points)
                                    .map(coordinate -> new Coordinate(coordinate.getLatitude().doubleValue(), coordinate.getLongitude().doubleValue()))
                                    .collect(Collectors.toList());

                            subroute.getCoordinates().addAll(coordinates);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        /**
                         * If the route does not exist yet in the map, create it
                         * with a blank list of subroutes.
                         */
                        if (!intermediateRoutes.containsKey(routeName))
                            intermediateRoutes.put(routeName, new ArrayList<>());

                        /**
                         * Add the subroutes to the map.
                         */
                        intermediateRoutes.get(routeName).add(subroute);
                    });

            this.routes = intermediateRoutes
                    .entrySet()
                    .stream()
                    .map(entrySet -> new Route(entrySet.getKey(), entrySet.getValue()))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createJourneys() {

        for (Car c : cars) {
            int rndmRouteIndex = rndm.nextInt(routes.size());
            Journey journey = new Journey(this, messageProducer, c, routes.get(rndmRouteIndex));
            journeys.add(journey);
        }
    }

    public Route getNewRoute() {
        int rndmRouteIndex = rndm.nextInt(routes.size());
        Route route = routes.get(rndmRouteIndex);
        route.setRouteDriven(false);
        route.setAllSubRoutesToFalse();
        return route;
    }

    public void startSimulation() throws InterruptedException {
        for (Thread t : journeys) {
            t.start();
            Thread.sleep((rndm.nextInt(10 - 1 + 1) + 1) * 1000);
        }
    }
}
