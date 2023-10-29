package com.ejemplo.solarease;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import java.time.LocalDate;
public class MainActivity extends AppCompatActivity {
    private EditText latitudeEditText;
    private EditText longitudeEditText;
    private EditText areaEditText;
    private SeekBar inclinationSeekBar;
    private TextView inclinationTextView;
    private Button calculateButton;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        latitudeEditText = findViewById(R.id.latitude_edittext);
        longitudeEditText = findViewById(R.id.longitude_edittext);
        areaEditText = findViewById(R.id.area_edittext);
        inclinationSeekBar = findViewById(R.id.inclination_seekbar);
        inclinationTextView = findViewById(R.id.inclination_textview);
        calculateButton = findViewById(R.id.calculate_button);
        resultTextView = findViewById(R.id.result_textview);
        inclinationSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                inclinationTextView.setText("Inclinación de los paneles: " + progress + "°");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double latitud = Double.parseDouble(latitudeEditText.getText().toString());
                double longitud = Double.parseDouble(longitudeEditText.getText().toString());
                double area = Double.parseDouble(areaEditText.getText().toString());
                int inclinacion = inclinationSeekBar.getProgress();
                double produccionEnergia = calcularProduccionEnergia(latitud, longitud, area,
                        inclinacion);
                resultTextView.setText("Producción de energía: " + produccionEnergia + " kWh");
            }
        });
    }

    private double calcularProduccionEnergia(double latitud, double longitud, double area, int inclinacion) {
        double latitudRad = Math.toRadians(latitud);
        double longitudRad = Math.toRadians(longitud);
        double inclinacionRad = Math.toRadians(inclinacion);
        @SuppressLint({"NewApi", "LocalSuppress"}) int diaDelAnio = LocalDate.now().getDayOfYear();
        double anguloIncidencia = Math.acos(Math.sin(latitudRad) * Math.sin(inclinacionRad) + Math.cos(latitudRad) * Math.cos(inclinacionRad) * Math.cos(longitudRad));
        double constanteSolar = 0.1367;
        double radiacion = constanteSolar * Math.cos(anguloIncidencia) * (1 + 0.033 * Math.cos(Math.toRadians(360 * diaDelAnio / 365.0)));
        double areaPanel = area / 10000.0;
        double eficienciaPanel = 0.16;
        double factorPerdidas = 0.9;
        double produccionEnergia = areaPanel * radiacion * eficienciaPanel * factorPerdidas;
        return produccionEnergia;
    }

    private double calcularProduccionEnergia(double latitud, double longitud, int areaInt, int inclinacion)
    {
        double area = (double) Math.round(areaInt);
        return calcularProduccionEnergia(latitud, longitud, area, inclinacion);
    }
}