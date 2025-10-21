# StepUp 🚶‍♂️

StepUp es una aplicación Android para el seguimiento habitos y actividades física, desarrollada como proyecto educativo para aprender los fundamentos del desarrollo en Android.

## 📱 Descripción

StepUp permite a los usuarios monitorizar su actividad diaria a través del contador de habitos integrado en sus dispositivos Android. La aplicación ofrece una interfaz intuitiva para visualizar el progreso diario y motivar un estilo de vida más activo.

## ✨ Características

- 📊 Contador de habitos en tiempo real
- 📈 Visualización del progreso diario y estadisticas con progreso
- 💾 Almacenamiento local de datos históricos
- 🎨 Interfaz de usuario moderna y responsiva

## 🛠️ Tecnologías Utilizadas

- **Lenguaje:** Kotlin
- **IDE:** Android Studio
- **Arquitectura:** MVVM (Model-View-ViewModel)
- **UI:** XML Layouts / Jetpack Compose
- **Sensores:** Android Sensor Framework
- **Persistencia:** SharedPreferences / Room Database
- **Componentes:** 
  - LiveData
  - ViewModel
  - Material Design Components

## 📋 Requisitos Previos

- Android Studio Arctic Fox o superior
- SDK de Android 24 (Android 7.0) o superior
- Dispositivo Android o emulador con sensor de pasos
- Conocimientos básicos de Kotlin y Android

## 🚀 Instalación y Configuración

1. **Clonar el repositorio:**
```bash
git clone https://github.com/ihernandezgoo/AndroidDev.git
cd AndroidDev/UD00_Projects/StepUp
```

2. **Abrir el proyecto en Android Studio:**
   - Abre Android Studio
   - Selecciona "Open an existing project"
   - Navega hasta la carpeta StepUp
   - Espera a que Gradle sincronice las dependencias

3. **Configurar el dispositivo:**
   - Conecta un dispositivo físico con depuración USB activada
   - O configura un emulador Android (AVD)

4. **Compilar y ejecutar:**
   - Haz clic en el botón "Run" (▶️) en Android Studio
   - Selecciona tu dispositivo/emulador
   - Espera a que la aplicación se instale e inicie

## 📱 Permisos Requeridos

```xml
<uses-permission android:name="android.permission.ACTIVITY_RECOGNITION" />
<uses-feature android:name="android.hardware.sensor.stepcounter" />
```

**Nota:** Para Android 10 (API 29) y superior, se requiere permiso en tiempo de ejecución para ACTIVITY_RECOGNITION.



### Almacenamiento de Datos
Los datos se persisten localmente para mantener el historial de actividad del usuario incluso cuando la aplicación está cerrada.

### Notificaciones
Sistema opcional de recordatorios para motivar al usuario a cumplir sus objetivos diarios.

## 🧪 Testing

Para ejecutar los tests unitarios:

```bash
./gradlew test
```

Para ejecutar los tests instrumentados:

```bash
./gradlew connectedAndroidTest
```

## 🐛 Problemas Conocidos

- El sensor de pasos puede no estar disponible en todos los emuladores
- La precisión del contador depende del hardware del dispositivo
- En segundo plano, el sistema puede limitar el acceso al sensor para ahorrar batería

## 🔮 Mejoras Futuras

- [ ] Integración con Google Fit
- [ ] Gráficos de progreso semanal/mensual
- [ ] Sistema de logros y badges
- [ ] Compartir logros en redes sociales
- [ ] Modo de desafíos entre usuarios
- [ ] Exportación de datos a CSV

## 👥 Contribuciones

Las contribuciones son bienvenidas. Por favor:

1. Haz fork del repositorio
2. Crea una rama para tu característica (`git checkout -b feature/nueva-caracteristica`)
3. Realiza tus cambios y haz commit (`git commit -am 'Añade nueva característica'`)
4. Sube los cambios a tu rama (`git push origin feature/nueva-caracteristica`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto es de código abierto y está disponible bajo la [Licencia MIT](LICENSE).

## 👨‍💻 Autor

**ihernandezgoo**
- GitHub: [@ihernandezgoo](https://github.com/ihernandezgoo)

⭐ Si este proyecto te ha sido útil, ¡considera darle una estrella en GitHub!