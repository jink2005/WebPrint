# WebPrint
![WebPrint](https://github.com/micwallace/WebPrint/blob/master/src/webprint/img/webprinticon.png)

Print directly to printers using Javascript.

WebPrint is a fork of QZ-print: https://github.com/qzindustries/qz-print

It uses postMessage API and/or ajax calls to communicate with the Java applet.
This differs from QZ-print, that uses Java Web Start and the Java deployment framework for communication.

The Java deployment framework is set to be removed in early 2015, I wanted to find another way to use QZ-print, which provides a seamless printing experience for web applications.

See an example.html/webprint.js for usage or [click here](https://wallaceit.com.au/webprint/example.html) for a live version of the example.

If WebPrint has helped with your project, please donate to support it's future development.

[![Donate to WebPrint](https://www.paypalobjects.com/en_AU/i/btn/btn_donateCC_LG.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=7JBL64AV5XDWG)

# OS Compatability
The main Webprint applet (hosted here) is compatible with Windows, OSX and Linux.

Android support is provided via the Android app:
https://github.com/micwallace/webprint-android
NOTE: Serial and HTML printing is not currently supported on Android.

The javacript library will prompt you to install or open the correct applet if it is not running.

# HTTPS usage
If you site uses HTTPS, requests to HTTP server are almost certainly blocked by default due to mixed-content restrictions.
Thankfully, WebPrint uses an innovative solution to avoid this by using an intermediary browser tab/window and the postMessage API.
This negates the need for end-users to change their browser settings.
For an example on how to use this feature, see example.html in the project. Note that the example prints ESC/P formatted data which may not be compatible with all printers.

# How this came about
Mid last year I developed an android applet that essentially accepts http requests and forwards them to the specified socket or network printer.
https://github.com/micwallace/HttpSocketAdaptor

After a few months I found this solution stopped working due to stronger mixed-content restrictions in most browsers.
Thanks to the postMessage API I was able to securely avoid these restrictions.

After that WebPrint was just a matter of time.

# Thanks
A big thanks to the qz-print guys for allowing this project to happen and EJ Technologies for providing their excellent cross platform installer.

[![Install4j](https://www.ej-technologies.com/images/product_banners/install4j_large.png)](http://www.ej-technologies.com/products/install4j/overview.html)

[Install4j: Multi-platform installer builder](http://www.ej-technologies.com/products/install4j/overview.html)

# Install Dependencies

https://github.com/qzind/tray/wiki/install-dependencies

# Compiling

https://github.com/qzind/tray/wiki/Compiling

## Steps

1. First, install dependencies (jdk, git, ant, nsis/makeself) per Install Dependencies

### JDK
JDK7 is sufficient for building QZ Tray/QZ Print 1.9

### Windows
Install :warning:JDK 7 or higher:

http://www.oracle.com/technetwork/java/javase/downloads/

Download and extract Apache Ant:

https://ant.apache.org/bindownload.cgi

Setup environment variables JAVA_HOME, PATH, etc:

Assume Ant is installed in c:\ant\. The following commands set up the environment:

```
 set ANT_HOME=c:\ant
 set JAVA_HOME=c:\jdk1.7.0_51
 set PATH=%PATH%;%ANT_HOME%\bin
```

http://ant.apache.org/manual/install.html#setup

Install NSIS 3.0+:

http://nsis.sourceforge.net/Download

Install git:

https://git-scm.com/download/win

### Mac
Install command line tools for OS X (installation methods vary)

Install :warning:JDK 7 or higher via: http://www.oracle.com/technetwork/java/javase/downloads/

Install homebrew via:

```
ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
```

Install brew dependencies via:

```
brew install ant makeself nsis
```

Install git:

https://git-scm.com/download/mac

### Ubuntu
Install all dependencies (:warning:jdk7, ant, nsis, makeself) via:

```
sudo apt-get install git openjdk-7-jdk ant nsis makeself
```

### Fedora
Install all dependencies (:warning:jdk, ant, nsis, makeself) via:

```
sudo yum install git java-1.?.0-openjdk-devel java-1.?.0-openjdk-openjfx-devel ant nsis makeself
```

2. Clone the source code per Clone Source Code

3. Pull in the latest source code

```
cd tray
git pull
```
4. Compile

```
ant
```

Optional: Start the software up using this command.

Basic syntax (after closing all instances of QZ Tray)

```
java -Xms512M -jar qz-tray.jar
```

Windows (after closing all instances of QZ Tray)

```
java -Xms512M -jar "%PROGRAMFILES%\QZ Tray\qz-tray.jar"
```

Mac OS X (after closing all instances of QZ Tray)

```
/Applications/QZ\ Tray.app/Contents/MacOS/QZ\ Tray
```

Linux/Other (after closing all instances of QZ Tray)

```
java -Xms512M -jar /opt/qz-tray/qz-tray.jar
```

QZ Tray launches with 512MB of memory. We must specify -Xms512M in the command in order to prevent unexpected behavior and crashing. This unnecessary for Mac unless following the basic syntax (java -jar qz-tray.jar) . More memory can be allocated but should not be necessary.

5. Package

```
ant nsis       # <-- Windows installer
ant pkgbuild   # <-- Apple installer
ant makeself   # <-- Linux installer
```

6. QZ Tray places log files in the following locations:

Windows: %APPDATA%\qz
Mac: ~/Library/Application Support/qz
Linux: ~/.qz


Note: The installer will be placed in ./out/qz-tray-x.x.x.x, (i.e. .exe, .run, .pkg)

