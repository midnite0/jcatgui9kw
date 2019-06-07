# jCatGUI [9kw edition]
*jCatGUI [9kw edition]* is an application written to collect credits on 9kw.eu service in more comfortable way than in a browser.
The application is written in Java, so you can run it on every operating system: Windows, Linux or OS X.
## Advantages:
* small handle window
* possibility to hide window after solving of a job and show again after receiving of a new one
* possibility to place window on foreground
* audio notification (custom sound possible)
* zoom
* definable interval between captchas
* profiles

## Execute the jar file:
If not already done, first download and install the Java Runtime Environment (it is required to run java applications)
Then follow the steps for your operating system:
### Windows:
double click on jCatGUI_9kw.jar file or run it from the console with: javaw -jar jCatGUI_9kw.jar
### Linux
set the executable bit on jCatGUI_9kw.jar file and then double click on it or run it from a shell with: java -jar jCatGUI_9kw.jar
### MacOS X
assign "Jar Launcher" as the default app for jCatGUI_9kw.jar file or run it from a shell with:  java -jar jCatGUI_9kw.jar

## Custom profile
To start jCatGUI [9kw edition] with a custom profile:
### Windows
run in console or create a shortcut for:
javaw -jar jCatGUI_9kw.jar profilename
### Linux & MacOS X
java -jar jCatGUI_9kw.jar profilename

## Custom sound notification
Place a wave file named "notify.wav" into a same folder with jCatGUI_9kw.jar to play your own sound on receiving of a new captcha.

## Proxy server setting: (http.proxyUser and http.proxyPassword settings are optional!)
### Windows
run in console or create a shortcut for:
javaw -Dhttp.proxyHost=host -Dhttp.proxyPort=port -Dhttp.proxyUser=username -Dhttp.proxyPassword=password -jar jCatGUI_9kw.jar
### Linux & MacOS X
java -Dhttp.proxyHost=host -Dhttp.proxyPort=port -Dhttp.proxyUser=username -Dhttp.proxyPassword=password -jar jCatGUI_9kw.jar
#### Example
java -Dhttp.proxyHost=10.10.10.200 -Dhttp.proxyPort=3128 -jar jCatGUI_9kw.jar
