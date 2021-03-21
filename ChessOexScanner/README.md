# Chess OEX scanner

Use the installed chess OEX engines from your device in your application.

## Compilation

In order to compile the AAR archive, open Gradle toolbar in Android Studio and launch task `ChessOexScanner/build/build`.

### Usage

Import the release AAR into your project.
Create a new `ChessEngineUtils` providing its constructor the context (`Context`) and the application ID(`String`) .
Then the instance will have the following methods :

* `fun getMyStoreEnginesNames(): Array<String> {` : get the available engines names from the
engines you get delivered from PlayStore,

* `fun installEngineFromMyStore(index: Int)` : copy an engine into
the application internal files (more precisely, into the `engines` subfolder). The index matches the
order got from `getMyStoreEnginesNames()`,

* `fun newVersionAvailableFromMyStoreFor(index: Int) : Boolean {` :
get the last version code of a given engine, so that you'll be able to install it again if a new version
is available,

* `fun listInstalledEngines() : Array<String> {` :
get the list of installed engines (copied into internal files),

* `fun executeInstalledEngine(index: Int, errorCallback: (Error) -> Unit) {` :
execute an installed engine. The index matches the order got from `listInstalledEngines()`,

* `fun sendCommandToRunningEngine(command: String) {` : send a command to the current running engine
(if any),

* `fun readCurrentEnginePendingOutputs(): Array<String> {`: read outputs from current running engine,
or empty array if no running engine,

* `fun stopCurrentRunningEngine() {` : close current engine if any.

## CREDITS

Using code from https://code.google.com/archive/p/chessenginesupport-androidlib/.