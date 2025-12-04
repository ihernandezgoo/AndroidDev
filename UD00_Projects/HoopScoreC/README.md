# HoopScoreC - Aplicación de Marcador de Baloncesto

## Descripción
Aplicación Android de marcador de baloncesto que permite configurar equipos con jugadores antes de iniciar el partido.

## Características

### Pantalla de Configuración (SetupActivity)
- **Pantalla de inicio:** Al iniciar la app, aparece la pantalla de configuración
- **Configuración de equipos:**
  - Ingresar nombres de 2 equipos
  - Añadir jugadores por equipo
  - Mínimo: 5 jugadores por equipo
  - Máximo: 12 jugadores por equipo
- **Validación:** Verifica que se cumplan los requisitos antes de iniciar
- **Botón "INICIAR PARTIDO":** Navega al marcador con los datos configurados

### Pantalla de Marcador (MainActivity)
- **Marcador dividido:** Muestra los dos equipos lado a lado
  - Equipo 1: Color azul
  - Equipo 2: Color rojo
- **Nombres personalizados:** Muestra los nombres de los equipos ingresados
- **Puntuación:**
  - Botones para sumar puntos: +1, +2, +3 para cada equipo
  - Marcador grande y visible
- **Botón Reset:** Reinicia el marcador a 0-0

## Tecnologías Utilizadas
- **Lenguaje:** Kotlin
- **Android SDK:** Min SDK 24, Target SDK 36
- **View Binding:** Habilitado para acceso seguro a las vistas
- **Material Design:** Para UI moderna y atractiva

## Estructura del Proyecto
```
app/
├── src/main/
│   ├── java/com/example/hoopscorec/
│   │   ├── SetupActivity.kt        # Pantalla de configuración inicial
│   │   ├── MainActivity.kt         # Pantalla del marcador
│   │   └── Taldeak_activity.kt     # (Archivo antiguo)
│   ├── res/
│   │   └── layout/
│   │       ├── activity_setup.xml  # Layout de configuración
│   │       └── activity_main.xml   # Layout del marcador
│   └── AndroidManifest.xml
```

## Flujo de la Aplicación
1. **Inicio:** Se lanza SetupActivity
2. **Configuración:** Usuario ingresa:
   - Nombre del Equipo 1 y sus jugadores (5-12)
   - Nombre del Equipo 2 y sus jugadores (5-12)
3. **Validación:** Se verifica que se cumplan los requisitos
4. **Navegación:** Al presionar "INICIAR PARTIDO", se abre MainActivity
5. **Partido:** Usuario puede:
   - Sumar puntos a cada equipo (+1, +2, +3)
   - Reiniciar el marcador

## Compilación
Para compilar el proyecto, ejecuta:
```bash
./gradlew assembleDebug
```

## Instalación
Para instalar en un dispositivo o emulador:
```bash
./gradlew installDebug
```

## Notas
- ViewBinding está habilitado en el proyecto
- Los bindings se generan automáticamente al compilar
- La aplicación mantiene los nombres de los equipos durante todo el partido

