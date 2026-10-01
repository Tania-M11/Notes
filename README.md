# Notes

Android persistence – encryption


<img width="720" height="1600" alt="image" src="https://github.com/user-attachments/assets/dd5cc65d-e059-4f5e-b714-81299cf6588e" />

Aplicación Android **"Notas confidenciales"**, desarrollada para la actividad
**Punto 4: Seguridad en el Almacenamiento y Cifrado**.

La app guarda la misma nota en dos medios de almacenamiento a la vez para poder
comparar, con los archivos a la vista, qué protege realmente el cifrado local
frente al almacenamiento externo.

## El experimento

| | Interno privado | Externo |
|---|---|---|
| Mecanismo | `EncryptedSharedPreferences` | archivo de texto plano |
| Ruta | `/data/data/com.example.notes/shared_prefs/notas_cifradas.xml` | `/sdcard/Android/data/com.example.notes/files/nota_externa.txt` |
| Cifrado | claves AES256-SIV, valores AES256-GCM | ninguno |
| Llave | Android Keystore (no se escribe en el archivo) | — |
| Acceso por ADB | requiere `run-as` | `adb pull` directo |
| Resultado | base64 ilegible | la nota se lee tal cual |

Las dos escrituras salen de la misma función, `NotesRepository.persistir()`, así
que ambos archivos contienen siempre la misma información y la comparación es
válida. **La copia externa nunca se lee**: existe solo para la demostración.

## Cómo se cifra

```kotlin
val llaveMaestra = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
    .build()

EncryptedSharedPreferences.create(
    context,
    "notas_cifradas",
    llaveMaestra,
    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
)
```

Dos esquemas distintos, por una razón concreta:

- **Claves con AES256-SIV**, cifrado determinista: el nombre `"notas"` siempre
  produce el mismo resultado cifrado, porque si no la app no podría volver a
  encontrar la entrada.
- **Valores con AES256-GCM**, no determinista: cada guardado usa un nonce
  aleatorio, y GCM además detecta si el archivo fue manipulado.

La llave maestra se genera dentro del Android Keystore y nunca sale de ahí. Por
eso copiar el `.xml` no sirve: también se copian los juegos de llaves que la
librería guarda en el mismo archivo, pero están cerrados con una llave que se
quedó en el dispositivo.

## Verificación con ADB

```bash
# Interno cifrado: solo se abre haciéndose pasar por la app
adb shell run-as com.example.notes cat shared_prefs/notas_cifradas.xml

# Sin run-as, el sistema frena antes de llegar al cifrado
adb shell cat /data/data/com.example.notes/shared_prefs/notas_cifradas.xml
# -> Permission denied

# Externo: sale directo, sin permisos
adb pull /sdcard/Android/data/com.example.notes/files/nota_externa.txt
```

También se puede hacer desde **Device File Explorer** de Android Studio
(*View → Tool Windows → Device File Explorer*).

Nota: desde Android 11 el explorador de archivos del propio teléfono no entra a
`/Android/data/`. El archivo no está protegido — ADB y Device File Explorer
llegan sin problema —, solo está oculto de la interfaz del dispositivo.

La pantalla **Demostración** de la app muestra el contenido real de los dos
archivos leído del disco, incluido el mismo dato interno visto de dos maneras:
crudo (base64) y descifrado por `EncryptedSharedPreferences`. El veredicto se
calcula buscando el título de una nota real dentro de cada archivo.

## Estructura

```
app/src/main/java/com/example/notes/
├── data/
│   ├── Note.kt               modelo y serialización JSON
│   ├── SecureNoteStore.kt    almacenamiento cifrado (EncryptedSharedPreferences)
│   ├── PlainExternalCopy.kt  copia externa sin cifrar
│   └── NotesRepository.kt    escribe en los dos medios e inspecciona ambos
├── ui/
│   ├── components/           componentes con el estilo pixel art del mockup
│   ├── screens/              las 8 pantallas
│   └── theme/                paleta, tipografía y formas
├── NotesViewModel.kt         estado y navegación con pila propia
└── MainActivity.kt
```

## Stack

- Kotlin 2.2.10 · Jetpack Compose (Material 3) · AGP 9.4.1 · Gradle 9.6.0
- `androidx.security:security-crypto:1.1.0-alpha06`
- minSdk 24 · targetSdk 37

El almacenamiento externo usa `getExternalFilesDir()`, que no requiere ningún
permiso: desde Android 10 el almacenamiento está *scoped* y esa carpeta es la
zona externa propia de la app.

## Nota técnica

`androidx.security:security-crypto` está **deprecado** por Google. Se usa aquí
porque es el mecanismo que la actividad pide de forma explícita. La alternativa
vigente es cifrar manualmente con el Android Keystore más AES-GCM, o usar
SQLCipher para una base de datos.
<img width="720" height="1600" alt="image" src="https://github.com/user-attachments/assets/bd255d07-04dc-4a4b-87af-3a63e4995945" />
<img width="720" height="1600" alt="image" src="https://github.com/user-attachments/assets/8dc6222c-8fec-463f-b838-d26d9100d31d" />
<img width="720" height="1600" alt="image" src="https://github.com/user-attachments/assets/105f4234-1fdc-4ae5-9084-d9005308b4ca" />
<img width="720" height="1600" alt="image" src="https://github.com/user-attachments/assets/79c3d50b-1370-4589-9527-5b69d6aa0d09" />
<img width="720" height="1600" alt="image" src="https://github.com/user-attachments/assets/45c80390-26af-44f5-a70f-df0d91843d04" />
<img width="720" height="1600" alt="image" src="https://github.com/user-attachments/assets/820208fc-bcf8-4a6c-82c6-b3ca45b1ebd9" />
<img width="720" height="1600" alt="image" src="https://github.com/user-attachments/assets/c6cf3ca0-4b22-469e-8d00-a1f9d80cf995" />
<img width="720" height="1600" alt="image" src="https://github.com/user-attachments/assets/1d662cf1-82bd-4fed-ad31-99c928158fcf" />



