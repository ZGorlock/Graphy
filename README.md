
# Graphy


## The 3D Sandbox


----


![Polyhedra Explosion Demo](https://github.com/ZGorlock/Graphy/blob/master/etc/demo/PolyhedraExplosion-demo.jpg?raw=true)


----


### Table of Contents:

- [**Usage**](#usage)

+ [**Controls**](#controls)


----


# Usage

To run this project you will need to have [**Java 13.0.2**](https://jdk.java.net/archive/) or higher, as well as [**Maven 3.8.6**](https://maven.apache.org/download.cgi) or higher.

\
You can run any:

- Scene in the package: `graphy.main.scene`
- Drawing in the package: `graphy.main.drawing`

\
The project can be run from an IDE or by using the scripts in the project directory.
\
Depending on your operating system use either the _.bat_ scripts (Windows) or the _.sh_ scripts (Linux).

- On Windows:
  - The syntax is: **_graphy.bat (scene|drawing) \<Class\>_**
  - Examples:
    - Run the _Polyhedra Explosion_ Scene: `graphy.bat scene PolyhedraExplosion`
    - Run a _Mandelbrot_ Drawing: `graphy.bat drawing Mandelbrot`

+ On Linux:
  + The syntax is: **_graphy.sh (scene|drawing) \<Class\>_**
  + Examples:
    + Run the _Polyhedra Explosion_ Scene: `./graphy.sh scene PolyhedraExplosion`
    + Run a _Mandelbrot_ Drawing: `./graphy.sh drawing Mandelbrot`

\
Alternatively you can run it with Maven directly from the command line:

```shell
mvn compile && mvn exec:java -Dexec.mainClass="graphy.main.scene.PolyhedraExplosion"
```


&nbsp;

----


# Controls

While the controls may vary between different Scenes and Drawings, the default hotkeys are as follows: 

|          **HOTKEY**           |          **EFFECT**          |
|:-----------------------------:|:----------------------------:|
|                               |                              |
|       `Click` + `Drag`        |        Rotate Camera         |
|      `W`, `A`, `S`, `D`       |        Rotate Camera         |
|           `Scroll`            |        Zoom In / Out         |
|           `Q`, `Z`            |        Zoom In / Out         |
| `Up`, `Left`, `Down`, `Right` |         Move Camera          |
|              `E`              |      Toggle Perspective      |
|            &nbsp;             |            &nbsp;            |
|              `1`              |  View + Control: `Camera 1`  |
|              `2`              |  View + Control: `Camera 2`  |
|              `3`              |  View + Control: `Camera 3`  |
|              `4`              |       View: `Camera 1`       |
|              `5`              |       View: `Camera 2`       |
|              `6`              |       View: `Camera 3`       |
|              `7`              |     Control: `Camera 1`      |
|              `8`              |     Control: `Camera 2`      |
|              `9`              |     Control: `Camera 3`      |
|            &nbsp;             |            &nbsp;            |
|              `/`              |     Display Mode: `Edge`     |
|              `*`              |    Display Mode: `Vertex`    |
|              `-`              |     Display Mode: `Face`     |
|            &nbsp;             |            &nbsp;            |
|              `C`              |       Take Screenshot        |
|         `Ctrl` + `C`          | Take Screenshot to Clipboard |
|              `V`              |    Start / Stop Recording    |
|              `B`              |    Open Capture Directory    |
|                               |                              |


&nbsp;

----
