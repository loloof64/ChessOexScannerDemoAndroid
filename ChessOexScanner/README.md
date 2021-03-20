# Chess OEX scanner

Use the installed chess OEX engines from your device in your application.

## Compilation

In order to compile the AAR archive, open Gradle toolbar in Android Studio and launch task `ChessOexScanner/build/build`.

### Usage

Import the release AAR into your project. Then you have useful methods in Object `ChessEngineUtils`

* `fun getEnginesNames(context: Context): Array<String>` : get the available engines names from the
engines you get delivered from PlayStore,

* `fun getEnginesCount(context: Context): Int {` : get the available engines count

* `fun copyEngineInInternalFiles(context: Context, index: Int): EngineData {` : copy an engine into
the application internal files (more precisely, into the `engines` subfolder). `EngineData` is 
simply the `data class EngineData(val fileName: String, val packageName: String, val versionCode: Int)`,
so that you have the possibility to store those data in the preferences, and eventually check is a new
version is available, with the following method. Be careful to check that the index is in bounds before !

* `fun getEngineLastVersionCode(context: Context, currentData: EngineData): Int {` :
get the last version code of a given engine, so that you'll be able to copy it again if a new version
is available.

## CREDITS

Using code from https://code.google.com/archive/p/chessenginesupport-androidlib/.