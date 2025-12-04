package com.example.roomgps

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.roomgps.data.AppDatabase
import com.example.roomgps.data.entity.Gps
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_main)

            val db = AppDatabase.getDatabase(this)
            val gpsDao = db.gpsDao()

            val container = findViewById<LinearLayout>(R.id.gps_container)
            val fab = findViewById<FloatingActionButton>(R.id.fab_add)

            if (container == null) {
                Log.e("MainActivity", "gps_container not found in layout")
                Toast.makeText(this, "Error: layout no cargado correctamente", Toast.LENGTH_LONG).show()
                return
            }

            // Insertar un GPS de ejemplo si la tabla está vacía (solo para debug/test)
            lifecycleScope.launch {
                try {
                    val current = gpsDao.getAllGps().first()
                    if (current.isEmpty()) {
                        val sample = Gps(latitude = 40.416775, longitude = -3.703790, destinoNombre = "Ejemplo: Madrid")
                        gpsDao.insert(sample)
                        Log.i("MainActivity", "GPS de ejemplo insertado")
                    }
                } catch (e: Exception) {
                    Log.e("MainActivity", "Error comprobando/insertando sample GPS", e)
                }
            }

            // Observa los GPS desde la base de datos y actualiza la UI
            lifecycleScope.launch {
                try {
                    gpsDao.getAllGps().collectLatest { list ->
                        container.removeAllViews()
                        list.forEach { gps ->
                            val btn = Button(this@MainActivity).apply {
                                layoutParams = LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                                ).apply {
                                    setMargins(0, 8, 0, 8)
                                }
                                text = gps.destinoNombre + " (" + gps.latitude + ", " + gps.longitude + ")"
                                setOnClickListener {
                                    try {
                                        openGoogleMaps(gps.latitude, gps.longitude)
                                    } catch (e: Exception) {
                                        Log.e("MainActivity", "Error abriendo maps", e)
                                        Toast.makeText(this@MainActivity, "No se pudo abrir Maps", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                            container.addView(btn)
                        }
                    }
                } catch (e: Exception) {
                    Log.e("MainActivity", "Error observando la base de datos", e)
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Error al leer la base de datos", Toast.LENGTH_LONG).show()
                    }
                }
            }

            if (fab == null) {
                Log.w("MainActivity", "FAB no encontrada (id: fab_add)")
            } else {
                fab.setOnClickListener {
                    // Toast para verificar que el click se detecta
                    Toast.makeText(this, "FAB pulsado", Toast.LENGTH_SHORT).show()
                    showAddGpsDialog(gpsDao)
                }
            }

        } catch (e: Exception) {
            Log.e("MainActivity", "Error en onCreate", e)
            Toast.makeText(this, "Error al iniciar la app: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun showAddGpsDialog(gpsDao: com.example.roomgps.data.dao.GpsDao) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Añadir coordenadas")

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        val inputLat = EditText(this)
        inputLat.hint = "Latitud"
        val inputLon = EditText(this)
        inputLon.hint = "Longitud"
        val inputName = EditText(this)
        inputName.hint = "Nombre destino"

        layout.addView(inputName)
        layout.addView(inputLat)
        layout.addView(inputLon)

        builder.setView(layout)

        builder.setPositiveButton("Guardar") { dialog, _ ->
            try {
                val lat = inputLat.text.toString().toDoubleOrNull()
                val lon = inputLon.text.toString().toDoubleOrNull()
                val name = inputName.text.toString().ifBlank { "Destino" }
                if (lat != null && lon != null) {
                    val gps = Gps(latitude = lat, longitude = lon, destinoNombre = name)
                    lifecycleScope.launch {
                        try {
                            gpsDao.insert(gps)
                        } catch (e: Exception) {
                            Log.e("MainActivity", "Error insertando GPS", e)
                            runOnUiThread {
                                Toast.makeText(this@MainActivity, "Error al guardar coordenadas", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(this, "Latitud o longitud inválida", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error en diálogo de añadir GPS", e)
                Toast.makeText(this, "Error al procesar los datos", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancelar") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    private fun openGoogleMaps(lat: Double, lon: Double) {
        try {
            val uri = Uri.parse("geo:$lat,$lon?q=$lat,$lon")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setPackage("com.google.android.apps.maps")
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                // Si no hay Google Maps, abrir en navegador con Google Maps web
                val webUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=$lat,$lon")
                val webIntent = Intent(Intent.ACTION_VIEW, webUri)
                startActivity(webIntent)
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error al lanzar intent de Maps", e)
            Toast.makeText(this, "No se pudo abrir Maps", Toast.LENGTH_SHORT).show()
        }
    }
}