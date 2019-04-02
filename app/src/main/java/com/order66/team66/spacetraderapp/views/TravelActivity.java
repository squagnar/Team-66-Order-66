package com.order66.team66.spacetraderapp.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.order66.team66.spacetraderapp.R;
import com.order66.team66.spacetraderapp.models.CargoHold;
import com.order66.team66.spacetraderapp.models.Planet;
import com.order66.team66.spacetraderapp.models.Player;
import com.order66.team66.spacetraderapp.models.SolarSystem;
import com.order66.team66.spacetraderapp.viewmodels.MarketViewModel;

import java.util.ArrayList;
import java.util.List;

public class TravelActivity extends AppCompatActivity {

    private TextView currentPlanet;
    private TextView shipType;
    private TextView fuelRemaining;
    private TextView cargoRemaining;
    private Spinner planetSpinner;
    private Button travelButton;

    private MarketViewModel viewModel;
    private Planet planet;
    private SolarSystem solarSystem;
    private Player player;
    private CargoHold cargo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);

        viewModel = new MarketViewModel();
        planet = viewModel.getPlanet();
        solarSystem = viewModel.getPlanet().getSolarSystem();
        player = viewModel.getPlayer();
        cargo = player.getCargoHold();

        currentPlanet = findViewById(R.id.current_planet_text);
        shipType = findViewById(R.id.ship_type_text);
        fuelRemaining = findViewById(R.id.fuel_remaining_amount_text);
        cargoRemaining = findViewById(R.id.available_cargo_amount_text);
        planetSpinner = findViewById(R.id.planet_spinner);
        travelButton = findViewById(R.id.travel_button);

        currentPlanet.setText(planet.getName());
        shipType.setText(player.getSpaceship().getName());
        fuelRemaining.setText(String.format("%s", player.getCurrentFuel()));
        cargoRemaining.setText(String.format("%s", (player.getCargoHold().getMaxCapacity() -
                player.getCargoHold().getCurrentCapacity())));

        List<String> planetNames = new ArrayList<>();
        for(Planet planet : solarSystem.getAllPlanets()) {
            planetNames.add(planet.getName());
        }

        ArrayAdapter<String> planetAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, planetNames);
        planetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        planetSpinner.setAdapter(planetAdapter);

        planetSpinner.setSelection(planetNames.indexOf(planet.getName()));
    }

    public void onClick(View view) {
        try {
            viewModel.travel(solarSystem.getPlanet(planetSpinner.getSelectedItemPosition()));
            Intent intent = new Intent(TravelActivity.this, HomeActivity.class);
            startActivity(intent);
        } catch (RuntimeException e) {
            Toast.makeText(this,"You don't have enough fuel for that!", Toast.LENGTH_LONG).show();
        }
    }
}
