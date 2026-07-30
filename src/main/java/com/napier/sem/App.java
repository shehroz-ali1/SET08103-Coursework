package com.napier.sem;

import java.sql.*;
import java.util.ArrayList;

public class App {
    private Connection con = null;

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                Thread.sleep(30000);
                con = DriverManager.getConnection("jdbc:mysql://db:3306/world?useSSL=false&allowPublicKeyRetrieval=true", "root", "password");
                System.out.println("Successfully connected");
                break;
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " + i);
                System.out.println(sqle.getMessage());
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    public void disconnect() {
        if (con != null) {
            try {
                con.close();
                System.out.println("Disconnected successfully");
            } catch (Exception e) {
                System.out.println("Error closing connection to database");
            }
        }
    }

    public ArrayList<Country> getAllCountries() {
        try {
            Statement stmt = con.createStatement();
            String strSelect =
                    "SELECT country.Code, country.Name, country.Continent, country.Region, country.Population, city.Name AS Capital "
                    + "FROM country LEFT JOIN city ON country.Capital = city.ID "
                    + "ORDER BY country.Population DESC";
            ResultSet rset = stmt.executeQuery(strSelect);
            ArrayList<Country> countries = new ArrayList<Country>();
            while (rset.next()) {
                Country ctr = new Country();
                ctr.code = rset.getString("Code");
                ctr.name = rset.getString("Name");
                ctr.continent = rset.getString("Continent");
                ctr.region = rset.getString("Region");
                ctr.population = rset.getLong("Population");
                ctr.capital = rset.getString("Capital");
                countries.add(ctr);
            }
            return countries;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void printCountries(ArrayList<Country> countries) {
        if (countries == null || countries.isEmpty()) {
            System.out.println("No countries found.");
            return;
        }
        System.out.println(String.format("%-10s %-50s %-20s %-30s %-20s %-30s", "Code", "Name", "Continent", "Region", "Population", "Capital"));
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------");
        for (Country ctr : countries) {
            System.out.println(String.format("%-10s %-50s %-20s %-30s %-20s %-30s", ctr.code, ctr.name, ctr.continent, ctr.region, ctr.population, ctr.capital));
        }
    }

    public ArrayList<City> getAllCities() {
        try {
            Statement stmt = con.createStatement();
            String strSelect =
                    "SELECT city.Name, country.Name AS Country, city.District, city.Population "
                    + "FROM city JOIN country ON city.CountryCode = country.Code "
                    + "ORDER BY city.Population DESC";
            ResultSet rset = stmt.executeQuery(strSelect);
            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next()) {
                City city = new City();
                city.name = rset.getString("Name");
                city.country = rset.getString("Country");
                city.district = rset.getString("District");
                city.population = rset.getLong("Population");
                cities.add(city);
            }
            return cities;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void printCities(ArrayList<City> cities) {
        if (cities == null || cities.isEmpty()) {
            System.out.println("No cities found.");
            return;
        }
        System.out.println(String.format("%-40s %-40s %-30s %-20s", "Name", "Country", "District", "Population"));
        System.out.println("----------------------------------------------------------------------------------------------------------------------");
        for (City city : cities) {
            System.out.println(String.format("%-40s %-40s %-30s %-20s", city.name, city.country, city.district, city.population));
        }
    }

    public ArrayList<City> getAllCapitalCities() {
        try {
            Statement stmt = con.createStatement();
            String strSelect =
                    "SELECT city.Name, country.Name AS Country, city.Population "
                    + "FROM country JOIN city ON country.Capital = city.ID "
                    + "ORDER BY city.Population DESC";
            ResultSet rset = stmt.executeQuery(strSelect);
            ArrayList<City> capitals = new ArrayList<City>();
            while (rset.next()) {
                City capital = new City();
                capital.name = rset.getString("Name");
                capital.country = rset.getString("Country");
                capital.population = rset.getLong("Population");
                capitals.add(capital);
            }
            return capitals;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void printCapitalCities(ArrayList<City> capitals) {
        if (capitals == null || capitals.isEmpty()) {
            System.out.println("No capital cities found.");
            return;
        }
        System.out.println(String.format("%-40s %-40s %-20s", "Name", "Country", "Population"));
        System.out.println("----------------------------------------------------------------------------------------------------");
        for (City capital : capitals) {
            System.out.println(String.format("%-40s %-40s %-20s", capital.name, capital.country, capital.population));
        }
    }

    /**
     * Requirement 4: The top N populated cities in the world where N is provided by the user.
     */
    public ArrayList<City> getTopNCities(int n) {
        try {
            // We use a PreparedStatement to safely inject the number 'n' into our SQL query
            String strSelect =
                    "SELECT city.Name, country.Name AS Country, city.District, city.Population "
                    + "FROM city JOIN country ON city.CountryCode = country.Code "
                    + "ORDER BY city.Population DESC "
                    + "LIMIT ?";

            PreparedStatement pstmt = con.prepareStatement(strSelect);
            pstmt.setInt(1, n); // This replaces the '?' with our number n
            ResultSet rset = pstmt.executeQuery();

            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next()) {
                City city = new City();
                city.name = rset.getString("Name");
                city.country = rset.getString("Country");
                city.district = rset.getString("District");
                city.population = rset.getLong("Population");
                cities.add(city);
            }
            return cities;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        App a = new App();
        a.connect();

        System.out.println("\n--- Requirement 1: All Countries by Population ---");
        a.printCountries(a.getAllCountries());

        System.out.println("\n--- Requirement 2: All Cities by Population ---");
        a.printCities(a.getAllCities());

        System.out.println("\n--- Requirement 3: All Capital Cities by Population ---");
        a.printCapitalCities(a.getAllCapitalCities());

        System.out.println("\n--- Requirement 4: Top 10 Populated Cities in the World ---");
        // We pass the number 10 as our 'N' value here
        a.printCities(a.getTopNCities(10));

        a.disconnect();
    }
}