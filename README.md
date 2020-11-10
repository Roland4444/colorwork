# Build and Run:

**build:**

`mvn clean compile assembly:single`

# Run:
# On Windows:
1) **Download OpenCV 4.1.0 https://opencv.org/releases/**

2) **exec mvn install:**

`mvn install:install-file -Dfile="C:\opencv\build\java\opencv-410.jar" -DgroupId=org -DartifactId=opencv -Dversion=4.1.0 -Dpackaging=jar`

3) **In environment variable of windows add in "path":**

`setx path "%path%;C:\opencv\build\x64\vc15\bin"`

4)  **Reboot**

5)  **Run configurations with VM options:**

`-Djava.library.path="C:\opencv\build\java\x64"`

For example:

**run in console-mode**

`java -Djava.library.path="C:\opencv4\build\java\x64" -jar scales-non-ferrous-1.0.0.jar`

**run in window-mode**

`start javaw -Djava.library.path="C:\opencv4\build\java\x64" -jar -Xms1024m -Xmx1024m scales-non-ferrous-1.0.0.jar`

# On Linux:# colorwork
# colorwork
